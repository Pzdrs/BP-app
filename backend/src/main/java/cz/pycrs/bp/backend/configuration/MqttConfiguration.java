package cz.pycrs.bp.backend.configuration;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.messaging.MessageChannel;

import java.util.UUID;

@Configuration
public class MqttConfiguration {
    @Value("${mqtt.host}")
    private String host;

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;

    private static final String CLIENT_ID = String.format("es-gps_server-%s", UUID.randomUUID());

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();

        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{host});
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        factory.setConnectionOptions(options);

        return factory;
    }

    @Bean
    public MessageChannel inboundMqtt() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer gnssAdapter() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                CLIENT_ID, mqttClientFactory(), "gnss"
        );
        adapter.setOutputChannel(inboundMqtt());
        return adapter;
    }
}
