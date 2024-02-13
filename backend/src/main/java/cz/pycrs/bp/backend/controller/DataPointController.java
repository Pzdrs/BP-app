package cz.pycrs.bp.backend.controller;

import cz.pycrs.bp.backend.entity.datapoint.dto.DataPointDetail;
import cz.pycrs.bp.backend.service.DataPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/datapoint")
@RequiredArgsConstructor
public class DataPointController {
    private final DataPointService dataPointService;

    @GetMapping("/all")
    public List<DataPointDetail> all(
            @RequestParam String source,
            @RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return dataPointService.getAllBySource(source, start,end).stream().map(DataPointDetail::new).toList();
    }
}
