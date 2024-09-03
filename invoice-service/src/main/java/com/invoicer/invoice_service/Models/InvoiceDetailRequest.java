package com.invoicer.invoice_service.Models;

import lombok.Data;

@Data
public class InvoiceDetailRequest {
    private int product_id;
    private int quantity;

}
