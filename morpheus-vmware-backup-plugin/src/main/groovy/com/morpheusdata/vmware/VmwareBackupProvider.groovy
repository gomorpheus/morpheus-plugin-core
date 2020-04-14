package com.morpheusdata.vmware

import com.bertramlabs.plugins.karman.CloudFile
import com.morpheusdata.core.BackupProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Backup
import com.morpheusdata.model.BackupResult
import com.morpheusdata.model.Instance
import com.morpheusdata.model.Restore
import com.morpheusdata.model.VirtualImage
import com.morpheusdata.vmware.util.ComputeUtility
import com.morpheusdata.vmware.util.VmwareComputeUtility
import groovy.util.logging.Slf4j
import io.reactivex.Observable

import java.util.zip.ZipOutputStream

@Slf4j
class VmwareBackupProvider implements BackupProvider {
	MorpheusContext ctx
	Plugin plugin

	VmwareBackupProvider(Plugin plugin, MorpheusContext morpheusContext) {
		this.ctx = morpheusContext
		this.plugin = plugin
	}

	@Override
	MorpheusContext getMorpheusContext() {
		ctx
	}

	@Override
	String getProviderCode() {
		return 'vmware'
	}

	@Override
	String getProviderName() {
		return 'vmware'
	}

	@Override
	def getBackupRestoreInstanceConfig(BackupResult backupResult, Instance backupInstance, Map opts) {
		def rtn = [success:false]
		try {
			Backup backup = backupResult.backup
			def container = ctx.compute.getContainerById(backup.containerId).blockingGet()
			log.info("getBackupRestoreInstanceConfig: {}", backupResult)
			def instanceOpts = [:]
			def layoutId = backupResult.instanceLayoutId ?: backup.instanceLayoutId ?: backupInstance?.layout?.id
			def planId = backupResult.planId ?: backup.servicePlanId ?: backupInstance?.plan?.id
			def instanceLayout = ctx.compute.getInstanceTypeLayoutById(layoutId).blockingGet() // InstanceTypeLayout.read(id)
			def newLayout = instanceLayout.cloneLayoutId ? ctx.compute.getInstanceTypeLayoutById(instanceLayout.cloneLayoutId).blockingGet() : instanceLayout
			def newPlan = ctx.compute.getServicePlanById(planId).blockingGet() // ServicePlan.read(planId)
			instanceOpts.instance = [account:backup.account, instanceType:newLayout.instanceType, layout:newLayout,
									 plan:newPlan, name:opts.name ?: (backupInstance ? backupInstance.name + ' clone' : backup.name),
									 site:[id:(opts.siteId ?: backupInstance.site.id)],
									 createdBy:[id:opts.userId]]
			instanceOpts.config = backupResult.getConfigMap() ?: [:]
			instanceOpts.servicePlan = newPlan.id
			instanceOpts.servicePlanOptions = [maxMemory:backupResult.maxMemory ?: backupInstance?.maxMemory,
											   maxStorage:backupInstance?.maxStorage, maxCores:backupResult.maxCores ?: backupInstance?.maxCores, coresPerSocket: backupResult.coresPerSocket ?: backupInstance?.coresPerSocket]
			def resourcePool
			if(backupResult.resourcePoolId) {
				resourcePool = ctx.compute.getComputeZonePoolById(backupResult.resourcePoolId)// ComputeZonePool.read(backupResult.resourcePoolId)
			}
			instanceOpts.vmwareResourcePoolId = resourcePool?.externalId ?: container?.server?.resourcePool?.externalId
			instanceOpts.volumes = opts.volumes ?: backupResult?.volumesMap?.clone() ?: backupInstance?.volumesMap?.clone()
			instanceOpts.networkInterfaces = opts.networkInterfaces ?: backupResult.interfacesMap?.clone()  ?: backupInstance?.interfacesMap?.clone()
			instanceOpts.storageController = opts.storageController ?: backupResult.controllersMap?.clone()  ?: backupInstance.controllersMap?.clone()
			def osType = backupResult.osTypeId ? ctx.compute.getOsTypeById(backupResult.osTypeId) : container?.server?.serverOs
			if(opts.zoneId)
				instanceOpts.zoneId = opts.zoneId
			//if backup results are copied down and snapshot no longer exists
			if(backupResult.snapshotExtracted) {
				//need to convert the extracted snapshot to a virtual image
				//look for image first
				VirtualImage virtualImage = ctx.compute.findVirtualImageByOwnerAndCode(backup.account, "backup.${backup.id}.${backupResult.id}").blockingGet() //VirtualImage.findByOwnerAndCode(backup.account, "backup.${backup.id}.${backupResult.id}")
				if(!virtualImage) {
					def backupDate = backupResult.startDate ? ctx.backup.extractBackupDate(backupResult.startDate) : new Date()
					def transferOpts = [imageType:'vmdk', zoneTypeCode:'vmware', osType:osType, name:"${backupResult.backupName}-${backupDate.time}"]
					def transferResults = ctx.backup.transferBackupToVirtualImage(backup.account, backupResult, transferOpts)
					if(transferResults.success == true)
						virtualImage = transferResults.virtualImage
				}
				if(virtualImage) {
					instanceOpts.template = "${virtualImage.id}"
					instanceOpts.provisionOpts = [:]
					rtn.success = true
					rtn.instanceOpts = instanceOpts
				}
			} else {
				//still a snapshot restore normal way
				instanceOpts.provisionOpts = [backupSetId:backupResult.backupSetId]
				rtn.success = true
				rtn.instanceOpts = instanceOpts
			}
		} catch(e) {
			log.error("getBackupRestoreInstanceConfig error: ${e}", e)
		}
		return rtn
	}

