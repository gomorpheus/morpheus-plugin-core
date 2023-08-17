package com.morpheusdata.core.costing;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.core.util.ApiParameterMap;
import com.morpheusdata.model.Account;
import com.morpheusdata.model.AccountInvoice;
import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.User;
import com.morpheusdata.response.costing.CloudCostResponse;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Provides accessor methods for fetching invoice/costing data from the Morpheus Database.
 * The invoice data is organized by resource association or by summary. There are rollup invoices for Groups,Clouds,Users,
 * Instances, Containers and even Accounts. These queries can be intensive, it is important to try and chunk or appropriately
 * filter the particular invoices that are wished for in order to minimize any overhead. These helper methods are useful
 * for creating custom reports or custom pages/widgets.
 *
 * @author David Estes
 * @since 0.12.2
 */
public interface MorpheusAccountInvoiceService extends MorpheusDataService<AccountInvoice> {
	Flowable<AccountInvoice> listByApiParams(User user, ApiParameterMap<String,Object> parameters);

	Single<CloudCostResponse> loadCloudCost(Account account, Cloud cloud, Date startDate, Date endDate);
	Single<CloudCostResponse> loadCloudCost(Account account, Cloud cloud, Date startDate, Date endDate, Boolean byTenant);
	Single<CloudCostResponse> loadCloudCost(Account account, Cloud cloud, Date startDate, Date endDate, Boolean byTenant, Map additionalOptions);

	/**
	 * Ensures that an active invoice record exists based on the scoped account and owner as well as the object the invoice is associated with.
	 * THe refType and refId/refUUID act as a polymorphic association to other morpheus model constructs such as
	 * AccountResource, ComputeZone, VirtualImage,ComputeServer,StorageVolume,NetworkLoadBalancer,etc.
	 * A result object will come back with a generated invoice if not found or a reference to an existing invoice updated with the provided information.
	 *
	 * @param owner The owner/manager of the object we are building an invoice for
	 *              (important on shared clouds where this would be the master account).
	 * @param account The current account this object belongs to that the invoice data should get associated with.
	 * @param refType The polymorphic object class to associate the invoice too (i.e. ComputeZone,ComputeServer,VirtualImage,NetworkLoadBalancer,Snapshot,StorageVolume,etc). See List From {@link AccountInvoice} REF_ properties
	 * @param refId The database id of the object to associate based on the refType
	 * @param refUUID The uuid of the object based on refType this is associated to. UUID Should always be used now to prevent id reuse
	 * @param costDate The date the invoice is being built
	 * @param invoice The properties of the invoice record we are creating
	 * @param newRecord If not found , do we create a new record.
	 * @return a Lookup result stating if the object was found or created and a copy of the saved invoice record with its populated identity.
	 */
	Single<InvoiceLookupResults> ensureActiveAccountInvoice(Account owner,Account account, String refType, Long refId, String refUUID, Date costDate, AccountInvoice invoice, Boolean newRecord);

	/**
	 * Ensures that an active invoice record exists based on the scoped account and owner as well as the object the invoice is associated with.
	 * THe refType and refId/refUUID act as a polymorphic association to other morpheus model constructs such as
	 * AccountResource, ComputeZone, VirtualImage,ComputeServer,StorageVolume,NetworkLoadBalancer,etc.
	 * A result object will come back with a generated invoice if not found or a reference to an existing invoice updated with the provided information.
	 *
	 * @param owner The owner/manager of the object we are building an invoice for
	 *              (important on shared clouds where this would be the master account).
	 * @param account The current account this object belongs to that the invoice data should get associated with.
	 * @param refType The polymorphic object class to associate the invoice too (i.e. ComputeZone,ComputeServer,VirtualImage,NetworkLoadBalancer,Snapshot,StorageVolume,etc). See List From {@link AccountInvoice} REF_ properties
	 * @param refId The database id of the object to associate based on the refType
	 * @param refUUID The uuid of the object based on refType this is associated to. UUID Should always be used now to prevent id reuse
	 * @param costDate The date the invoice is being built
	 * @param invoice The properties of the invoice record we are creating
	 * @return a Lookup result stating if the object was found or created and a copy of the saved invoice record with its populated identity.
	 */
	Single<InvoiceLookupResults> ensureActiveAccountInvoice(Account owner,Account account, String refType, Long refId, String refUUID, Date costDate, AccountInvoice invoice);

	/**
	 * Reconciles invoice totals based on the associated {@link com.morpheusdata.model.provisioning.AccountInvoiceItem}
	 * records on the invoice.
	 * @param invoiceIds a list of invoices to reconcile
	 * @return completable state
	 */
	Completable bulkReconcileInvoices(Collection<Long> invoiceIds);

	Completable summarizeCloudInvoice(Cloud cloud, String period, Date costDate, Collection<String> additionalCloudUUIDs);

	Completable summarizeCloudInvoice(Cloud cloud, String period, Date costDate, Collection<String> additionalCloudUUIDs, Date maxActualDate);

	Completable processProjectedCosts(Cloud cloud,String period,Collection<String> additionalCloudExternalIds);

	Completable processProjectedCosts(Cloud cloud,String period,Collection<String> additionalCloudExternalIds, Boolean definitive);

	public class InvoiceLookupResults {
		public Boolean found = false;
		public AccountInvoice invoice;
	}

}
