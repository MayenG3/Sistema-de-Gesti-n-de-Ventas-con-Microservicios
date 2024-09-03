package com.invoicer.product_service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.invoicer.product_service.Entity.Products;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer>{

}
