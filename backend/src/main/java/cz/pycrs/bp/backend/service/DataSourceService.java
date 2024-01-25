package cz.pycrs.bp.backend.service;

import cz.pycrs.bp.backend.entity.datasource.DataSource;
import cz.pycrs.bp.backend.entity.user.User;
import cz.pycrs.bp.backend.payload.DataSourceAdoptionRequest;
import cz.pycrs.bp.backend.payload.DataSourceUpdateRequest;

import java.util.List;

public interface DataSourceService {
    DataSource getOrRegister(String mac);

    List<DataSource> getAllDataSources();
    List<DataSource> getAllDataSourcesForUser(User user);

    void deleteDataSource(String id);

    DataSource adoptDataSource(String id, DataSourceAdoptionRequest request);

    DataSource updateDataSource(String id, DataSourceUpdateRequest request);

    List<String> getUniqueGroups();
}
