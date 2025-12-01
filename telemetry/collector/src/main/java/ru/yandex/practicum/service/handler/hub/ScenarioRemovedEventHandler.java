package ru.yandex.practicum.service.handler.hub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto.PayloadCase;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.service.producer.CollectorKafkaProducer;

@Slf4j
@Component
public class ScenarioRemovedEventHandler extends BaseHubEventHandler<ScenarioRemovedEventAvro> {

    public ScenarioRemovedEventHandler(CollectorKafkaProducer producer) {
        super(producer);
    }

    @Override
    protected ScenarioRemovedEventAvro protoToAvro(HubEventProto eventProto) {
        log.info("Преобразование HubEventProto в ScenarioRemovedEventAvro");

        ScenarioRemovedEventAvro avro = ScenarioRemovedEventAvro.newBuilder()
                .setName(eventProto.getScenarioRemovedEvent().getName())
                .build();

        log.info("Преобразование HubEventProto в ScenarioRemovedEventAvro завершено");
        return avro;
    }

    @Override
    public HubEventProto.PayloadCase getMessageHubType() {
        return PayloadCase.SCENARIO_REMOVED_EVENT;
    }
}
