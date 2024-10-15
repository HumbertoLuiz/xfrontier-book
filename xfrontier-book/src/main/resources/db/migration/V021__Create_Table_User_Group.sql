CREATE TABLE IF NOT EXISTS `user_group` (
	`user_id` bigint NOT NULL,
	`group_id` bigint NOT NULL,
	PRIMARY KEY (`user_id`, `group_id`),
	CONSTRAINT `fk_user_group_group` FOREIGN KEY (`group_id`) REFERENCES `group_entity` (`id`),
	CONSTRAINT `fk_user_group_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

