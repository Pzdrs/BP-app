package cz.pycrs.bp.backend.controller;

import cz.pycrs.bp.backend.entity.datapoint.dto.DataPointDetail;
import cz.pycrs.bp.backend.service.DataPointService;
import cz.pycrs.bp.backend.service.DataSourceService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/datapoint")
@RequiredArgsConstructor
public class DataPointController {
    private final DataPointService dataPointService;
    private final DataSourceService dataSourceService;

    @GetMapping("/all")
    public List<DataPointDetail> all(
            @RequestParam String source,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return dataPointService.getAllBySource(source, start, end).stream().map(DataPointDetail::new).toList();
    }

    @GetMapping(
            value = "/listen",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public SseEmitter liveLocationTracking(
            @RequestParam String sources,
            HttpSession session
    ) {
        var dataSources = Arrays.stream(sources.strip().split(","))
                .map(dataSourceService::getDataSource)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        return dataSourceService.registerEmitter(session, dataSources);
    }

    @DeleteMapping("/erase")
    public void eraseAll() {
        dataPointService.deleteAllDataPoints();
    }
}
