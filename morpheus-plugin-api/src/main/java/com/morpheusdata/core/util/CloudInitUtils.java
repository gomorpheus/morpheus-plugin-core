package com.morpheusdata.core.util;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.TaskResult;
import io.reactivex.rxjava3.core.Maybe;

public class CloudInitUtils {

	public static TaskResult disableCloudInitCache(MorpheusContext morpheusContext, ComputeServer server) {
		if(server.getSourceImage() != null && server.getSourceImage().isCloudInit() && server.getServerOs() != null && server.getServerOs().getPlatform().toString().equals("windows")) {
			return morpheusContext.executeCommandOnServer(server, "sudo rm -f /etc/cloud/cloud.cfg.d/99-manual-cache.cfg; sudo mv /etc/machine-id /var/tmp/machine-id-old ; sync; sync;", false, server.getSshUsername(), server.getSshPassword(), null, null, null, null, true, true).blockingGet();
		}
		return null;
	}

	public  static TaskResult restoreCloudInitCache(MorpheusContext morpheusContext, ComputeServer server) {
		if(server.getSourceImage() != null && server.getSourceImage().isCloudInit() && server.getServerOs() != null && server.getServerOs().getPlatform().toString().equals("windows")) {
			return morpheusContext.executeCommandOnServer(server, "sudo bash -c \"echo 'manual_cache_clean: True' >> /etc/cloud/cloud.cfg.d/99-manual-cache.cfg\"; sudo mv /var/lib/cloud/instance-back /var/lib/cloud/instance; sudo cat /var/tmp/machine-id-old > /etc/machine-id ; sudo rm /var/tmp/machine-id-old; sync; sync;", false, server.getSshUsername(), server.getSshPassword(), null, null, null, null, true, true).blockingGet();
		}
		return null;
	}
}
