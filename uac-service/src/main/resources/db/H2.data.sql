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
  '',
  '{}',
  '["USER","ADMIN"]'
);


INSERT INTO social_accounts (
  gmt_create,
  gmt_modified,
  type,
  open_id,
  token,
  nickname,
  avatar,
  user_id
) VALUES (
  NOW(),
  NOW(),
  1,
  10001,
  'token-token-token',
  'jerry',
  '',
  1
);