-- Inserisci i ruoli base
INSERT INTO role (name) VALUES ('ROLE_ADMIN');
INSERT INTO role (name) VALUES ('ROLE_USER');

-- Inserisci utente amministratore (password: admin123)
INSERT INTO user (username, password) VALUES 
('admin', '$2a$10$Q1/j5Z1KjI6qrqYLzJZBjO.BowOLFzjGkLrWqVXNGF0jvPf6vlcOa');

-- Assegna ruolo admin all'utente admin
INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id 
FROM user u, role r 
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN';