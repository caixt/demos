package com.github.cxt.springmvc.springconfig;

import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;


@Component
@Scope(value=WebApplicationContext.SCOPE_SESSION, proxyMode=ScopedProxyMode.INTERFACES)
public class SessionBean implements ISessionBean {
   private UUID uuid;
   public SessionBean(){
       uuid = UUID.randomUUID();
   }
   public void printId(){
       System.out.println("SessionBean:"+uuid);
   }
}