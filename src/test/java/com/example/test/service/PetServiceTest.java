package com.example.test.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.whiskerspawstailtelegrambot.entity.Pet;
import pro.sky.whiskerspawstailtelegrambot.entity.Shelter;
import pro.sky.whiskerspawstailtelegrambot.exception.ElemNotFound;
import pro.sky.whiskerspawstailtelegrambot.mapper.PetMapper;
import pro.sky.whiskerspawstailtelegrambot.mapper.PetMapperImpl;
import pro.sky.whiskerspawstailtelegrambot.record.PetRecord;
import pro.sky.whiskerspawstailtelegrambot.repository.PetRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    PetRepository petRepository;
    @Mock
    PetMapper petMapper;
    @InjectMocks
    PetService out;
    PetMapper mapper = new PetMapperImpl();

    @Test
    void findPetPositiveTest() {
        Pet pet = getTestPet();
        lenient().when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        lenient().when(petMapper.toRecord(pet)).thenReturn(mapper.toRecord(pet));
        PetRecord exceptedRecord = mapper.toRecord(pet);
        PetRecord actual = out.findPet(1L);
        assertEquals(actual, exceptedRecord);
    }

    @Test
    void findPetNegativeTest() {
        lenient().when(petRepository.findById(anyLong())).thenThrow(ElemNotFound.class);
        assertThrows(ElemNotFound.class, () -> out.findPet(1L));
    }

    @Test
    void findAllPetEmptyListTest() {
        lenient().when(petRepository.findAll()).thenReturn(List.of());
        assertTrue(out.findAllPet().isEmpty());
    }
    @Test
    void findAllPetPositiveTest() {
        List<Pet> pets = getTestPetList();
        List<PetRecord> dogRecords = getTestPetRecords();

        lenient().when(petRepository.findAll()).thenReturn(pets);
        lenient().when(petMapper.toRecordList(pets)).thenReturn(mapper.toRecordList(pets));

        List<PetRecord> checkedDogRecords = List.copyOf(out.findAllPet());

        assertEquals(checkedDogRecords.size(), dogRecords.size());
        assertTrue(out.findAllPet().containsAll(dogRecords));
    }

    @Test
    void removePetPositiveTest() {
        Pet pet = getTestPet();

        lenient().when(petRepository.findById(anyLong())).thenReturn(Optional.of(pet));
        lenient().when(petMapper.toRecord(pet)).thenReturn(mapper.toRecord(pet));
        lenient().doNothing().when(petRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> out.removePet(1L));

    }

    @Test
    void removePetNegativeTest() {

        lenient().when(petRepository.findById(anyLong())).thenThrow(ElemNotFound.class);
        assertThrows(ElemNotFound.class, () -> out.removePet(1L));

    }

    @Test
    void editPetPositiveTest() throws IOException {
        Pet pet = getTestPet();
        PetRecord petRecord = mapper.toRecord(pet);
        String age = petRecord.getAge();
       PetService petService = mock(PetService.class);
        doNothing().when(petService).editPet(petRecord.getId(), null, "1", null, null);
        Assertions.assertThatNoException().isThrownBy(() -> petService.editPet(pet.getId(), null, "1", null, null));
        verify(petService, times(1)).editPet(pet.getId(), null, "1", null, null);

    }

    @Test
    void editPetNegativeTest() {
        lenient().when(petRepository.findById(anyLong())).thenThrow(ElemNotFound.class);
        assertThrows(ElemNotFound.class, () -> out.editPet(1L, "", "1", "", getTestPhoto()));
    }

    @Test
    void uploadPhotoNegativeTest() {
        lenient().when(petRepository.findById(anyLong())).thenThrow(ElemNotFound.class);
        assertThrowsExactly(ElemNotFound.class, () -> out.uploadPhoto(1L, getTestPhoto()));
    }

    @Test
    void uploadPhotoPositiveTest() throws IOException {
        Pet pet = getTestPet();
        MultipartFile photo = getTestPhoto();
        pet.setPhoto(photo.getBytes());
        pet.setFilePath("/ptath");
        pet.setFileSize(photo.getSize());
        pet.setMediaType(photo.getContentType());
        Long id = pet.getId();
        PetService petService = mock(PetService.class);
        doNothing().when(petService).uploadPhoto(pet.getId(), photo);
        Assertions.assertThatNoException().isThrownBy(() ->petService.uploadPhoto(pet.getId(), photo));
        verify(petService, times(1)).uploadPhoto(id, photo);

    }

    @Test
    void addIdAdoptiveParentTest() {

        PetService petService = mock(PetService.class);
        doNothing().when(petRepository).addIdAdoptiveParent(1L, 1L);
        petRepository.addIdAdoptiveParent(1L, 1L);
        verify(petRepository, times(1)).addIdAdoptiveParent(1L, 1L);
    }


    /**
     * пет для теста
     * @return
     */
    private Pet getTestPet() {
        Pet pet = new Pet();
        pet.setId(1L);
        pet.setFullName("Шарик Бобикович");
        pet.setAge("3");
        pet.setDescription("двортерьер");
        pet.setFileSize(1024);
        pet.setShelter(getTestShelter());
        pet.setPhoto(null);
        pet.setMediaType("");
        pet.setReports(List.of());
        pet.setAdoptiveParent(null);
        return pet;
    }

    /**
     * шелтер для теста
     * @return
     */
    private Shelter getTestShelter() {
        Shelter shelter = new Shelter();
        shelter.setId(1L);
        return shelter;
    }

    /**
     * коллекция петов
     * @return
     */
    private List<Pet> getTestPetList() {
        List<Pet> pets = new ArrayList<>();

        Pet pet = new Pet();
        pet.setId(2L);
        pet.setFullName("Бобик");
        pet.setAge("5");
        pet.setDescription("двортерьер");
        pet.setFileSize(1024);
        pet.setShelter(getTestShelter());
        pet.setPhoto(null);
        pet.setMediaType("");
        pet.setReports(List.of());
        pet.setAdoptiveParent(null);

        pets.add(pet);
        pets.add(getTestPet());
        return pets;
    }

    /**
     * Преобразование коллекции петов в рекорды
     * @return
     */

    private List<PetRecord> getTestPetRecords() {
        List<PetRecord> petRecords = new ArrayList<>();
        for (Pet pet : getTestPetList()) {
            petRecords.add(mapper.toRecord(pet));
        }
        return petRecords;
    }




    /**
     * Файл фото для теста
     * @return
     */

    private MockMultipartFile getTestPhoto() {
        return new MockMultipartFile("data", "photo.jpeg",
            MediaType.MULTIPART_FORM_DATA_VALUE, "photo.jpeg".getBytes());
    }
}