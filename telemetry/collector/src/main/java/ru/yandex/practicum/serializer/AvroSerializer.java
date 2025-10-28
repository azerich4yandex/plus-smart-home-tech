package ru.yandex.practicum.serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;

@UtilityClass
@Slf4j
public class AvroSerializer {

    public <T extends SpecificRecordBase> byte[] serialize(T record) {
        log.info("Сериализация типа {}", record.getClass());
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            DatumWriter<T> writer = new SpecificDatumWriter<>(record.getSchema());

            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);

            writer.write(record, encoder);

            encoder.flush();

            log.info("Возврат результатов на уровень сервиса");
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка сериализации " + record.getClass(), e);
        }
    }
}
