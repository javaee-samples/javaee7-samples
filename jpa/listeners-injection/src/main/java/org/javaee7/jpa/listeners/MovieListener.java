package org.javaee7.jpa.listeners;

import javax.ejb.EJB;
import javax.persistence.PostLoad;

/**
 * @author Kuba Marchwicki
 */
public class MovieListener {

    @EJB
    RatingService service;

    @PostLoad
    public void loadMovieRating(Movie movie) {
        Integer rating = service.movieRating(movie.getName());
        movie.setRating(rating);
    }
}
