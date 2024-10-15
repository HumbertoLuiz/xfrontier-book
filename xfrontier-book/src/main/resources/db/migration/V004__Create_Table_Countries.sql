CREATE TABLE IF NOT EXISTS `countries` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `initials` varchar(5) NOT NULL,
  UNIQUE KEY `uk_initials` (`initials`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;