package cz.pycrs.bp.backend.service.impl;

import cz.pycrs.bp.backend.entity.datasource.DataSource;
import cz.pycrs.bp.backend.payload.DataSourceAdoptionRequest;
import cz.pycrs.bp.backend.payload.DataSourceUpdateRequest;
import cz.pycrs.bp.backend.repository.DataSourceRepository;
import cz.pycrs.bp.backend.service.DataSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public List<DataSource> getAllDataSources() {
        return dataSourceRepository.findAll();
    }

    @Override
    public void deleteDataSource(String id) {
        dataSourceRepository.deleteById(id);
    }

    @Override
    public DataSource adoptDataSource(String id, DataSourceAdoptionRequest request) {
        Optional<DataSource> dataSourceOptional = dataSourceRepository.findById(id);
        if (dataSourceOptional.isEmpty()) {
            throw new RuntimeException("Data source not found");
        }

        DataSource dataSource = dataSourceOptional.get();
        dataSource.setName(request.name());
        dataSource.setColor(request.color());
        dataSource.setAdopted(true);
        return dataSourceRepository.save(dataSource);
    }

    @Override
    public DataSource updateDataSource(String id, DataSourceUpdateRequest request) {
        return dataSourceRepository.findById(id).map(dataSource -> {
            dataSource.setName(request.name());
            dataSource.setColor(request.color());
            dataSource.setGroups(request.groups());
            return dataSourceRepository.save(dataSource);
        }).orElseThrow(() -> new RuntimeException("Data source not found"));
    }

    @Override
    public List<String> getUniqueGroups() {
        return dataSourceRepository.findAll()
                .stream()
                .flatMap(dataSource -> dataSource.getGroups().stream())
                .distinct().toList();
    }
}
