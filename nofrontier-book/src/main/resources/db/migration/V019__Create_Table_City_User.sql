CREATE TABLE IF NOT EXISTS `city_user` (
  `user_id` bigint NOT NULL,
  `city_id` bigint NOT NULL,
  KEY (`user_id`),
  KEY (`city_id`),
  CONSTRAINT FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT FOREIGN KEY (`city_id`) REFERENCES `cities` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;