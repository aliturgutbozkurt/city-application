INSERT INTO roles (id, name)
VALUES (1, 'ROLE_USER');
INSERT INTO roles (id, name)
VALUES (2, 'ROLE_ADMIN');
INSERT INTO roles (id, name)
VALUES (3, 'ROLE_ALLOW_EDIT');


INSERT INTO users (full_name, username, password)
VALUES ('John Doe', 'johndoe', '$2a$10$9hT5B0OxwMuz3OhxHZ6Ym.mCW8wDln9i.tS4OmitygSf8Mn5QPKO6');


INSERT INTO users_roles (user_id, role_id)
VALUES (1,2);
INSERT INTO users_roles (user_id, role_id)
VALUES (1,3);
