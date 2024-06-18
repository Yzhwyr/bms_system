CREATE TABLE warning_rule (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rule_id INT NOT NULL,
    name VARCHAR(50) NOT NULL,
    battery_type ENUM('三元电池', '铁锂电池') NOT NULL,
    warning_rule TEXT NOT NULL
);
CREATE TABLE car (
    vid VARCHAR(16) NOT NULL,
    id INT AUTO_INCREMENT PRIMARY KEY,
    battery_type ENUM('三元电池', '铁锂电池') NOT NULL,
    total_mileage INT NOT NULL,
    health_status INT NOT NULL
);
