package devcamp.ottogi.userservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@EnableScheduling
@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();



    //웹소켓 연결
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Web Socket 연결 완료");
        String sessionId = session.getId();
        sessions.put(sessionId, session); // 세션 저장
        log.info("Web Session 저장 완료, : {}", sessionId);
    }

    // 데이터 통신
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        if(textMessage.getPayload().equalsIgnoreCase("pong")){
            log.info("Received pong:{}",session.getId());
        }
    }

    // 소켓 연결 종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("Web Socket 연결 끊김");
        String sessionId = session.getId();
        sessions.remove(sessionId);
        log.info("Session 삭제");
    }

    @Scheduled(fixedRate = 2000)
    public void expire() {
        sessions.values().forEach(webSocketSession -> {
            try{
                webSocketSession.sendMessage(new TextMessage("ping"));
            }
            catch(IOException e){
                e.printStackTrace();
            }
        });
    }

}
