CREATE TABLE IF NOT EXISTS produits (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          nom VARCHAR(255) NOT NULL,
                          description TEXT,
                          prix_unitaire DOUBLE NOT NULL,
                          stock_actuel INT NOT NULL DEFAULT 0,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);