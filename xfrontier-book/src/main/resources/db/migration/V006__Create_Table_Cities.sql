CREATE TABLE IF NOT EXISTS `cities` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `ibge_code` varchar(20) NULL,
  `state_id` bigint NOT NULL,
  UNIQUE KEY `uk_ibge_code` (`ibge_code`),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_city_state` FOREIGN KEY (`state_id`) REFERENCES `states` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;