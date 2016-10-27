package com.gluonhq.samples.cloudlink.connector.rest.web;

import com.gluonhq.samples.cloudlink.connector.rest.model.Note;
import com.gluonhq.samples.cloudlink.connector.rest.service.NoteService;

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/front")
public class FrontHandler {

    @EJB
    private NoteService noteService;

    private static final String CHARSET = "charset=UTF-8";

    @GET
    @Path("/notes")
    @Produces(MediaType.APPLICATION_JSON + "; " + CHARSET)
    public String notes() {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        List<Note> notes = noteService.findAll();
        notes.stream().map(note -> Json.createObjectBuilder()
                .add("id", note.getId())
                .add("title", note.getTitle())
                .add("text", note.getText())
                .add("creationDate", note.getCreationDate())
                .build())
                .forEach(jsonArrayBuilder::add);
        return jsonArrayBuilder.build().toString();
    }
}
