package com.morpheusdata.core.util;

import com.github.jknack.handlebars.internal.lang3.ArrayUtils;
import com.morpheusdata.model.*;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.text.SimpleDateFormat;
import java.util.*;

public class InvoiceUtility {
	public static String getCurrentPeriodString() {
		return getPeriodString(new Date());
	}

	public static String getPeriodString(Date costDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		return dateFormat.format(costDate);
	}

	public static Date getPeriodStartDate(String periodString) {
		int year = Integer.parseInt(periodString.substring(0,4));
		int month = Integer.parseInt(periodString.substring(4,6));
		Calendar rtn = Calendar.getInstance();
		rtn.set(Calendar.HOUR_OF_DAY,0);
		rtn.set(Calendar.MINUTE,0);
		rtn.set(Calendar.SECOND,0);
		rtn.set(Calendar.MILLISECOND,0);
		rtn.set(Calendar.DAY_OF_MONTH,1);
		rtn.set(Calendar.MONTH,month -1);
		rtn.set(Calendar.YEAR,year - 1);
		return rtn.getTime();
	}

	public static Date getPeriodStart(Date date) {
		return DateUtility.getStartOfMonth(date);
	}

	public static Date getPeriodEnd(Date date) {
		return DateUtility.getEndOfMonth(date);
	}

	public static Boolean checkDateCheckHash(Date billingStartDate, Date lineItemDate, String existingHash) throws DecoderException {

		byte[] hashArray;
		if(existingHash != null) {
			hashArray = Hex.decodeHex(existingHash);
		} else {
			return false;
		}

		long hourLong = ((lineItemDate.getTime() - billingStartDate.getTime()) / 3600000L );
		int currentHour = (int) hourLong;
		int hourByteIndex = currentHour / 8;
		int bitOffset = currentHour % 8;

		byte hourByte = hashArray[hourByteIndex];
		return (hourByte & (byte) ((byte) 0x01 << bitOffset)) != 0;
	}

	public static String updateDateCheckHash(Date billingStartDate, Date lineItemDate, String existingHash) throws DecoderException {
		Byte[] hashArray;
		if(existingHash != null) {
			hashArray = ArrayUtils.toObject(Hex.decodeHex(existingHash));
		} else {
			hashArray = new Byte[96];
			for(int x=0;x<96;x++) {
				hashArray[x] = 0x00;
			}
		}

		long hourLong = ((lineItemDate.getTime() - billingStartDate.getTime()) / 3600000L );
		int currentHour = (int) hourLong;
		int hourByteIndex = currentHour / 8;
		int bitOffset = currentHour % 8;
		if(hashArray[hourByteIndex] == null) {
			hashArray[hourByteIndex] = 0x00;
		}
		hashArray[hourByteIndex] = (byte) (hashArray[hourByteIndex] | (byte)((byte)(0x01) << bitOffset));
		StringBuilder sb = new StringBuilder();
		for (byte b : hashArray) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
		//return Arrays.toString(Hex.encodeHex(ArrayUtils.toPrimitive(hashArray)));
	}

	public static void configureResourceInvoice(AccountInvoice invoice, Cloud cloud, Account account, Long refId, String refType, String refName, String refUUID, String resourceExternalId) {
		invoice.setRefId(refId);
		invoice.setRefName(refName);
		invoice.setRefType(refType);
		invoice.setRefUUID(refUUID);
		invoice.setCloudId(cloud.getId());
		invoice.setCloudName(cloud.getName());
		invoice.setCloudRegion(cloud.getRegionCode());
		invoice.setResourceExternalId(resourceExternalId);

		if(account != null) {
			invoice.setAccountId(account.getId());
			invoice.setAccountName(account.getName());
		}
	}

	public static void configureResourceInvoice(AccountInvoice invoice, Cloud cloud, AccountResource resource) {
		configureResourceInvoice(invoice, cloud, cloud.getAccount(), resource.getId(), "AccountResource", resource.getName(), resource.getUuid(), resource.getExternalId());
		invoice.setUserId(resource.getUserId());
		invoice.setSiteId(resource.getSiteId());
		invoice.setPlanId(resource.getPlanId());
		invoice.setUserName(resource.getUserName());
		invoice.setPlanName(resource.getPlanName());
		invoice.setLayoutId(resource.getLayoutId());
		invoice.setLayoutName(resource.getLayoutName());
		invoice.setServerId(resource.getServerId());
		invoice.setServerName(resource.getServerName());
		invoice.setResourceName(resource.getName());
		invoice.setResourceType(resource.getType().getName());
		invoice.setResourceUuid(resource.getUuid());
	}