	@Override
	def executeBackup(Backup backup, Map backupConfig, Map opts) {
		def rtn = [success:false]
		rtn += backupConfig

		try {
			def backupResult = backupConfig.backupResult as BackupResult
			def container = ctx.compute.getContainerById(rtn.containerId).blockingGet() // Container.read(rtn.containerId)
			def instance = ctx.compute.getInstanceById(container.instance.id).blockingGet() // Instance.read(container.instanceId)
			def server = ctx.compute.getComputeServerById(container.server.id).blockingGet() // ComputeServer.read(container.serverId)
			def zone = ctx.compute.getComputeZoneById(server.zone.id).blockingGet() // ComputeZone.read(server.zoneId)
			def zoneType = ctx.compute.getComputeZoneTypeById(zone.zoneType.id).blockingGet() // ComputeZoneType.read(zone.zoneTypeId)
			def apiUrl
			def apiUsername
			def apiPassword
			def providerInfo
			def archiveName
			if(zoneType.code == 'esxi') {
				def node = ctx.compute.getComputeServerById(container.getConfigProperty('hostId').toLong()).blockingGet()
				apiUrl = VmwareComputeUtility.getVmwareHostUrl(node)
				apiUsername = node.sshUsername
				apiPassword = node.sshPassword
			} else {
				apiUrl = VmwareComputeUtility.getVmwareApiUrl(zone)
				apiUsername = VmwareComputeUtility.getVmwareUsername(zone)
				apiPassword = VmwareComputeUtility.getVmwarePassword(zone)
			}
			// log.info("backupConfig container: {}", rtn)
			//config
			log.info("Preparing To Execute Vmware Snapshot Backup for ${server?.name}")

			def snapshotName = "${instance.name}.${container.id}.${System.currentTimeMillis()}"
			def snapshotOpts = [externalId:server.externalId, snapshotName:snapshotName, snapshotDescription:'']
			def outputPath = rtn.backupConfig.workingPath
			def outputFile = new File(outputPath)
			outputFile.mkdirs()
			//set process id
			ctx.backup.setBackupProcessId(backupResult, '1000', 'exportSnapshot')
			//update status
			ctx.backup.updateBackupStatus(backupResult.id, 'IN_PROGRESS', [
					planId: container.plan?.id,
					instanceLayoutId: instance.layout?.id,
					maxMemory: container.maxMemory,
					maxCores: container.maxCores,
					coresPerSocket: container.coresPerSocket,
					controllers: instance.getControllersMap(),
					volumes: instance.getVolumesMap(),
					resourcePoolId: container.server.resourcePool?.id,
					sshUsername: container.server.sshUsername,
					sshPassword: container.server.sshPassword,
					isCloudInit: container.server.isCloudInit,
					osTypeId: container.server.serverOs?.id,
					interfaces: instance.getInterfacesMap()])
			//sync for non windows
			if(server.sourceImage && server.sourceImage.isCloudInit && server.serverOs?.platform != 'windows')
				ctx.provision.executeComputeServerCommand('vmwareProvisionService', server, 'sudo rm -f /etc/cloud/cloud.cfg.d/99-manual-cache.cfg ; sync', [guestExec: true])
			//take snapshot

			Observable.create({
				log.info("Executing Vmware Snapshot Backup for ${server?.name}")
				Map snapshotResults = VmwareComputeUtility.snapshotVm(apiUrl, apiUsername, apiPassword, snapshotOpts)
				return snapshotResults
			}).flatMap({ Map snapshotResults ->
				try {
					def tmpServer = ctx.compute.getComputeServerById(server.id).blockingGet() // ComputeServer.get(server.id)
					if(tmpServer.sourceImage && tmpServer.sourceImage.isCloudInit && tmpServer.serverOs?.platform != 'windows')
						ctx.provision.executeComputeServerCommand('vmwareProvisionService', server, "sudo bash -c \"echo 'manual_cache_clean: True' >> /etc/cloud/cloud.cfg.d/99-manual-cache.cfg\" ; sync", [guestExec: true])

					snapshotResults.providerInfo = ctx.backup.getBackupStorageProviderConfig(backup.account,backup.id)
					providerInfo = snapshotResults.providerInfo
					archiveName = "backup.${rtn.backupResultId}.zip"
					if(backup.copyToStore) {
						ctx.backup.updateBackupStatus(backupResult.id, 'IN_PROGRESS', [providerType:snapshotResults.providerInfo.providerType, targetBucket:snapshotResults.providerInfo.bucketName, targetDirectory:"backup.${backup.id}", targetArchive:archiveName, imageType: 'vmdk'])
					}
					//we have to do some piping magic for this one
				} catch(ex) {
					log.warn("Warning resetting base cloudinit image for vmware backup ${ex.message}",ex)
					//cleanup failure
				}

				return snapshotResults
			}).flatMap({  Map snapshotResults ->
				if(snapshotResults.success && backup.copyToStore) {
					log.info("Snapshot Completed For Server: ${server.name}... Beginning Transfer to Target Storage")
					CloudFile zipFile = snapshotResults.providerInfo.provider[snapshotResults.providerInfo.bucketName]["backup.${backup.id}/${archiveName}"]
					PipedOutputStream outStream = new PipedOutputStream()
					InputStream istream  = new PipedInputStream(outStream)
					BufferedOutputStream buffOut = new BufferedOutputStream(outStream,1048576)
					ZipOutputStream zipStream = new ZipOutputStream(buffOut)
					def saveResults = [success:false]
					def saveThread = Thread.start {
						try {
							zipFile.setInputStream(istream)
							zipFile.save()
							saveResults.archiveSize = zipFile.getContentLength()
							saveResults.success = true
						} catch(ex) {
							log.error("Error Saving Backup File! ${ex.message}", ex)
							try {
								zipStream.close()
							} catch(ignore) {
								//dont care about exception on this but we need to close it on save failure thread in the event we need to cutoff the stream
							}
						}
					}
					def exportOpts = [targetDir:outputPath, targetZipStream:zipStream, snapshotId:snapshotResults.snapshotId, vmName:"${instance.name}.${container.id}"]
					log.debug("exportOpts: {}", exportOpts)
					def exportResults = VmwareComputeUtility.exportVmSnapshot(apiUrl, apiUsername, apiPassword, exportOpts)
					log.debug("got: {}", exportResults)
					saveThread.join()
					snapshotResults.saveResults = saveResults

				} else if(snapshotResults.success) {
					log.info("Snapshot Complete, Leaving on Cloud as Storage Mode is turned off.")
					snapshotResults.saveResults = [success:true]
				}
				return snapshotResults
			}).flatMap({ Map snapshotResults ->
				def saveResults = snapshotResults.saveResults
				if(saveResults.success == true) {
					log.info("backup complete: {}", snapshotResults)
					if(backup.copyToStore) {
						def statusMap = [backupResultId:rtn.backupResultId, executorIP:rtn.ipAddress, destinationPath:outputPath, snapshotId:snapshotResults.snapshotId,
										 snapshotExternalId:snapshotResults.externalId, providerType:snapshotResults.providerInfo.providerType, targetBucket:snapshotResults.providerInfo.bucketName,
										 targetDirectory:"backup.${backup.id}", targetArchive:archiveName, backupSizeInMb:(saveResults.archiveSize ?: 1).div(ComputeUtility.ONE_MEGABYTE),
										 imageType:'vmdk', snapshotExtracted:'yes', success:true]
						ctx.backup.updateBackupStatus(backupResult.id, statusMap)
					} else {

						def statusMap = [backupResultId:rtn.backupResultId, executorIP:rtn.ipAddress, destinationPath:outputPath, snapshotId:snapshotResults.snapshotId,
										 snapshotExternalId:snapshotResults.externalId, providerType:'vmware', providerBasePath:'vmware', targetBucket:snapshotResults.externalId,
										 targetDirectory:snapshotName, targetArchive:snapshotResults.snapshotId, backupSizeInMb:(snapshotResults.archiveSize ?: 0).div(ComputeUtility.ONE_MEGABYTE),
										 snapshotExtracted:'no', success:true]
						ctx.backup.updateBackupStatus(backupResult.id, statusMap)
					}
					//remove the snapshot
				} else {
					log.error("Backup Failed ${snapshotResults}")
					def error = saveResults.error ?: "Failed to save backup result"
					def statusMap = [backupResultId:rtn.backupResultId, executorIP:rtn.ipAddress, destinationPath:outputPath,
									 backupSizeInMb:0, success:false, errorOutput:error.encodeAsBase64()]
					ctx.backup.updateBackupStatus(backupResult.id, statusMap)
					ctx.backup.deleteBackupResult(backup.account, backup.id, snapshotResults.providerInfo.storageProviderId, "backup.${backup.id}", archiveName)

				}
				return snapshotResults
			}).flatMap({ Map snapshotResults ->
				if(backup.copyToStore) {
					VmwareComputeUtility.removeAllVmSnapshots(apiUrl, apiUsername, apiPassword, [externalId: server.externalId])
				}
				return snapshotResults
			}).doOnError( { Throwable e ->
				log.error("executeBackup: ${e}", e)
				rtn.message = e.getMessage()
				def error = "Failed to execute backup"
				def statusMap = [backupResultId:rtn.backupResultId, executorIP:rtn.ipAddress,
								 backupSizeInMb:0, success:false, errorOutput:error.encodeAsBase64()]
				ctx.backup.updateBackupStatus(rtn.backupResultId, statusMap)

				if(archiveName && providerInfo) {
					try {
						ctx.backup.deleteBackupResult(backup.account, backup.id, providerInfo.storageProviderId, "backup.${backup.id}", archiveName)
					} catch(e3) {
						log.warn("Error cleaning up failed backup files for backup: {}",e3.message,e3)
					}
				}

				if(backup.copyToStore) {
					VmwareComputeUtility.removeAllVmSnapshots(apiUrl, apiUsername, apiPassword, [externalId: server.externalId])
				}
			}).subscribe()
		} catch(e) {
			log.error("executeBackup: ${e}", e)
		}
		return rtn
	}

