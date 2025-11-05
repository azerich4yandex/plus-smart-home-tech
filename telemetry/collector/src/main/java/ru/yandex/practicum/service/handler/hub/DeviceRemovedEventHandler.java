package ru.yandex.practicum.service.handler.hub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.service.producer.CollectorKafkaProducer;

@Slf4j
@Component
public class DeviceRemovedEventHandler extends BaseHubEventHandler<DeviceRemovedEventAvro> {

    public DeviceRemovedEventHandler(CollectorKafkaProducer producer) {
        super(producer);
    }

    @Override
    protected DeviceRemovedEventAvro protoToAvro(HubEventProto eventProto) {
        log.info("Преобразование HubEventProto в DeviceRemovedEventAvro");

        DeviceRemovedEventAvro avro = DeviceRemovedEventAvro.newBuilder()
                .setId(eventProto.getDeviceRemoved().getId())
                .build();

        log.info("Преобразование HubEventProto в DeviceRemovedEventAvro завершено");
        return avro;
    }

    @Override
    public HubEventProto.PayloadCase getMessageHubType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }
}
