package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.hub.HubEvent;
import ru.yandex.practicum.model.sensor.SensorEvent;
import ru.yandex.practicum.service.CollectorService;

/**
 * API для передачи событий от датчиков и хабов
 */
@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
public class CollectorController {

    private final CollectorService collectorService;

    /**
     * Обработчик событий датчиков
     *
     * @param event событие датчика {@link SensorEvent}
     */
    @PostMapping("/sensors")
    public ResponseEntity<Void> collectSensorEvent(@Valid @RequestBody SensorEvent event) {
        log.info("Создание события SensorEvent на уровне контроллера");
        log.info("Получен SensorEvent: {}", event);

        collectorService.sendSensorEvent(event);

        log.info("Возврат результатов создания SensorEvent на уровень клиента");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Обработчик событий хабов
     *
     * @param event событие хаба {@link HubEvent}
     */
    @PostMapping("/hubs")
    public ResponseEntity<Void> collectHub(@Valid @RequestBody HubEvent event) {
        log.info("Создание события HubEvent на уровне контроллера");
        log.info("Получен HubEvent: {}", event);

        collectorService.sendHubEvent(event);

        log.info("Возврат результатов создания HubEvent на уровень клиента");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}