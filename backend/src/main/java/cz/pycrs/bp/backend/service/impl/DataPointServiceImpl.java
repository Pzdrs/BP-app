package cz.pycrs.bp.backend.service.impl;

import cz.pycrs.bp.backend.entity.datapoint.DataPoint;
import cz.pycrs.bp.backend.entity.datapoint.dto.DataPointDetail;
import cz.pycrs.bp.backend.entity.datasource.DataSource;
import cz.pycrs.bp.backend.repository.DataPointRepository;
import cz.pycrs.bp.backend.service.DataPointService;
import cz.pycrs.bp.backend.service.DataSourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.log.LogMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class DataPointServiceImpl implements DataPointService {
    private final DataPointRepository dataPointRepository;
    private final DataSourceService dataSourceService;

    @Override
    public void create(DataPoint dataPoint) {
        DataPoint savedDataPoint = dataPointRepository.save(dataPoint);
        dataSourceService.getEmitterRegistry().forEach((emitter, dataSources) -> {
            if (dataSources.contains(savedDataPoint.getSource())) {
                emitDataPoint(savedDataPoint, emitter);
            }
        });
    }

    private void emitDataPoint(DataPoint dataPoint, SseEmitter emitter) {
        try {
            emitter.send(new DataPointDetail(dataPoint));
        } catch (Exception e) {
            log.error(LogMessage.format("Failed to send datapoint %s", dataPoint.getId().toString(), e));
            dataSourceService.getEmitterRegistry().remove(emitter);
        }
    }

    @Override
    public List<DataPoint> getAllBySource(String source, LocalDateTime start, LocalDateTime end) {
        DataSource dataSource = dataSourceService.getDataSource(source);
        return dataPointRepository
                .findAllBySource(dataSource)
                .stream()
                .filter(dataPoint -> {
                    LocalDateTime timestamp = dataPoint.getTimestamp().toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime();
                    return timestamp.isAfter(start) && timestamp.isBefore(end);
                }).toList();
    }
}
