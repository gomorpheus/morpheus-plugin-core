package com.morpheusdata.core.costing;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.AccountInvoiceItem;

/**
 * Provides access to querying line items on an invoice. Line items represent individual rows or costs that
 * are associated with a larger invoice. This could be a storage cost, a compute cost, etc. This is, for the most part,
 * a standard {@link MorpheusDataService}.
 *
 * @author David Estes
 */
public interface MorpheusAccountInvoiceItemService extends MorpheusDataService<AccountInvoiceItem,AccountInvoiceItem> {
}
