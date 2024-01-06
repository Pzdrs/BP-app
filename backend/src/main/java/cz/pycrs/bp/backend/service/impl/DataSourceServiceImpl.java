package cz.pycrs.bp.backend.service.impl;

import cz.pycrs.bp.backend.entity.DataSource;
import cz.pycrs.bp.backend.repository.DataSourceRepository;
import cz.pycrs.bp.backend.service.DataSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataSourceServiceImpl implements DataSourceService {
    private final DataSourceRepository dataSourceRepository;

    @Override
    public DataSource getOrRegister(String mac) {
        return dataSourceRepository
                .findByMac(mac)
                .orElseGet(() -> {
                    System.out.println("Registering a new data source: " + mac);
                    return dataSourceRepository.save(new DataSource(mac));
                });
    }
}
