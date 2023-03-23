-- Вспомогательная таблица для списка документов
CREATE TABLE shelter_list_of_doc
(
    shelter_id  bigint,
    index       bigint,
    list_if_doc text
);
-- Вспомогательная таблица для списка кинологов
CREATE TABLE shelter_cynologist
(
    shelter_id  bigint,
    index       bigint,
    cynologist text
);
-- Вспомогательная таблица для списка причин отказа
CREATE TABLE shelter_list_of_reason_for_rejection
(
    shelter_id  bigint,
    index       bigint,
    list_of_reason_for_rejection text
);
-- Таблица с информацией о приютах
CREATE TABLE shelter
(
    id                                         bigserial,
    about_shelter                              text,
    shelter_safety_equipment                   text,
    rule_of_meeting                            text,
    rec_of_transportation                      text,
    home_improvement_for_puppy                 text,
    home_improvement_for_pet                   text,
    home_improvement_for_pet_with_disabilities text,
    cynologist_advice                          text,
    PRIMARY KEY (id)
)