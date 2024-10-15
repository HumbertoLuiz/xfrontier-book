CREATE TABLE IF NOT EXISTS `rating` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  `score` double NOT NULL,
  `visibility` bit(1) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `rated_id` bigint NOT NULL,
  `rater_id` bigint DEFAULT NULL,  
  PRIMARY KEY (`id`),
  FOREIGN KEY (`rated_id`) REFERENCES `users` (`id`),
  FOREIGN KEY (`rater_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;