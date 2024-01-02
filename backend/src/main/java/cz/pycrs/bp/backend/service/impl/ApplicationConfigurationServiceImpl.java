package cz.pycrs.bp.backend.service.impl;

import cz.pycrs.bp.backend.entity.ApplicationConfiguration;
import cz.pycrs.bp.backend.entity.configuration.MqttConfiguration;
import cz.pycrs.bp.backend.repository.ApplicationConfigurationRepository;
import cz.pycrs.bp.backend.service.ApplicationConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationConfigurationServiceImpl implements ApplicationConfigurationService {
    private final ApplicationConfigurationRepository applicationConfigurationRepository;

    @Override
    public MqttConfiguration getMqttConfiguration() {
        return applicationConfigurationRepository
                .findById(MqttConfiguration.KEY)
                .map(applicationConfiguration -> (MqttConfiguration) applicationConfiguration.getValue())
                .orElse(null);
    }

    @Override
    public MqttConfiguration setMqttConfiguration(MqttConfiguration mqttConfiguration) {
        return (MqttConfiguration) applicationConfigurationRepository
                .save(new ApplicationConfiguration(MqttConfiguration.KEY, mqttConfiguration))
                .getValue();
    }
}
