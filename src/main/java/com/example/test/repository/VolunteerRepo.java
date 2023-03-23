package com.example.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.whiskerspawstailtelegrambot.entity.Volunteer;

import java.util.Optional;

/**
 * Репозиторий для волонтеров
 */
@Repository
public interface VolunteerRepo extends JpaRepository<Volunteer, Long> {
    Optional<Volunteer> findVolunteerByFullName(String fullName);
}
