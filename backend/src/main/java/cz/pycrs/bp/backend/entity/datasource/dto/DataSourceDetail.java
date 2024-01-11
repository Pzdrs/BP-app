package cz.pycrs.bp.backend.entity.datasource.dto;

import cz.pycrs.bp.backend.entity.datasource.DataSource;

import java.util.Date;

public record DataSourceDetail(
        String id,
        String mac,
        String name,
        String color,
        boolean adopted,
        Date created,
        Date updated
) {
    public DataSourceDetail(DataSource dataSource) {
        this(
                dataSource.getId().toHexString(),
                dataSource.getMac(),
                dataSource.getName(),
                dataSource.getColor(),
                dataSource.isAdopted(),
                dataSource.getCreated(),
                dataSource.getUpdated()
        );
    }
}
