package com.example.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.whiskerspawstailtelegrambot.entity.AdoptiveParent;

/**
 * Репозиторий для усыновителей
 */
//@Repository
public interface AdoptiveParentRepo extends JpaRepository<AdoptiveParent, Long> {
    AdoptiveParent getAdoptiveParentByFullName(String fullName);
    AdoptiveParent getAdoptiveParentByPhone(String phone);
    AdoptiveParent getAdoptiveParentByChatId(Long chatId);




}
