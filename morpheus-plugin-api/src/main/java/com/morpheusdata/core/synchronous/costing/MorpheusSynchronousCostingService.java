package com.morpheusdata.core.synchronous.costing;

public interface MorpheusSynchronousCostingService {

	MorpheusSynchronousAccountInvoiceService getInvoice();

	MorpheusSynchronousAccountInvoiceItemService getInvoiceItem();
}
