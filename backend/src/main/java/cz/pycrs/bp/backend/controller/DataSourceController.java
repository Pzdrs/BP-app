package cz.pycrs.bp.backend.controller;

import cz.pycrs.bp.backend.entity.datasource.dto.DataSourceDetail;
import cz.pycrs.bp.backend.entity.user.User;
import cz.pycrs.bp.backend.payload.DataSourceAdoptionRequest;
import cz.pycrs.bp.backend.payload.DataSourceUpdateRequest;
import cz.pycrs.bp.backend.service.DataSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/datasource")
@RequiredArgsConstructor
public class DataSourceController {
    private final DataSourceService dataSourceService;

    @GetMapping("/all")
    public List<DataSourceDetail> allUsers(Authentication authentication) {
        return dataSourceService.getAllDataSourcesForUser(((User) authentication.getPrincipal()))
                .stream().map(DataSourceDetail::new).toList();
    }

    @PostMapping("/{id}/adopt")
    public DataSourceDetail adoptDataSource(@PathVariable String id, @RequestBody DataSourceAdoptionRequest request) {
        return new DataSourceDetail(dataSourceService.adoptDataSource(id, request));
    }

    @PatchMapping("/{id}")
    public DataSourceDetail updateUser(@PathVariable String id, @RequestBody DataSourceUpdateRequest request) {
        return new DataSourceDetail(dataSourceService.updateDataSource(id, request));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        dataSourceService.deleteDataSource(id);
    }

    @GetMapping("/groups")
    public List<String> allUniqueGroups() {
        return dataSourceService.getUniqueGroups();
    }
}
