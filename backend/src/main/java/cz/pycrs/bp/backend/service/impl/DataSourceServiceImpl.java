package cz.pycrs.bp.backend.service.impl;

import cz.pycrs.bp.backend.entity.datasource.DataSource;
import cz.pycrs.bp.backend.entity.notification.Notification;
import cz.pycrs.bp.backend.entity.user.Role;
import cz.pycrs.bp.backend.entity.user.User;
import cz.pycrs.bp.backend.payload.DataSourceAdoptionRequest;
import cz.pycrs.bp.backend.payload.DataSourceUpdateRequest;
import cz.pycrs.bp.backend.repository.DataSourceRepository;
import cz.pycrs.bp.backend.service.DataSourceService;
import cz.pycrs.bp.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;

@Service
@RequiredArgsConstructor
public class DataSourceServiceImpl implements DataSourceService {
    /**
     * <p>1. If the user is an administrator, they have access to every data source.</p>
     * <p>2. If the user's <i>dataSources</i> list contains the data source's <b>id</b>, the data source is visible to the user.</p>
     * <p>3. If the user's <i>dataSources</i> list contains <b>any of the data source's groups</b>, the data source is visible to the user.</p>
     * <p>4. Otherwise, the data source is not visible to the user.</p>
     */
    public static final BiPredicate<DataSource, User> IS_VISIBLE_TO_USER = (dataSource, user) -> {
        if (user.getRole() == Role.ADMINISTRATOR) return true;
        if (user.getDataSources().contains(dataSource.getId().toString())) return true;
        return dataSource.getGroups().stream().anyMatch(user.getDataSources()::contains);
    };

    private final DataSourceRepository dataSourceRepository;
    private final NotificationService notificationService;

    @Override
    public DataSource getOrRegister(String mac) {
        return dataSourceRepository
                .findByMac(mac)
                .orElseGet(() -> {
                    System.out.println("Registering a new data source: " + mac);
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
    public List<DataSource> getAllDataSources() {
        return dataSourceRepository.findAll();
    }

    @Override
    public List<DataSource> getAllDataSourcesForUser(User user) {
        return getAllDataSources().stream().filter(dataSource -> IS_VISIBLE_TO_USER.test(dataSource, user)).toList();
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
