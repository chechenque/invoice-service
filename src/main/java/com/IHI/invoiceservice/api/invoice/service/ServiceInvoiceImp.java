package com.IHI.invoiceservice.api.invoice.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.IHI.invoiceservice.api.invoice.dto.DtoCustomer;
import com.IHI.invoiceservice.api.invoice.dto.DtoProduct;
import com.IHI.invoiceservice.api.invoice.entity.Invoice;
import com.IHI.invoiceservice.api.invoice.entity.InvoiceItem;
import com.IHI.invoiceservice.api.invoice.repository.InvoiceRepository;
import com.IHI.invoiceservice.exceptionHandling.ApiException;

@Service
public class ServiceInvoiceImp implements ServiceInvoice {

	@Autowired
	InvoiceRepository invoiceRepository;
	
	@Autowired
	RestTemplate restTemplate;
	
	
	@Override
	public List<Invoice> getInvoices() {
		try {
			List<Invoice> invoices = invoiceRepository.getInvoices();
			for(Invoice invoice : invoices) {
				setCostsInvoice(invoice);
			}
			return invoices;
		}catch(Exception e) {
			throw new ApiException(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
		}
	}

	@Override
	public Invoice getInvoice(Integer id) {
		try {
			Invoice invoice = ((Optional<Invoice>) invoiceRepository.findById(id)).get();
			setCostsInvoice(invoice);
			return invoice;
			
		}catch(Exception e) {
			throw new ApiException(HttpStatus.NOT_FOUND,e.getLocalizedMessage());
		}
	}

	@Override
	public void createInvoice(Invoice invoice) {
		try {
			getCustomer(invoice.getCustomerCode());
			for(InvoiceItem ii: invoice.getInvoiceItems()) {
				getProduct(ii.getProductCode());
			}
			invoice.setStatus(1);
			invoice.setCreatedAt(LocalDate.now());
			invoiceRepository.save(invoice);
			
		}catch(Exception e) {
			throw new ApiException(HttpStatus.NOT_FOUND,e.getLocalizedMessage());
		}
	}

	@Override
	public void deleteInvoice(Integer id) {
		try {
			getInvoice(id);
			invoiceRepository.deleteInvoice(id);
		}catch(Exception e) {
			throw new ApiException(HttpStatus.NOT_FOUND,e.getLocalizedMessage());
		}
	}
	
	private DtoCustomer getCustomer(Integer customerCode) {
		try {
			ResponseEntity<DtoCustomer> response = restTemplate.getForEntity(
					"http://localhost:8080/customer/"+customerCode,
					DtoCustomer.class);
			return response.getBody();
		}catch(Exception e) {
			throw new ApiException(HttpStatus.NOT_FOUND,"customer code "+ customerCode+" is not valid");
		}
	}

	
	private DtoProduct getProduct(String productCode){
		try {
			ResponseEntity<DtoProduct> response = restTemplate.getForEntity(
					"http://localhost:8081/product/"+productCode,
					DtoProduct.class);
			return response.getBody();
		}catch(Exception e) {
			throw new ApiException(HttpStatus.NOT_FOUND,"product code "+ productCode+" is not valid");
		}
	}
	
	private void setCostsInvoiceItem(InvoiceItem invoiceItem) {
		DtoProduct dp = getProduct(invoiceItem.getProductCode());
		invoiceItem.setUnitPrice(dp.getPrecio());
		invoiceItem.setSubtotal(dp.getPrecio()*invoiceItem.getQuantity());
		invoiceItem.setTotal(invoiceItem.getSubtotal()*(1+invoiceItem.getTaxPercentage()));
	}
	
	private void setCostsInvoice(Invoice invoice) {
		if(invoice.getStatus() != 1) {
			throw new ApiException(HttpStatus.BAD_REQUEST,"Cannot get invoice, it has been canceled");
		}
		
		Double subtotal = 0.0;
		Double taxes = 0.0;
		
		for(InvoiceItem ii: invoice.getInvoiceItems()) {
			setCostsInvoiceItem(ii);
			subtotal += ii.getSubtotal();
			taxes += ii.getSubtotal()*ii.getTaxPercentage();
		}
		
		invoice.setSubtotal(subtotal);
		invoice.setTaxes(taxes);
		invoice.setTotal(subtotal+taxes);
	}	
}
