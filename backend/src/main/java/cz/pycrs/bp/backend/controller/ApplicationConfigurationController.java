package cz.pycrs.bp.backend.controller;

import cz.pycrs.bp.backend.entity.configuration.MqttConfiguration;
import cz.pycrs.bp.backend.service.ApplicationConfigurationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
public class ApplicationConfigurationController {
    private final ApplicationConfigurationService applicationConfigurationService;

    @GetMapping("/mqtt")
    public MqttConfiguration getMqttConfiguration() {
        return applicationConfigurationService.getMqttConfiguration();
    }

    @PostMapping("/mqtt")
    public MqttConfiguration setMqttConfiguration(@Valid @RequestBody MqttConfiguration mqttConfiguration) {
        return applicationConfigurationService.setMqttConfiguration(mqttConfiguration);
    }
}
