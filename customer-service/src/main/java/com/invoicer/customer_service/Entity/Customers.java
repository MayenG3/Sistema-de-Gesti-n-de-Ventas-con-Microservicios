package com.invoicer.customer_service.Entity;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "customer")

public class Customers {
	
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id")
	    private int id;
	    private String name;
	    private String email;
	    private String phone;
	    private String address;
	    private String status;
	    @Column(name = "created_at", updatable = false, nullable = false)
	    private LocalDateTime createdAt = LocalDateTime.now();

	    @Column(name = "updated_at", nullable = false)
	    private LocalDateTime updatedAt = LocalDateTime.now();

	    @PrePersist
	    protected void onCreate() {
	        createdAt = LocalDateTime.now();
	        updatedAt = LocalDateTime.now();
	    }

	    @PreUpdate
	    protected void onUpdate() {
	        updatedAt = LocalDateTime.now();
	    }
	    
}
