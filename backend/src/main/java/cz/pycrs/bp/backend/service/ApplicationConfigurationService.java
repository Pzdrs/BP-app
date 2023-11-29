package cz.pycrs.bp.backend.service;

import cz.pycrs.bp.backend.entity.configuration.MqttConfiguration;

public interface ApplicationConfigurationService {
    MqttConfiguration getMqttConfiguration();
}
