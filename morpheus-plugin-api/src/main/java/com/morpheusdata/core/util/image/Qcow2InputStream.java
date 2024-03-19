package com.morpheusdata.core.util.image;

import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * Created by davidestes on 4/11/16.
 */
public class Qcow2InputStream extends InputStream {

	public Qcow2InputStream(InputStream sourceStream, boolean cacheHeaderBytes) throws IllegalArgumentException, IllegalStateException, IOException {
		if(!DefaultGroovyMethods.asBoolean(sourceStream)) {
			throw new IllegalArgumentException("A source stream must be passed to the XvaInputStream constructor.");
		}
		this.sourceStream = sourceStream;
		this.cacheHeaderBytes = cacheHeaderBytes;
		initializeQcowHeader();
	}

	public Qcow2InputStream(InputStream sourceStream) throws IllegalArgumentException, IllegalStateException, IOException {
		this(sourceStream, false);
	}

	private void initializeQcowHeader() throws IllegalStateException, IOException {
		this.qcowHeader = new QcowHeader();
		//Note: we dont want to use any Helper classes for parsing the stream because most of them close the stream
		int b;
		b = read_();
		qcowHeader.setMagic(new String(new char[]{(char)b}));
		b = read_();
		qcowHeader.setMagic(qcowHeader.getMagic() + (char)b);
		b = read_();
		qcowHeader.setMagic(qcowHeader.getMagic() + (char)b);
		b = read_();// 0xfb ignore please
		position = 4L;
		//b = sourceStream.read()
		qcowHeader.setVersion(read32());
		qcowHeader.setBackingFileOffset(read64());
		qcowHeader.setBackingFileSize(read32());
		qcowHeader.setClusterBits(read32());
		qcowHeader.setSize(read64());
		qcowHeader.setCryptMethod(read32());
		qcowHeader.setL1Size(read32());
		qcowHeader.setL1TableOffset(read64());
		qcowHeader.setRefcountTableOffset(read64());
		qcowHeader.setRefcountTableClusters(read32());
		qcowHeader.setNbSnapshots(read32());
		qcowHeader.setSnapshotsOffset(read64());
		clusterCache = new byte[qcowHeader.getClusterSize().intValue()];
		initializeL1Table();
	}

	private void initializeL1Table() throws IOException {
		qcowHeader.setL1Table(new long[qcowHeader.getL1Size().intValue()]);
		seek(qcowHeader.getL1TableOffset());//Seek to L1 Table Position
		read64(qcowHeader.getL1Table());//Read in the L1 Table
		qcowHeader.setL2Table(new LinkedHashMap<Long, Long[]>());
		clusterCache = new byte[qcowHeader.getClusterSize().intValue()];
	}

	private Long seek(long destinationPos) throws IOException {
		if(destinationPos < position) {
			throw new IOException("Invalid attempt to seek backwards in a stream. Can only seek forward.");
		}
		long seekDistance = destinationPos - position;
		while(seekDistance > 0) {
			byte[] throwAway = new byte[1024];
			int readLen = DefaultGroovyMethods.asType(Math.min(1024, seekDistance), Integer.class);
			int c = read_(throwAway, 0, readLen);
			if(c < 0) {
				throw new IOException("Premature end of file reached while seeking to position");
			}
			seekDistance -= c;
		}
		return position = destinationPos;
	}

	private long read32() throws IOException {
		byte[] buff = new byte[4];
		int c = read_(buff);
		if(c < 0) {
			return -1;
		}
		position += 4;
		return (((long) buff[3]) & 0xFFL) + ((((long) buff[2]) & 0xFFL) << 8) + ((((long) buff[1]) & 0xFFL) << 16) + ((((long) buff[0]) & 0xFFL) << 24);
	}

	private long read64() throws IOException {
		long[] byteElement = new long[1];
		read64(byteElement);
		return ((byteElement[0]));
	}

