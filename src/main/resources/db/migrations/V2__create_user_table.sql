CREATE TABLE UserEntity
(
    id                BIGINT,
    username          VARCHAR(255) NOT NULL,
    firstName         VARCHAR(255),
    lastName          VARCHAR(255),
    email             VARCHAR(255) NOT NULL,
    profilePictureUrl VARCHAR(255)
);

ALTER TABLE UserEntity
    ADD CONSTRAINT uc_userentity_email UNIQUE (email);

ALTER TABLE UserEntity
    ADD CONSTRAINT uc_userentity_username UNIQUE (username);