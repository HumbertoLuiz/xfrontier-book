CREATE TABLE IF NOT EXISTS `order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `unit_price` decimal(10,2) NOT NULL,
  `total_price` decimal(10,2) NOT NULL,
  `quantity` smallint NOT NULL,
  `observation` varchar(255) NULL,
  `order_id` bigint NOT NULL,
  `book_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,  
  PRIMARY KEY (id),
  UNIQUE KEY `uk_order_item_book` (`order_id`, `book_id`, `product_id`),
  CONSTRAINT `fk_order_item_order` FOREIGN KEY (`order_id`) REFERENCES `order_entity` (`id`),
  CONSTRAINT `fk_order_item_book` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`),
  CONSTRAINT `fk_order_item_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;