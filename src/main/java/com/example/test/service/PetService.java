package com.example.test.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.whiskerspawstailtelegrambot.entity.Pet;
import pro.sky.whiskerspawstailtelegrambot.exception.ElemNotFound;
import pro.sky.whiskerspawstailtelegrambot.mapper.PetMapper;
import pro.sky.whiskerspawstailtelegrambot.record.PetRecord;
import pro.sky.whiskerspawstailtelegrambot.repository.PetRepository;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

import static java.nio.file.StandardOpenOption.CREATE_NEW;


/**
 * Сервис слой для питомца
 */
@Service
@Slf4j
@Transactional
public class  PetService {

    @Value("${pet.photo.dir.path}")
    private String petDir;

    PetRepository petRepository;
    PetMapper petMapper;


    public PetService(PetRepository petRepository, PetMapper petMapper) {
        this.petRepository = petRepository;
        this.petMapper = petMapper;
    }


    /**
     * Получение питомца в БД по id
     * @param petId
     * @return возвращает питомца
     */
    public PetRecord findPet(Long petId) { //Get
        log.info("Поиск питомца в БД" + petId);
        Pet pet = petRepository.findById(petId).orElseThrow(ElemNotFound::new);
        return petMapper.toRecord(pet);
    }

    /**
     * Коллекция всех питомцев в БД
     * @return список всех питомцев хранящиеся в БД
     */
    public Collection<PetRecord> findAllPet() { //GetAll
        log.info("Поиск всех питомцев в БД");
        return petMapper.toRecordList(petRepository.findAll());
    }


    /**
     * Удаление сбаки из БД по id
     *
     * @param petId
     */
    public PetRecord removePet(Long petId) { //Delete
        log.info("Поиск питомца в БД");
        PetRecord petRecord = findPet(petId);
        petRepository.deleteById(petRecord.getId());
        return petRecord;
    }

    /**
     * Изменение питомца в БД
     *
     * @param petId
     * @param fullName
     * @param age
     * @param description
     * @param photo
     * @throws IOException
     */
    public void editPet(Long petId, String fullName, String age, String description, MultipartFile photo) throws IOException { //Put
        log.info("Изменение данных питомца в БД");
        Pet pet = petMapper.toEntity(findPet(petId));
        if (fullName != null && !fullName.isEmpty() && !fullName.isBlank()) {
            pet.setFullName(fullName);
        }
        if (age != null && !age.isEmpty() && !age.isBlank()) {
            pet.setAge(age);
        }
        if (description != null && !description.isEmpty() && !description.isBlank()) {
            pet.setDescription(description);
        }
        petRepository.save(pet);
        PetRecord petRecord = petMapper.toRecord(pet);
        if (photo != null) {
            uploadPhoto(petRecord.getId(), photo);
        }
    }

    /**
     * Добавление id усыновителя в БД в таблицу Pet
     * @param petId
     * @param adoptiveParentId
     */
    public void addIdAdoptiveParent(Long petId, Long adoptiveParentId) {//Put
        log.info("Изменение данных питомца в БД");
        Pet pet = petMapper.toEntity(findPet(petId));
        petRepository.addIdAdoptiveParent(petId, adoptiveParentId);

    }

    /**
     * загрузка фотографии питомца в БД
     * @param petId
     * @param file
     * @throws IOException
     */
    public void uploadPhoto(Long petId, MultipartFile file) throws IOException { //Put фото
        PetRecord petRecord = findPet(petId);
        Path filePath = Path.of(petDir, petId + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = file.getInputStream();
            OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
            BufferedInputStream bis = new BufferedInputStream(is, 1024);
            BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        Pet pet = petMapper.toEntity(petRecord);
        pet.setFilePath(filePath.toString());
        pet.setFileSize(file.getSize());
        pet.setMediaType(file.getContentType());
        pet.setPhoto(file.getBytes());
        petRepository.save(pet);
    }

    /**
     * вспомогательный медот для загрузки фотографий
     * @return расширение файла
     */
    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * Добавление нового питомца в БД
     *
     * @param fullName
     * @param age
     * @param description
     * @param photo
     * @throws IOException
     */
    public void addPet(String fullName, String age, String description, MultipartFile photo) throws IOException { //Post
        log.info("Добавление питомца в БД");
        Pet pet = new Pet();
        if (fullName != null && !fullName.isEmpty() && !fullName.isBlank()) {
            pet.setFullName(fullName);
        }
        if (age != null && !age.isEmpty() && !age.isBlank()) {
            pet.setAge(age);
        }
        if (description != null && !description.isEmpty() && !description.isBlank()) {
            pet.setDescription(description);
        }
        petRepository.save(pet);
        PetRecord petRecord = petMapper.toRecord(pet);
        if (photo != null) {
            uploadPhoto(petRecord.getId(), photo);
        }


    }





}
