package com.gluonhq.samples.cloudlink.connector.rest;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * The JAX-RS application listening at /rest/* for incoming REST requests.
 */
@ApplicationPath("rest")
public class ConnectorRestApp extends ResourceConfig {
    public ConnectorRestApp() {
        packages(true, ConnectorRestApp.class.getPackage().getName());
    }
}
