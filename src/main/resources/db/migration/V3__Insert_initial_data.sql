-- Inserisce i ruoli
INSERT IGNORE INTO role (name) VALUES ('ROLE_ADMIN');
INSERT IGNORE INTO role (name) VALUES ('ROLE_USER');

DELETE ur FROM user_roles ur
INNER JOIN user u ON ur.user_id = u.id
WHERE u.username = 'admin';

DELETE FROM user WHERE username = 'admin';

-- Hash BCrypt per "admin123": $2a$10$rQ9.v9E6mYOLzUzj4dYq5OX5CZYfLzRzUvW5qvC0Y4z8r4p1P5dK.
INSERT INTO user (username, password) VALUES
('admin', '$2a$10$rQ9.v9E6mYOLzUzj4dYq5OX5CZYfLzRzUvW5qvC0Y4z8r4p1P5dK.');

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM user u, role r
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN';