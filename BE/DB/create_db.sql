CREATE DATABASE `rent_house` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

-- rent_house.content definition

CREATE TABLE `content` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '内容ID',
  `form_id` int NOT NULL COMMENT '表单ID',
  `form_json` varchar(100) DEFAULT NULL COMMENT 'Form的整体架构',
  `form_data` json DEFAULT NULL COMMENT '表单数据',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- rent_house.formInfo definition

CREATE TABLE `formInfo` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL,
  `create_date` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;