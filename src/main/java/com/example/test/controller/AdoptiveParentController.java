package com.example.test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;

/**
 * Котроллер для волонтеров.
 */
@RestController
@Getter
@Setter
@Slf4j
@Tag(name = "AdoptiveParentController",
        description = "The Admin API")
public class AdoptiveParentController {
    final AdoptiveParentService service;

    public AdoptiveParentController(AdoptiveParentService service) {
        this.service = service;
    }

    //пока спрятали
    @Operation(summary = "Получить массив отчетов по идентификатору родитетеля и питомца",
            hidden = true)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Get list of Reports",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ReportRecord.class)))
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
    @RequestMapping(value = "/getReportByParentAndPet", method = RequestMethod.GET)
    public ResponseEntity<Collection<ReportRecord>> getReportByParentAndPet(
            @NotBlank(message = "parentId is empty")
            @Min(value = 1, message = "Идентификатор родителя должен быть больше 0")
            @RequestParam(name = "parentId")
            @Parameter(description = "Идентификатор родителя",
                    example = "1")
            long parentId,
            @NotBlank(message = "petId is empty")
            @Min(value = 1, message = "Идентификатор питомца должен быть больше 0")
            @RequestParam(name = "petId")
            @Parameter(description = "Идентификатор питомца",
                    example = "1")
            long petId) {
        return ResponseEntity.ok(service.getReportByParentAndPet(parentId, petId));
    }

    @Operation(summary = "Получить усыновителя по id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Get AdoptiveParent by current parentId,list of pet and reports",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AdoptiveParentRecord.class)))
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
    @RequestMapping(value = "/getAdoptiveParentByID", method = RequestMethod.GET)
    public ResponseEntity<AdoptiveParentRecord> getAdoptiveParentByID
            (
                    @NotBlank(message = "parentId is empty")
                    @Min(value = 1, message = "Идентификатор родителя должен быть больше 0")
                    @RequestParam(name = "parentId")
                    @Parameter(description = "Идентификатор родителя",
                            example = "1")
                    long parentId
            ) {
        return ResponseEntity.ok(service.getAdoptiveParentByID(parentId));
    }

    @Operation(summary = "Удалить усыновителя по id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Delete adoptiveParent from db",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AdoptiveParentRecord.class)))
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
    @RequestMapping(value = "/deleteAdoptiveParentByID", method = RequestMethod.DELETE)
    public ResponseEntity<AdoptiveParentRecord> deleteAdoptiveParentByID
            (
                    @NotBlank(message = "parentId is empty")
                    @Min(value = 1, message = "Идентификатор родителя должен быть больше 0")
                    @RequestParam(name = "parentId")
                    @Parameter(description = "Идентификатор родителя",
                            example = "1")
                    long parentId
            ) {
        return ResponseEntity.ok(service.deleteAdoptiveParentByID(parentId));
    }


    @Operation(summary = "Добавить усыновителя")
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Add adoptiveParent to db",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = AdoptiveParentRecord.class)))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Посмотрите на правильность ввода всех полей. Не должно быть null и пустая строка",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = MethodArgumentNotValidException.class)))
                            }
                    )
            })
    @RequestMapping(value = "/addAdoptiveParent", method = RequestMethod.POST)
    public ResponseEntity<AdoptiveParentRecord> addAdoptiveParent
            (
                    @Valid
                    @RequestBody
                    AdoptiveParentRecord adoptiveParentRecord
            ) {
        return ResponseEntity.ok(service.addAdoptiveParent(adoptiveParentRecord));
    }

    @Operation(summary = "Получить всех усыновителей")
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get list of parent from db",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = AdoptiveParentRecord.class)))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Нет такой сущности",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = MethodArgumentNotValidException.class)))
                            }
                    )
            })
    @RequestMapping(value = "/getListOfAdoptiveParent", method = RequestMethod.GET)
    public ResponseEntity<Collection<AdoptiveParentRecord>> getListOfAdoptiveParent() {
        return ResponseEntity.ok(service.getListOfAdoptiveParent());
    }

    @Operation(summary = "Апдейт по ID")
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get new Entity",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = AdoptiveParentRecord.class)))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Посмотрите на правильность ввода всех полей. Не должно быть null и пустая строка",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = MethodArgumentNotValidException.class)))
                            }
                    )
            })
    @RequestMapping(value = "/updateAdoptiveParent", method = RequestMethod.PUT)
    public ResponseEntity<AdoptiveParentRecord> updateAdoptiveParent(
            @NotBlank(message = "parentId is empty")
            @Min(value = 1, message = "Идентификатор родителя должен быть больше 0")
            @RequestParam(name = "parentId")
            @Parameter(description = "Идентификатор родителя",
                    example = "1")
            long parentId,
            @Valid
            @RequestBody
            AdoptiveParentRecord adoptiveParentRecord
    ) {
        return ResponseEntity.ok(service.updateAdoptiveParent(parentId, adoptiveParentRecord));
    }

    /**
     * Поиск id усыновителя
     *
     * @param fullName Поспелов Дмитрий александрови (необязательный параметр)
     * @param phone    Телефон усыновителя (необязательный параметр)
     * @param chatId   chatId (необязательный параметр)
     * @return id усыновителя
     */
    @Operation(summary = "Поиск Id усыновителя")
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get Id AdoptiveParent",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = Long.class))
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Нужно заполнить хотя бы один параметр",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = ErrorResponse.class))
                            }
                    )
            })
    @RequestMapping(value = "/getParentIdByNameAndPhoneAndChatId", method = RequestMethod.GET)
    public ResponseEntity<Long> getParentIdByNameAndPhoneAndChatId(
            @Size(message = "Длина не должна быть меньше 5 знаков и не больше 30", min = 5, max = 30)
            @RequestParam(name = "fullName", required = false)
            @Parameter(description = "Полное имя родителя",
                    example = "Поспелов Дмитрий александрович")
            String fullName,
            @Size(message = "Длина не должна быть меньше 5 знаков и не больше 30", min = 5, max = 30)
            @RequestParam(name = "phone", required = false)
            @Parameter(description = "Телефон усыновителя",
                    example = "89246554324")
            String phone,
            @RequestParam(name = "chatId", required = false)
            @Parameter(description = "Chat Id",
                    example = "23")
            Long chatId
    ) {
        return ResponseEntity.ok(service
                .getParentIdByNameAndPhoneAndChatId(fullName, phone, chatId));
    }
}


