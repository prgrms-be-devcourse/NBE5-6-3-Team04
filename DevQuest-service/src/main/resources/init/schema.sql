-- DROP TABLES (자식 → 부모 순서)

DROP TABLE IF EXISTS user_interest;
DROP TABLE IF EXISTS todos;
DROP TABLE IF EXISTS goal;
DROP TABLE IF EXISTS users_achieve;
DROP TABLE IF EXISTS achievement;
DROP TABLE IF EXISTS user_image;
DROP TABLE IF EXISTS project_recommendation;
DROP TABLE IF EXISTS ai_feedback;
DROP TABLE IF EXISTS goal_company;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS level;
DROP TABLE IF EXISTS interest;

-- level (최상위 부모)
CREATE TABLE level (
                       level_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                       level_name VARCHAR(255),
                       xp INT
);

-- user
CREATE TABLE user (
                      user_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                      level_id BIGINT NOT NULL,
                      user_image VARCHAR(255),
                      email VARCHAR(255) NOT NULL,
                      password VARCHAR(255) NOT NULL,
                      nickname VARCHAR(255) NOT NULL,
                      role VARCHAR(255) NOT NULL,
                      exp INT NOT NULL,
                      profile_image_url VARCHAR(255),
                      comment VARCHAR(255),
                      created_at TIMESTAMP NULL,
                      deleted_at TIMESTAMP NULL,
                      is_notification_on BOOLEAN DEFAULT false,
                      FOREIGN KEY (level_id) REFERENCES level(level_id)
);

-- interest
CREATE TABLE interest (
                          interest_id BIGINT NOT NULL AUTO_INCREMENT,
                          type VARCHAR(255) NOT NULL COMMENT '관심분야 종류 (직업/언어/프레임워크)',
                          interest_name VARCHAR(255) NOT NULL COMMENT '관심분야명 (백엔드/Java/SpringBoot)',
                        roadmap_url VARCHAR(255),
                          PRIMARY KEY (interest_id)
);

-- goal_company
CREATE TABLE goal_company (
                              company_id BIGINT NOT NULL AUTO_INCREMENT,
                              user_id BIGINT NOT NULL,
                              company_name VARCHAR(255),
                              content VARCHAR(255),
                              created_at TIMESTAMP NULL,
                              status VARCHAR(255),
                              end_date DATETIME,
                              PRIMARY KEY (company_id),
                              FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- goal
CREATE TABLE goal (
                      goal_id BIGINT NOT NULL AUTO_INCREMENT,
                      company_id BIGINT NOT NULL,
                      title VARCHAR(255) NOT NULL,
                      start_date DATETIME,
                      end_date DATETIME,
                      created_at TIMESTAMP NULL,
                      is_done BOOLEAN,
                      PRIMARY KEY (goal_id),
                      FOREIGN KEY (company_id) REFERENCES goal_company(company_id)
);

-- todos
CREATE TABLE todos (
                       todo_id BIGINT NOT NULL AUTO_INCREMENT,
                       goal_id BIGINT NOT NULL,
                       content VARCHAR(255),
                       start_date TIMESTAMP NULL,
                       end_date TIMESTAMP NULL,
                       is_done BOOLEAN,
                       PRIMARY KEY (todo_id),
                       FOREIGN KEY (goal_id) REFERENCES goal(goal_id)
);

-- achievement
CREATE TABLE achievement
(
    achieve_id  BIGINT NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255),
    description VARCHAR(255),
    PRIMARY KEY (achieve_id)
);

-- users_achieve
CREATE TABLE users_achieve
(
    id          BIGINT    NOT NULL AUTO_INCREMENT,
    user_id     BIGINT    NOT NULL,
    achieve_id  BIGINT    NOT NULL,
    achieved_at TIMESTAMP NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user (user_id),
    FOREIGN KEY (achieve_id) REFERENCES achievement (achieve_id)
);

-- user_image
CREATE TABLE user_image (
                            user_image_id BIGINT NOT NULL AUTO_INCREMENT,
                            user_id BIGINT NOT NULL,
                            origin_file_name VARCHAR(255),
                            rename_file_name VARCHAR(255),
                            save_path VARCHAR(255),
                            created_at TIMESTAMP NULL,
                            PRIMARY KEY (user_image_id),
                            FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- project_recommendation
CREATE TABLE project_recommendation (
                                        recom_id BIGINT NOT NULL AUTO_INCREMENT,
                                        user_id BIGINT NOT NULL,
                                        content VARCHAR(255),
                                        created_at TIMESTAMP NULL,
                                        PRIMARY KEY (recom_id),
                                        FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- ai_feedback
CREATE TABLE ai_feedback (
                             feedback_id BIGINT NOT NULL AUTO_INCREMENT,
                             user_id BIGINT NOT NULL,
                             feedback VARCHAR(255),
                             created_at TIMESTAMP NULL,
                             PRIMARY KEY (feedback_id),
                             FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- user_interest (가장 하위 테이블)
CREATE TABLE user_interest (
                               id BIGINT NOT NULL AUTO_INCREMENT,
                               user_id BIGINT NOT NULL,
                               interest_id BIGINT NOT NULL,
                               PRIMARY KEY (id),
                               FOREIGN KEY (user_id) REFERENCES user(user_id),
                               FOREIGN KEY (interest_id) REFERENCES interest(interest_id)
);