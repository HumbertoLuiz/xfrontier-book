CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `complete_name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `register_date` datetime NOT NULL,
  `user_type` varchar(11) NOT NULL,
  `enabled` bit NOT NULL,
  `document_picture` bigint,
  `user_picture` bigint,
  UNIQUE KEY `uk_email` (`email`),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_user_document_picture` FOREIGN KEY (`document_picture`) REFERENCES `picture` (`id`),
  CONSTRAINT `fk_user_user_picture` FOREIGN KEY (`user_picture`) REFERENCES `picture` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