	@Override
	def extractBackup(BackupResult backupResult, Map opts) {
		def rtn = [success:false]
		try {
			rtn.backupResultId = backupResult.id
			def backup = ctx.backup.getBackupById(backupResult.backup.id).blockingGet() // Backup.get(backupResult.backup.id)
			def container = ctx.compute.getContainerById(backup.containerId).blockingGet() //Container.read(backup.containerId)
			def instance = ctx.compute.getInstanceTypeLayoutById(container.instance.id).blockingGet() //Instance.read(container.instanceId)
			def outputPath = ctx.backup.getWorkingBackupPath(backup.id, backupResult.id)
			def outputFile = new File(outputPath)
			outputFile.mkdirs()
			def exportOpts = [targetDir:outputPath, snapshotId:backupResult.snapshotId, vmName:"${instance.name}.${container.id}"]
			log.info("extractBackup options: ${exportOpts}")
			def exportResults = ctx.backup.exportSnapshot('vmwareProvisionService', container, exportOpts)
			log.info("extractBackup results: ${exportResults}")
			def saveResults = ctx.backup.saveBackupResults(backup.account, outputFile.getPath(), backup.id)
			if(saveResults.success == true) {
				def statusMap = [backupResultId :rtn.backupResultId, destinationPath:outputPath, snapshotExtracted:'yes',
								 providerType   :saveResults.providerType, providerBasePath:saveResults.basePath, targetBucket:saveResults.targetBucket,
								 targetDirectory:saveResults.targetDirectory, targetArchive:saveResults.targetArchive,
								 backupSizeInMb : (saveResults.archiveSize ?: 1) / ComputeUtility.ONE_MEGABYTE, success:true]
				ctx.backup.updateBackupResult(backupResult.id, statusMap)
				rtn.success = true
				rtn.destinationPath = outputPath
				rtn.snapshotId = statusMap.snapshotId
				rtn.targetBucket = statusMap.targetBucket
				rtn.targetProvider = statusMap.providerType
				rtn.targetBase = statusMap.providerBasePath
				rtn.targetDirectory = statusMap.targetDirectory
				rtn.targetArchive = statusMap.targetArchive
			} else {
				def error = saveResults.error ?: "Failed to save backup result"
				rtn.message = error
			}
		} catch(e) {
			log.error("extractBackup: ${e}", e)
			rtn.message = "Failed to extract backup: ${e.getMessage()}"
		}
		return rtn
	}


