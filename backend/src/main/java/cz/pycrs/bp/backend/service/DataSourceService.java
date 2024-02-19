package cz.pycrs.bp.backend.service;

import cz.pycrs.bp.backend.entity.datasource.DataSource;
import cz.pycrs.bp.backend.entity.user.User;
import cz.pycrs.bp.backend.payload.DataSourceAdoptionRequest;
import cz.pycrs.bp.backend.payload.DataSourceUpdateRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DataSourceService {
    DataSource getOrRegister(String mac);

    DataSource getDataSource(String id);

    List<DataSource> getAllDataSources();

    List<DataSource> getAllDataSourcesForUser(User user);

    void deleteDataSource(String id);

    DataSource adoptDataSource(String id, DataSourceAdoptionRequest request);

    DataSource updateDataSource(String id, DataSourceUpdateRequest request);

    List<String> getUniqueGroups();

    SseEmitter registerEmitter(HttpSession session, Set<DataSource> dataSources);
    Map<SseEmitter, Set<DataSource>> getEmitterRegistry();
}
