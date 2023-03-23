package com.example.test.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.whiskerspawstailtelegrambot.entity.Shelter;
import pro.sky.whiskerspawstailtelegrambot.exception.ElemNotFound;
import pro.sky.whiskerspawstailtelegrambot.repository.ShelterRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

/**
 * Юнит тесты на сервис приюта
 */
@ExtendWith(MockitoExtension.class)
public class ShelterServiceTest {
   Long numberShelter = 1L;
    List<String> exampleList = new ArrayList<>(List.of("1","2","3"));
    @InjectMocks
    private ShelterService shelterService;

    @Mock
    private ShelterRepo shelterRepo;
    Shelter shelter = new Shelter(1L,"ttt","uuu","jjj","iii","ppp","ttt",
            "fff",exampleList,"ggg","jjj","nnn",
            "ccc","kkk",exampleList,
            exampleList,null,null,new  byte[]{1,2,3});


    @Test
    void getOfShelterMessagePositiveTest(){
        when(shelterRepo.findById(numberShelter)).thenReturn(Optional.ofNullable(shelter));
        assertThat(shelterService.getOfShelterMessage((byte) 3)).isEqualTo("fff");
        verify(shelterRepo, times(1)).findById(numberShelter);

    }
    @Test
    void getOfShelterMessageNegativeTest(){
        assertThatExceptionOfType(ElemNotFound.class).isThrownBy(() -> shelterService.getOfShelterMessage((byte) 3));
    }


}
