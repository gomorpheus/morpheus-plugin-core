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

package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

/**
 * Represents snapshot files available on StorageVolumes, etc
 */
public class SnapshotFile extends MorpheusModel {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Snapshot snapshot;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected StorageVolume volume;
	protected String name;
	protected String type;
	protected String externalId;
	protected String path;
	protected String exportPath;
	protected Integer diskIndex;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public StorageVolume getVolume() {
		return volume;
	}

	public void setVolume(StorageVolume volume) {
		this.volume = volume;
	}

	public Snapshot getSnapshot() {
		return snapshot;
	}

	public void setSnapshot(Snapshot snapshot) {
		this.snapshot = snapshot;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		markDirty("type", type);
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
		markDirty("path", path);
	}

	public String getExportPath() {
		return exportPath;
	}

	public void setExportPath(String exportPath) {
		this.exportPath = exportPath;
		markDirty("exportPath", exportPath);
	}

	public Integer getDiskIndex() {
		return diskIndex;
	}

	public void setDiskIndex(Integer diskIndex) {
		this.diskIndex = diskIndex;
		markDirty("diskIndex", diskIndex);
	}
}
