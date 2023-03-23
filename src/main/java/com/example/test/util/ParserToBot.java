package com.example.test.util;

import org.springframework.stereotype.Component;
import pro.sky.whiskerspawstailtelegrambot.exception.ElemNotFound;
import pro.sky.whiskerspawstailtelegrambot.record.PetRecord;
import pro.sky.whiskerspawstailtelegrambot.record.VolunteerRecord;
import pro.sky.whiskerspawstailtelegrambot.textAndButtonsAndKeyboard.AllText;

import java.util.Collection;

/**
 * Класс для парсинга из бд.
 */

@Component
public class ParserToBot {

  /**
   * Парсит строки из каждой сущнсоти в одну строку. Тут конкретно для ответа на вызов
   * /calltovolunteer
   *
   * @param volunteerList принимаем лист волонтеров
   * @return отпарсиная строка для ответа
   */
  public String parserVolunteer(Collection<VolunteerRecord> volunteerList) {
    if (volunteerList.isEmpty()) {
      throw new ElemNotFound();
    }
    StringBuilder stringBuilder = new StringBuilder();
    int count = 0;
    for (VolunteerRecord volunteerRecord : volunteerList) {
      stringBuilder.append(++count)
          .append(") ")
          .append(volunteerRecord)
          .append('\n').append('\n');
    }
    return stringBuilder.toString();
  }

  public String parserPet(Collection<PetRecord> petRecords) {
    if (petRecords.isEmpty()) {
      return null;
    }

    StringBuilder stringBuilder = new StringBuilder();
    int count = 0;
    for (PetRecord petRecord : petRecords) {
      stringBuilder.append(++count)
          .append(") ")
          .append("ID: ")
          .append(petRecord.getId())
          .append('\n')
          .append("Имя: ")
          .append(petRecord.getFullName())
          .append('\n')
          .append("Возраст: ")
          .append(petRecord.getAge())
          .append(".")
          .append('\n')
          .append(AllText.DELIMITER_FOR_PARSER_PETS);
    }
    return stringBuilder.toString();
  }

  public Long parserStringPetId(String textMessage) {
    if (textMessage == null) {
      return null;
    }
    try {
      int index =  textMessage.indexOf("ID: ");
      String id = textMessage.substring(index, index + 1);
      return Long.parseLong(id);
    }catch (Exception e){
      return null;
    }

  }
}
