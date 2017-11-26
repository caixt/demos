package com.github.cxt.socketio.handler;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ChatEventHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static Map<String, SocketIOClient> users = new HashMap<>();
	private static Map<String, String> keys = new HashMap<>();
	private static List<String> userLists = new ArrayList<String>();  

    @OnConnect  
    public void onConnect(SocketIOClient client)  
    {  
    	logger.info("connect:" + client.getSessionId().toString());
    }
    
    //添加@OnDisconnect事件，客户端断开连接时调用，刷新客户端信息  
    @OnDisconnect  
    public void onDisconnect(SocketIOClient client)  
    {  
    	logger.info("disconnect:" + client.getSessionId().toString());
    	String key = client.getSessionId().toString();
    	String userName = keys.get(key);
        userLists.remove(userName);  
        keys.remove(key);
        users.remove(userName);  
        client.getNamespace().getBroadcastOperations().sendEvent("users", userLists);  
    }

 
    @OnEvent(value = "groupChatEvent")
    public void onGroupChatEvent(SocketIOClient client, AckRequest request, ChatObject chatObject) {  
    	client.getNamespace().getBroadcastOperations().sendEvent("groupChatEvent", chatObject);  
    } 

    @OnEvent(value = "chatEvent")
    public void onChatEvent(SocketIOClient client, AckRequest request, ChatObject chatObject) {  
        SocketIOClient fromSocketIoClient = users.get(chatObject.getUserName());  
        String toUser = chatObject.getToUser();
        SocketIOClient toSocketIoClient = users.get(toUser); 
        if(toSocketIoClient != null){  
            toSocketIoClient.sendEvent("chatEvent",chatObject);  
        }
        if(fromSocketIoClient != null){  
            fromSocketIoClient.sendEvent("chatEvent",chatObject);  
        }
    }  
    
    
    
    @OnEvent(value = "login")
    public void onLogin(SocketIOClient client, ChatObject chatObject, AckRequest ackRequest) {
    	String key = client.getSessionId().toString();
        users.put(chatObject.getUserName(), client);  
        keys.put(key, chatObject.getUserName());
        userLists.add(chatObject.getUserName());  
        client.getNamespace().getBroadcastOperations().sendEvent("users", userLists);  
    }  
}

