package com.example.test.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.whiskerspawstailtelegrambot.entity.AdoptiveParent;
import pro.sky.whiskerspawstailtelegrambot.entity.Pet;
import pro.sky.whiskerspawstailtelegrambot.exception.ElemNotFound;
import pro.sky.whiskerspawstailtelegrambot.mapper.PetMapper;
import pro.sky.whiskerspawstailtelegrambot.record.PetRecord;
import pro.sky.whiskerspawstailtelegrambot.repository.AdoptiveParentRepo;
import pro.sky.whiskerspawstailtelegrambot.repository.PetRepository;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Сервис для управления испытательными сроками
 */
@Service
@Slf4j
public class TestPeriodService {

    //Репозиторий с питомцами
    private final PetRepository petRepository;
    //Репозиторий усыновителей
    private final AdoptiveParentRepo adoptiveParentRepo;
    //Маппер питомцев
    private final PetMapper petMapper;

    public TestPeriodService(PetRepository petRepository, AdoptiveParentRepo adoptiveParentRepo, PetMapper petMapper) {
        this.petRepository = petRepository;
        this.adoptiveParentRepo = adoptiveParentRepo;
        this.petMapper = petMapper;
    }

    /**
     * Находит питомцев без родителей
     * @return  - список питомцев
     */
    public Collection<PetRecord> getPetsDoesntHaveParent() {
        log.info("Получаем список животных, у которых нет приемных родителей");
        return petMapper.toRecordList(petRepository.findPetByAdoptiveParentIsNull());
    }

    /**
     * Производит усыновление (устанавливает родителя и испытательный срок)
     * @param petId             - идентификатор питомца
     * @param adoptiveParentId  - идентификатор усыновителя
     * @param testPeriodDays    - длительность испытательного срока в днях
     * @return                  - сущность измененного питомца
     */
    public PetRecord addAdoptiveParent(long petId, long adoptiveParentId, int testPeriodDays) {
        log.info("Добавляем усыновителя и устанавливаем испытательный срок");

        Pet pet = petRepository.findPetByIdAndAdoptiveParentIsNull(petId).orElseThrow(ElemNotFound::new);
        AdoptiveParent adoptiveParent = adoptiveParentRepo.findById(adoptiveParentId).orElseThrow(ElemNotFound::new);

        pet.setAdoptiveParent(adoptiveParent);
        pet.setTestPeriod(true);
        pet.setTestPeriodTime(LocalDateTime.now().plusDays(testPeriodDays));

        return petMapper.toRecord(petRepository.save(pet));
    }

    /**
     * Возвращает питомца обратно в приют
     * @param petId     - идентификатор питомца
     * @return          - сущность питомца
     */
    public PetRecord returnPetToShelter(long petId) {
        log.info("Возвращаем питомца в приют");

        Pet pet = petRepository.findById(petId).orElseThrow(ElemNotFound::new);
        pet.setAdoptiveParent(null);
        pet.setTestPeriod(false);
        pet.setTestPeriodTime(null);

        return petMapper.toRecord(petRepository.save(pet));
    }

    /**
     * Увеличивает испытательный срок
     * @param petId     - идентификатор питомца
     * @param days      - на сколько дней увеличить
     * @return          - измененную сущность питомца
     */
    public PetRecord extendTestPeriod(long petId, int days) {
        log.info("Увеличиваем испытательный срок");
        Pet pet = petRepository.findPetByAdoptiveParentIsNotNullAndIdAndTestPeriod(petId, true)
                .orElseThrow(ElemNotFound::new);
        pet.setTestPeriodTime(pet.getTestPeriodTime().plusDays(days));

        return petMapper.toRecord(petRepository.save(pet));
    }

    /**
     * Завершает испытательный срок
     * @param petId     - идентификатор питомца
     * @return          - измененную сущность питомца
     */
    public PetRecord completeTestPeriod(long petId) {
        log.info("Завершаем испытательный срок");
        Pet pet = petRepository.findPetByAdoptiveParentIsNotNullAndIdAndTestPeriod(petId, true)
                .orElseThrow(ElemNotFound::new);
        pet.setTestPeriod(false);
        pet.setTestPeriodTime(null);

        return petMapper.toRecord(petRepository.save(pet));
    }

    /**
     * Возвращает список питомцев на испытательном сроке
     * @return  - список питомцев на испытательном сроке
     */
    public Collection<PetRecord> getPetsHaveTestPeriod() {
        log.info("Возвращает питомцев, которые находятся на испытательном сроке");
        return petMapper.toRecordList(petRepository.findPetByTestPeriod(true));
    }

    /**
     * Возвращает список питомцев, у которых завершился/завершается испытательный срок
     * @param days  - количество дней до завершения испытательного срока
     * @return      - список питомцев
     */
    public Collection<PetRecord> getPetsWithEndedTestPeriod(int days) {
        log.info("Возвращает список питомцев, у которых заканчивается испытательный срок");
        return petMapper.toRecordList(petRepository.findPetsByTestPeriodAndTestPeriodTimeBefore(true,
                LocalDateTime.now().plusDays(days)));
    }
}
