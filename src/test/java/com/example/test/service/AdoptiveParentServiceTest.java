package com.example.test.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.whiskerspawstailtelegrambot.entity.AdoptiveParent;
import pro.sky.whiskerspawstailtelegrambot.entity.Pet;
import pro.sky.whiskerspawstailtelegrambot.exception.ElemNotFound;
import pro.sky.whiskerspawstailtelegrambot.mapper.AdoptiveParentMapper;
import pro.sky.whiskerspawstailtelegrambot.record.AdoptiveParentRecord;
import pro.sky.whiskerspawstailtelegrambot.record.PetRecord;
import pro.sky.whiskerspawstailtelegrambot.repository.AdoptiveParentRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Юнит тесты на сервис усыновителя
 */
@ExtendWith(MockitoExtension.class)
class AdoptiveParentServiceTest {
    @Mock
    AdoptiveParentRepo adoptiveParentRepo;
    @Spy
    AdoptiveParentMapper adoptiveParentMapper;
    @InjectMocks
    AdoptiveParentService adoptiveParentService;

    String ANYSTRING;
    long ANYLONG;
    List<Pet> pets;
    List<PetRecord> petRecords;
    AdoptiveParentRecord adoptiveParentTestPositive;
    AdoptiveParent adoptiveParent;

    @BeforeEach
    void init(){
        ANYSTRING = "anystring";
        ANYLONG = 1L;
        pets = new ArrayList<>();
        petRecords = new ArrayList<>();
        adoptiveParentTestPositive = new AdoptiveParentRecord(1l, "fullName", "phone", true, "start", 0l,
            petRecords);
        adoptiveParent = new AdoptiveParent(1l, "fullName", "phone", true, "start", 0l, pets);
    }

    @AfterEach
    void clearAll(){
        ANYSTRING = null;
        ANYLONG = 0;
        pets.clear();
        petRecords.clear();
        adoptiveParentTestPositive = null;
        adoptiveParent = null;
    }

    @Test
    void getParentIdByNameAndPhoneAndChatIdPositiveFullName() {
        when(adoptiveParentRepo.getAdoptiveParentByFullName(ANYSTRING)).thenReturn(adoptiveParent);

        assertThat(adoptiveParentService.getParentIdByNameAndPhoneAndChatId(ANYSTRING, null, null)).isEqualTo(ANYLONG);

        verify(adoptiveParentRepo, times(1)).getAdoptiveParentByFullName(ANYSTRING);
    }

    @Test
    void getParentIdByNameAndPhoneAndChatIdPositivePhone() {
        when(adoptiveParentRepo.getAdoptiveParentByPhone(ANYSTRING)).thenReturn(adoptiveParent);

        assertThat(adoptiveParentService.getParentIdByNameAndPhoneAndChatId(null, ANYSTRING, null)).isEqualTo(ANYLONG);

        verify(adoptiveParentRepo, times(1)).getAdoptiveParentByPhone(ANYSTRING);
    }

    @Test
    void getParentIdByNameAndPhoneAndChatIdPositiveChatId() {
        when(adoptiveParentRepo.getAdoptiveParentByChatId(ANYLONG)).thenReturn(adoptiveParent);

        assertThat(adoptiveParentService.getParentIdByNameAndPhoneAndChatId(null, null, ANYLONG)).isEqualTo(1L);

        verify(adoptiveParentRepo, times(1)).getAdoptiveParentByChatId(ANYLONG);
    }

    @Test
    void getParentIdByNameAndPhoneAndChatIdNegative() {
        assertThatExceptionOfType(ElemNotFound.class).isThrownBy(() -> adoptiveParentService.getParentIdByNameAndPhoneAndChatId(null, null, null));

        assertThatExceptionOfType(ElemNotFound.class).isThrownBy(() -> adoptiveParentService.getParentIdByNameAndPhoneAndChatId("", "", 0L));
    }

    @Test
    void getAdoptiveParentByIDPositive() {
        when(adoptiveParentRepo.findById(anyLong())).thenReturn(Optional.ofNullable(adoptiveParent));

        when(adoptiveParentMapper.toRecord(adoptiveParent)).thenReturn(adoptiveParentTestPositive);

        assertThat(adoptiveParentService.getAdoptiveParentByID(1L)).isEqualTo(adoptiveParentTestPositive);

        verify(adoptiveParentRepo, times(1)).findById(any());

    }

    @Test
    void getAdoptiveParentByIDNegative() {
        assertThatExceptionOfType(ElemNotFound.class).isThrownBy(() -> adoptiveParentService.getAdoptiveParentByID(1));
    }

