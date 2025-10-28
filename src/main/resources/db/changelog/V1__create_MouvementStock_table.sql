


CREATE TABLE IF NOT EXISTS mouvement_stock (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 produit_id BIGINT NOT NULL,
                                 commande_id BIGINT,
                                 type_mouvement VARCHAR(20) NOT NULL,
                                 quantite INT NOT NULL,
                                 date_mouvement TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 cout_unitaire DOUBLE,
                                 remarque VARCHAR(500),
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 CONSTRAINT fk_mouvement_produit
                                     FOREIGN KEY (produit_id)
                                         REFERENCES produits (id)
                                         ON DELETE RESTRICT,
                                 CONSTRAINT fk_mouvement_commande
                                     FOREIGN KEY (commande_id)
                                         REFERENCES commande_fournisseur (id)
                                         ON DELETE SET NULL
);
