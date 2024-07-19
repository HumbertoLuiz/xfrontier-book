CREATE TABLE IF NOT EXISTS `product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` TEXT NOT NULL,
  `format` varchar(255) NOT NULL,
  `edition` varchar(255) NOT NULL,
  `price` decimal(10, 2) NOT NULL,
  `active` BIT(1) NOT NULL DEFAULT b'1',
  `book_id` bigint,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_product_book` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

