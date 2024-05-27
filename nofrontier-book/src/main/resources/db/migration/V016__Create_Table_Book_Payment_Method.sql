CREATE TABLE IF NOT EXISTS `book_payment_method` (
	`book_id` bigint NOT NULL,
	`payment_method_id` bigint NOT NULL,	
	PRIMARY KEY (`book_id`, `payment_method_id`),
	CONSTRAINT `fk_payment_method_book` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`),
	CONSTRAINT `fk_payment_method_payment_method` FOREIGN KEY (`payment_method_id`) REFERENCES `payment_method` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;