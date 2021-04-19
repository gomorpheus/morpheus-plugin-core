package com.morpheusdata.maas.plugin

import com.morpheusdata.model.Account
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.ImageType
import com.morpheusdata.model.VirtualImage
import spock.lang.Specification

class MaasComputeUtilitySpec extends Specification {

	void "bootImageToVirtualImage"() {
		given:
		Map apiResponse = [name: 'ubuntu/16.04', architecture: 'amd64']
		Cloud cloud = new Cloud(id: 1)

		when:
		VirtualImage image = MaasComputeUtility.bootImageToVirtualImage(cloud, apiResponse)

		then:
		image.code == 'maas.image.1.ubuntu/16.04'
		image.name == 'ubuntu/16.04'
		image.imageType == ImageType.pxe
		image.category == 'maas.image.1'
		image.externalId == 'ubuntu/16.04'
	}

	void "machineToComputeServer"() {
		given:
		Map machine = [hostname: 'mymaas', system_id: 'server1', pool: [id: 'pool_1']]
		Cloud cloud = new Cloud(id: 1, account: new Account(id: 2))

		when:
		ComputeServer server = MaasComputeUtility.machineToComputeServer(machine, cloud)

		then:
		server.externalId == 'server1'
		server.name == 'mymaas'
		server.cloud == cloud
        server.account == cloud.account
	}
}
