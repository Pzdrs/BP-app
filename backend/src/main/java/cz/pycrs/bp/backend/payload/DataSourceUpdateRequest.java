package cz.pycrs.bp.backend.payload;

import java.util.List;

public record DataSourceUpdateRequest(
        String name,
        String color,
        List<String> groups
) {
}
