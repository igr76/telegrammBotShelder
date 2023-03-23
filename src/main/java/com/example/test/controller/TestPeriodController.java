package com.example.test.controller;

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
 * Контроллер для управления испытательными сроками
 */
@RestController
@RequestMapping("/testPeriod")
@Slf4j
@Tag(name = "TestPeriodController",
        description = "The Admin API")
public class TestPeriodController {

    private final TestPeriodService testPeriodService;

    public TestPeriodController(TestPeriodService testPeriodService) {
        this.testPeriodService = testPeriodService;
    }

    @Operation(summary = "Получить животных, у которых нет родителей")
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получен список животных",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = PetRecord.class)))
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
    @GetMapping(value = "/getListOfPetDoesntHaveParent")
    public ResponseEntity<Collection<PetRecord>> getListOfPetDoesntHaveParentRecord() {
        return ResponseEntity.ok(testPeriodService.getPetsDoesntHaveParent());
    }

    @Operation(summary = "Усыновление и установка испытательного срока")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Усыновление произошло, испытательный срок начался",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PetRecord.class)))
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
    @PostMapping("/addAdoptiveParent/{petId}")
    public ResponseEntity<PetRecord> addAdoptiveParent(@NotBlank(message = "petId пустой")
                                                       @Min(value = 1, message = "Идентификатор питомца " +
                                                               "должен быть больше 0")
                                                       @PathVariable(name = "petId")
                                                       @Parameter(description = "Идентификатор питомца",
                                                               example = "1")
                                                       Long petId,
                                                       @RequestParam
                                                       @NotBlank(message = "adoptiveParentId пустой")
                                                       @Min(value = 1, message = "Идентификатор усыновителя " +
                                                               "должен быть больше 0")
                                                       @Parameter(description = "Идентификатор усыновителя",
                                                               example = "1")
                                                       Long adoptiveParentId,
                                                       @RequestParam
                                                       @NotBlank(message = "испытательный срок пустой")
                                                       @Min(value = 1, message = "Испытательный срок " +
                                                               "должен быть больше 0")
                                                       @Parameter(description = "Длительность испытательного срока " +
                                                               "в днях",
                                                               example = "30")
                                                       Integer testPeriodDays
    ) {

        return ResponseEntity.ok(testPeriodService.addAdoptiveParent(petId, adoptiveParentId, testPeriodDays));
    }

    @Operation(summary = "Возвращение питомца в приют")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Питомца вернули в приют",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PetRecord.class)))
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
    @PostMapping("/returnPetToShelter/{petId}")
    public ResponseEntity<PetRecord> returnPetToShelter(@NotBlank(message = "petId пустой")
                                                        @Min(value = 1, message = "Идентификатор питомца " +
                                                                "должен быть больше 0")
                                                        @PathVariable(name = "petId")
                                                        @Parameter(description = "Идентификатор питомца",
                                                                example = "1")
                                                        Long petId
    ) {

        return ResponseEntity.ok(testPeriodService.returnPetToShelter(petId));
    }

    @Operation(summary = "Увеличение испытательного срока")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Испытательный срок увеличен",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PetRecord.class)))
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
    @PostMapping("/extendTestPeriod/{petId}")
    public ResponseEntity<PetRecord> extendTestPeriod(@NotBlank(message = "petId пустой")
                                                      @Min(value = 1, message = "Идентификатор питомца " +
                                                              "должен быть больше 0")
                                                      @PathVariable(name = "petId")
                                                      @Parameter(description = "Идентификатор питомца",
                                                              example = "1")
                                                      Long petId,
                                                      @RequestParam
                                                      @NotBlank(message = "испытательный срок пустой")
                                                      @Min(value = 1, message = "Испытательный срок " +
                                                              "должен быть больше 0")
                                                      @Parameter(description = "Испытательный срок увеличен " +
                                                              "на дней",
                                                              example = "14")
                                                      Integer days
    ) {

        return ResponseEntity.ok(testPeriodService.extendTestPeriod(petId, days));
    }

    @Operation(summary = "Завершение испытательного срока")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Испытательный срок успешно завершен",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PetRecord.class)))
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
    @PostMapping("/completeTestPeriod/{petId}")
    public ResponseEntity<PetRecord> completeTestPeriod(@NotBlank(message = "petId пустой")
                                                        @Min(value = 1, message = "Идентификатор питомца " +
                                                                "должен быть больше 0")
                                                        @PathVariable(name = "petId")
                                                        @Parameter(description = "Идентификатор питомца",
                                                                example = "1")
                                                        Long petId
    ) {

        return ResponseEntity.ok(testPeriodService.completeTestPeriod(petId));
    }

    @Operation(summary = "Получить животных на испытательном сроке")
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получен список животных на испытательном сроке",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = PetRecord.class)))
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
    @GetMapping(value = "/getPetsHaveTestPeriod")
    public ResponseEntity<Collection<PetRecord>> getPetsHaveTestPeriod() {
        return ResponseEntity.ok(testPeriodService.getPetsHaveTestPeriod());
    }

    @Operation(summary = "Получить питомцев, у которых заканчивается испытательный срок")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Список получен",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PetRecord.class)))
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
    @GetMapping("/getPetsWithEndedTestPeriod")
    public ResponseEntity<Collection<PetRecord>> getPetsWithEndedTestPeriod(@RequestParam
                                                                            @NotBlank(message = " количество дней пусто")
                                                                            @Min(value = 0, message = "Количество дней " +
                                                                                    "должно быть не меньше 0")
                                                                            @Parameter(description = "Количество дней до завершения " +
                                                                                    "испытательного срока",
                                                                                    example = "1")
                                                                            Integer days
    ) {

        return ResponseEntity.ok(testPeriodService.getPetsWithEndedTestPeriod(days));
    }
}
