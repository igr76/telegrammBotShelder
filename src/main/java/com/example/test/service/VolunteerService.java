package com.example.test.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.whiskerspawstailtelegrambot.entity.Volunteer;
import pro.sky.whiskerspawstailtelegrambot.exception.ElemNotFound;
import pro.sky.whiskerspawstailtelegrambot.mapper.VolunteerMapper;
import pro.sky.whiskerspawstailtelegrambot.record.VolunteerRecord;
import pro.sky.whiskerspawstailtelegrambot.repository.VolunteerRepo;

import java.util.Collection;

/**
 * Сервис для волонтеров
 */
@Service
@Slf4j
public class VolunteerService {

    private final VolunteerRepo volunteerRepo;
    private final VolunteerMapper volunteerMapper;

    public VolunteerService(VolunteerRepo volunteerRepo, VolunteerMapper volunteerMapper) {
        this.volunteerRepo = volunteerRepo;
        this.volunteerMapper = volunteerMapper;
    }

    /**
     * Возвращает всех волонтеров из БД
     * @return  - список всех волонтеров
     */
    public Collection<VolunteerRecord> getAllVolunteers() {
        log.info("Получаем список всех волонтеров");
        return volunteerMapper.toRecordList(volunteerRepo.findAll());
    }

    /**
     * Добавление волонтера в БД
     * @param fullName  - новое полное имя
     * @param phone     - новый номер телефона
     * @param info      - новая информация
     * @param schedule  - новый режим работы
     * @return          - сохраненную в БД запись волонтера
     */
    public VolunteerRecord addVolunteer(String fullName, String phone, String info, String schedule) {
        log.info("Добавляем волонтера" + fullName);

        Volunteer volunteer = new Volunteer();
        volunteer.setInfoVolunteer(info);
        volunteer.setSchedule(schedule);
        volunteer.setPhone(phone);
        volunteer.setFullName(fullName);
        return volunteerMapper.toRecord(volunteerRepo.save(volunteer));
    }

    /**
     * Удаление волонтера по Id
     * @param volunteerId   - id волонтера
     * @return              - удаленная сущность
     */
    public VolunteerRecord deleteVolunteerById(long volunteerId) {
        log.info("Удаляем волонтера" + volunteerId);
        VolunteerRecord volunteerRecord = getVolunteerById(volunteerId);
        volunteerRepo.deleteById(volunteerId);
        return volunteerRecord;
    }

    /**
     * Возвращает сущность волонтер по Id
     * @param volunteerId   - id для поиска
     * @return              - найденная сущность
     */
    public VolunteerRecord getVolunteerById(long volunteerId) {
        log.info("Ищем волонтера по id" + volunteerId);
        return volunteerMapper.toRecord(volunteerRepo.findById(volunteerId).orElseThrow(ElemNotFound::new));
    }

    /**
     * Поиск волонтера по имени
     * @param fullName  - полное имя волонтера
     * @return          - возвращает найденного волонтера
     */
    public VolunteerRecord getVolunteerByFullName(String fullName) {
        log.info("Ищем волонтера по имени " + fullName);
        return volunteerMapper.toRecord(volunteerRepo.findVolunteerByFullName(fullName).orElseThrow(ElemNotFound::new));
    }

    /**
     * Обновляет волонтера по Id
     * @param volunteerId   - id волонтера, которого необходимо обновить
     * @param fullName      - новое имя
     * @param phone         - новый номер телефона
     * @param info          - новая информация
     * @param schedule      - новое время работы
     * @return              - обновленную запись волонтера
     */
    public VolunteerRecord updateVolunteer(long volunteerId, String fullName, String phone, String info,
                                           String schedule, Long shelterId) {

        log.info("Обновление волонтера" + volunteerId);
        Volunteer oldVolunteer = volunteerMapper.toEntity(getVolunteerById(volunteerId));

        if (fullName != null) {
            oldVolunteer.setFullName(fullName);
        }
        if (phone != null) {
            oldVolunteer.setPhone(phone);
        }
        if (info != null) {
            oldVolunteer.setInfoVolunteer(info);
        }
        if (schedule != null) {
            oldVolunteer.setSchedule(schedule);
        }
        if (shelterId != null) {
            //TODO: Добавить установку приюта, когда добавиться поиск по id в ShelterService
        }

        return volunteerMapper.toRecord(volunteerRepo.save(oldVolunteer));
    }
}
