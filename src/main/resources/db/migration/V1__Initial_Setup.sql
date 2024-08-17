CREATE TABLE app_user (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          phone_number VARCHAR(15) UNIQUE NOT NULL,
                          balance DECIMAL(19,2) NOT NULL
);

CREATE TABLE transaction (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             amount DECIMAL(19,2) NOT NULL,
                             type VARCHAR(20) NOT NULL,
                             user_id BIGINT,
                             FOREIGN KEY (user_id) REFERENCES app_user(id)
);
