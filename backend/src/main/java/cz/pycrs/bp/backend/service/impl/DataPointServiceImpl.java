package cz.pycrs.bp.backend.service.impl;

import cz.pycrs.bp.backend.entity.datapoint.DataPoint;
import cz.pycrs.bp.backend.entity.datasource.DataSource;
import cz.pycrs.bp.backend.repository.DataPointRepository;
import cz.pycrs.bp.backend.service.DataPointService;
import cz.pycrs.bp.backend.service.DataSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataPointServiceImpl implements DataPointService {
    private final DataPointRepository dataPointRepository;
    private final DataSourceService dataSourceService;

    @Override
    public void create(DataPoint dataPoint) {
        dataPointRepository.save(dataPoint);
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
