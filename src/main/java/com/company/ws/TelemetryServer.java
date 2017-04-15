/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.ws;

import com.company.beans.SessionRegistryBean;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author 
 */
@Stateful
@ServerEndpoint("/telemetry")
public class TelemetryServer {

    private final static Logger log = Logger.getLogger(TelemetryServer.class.getName());

  
    @Inject
    SessionRegistryBean sessionRegBean;

    public void send(String msg) throws Exception{
        for (Session sess : sessionRegBean.getAll()) {

            sess.getAsyncRemote().sendText(msg);
        }
    }

    @OnOpen
    public void open(Session session) {
        log.info("Open session:" + session.getId());

        sessionRegBean.add(session);

    }

    @OnClose
    public void close(Session session, CloseReason c) {
        log.info("Closing:" + session.getId());
        log.info("close reason: " + c.getReasonPhrase());

        sessionRegBean.remove(session);
    }

}
