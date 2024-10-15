CREATE TABLE IF NOT EXISTS `order_entity` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(36) NOT NULL UNIQUE,
  `subtotal` decimal(10,2) NOT NULL,
  `shipping_rate` decimal(10,2) NOT NULL,
  `total_value` decimal(10,2) NOT NULL,
  `order_status` varchar(10) NOT NULL,
  `creation_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `confirmation_date` datetime,
  `cancellation_date` datetime,
  `delivery_date` datetime,
  `payment_method_id` bigint NOT NULL,
  `user_customer_id` bigint NOT NULL,
  `shipping_address_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_order_payment_method` FOREIGN KEY (`payment_method_id`) REFERENCES `payment_method` (`id`),
  CONSTRAINT `fk_order_user_customer` FOREIGN KEY (`user_customer_id`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_order_shipping_address` FOREIGN KEY (`shipping_address_id`) REFERENCES `address` (`id`),
  CONSTRAINT `uk_order_code` UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*
CREATE TRIGGER before_insert_order_entity
BEFORE INSERT ON order_entity
FOR EACH ROW
SET NEW.code = UUID();
*/