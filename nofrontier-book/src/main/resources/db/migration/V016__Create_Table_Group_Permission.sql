CREATE TABLE IF NOT EXISTS `group_permission` (
	`group_id` bigint NOT NULL,
	`permission_id` bigint NOT NULL,	
	PRIMARY KEY (`group_id`, `permission_id`),
	CONSTRAINT `fk_group_permission_permission` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`),
	CONSTRAINT `fk_group_permission_group` FOREIGN KEY (`group_id`) REFERENCES `group_entity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

