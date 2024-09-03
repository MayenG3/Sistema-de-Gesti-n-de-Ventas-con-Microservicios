package com.invoicer.product_service.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoicer.product_service.Entity.Products;
import com.invoicer.product_service.Repository.ProductRepository;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/products")
public class ProductController {
	 @Autowired
	    private ProductRepository productRepository;

	    @GetMapping
	    public List<Products> getProducts() {
	        return productRepository.findAll();
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<Map<String, String>> getProductById(@PathVariable int id) {
	        return productRepository.findById(id)
	            .map(product -> ResponseEntity.ok(Map.of(
	                "id", String.valueOf(product.getId()), 
	                "name", product.getName(), 
	                "description", product.getDescription(), 
	                "price", product.getPrice().toString(),  
	                "stock", String.valueOf(product.getStock()), 
	                "status", product.getStatus())))
	            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                           .body(Map.of("error", "El producto con el ID " + id + " no existe.")));
	    }
	    
	    @PostMapping
	    public ResponseEntity<?> save(@RequestBody Products product) {
	        try {
	            if (product.getName() == null || product.getPrice() == null) {
	                return ResponseEntity.badRequest().body("Nombre y precio son obligatorios");
	            }
	            Products savedProduct = productRepository.save(product);
	            return ResponseEntity.ok(savedProduct);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el producto: " + e.getMessage());
	        }
	    }

	    @PutMapping
	    public ResponseEntity<Map<String, String>> update(@RequestBody Products product) {
	        if (!productRepository.existsById(product.getId())) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                 .body(Map.of("error", "El Producto con el ID proporcionado no existe."));
	        }
	        try {
	            Products updatedProduct = productRepository.save(product);
	            return ResponseEntity.ok(Map.of("message", "Producto actualizado con éxito.", "id", String.valueOf(updatedProduct.getId())));
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                                 .body(Map.of("error", "Error al actualizar el producto."));
	        }
	    }
	    
	    @PutMapping("/{id}/reduce-stock")
	    public ResponseEntity<?> reduceStock(@PathVariable int id, @RequestBody Map<String, Integer> request) {
	        return productRepository.findById(id).map(product -> {
	            int quantity = request.get("quantity");
	            if (product.getStock() < quantity) {
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                                     .body(Map.of("error", "No hay suficiente stock para reducir la cantidad solicitada."));
	            }
	            product.setStock(product.getStock() - quantity);
	            productRepository.save(product);
	            return ResponseEntity.ok(Map.of("message", "Stock reducido con éxito.", "new_stock", String.valueOf(product.getStock())));
	        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                          .body(Map.of("error", "El producto con el ID " + id + " no existe.")));
	    }

}
