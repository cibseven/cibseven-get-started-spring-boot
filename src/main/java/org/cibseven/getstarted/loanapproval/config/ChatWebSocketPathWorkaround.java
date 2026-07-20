package org.cibseven.getstarted.loanapproval.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WORKAROUND (temporary): the EE chat STOMP endpoint is registered by
 * {@code WebSocketConfigEE} at {@code /ws/chat} relative to the servlet context root,
 * but the webclient is served under the webapp application-path {@code /webapp}, so the
 * chat client (chatService.js) connects to {@code /webapp/ws/chat} and gets a 404 on the
 * WebSocket handshake.
 *
 * <p>Registering the same endpoint under {@code /webapp/ws/chat} makes the two agree. The
 * shared broker config and the JWT inbound-channel interceptor from {@code WebSocketConfigEE}
 * apply to this endpoint as well.
 *
 * <p>TODO: replace with a proper fix in cibseven-webclient-ee / cibseven-modeler-ee — the WS
 * endpoint should be application-path aware, or the client should derive the WS URL from the
 * context root. Tracked separately.
 */
@Configuration
public class ChatWebSocketPathWorkaround implements WebSocketMessageBrokerConfigurer {

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/webapp/ws/chat").setAllowedOriginPatterns("*");
  }
}
