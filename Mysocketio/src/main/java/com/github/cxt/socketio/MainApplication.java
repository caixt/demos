package com.github.cxt.socketio;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAutoConfiguration
@SpringBootApplication
@EnableAsync
public class MainApplication extends WebMvcConfigurerAdapter {
	
    @Value("${wss.server.host}")
    private String host;

    @Value("${wss.server.port}")
    private Integer port;

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}
	
	
    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname(host);
        config.setPort(port);
        SocketIOServer server =  new SocketIOServer(config);
        return server;
    }
    
    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer server) {
        return new SpringAnnotationScanner(server);
    }
    
//    @Bean
//    public SocketIOServer socketIOServer(ChatEventHandler chatEventHandler) {
//        Configuration config = new Configuration();
//        config.setHostname(host);
//        config.setPort(port);
//        SocketIOServer server =  new SocketIOServer(config);
//        server.addNamespace("/cxt/game").addListeners(chatEventHandler);
//        return server;
//    }    
}
