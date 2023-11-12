CREATE TABLE `broadcast_number` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`user_name` varchar(25) NOT NULL,
	`wa_number` varchar(15) NOT NULL UNIQUE,
	`created` TIMESTAMP NOT NULL,
	`updated` TIMESTAMP NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `recipient_number` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`user_name` varchar(25) NOT NULL,
	`wa_number` varchar(15) NOT NULL UNIQUE,
	`created` TIMESTAMP NOT NULL,
	`updated` TIMESTAMP NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `site_pop` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`pop_id` varchar(25) NOT NULL,
	`name` varchar(50) NOT NULL,
	`type` varchar(15) NOT NULL,
	`created` TIMESTAMP NOT NULL,
	`updated` TIMESTAMP NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `pm_schedule` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`site_pop` INT NOT NULL,
	`recipient` INT NOT NULL,
	`scheduled_date` DATE NOT NULL,
	`realization_date` DATE NOT NULL,
	`is_reminder_sent` BOOLEAN NOT NULL,
	`created` TIMESTAMP NOT NULL,
	`updated` TIMESTAMP NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `broadcast_history` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`sender` INT NOT NULL,
	`pm_event` INT NOT NULL,
	`created` TIMESTAMP NOT NULL,
	PRIMARY KEY (`id`)
);

ALTER TABLE `pm_schedule` ADD CONSTRAINT `pm_schedule_fk0` FOREIGN KEY (`site_pop`) REFERENCES `site_pop`(`id`);

ALTER TABLE `pm_schedule` ADD CONSTRAINT `pm_schedule_fk1` FOREIGN KEY (`recipient`) REFERENCES `recipient_number`(`id`);

ALTER TABLE `broadcast_history` ADD CONSTRAINT `broadcast_history_fk0` FOREIGN KEY (`sender`) REFERENCES `broadcast_number`(`id`);

ALTER TABLE `broadcast_history` ADD CONSTRAINT `broadcast_history_fk1` FOREIGN KEY (`pm_event`) REFERENCES `pm_schedule`(`id`);






