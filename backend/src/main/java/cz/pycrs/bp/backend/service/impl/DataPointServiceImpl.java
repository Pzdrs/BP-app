package cz.pycrs.bp.backend.service.impl;

import cz.pycrs.bp.backend.entity.DataPoint;
import cz.pycrs.bp.backend.entity.DataSource;
import cz.pycrs.bp.backend.repository.DataPointRepository;
import cz.pycrs.bp.backend.service.DataPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataPointServiceImpl implements DataPointService {
    private final DataPointRepository dataPointRepository;

    @Override
    public void create(DataPoint dataPoint) {
        dataPointRepository.save(dataPoint);
    }
}
