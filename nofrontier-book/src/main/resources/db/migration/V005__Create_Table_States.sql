CREATE TABLE IF NOT EXISTS `states` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `ibge_code` varchar(20) NULL,
  `country_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_ibge_code` (`ibge_code`),
  CONSTRAINT `fk_states_countries` FOREIGN KEY (`country_id`) REFERENCES `countries` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
