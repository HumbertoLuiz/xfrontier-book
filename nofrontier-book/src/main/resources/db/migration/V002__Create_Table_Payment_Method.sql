CREATE TABLE IF NOT EXISTS `payment_method` (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`description` varchar(60) NOT NULL,
	`update_date` datetime NOT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;