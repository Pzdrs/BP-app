package cz.pycrs.bp.backend.configuration;

import cz.pycrs.bp.backend.service.ApplicationConfigurationService;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
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
@RequiredArgsConstructor
public class MqttConfiguration {
    private final ApplicationConfigurationService configurationService;

    private static final String CLIENT_ID = String.format("es-gps_server-%s", UUID.randomUUID());

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();

        configurationService.getMqttConfiguration().ifPresent(mqttConfiguration -> {
            mqttConfiguration.apply(options);
            factory.setConnectionOptions(options);
        });

        return factory;
    }

    @Bean
    public MessageChannel inboundMqttChannel() {
        return new DirectChannel();
    }
    @Bean
    public MessageProducer gnssAdapter() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                CLIENT_ID, mqttClientFactory(), "gnss"
        );
        adapter.setOutputChannel(inboundMqttChannel());
        return adapter;
    }
}
