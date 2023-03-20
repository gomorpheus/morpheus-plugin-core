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
