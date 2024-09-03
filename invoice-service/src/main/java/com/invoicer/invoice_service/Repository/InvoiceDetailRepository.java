package com.invoicer.invoice_service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.invoicer.invoice_service.Entity.InvoicesDetails;
import java.util.List;

@Repository
public interface InvoiceDetailRepository extends JpaRepository<InvoicesDetails, Integer> {
    // Método para encontrar detalles de factura por ID de factura
    List<InvoicesDetails> findAllByInvoiceId(int invoiceId);
}
