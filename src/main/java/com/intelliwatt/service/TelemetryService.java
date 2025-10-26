package com.intelliwatt.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.intelliwatt.entity.Reading;
import com.intelliwatt.repository.ReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TelemetryService {

    @Autowired
    private InfluxDBClient influxDBClient;

    @Autowired
    private ReadingRepository readingRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleTelemetry(Message<String> message) {
        try {
            String payload = message.getPayload();
            JsonNode jsonNode = objectMapper.readTree(payload);

            Long deviceId = jsonNode.get("deviceId").asLong();
            Double voltage = jsonNode.get("voltage").asDouble();
            Double current = jsonNode.get("current").asDouble();
            Double power = jsonNode.get("power").asDouble();
            Double energy = jsonNode.get("energy").asDouble();

            // Store in PostgreSQL
            Reading reading = new Reading();
            reading.setDeviceId(deviceId);
            reading.setVoltage(voltage);
            reading.setCurrent(current);
            reading.setPower(power);
            reading.setEnergy(energy);
            readingRepository.save(reading);

            // Store in InfluxDB
            Point point = Point.measurement("telemetry")
                    .addTag("deviceId", deviceId.toString())
                    .addField("voltage", voltage)
                    .addField("current", current)
                    .addField("power", power)
                    .addField("energy", energy)
                    .time(Instant.now(), WritePrecision.NS);

            WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
            writeApi.writePoint(point);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
