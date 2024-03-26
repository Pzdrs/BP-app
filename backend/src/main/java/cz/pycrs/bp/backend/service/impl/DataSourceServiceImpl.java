package cz.pycrs.bp.backend.service.impl;

import cz.pycrs.bp.backend.entity.accesstoken.AccessToken;
import cz.pycrs.bp.backend.entity.datasource.DataSource;
import cz.pycrs.bp.backend.entity.notification.Notification;
import cz.pycrs.bp.backend.entity.user.Role;
import cz.pycrs.bp.backend.entity.user.User;
import cz.pycrs.bp.backend.payload.DataSourceAdoptionRequest;
import cz.pycrs.bp.backend.payload.DataSourceUpdateRequest;
import cz.pycrs.bp.backend.repository.DataSourceRepository;
import cz.pycrs.bp.backend.service.DataSourceService;
import cz.pycrs.bp.backend.service.NotificationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;

@Log4j2
@Service
@RequiredArgsConstructor
public class DataSourceServiceImpl implements DataSourceService {
    /**
     * <p>1. If the user is an administrator, they have access to every data source.</p>
     * <p>2. If the user's <i>dataSources</i> list contains the data source's <b>id</b>, the data source is visible to the user.</p>
     * <p>3. If the user's <i>dataSources</i> list contains <b>any of the data source's groups</b>, the data source is visible to the user.</p>
     * <p>4. Otherwise, the data source is not visible to the user.</p>
     */
    public static final BiPredicate<DataSource, Object> IS_VISIBLE_TO_USER = (dataSource, authentication) -> {
        var user = User.from((Authentication) authentication);

        if (user.getRole() == Role.ADMINISTRATOR) return true;
        if (user.getDataSources().contains(dataSource.getId().toString())) return true;
        return dataSource.getGroups().stream().anyMatch(user.getDataSourceGroups()::contains);
    };

    /**
     * Keeps track of each session, that is listening to one or more data sources.
     */
    public final Map<SseEmitter, Set<DataSource>> dataSourceEmitters = new ConcurrentHashMap<>();

    private final DataSourceRepository dataSourceRepository;
    private final NotificationService notificationService;

    @Override
    public DataSource getOrRegister(String mac) {
        return dataSourceRepository
                .findByMac(mac)
                .orElseGet(() -> {
                    log.info(LogMessage.format("Registering a new data source: %s", mac));
                    notificationService.notifyAdministrators(
                            new Notification(
                                    Notification.Severity.INFO,
                                    "New data source discovered",
                                    String.format("A new data source with MAC address %s has been discovered.", mac),
                                    Map.of("actions", Map.of("resolveUrl", "/data-sources"))
                            )
                    );
                    return dataSourceRepository.save(new DataSource(mac));
                });
    }

    @Override
    public DataSource getDataSource(String id) {
        return dataSourceRepository.findById(id).orElse(null);
    }

    @Override
    public List<DataSource> getAllDataSources() {
        return dataSourceRepository.findAll();
    }

    @Override
    public List<DataSource> getAllDataSourcesForUser(Authentication authentication) {
        return getAllDataSources().stream().filter(dataSource -> IS_VISIBLE_TO_USER.test(dataSource, authentication)).toList();
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

    @Override
    public SseEmitter registerEmitter(HttpSession session, Set<DataSource> dataSources) {
        log.info("Registering a new emitter for session {}", session.getId());
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        dataSourceEmitters.put(emitter, dataSources);
        return emitter;
    }

    @Override
    public Map<SseEmitter, Set<DataSource>> getEmitterRegistry() {
        return dataSourceEmitters;
    }
}
