package com.example.rest;

import com.example.domain.Note;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Path("/note")
public interface NoteResource {

    @GET
    @Path("/")
    List<Note> getNotes();

    @DELETE
    @Path("/{id}")
    void removeNote(@PathParam("id") Long noteId);

    @POST
    @Path("/")
    Note saveNote(Note note);
}