	public static void configureServerInvoice(AccountInvoice invoice, Cloud cloud, ComputeServer server) {
		configureResourceInvoice(invoice, cloud, server.getAccount(), server.getId(), "ComputeServer", server.getName(), server.getUuid(), server.getExternalId());

		if(server.getCreatedBy() != null) {
			invoice.setUserId(server.getCreatedBy().getId());
			invoice.setUserName(server.getCreatedBy().getUsername());
		}
		invoice.setSiteId(server.getProvisionSiteId());
		if(server.getPlan() != null) {
			invoice.setPlanId(server.getPlan().getId());
			invoice.setPlanName(server.getPlan().getName());
		}
		if(server.getLayout() != null) {
			invoice.setLayoutId(server.getLayout().getId());
			invoice.setLayoutName(server.getLayout().getName());
		}
		invoice.setServerId(server.getId());
		invoice.setServerName(server.getName());
	}

	public static void configureVolumeInvoice(AccountInvoice invoice, Cloud cloud, StorageVolume volume) {
		configureResourceInvoice(invoice, cloud, volume.getAccount(), volume.getId(), "StorageVolume", volume.getName(), volume.getUuid(), volume.getExternalId());
	}

	public static void configureLoadBalancerInvoice(AccountInvoice invoice, Cloud cloud, NetworkLoadBalancer loadBalancer) {
		configureResourceInvoice(invoice, cloud, loadBalancer.getAccount(), loadBalancer.getId(), "NetworkLoadBalancer", loadBalancer.getName(), loadBalancer.getUuid(), loadBalancer.getExternalId());
	}

	public static void configureWorkloadInvoice(AccountInvoice invoice, Cloud cloud, Workload workload) {
		configureResourceInvoice(invoice, cloud, workload.getAccount(), workload.getId(), "Container", workload.getName(), workload.getUuid(), workload.getExternalId());

		ComputeServer server = workload.getServer();
		Instance instance = workload.getInstance();

		if(server.getCreatedBy() != null) {
			invoice.setUserId(server.getCreatedBy().getId());
			invoice.setUserName(server.getCreatedBy().getUsername());
		}
		invoice.setSiteId(server.getProvisionSiteId());
		if(instance.getPlan() != null) {
			invoice.setPlanId(instance.getPlan().getId());
			invoice.setPlanName(instance.getPlan().getName());
		}
		if(server.getLayout() != null) {
			invoice.setLayoutId(server.getLayout().getId());
			invoice.setLayoutName(server.getLayout().getName());
		}
		invoice.setServerId(server.getId());
		invoice.setServerName(server.getName());
		invoice.setInstanceId(instance.getId());
		invoice.setInstanceName(instance.getName());
	}

	public static void configureInstanceInvoice(AccountInvoice invoice, Cloud cloud, Instance instance) {
		configureResourceInvoice(invoice, cloud, instance.getAccount(), instance.getId(), "Instance", instance.getDisplayName(), instance.getUuid(), instance.getExternalId());

		ComputeServer server = instance.getContainers().size() > 0 ? ((Workload) instance.getContainers().toArray()[0]).getServer() : null;
		if(server != null) {
			invoice.setServerId(server.getId());
			invoice.setServerName(server.getName());
		}
		if(instance.getCreatedBy() != null) {
			invoice.setUserId(instance.getCreatedBy().getId());
			invoice.setUserName(instance.getCreatedBy().getUsername());
		}
		if(instance.getSite() != null) {
			invoice.setSiteId(instance.getSite().getId());
			invoice.setSiteName(instance.getSite().getName());
		}
		if(instance.getPlan() != null) {
			invoice.setPlanId(instance.getPlan().getId());
			invoice.setPlanName(instance.getPlan().getName());
		}
		invoice.setInstanceId(instance.getId());
		invoice.setInstanceName(instance.getDisplayName());
	}

	public static void configureCloudInvoice(AccountInvoice invoice, Cloud cloud, ComputeSite site) {
		configureResourceInvoice(invoice, cloud, null, cloud.getId(), "ComputeZone", cloud.getName(), cloud.getUuid(), cloud.getExternalId());
		invoice.setSiteId(site.getId());
	}
}
