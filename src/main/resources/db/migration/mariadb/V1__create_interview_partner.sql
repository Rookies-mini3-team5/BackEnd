CREATE TABLE `user` (
    `id` integer PRIMARY KEY AUTO_INCREMENT,
    `username` varchar(50) UNIQUE NOT NULL,
    `name` varchar(50) NOT NULL,
    `password` varchar(100) NOT NULL,
    `email` varchar(100) NOT NULL ,
    `role` varchar(10) NOT NULL,
    `updated_at` datetime,
    `created_at` datetime NOT NULL
)engine=InnoDB;

CREATE TABLE `user_picture` (
    `id` integer PRIMARY KEY AUTO_INCREMENT,
    `user_id` integer NOT NULL,
    `original_file_name` varchar(100) NOT NULL,
    `file_path` varchar(200) NOT NULL,
    `file_size` varchar(45) NOT NULL,
    `created_at` datetime NOT NULL,
    `updated_at` datetime
)engine=InnoDB;

CREATE TABLE `section` (
    `id` integer PRIMARY KEY AUTO_INCREMENT,
    `user_id` integer NOT NULL,
    `job_id` integer NOT NULL,
    `occupational_id` integer NOT NULL,
    `name` varchar(100),
    `resume` text,
    `emphasize` text,
    `created_at` datetime NOT NULL,
    `updated_at` datetime NOT NULL
)engine=InnoDB;

CREATE TABLE `gpt_question` (
    `id` integer PRIMARY KEY AUTO_INCREMENT,
    `section_id` integer NOT NULL,
    `question` text NOT NULL,
    `answer_guide` text NOT NULL
)engine=InnoDB;

CREATE TABLE `interview_answer` (
    `id` integer PRIMARY KEY AUTO_INCREMENT,
    `gpt_question_id` integer NOT NULL,
    `answer` text NOT NULL,
    `feedback` text
)engine=InnoDB;

CREATE TABLE `user_question` (
    `id` integer PRIMARY KEY AUTO_INCREMENT,
    `section_id` integer NOT NULL,
    `question` text NOT NULL,
    `answer` text
)engine=InnoDB;

CREATE TABLE `occupational` (
    `id` integer PRIMARY KEY AUTO_INCREMENT,
    `occupational_name` varchar(50) NOT NULL
)engine=InnoDB;

CREATE TABLE `job` (
    `id` integer PRIMARY KEY AUTO_INCREMENT,
    `occupational_id` integer NOT NULL,
    `job_name` varchar(50) NOT NULL
)engine=InnoDB;

ALTER TABLE `section` ADD FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)  ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `user_picture` ADD FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)  ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `gpt_question` ADD FOREIGN KEY (`section_id`) REFERENCES `section` (`id`)  ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `interview_answer` ADD FOREIGN KEY (`gpt_question_id`) REFERENCES `gpt_question` (`id`)  ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `user_question` ADD FOREIGN KEY (`section_id`) REFERENCES `section` (`id`)  ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `job` ADD FOREIGN KEY (`occupational_id`) REFERENCES `occupational` (`id`)  ON DELETE CASCADE ON UPDATE CASCADE;





