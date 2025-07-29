CREATE TABLE booking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL,
    time TIME NOT NULL,
    number_of_guests INT NOT NULL CHECK (number_of_guests >= 1 AND number_of_guests <= 20),
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    UNIQUE KEY unique_booking_datetime (date, time)
);

-- Indici per migliorare le performance
CREATE INDEX idx_booking_date ON booking(date);
CREATE INDEX idx_booking_user_id ON booking(user_id);
CREATE INDEX idx_booking_datetime ON booking(date, time);