	private int read64(long[] longArray) throws IOException {
		int elementsRead = 0;
		byte[] buff = new byte[longArray.length * 8];

		int c = read_(buff);
		if(c < 0) {
			return -1;
		}

		position += c;
		if(c % 8 > 0) {
			throw new IOException("L1 Table from QCOW2 File is non readable or corrupt.");
		}
		while(c > 0) {
			longArray[elementsRead] = (((long) buff[7 + elementsRead * 8]) & 0xFFL) + ((((long) buff[6 + elementsRead * 8]) & 0xFFL) << 8) + ((((long) buff[5 + elementsRead * 8]) & 0xFFL) << 16) + ((((long) buff[4 + elementsRead * 8]) & 0xFFL) << 24) + ((((long) buff[3 + elementsRead * 8]) & 0xFFL) << 32) + ((((long) buff[2 + elementsRead * 8]) & 0xFFL) << 40) + ((((long) buff[1 + elementsRead * 8]) & 0xFFL) << 48) + ((((long) buff[elementsRead * 8]) & 0xFFL) << 56);
			c -= 8;
			elementsRead++;
		}


		return elementsRead;
	}

	@Override
	public int read() throws IOException {
		byte[] b = new byte[1];
		int c = read(b, 0, 1);
		if(c <= 0) {
			return -1;
		}


		return DefaultGroovyMethods.asType(b[0], Integer.class);
	}

	@Override
	public int available() throws IOException {
		return sourceStream.available();
	}

	@Override
	public int read(byte[] buffer) throws IOException {
		return sourceStream.read(buffer);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int bytesRead = 0;
		int l2_entries = DefaultGroovyMethods.asType(qcowHeader.getClusterSize() / 8, Integer.class);
		if(offset >= qcowHeader.getSize()) {
			return -1;
		}

		while(bytesRead < len) {
			//println "Starting read loop"
			long clusterOffset = DefaultGroovyMethods.asType(offset % qcowHeader.getClusterSize(), Long.class);
			int l2Index = DefaultGroovyMethods.asType((DefaultGroovyMethods.asType(offset / qcowHeader.getClusterSize(), Long.class)) % l2_entries, Integer.class);
			int l1Index = DefaultGroovyMethods.asType((DefaultGroovyMethods.asType(offset / qcowHeader.getClusterSize(), Long.class)) / l2_entries, Integer.class);
			//println "L1 Index of ${l1Index} - ${qcowHeader.l1Size} - ${offset} of ${qcowHeader.size}"
			long l2Location = qcowHeader.getL2Location(l1Index);
			if(l2Location != 0) {
				//	println "l2 location found at ${l2Location}"
				Long[] l2Table = loadL2Cluster(l2Location);
				boolean compressed = (l2Table[l2Index] & 0x4000000000000000L) > 0;
//				println "Checking for compress ${Long.toHexString(l2Table[l2Index])} - ${Long.toHexString(l2Table[l2Index] & 0x4000000000000000L)}"
				long desiredPosition = 0;
				long compressedSize = 0;

				if(compressed) {
					//	println "Detected Compressed Cluster"
					int x = 62 - (qcowHeader.getClusterBits().intValue() - 8);
					long bitMask = 0;
					for(int counter = 0; counter <= x; counter++) {
						bitMask <<= 1;
						bitMask |= 1;
					}


					desiredPosition = DefaultGroovyMethods.asType(l2Table[l2Index] & bitMask, Long.class);//Mask off reserved bits
					compressedSize = DefaultGroovyMethods.asType((DefaultGroovyMethods.asType(l2Table[l2Index] & ~bitMask & 0x3FFFFFFFFFFFFFFFL, Long.class)) >> (DefaultGroovyMethods.asType(x + 2, Long.class)),
					Long.class);//in 512 byte sectors

				} else {
					desiredPosition = DefaultGroovyMethods.asType(l2Table[l2Index] & 0x00FFFFFFFFFFFF00L, Long.class);//Mask off reserved bits

				}

				if(currentCluster != desiredPosition) {
					if(desiredPosition != 0) {
						seek(desiredPosition);
						clusterCache = new byte[qcowHeader.getClusterSize().intValue()];
						if(compressed) {
							byte[] zipCache = new byte[(int)compressedSize * 512];
							position += sourceStream.read(zipCache);
							Inflater decompressor = new Inflater();
							decompressor.setInput(zipCache, 0, (int)compressedSize * 512);
							try {
								decompressor.inflate(clusterCache);
							} catch(DataFormatException df) {
								throw new IOException("Unable to decompress compressed cluster in QCOW2",df);
							}

							decompressor.end();
						} else {
							position += sourceStream.read(clusterCache);
						}

					} else {
						//	println "Blank Cluster initialize"
						clusterCache = new byte[qcowHeader.getClusterSize().intValue()];
					}

					currentCluster = desiredPosition;
				} else {
					//println "Already at initial Cluster"
				}

			} else {
				currentCluster = 0L;
				clusterCache = new byte[qcowHeader.getClusterSize().intValue()];
			}

			InputStream clusterIs = new ByteArrayInputStream(clusterCache, DefaultGroovyMethods.asType(clusterOffset, Integer.class), DefaultGroovyMethods.asType(qcowHeader.getClusterSize() - clusterOffset, Integer.class));
			int c = ((ByteArrayInputStream) clusterIs).read(b, off + bytesRead, len - bytesRead);

			if(c > 0) {
				bytesRead += c;
				offset += c;
			} else {
				break;
			}

		}


		return bytesRead;
	}

