-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema ssafytrip
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema ssafytrip
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ssafytrip` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `ssafytrip` ;

-- -----------------------------------------------------
-- Table `ssafytrip`.`member`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`member` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(255) NULL DEFAULT NULL,
  `name` VARCHAR(45) NOT NULL,
  `role` ENUM('ADMIN', 'USER') NULL DEFAULT NULL,
  `created_at` DATETIME NULL DEFAULT NULL,
  `birth_date` DATE NULL DEFAULT NULL,
  `profile_image` VARCHAR(500) NULL DEFAULT NULL,
  `deleted_at` DATETIME NULL DEFAULT NULL,
  `blocked_at` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 24
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafytrip`.`article`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`article` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `view_count` INT NOT NULL,
  `created_at` DATETIME NULL DEFAULT NULL,
  `modified_at` DATETIME NULL DEFAULT NULL,
  `title` VARCHAR(45) NOT NULL,
  `content` VARCHAR(5000) NULL DEFAULT NULL,
  `member_id` BIGINT NOT NULL,
  `article_type` ENUM('NOTICE', 'FREE', 'QNA') NOT NULL,
  `deleted_at` DATETIME NULL DEFAULT NULL,
  `like_count` INT NULL DEFAULT NULL,
  `blocked_at` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_article_member1_idx` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_article_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `ssafytrip`.`member` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 52
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafytrip`.`article_like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`article_like` (
  `article_id` BIGINT NOT NULL,
  `member_id` BIGINT NOT NULL,
  `created_at` DATETIME NOT NULL,
  `deleted_at` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`article_id`, `member_id`),
  INDEX `fk_member` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_article`
    FOREIGN KEY (`article_id`)
    REFERENCES `ssafytrip`.`article` (`id`),
  CONSTRAINT `fk_member`
    FOREIGN KEY (`member_id`)
    REFERENCES `ssafytrip`.`member` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafytrip`.`article_report`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`article_report` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `article_id` BIGINT NOT NULL,
  `member_id` BIGINT NOT NULL,
  `reason` VARCHAR(1000) NOT NULL,
  `created_at` DATETIME NOT NULL,
  `handled_at` DATETIME NULL DEFAULT NULL,
  `status` ENUM('PENDING', 'APPROVED', 'REJECTED') NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_article_report_article` (`article_id` ASC) VISIBLE,
  INDEX `fk_article_report_member` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_article_report_article`
    FOREIGN KEY (`article_id`)
    REFERENCES `ssafytrip`.`article` (`id`),
  CONSTRAINT `fk_article_report_member`
    FOREIGN KEY (`member_id`)
    REFERENCES `ssafytrip`.`member` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafytrip`.`sido`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`sido` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '시도번호',
  `sido_code` INT NOT NULL COMMENT '시도코드',
  `sido_name` VARCHAR(20) NOT NULL COMMENT '시도이름',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `sido_code_UNIQUE` (`sido_code` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 35
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
COMMENT = '시도정보테이블';


-- -----------------------------------------------------
-- Table `ssafytrip`.`gugun`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`gugun` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '구군번호',
  `sido_code` INT NOT NULL COMMENT '시도코드',
  `gugun_code` INT NOT NULL COMMENT '구군코드',
  `gugun_name` VARCHAR(20) NOT NULL COMMENT '구군이름',
  PRIMARY KEY (`id`),
  INDEX `guguns_sido_to_sidos_cdoe_fk_idx` (`sido_code` ASC) VISIBLE,
  INDEX `gugun_code_idx` (`gugun_code` ASC) VISIBLE,
  CONSTRAINT `guguns_sido_to_sidos_cdoe_fk`
    FOREIGN KEY (`sido_code`)
    REFERENCES `ssafytrip`.`sido` (`sido_code`))
ENGINE = InnoDB
AUTO_INCREMENT = 469
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
COMMENT = '구군정보테이블';


