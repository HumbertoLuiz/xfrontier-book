ALTER TABLE `users`
  ADD `document_picture` bigint DEFAULT NULL,
  ADD `user_picture` bigint DEFAULT NULL,
  ADD FOREIGN KEY (`user_picture`) REFERENCES `picture` (`id`),
  ADD FOREIGN KEY (`document_picture`) REFERENCES `picture` (`id`);