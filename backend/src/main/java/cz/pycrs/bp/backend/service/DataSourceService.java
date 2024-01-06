package cz.pycrs.bp.backend.service;

import cz.pycrs.bp.backend.entity.DataSource;

public interface DataSourceService {
    DataSource getOrRegister(String mac);
}
