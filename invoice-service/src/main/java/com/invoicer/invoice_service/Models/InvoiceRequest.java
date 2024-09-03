package com.invoicer.invoice_service.Models;

import java.util.List;

import lombok.Data;

@Data
public class InvoiceRequest {
    private int customer_id;
    private List<InvoiceDetailRequest> invoiceDetails;
}
