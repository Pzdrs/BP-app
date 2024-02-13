package cz.pycrs.bp.backend.handler;

import cz.pycrs.bp.backend.entity.datapoint.DataPoint;
import cz.pycrs.bp.backend.entity.datasource.DataSource;
import cz.pycrs.bp.backend.service.DataPointService;
import cz.pycrs.bp.backend.service.DataSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.util.Map;

@RequiredArgsConstructor
public class MqttMessageHandler implements MessageHandler {
    private final DataSourceService dataSourceService;
    private final DataPointService dataPointService;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        JsonParser springParser = JsonParserFactory.getJsonParser();
        Map<String, Object> data = springParser.parseMap(String.valueOf(message.getPayload()));

        DataSource dataSource = dataSourceService.getOrRegister(String.valueOf(data.get("source")));

        if (!dataSource.isAdopted()) return;

        DataPoint dataPoint = new DataPoint(
                dataSource,
                (double) data.get("lng"),
                (double) data.get("lat"),
                (double) data.get("alt"),
                (double) data.get("speed"),
                (double) data.get("course")
        );

        dataPointService.create(dataPoint);
    }
}
