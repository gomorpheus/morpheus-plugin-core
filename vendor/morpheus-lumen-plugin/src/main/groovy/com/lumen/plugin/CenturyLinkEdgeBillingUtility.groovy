package com.lumen.plugin

import com.morpheusdata.apiutil.RestApiUtil
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.ComputeZone
import groovy.util.logging.Slf4j
import groovy.json.JsonBuilder
import groovy.xml.MarkupBuilder

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.time.Instant

@Slf4j
class CenturyLinkEdgeBillingUtility {
	static waitInterval = 10000l
	static maxWaitAttempts = 60
	static tokenBuffer = 1000l * 60l //60 second buffer

	static def getBillingAuthConfig(ComputeZone zone, Map customerConfig) {
		def zoneConfig = zone.getConfigMap()
		def rtn = [
				apiUrl:zoneConfig.billingServiceUrl,
				apiVersion:'v1',
				basePath:"/messaging-producer/publish/",
				apiKey: zoneConfig.billingApiKey,
				apiSecret:zoneConfig.billingApiSecret,
				accountId:customerConfig.accountNumber,
		]
		return rtn
	}

	static def digestBilling(String digestTime, String digestSecret) {
		final String algorithm = "HmacSHA256"
		SecretKeySpec signingKey = new SecretKeySpec(digestSecret.bytes, algorithm)
		Mac mac = Mac.getInstance(algorithm)
		mac.init(signingKey)
		byte[] rawHmac = mac.doFinal(digestTime.bytes)
		return rawHmac.encodeBase64()
	}

	static def lumenBillingEvent(String event, ComputeServer server = [:], Map customerConfig = [:]) {
		def billingConfig = getBillingAuthConfig(server?.zone, customerConfig)
		if(!billingConfig?.apiUrl) {
			log.info("lumenBillingEvent skipped, apiUrl not configured.")
			return
		}

		def circuitId = findCircuitId(server)
		if(!circuitId) {
			log.info("Can not call billing for {} as there was no circuitId found on any interface.", server)
			return
		}
		def correlationIdString = "${circuitId}-Deactivation"
		boolean isDisconnect = true
		if(event == "MILESTONE.ACTIVATION_COMPLETE") {
			correlationIdString = "${circuitId}-Activation"
			isDisconnect = false
		}

		def root = isDisconnect ? "command" : "event"
		def type = isDisconnect ? "commandType" : "eventType"

		def writer = new StringWriter()
		def xml = new MarkupBuilder(writer)
		xml.mkp.xmlDeclaration(version: "1.0", encoding: "UTF-8")
		xml."$root"() {
			messageType('E.Cloud.EdgeServices.Compute.Event')
			"$type"(event)
			eventTimestamp(Instant.now().toString())
			correlationId(correlationIdString)
			messageSource('Morpheus')
			businessKeys() {
				businessKey() {
					entity('DynamicConnectionService')
					name('serviceId')
					value(circuitId)
				}
				businessKey() {
					entity('Account')
					name('billingAccountNumber')
					value(customerConfig.accountNumber)
				}
				businessKey() {
					entity('Product')
					name('productCode')
					value('00080')
				}
			}
			attributes() {
				attribute() {
					name("networkType")
					value("Public IP")
				}
				attribute() {
					name("serverSize")
					value("${server.plan.name} – ${server.plan.maxCores} – ${server.plan.maxMemory} – ${server.plan.maxStorage}")
				}
				attribute() {
					name("locationA")
					value(server.zone.name)
				}
				attribute() {
					name("rateCurrency")
					value("USD") // User currency
				}
				attribute() {
					name("ratePerUnit")
					value(server.hourlyPrice)
				}
				attribute() {
					name("unitOfMeature")
					value("Hour")
				}
				attribute() {
					name("eventTime")
					value(Instant.now().toString().replace("T", " ").replace("Z", ""))
				}
				attribute() {
					name("billingType")
					value("hourly")
				}

				attribute() {
					name("glmIdA")
					value("SNJUCACL")
				}
			}
		}
		def body = writer.toString()
		billingConfig.isDisconnect = isDisconnect
		callLumenBillingApi(body, billingConfig)
	}

	static String findCircuitId(ComputeServer server) {
		def serverInterfaces = server?.interfaces?.findAll{ it.network != null && it.active }
		def serverInterface = serverInterfaces?.find { it?.network?.getConfigProperty('circuitId') != null }
		serverInterface?.network?.getConfigProperty('circuitId')
	}

