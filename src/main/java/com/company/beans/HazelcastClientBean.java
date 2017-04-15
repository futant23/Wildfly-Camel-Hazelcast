/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.beans;

import com.company.ws.TelemetryServer;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MapEvent;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryEvictedListener;
import com.hazelcast.map.listener.EntryRemovedListener;
import com.hazelcast.map.listener.EntryUpdatedListener;
import com.hazelcast.map.listener.MapClearedListener;
import com.hazelcast.map.listener.MapEvictedListener;
import java.util.Collection;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * Assumes a hazelcast instance is running on the same
 * host. Also assumes said instance is publishing data
 * on a collection named: 'telemetryMap'.
 *
 * @author 
 */
@Startup
@Singleton
public class HazelcastClientBean {

    private final static Logger log = Logger.getLogger(HazelcastClientBean.class.getName());

    @EJB
    private TelemetryServer socket;

    private HazelcastInstance client;

    private IMap<String, String> map;

    @PostConstruct
    public void init() {
        log.info("init()");

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.addAddress("localhost:54322");

        client = HazelcastClient.newHazelcastClient(clientConfig);
        map = client.getMap("telemetryMap");

        log.info("number of entries: " + map.size());
        for (String se : map.values()) {
            log.fine("--->" + se.toString());
        }

        map.addEntryListener(new SimpleEventListener(), true);

        log.info("done ...");
    }

    @PreDestroy
    public void shutdown() {
        log.warning("=============================================> stopping!");
        client.shutdown();
    }

    public void send(String msg) {
        try {
            socket.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Lock(LockType.READ)
    public String getAll() {
        StringBuffer sb = new StringBuffer();
        for (String se : map.values()) {
            sb.append(se.toString());
        }
        
        return sb.toString();
    }
    
   public Collection<String> getValues() {
       return map.values();
   }

    class SimpleEventListener implements EntryAddedListener<String, String>,
            EntryRemovedListener<String, String>,
            EntryUpdatedListener<String, String>,
            EntryEvictedListener<String, String>,
            MapEvictedListener,
            MapClearedListener {

        @Override
        public void entryAdded(EntryEvent<String, String> event) {
            log.fine("Entry Added:" + event);
            if (event != null) {
                send(event.toString());
            }
        }

        @Override
        public void entryRemoved(EntryEvent<String, String> event) {
            log.fine("Entry Removed:" + event);
            if (event != null) {
                send(event.toString());
            }

        }

        @Override
        public void entryUpdated(EntryEvent<String, String> event) {
            log.fine("Entry Updated:" + event);
            if (event != null) {
                send(event.toString());
            }

        }

        @Override
        public void entryEvicted(EntryEvent<String, String> event) {
        }

        @Override
        public void mapEvicted(MapEvent event) {
        }

        @Override
        public void mapCleared(MapEvent event) {
        }

    }
}
