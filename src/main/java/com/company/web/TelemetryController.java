/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.web;

import com.company.beans.HazelcastClientBean;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
 
/**
 *
 * @author 
 */

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class TelemetryController {
 
    
    @EJB
    private HazelcastClientBean bean;
   
    
    @GET
    @Path("/telemetry")
    public String getTelemetry() {
        
        return bean.getAll();
    }
    
    
//    @GET
//@Produces({MediaType.TEXT_HTML})
//public InputStream viewHome()
//{
//   File f = getFileFromSomewhere();
//   return new FileInputStream(f);
//}
   
}
