CREATE TABLE IF NOT EXISTS `token_black_list` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `token` varchar(512) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;