-- -----------------------------------------------------
-- Table `ssafytrip`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`category` (
  `category_id` BIGINT NOT NULL COMMENT '콘텐츠타입번호',
  `category_name` VARCHAR(45) NOT NULL COMMENT '콘텐츠타입이름',
  PRIMARY KEY (`category_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
COMMENT = '콘텐츠타입정보테이블';


-- -----------------------------------------------------
-- Table `ssafytrip`.`attraction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`attraction` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '명소코드',
  `content_id` INT NOT NULL COMMENT '콘텐츠번호',
  `title` VARCHAR(500) NULL DEFAULT NULL COMMENT '명소이름',
  `category_id` BIGINT NULL DEFAULT NULL COMMENT '콘텐츠타입',
  `sido_code` INT NULL DEFAULT NULL COMMENT '시도코드',
  `gugun_code` INT NULL DEFAULT NULL COMMENT '구군코드',
  `image1` VARCHAR(100) NULL DEFAULT NULL COMMENT '이미지경로1',
  `image2` VARCHAR(100) NULL DEFAULT NULL COMMENT '이미지경로2',
  `map_level` INT NULL DEFAULT NULL COMMENT '줌레벨',
  `latitude` DECIMAL(20,17) NULL DEFAULT NULL COMMENT '위도',
  `longitude` DECIMAL(20,17) NULL DEFAULT NULL COMMENT '경도',
  `tel` VARCHAR(20) NULL DEFAULT NULL COMMENT '전화번호',
  `address1` VARCHAR(100) NULL DEFAULT NULL COMMENT '주소1',
  `address2` VARCHAR(100) NULL DEFAULT NULL COMMENT '주소2',
  `homepage` VARCHAR(1000) NULL DEFAULT NULL COMMENT '홈페이지',
  `overview` VARCHAR(10000) NULL DEFAULT NULL COMMENT '설명',
  `like_count` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `attractions_typeid_to_types_typeid_fk_idx` (`category_id` ASC) VISIBLE,
  INDEX `attractions_sido_to_sidos_code_fk_idx` (`sido_code` ASC) VISIBLE,
  INDEX `attractions_sigungu_to_guguns_gugun_fk_idx` (`gugun_code` ASC) VISIBLE,
  CONSTRAINT `attractions_area_to_sidos_code_fk`
    FOREIGN KEY (`sido_code`)
    REFERENCES `ssafytrip`.`sido` (`sido_code`),
  CONSTRAINT `attractions_sigungu_to_guguns_gugun_fk`
    FOREIGN KEY (`gugun_code`)
    REFERENCES `ssafytrip`.`gugun` (`gugun_code`),
  CONSTRAINT `attractions_typeid_to_types_typeid_fk`
    FOREIGN KEY (`category_id`)
    REFERENCES `ssafytrip`.`category` (`category_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 107559
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
COMMENT = '명소정보테이블';


-- -----------------------------------------------------
-- Table `ssafytrip`.`attraction_like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`attraction_like` (
  `attraction_id` BIGINT NOT NULL,
  `member_id` BIGINT NOT NULL,
  `created_at` DATETIME NOT NULL,
  `deleted_at` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`attraction_id`, `member_id`),
  INDEX `fk_attraction_like_member` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_attraction_like_attraction`
    FOREIGN KEY (`attraction_id`)
    REFERENCES `ssafytrip`.`attraction` (`id`),
  CONSTRAINT `fk_attraction_like_member`
    FOREIGN KEY (`member_id`)
    REFERENCES `ssafytrip`.`member` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafytrip`.`comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`comment` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `parent_id` BIGINT NULL DEFAULT NULL,
  `content` VARCHAR(1000) NOT NULL,
  `member_id` BIGINT NOT NULL,
  `article_id` BIGINT NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` DATETIME NULL DEFAULT NULL,
  `deleted_at` DATETIME NULL DEFAULT NULL,
  `blocked_at` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `parent_id` (`parent_id` ASC) VISIBLE,
  INDEX `member_id` (`member_id` ASC) VISIBLE,
  INDEX `article_id` (`article_id` ASC) VISIBLE,
  CONSTRAINT `comment_ibfk_1`
    FOREIGN KEY (`parent_id`)
    REFERENCES `ssafytrip`.`comment` (`id`),
  CONSTRAINT `comment_ibfk_2`
    FOREIGN KEY (`member_id`)
    REFERENCES `ssafytrip`.`member` (`id`),
  CONSTRAINT `comment_ibfk_3`
    FOREIGN KEY (`article_id`)
    REFERENCES `ssafytrip`.`article` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 99
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafytrip`.`comment_report`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`comment_report` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `comment_id` BIGINT NOT NULL,
  `member_id` BIGINT NOT NULL,
  `reason` TEXT NOT NULL,
  `created_at` DATETIME NOT NULL,
  `handled_at` DATETIME NULL DEFAULT NULL,
  `status` ENUM('PENDING', 'APPROVED', 'REJECTED') NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_comment_report_comment` (`comment_id` ASC) VISIBLE,
  INDEX `fk_comment_report_member` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_comment_report_comment`
    FOREIGN KEY (`comment_id`)
    REFERENCES `ssafytrip`.`comment` (`id`),
  CONSTRAINT `fk_comment_report_member`
    FOREIGN KEY (`member_id`)
    REFERENCES `ssafytrip`.`member` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafytrip`.`member_place`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`member_place` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME NULL DEFAULT NULL,
  `modified_at` DATETIME NULL DEFAULT NULL,
  `name` VARCHAR(45) NOT NULL,
  `latitude` DECIMAL(20,17) NULL DEFAULT NULL,
  `longitude` DECIMAL(20,17) NULL DEFAULT NULL,
  `member_id` BIGINT NOT NULL,
  `visited_at` DATE NULL DEFAULT NULL,
  `description` VARCHAR(1000) NULL DEFAULT NULL,
  `address` VARCHAR(100) NULL DEFAULT NULL,
  `category_id` BIGINT NOT NULL,
  `image` VARCHAR(256) NULL DEFAULT NULL,
  `sido_id` BIGINT NOT NULL,
  `gugun_id` BIGINT NOT NULL,
  `like_count` INT NULL DEFAULT NULL,
  `deleted_at` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_hotplace_member1_idx` (`member_id` ASC) VISIBLE,
  INDEX `fk_hotplace_category1_idx` (`category_id` ASC) VISIBLE,
  INDEX `fk_member_place_sido1_idx` (`sido_id` ASC) VISIBLE,
  INDEX `fk_member_place_gugun1_idx` (`gugun_id` ASC) VISIBLE,
  CONSTRAINT `fk_hotplace_category1`
    FOREIGN KEY (`category_id`)
    REFERENCES `ssafytrip`.`category` (`category_id`),
  CONSTRAINT `fk_hotplace_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `ssafytrip`.`member` (`id`),
  CONSTRAINT `fk_member_place_gugun1`
    FOREIGN KEY (`gugun_id`)
    REFERENCES `ssafytrip`.`gugun` (`id`),
  CONSTRAINT `fk_member_place_sido1`
    FOREIGN KEY (`sido_id`)
    REFERENCES `ssafytrip`.`sido` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 16
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafytrip`.`member_place_like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`member_place_like` (
  `member_place_id` BIGINT NOT NULL,
  `member_id` BIGINT NOT NULL,
  `created_at` DATETIME NOT NULL,
  `deleted_at` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`member_place_id`, `member_id`),
  INDEX `fk_member_place_like_member` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_member_place_like_member`
    FOREIGN KEY (`member_id`)
    REFERENCES `ssafytrip`.`member` (`id`),
  CONSTRAINT `fk_member_place_like_place`
    FOREIGN KEY (`member_place_id`)
    REFERENCES `ssafytrip`.`member_place` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafytrip`.`oauth_provider`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`oauth_provider` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `provider` VARCHAR(50) NOT NULL,
  `key` VARCHAR(255) NOT NULL,
  `member_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_oauth_member` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_oauth_member`
    FOREIGN KEY (`member_id`)
    REFERENCES `ssafytrip`.`member` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 8
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafytrip`.`trip_plan`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`trip_plan` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME NULL DEFAULT NULL,
  `modified_at` DATETIME NULL DEFAULT NULL,
  `title` VARCHAR(45) NOT NULL,
  `description` VARCHAR(1000) NULL DEFAULT NULL,
  `start_date` DATE NULL DEFAULT NULL,
  `end_date` DATE NULL DEFAULT NULL,
  `member_id` BIGINT NOT NULL,
  `deleted_at` DATETIME NULL DEFAULT NULL,
  `like_count` INT NULL DEFAULT NULL,
  `view_count` INT NULL DEFAULT NULL,
  `total_days` INT NULL DEFAULT NULL,
  `season` ENUM('WINTER', 'FALL', 'SUMMER', 'SPRING') NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_trip_plan_member_idx` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_trip_plan_member`
    FOREIGN KEY (`member_id`)
    REFERENCES `ssafytrip`.`member` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 82
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafytrip`.`trip_plan_day`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`trip_plan_day` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `date` DATE NOT NULL,
  `day_order` INT NOT NULL,
  `trip_plan_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_trip_plan_day_plan` (`trip_plan_id` ASC) VISIBLE,
  CONSTRAINT `fk_trip_plan_day_plan`
    FOREIGN KEY (`trip_plan_id`)
    REFERENCES `ssafytrip`.`trip_plan` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 142
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafytrip`.`plan_item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`plan_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(1000) NULL DEFAULT NULL,
  `item_order` INT NOT NULL,
  `item_type` VARCHAR(45) NULL DEFAULT NULL,
  `attraction_id` BIGINT NULL DEFAULT NULL,
  `member_place_id` BIGINT NULL DEFAULT NULL,
  `trip_plan_day_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_plan_item_attraction1_idx` (`attraction_id` ASC) VISIBLE,
  INDEX `fk_plan_item_member_place1_idx` (`member_place_id` ASC) VISIBLE,
  INDEX `fk_trip_plan_day` (`trip_plan_day_id` ASC) VISIBLE,
  CONSTRAINT `fk_plan_item_attraction1`
    FOREIGN KEY (`attraction_id`)
    REFERENCES `ssafytrip`.`attraction` (`id`),
  CONSTRAINT `fk_plan_item_member_place1`
    FOREIGN KEY (`member_place_id`)
    REFERENCES `ssafytrip`.`member_place` (`id`),
  CONSTRAINT `fk_trip_plan_day`
    FOREIGN KEY (`trip_plan_day_id`)
    REFERENCES `ssafytrip`.`trip_plan_day` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 249
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafytrip`.`qna_answer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`qna_answer` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `article_id` BIGINT NOT NULL,
  `member_id` BIGINT NULL DEFAULT NULL,
  `content` VARCHAR(5000) NULL DEFAULT NULL,
  `created_at` DATETIME NULL DEFAULT NULL,
  `status` ENUM('PENDING', 'ANSWERED') NOT NULL DEFAULT 'PENDING',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `article_id` (`article_id` ASC) VISIBLE,
  INDEX `fk_qna_answer_member` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_qna_answer_article`
    FOREIGN KEY (`article_id`)
    REFERENCES `ssafytrip`.`article` (`id`),
  CONSTRAINT `fk_qna_answer_member`
    FOREIGN KEY (`member_id`)
    REFERENCES `ssafytrip`.`member` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 18
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafytrip`.`route_connection`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`route_connection` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `from_item_id` BIGINT NOT NULL,
  `to_item_id` BIGINT NOT NULL,
  `distance` VARCHAR(255) NULL DEFAULT NULL,
  `duration` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_route_connection_from_item` (`from_item_id` ASC) VISIBLE,
  INDEX `fk_route_connection_to_item` (`to_item_id` ASC) VISIBLE,
  CONSTRAINT `fk_route_connection_from_item`
    FOREIGN KEY (`from_item_id`)
    REFERENCES `ssafytrip`.`plan_item` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_route_connection_to_item`
    FOREIGN KEY (`to_item_id`)
    REFERENCES `ssafytrip`.`plan_item` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 145
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafytrip`.`trip_plan_like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`trip_plan_like` (
  `trip_plan_id` BIGINT NOT NULL,
  `member_id` BIGINT NOT NULL,
  `created_at` DATETIME NOT NULL,
  `deleted_at` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`trip_plan_id`, `member_id`),
  INDEX `fk_trip_plan_like_member` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_trip_plan_like_member`
    FOREIGN KEY (`member_id`)
    REFERENCES `ssafytrip`.`member` (`id`),
  CONSTRAINT `fk_trip_plan_like_plan`
    FOREIGN KEY (`trip_plan_id`)
    REFERENCES `ssafytrip`.`trip_plan` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafytrip`.`trip_style`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`trip_style` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `trip_plan_id` BIGINT NOT NULL,
  `style` ENUM('RELAXING', 'SIGHTSEEING', 'NATURE', 'CULTURAL', 'FOODIE', 'ADVENTURE', 'SHOPPING', 'PHOTO') NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_trip_style_trip_plan` (`trip_plan_id` ASC) VISIBLE,
  CONSTRAINT `fk_trip_style_trip_plan`
    FOREIGN KEY (`trip_plan_id`)
    REFERENCES `ssafytrip`.`trip_plan` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 68
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
