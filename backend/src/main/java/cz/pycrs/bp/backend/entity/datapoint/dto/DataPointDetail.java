package cz.pycrs.bp.backend.entity.datapoint.dto;

import cz.pycrs.bp.backend.entity.datapoint.DataPoint;

import java.util.Date;

public record DataPointDetail(
        String id,
        String source,
        Date timestamp,
        double lat, double lng, double alt,
        double speed, double course
) {
    public DataPointDetail(DataPoint dataPoint) {
        this(
                dataPoint.getId().toString(),
                dataPoint.getSource().getId().toString(),
                dataPoint.getTimestamp(),
                dataPoint.getLat(), dataPoint.getLng(), dataPoint.getAlt(),
                dataPoint.getSpeed(), dataPoint.getCourse()
        );
    }
}
