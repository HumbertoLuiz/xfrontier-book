CREATE TABLE `address` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `street` varchar(60) NOT NULL,
  `numero` varchar(10) NOT NULL,
  `neighborhood` varchar(30) NOT NULL,
  `complement` varchar(255) DEFAULT NULL,
  `zipCode` varchar(8) NOT NULL,
  `city` varchar(30) NOT NULL,
  `state` varchar(2) NOT NULL,
  PRIMARY KEY (`id`)
);

ALTER TABLE `person`
  ADD `address_id` bigint DEFAULT NULL,
  ADD UNIQUE KEY (`cpf`),
  ADD UNIQUE KEY (`key_pix`),
  ADD KEY (`address_id`),
  ADD CONSTRAINT FOREIGN KEY (`address_id`) REFERENCES `address` (`id`);