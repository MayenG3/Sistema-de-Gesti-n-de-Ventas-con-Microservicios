package com.invoicer.customer_service.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.invoicer.customer_service.Entity.*;
import com.invoicer.customer_service.Repository.*;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public List<Customers> getCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> getCustomerById(@PathVariable int id) {
        return customerRepository.findById(id)
            .map(customer -> ResponseEntity.ok(Map.of("id", String.valueOf(customer.getId()), "name", customer.getName(), "email", customer.getEmail(), "phone", customer.getPhone(), "address", customer.getAddress(), "status", customer.getStatus())))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                                           .body(Map.of("error", "El cliente con el ID " + id + " no existe.")));
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Customers customer) {
        try {
            if (customer.getName() == null || customer.getEmail() == null) {
                return ResponseEntity.badRequest().body("Nombre y email son obligatorios");
            }
            
            Customers savedCustomer = customerRepository.save(customer);
            return ResponseEntity.ok(savedCustomer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el cliente: " + e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<Map<String, String>> update(@RequestBody Customers customer) {
        if (!customerRepository.existsById(customer.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Map.of("error", "El cliente con el ID proporcionado no existe."));
        }
        try {
            Customers updatedCustomer = customerRepository.save(customer);
            return ResponseEntity.ok(Map.of("message", "Cliente actualizado con éxito.", "id", String.valueOf(updatedCustomer.getId())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", "Error al actualizar el cliente."));
        }
    }
}
