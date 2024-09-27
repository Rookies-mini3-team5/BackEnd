CREATE TABLE `calendar` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `user_id` INT NOT NULL,
    `date` VARCHAR(50) NOT NULL,
    `memo` VARCHAR(100) NOT NULL,
    CONSTRAINT fk_user_id FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;






