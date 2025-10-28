

CREATE TABLE IF NOT EXISTS commande_produit (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  commande_id BIGINT NOT NULL,
                                  produit_id BIGINT NOT NULL,
                                  quantite INT NOT NULL,
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  CONSTRAINT fk_commande_produit_commande
                                      FOREIGN KEY (commande_id)
                                          REFERENCES commande_fournisseur (id)
                                          ON DELETE CASCADE,
                                  CONSTRAINT fk_commande_produit_produit
                                      FOREIGN KEY (produit_id)
                                          REFERENCES produits (id)
                                          ON DELETE RESTRICT,
                                  CONSTRAINT uq_commande_produit UNIQUE (commande_id, produit_id)
);