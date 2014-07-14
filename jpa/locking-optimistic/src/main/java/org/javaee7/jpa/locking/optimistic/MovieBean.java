package org.javaee7.jpa.locking.optimistic;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

/**
 * @author Arun Gupta
 */
@Stateless
public class MovieBean {

    @PersistenceContext
    EntityManager em;
    
    public void addMovies() {
        Movie[] movies = new Movie[4];
        movies[0] = new Movie(1, "The Matrix", "Keanu Reeves, Laurence Fishburne, Carrie-Ann Moss");
        movies[1] = new Movie(2, "The Lord of The Rings", "Elijah Wood, Ian Mckellen, Viggo Mortensen");
        movies[2] = new Movie(3, "Inception", "Leonardo DiCaprio");
        movies[3] = new Movie(4, "The Shining", "Jack Nicholson, Shelley Duvall");
        for (Movie m : movies) {
            em.persist(m);
        }
    }

    public List<Movie> listMovies() {
        return em.createNamedQuery("Movie.findAll", Movie.class).getResultList();
    }

    public void updateMovie() {
        Movie m = em.find(Movie.class, 3);
//        em.lock(m, LockModeType.OPTIMISTIC);
        m.setName("INCEPTION");
        em.merge(m);
        em.flush();
    }
    
    public void deleteMovie() {
        Movie m = em.find(Movie.class, 1);
        em.remove(m);
        em.flush();
    }
    
}
