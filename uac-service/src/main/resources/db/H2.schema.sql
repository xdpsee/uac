CREATE SCHEMA test;
USE test;
CREATE TABLE IF NOT EXISTS users (
  id           BIGINT PRIMARY KEY AUTO_INCREMENT,
  gmt_create   DATETIME    NOT NULL,
  gmt_modified DATETIME    NOT NULL,
  phone        VARCHAR(16) NOT NULL,
  secret       VARCHAR(32) NOT NULL,
  nickname     VARCHAR(32) NOT NULL,
  avatar       VARCHAR(255)       DEFAULT '',
  profile      VARCHAR(4096)      DEFAULT '{}',
  CONSTRAINT user_uk_1 UNIQUE (phone)
);

CREATE TABLE IF NOT EXISTS social_accounts (
  id           BIGINT PRIMARY KEY AUTO_INCREMENT,
  gmt_create   DATETIME    NOT NULL,
  gmt_modified DATETIME    NOT NULL,
  type         INTEGER     NOT NULL,
  open_id      BIGINT      NOT NULL,
  token        VARCHAR(64) NOT NULL,
  nickname     VARCHAR(32)        DEFAULT '',
  avatar       VARCHAR(255)       DEFAULT '',
  activated    BOOL               DEFAULT 0,
  user_id      BIGINT             DEFAULT 0,
  CONSTRAINT social_account_uk_1 UNIQUE (type, open_id)
);

CREATE INDEX social_account_idx_1
  ON social_accounts (user_id);
CREATE INDEX social_account_idx_2
  ON social_accounts (type, open_id, user_id);


