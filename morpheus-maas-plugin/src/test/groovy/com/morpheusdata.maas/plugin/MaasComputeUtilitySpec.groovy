package com.morpheusdata.maas.plugin

import com.morpheusdata.model.Account
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.ImageType
import com.morpheusdata.model.VirtualImage
import com.morpheusdata.model.ServicePlan
import com.morpheusdata.model.OsType
import com.morpheusdata.model.ComputeZonePool
import com.morpheusdata.model.ComputeServerInterface
import com.morpheusdata.model.NetworkDomain
import com.morpheusdata.model.StorageVolume

import spock.lang.Specification

class MaasComputeUtilitySpec extends Specification {

	void "bootImageToVirtualImage"() {
		given:
		Map apiResponse = [name: 'ubuntu/16.04', architecture: 'amd64', id: 5]
		Cloud cloud = new Cloud(id: 1)

		when:
		VirtualImage image = MaasComputeUtility.bootImageToVirtualImage(cloud, apiResponse, null)

		then:
		image.code == 'maas.image.1.ubuntu/16.04'
		image.name == 'ubuntu/16.04 - amd64'
		image.imageType == ImageType.pxe
		image.category == 'maas.image.1'
		image.externalId == 'ubuntu/16.04/5'
	}

	void "configureComputeServer with no existing"() {
		given:
		Map machine = [hostname: 'mymaas', system_id: 'server1', pool: [id: 'pool_1']]
		Cloud cloud = new Cloud(id: 1, account: new Account(id: 2))

		when:
		ComputeServer server = MaasComputeUtility.configureComputeServer(machine, null, cloud, null, null)

		then:
		server.externalId == 'server1'
		server.name == 'mymaas'
		server.cloud == cloud
        server.account == cloud.account
	}

	void "configureComputeServer with existing"() {
		given:
		Map machine = [hostname: 'mymaas', system_id: 'server1', pool: [id: 'pool_1'], tag_names: ['large'], status: 22]

		def typePlans = new ArrayList<ServicePlan>()
		typePlans.add(new ServicePlan(id: 41, tagMatch: 'no match'))
		typePlans.add(new ServicePlan(id: 42, tagMatch: 'large'))


		Cloud cloud = new Cloud(id: 1, account: new Account(id: 2))
		OsType serverOs = new OsType(id: 5, name: 'some os')
		VirtualImage virtualImage = new VirtualImage(id: 6, name: 'some vi')
		ComputeZonePool resourcePool = new ComputeZonePool(id: 7)
		def interfaces = new ArrayList<>()
		interfaces.add(new ComputeServerInterface(id: 8))
		interfaces.add(new ComputeServerInterface(id: 82))
		def volumes = new ArrayList<>()
		volumes.add(new StorageVolume(id: 9))
		NetworkDomain networkDomain = new NetworkDomain(id: 3, name: 'network domain')

		ComputeServer existingServer = new ComputeServer(id: 2,
			uuid: '123',
			status: 'available',
			networkDomain: networkDomain,
			serverOs: serverOs,
			sourceImage: virtualImage,
			resourcePool: resourcePool,
			interfaces: interfaces,
			volumes: volumes)

		when:
		ComputeServer server = MaasComputeUtility.configureComputeServer(machine, existingServer, cloud, resourcePool, typePlans)

		then:
		server.externalId == 'server1'
		server.name == 'mymaas'
		server.cloud == cloud
		server.account == cloud.account
		server.status == 'error'
		server.uuid == '123'
		server.networkDomain.id == networkDomain.id
		server.serverOs.id == serverOs.id
		server.sourceImage.id == virtualImage.id
		server.resourcePool.id == resourcePool.id
		server.interfaces.size == interfaces.size()
		server.interfaces[0].id == interfaces[0].id
		server.volumes.size == volumes.size()
		server.volumes[0].id == volumes[0].id
	}
}