	@Override
	def restoreBackup(Restore backupRestore, BackupResult backupResult, Map opts) {
		def rtn = [success:false]
		try {
			if(backupResult.snapshotExtracted) {
				rtn.message = 'Restoring to current instance from a VMWare exported backup is not supported. User must restore to a new instance.'
				ctx.backup.updateBackupRestore(backupRestore, [status: BackupServiceStatus.STATUS_FAILED, errorMessage: rtn.message])
			} else {
				def container = ctx.compute.getContainerById(backupResult.containerId).blockingGet() // Container.read(backupResult.containerId)
				def server = ctx.compute.getComputeServerById(container.server.id).blockingGet() // ComputeServer.read(container.serverId)
				def zone = ctx.compute.getComputeZoneById(server.zone.id).blockingGet() // ComputeZone.read(server.zoneId)
				def apiUrl
				def apiUsername
				def apiPassword
				apiUrl = VmwareComputeUtility.getVmwareApiUrl(zone)
				apiUsername = VmwareComputeUtility.getVmwareUsername(zone)
				apiPassword = VmwareComputeUtility.getVmwarePassword(zone)
				def snapshotId = backupResult.resultArchive
				def snapshotOpts = [snapshotId:snapshotId]
				//get original power state
				def serverDetail = VmwareComputeUtility.getServerDetail(apiUrl, apiUsername, apiPassword, [externalId:server.externalId])
				def powerOn = serverDetail?.success ? serverDetail.results?.server?.poweredOn : false
				//execute restore
				def results = VmwareComputeUtility.restoreVmSnapshot(apiUrl, apiUsername, apiPassword, snapshotOpts)
				log.info("restore complete: {} output: {}", results.success, results.msg)
				if(results.success)
					ctx.backup.updateBackupRestore(backupRestore, [status:BackupServiceStatus.STATUS_SUCCEEDED])
				else
					ctx.backup.updateBackupRestore(backupRestore, [status:BackupServiceStatus.STATUS_FAILED])
				//if original state was powered on, make sure it is started after restore
				if(powerOn) {
					log.info("starting {}", server.externalId)
					VmwareComputeUtility.startVm(apiUrl, apiUsername, apiPassword, [externalId:server.externalId])
				}
				rtn.success = true
			}
		} catch(e) {
			log.error("restoreBackup: ${e}", e)
			rtn.success = false
			rtn.message = e.getMessage()
			ctx.backup.updateBackupRestore(backupRestore, [status:BackupServiceStatus.STATUS_FAILED, errorMessage: rtn.message])
		}
		return rtn
	}

