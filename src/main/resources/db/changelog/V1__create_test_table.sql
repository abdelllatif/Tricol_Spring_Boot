CREATE TABLE IF NOT EXISTS test(
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  commande_id BIGINT NOT NULL
)
--rollback DROP TABLE test