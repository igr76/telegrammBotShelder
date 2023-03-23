-- Связывает таблицы shelter и pet
ALTER TABLE pet ADD COLUMN shelter_id bigint;

ALTER TABLE pet
    ADD CONSTRAINT fk_pets_shelters FOREIGN KEY (shelter_id) REFERENCES shelter (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- Связывает таблицы shelter и volunteer
ALTER TABLE volunteer ADD COLUMN shelter_id bigint;

ALTER TABLE volunteer
    ADD CONSTRAINT fk_volunteers_shelters FOREIGN KEY (shelter_id) REFERENCES shelter (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

-- Связывает таблицы pet и adoptive_parent
ALTER TABLE pet ADD COLUMN adoptive_parent_id bigint;

ALTER TABLE pet
    ADD CONSTRAINT fk_pets_adoptive_parents FOREIGN KEY (adoptive_parent_id) REFERENCES adoptive_parent (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;