	@Override
	def cleanBackupResult(BackupResult backupResult) {
		def rtn = [success:true]
		try {
			def backup = backupResult.backup
			def snapshotId = backupResult?.resultArchive
			if(backup.copyToStore) {
				ctx.backup.cleanBackupResult(backupResult)
			} else {
				if(snapshotId) {
					def container = ctx.compute.getContainerById(backupResult.containerId).blockingGet() //Container.read(backupResult.containerId)
					def server = ctx.compute.getComputeServerById(container.server.id).blockingGet() // ComputeServer.read(container.serverId)
					def zone = ctx.compute.getComputeZoneById(server.zone.id).blockingGet()// ComputeZone.read(server.zoneId)
					def zoneType = ctx.compute.getComputeZoneTypeById(zone.zoneType.id) //ComputeZoneType.read(zone.zoneTypeId)
					def apiUrl
					def apiUsername
					def apiPassword
					if(zoneType.code == 'esxi') {
						def node = ctx.compute.getComputeServerById(container.getConfigProperty('hostId').toLong()).blockingGet()
						apiUrl = VmwareComputeUtility.getVmwareHostUrl(node)
						apiUsername = node.sshUsername
						apiPassword = node.sshPassword
					} else {
						apiUrl = VmwareComputeUtility.getVmwareApiUrl(zone)
						apiUsername = VmwareComputeUtility.getVmwareUsername(zone)
						apiPassword = VmwareComputeUtility.getVmwarePassword(zone)
					}
					def snapshotOpts = [snapshotId:snapshotId]
					//remove the snapshot
					log.info("attempting to delete vmware snapshot ${snapshotId}")
					rtn = VmwareComputeUtility.removeVmSnapshot(apiUrl, apiUsername, apiPassword, snapshotOpts)
					log.info("delete snapshot complete: ${rtn.success} output: ${rtn.msg}")
				}
			}

		} catch(e) {
			log.error("deleteBackupResult: ${e}", e)
			rtn.message = e.getMessage()
		}
		return rtn
	}
}
