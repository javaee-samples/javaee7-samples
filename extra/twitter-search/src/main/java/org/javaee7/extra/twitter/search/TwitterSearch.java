package org.javaee7.extra.twitter.search;

import javax.inject.Inject;
import org.glassfish.samples.twitter.api.SearchResults;
import org.glassfish.samples.twitter.api.Twitter;

/**
 * @author Arun Gupta
 */
public class TwitterSearch {

    @Inject
    Twitter twitter;

    public SearchResults getResults(String query) {
        return twitter.search(query, SearchResults.class);
    }
}
