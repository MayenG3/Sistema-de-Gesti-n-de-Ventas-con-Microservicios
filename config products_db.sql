create database product_db
use product_db

CREATE TABLE product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    price DECIMAL(10, 2) NOT NULL,
    stock INT DEFAULT 0,
    status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


INSERT INTO product (name, description, price, stock, status)
VALUES 
('Laptop', 'Laptop con pantalla de 15.6 pulgadas, procesador Intel Core i7', 1200.00, 10, 'Active'),
('Smartphone', 'Smartphone con pantalla de 6.5 pulgadas y cámara de 48MP', 600.00, 25, 'Active'),
('Mouse inalámbrico', 'Mouse inalámbrico con diseño ergonómico', 25.00, 100, 'Active'),
('Teclado mecánico', 'Teclado mecánico con retroiluminación RGB', 80.00, 50, 'Active'),
('Monitor 24 pulgadas', 'Monitor Full HD de 24 pulgadas', 200.00, 15, 'Active');

select * from product