package com.example.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.whiskerspawstailtelegrambot.entity.Pet;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

/**
 * Репозиторий для питомцев
 */
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE pet SET adoptive_parent_id = :adoptiveParentId,  test_period = TRUE, " +
            "test_period_time = now() WHERE id = :petId")
    void addIdAdoptiveParent(Long petId, Long adoptiveParentId);

    /**
     * Находит всех питомцев, у которых нет родителей
     * @return  - список животных в приюте
     */
    Collection<Pet> findPetByAdoptiveParentIsNull();

    /**
     * Находит питомца, у которого есть родители
     * @param petId         - идентификатор питомца
     * @param testPeriod    - находится ли на испытательном сроке
     * @return              - возвращает питомца, есл все условия соблюдены
     */
    Optional<Pet> findPetByAdoptiveParentIsNotNullAndIdAndTestPeriod(Long petId, Boolean testPeriod);

    /**
     * Находит список питомцев на испытательном сроке
     * @param isTestPeriod - true, если нужны питомцы на испытательном сроке
     * @return             - список питомцев
     */
    Collection<Pet> findPetByTestPeriod(Boolean isTestPeriod);

    /**
     * Находит питомца по id, если у него нет родителя
     * @param petId - идентификатор питомца
     * @return      - сущность питомца
     */
    Optional<Pet> findPetByIdAndAdoptiveParentIsNull(Long petId);

    Collection<Pet> findPetsByTestPeriodAndTestPeriodTimeBefore(boolean testPeriod, LocalDateTime testPeriodTime);
}
