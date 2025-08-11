INSERT IGNORE INTO role (name) VALUES ('ROLE_ADMIN');
INSERT IGNORE INTO role (name) VALUES ('ROLE_USER');

DELETE FROM user_roles WHERE user_id = (SELECT id FROM user WHERE username = 'admin');
DELETE FROM user WHERE username = 'admin';

-- password: admin123
INSERT INTO user (username, password) VALUES
('admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.');

-- Assegna ruolo admin all'utente admin
INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id 
FROM user u, role r 
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN';