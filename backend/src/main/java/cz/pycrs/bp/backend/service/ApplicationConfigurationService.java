package cz.pycrs.bp.backend.service;

import cz.pycrs.bp.backend.entity.configuration.MqttConfiguration;

import java.util.Optional;

public interface ApplicationConfigurationService {
    Optional<MqttConfiguration> getMqttConfiguration();

    MqttConfiguration setMqttConfiguration(MqttConfiguration mqttConfiguration);
}
