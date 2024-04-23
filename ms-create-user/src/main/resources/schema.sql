CREATE TABLE Users (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    last_login TIMESTAMP,
    token VARCHAR(255),
    is_active BOOLEAN
);

CREATE TABLE Phones (
    id SERIAL PRIMARY KEY,
    user_id UUID,
    number VARCHAR(20),
    city_code VARCHAR(10),
    country_code VARCHAR(10),
    FOREIGN KEY (user_id) REFERENCES Users(id)
);