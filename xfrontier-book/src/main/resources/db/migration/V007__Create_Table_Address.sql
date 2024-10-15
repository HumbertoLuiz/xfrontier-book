CREATE TABLE IF NOT EXISTS `address` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `street` varchar(60) NOT NULL,
  `number` varchar(10) NOT NULL,
  `neighborhood` varchar(30) NOT NULL,
  `complement` varchar(255) DEFAULT NULL,
  `zip_code` varchar(9) NOT NULL,
  `address_type` varchar(11) NOT NULL,
  `city_id` bigint NOT NULL,
  `person_id` bigint NULL,
  UNIQUE KEY `uk_zip_code` (`zip_code`),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_address_person` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`),
  CONSTRAINT `fk_address_city` FOREIGN KEY (`city_id`) REFERENCES `cities` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

