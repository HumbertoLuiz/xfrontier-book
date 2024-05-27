CREATE TABLE IF NOT EXISTS `product_image` (
  `book_id` bigint NOT NULL,
  `file_name` varchar(150) NOT NULL,
  `description` varchar(150),
  `content_type` varchar(80) NOT NULL,
  `size` int NOT NULL,
  PRIMARY KEY (`book_id`),
  CONSTRAINT `fk_book_image_book` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;