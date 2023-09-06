package com.morpheusdata.core.costing;

/**
 * Provides access to services related to Costing data and Invoice Data
 * @author David Estes
 *
 */
public interface MorpheusCostingService {

	MorpheusAccountInvoiceService getInvoice();

	MorpheusAccountInvoiceItemService getInvoiceItem();
}
