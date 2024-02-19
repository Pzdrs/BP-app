package cz.pycrs.bp.backend.handler;

import cz.pycrs.bp.backend.entity.datapoint.DataPoint;
import cz.pycrs.bp.backend.entity.datasource.DataSource;
import cz.pycrs.bp.backend.service.DataPointService;
import cz.pycrs.bp.backend.service.DataSourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class MqttMessageHandler implements MessageHandler {

    private final DataSourceService dataSourceService;
    private final DataPointService dataPointService;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        log.trace("Received a new GNSS data point");
        JsonParser springParser = JsonParserFactory.getJsonParser();
        Map<String, Object> data = springParser.parseMap(String.valueOf(message.getPayload()));

        DataSource dataSource = dataSourceService.getOrRegister(String.valueOf(data.get("source")));

        if (!dataSource.isAdopted()) return;

        DataPoint dataPoint = new DataPoint(
                dataSource,
                Double.parseDouble(String.valueOf(data.get("lng"))),
                Double.parseDouble(String.valueOf(data.get("lat"))),
                Double.parseDouble(String.valueOf(data.get("alt"))),
                Double.parseDouble(String.valueOf(data.get("speed"))),
                Double.parseDouble(String.valueOf(data.get("course")))
        );

        dataPointService.create(dataPoint);

    }
}
