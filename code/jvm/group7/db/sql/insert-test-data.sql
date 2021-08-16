--Makes execution independent - Active schema
SET SEARCH_PATH TO dbo;

--user_account(user_id, username, password);
INSERT INTO user_account(username, password) VALUES
  ('Alice', 'a589cbb20faf50e7c63950546a860b8d00d4af8e97ae09803b28e6b8363225f6089029b296a17d473e4f4866bc271775dddb086781514df7675de9c1acd64cea'), -- password: passAlice
  ('Bob', 'cbd7549d5c52a1b6cc1502b42aa22439797e7c89ceda1f71229afdfe84ece897b93f774b6bef12934e70eee4a4f0766e590b53bb96ede77def0d91839b552227'); -- password: passBob

--project(UserId, Username, Password);
INSERT INTO project(user_id, name, description) VALUES
    ((SELECT user_id from user_account WHERE username='Alice'), 'Daw Phase1', 'The evaluation focus will be on the HTTP API documentation and implementation.'),
    ((SELECT user_id from user_account WHERE username='Alice'), 'Daw Phase2', 'Browser client using REACT'),
    ((SELECT user_id from user_account WHERE username='Bob'), 'PFC', 'Last Semester Project');

