package com.example.rest;

import com.example.domain.Note;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class NoteResourceImpl implements NoteResource {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Note> getNotes()
    {
        return entityManager.createQuery("from Note order by id", Note.class).getResultList();
    }

    @Override
    public void removeNote(Long noteId)
    {
        final Note note = entityManager.find(Note.class, noteId);
        if (null == note) {
            throw new NoResultException("No note with id " + noteId + " found");
        }
        entityManager.remove(note);
    }

    @Override
    public Note saveNote(Note note)
    {
        entityManager.persist(note);
        return note;
    }
}
