SET REFERENTIAL_INTEGRITY FALSE;

-- -----------------------------------------------------
-- Table sido
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sido` (
      `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '시도번호',
      `sido_code` INT NOT NULL COMMENT '시도코드',
      `sido_name` VARCHAR(20) NOT NULL COMMENT '시도이름',
      CONSTRAINT `sido_code_UNIQUE` UNIQUE (`sido_code`)
);

-- -----------------------------------------------------
-- Table gugun
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gugun` (
        `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '구군번호',
        `sido_code` INT NOT NULL COMMENT '시도코드',
        `gugun_code` INT NOT NULL COMMENT '구군코드',
        `gugun_name` VARCHAR(20) NOT NULL COMMENT '구군이름',
        CONSTRAINT `gugun_code_UNIQUE` UNIQUE (`gugun_code`),
        CONSTRAINT `guguns_sido_to_sidos_cdoe_fk` FOREIGN KEY (`sido_code`) REFERENCES `sido` (`sido_code`)
);

-- -----------------------------------------------------
-- Table member
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `member` (
        `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
        `email` VARCHAR(45) NOT NULL,
        `password` VARCHAR(255) NULL DEFAULT NULL,
        `name` VARCHAR(45) NOT NULL,
        `role` VARCHAR(10) NULL DEFAULT NULL CHECK(role IN ('ADMIN', 'USER')),
        `created_at` DATETIME NULL DEFAULT NULL,
        `birth_date` DATE NULL DEFAULT NULL,
        `profile_image` VARCHAR(500) NULL DEFAULT NULL,
        `deleted_at` DATETIME NULL DEFAULT NULL,
        `blocked_at` DATETIME NULL DEFAULT NULL,
        CONSTRAINT `email_UNIQUE` UNIQUE (`email`)
);

-- -----------------------------------------------------
-- Table article
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `article` (
         `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
         `view_cnt` INT NOT NULL,
         `created_at` DATETIME NULL DEFAULT NULL,
         `modified_at` DATETIME NULL DEFAULT NULL,
         `title` VARCHAR(45) NOT NULL,
         `content` VARCHAR(5000) NULL DEFAULT NULL,
         `gugun_id` INT NULL DEFAULT NULL,
         `sido_id` INT NULL DEFAULT NULL,
         `member_id` INT NOT NULL,
         `deleted_at` DATETIME NULL DEFAULT NULL,
         `article_type` VARCHAR(10) NOT NULL CHECK(article_type IN ('NOTICE', 'FREE', 'QNA')),
         CONSTRAINT `fk_article_gugun1` FOREIGN KEY (`gugun_id`) REFERENCES `gugun` (`id`),
         CONSTRAINT `fk_article_member1` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
         CONSTRAINT `fk_article_sido1` FOREIGN KEY (`sido_id`) REFERENCES `sido` (`id`)
);

-- -----------------------------------------------------
-- Table category
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `category` (
      `category_id` INT NOT NULL PRIMARY KEY COMMENT '콘텐츠타입번호',
      `category_name` VARCHAR(45) NOT NULL COMMENT '콘텐츠타입이름'
);

-- -----------------------------------------------------
-- Table attraction
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `attraction` (
        `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '명소코드',
        `content_id` INT NOT NULL COMMENT '콘텐츠번호',
        `title` VARCHAR(500) NULL DEFAULT NULL COMMENT '명소이름',
        `category_id` INT NULL DEFAULT NULL COMMENT '콘텐츠타입',
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
        CONSTRAINT `attractions_area_to_sidos_code_fk` FOREIGN KEY (`sido_code`) REFERENCES `sido` (`sido_code`),
        CONSTRAINT `attractions_sigungu_to_guguns_gugun_fk` FOREIGN KEY (`gugun_code`) REFERENCES `gugun` (`gugun_code`),
        CONSTRAINT `attractions_typeid_to_types_typeid_fk` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`)
);

-- -----------------------------------------------------
-- Table comment
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `comment` (
         `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
         `article_id` INT NOT NULL,
         `created_at` DATETIME NULL DEFAULT NULL,
         `member_id` INT NOT NULL,
         `modified_at` DATETIME NULL DEFAULT NULL,
         `content` VARCHAR(500) NULL DEFAULT NULL,
         CONSTRAINT `fk_comment_article1` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`),
         CONSTRAINT `fk_comment_member1` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
);

