package app.lunchgowhere.config;


import app.lunchgowhere.interceptor.HandshakeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
@CrossOrigin(origins = "*")
public class  WebSocketConfig implements WebSocketMessageBrokerConfigurer  {

    private final HandshakeHandler handshakeHandler;

    @Autowired
    public WebSocketConfig(HandshakeHandler handshakeHandler) {
        this.handshakeHandler = handshakeHandler;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:3000/").setAllowedOriginPatterns("*");
        registry.addEndpoint("/sock")
                .setHandshakeHandler(handshakeHandler)
                .setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/room");

    }
}
