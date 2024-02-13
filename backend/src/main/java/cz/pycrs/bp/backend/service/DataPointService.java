package cz.pycrs.bp.backend.service;

import cz.pycrs.bp.backend.entity.datapoint.DataPoint;

import java.time.LocalDateTime;
import java.util.List;

public interface DataPointService {
    void create(DataPoint dataPoint);

    List<DataPoint> getAllBySource(String source, LocalDateTime start, LocalDateTime end);
}