	public void consume() throws IOException {
		byte[] buff = new byte[1024];
		while(read(buff, 0, 1024) != -1) {

		}

	}

	public void close() throws IOException {
		super.close();
		sourceStream.close();
		if (cacheHeaderBytes)
			qcowHeader.closeCache();
	}

	private Long[] loadL2Cluster(Long location) throws IOException {
		Long[] l2Table = qcowHeader.getL2Table().get(location);
		if(l2Table == null) {
			seek(location);
			int l2_entries = DefaultGroovyMethods.asType(qcowHeader.getClusterSize() / 8, Integer.class);
			long[] l2Entries = new long[l2_entries];
			Long[] L2Entries = new Long[l2_entries];

			read64(l2Entries);
			for(int counter=0;counter<l2_entries;counter++) {
				L2Entries[counter] = l2Entries[counter];
			}

			qcowHeader.getL2Table().put(location, L2Entries);
			return L2Entries;
		} else {
			return l2Table;
		}
	}

	public InputStream getSourceStream() {
		return sourceStream;
	}

	public void setSourceStream(InputStream sourceStream) {
		this.sourceStream = sourceStream;
	}

	public QcowHeader getQcowHeader() {
		return qcowHeader;
	}

	public void setQcowHeader(QcowHeader qcowHeader) {
		this.qcowHeader = qcowHeader;
	}

	public Long getPosition() {
		return position;
	}

	public void setPosition(Long position) {
		this.position = position;
	}

	public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public Long getCurrentCluster() {
		return currentCluster;
	}

	public void setCurrentCluster(Long currentCluster) {
		this.currentCluster = currentCluster;
	}

	public byte[] getClusterCache() {
		return clusterCache;
	}

	public void setClusterCache(byte[] clusterCache) {
		this.clusterCache = clusterCache;
	}

	/**
	 * One of several internal read methods that will perform reads on the sourceStream of this instance
	 * and cache the bytes of the QcowHeader to be used later, only if this stream was instantiated
	 * with the cache instruction
	 * @return
	 * @throws IOException
	 */
	private int read_() throws IOException {
		int b = sourceStream.read();
		if (cacheHeaderBytes)
			qcowHeader.cacheByte(b);
		return b;
	}

	private int read_(byte[] b, int off, int len) throws IOException {
		int bytesRead = sourceStream.read(b, off, len);
		if (cacheHeaderBytes)
			qcowHeader.cacheBytes(b, off, bytesRead);
		return bytesRead;
	}

	private int read_(byte[] b) throws IOException {
		int bytesRead = sourceStream.read(b);
		if (cacheHeaderBytes)
			qcowHeader.cacheBytes(b, 0, bytesRead);
		return bytesRead;
	}

	private InputStream sourceStream;
	private QcowHeader qcowHeader;
	private Long position = 0L;
	private Long offset = 0L;
	private Long currentCluster = 0L;
	private byte[] clusterCache;
	private boolean cacheHeaderBytes = false;

}
