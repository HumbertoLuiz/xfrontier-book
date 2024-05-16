CREATE TABLE IF NOT EXISTS `password_reset` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `token` varchar(255) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;