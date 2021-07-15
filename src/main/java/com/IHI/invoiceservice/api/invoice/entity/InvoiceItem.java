package com.IHI.invoiceservice.api.invoice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
public class InvoiceItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("id")
	@Column(name = "id_item")
	private Integer idItem;
	
	@JsonProperty("id_invoice")
	@Column(name = "id_invoice")
	private Integer idInvoice;
	
	@JsonProperty("quantity")
	@Column(name = "quantity")
	@NotNull(message = "quantity is required")
	@Min(value=1, message = "quantity must be greater than 0")
	private Integer quantity;
	
	@JsonProperty("tax_percentage")
	@Column(name = "tax_percentage")
	@NotNull(message = "tax_percentage is required")
	private Integer taxPercentage;
	
	@JsonProperty("subtotal")
	@Transient
	private Double subtotal;
	
	@JsonProperty("unit_price")
	@Transient
	private Double unitPrice;
	
	@JsonProperty("total")
	@Transient
	private Double total;
	
	@JsonProperty("product_code")
	@Column(name = "product_code")
	@NotNull(message = "product code is required")
	@Pattern(regexp = "[a-zA-Z0-9]*", message = "product code can only contain alphanumeric characters")
	private String productCode;
}