	static def lumenBandwidthBilling(String command, ComputeServer server, Map customerConfig = [:]) {
		def billingConfig = getBillingAuthConfig(server?.zone, customerConfig)
		if(!billingConfig?.apiUrl) {
			log.info("lumenBandwidthBilling skipped, apiUrl not configured.")
			return
		}

		def circuitId = findCircuitId(server)
		if(!circuitId) {
			log.info("Can not call billing for {} as there was no circuitId found on any interface.", server)
			return
		}

		def correlationIdString = "${circuitId}-Deactivation"
		boolean isDisconnect = true
		if(command == "Install") {
			correlationIdString = "${circuitId}-Activation"
			isDisconnect = false
		}

		def root = isDisconnect ? "command" : "event"
		def type = isDisconnect ? "commandType" : "eventType"


		log.info("lumenBandwidthBilling command: {}, circuitId: {}", command, circuitId)

		def eventTimeString = Instant.now().toString()
		def json = new JsonBuilder()
		json {
			billingAccountNumber customerConfig.accountNumber
			serviceId circuitId
			productCode "00080"
			networkType "Public IP"
			serverSize "${server.plan.name} – ${server.plan.maxCores} – ${server.plan.maxMemory} – ${server.plan.maxStorage}"
			rateCurrency "USD"
			unitOfMeature "GB"
			eventTime eventTimeString.replace("Z", "")
			billingType "Egress"
			glmIdA "SNJUCACL"
			glmIdB null
			rates([
					[
							rateType: null,
							rateDescription: "Tier-0 Rate Per GB",
							upperBound: 1.0,
							ratePerUnit: 0,
							rateStartDate: null,
							rateStopDate :null
					],
					[
							rateType: null,
							rateDescription: "Tier-1 Rate Per GB",
							upperBound: 10_000.0,
							ratePerUnit: 0.07,
							rateStartDate: null,
							rateStopDate :null
					],
					[
							rateType: null,
							rateDescription: "Tier-2 Rate Per GB",
							upperBound: 50_000.0,
							ratePerUnit: 0.06,
							rateStartDate: null,
							rateStopDate :null
					],
					[
							rateType: null,
							rateDescription: "Tier-3 Rate Per GB",
							upperBound: 150_000.0,
							ratePerUnit: 0.05,
							rateStartDate: null,
							rateStopDate :null
					],
					[
							rateType: null,
							rateDescription: "Tier-4 Rate Per GB",
							upperBound: 500_000.0,
							ratePerUnit: 0.04,
							rateStartDate: null,
							rateStopDate :null
					],
					[
							rateType: null,
							rateDescription: "Tier-5 Rate Per GB",
							upperBound: null,
							ratePerUnit: 0.03,
							rateStartDate: null,
							rateStopDate :null
					]
			])
			extendedAttributes {
				"FutureKeyToAdd" "FutureValToAdd"
			}
		}

		def writer = new StringWriter()
		def xml = new MarkupBuilder(writer)
		xml.mkp.xmlDeclaration(version: "1.0", encoding: "UTF-8")
		xml."$root"() {
			messageType("E.BillingAndRating.Billing.SES.EdgeComputeOrder.Bill.Command")
			"$type"(command) // Install == Start, Disconnect == Stop
			eventTimeStamp(eventTimeString)
			securityHeader() { applicationKey(billingConfig.apiKey) }
			correlationId(correlationIdString)
			messageSource("Morpheus")
			businessKeys() {
				businessKey() {
					entity("EdgeComputeService")
					name("serviceId")
					value(circuitId)
				}
				businessKey() {
					entity("Account")
					name("billingAccountNumber")
					value(customerConfig.accountNumber)
				}
				businessKey() {
					entity("Product")
					name("productCode")
					value("productCode")
				}
			}
			payload(json.toString())
		}
		def body = writer.toString()
		billingConfig.isDisconnect = isDisconnect
		callLumenBillingApi(body, billingConfig)
	}

	/*
		Used internally to call the API
	 */
	protected static def callLumenBillingApi(String body, Map billingConfig) {
		String digestTime = new Date().getTime()
		def hmac = digestBilling(digestTime, billingConfig.apiSecret)

		billingConfig.basePath = billingConfig?.isDissconnect ? "${billingConfig.basePath}/command" : "${billingConfig.basePath}/event"

		def headers = [
				'X-Level3-Application-Key': billingConfig.apiKey,
				'X-Level3-Digest': hmac.toString(),
				'X-Level3-Digest-Time': digestTime
		]
		log.info("callLumenBillingApi req headers: ${headers}")
		log.info("callLumenBillingApi req body: ${body}")
		def rtn = RestApiUtil.callXmlApi(billingConfig.apiUrl, billingConfig.basePath, [headers: headers, body: body] )
		log.info("callLumenBillingApi resp: ${rtn}")
		if (rtn.statusCode != 200) {
			log.error(rtn.error)
		}
		return rtn
	}
}
