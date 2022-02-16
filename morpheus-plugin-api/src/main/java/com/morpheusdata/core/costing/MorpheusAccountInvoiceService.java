package com.morpheusdata.core.costing;

import com.morpheusdata.core.util.ApiParameterMap;
import com.morpheusdata.model.Account;
import com.morpheusdata.model.AccountInvoice;
import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.User;
import com.morpheusdata.response.costing.CloudCostResponse;
import io.reactivex.Flowable;
import io.reactivex.Single;

import java.util.Date;
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
public interface MorpheusAccountInvoiceService {
	Flowable<AccountInvoice> listByApiParams(User user, ApiParameterMap<String,Object> parameters);

	Single<CloudCostResponse> loadCloudCost(Account account, Cloud cloud, Date startDate, Date endDate);
	Single<CloudCostResponse> loadCloudCost(Account account, Cloud cloud, Date startDate, Date endDate, Boolean byTenant);
	Single<CloudCostResponse> loadCloudCost(Account account, Cloud cloud, Date startDate, Date endDate, Boolean byTenant, Map additionalOptions);
}
