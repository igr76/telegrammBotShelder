package com.example.test.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Collection;

@RestController
@Getter
@Setter
@Slf4j
@RequestMapping("/pets")
@Tag(name = "PetController",
    description = "The Admin API")
public class PetController {
  private final PetService petService;
  private final PetMapper petMapper;


  public PetController(PetService petService, PetMapper petMapper) {
    this.petService = petService;
    this.petMapper = petMapper;
  }


  @Operation(summary = "Получение питомца по id")
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "Получена сущность питомца",
          content = {
              @Content(
                  mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = PetRecord.class)))
          }
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Либо не введены парметры, либо нет объекта по указанному идентификатору" +
              "и идентификаторы меньше 1",
          content = {
              @Content(
                  mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))
          }
      )
  })
  @GetMapping
  public ResponseEntity<?> findPet(@RequestParam(name = "id") Long id) {
    PetRecord petRecord = petService.findPet(id);
    return ResponseEntity.ok().body(petRecord);
  }

  @Operation(summary = "Получение списка всех питомцев в приюте")
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "Получен список всех питомцев из БД",
          content = {
              @Content(
                  mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = PetRecord.class)))
          }
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Либо не введены парметры, либо нет объекта по указанному идентификатору" +
              "и идентификаторы меньше 1",
          content = {
              @Content(
                  mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))
          }
      )
  })
  @GetMapping("/all")
  public ResponseEntity<?> findAllPet() {
    Collection<PetRecord> recordCollection = petService.findAllPet();
    return ResponseEntity.ok().body(recordCollection);
  }


  @Operation(summary = "Получение фото питомца по id")
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "Получена фото питомца",
          content = {
              @Content(
                  mediaType = "multipart/form-data",
                  array = @ArraySchema(schema = @Schema(implementation = PetRecord.class)))
          }
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Либо не введены парметры, либо нет объекта по указанному идентификатору" +
              "и идентификаторы меньше 1",
          content = {
              @Content(
                  mediaType = "multipart/form-data",
                  array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))
          }
      )
  })
  @GetMapping(value = "/photo", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<byte[]> getPetPhoto(@RequestParam(name = "id") Long id) {
    PetRecord pet = petService.findPet(id);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType(pet.getMediaType()));
    headers.setContentLength(pet.getPhoto().length);
    return ResponseEntity.status(HttpStatus.OK).headers(headers).body(pet.getPhoto());
  }

  @Operation(summary = "Добавление нового питомца в БД")
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "Добавлен новый питомец",
          content = {
              @Content(
                  mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = PetRecord.class)))
          }
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Не введены парметры",
          content = {
              @Content(
                  mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))
          }
      )
  })
  @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> addPet(
      @RequestParam(required = false, name = "name") String fullName,
      @RequestParam(required = false, name = "age") String age,
      @RequestParam(required = false, name = "des") String description,
      @RequestParam(required = false, name = "photo") MultipartFile photo) throws IOException {
    petService.addPet(fullName, age, description, photo);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Изменение данных питомца")
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "Изменены данные питомца",
          content = {
              @Content(
                  mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = PetRecord.class)))
          }
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Либо не введены парметры, либо нет объекта по указанному идентификатору" +
              "и идентификаторы меньше 1",
          content = {
              @Content(
                  mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))
          }
      )
  })
  @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> editPet(
      @RequestParam(name ="id") Long id,
      @RequestParam(required = false, name = "name") String fullName,
      @RequestParam(required = false, name = "age") String age,
      @RequestParam(required = false, name = "des") String description,
      @RequestParam(required = false) MultipartFile photo) throws IOException {
    petService.editPet(id, fullName, age, description, photo);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Добавление id усыновителя в БД в таблицу Pet")
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "Добавлено id усыновителя",
          content = {
              @Content(
                  mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = PetRecord.class)))
          }
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Либо не введены парметры, либо нет объекта по указанному идентификатору" +
              "и идентификаторы меньше 1",
          content = {
              @Content(
                  mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))
          }
      )
  })
  @PutMapping(value = "/parent")
  public ResponseEntity<PetRecord> addIdAdoptiveParent(@RequestParam(name = "petId") Long petId, @RequestParam(name = "id") Long adoptiveParentId) {
    petService.addIdAdoptiveParent(petId, adoptiveParentId);
    return ResponseEntity.ok().build();
  }


  @Operation(summary = "Удаление питомца по id")
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "Удален питомцев из БД",
          content = {
              @Content(
                  mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = PetRecord.class)))
          }
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Либо не введены парметры, либо нет объекта по указанному идентификатору" +
              "и идентификаторы меньше 1",
          content = {
              @Content(
                  mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))
          }
      )
  })
  @DeleteMapping
  public ResponseEntity<PetRecord> removePet(@RequestParam(name = "id") Long id) {
    petService.removePet(id);
    return ResponseEntity.ok().build();
  }

}
