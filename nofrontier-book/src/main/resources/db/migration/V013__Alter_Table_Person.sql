ALTER TABLE `person`
	ADD CONSTRAINT `fk_person_users` FOREIGN KEY (`user_id`) REFERENCES `users` (id);