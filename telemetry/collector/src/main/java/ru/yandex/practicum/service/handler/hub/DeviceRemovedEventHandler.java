package ru.yandex.practicum.service.handler.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto.PayloadCase;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.mapper.HubEventAvroMapper;
import ru.yandex.practicum.mapper.HubEventProtoMapper;
import ru.yandex.practicum.model.hub.HubEvent;

@Component
public class DeviceRemovedEventHandler extends BaseHubEventHandler {

    public DeviceRemovedEventHandler(
            KafkaTemplate<String, SpecificRecordBase> kafkaTemplate) {
        super(kafkaTemplate);
    }

    @Override
    protected HubEvent mapHubEventProtoToModel(HubEventProto eventProto) {
        HubEvent event = HubEventProtoMapper.toDeviceRemovedEvent(eventProto);
        return mapBaseProtoFieldsToModel(event, eventProto);
    }

    @Override
    protected HubEventAvro mapModelToHubEventAvro(HubEvent event) {
        HubEventAvro eventAvro = HubEventAvroMapper.toAvro(event);
        return mapBaseModelFieldsToAvro(event, eventAvro);
    }

    @Override
    public PayloadCase getMessageHubType() {
        return PayloadCase.DEVICE_REMOVED;
    }
}
