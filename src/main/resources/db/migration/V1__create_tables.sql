CREATE TABLE IF NOT EXISTS `users` (
    `id` VARCHAR(50) NOT NULL,
    `email` VARCHAR(100) UNIQUE NOT NULL,
    `phone_number` VARCHAR(100) UNIQUE NOT NULL,
    `password` VARCHAR(100) NOT NULL,
    `full_name` VARCHAR(100) NOT NULL,

    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `families` (
    `id` VARCHAR(50) NOT NULL,
    `name` VARCHAR(100) NOT NULL,

    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `family_members` (
    `id` VARCHAR(50) NOT NULL,
    `member_id` VARCHAR(50) NOT NULL,
    `family_id` VARCHAR(50) NOT NULL,
    `is_admin` BOOLEAN NOT NULL,
    `role` VARCHAR(30),

    PRIMARY KEY (`id`),
    FOREIGN KEY (`member_id`) REFERENCES `users`(`id`),
    FOREIGN KEY (`family_id`) REFERENCES `families`(`id`)
);

CREATE TABLE IF NOT EXISTS `requests` (
    `id` VARCHAR(50) NOT NULL,
    `user_id` VARCHAR(50) NOT NULL,
    `family_id` VARCHAR(50) NOT NULL,
    `status` VARCHAR(50) NOT NULL,

    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`),
    FOREIGN KEY (`family_id`) REFERENCES `families`(`id`)
)
