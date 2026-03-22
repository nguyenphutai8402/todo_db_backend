-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.4.32-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.15.0.7171
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping structure for table tododb.activities
CREATE TABLE IF NOT EXISTS `activities` (
  `actor_user_id` bigint(20) NOT NULL,
  `board_id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `target_id` bigint(20) DEFAULT NULL,
  `data` text DEFAULT NULL,
  `target_type` varchar(255) DEFAULT NULL,
  `action` enum('ADD_ATTACHMENT','ADD_COMMENT','ADD_MEMBER','ARCHIVE_CARD','CREATE_CARD','MOVE_CARD','REMOVE_MEMBER','UPDATE_CARD') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKghjl3iucs4ju9xi96etatqs94` (`actor_user_id`),
  KEY `FKnju5v17qjhtup894irq03w4ij` (`board_id`),
  CONSTRAINT `FKghjl3iucs4ju9xi96etatqs94` FOREIGN KEY (`actor_user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKnju5v17qjhtup894irq03w4ij` FOREIGN KEY (`board_id`) REFERENCES `boards` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table tododb.board_members
CREATE TABLE IF NOT EXISTS `board_members` (
  `board_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `joined_at` datetime(6) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `contextual_role` enum('MEMBER','OBSERVER','OWNER') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKdip0gtav4cxokql76sf0981t4` (`board_id`,`user_id`),
  KEY `FK80hd8sx9wrhibcfwv37pvmxb6` (`user_id`),
  CONSTRAINT `FK80hd8sx9wrhibcfwv37pvmxb6` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKfqm0ki2w8yabmxwctnct8sb91` FOREIGN KEY (`board_id`) REFERENCES `boards` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table tododb.boards
CREATE TABLE IF NOT EXISTS `boards` (
  `is_archived` bit(1) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `workspace_id` bigint(20) NOT NULL,
  `name` varchar(150) NOT NULL,
  `description` text DEFAULT NULL,
  `visibility` enum('PRIVATE','PUBLIC') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqwgj7k9y79vuoa0sy5jagcj28` (`created_by`),
  KEY `FKthpwqhdcr9u32c3tkrh7isid4` (`workspace_id`),
  CONSTRAINT `FKqwgj7k9y79vuoa0sy5jagcj28` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKthpwqhdcr9u32c3tkrh7isid4` FOREIGN KEY (`workspace_id`) REFERENCES `workspaces` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table tododb.card_attachments
CREATE TABLE IF NOT EXISTS `card_attachments` (
  `card_id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `size_bytes` bigint(20) DEFAULT NULL,
  `uploaded_by` bigint(20) NOT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_url` text NOT NULL,
  `mime_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKm0ekm2hd33ea6mxwf16x4a94o` (`card_id`),
  KEY `FKhumxakytsd3yhmtqc58uw7182` (`uploaded_by`),
  CONSTRAINT `FKhumxakytsd3yhmtqc58uw7182` FOREIGN KEY (`uploaded_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKm0ekm2hd33ea6mxwf16x4a94o` FOREIGN KEY (`card_id`) REFERENCES `cards` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table tododb.card_comments
CREATE TABLE IF NOT EXISTS `card_comments` (
  `card_id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `content` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK56yn9p41ijrjb2ujighsvms06` (`card_id`),
  KEY `FK33thqapaplel3c6i7xadeu8q0` (`user_id`),
  CONSTRAINT `FK33thqapaplel3c6i7xadeu8q0` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK56yn9p41ijrjb2ujighsvms06` FOREIGN KEY (`card_id`) REFERENCES `cards` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table tododb.card_labels
CREATE TABLE IF NOT EXISTS `card_labels` (
  `card_id` bigint(20) NOT NULL,
  `label_id` bigint(20) NOT NULL,
  KEY `FKjtuk7wmotl3wjflqgnk5tve2y` (`label_id`),
  KEY `FKo589elw417q3d7d7bbvooenm4` (`card_id`),
  CONSTRAINT `FKjtuk7wmotl3wjflqgnk5tve2y` FOREIGN KEY (`label_id`) REFERENCES `labels` (`id`),
  CONSTRAINT `FKo589elw417q3d7d7bbvooenm4` FOREIGN KEY (`card_id`) REFERENCES `cards` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table tododb.card_members
CREATE TABLE IF NOT EXISTS `card_members` (
  `added_at` datetime(6) DEFAULT NULL,
  `card_id` bigint(20) DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKdxgs7sla9ad87g1b1xnlw2t3y` (`card_id`,`user_id`),
  KEY `FKct2y3d055f305miv95mvtuxl9` (`user_id`),
  CONSTRAINT `FKct2y3d055f305miv95mvtuxl9` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKkfc4clyo8lw6mraqme1ax3pk2` FOREIGN KEY (`card_id`) REFERENCES `cards` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table tododb.cards
CREATE TABLE IF NOT EXISTS `cards` (
  `is_archived` bit(1) DEFAULT NULL,
  `position` int(11) NOT NULL,
  `board_id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `due_at` datetime(6) DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `list_id` bigint(20) NOT NULL,
  `start_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `title` varchar(200) NOT NULL,
  `description` text DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKk0nnnx4q6pmiiwp0u5i26vhlm` (`board_id`),
  KEY `FK2qgc639ele5paxfuc3gru1ljk` (`created_by`),
  KEY `FK4f85guj10y6pwodg6h6qgkuuq` (`list_id`),
  CONSTRAINT `FK2qgc639ele5paxfuc3gru1ljk` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FK4f85guj10y6pwodg6h6qgkuuq` FOREIGN KEY (`list_id`) REFERENCES `lists` (`id`),
  CONSTRAINT `FKk0nnnx4q6pmiiwp0u5i26vhlm` FOREIGN KEY (`board_id`) REFERENCES `boards` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table tododb.checklist_items
CREATE TABLE IF NOT EXISTS `checklist_items` (
  `is_done` bit(1) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `checklist_id` bigint(20) NOT NULL,
  `completed_at` datetime(6) DEFAULT NULL,
  `completed_by` bigint(20) DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(300) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKosaywfe8917yt4yapp57fxqf9` (`checklist_id`),
  KEY `FK61qu1wqs9k0xolciraxwhj4q1` (`completed_by`),
  CONSTRAINT `FK61qu1wqs9k0xolciraxwhj4q1` FOREIGN KEY (`completed_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKosaywfe8917yt4yapp57fxqf9` FOREIGN KEY (`checklist_id`) REFERENCES `checklists` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table tododb.checklists
CREATE TABLE IF NOT EXISTS `checklists` (
  `position` int(11) NOT NULL,
  `card_id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `title` varchar(150) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKoj4bw9uwdrk2h3yfglgyxuq2u` (`card_id`),
  CONSTRAINT `FKoj4bw9uwdrk2h3yfglgyxuq2u` FOREIGN KEY (`card_id`) REFERENCES `cards` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table tododb.invites
CREATE TABLE IF NOT EXISTS `invites` (
  `accepted_at` datetime(6) DEFAULT NULL,
  `board_id` bigint(20) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `expires_at` datetime(6) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `workspace_id` bigint(20) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `token` varchar(255) NOT NULL,
  `contextual_role` enum('MEMBER','OBSERVER','OWNER') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK1ws9kt1ybdrcww2o5w8300lty` (`token`),
  KEY `FK9hsiws556hnuqrk2mhn19o2xr` (`board_id`),
  KEY `FKaffo56dg0g71o6yssvubfy8cr` (`created_by`),
  KEY `FK9py5y3gho9juhlm92msna1iup` (`workspace_id`),
  CONSTRAINT `FK9hsiws556hnuqrk2mhn19o2xr` FOREIGN KEY (`board_id`) REFERENCES `boards` (`id`),
  CONSTRAINT `FK9py5y3gho9juhlm92msna1iup` FOREIGN KEY (`workspace_id`) REFERENCES `workspaces` (`id`),
  CONSTRAINT `FKaffo56dg0g71o6yssvubfy8cr` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table tododb.labels
CREATE TABLE IF NOT EXISTS `labels` (
  `board_id` bigint(20) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `color` varchar(30) NOT NULL,
  `name` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2cqcyvlx9wc7pn9vr6n6uqops` (`board_id`),
  CONSTRAINT `FK2cqcyvlx9wc7pn9vr6n6uqops` FOREIGN KEY (`board_id`) REFERENCES `boards` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table tododb.lists
CREATE TABLE IF NOT EXISTS `lists` (
  `is_archived` bit(1) DEFAULT NULL,
  `position` int(11) NOT NULL,
  `board_id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `name` varchar(150) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjej0g3q3kr0y7nckn7knxooqp` (`board_id`),
  CONSTRAINT `FKjej0g3q3kr0y7nckn7knxooqp` FOREIGN KEY (`board_id`) REFERENCES `boards` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table tododb.users
CREATE TABLE IF NOT EXISTS `users` (
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `avatar_url` text DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `global_role` enum('ADMIN','USER') NOT NULL,
  `status` enum('ACTIVE','BLOCKED') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table tododb.workspace_members
CREATE TABLE IF NOT EXISTS `workspace_members` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `joined_at` datetime(6) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `workspace_id` bigint(20) NOT NULL,
  `contextual_role` enum('MEMBER','OBSERVER','OWNER') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6se2rw5firt04m4vpmqvbnr4u` (`workspace_id`,`user_id`),
  KEY `FK6vtnpc3eexk504u61uepn40p1` (`user_id`),
  CONSTRAINT `FK6vtnpc3eexk504u61uepn40p1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKw9hq87n3rvq2c4j47qo78i5r` FOREIGN KEY (`workspace_id`) REFERENCES `workspaces` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table tododb.workspaces
CREATE TABLE IF NOT EXISTS `workspaces` (
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `name` varchar(150) NOT NULL,
  `description` text DEFAULT NULL,
  `logo_url` text DEFAULT NULL,
  `visibility` enum('PRIVATE','PUBLIC') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKadrg6adoh0289s3gtbkv3fxmq` (`name`,`created_by`),
  KEY `FKlwdvhq4w0563rrp55oy8m0pcb` (`created_by`),
  CONSTRAINT `FKlwdvhq4w0563rrp55oy8m0pcb` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
