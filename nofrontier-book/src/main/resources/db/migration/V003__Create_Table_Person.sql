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
  `enabled` bit NOT NULL,
  UNIQUE KEY `cpf` (`cpf`),
  UNIQUE KEY `key_pix` (`key_pix`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
