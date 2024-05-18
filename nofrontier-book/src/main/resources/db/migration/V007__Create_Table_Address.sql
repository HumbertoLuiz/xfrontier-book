CREATE TABLE IF NOT EXISTS `address` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `street` varchar(60) NOT NULL,
  `number` varchar(10) NOT NULL,
  `neighborhood` varchar(30) NOT NULL,
  `complement` varchar(255) DEFAULT NULL,
  `zip_code` varchar(9) NOT NULL,
  `address_type` varchar(11) NOT NULL,
  `city_id` bigint NOT NULL,
  UNIQUE KEY `uk_zip_code` (`zip_code`),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_address_city` FOREIGN KEY (`city_id`) REFERENCES `cities` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `users`
  ADD `address_id` bigint DEFAULT NULL,
  ADD KEY (`address_id`),
  ADD CONSTRAINT FOREIGN KEY (`address_id`) REFERENCES `address` (`id`);
