package org.javaee7.jaxws.endpoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.jws.WebService;

/**
 * 
 * @author Fermin Gallego
 *
 */
@WebService(endpointInterface = "org.javaee7.jaxws.endpoint.EBookStore",
    serviceName = "EBookStoreImplService")
public class EBookStoreImpl implements EBookStore {

    private HashMap<String, EBook> eBookCollection = new HashMap<String, EBook>();

    @Override
    public String welcomeMessage(String name) {
        return "Welcome to EBookStore WebService, Mr/Mrs " + name;
    }

    @Override
    public List<String> findEBooks(String text) {
        List<String> foundTitles = new ArrayList<String>();
        for (String title : eBookCollection.keySet()) {
            if (title.contains(text)) {
                foundTitles.add(title);
            }
        }
        return foundTitles;
    }

    @Override
    public EBook takeBook(String title) {
        return eBookCollection.get(title);
    }

    @Override
    public void saveBook(EBook eBook) {
        eBookCollection.put(eBook.getTitle(), eBook);

    }

    @Override
    public EBook addAppendix(EBook eBook, int appendixPages) {
        eBook.setNumPages((eBook.getNumPages() + appendixPages));
        eBookCollection.put(eBook.getTitle(), eBook);
        return eBook;
    }

}
