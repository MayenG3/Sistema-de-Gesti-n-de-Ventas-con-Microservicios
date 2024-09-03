CREATE DATABASE customer_db;

USE customer_db

CREATE TABLE customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(500),         -- Usar VARCHAR si la longitud de la dirección no es excesiva
    status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO customer (name, email, phone, address, status)
VALUES 
('Juan Pérez', 'juan.perez@gmail.com', '38237232', 'Avenida La Reforma, Ciudad de Guatemala', 'Active'),
('María López', 'maria.lopez@gmail.com', '45679872', 'Calle 10, Zona 1, Ciudad de Guatemala', 'Inactive'),
('Carlos Martínez', 'carlos.martinez@gmail.com', '50873452', 'Boulevard Liberación, Ciudad de Guatemala', 'Active'),
('Ana Gómez', 'ana.gomez@gmail.com', '34567890', 'Avenida Las Américas, Ciudad de Guatemala', 'Active'),
('Luis Fernández', 'luis.fernandez@gmail.com', '32546723', 'Calle de la Independencia, Ciudad de Guatemala', 'Inactive');


drop table customer


select * from customer