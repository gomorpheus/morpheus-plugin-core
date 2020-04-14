package com.morpheusdata.vmware.util

import com.github.stephenc.javaisotools.eltorito.impl.ElToritoConfig
import com.github.stephenc.javaisotools.iso9660.ISO9660File
import com.github.stephenc.javaisotools.iso9660.ISO9660RootDirectory
import com.github.stephenc.javaisotools.iso9660.impl.CreateISO
import com.github.stephenc.javaisotools.iso9660.impl.ISO9660Config
import com.github.stephenc.javaisotools.joliet.impl.JolietConfig
import com.github.stephenc.javaisotools.rockridge.impl.RockRidgeConfig
import com.github.stephenc.javaisotools.sabre.impl.ByteArrayDataReference
import groovy.util.logging.Commons

@Commons
class IsoUtility {

	static buildCloudIso(platform, metaData, userData, networkData = null) {
		if(platform == 'windows')
			return buildCloudbaseConfigIso(metaData, userData, networkData)
		return buildCloudConfigIso(metaData, userData, networkData)
	}

	static buildCloudConfigIso(metaData, userData, networkData) {
		def rtn
		try {
			ISO9660RootDirectory.MOVED_DIRECTORIES_STORE_NAME = 'rr_moved'
			def isoRoot = new ISO9660RootDirectory()
			def cloudConfigData = new ByteArrayDataReference(userData.bytes)
			def cloudConfigMetaData = new ByteArrayDataReference(metaData.bytes)
			def cloudConfigOutput = new ISO9660File(cloudConfigData, 'user-data', new Date().time)
			isoRoot.addFile(cloudConfigOutput)
			def cloudConfigMetaOutput = new ISO9660File(cloudConfigMetaData, 'meta-data', new Date().time)
			isoRoot.addFile(cloudConfigMetaOutput)
			def iso9660Config = new ISO9660Config()
			iso9660Config.allowASCII(true)
			iso9660Config.setInterchangeLevel(2)
			iso9660Config.restrictDirDepthTo8(true)
			iso9660Config.setPublisher('morpheus')
			iso9660Config.setSystemID('morpheus')
			iso9660Config.setVolumeID('cidata')
			iso9660Config.setDataPreparer('morpheus')
			iso9660Config.forceDotDelimiter(false)
			iso9660Config.restrictDirDepthTo8(false)
			def rrConfig = new RockRidgeConfig() //null //new RockRidgeConfig()
			//rrConfig.setMkisofsCompatibility(true)
			//rrConfig.setPublisher('morpheus')
			//rrConfig.setVolumeID('config')
			//rrConfig.setDataPreparer('morpheus')
			ElToritoConfig elToritoConfig = null
			// Joliet support
			def jolietConfig = new JolietConfig()
			jolietConfig.setPublisher('morpheus')
			jolietConfig.setVolumeID('cidata')
			jolietConfig.setDataPreparer('morpheus')
			jolietConfig.setUCS2Level(1)
			jolietConfig.forceDotDelimiter(false)
			def cloudConfigStream = new ISOImageByteArrayHandler()
			def iso = new CreateISO(cloudConfigStream, isoRoot)
			iso.process(iso9660Config, rrConfig, jolietConfig, elToritoConfig)
			log.debug('iso complete')
			rtn = cloudConfigStream.getOutputStream()
			//println("rtn ${rtn.toByteArray()}")
		} catch(e) {
			log.error("buildCloudConfigIso error: ${e}", e)
		}
		return rtn
	}

	static buildCloudbaseConfigIso(metaData, userData, networkData) {
		def rtn
		try {
			ISO9660RootDirectory.MOVED_DIRECTORIES_STORE_NAME = 'rr_moved'
			def isoRoot = new ISO9660RootDirectory()
			def cloudConfigData = new ByteArrayDataReference(userData.bytes)
			def cloudConfigMetaData = new ByteArrayDataReference(metaData.bytes)
			def osDir = isoRoot.addDirectory('openstack')
			def latestDir = osDir.addDirectory('latest')
			def cloudConfigOutput = new ISO9660File(cloudConfigData, 'user_data', new Date().time)
			latestDir.addFile(cloudConfigOutput)
			def cloudConfigMetaOutput = new ISO9660File(cloudConfigMetaData, 'meta_data.json', new Date().time)
			latestDir.addFile(cloudConfigMetaOutput)
			if(networkData) {
				def networkConfigData = new ByteArrayDataReference(networkData.bytes)
				def networkDir = osDir.addDirectory('content')
				def networkConfigOutput = new ISO9660File(networkConfigData, 'network_config', new Date().time)
				networkDir.addFile(networkConfigOutput)
			}
			def iso9660Config = new ISO9660Config()
			iso9660Config.allowASCII(true)
			iso9660Config.setInterchangeLevel(2)
			iso9660Config.restrictDirDepthTo8(true)
			iso9660Config.setPublisher('morpheus')
			iso9660Config.setSystemID('morpheus')
			iso9660Config.setVolumeID('config-2')
			iso9660Config.setDataPreparer('morpheus')
			iso9660Config.forceDotDelimiter(false)
			iso9660Config.restrictDirDepthTo8(false)
			def rrConfig = new RockRidgeConfig() //null //new RockRidgeConfig()
			//rrConfig.setMkisofsCompatibility(true)
			//rrConfig.setPublisher('morpheus')
			//rrConfig.setVolumeID('config')
			//rrConfig.setDataPreparer('morpheus')
			ElToritoConfig elToritoConfig = null
			// Joliet support
			def jolietConfig = new JolietConfig()
			jolietConfig.setPublisher('morpheus')
			jolietConfig.setVolumeID('config-2')
			jolietConfig.setDataPreparer('morpheus')
			jolietConfig.setUCS2Level(1)
			jolietConfig.forceDotDelimiter(false)
			def cloudConfigStream = new ISOImageByteArrayHandler()
			def iso = new CreateISO(cloudConfigStream, isoRoot)
			iso.process(iso9660Config, rrConfig, jolietConfig, elToritoConfig)
			log.debug('iso complete')
			rtn = cloudConfigStream.getOutputStream()
			//println("rtn ${rtn.toByteArray()}")
		} catch(e) {
			log.error("buildCloudConfigIso error: ${e}", e)
		}
		return rtn
	}

}
