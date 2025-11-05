package ru.yandex.practicum.service.handler.hub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.service.producer.CollectorKafkaProducer;

@Slf4j
@Component
public class DeviceAddedEventHandler extends BaseHubEventHandler<DeviceAddedEventAvro> {

    public DeviceAddedEventHandler(CollectorKafkaProducer producer) {
        super(producer);
    }

    @Override
    protected DeviceAddedEventAvro protoToAvro(HubEventProto eventProto) {
        log.info("Преобразование HubEventProto в DeviceAddedEventAvro");

        DeviceAddedEventProto proto = eventProto.getDeviceAdded();
        DeviceTypeAvro deviceType = DeviceTypeAvro.valueOf(proto.getType().name());
        DeviceAddedEventAvro avro = DeviceAddedEventAvro.newBuilder()
                .setId(proto.getId())
                .setType(deviceType)
                .build();

        log.info("Преобразование HubEventProto в DeviceAddedEventAvro завершено");
        return avro;
    }

    @Override
    public HubEventProto.PayloadCase getMessageHubType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }
}
