CREATE TABLE pet
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(256),
    age VARCHAR,
    description TEXT,
    file_path TEXT,
    size BIGINT DEFAULT 0,
    type VARCHAR,
    photo oid
);