    @Test
    void deleteAdoptiveParentByIDPositive() {

        when(adoptiveParentRepo.findById(ANYLONG)).thenReturn(Optional.ofNullable(adoptiveParent));
        when(adoptiveParentMapper.toRecord(adoptiveParent)).thenReturn(adoptiveParentTestPositive);
        doNothing().when(adoptiveParentRepo).deleteById(ANYLONG);


        assertThat(adoptiveParentService.deleteAdoptiveParentByID(1L)).isEqualTo(adoptiveParentTestPositive);

        verify(adoptiveParentRepo, times(1)).findById(any());
        verify(adoptiveParentRepo, times(1)).deleteById(any());
    }

    @Test
    void deleteAdoptiveParentByIDNegative() {

        assertThatExceptionOfType(ElemNotFound.class).isThrownBy(() -> adoptiveParentService.deleteAdoptiveParentByID(1));
    }

    @Test
    void addAdoptiveParentPositive() {
        when(adoptiveParentMapper.toEntity(adoptiveParentTestPositive)).thenReturn(adoptiveParent);

        when(adoptiveParentRepo.save(adoptiveParent)).thenReturn(adoptiveParent);

        when(adoptiveParentMapper.toRecord(adoptiveParent)).thenReturn(adoptiveParentTestPositive);

        assertThat(adoptiveParentService.addAdoptiveParent(adoptiveParentTestPositive)).isEqualTo(adoptiveParentTestPositive);

        verify(adoptiveParentRepo, times(1)).save(adoptiveParent);

    }

    @Test
    void addAdoptiveParentNegativeWithNullEntity() {
        assertThatExceptionOfType(ElemNotFound.class).isThrownBy(() -> adoptiveParentService.addAdoptiveParent(null));

    }

    @Test
    void addAdoptiveParentNegativeWithEmptyOrMistakeFields() {
        assertThatExceptionOfType(ElemNotFound.class).isThrownBy(() -> adoptiveParentService.addAdoptiveParent(null));
    }

    @Test
    void getListOfAdoptiveParentPositive() {
        List<AdoptiveParent> adoptiveParents = new ArrayList<>();
        adoptiveParents.add(adoptiveParent);
        List<AdoptiveParentRecord> adoptiveParentRecords = new ArrayList<>();
        adoptiveParentRecords.add(adoptiveParentTestPositive);

        when(adoptiveParentRepo.findAll()).thenReturn(adoptiveParents);

        when(adoptiveParentMapper.toRecordList(adoptiveParents)).thenReturn(adoptiveParentRecords);

        assertThat(adoptiveParentService.getListOfAdoptiveParent()).asList().contains(adoptiveParentTestPositive);

        verify(adoptiveParentRepo, times(1)).findAll();

    }

    @Test
    void getListOfAdoptiveParentNegative() {
        when(adoptiveParentRepo.findAll()).thenReturn(null);

        when(adoptiveParentMapper.toRecordList(null)).thenReturn(null);

        assertThatExceptionOfType(ElemNotFound.class).isThrownBy(() -> adoptiveParentService.getListOfAdoptiveParent());

        verify(adoptiveParentRepo, times(1)).findAll();

    }

    @Test
    void updateAdoptiveParentPositive() {
        when(adoptiveParentRepo.findById(anyLong())).thenReturn(Optional.ofNullable(adoptiveParent));

        when(adoptiveParentMapper.toEntity(adoptiveParentTestPositive)).thenReturn(adoptiveParent);

        when(adoptiveParentRepo.save(adoptiveParent)).thenReturn(adoptiveParent);

        when(adoptiveParentMapper.toRecord(adoptiveParent)).thenReturn(adoptiveParentTestPositive);

        assertThat(adoptiveParentService.updateAdoptiveParent(ANYLONG, adoptiveParentTestPositive)).isEqualTo(adoptiveParentTestPositive);

        verify(adoptiveParentRepo, times(1)).findById(any());

        verify(adoptiveParentRepo, times(1)).save(adoptiveParent);
    }

    @Test
    void updateAdoptiveParentNegative() {
        assertThatExceptionOfType(ElemNotFound.class).isThrownBy(() -> adoptiveParentService.updateAdoptiveParent(1, null));
        assertThatExceptionOfType(ElemNotFound.class).isThrownBy(() -> adoptiveParentService.updateAdoptiveParent(0, adoptiveParentTestPositive));
    }

}
