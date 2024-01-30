package cz.pycrs.bp.backend.service.impl;

import cz.pycrs.bp.backend.entity.ApplicationConfiguration;
import cz.pycrs.bp.backend.entity.configuration.MqttConfiguration;
import cz.pycrs.bp.backend.repository.ApplicationConfigurationRepository;
import cz.pycrs.bp.backend.service.ApplicationConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationConfigurationServiceImpl implements ApplicationConfigurationService {
    private final ApplicationConfigurationRepository applicationConfigurationRepository;

    @Override
    public Optional<MqttConfiguration> getMqttConfiguration() {
        return Optional.ofNullable(
                (MqttConfiguration) applicationConfigurationRepository
                        .findById(MqttConfiguration.KEY)
                        .map(ApplicationConfiguration::getValue)
                        .orElse(null)
        );
    }

    @Override
    public MqttConfiguration setMqttConfiguration(MqttConfiguration mqttConfiguration) {
        return (MqttConfiguration) applicationConfigurationRepository
                .save(new ApplicationConfiguration(MqttConfiguration.KEY, mqttConfiguration))
                .getValue();
    }
}
