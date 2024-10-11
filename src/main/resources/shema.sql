CREATE TABLE role
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE category
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE product
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    price       INT          NOT NULL,
    category_id BIGINT,
    CONSTRAINT fk_category
        FOREIGN KEY (category_id)
            REFERENCES category (id)
            ON DELETE SET NULL
);

CREATE TABLE user
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL
);


CREATE TABLE user_roles
(
    user_id BIGINT NOT NULL,
    roles   BIGINT NOT NULL,
    PRIMARY KEY (user_id, roles),
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES user (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_role
        FOREIGN KEY (roles)
            REFERENCES role (id)
            ON DELETE CASCADE
);
