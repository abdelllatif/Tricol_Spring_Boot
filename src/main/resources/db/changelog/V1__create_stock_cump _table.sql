CREATE TABLE IF NOT EXISTS stock_cump (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            produit_id BIGINT NOT NULL,
                            cout_unitaire_cump DOUBLE NOT NULL,
                            CONSTRAINT fk_stockcump_produit FOREIGN KEY (produit_id) REFERENCES produits(id)
);
