package com.company.routes;

import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.RouteBuilder;

import org.wildfly.extension.camel.CamelAware;

@Startup
@CamelAware
@ApplicationScoped
public class SimpleRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
    	from("direct:start").to("log:info");
    }
    
   
}
