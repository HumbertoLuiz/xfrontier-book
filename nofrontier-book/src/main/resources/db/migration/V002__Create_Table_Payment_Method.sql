CREATE TABLE IF NOT EXISTS `payment_method` (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`created_at` datetime(6) NOT NULL,
  	`updated_at` datetime(6) NOT NULL,
	`description` varchar(60) NOT NULL,
	`status` varchar(11) NOT NULL,
	`transaction_id` varchar(255) NOT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;