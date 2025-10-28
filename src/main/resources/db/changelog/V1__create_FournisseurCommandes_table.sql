
CREATE TABLE IF NOT EXISTS commande_fournisseur (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      fournisseur_id BIGINT NOT NULL,
                                      date_commande TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      statut ENUM('EN_ATTENTE', 'VALIDEE', 'LIVREE', 'ANNULEE') NOT NULL DEFAULT 'EN_ATTENTE',
                                      montant_total DOUBLE NOT NULL DEFAULT 0,
                                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                      CONSTRAINT fk_commande_fournisseur_fournisseur
                                          FOREIGN KEY (fournisseur_id)
                                              REFERENCES fournisseurs (id)
                                              ON DELETE RESTRICT
);