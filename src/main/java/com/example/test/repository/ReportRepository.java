package com.example.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.whiskerspawstailtelegrambot.entity.Report;

/**
 * Репозиторий для питомцев
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

   Report getReportByPet_id(Long id);

}
