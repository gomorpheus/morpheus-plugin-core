package com.morpheusdata.core.util.image;

import org.codehaus.groovy.runtime.DefaultGroovyMethods;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * Created by davidestes on 4/11/16.
 */
public class QcowHeader {
	public QcowHeader() {
		this.cachedBytes = new ByteArrayOutputStream();
	}

	public Long getClusterSize() {
		return DefaultGroovyMethods.asType(1 << clusterBits, Long.class);
	}

	public long getL2Location(int l1Index) {
		long l1Entry = l1Table[l1Index];
		return l1Entry & 0x00FFFFFFFFFFFF00L;
	}

	public String getMagic() {
		return magic;
	}

	public void setMagic(String magic) {
		this.magic = magic;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Long getBackingFileOffset() {
		return backingFileOffset;
	}

	public void setBackingFileOffset(Long backingFileOffset) {
		this.backingFileOffset = backingFileOffset;
	}

	public Long getBackingFileSize() {
		return backingFileSize;
	}

	public void setBackingFileSize(Long backingFileSize) {
		this.backingFileSize = backingFileSize;
	}

	public Long getClusterBits() {
		return clusterBits;
	}

	public void setClusterBits(Long clusterBits) {
		this.clusterBits = clusterBits;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Long getCryptMethod() {
		return cryptMethod;
	}

	public void setCryptMethod(Long cryptMethod) {
		this.cryptMethod = cryptMethod;
	}

	public Long getL1Size() {
		return l1Size;
	}

	public void setL1Size(Long l1Size) {
		this.l1Size = l1Size;
	}

	public Long getL1TableOffset() {
		return l1TableOffset;
	}

	public void setL1TableOffset(Long l1TableOffset) {
		this.l1TableOffset = l1TableOffset;
	}

	public Long getRefcountTableOffset() {
		return refcountTableOffset;
	}

	public void setRefcountTableOffset(Long refcountTableOffset) {
		this.refcountTableOffset = refcountTableOffset;
	}

	public Long getRefcountTableClusters() {
		return refcountTableClusters;
	}

	public void setRefcountTableClusters(Long refcountTableClusters) {
		this.refcountTableClusters = refcountTableClusters;
	}

	public Long getNbSnapshots() {
		return nbSnapshots;
	}

	public void setNbSnapshots(Long nbSnapshots) {
		this.nbSnapshots = nbSnapshots;
	}

	public Long getSnapshotsOffset() {
		return snapshotsOffset;
	}

	public void setSnapshotsOffset(Long snapshotsOffset) {
		this.snapshotsOffset = snapshotsOffset;
	}

	public long[] getL1Table() {
		return l1Table;
	}

	public void setL1Table(long[] l1Table) {
		this.l1Table = l1Table;
	}

	public LinkedHashMap<Long, Long[]> getL2Table() {
		return l2Table;
	}

	public void setL2Table(LinkedHashMap<Long, Long[]> l2Table) {
		this.l2Table = l2Table;
	}

	public byte[] getBytes() {
		return cachedBytes.toByteArray();
	}

	protected void cacheByte(int b) throws IOException {
		this.cachedBytes.write(b);
	}

	protected void cacheBytes(byte[] bytes) throws IOException {
		this.cachedBytes.write(bytes);
	}

	protected void cacheBytes(byte[] b, int off, int len) throws IOException {
		this.cachedBytes.write(b, off, len);
	}

	protected void closeCache() throws IOException {
		if (cachedBytes != null)
			cachedBytes.close();
	}

	private String magic;
	private Long version;
	private Long backingFileOffset;
	private Long backingFileSize;
	private Long clusterBits;
	private Long size;
	private Long cryptMethod;
	private Long l1Size;
	private Long l1TableOffset;
	private Long refcountTableOffset;
	private Long refcountTableClusters;
	private Long nbSnapshots;
	private Long snapshotsOffset;
	private long[] l1Table;
	private LinkedHashMap<Long, Long[]> l2Table;
	private ByteArrayOutputStream cachedBytes;
}
