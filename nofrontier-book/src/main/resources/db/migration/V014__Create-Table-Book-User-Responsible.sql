CREATE TABLE IF NOT EXISTS book_user_responsible (
  `book_id` bigint not null,
  `user_id` bigint not null,
  PRIMARY KEY (`book_id`, `user_id`),
  CONSTRAINT fk_book_user_book FOREIGN KEY (book_id) REFERENCES books (id),
  CONSTRAINT fk_book_user_user FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




