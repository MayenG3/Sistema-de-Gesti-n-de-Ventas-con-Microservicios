package com.invoicer.invoice_service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.invoicer.invoice_service.Entity.*;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoices, Integer> {
	
}

