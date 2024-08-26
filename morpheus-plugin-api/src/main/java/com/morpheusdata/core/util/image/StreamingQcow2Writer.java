/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.core.util.image;

import org.apache.commons.codec.Charsets;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class StreamingQcow2Writer {

	private static final long CLUSTER_SIZE = 65536;
	private static final long REPORT_INTERVAL_BYTES = 500_000_000; // 500 MB

	private long inputSize;
	private int l1Clusters;
	private long l1Offset;
	private int refcountTableClusters;
	private long firstDataCluster;
	private List<Long> dataClusters;

	public StreamingQcow2Writer(long inputSize, Iterator<Range<Long>> ranges) {
		this.inputSize = inputSize;
		this.dataClusters = new ArrayList<>();

		Long lastCluster = null;
		while (ranges.hasNext()) {
			Range<Long> range = ranges.next();

			long fromCluster = range.getStart() / CLUSTER_SIZE;
			long toCluster = divideAndRoundUp(range.getEnd(), CLUSTER_SIZE);

			if (lastCluster != null) {
				if (fromCluster < lastCluster) {
					throw new IllegalArgumentException("Data clusters are not sorted");
				} else if (fromCluster == lastCluster) {
					fromCluster++;
				}
			}
			lastCluster = toCluster - 1;

			for (long cluster = fromCluster; cluster < toCluster; cluster++) {
				dataClusters.add(cluster);
			}
		}

		long guestClusters = divideAndRoundUp(inputSize, CLUSTER_SIZE);
		long l2Tables = divideAndRoundUp(guestClusters * 8, CLUSTER_SIZE);

		this.l1Clusters = (int) divideAndRoundUp(l2Tables * 8, CLUSTER_SIZE);

		long refcountBlocks = 1;
		this.refcountTableClusters = 1;

		while (true) {
			long totalClusters = 1 + refcountTableClusters + refcountBlocks + l1Clusters + l2Tables + dataClusters.size();
			long newRefcountBlocks = divideAndRoundUp(totalClusters * 2, CLUSTER_SIZE);
			if (newRefcountBlocks == refcountBlocks) {
				break;
			}
			refcountBlocks = newRefcountBlocks;
			this.refcountTableClusters = (int) divideAndRoundUp(refcountBlocks * 8, CLUSTER_SIZE);
		}

		this.l1Offset = CLUSTER_SIZE * (1 + refcountTableClusters + refcountBlocks);
		this.firstDataCluster = 1 + refcountTableClusters + refcountBlocks + l1Clusters + l2Tables;
	}

	private static long divideAndRoundUp(long a, long b) {
		return (a + b - 1) / b;
	}

	public long fileSize() {
		return CLUSTER_SIZE * totalClusters();
	}

	private long totalClusters() {
		return firstDataCluster + dataClusters.size();
	}

	public long totalGuestClusters() {
		return divideAndRoundUp(inputSize, CLUSTER_SIZE);
	}

	public void writeHeader(OutputStream outputStream) throws IOException {
		// Magic
		//String magic = "QFI" + (char) 0xFB;
		outputStream.write('Q');
		outputStream.write('F');
		outputStream.write('I');
		outputStream.write(0xFB);

		//outputStream.write(magic.getBytes(StandardCharsets.US_ASCII));

		// Version
		writeInt(outputStream, 2);

		// Backing file name offset (0 = no backing file)
		writeLong(outputStream, 0);

		// Backing file name length
		writeInt(outputStream, 0);

		// Number of bits per cluster address, 1<<bits is the cluster size
		assert CLUSTER_SIZE == 1 << 16;
		writeInt(outputStream, 16);

		// Virtual disk size in bytes
		writeLong(outputStream, inputSize);

		// Encryption method (none)
		writeInt(outputStream, 0);

		// L1 table size (number of entries)
		long l2EntriesPerCluster = CLUSTER_SIZE / 8;
		long l1Entries = totalGuestClusters() / l2EntriesPerCluster;
		writeInt(outputStream, (int) l1Entries);

		// L1 table offset
		writeLong(outputStream, l1Offset);

		// Refcount table offset
		writeLong(outputStream, CLUSTER_SIZE);

		// Refcount table length in clusters
		writeInt(outputStream, refcountTableClusters);

		// Number of snapshots in the image
		writeInt(outputStream, 0);

		// Offset of the snapshot table (must be aligned to clusters)
		writeLong(outputStream, 0);

		outputStream.write(new byte[(int) CLUSTER_SIZE - 72]);

		writeRefcountTable(outputStream);
		writeMappingTable(outputStream);
	}

	private void writeRefcountTable(OutputStream outputStream) throws IOException {
		long refcountBlocks = divideAndRoundUp(totalClusters() * 2, CLUSTER_SIZE);

		// Table
		for (long block = 0; block < refcountBlocks; block++) {
			writeLong(outputStream, CLUSTER_SIZE * (1 + refcountTableClusters + block));
		}

		long refcountEntriesPerCluster = CLUSTER_SIZE / 8;
		long lastClusterEntries = refcountBlocks % refcountEntriesPerCluster;
		if (lastClusterEntries > 0) {
			for (long i = lastClusterEntries; i < refcountEntriesPerCluster; i++) {
				writeLong(outputStream, 0);
			}
		}

		// Blocks
		for (long i = 0; i < totalClusters(); i++) {
			writeShort(outputStream, (short) 1);
		}

		long blockEntriesPerCluster = CLUSTER_SIZE / 2;
		long lastBlockClusterEntries = totalClusters() % blockEntriesPerCluster;
		if (lastBlockClusterEntries > 0) {
			for (long i = lastBlockClusterEntries; i < blockEntriesPerCluster; i++) {
				writeShort(outputStream, (short) 0);
			}
		}
	}

	private void writeMappingTable(OutputStream outputStream) throws IOException {
		Map<Long, Long> mapping = new HashMap<>();
		for (int i = 0; i < dataClusters.size(); i++) {
			mapping.put(dataClusters.get(i), (long) i + firstDataCluster);
		}

		// L1 table
		long l1EntriesPerCluster = CLUSTER_SIZE / 8;
		long l1Entries = divideAndRoundUp(totalGuestClusters(), l1EntriesPerCluster);
		for (long entry = 0; entry < l1Entries; entry++) {
			long offset = l1Offset + l1Clusters * CLUSTER_SIZE + entry * CLUSTER_SIZE;
			long l1Entry = offset | (1L << 63);
			writeLong(outputStream, l1Entry);
		}

		long lastClusterEntries = l1Entries % l1EntriesPerCluster;
		if (lastClusterEntries > 0) {
			for (long i = lastClusterEntries; i < l1EntriesPerCluster; i++) {
				writeLong(outputStream, 0);
			}
		}

		// L2 table
		for (long guestCluster = 0; guestCluster < totalGuestClusters(); guestCluster++) {
			long l2Entry = mapping.getOrDefault(guestCluster, 0L);
			long offset = l2Entry*CLUSTER_SIZE;
			if (offset != 0) {
				offset |= (0L << 62) | (1L << 63);
			}

			writeLong(outputStream, offset);
		}

		long l2EntriesPerCluster = CLUSTER_SIZE / 8;
		lastClusterEntries = totalGuestClusters() % l2EntriesPerCluster;
		if (lastClusterEntries > 0) {
			for (long i = lastClusterEntries; i < l2EntriesPerCluster; i++) {
				writeLong(outputStream, 0);
			}
		}
	}

	public void copyData(RandomAccessFile reader, OutputStream writer) throws IOException {
		long written = firstDataCluster * CLUSTER_SIZE;
		byte[] buffer = new byte[(int) CLUSTER_SIZE];
		for (Long cluster : dataClusters) {
			reader.seek(cluster * CLUSTER_SIZE);
			int bytesRead = reader.read(buffer);
			writer.write(buffer, 0, bytesRead);

			if ((written + CLUSTER_SIZE) / REPORT_INTERVAL_BYTES != written / REPORT_INTERVAL_BYTES) {
				System.err.printf("%d/%d bytes written%n", written + CLUSTER_SIZE, fileSize());
			}
			written += CLUSTER_SIZE;
		}
	}

	private void writeInt(OutputStream outputStream, int value) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
		buffer.putInt(value);
		outputStream.write(buffer.array());
	}

	private void writeLong(OutputStream outputStream, long value) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN);
		buffer.putLong(value);
		outputStream.write(buffer.array());
	}

	private void writeShort(OutputStream outputStream, short value) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN);
		buffer.putShort(value);
		outputStream.write(buffer.array());
	}

	public static class Range<T extends Comparable<? super T>> {
		private final T start;
		private final T end;

		public Range(T start, T end) {
			this.start = start;
			this.end = end;
		}

		public T getStart() {
			return start;
		}

		public T getEnd() {
			return end;
		}
	}
}