-- -----------------------------------------------------
-- Table member_place
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `member_place` (
          `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
          `created_at` DATETIME NULL DEFAULT NULL,
          `modified_at` DATETIME NULL DEFAULT NULL,
          `name` VARCHAR(45) NOT NULL,
          `latitude` DECIMAL(20,17) NULL DEFAULT NULL,
          `longitude` DECIMAL(20,17) NULL DEFAULT NULL,
          `member_id` INT NOT NULL,
          `visited_at` DATE NULL DEFAULT NULL,
          `description` VARCHAR(1000) NULL DEFAULT NULL,
          `address` VARCHAR(100) NULL DEFAULT NULL,
          `category_id` INT NOT NULL,
          `image` VARCHAR(256) NULL DEFAULT NULL,
          `sido_id` INT NOT NULL,
          `gugun_id` INT NOT NULL,
          CONSTRAINT `fk_hotplace_category1` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`),
          CONSTRAINT `fk_hotplace_member1` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
          CONSTRAINT `fk_member_place_sido1` FOREIGN KEY (`sido_id`) REFERENCES `sido` (`id`),
          CONSTRAINT `fk_member_place_gugun1` FOREIGN KEY (`gugun_id`) REFERENCES `gugun` (`id`)
);

-- -----------------------------------------------------
-- Table oauth_provider
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `oauth_provider` (
        `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
        `provider` VARCHAR(50) NOT NULL,
        `key` VARCHAR(255) NOT NULL,
        `member_id` INT NOT NULL,
        CONSTRAINT `fk_oauth_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
);

-- -----------------------------------------------------
-- Table trip_plan
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `trip_plan` (
       `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
       `created_at` DATETIME NULL DEFAULT NULL,
       `modified_at` DATETIME NULL DEFAULT NULL,
       `title` VARCHAR(45) NOT NULL,
       `description` VARCHAR(1000) NULL DEFAULT NULL,
       `start_date` DATE NULL DEFAULT NULL,
       `end_date` DATE NULL DEFAULT NULL,
       `member_id` INT NOT NULL,
       CONSTRAINT `fk_trip_plan_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
);

-- -----------------------------------------------------
-- Table plan_item
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `plan_item` (
       `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
       `description` VARCHAR(1000) NULL DEFAULT NULL,
       `item_order` INT NOT NULL,
       `trip_plan_id` INT NOT NULL,
       `item_type` VARCHAR(45) NULL DEFAULT NULL,
       `attraction_id` INT NULL DEFAULT NULL,
       `member_place_id` INT NULL DEFAULT NULL,
       CONSTRAINT `fk_plan_item_attraction1` FOREIGN KEY (`attraction_id`) REFERENCES `attraction` (`id`),
       CONSTRAINT `fk_plan_item_member_place1` FOREIGN KEY (`member_place_id`) REFERENCES `member_place` (`id`),
       CONSTRAINT `fk_plan_item_trip_plan1` FOREIGN KEY (`trip_plan_id`) REFERENCES `trip_plan` (`id`)
);

SET REFERENTIAL_INTEGRITY TRUE;


-- -----------------------------------------------------
-- Insert
-- -----------------------------------------------------
INSERT INTO sido (id, sido_code, sido_name) VALUES 
(18, 1, '서울'),
(19, 2, '인천'),
(20, 3, '대전'),
(21, 4, '대구'),
(22, 5, '광주'),
(23, 6, '부산'),
(24, 7, '울산'),
(25, 8, '세종특별자치시'),
(26, 31, '경기도'),
(27, 32, '강원특별자치도'),
(28, 33, '충청북도'),
(29, 34, '충청남도'),
(30, 35, '경상북도'),
(31, 36, '경상남도'),
(32, 37, '전북특별자치도'),
(33, 38, '전라남도'),
(34, 39, '제주도');

INSERT INTO gugun (id, sido_code, gugun_code, gugun_name) VALUES
(235, 1, 1, '강남구'),
(236, 1, 2, '강동구'),
(237, 1, 3, '강북구'),
(238, 1, 4, '강서구'),
(239, 1, 5, '관악구'),
(240, 1, 6, '광진구'),
(241, 1, 7, '구로구'),
(242, 1, 8, '금천구'),
(243, 1, 9, '노원구'),
(244, 1, 10, '도봉구'),
(245, 1, 11, '동대문구'),
(246, 1, 12, '동작구'),
(247, 1, 13, '마포구'),
(248, 1, 14, '서대문구'),
(249, 1, 15, '서초구'),
(250, 1, 16, '성동구'),
(251, 1, 17, '성북구'),
(252, 1, 18, '송파구'),
(253, 1, 19, '양천구'),
(254, 1, 20, '영등포구'),
(255, 1, 21, '용산구'),
(256, 1, 22, '은평구'),
(257, 1, 23, '종로구'),
(258, 1, 24, '중구');

INSERT INTO category (category_id, category_name) VALUES
(12, '관광지'),
(14, '문화시설'),
(15, '축제공연행사'),
(25, '여행코스'),
(28, '레포츠'),
(32, '숙박'),
(38, '쇼핑'),
(39, '관광지');
