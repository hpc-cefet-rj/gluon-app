package com.gluonhq.samples.cloudlink.connector.rest.handler;

import com.gluonhq.samples.cloudlink.connector.rest.model.Settings;
import com.gluonhq.samples.cloudlink.connector.rest.service.SettingsService;

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The REST handler that is able to handle incoming REST HTTP requests coming from Gluon CloudLink pertaining objects.
 *
 * The full path in the application is for example: http://HOSTNAME:PORT/CONTEXTROOT/rest/object/abcd
 */
@Path("/object/{objectIdentifier}")
public class ObjectHandler {

    @EJB
    private SettingsService settingsService;

    private static final Logger LOG = Logger.getLogger(ObjectHandler.class.getName());

    private static final String CHARSET = "charset=UTF-8";

    /**
     * Called by Gluon CloudLink when an object with the specified identifier is retrieved for the first time from the
     * client application, but does not yet exist in Gluon CloudLink. This implementation will return the JSON payload
     * of the Settings object that is read from the database.
     *
     * @param objectIdentifier the identifier of the object that is being retrieved from Gluon CloudLink
     * @return a string representation of the constructed JSON Object
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON + "; " + CHARSET)
    public String getObject(@PathParam("objectIdentifier") String objectIdentifier) {
        LOG.log(Level.INFO, "Return object " + objectIdentifier);
        Settings settings = settingsService.findSettings();
        return Json.createObjectBuilder()
                .add("showDate", settings.isShowDate())
                .add("ascending", settings.isAscending())
                .add("sortingId", settings.getSortingId())
                .add("fontSize", settings.getFontSize())
                .build().toString();
    }

    /**
     * Called by Gluon CloudLink when a new object is added. This implementation will update the Settings object in the
     * database.
     *
     * @param objectIdentifier the identifier of the object that is added
     * @param payload the raw JSON payload of the object that is added
     * @return an empty response, as the response is ignored by Gluon CloudLink
     */
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON + "; " + CHARSET)
    @Produces(MediaType.APPLICATION_JSON + "; " + CHARSET)
    public String addObject(@PathParam("objectIdentifier") String objectIdentifier,
                            String payload) {
        LOG.log(Level.INFO, "Added object with id " + objectIdentifier + ": " + payload);
        try (JsonReader jsonReader = Json.createReader(new StringReader(payload))) {
            JsonObject jsonObject = jsonReader.readObject();
            boolean showDate = !jsonObject.containsKey("showDate") || jsonObject.getBoolean("showDate");
            boolean ascending = !jsonObject.containsKey("ascending") || jsonObject.getBoolean("ascending");
            int sortingId = jsonObject.containsKey("sortingId") ? jsonObject.getInt("sortingId") : 0;
            int fontSize = jsonObject.containsKey("fontSize") ? jsonObject.getInt("fontSize") : 10;
            settingsService.updateSettings(showDate, ascending, sortingId, fontSize);
        }
        return "{}";
    }

    /**
     * Called by Gluon CloudLink when an existing object is updated. This implementation will update the Settings object
     * in the database.
     *
     * @param objectIdentifier the identifier of the object that is updated
     * @param payload the raw JSON payload of the object that is updated
     * @return an empty response, as the response is ignored by Gluon CloudLink
     */
    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON + "; " + CHARSET)
    @Produces(MediaType.APPLICATION_JSON + "; " + CHARSET)
    public String updateObject(@PathParam("objectIdentifier") String objectIdentifier,
                               String payload) {
        LOG.log(Level.INFO, "Updated object with id " + objectIdentifier + ": " + payload);
        try (JsonReader jsonReader = Json.createReader(new StringReader(payload))) {
            JsonObject jsonObject = jsonReader.readObject();
            boolean showDate = !jsonObject.containsKey("showDate") || jsonObject.getBoolean("showDate");
            boolean ascending = !jsonObject.containsKey("ascending") || jsonObject.getBoolean("ascending");
            int sortingId = jsonObject.containsKey("sortingId") ? jsonObject.getInt("sortingId") : 0;
            int fontSize = jsonObject.containsKey("fontSize") ? jsonObject.getInt("fontSize") : 10;
            settingsService.updateSettings(showDate, ascending, sortingId, fontSize);
        }
        return "{}";
    }

    /**
     * Called by Gluon CloudLink when an existing object is removed. This implementation will update the Settings object
     * in the database with default values.
     *
     * @param objectIdentifier the identifier of the object that is removed
     * @return an empty response, as the response is ignored by Gluon CloudLink
     */
    @POST
    @Path("/remove")
    @Produces(MediaType.APPLICATION_JSON + "; " + CHARSET)
    public String removeObject(@PathParam("objectIdentifier") String objectIdentifier) {
        LOG.log(Level.INFO, "Removed object with id " + objectIdentifier);
        settingsService.updateSettings(true, true, 0, 10);
        return "{}";
    }

}
