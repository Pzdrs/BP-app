package cz.pycrs.bp.backend.entity.configuration;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public record MqttConfiguration(String host, int port, String username, String password) {
    public static final String KEY = "mqtt";

    public MqttConnectOptions apply(MqttConnectOptions options) {
        options.setServerURIs(new String[]{host});
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        return options;
    }
}
