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

package com.morpheusdata.core.backup;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.Plugin;
import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.model.Icon;
import com.morpheusdata.model.OptionType;
import com.morpheusdata.response.ServiceResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class MorpheusBackupProvider extends AbstractBackupProvider {

	public MorpheusBackupProvider(Plugin plugin, MorpheusContext morpheusContext) {
		super(plugin, morpheusContext);
	}

	@Override
	public String getCode() {
		return "morpheus";
	}

	@Override
	public String getName() {
		return "Morpheus";
	}



	@Override
	public BackupJobProvider getBackupJobProvider() {
		return null;
	}

	@Override
	public Icon getIcon() {
		Icon icon = new Icon();
		icon.setPath("standard.svg");
		icon.setDarkPath("standard-dark.svg");

		return icon;
	}

	@Override
	public Collection<OptionType> getOptionTypes() {
		// stored internally by the Morpheus Appliance
		Collection<OptionType> optionTypes = new ArrayList();
		return optionTypes;
	}

	@Override
	public Collection<OptionType> getBackupJobOptionTypes() {
		// stored internally by the Morpheus Appliance
		Collection<OptionType> optionTypes = new ArrayList();
		return optionTypes;
	}

	@Override
	public Collection<OptionType> getBackupOptionTypes() {
		// stored internally by the Morpheus Appliance
		Collection<OptionType> optionTypes = new ArrayList();
		return optionTypes;
	}

	@Override
	public Collection<OptionType> getReplicationGroupOptionTypes() {
		Collection<OptionType> optionTypes = new ArrayList();
		return optionTypes;
	}

	@Override
	public Collection<OptionType> getReplicationOptionTypes() {
		Collection<OptionType> optionTypes = new ArrayList();
		return optionTypes;
	}

	@Override
	public Collection<OptionType> getInstanceReplicationGroupOptionTypes() {
		Collection<OptionType> optionTypes = new ArrayList();
		return optionTypes;
	}

	@Override
	public ServiceResponse deleteBackupProvider(BackupProvider backupProviderModel, Map opts) {
		// handled internally by the Morpheus Appliance
		return null;
	}

	@Override
	public ServiceResponse refresh(BackupProvider backupProvider) {
		// handled internally by the Morpheus Appliance
		return null;
	}
}
