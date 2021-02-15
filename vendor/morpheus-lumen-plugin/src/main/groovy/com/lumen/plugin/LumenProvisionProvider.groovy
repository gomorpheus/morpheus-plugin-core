package com.lumen.plugin

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisionProvider
import com.morpheusdata.model.Account
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.ComputeServerInterface
import com.morpheusdata.model.ComputeZone
import com.morpheusdata.model.Container
import com.morpheusdata.model.Instance
import com.morpheusdata.model.Network
import com.morpheusdata.model.NetworkSubnet
import com.morpheusdata.model.NetworkType
import com.morpheusdata.model.ProvisionType
import com.morpheusdata.response.ServiceResponse
import groovy.transform.AutoImplement
import groovy.util.logging.Log4j
import groovy.util.logging.Slf4j
import io.reactivex.Single
import sun.net.NetworkServer

@AutoImplement
@Slf4j
class LumenProvisionProvider implements ProvisionProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	LumenProvisionProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
	}

	Single<ServiceResponse> provisionComplete(Account account, Container container) {
		return Single.just(ServiceResponse.success())
	}

	Single<ServiceResponse> deProvisionStarted(Account account, Container container) {
		return Single.just(ServiceResponse.success())
	}

	@Override
	String getProviderCode() {
		return 'lumen-provision'
	}

	@Override
	String getProviderName() {
		return 'Lumen'
	}
	// Below raw from Morpheus:

	def defaultCustomer = [accountNumber:'morpheus', accountName:'morpheus']

	Single<ServiceResponse> validateContainer(Map opts = [:]) {
		log.debug("validateContainer: ${opts.config}")
		def rtn = [success:true, errors:[]]
		Single.just(ServiceResponse.success(rtn))
	}
}
