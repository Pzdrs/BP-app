package cz.pycrs.bp.backend.entity.configuration;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public record MqttConfiguration(
        @NotBlank
        String host,
        @Min(1) @Max(65535)
        int port,
        String username,
        String password
) {
    public static final String KEY = "mqtt";

    public MqttConnectOptions apply(MqttConnectOptions options) {
        options.setServerURIs(new String[]{host});
        if (hasCredentials()) {
            options.setUserName(username);
            options.setPassword(password.toCharArray());
        }
        return options;
    }

    public boolean hasCredentials() {
        return username != null && !username.isBlank() && password != null && !password.isBlank();
    }
}
