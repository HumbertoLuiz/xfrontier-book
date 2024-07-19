CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `complete_name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `register_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_type` varchar(11) NOT NULL,
  `enabled` bit NOT NULL,
  `person_id` bigint NULL,
  `document_picture` bigint NULL,
  `user_picture` bigint NULL,
  UNIQUE KEY `uk_email` (`email`),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_user_person` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`),
  CONSTRAINT `fk_user_document_picture` FOREIGN KEY (`document_picture`) REFERENCES `picture` (`id`),
  CONSTRAINT `fk_user_user_picture` FOREIGN KEY (`user_picture`) REFERENCES `picture` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;