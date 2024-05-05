CREATE TABLE IF NOT EXISTS `person` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(80) NOT NULL,
  `last_name` varchar(80) NOT NULL,
  `gender` varchar(6) NOT NULL,
  `cpf` varchar(11) DEFAULT NULL,
  `birth` date DEFAULT NULL,
  `phone_number` varchar(11) DEFAULT NULL,
  `key_pix` varchar(255) DEFAULT NULL,
  `enabled` BIT(1) NOT NULL DEFAULT b'1',
  `user_id` bigint(20),
  `address_id` bigint(20),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;