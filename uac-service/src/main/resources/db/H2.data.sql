INSERT INTO users (
  gmt_create,
  gmt_modified,
  phone,
  secret,
  nickname,
  avatar,
  `profile`,
  authorities
)
VALUES (
  NOW(),
  NOW(),
  '18621816233',
  '$2a$09$bJSanuD8PWLGgfXxLW9wSOPssXCm4O/F1EkipT50JVd8sCsC.JDGa',
  'zhenhui',
  'https://ss0.bdstatic.com/7Ls0a8Sm1A5BphGlnYG/sys/portrait/item/78177a6863656e5e02',
  '{}',
  '["USER","ADMIN"]'
);


INSERT INTO social_accounts (
  gmt_create,
  gmt_modified,
  type,
  open_id,
  nickname,
  avatar,
  user_id
) VALUES (
  NOW(),
  NOW(),
  2,
  5035618596,
  'jerry',
  '',
  1
);