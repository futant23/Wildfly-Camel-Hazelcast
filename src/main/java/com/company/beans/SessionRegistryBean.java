/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.beans;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.websocket.Session;
/**
 *
 * @author
 */
@Singleton
public class SessionRegistryBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    private final Set<Session> sessions =new HashSet<>();
    
    @Lock(LockType.READ)
    public Set<Session> getAll() {
        return Collections.unmodifiableSet(sessions);
    }
    
    @Lock(LockType.WRITE)
    public void add(Session session) {
        sessions.add(session);
    }
    
    @Lock(LockType.WRITE)
    public void remove(Session session) {
        sessions.remove(session);
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public Set<Session> getSessions() {
        return sessions;
    }
    
}
