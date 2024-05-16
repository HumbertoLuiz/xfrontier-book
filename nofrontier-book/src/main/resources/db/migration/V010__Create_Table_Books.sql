CREATE TABLE IF NOT EXISTS `books` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(250) NOT NULL,
  `author` varchar(180) NOT NULL,
  `isbn` varchar(13) NOT NULL,
  `price` decimal(10, 2) NOT NULL,
  `launch_date` date NOT NULL,
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified` datetime,
  `created_by` int NOT NULL,
  `last_modified_by` int,
  `active` tinyint NOT NULL DEFAULT 1,
  `order_id` bigint,
  UNIQUE KEY `uk_isbn` (`isbn`),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_books_order` FOREIGN KEY (`order_id`) REFERENCES `order_entity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;