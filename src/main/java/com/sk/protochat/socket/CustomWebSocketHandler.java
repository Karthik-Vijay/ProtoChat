package com.sk.protochat.socket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sk.protochat.dao.MessageDao;


public class CustomWebSocketHandler extends TextWebSocketHandler{

	private Map<String,WebSocketSession>sessions = new HashMap<String,WebSocketSession>();
	
	@Autowired
	MessageDao messageDao;
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
		Map<String, Object> attributes = session.getAttributes();
		
		String username = (String) attributes.get("username");
		
		sessions.put(username,session);
	}
	
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		
		Map<String, Object> attributes = session.getAttributes();
		
		String username = (String) attributes.get("username");
		
		com.sk.protochat.model.Message messageModel = 
				new ObjectMapper().readValue((String) message.getPayload(),com.sk.protochat.model.Message.class);
		
		messageModel.setFromUser(username);
		
		int messageId = messageDao.insert(messageModel);
		
		messageModel.setMessageId(messageId);
		
		TextMessage textMessage = new TextMessage(new ObjectMapper().writeValueAsString(messageModel));
		
		for(Map.Entry<String,WebSocketSession> each : sessions.entrySet()) {
			
			if(each.getKey().equals(messageModel.getToUser())) {
				
				each.getValue().sendMessage(textMessage);
			}
			
		}
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		
		Map<String, Object> attributes = session.getAttributes();
		
		String username = (String) attributes.get("username");
		
		sessions.remove(username);
	}
	
	
}
