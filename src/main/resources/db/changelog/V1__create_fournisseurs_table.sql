-- V1__create_fournisseurs_table.sql
CREATE TABLE IF NOT EXISTS fournisseurs (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              societe VARCHAR(255) NOT NULL,
                              adresse VARCHAR(255) NOT NULL,
                              contact VARCHAR(255) NOT NULL,
                              email VARCHAR(255) NOT NULL UNIQUE,
                              telephone VARCHAR(50) NOT NULL,
                              ville VARCHAR(100),
                              ice VARCHAR(50)
);
