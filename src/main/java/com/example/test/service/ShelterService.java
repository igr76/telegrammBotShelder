package com.example.test.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.whiskerspawstailtelegrambot.exception.ElemNotFound;
import pro.sky.whiskerspawstailtelegrambot.repository.ShelterRepo;

/**
 * Сервис слой для приюта
 */
@Service
@Slf4j
public class ShelterService {

    private ShelterRepo shelterRepo;

    public ShelterService(ShelterRepo shelterRepo) {
        this.shelterRepo = shelterRepo;
    }

    /**
     * Номер приюта
     */
    private final long numberShelter = 1L;
    /**
     * Метод, который показывает все сообщения приюта
     *в порядке очереди (number)
     * @return список всех сообщений
     */
    public String getOfShelterMessage(Byte number) {
        log.info("Was invoked method for get list of MessageShelter from DB");
        switch (number){
            case (1):
                return shelterRepo.findById(numberShelter).orElseThrow(ElemNotFound::new).getAboutShelter() ;
            case (2):
                return shelterRepo.findById(numberShelter).orElseThrow(ElemNotFound::new).getShelterSafetyEquipment();
            case (3):
                return shelterRepo.findById(numberShelter).orElseThrow(ElemNotFound::new).getRuleOfMeeting();
            case (4):
                return shelterRepo.findById(numberShelter).orElseThrow(ElemNotFound::new).getRecOfTransportation();
            case (5):
                return shelterRepo.findById(numberShelter).orElseThrow(ElemNotFound::new).getHomeImprovementForPuppy();
            case (6):
                return shelterRepo.findById(numberShelter).orElseThrow(ElemNotFound::new).getHomeImprovementForPet();
            case (7):
                return shelterRepo.findById(numberShelter).orElseThrow(ElemNotFound::new).getHomeImprovementForPetWithDisabilities();
            case (8):
                return shelterRepo.findById(numberShelter).orElseThrow(ElemNotFound::new).getCynologistAdvice();
            default:
                log.info("неверные входные данные сервивса вывода информации приюта");
                break;
        }


        return null;
    }
}
