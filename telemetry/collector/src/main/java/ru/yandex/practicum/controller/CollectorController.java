package ru.yandex.practicum.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc.CollectorControllerImplBase;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.service.handler.hub.HubEventHandler;
import ru.yandex.practicum.service.handler.sensor.SensorEventHandler;

/**
 * API для передачи событий от датчиков и хабов
 */
@GrpcService
@Slf4j
public class CollectorController extends CollectorControllerImplBase {

    private final Map<HubEventProto.PayloadCase, HubEventHandler> hubHandlers;
    private final Map<SensorEventProto.PayloadCase, SensorEventHandler> sensorHandlers;

    public CollectorController(Set<HubEventHandler> hubEventHandlers, Set<SensorEventHandler> sensorEventHandlers) {
        this.hubHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageHubType, Function.identity())
                );
        this.sensorHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageSensorType, Function.identity())
                );
    }

    /**
     * Обработчик событий датчиков
     */
    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        log.info("Создание события SensorEvent на уровне контроллера");

        try {
            log.info("Получен SensorEventProto: {}", request.getPayloadCase());

            if (sensorHandlers.containsKey(request.getPayloadCase())) {
                sensorHandlers.get(request.getPayloadCase()).handleEvent(request);
            } else {
                throw new IllegalArgumentException(
                        "Обработчик для SensorEventProto " + request.getPayloadCase() + " не найден.");
            }
            log.info("На уровне контроллера получена информация об успешном создании события SensorEventProto");

            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();

            log.info("Возврат результатов создания SensorEvent на уровень клиента");
        } catch (Exception e) {
            log.error("Ошибка обработки SensorEventProto", e);
            responseObserver.onError(
                    new StatusRuntimeException(Status.INTERNAL.withDescription(e.getMessage()).withCause(e)));
        }
    }

    /**
     * Обработчик событий хабов
     */
    @Override
    public void collectHubEvent(HubEventProto request, StreamObserver<Empty> responseObserver) {
        log.info("Создание события HubEvent на уровне контроллера");
        log.info("Получен HubEvent: {}", request);

        try {
            log.info("Получен HubEventProto: {}", request.getPayloadCase());

            if (hubHandlers.containsKey(request.getPayloadCase())) {
                hubHandlers.get(request.getPayloadCase()).handleEvent(request);
            } else {
                throw new IllegalArgumentException("Обработчик для HubEventProto " + request.getPayloadCase() + " не найден");
            }
            log.info("На уровне контроллера получена информация об успешном создании события HubEventProto");

            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();

            log.info("Возврат результатов создания HubEventProto на уровень клиента");
        } catch (Exception e) {
            log.error("Ошибка обработки HubEventProto", e);
            responseObserver.onError(
                    new StatusRuntimeException(Status.INTERNAL.withDescription(e.getMessage()).withCause(e)));
        }
    }
}