package com.invoicer.invoice_service.Controllers;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.invoicer.invoice_service.Entity.Invoices;
import com.invoicer.invoice_service.Entity.InvoicesDetails;
import com.invoicer.invoice_service.Repository.InvoiceRepository;
import com.invoicer.invoice_service.Repository.InvoiceDetailRepository;
import org.springframework.web.client.RestTemplate;
import com.invoicer.invoice_service.Models.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceDetailRepository invoiceDetailRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String CUSTOMER_SERVICE_URL = "http://localhost:8081/customers/";
    private static final String PRODUCT_SERVICE_URL = "http://localhost:8082/products/";

    @PostMapping
    public ResponseEntity<?> createInvoice(@RequestBody InvoiceRequest invoiceRequest) {
        // Validar customer
        ResponseEntity<Map> customerResponse = restTemplate.getForEntity(CUSTOMER_SERVICE_URL + invoiceRequest.getCustomer_id(), Map.class);
        if (customerResponse.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer not found.");
        }

        BigDecimal total = BigDecimal.ZERO;
        for (InvoiceDetailRequest detail : invoiceRequest.getInvoiceDetails()) {
            ResponseEntity<Map> productResponse = restTemplate.getForEntity(PRODUCT_SERVICE_URL + detail.getProduct_id(), Map.class);
            if (productResponse.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product not found.");
            }

            BigDecimal price = new BigDecimal((String)productResponse.getBody().get("price")); // Asegúrate de parsear el cuerpo correctamente
            total = total.add(price.multiply(BigDecimal.valueOf(detail.getQuantity())));
        }

        Invoices invoice = new Invoices();
        invoice.setCustomer_id(invoiceRequest.getCustomer_id());
        invoice.setTotal(total);
        invoice.setStatus("Emitida"); 
        invoiceRepository.save(invoice);


        for (InvoiceDetailRequest detail : invoiceRequest.getInvoiceDetails()) {
            InvoicesDetails invoiceDetail = new InvoicesDetails();
            invoiceDetail.setInvoiceId(invoice.getId());
            invoiceDetail.setProduct_id(detail.getProduct_id());
            invoiceDetail.setQuantity(detail.getQuantity());
            invoiceDetail.setPrice(new BigDecimal((String)restTemplate.getForEntity(PRODUCT_SERVICE_URL + detail.getProduct_id(), Map.class).getBody().get("price"))); // Asegúrate de parsear el cuerpo correctamente
            invoiceDetailRepository.save(invoiceDetail);
        }

        return ResponseEntity.ok(invoice);
    }

    @GetMapping
    public ResponseEntity<List<Invoices>> getAllInvoices() {
        List<Invoices> invoices = invoiceRepository.findAll();
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInvoiceById(@PathVariable int id) {
        Optional<Invoices> invoice = invoiceRepository.findById(id);
        if (invoice.isPresent()) {
            return ResponseEntity.ok(invoice.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invoice not found.");
        }
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<?> getInvoiceDetailsById(@PathVariable int id) {
        // Buscar la factura por ID
        Optional<Invoices> invoiceOpt = invoiceRepository.findById(id);
        if (invoiceOpt.isPresent()) {
            // Encontrar los detalles de la factura
            List<InvoicesDetails> invoiceDetails = invoiceDetailRepository.findAllByInvoiceId(id);
            return ResponseEntity.ok(invoiceDetails);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invoice not found.");
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, String>> updateInvoiceStatus(@PathVariable int id, @RequestBody Map<String, String> updateRequest) {
        Optional<Invoices> invoiceOpt = invoiceRepository.findById(id);
        if (invoiceOpt.isPresent()) {
            Invoices invoice = invoiceOpt.get();
            String newStatus = updateRequest.get("status");
            if (newStatus != null && (newStatus.equals("Emitida") || newStatus.equals("Anulada"))) {
                invoice.setStatus(newStatus);
                invoiceRepository.save(invoice);
                Map<String, String> response = new HashMap<>();
                response.put("message", "Invoice status updated successfully.");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Invalid status value.");
                return ResponseEntity.badRequest().body(response);
            }
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Invoice not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    @GetMapping("/{id}/full")
    public ResponseEntity<?> getInvoiceFullDetails(@PathVariable int id) {
        Optional<Invoices> invoiceOpt = invoiceRepository.findById(id);
        if (!invoiceOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invoice not found.");
        }

        Invoices invoice = invoiceOpt.get();
        List<InvoicesDetails> invoiceDetails = invoiceDetailRepository.findAllByInvoiceId(id);

        // Obtener detalles del cliente
        ResponseEntity<Map> customerResponse = restTemplate.getForEntity(CUSTOMER_SERVICE_URL + invoice.getCustomer_id(), Map.class);
        if (customerResponse.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer not found.");
        }
        Map<String, Object> customerData = customerResponse.getBody();

        // Obtener detalles de productos
        List<Map<String, Object>> productDetails = invoiceDetails.stream().map(detail -> {
            ResponseEntity<Map> productResponse = restTemplate.getForEntity(PRODUCT_SERVICE_URL + detail.getProduct_id(), Map.class);
            return productResponse.getBody();
        }).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("invoice", invoice);
        response.put("customer", customerData);
        response.put("details", invoiceDetails);
        response.put("products", productDetails);

        return ResponseEntity.ok(response);
    }

}
