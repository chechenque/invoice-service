package com.IHI.invoiceservice.api.invoice.service;

import java.util.List;

import com.IHI.invoiceservice.api.invoice.entity.Invoice;

public interface ServiceInvoice {

	public List<Invoice> getInvoices();
	
	public Invoice getInvoice(Integer id);
	
	public void createInvoice(Invoice invoice);
	
	public void deleteInvoice(Integer id);
}
