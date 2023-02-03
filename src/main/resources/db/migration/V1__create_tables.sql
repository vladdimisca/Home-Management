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

CREATE TABLE IF NOT EXISTS `comments` (
    `id` VARCHAR(50) NOT NULL,
    `content` VARCHAR(100) NOT NULL,
    `creation_date` DATETIME NOT NULL,
    `user_id` VARCHAR(50) NOT NULL,
    `task_id` VARCHAR(50) NOT NULL,

    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
);

CREATE TABLE IF NOT EXISTS `likes` (
    `id` VARCHAR(50) NOT NULL,
    `creation_date` DATETIME NOT NULL,
    `user_id` VARCHAR(50) NOT NULL,
    `comment_id` VARCHAR(50) NOT NULL,

    PRIMARY KEY (`id`),
    FOREIGN KEY (`comment_id`) REFERENCES `comments`(`id`)
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
);

CREATE TABLE IF NOT EXISTS `tasks` (
    `id` VARCHAR(50) NOT NULL,
    `creator_id` VARCHAR(50) NOT NULL,
    `assignee_id` VARCHAR(50),
    `family_id` VARCHAR(50) NOT NULL,
    `title` VARCHAR(100) NOT NULL,
    `description` VARCHAR(100) NOT NULL,
    `state` VARCHAR(30),
    `priority` VARCHAR(30),
    `creation_date` DATETIME DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    FOREIGN KEY (`creator_id`) REFERENCES `users`(`id`),
    FOREIGN KEY (`assignee_id`) REFERENCES `users`(`id`),
    FOREIGN KEY (`family_id`) REFERENCES `families`(`id`)
);