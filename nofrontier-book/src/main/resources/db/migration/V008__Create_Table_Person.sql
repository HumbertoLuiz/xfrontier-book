CREATE TABLE IF NOT EXISTS `person` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(80) NOT NULL,
  `last_name` varchar(80) NOT NULL,
  `gender` varchar(6) NOT NULL,
  `cpf` varchar(11) DEFAULT NULL,
  `birth` date DEFAULT NULL,
  `phone_number` varchar(15) DEFAULT NULL,
  `mobile_number` varchar(15) DEFAULT NULL,
  `key_pix` varchar(255) DEFAULT NULL,
  `enabled` BIT(1) NOT NULL DEFAULT b'1',
  `user_id` bigint,
  `address_id` bigint,
  UNIQUE KEY `cpf` (`cpf`),
  UNIQUE KEY `uk_key_pix` (`key_pix`),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_person_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_address_users` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
