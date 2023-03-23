package com.example.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.whiskerspawstailtelegrambot.entity.Shelter;

/**
 * Репозиторий для приюта
 */
@Repository
public interface ShelterRepo extends JpaRepository<Shelter, Long> {

}
