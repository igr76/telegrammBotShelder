package com.example.test.controller;

import com.example.test.exception.ErrorResponse;
import com.example.test.record.VolunteerRecord;
import com.example.test.service.VolunteerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Collection;

/**
 * Контроллер для волонтеров
 */
@RestController
@RequestMapping("/volunteer")
@Slf4j
@Tag(name = "VolunteerController",
        description = "The Admin API")
public class  VolunteerController {

    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @Operation(summary = "Получить волонтера по id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Получен волонтер по текущему id",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = VolunteerRecord.class)))
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
    @GetMapping("{volunteerId}")
    public ResponseEntity<VolunteerRecord> getVolunteer
            (
                    @NotBlank(message = "volunteerId пустой")
                    @Min(value = 1, message = "Идентификатор волонтера должен быть больше 0")
                    @PathVariable(name = "volunteerId")
                    @Parameter(description = "Идентификатор волонтера",
                            example = "1")
                    long volunteerId
            ) {
        return ResponseEntity.ok(volunteerService.getVolunteerById(volunteerId));
    }

    @Operation(summary = "Удалить волонтера по id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Волонтер удален из БД",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = VolunteerRecord.class)))
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
    @DeleteMapping("{volunteerId}")
    public ResponseEntity<VolunteerRecord> deleteVolunteerById
            (
                    @NotBlank(message = "volunteerId пустой")
                    @Min(value = 1, message = "Идентификатор волонтера должен быть больше 0")
                    @PathVariable(name = "volunteerId")
                    @Parameter(description = "Идентификатор волонтера",
                            example = "1")
                    long volunteerId
            ) {
        return ResponseEntity.ok(volunteerService.deleteVolunteerById(volunteerId));
    }


    @Operation(summary = "Получить всех волонтеров")
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получен список волонтеров",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = VolunteerRecord.class)))
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
    @GetMapping(value = "/getListOfVolunteer")
    public ResponseEntity<Collection<VolunteerRecord>> getListOfVolunteerRecord(){
        return ResponseEntity.ok(volunteerService.getAllVolunteers());
    }

    @Operation(summary = "Добавление нового волонтера в БД")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Добавлен новый волонтер",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = VolunteerRecord.class)))
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не введены параметры",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))
                    }
            )
    })
    @PostMapping("/addVolunteer")
    public ResponseEntity<VolunteerRecord> addVolunteer(@RequestParam
                                                            @NotBlank(message = "Полное имя пустое")
                                                            @Parameter(description = "Полное имя волонтера",
                                                                    example = "Иванов Иван Иванович")
                                                            String fullName,
                                                        @RequestParam
                                                            @NotBlank(message = "Номер телефона пуст")
                                                            @Parameter(description = "Номер телефона волонтера",
                                                                    example = "+79876543210")
                                                            String phone,
                                                        @RequestParam(required = false)
                                                            @NotBlank(message = "Отсутствует информация о волонтере")
                                                            @Parameter(description = "Информация о волонтере",
                                                                    example = "Это волонтер... Я занимаюсь...")
                                                            String info,
                                                        @RequestParam(required = false)
                                                            @NotBlank(message = "Отсутствует время работы волонтера")
                                                            @Parameter(description = "Время работы волонтера",
                                                                    example = "пн-пт с 9:00 до 17:00")
                                                            String schedule) {

        return ResponseEntity.ok(volunteerService.addVolunteer(fullName, phone, info, schedule));
    }

    @Operation(summary = "Обновление волонтера в БД")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные волонтера обновлены",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = VolunteerRecord.class)))
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не введены параметры",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))
                    }
            )
    })
    @PostMapping("{volunteerId}")
    public ResponseEntity<VolunteerRecord> updateVolunteer(@NotBlank(message = "volunteerId пустой")
                                                               @Min(value = 1, message = "Идентификатор волонтера " +
                                                                       "должен быть больше 0")
                                                               @PathVariable(name = "volunteerId")
                                                               @Parameter(description = "Идентификатор волонтера",
                                                                       example = "1")
                                                               Long volunteerId,
                                                           @RequestParam(required = false)
                                                                @Parameter(description = "Полное имя волонтера",
                                                                       example = "Иванов Иван Иванович")
                                                                String fullName,
                                                           @RequestParam(required = false)
                                                               @Parameter(description = "Номер телефона волонтера",
                                                                       example = "+79876543210")
                                                               String phone,
                                                           @RequestParam(required = false)
                                                               @Parameter(description = "Информация о волонтере",
                                                                       example = "Это волонтер... Я занимаюсь...")
                                                               String info,
                                                           @RequestParam(required = false)
                                                               @Parameter(description = "Время работы волонтера",
                                                                       example = "пн-пт с 9:00 до 17:00")
                                                               String schedule,
                                                           @RequestParam(required = false)
                                                               @Parameter(description = "ID приюта, в котором " +
                                                                       "работает волонтер",
                                                                       example = "1")
                                                               Long shelterId) {

        return ResponseEntity.ok(volunteerService.updateVolunteer(volunteerId, fullName, phone, info, schedule, shelterId));
    }

}
