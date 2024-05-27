CREATE TABLE IF NOT EXISTS `order_book` (
	`order_id` bigint NOT NULL,
	`book_id` bigint NOT NULL,	
	PRIMARY KEY (`order_id`, `book_id`),
	CONSTRAINT `fk_order_book` FOREIGN KEY (`order_id`) REFERENCES `order_entity` (`id`),
	CONSTRAINT `fk_book_order` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;