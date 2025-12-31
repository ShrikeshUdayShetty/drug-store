-- Drug Store Web Application Schema
-- Execute in MySQL Workbench to bootstrap the database

DROP DATABASE IF EXISTS drugstore;
CREATE DATABASE drugstore CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE drugstore;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE vendors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    store_name VARCHAR(160) NOT NULL,
    contact_person VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE medicines (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vendor_id INT NOT NULL,
    name VARCHAR(160) NOT NULL,
    description TEXT,
    manufacturing_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    price_per_unit DECIMAL(10,2) NOT NULL,
    stock_quantity INT NOT NULL,
    image_url VARCHAR(255),
    discount_percentage DECIMAL(5,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_medicine_vendor FOREIGN KEY (vendor_id) REFERENCES vendors(id) ON DELETE CASCADE
);
CREATE INDEX idx_medicine_name ON medicines(name);

CREATE TABLE cart_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    medicine_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_medicine FOREIGN KEY (medicine_id) REFERENCES medicines(id) ON DELETE CASCADE,
    CONSTRAINT uq_cart UNIQUE (user_id, medicine_id)
);

CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    discount_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    net_amount DECIMAL(10,2) NOT NULL,
    status ENUM('PLACED','CONFIRMED','SHIPPED','DELIVERED','CANCELLED') DEFAULT 'PLACED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    medicine_id INT NOT NULL,
    vendor_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    discount_percentage DECIMAL(5,2) DEFAULT 0.00,
    CONSTRAINT fk_orderitems_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_orderitems_medicine FOREIGN KEY (medicine_id) REFERENCES medicines(id) ON DELETE CASCADE,
    CONSTRAINT fk_orderitems_vendor FOREIGN KEY (vendor_id) REFERENCES vendors(id) ON DELETE CASCADE
);

-- Seed vendor and user accounts for quick testing
INSERT INTO vendors (store_name, contact_person, email, password_hash, phone)
VALUES
('MediSupply Hub', 'Anita Rao', 'vendor@example.com',
 '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', '9876543210');

INSERT INTO users (full_name, email, password_hash, phone, address)
VALUES
('Rohan Patil', 'user@example.com',
 '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8',
 '9123456780', 'MG Road, Bengaluru');

INSERT INTO medicines (vendor_id, name, description, manufacturing_date, expiry_date, price_per_unit,
                       stock_quantity, image_url, discount_percentage)
VALUES
(1, 'Paracetamol 500mg', 'Fast relief from fever and mild pain', '2024-01-15', '2026-01-14', 45.00, 200,
 'https://images.unsplash.com/photo-1580281657521-389f517911ef', 5.00),
(1, 'CoughEase Syrup', 'Soothing syrup for dry cough', '2024-03-10', '2025-09-09', 120.00, 150,
 'https://images.unsplash.com/photo-1580281658629-acf6f5f1df59', 12.00),
(1, 'Vitamin D3 Tablets', 'Daily supplement for bone health', '2023-12-01', '2025-11-30', 299.00, 80,
 'https://images.unsplash.com/photo-1580281657601-322b94f99562', 8.50);
