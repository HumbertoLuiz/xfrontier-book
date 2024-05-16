CREATE TABLE IF NOT EXISTS `order_payment_method` (
	`order_id` bigint NOT NULL,
	`payment_method_id` bigint NOT NULL,	
	PRIMARY KEY (`order_id`, `payment_method_id`),
	CONSTRAINT `fk_payment_method_order` FOREIGN KEY (`order_id`) REFERENCES `order_entity` (`id`),
	CONSTRAINT `fk_payment_method_payment_method` FOREIGN KEY (`payment_method_id`) REFERENCES `payment_method` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;