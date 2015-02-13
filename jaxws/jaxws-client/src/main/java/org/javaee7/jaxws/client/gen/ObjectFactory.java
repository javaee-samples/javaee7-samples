package org.javaee7.jaxws.client.gen;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.javaee7.jaxws.client.gen package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _FindEBooks_QNAME = new QName("http://endpoint.jaxws.javaee7.org/", "findEBooks");
    private final static QName _SaveBookResponse_QNAME = new QName("http://endpoint.jaxws.javaee7.org/", "saveBookResponse");
    private final static QName _WelcomeMessage_QNAME = new QName("http://endpoint.jaxws.javaee7.org/", "welcomeMessage");
    private final static QName _FindEBooksResponse_QNAME = new QName("http://endpoint.jaxws.javaee7.org/", "findEBooksResponse");
    private final static QName _AddAppendix_QNAME = new QName("http://endpoint.jaxws.javaee7.org/", "addAppendix");
    private final static QName _AddAppendixResponse_QNAME = new QName("http://endpoint.jaxws.javaee7.org/", "addAppendixResponse");
    private final static QName _TakeBook_QNAME = new QName("http://endpoint.jaxws.javaee7.org/", "takeBook");
    private final static QName _WelcomeMessageResponse_QNAME = new QName("http://endpoint.jaxws.javaee7.org/", "welcomeMessageResponse");
    private final static QName _TakeBookResponse_QNAME = new QName("http://endpoint.jaxws.javaee7.org/", "takeBookResponse");
    private final static QName _SaveBook_QNAME = new QName("http://endpoint.jaxws.javaee7.org/", "saveBook");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.javaee7.jaxws.client.gen
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link EBook }
     * 
     */
    public EBook createEBook() {
        return new EBook();
    }

    /**
     * Create an instance of {@link TakeBookResponse }
     * 
     */
    public TakeBookResponse createTakeBookResponse() {
        return new TakeBookResponse();
    }

    /**
     * Create an instance of {@link FindEBooksResponse }
     * 
     */
    public FindEBooksResponse createFindEBooksResponse() {
        return new FindEBooksResponse();
    }

    /**
     * Create an instance of {@link AddAppendix }
     * 
     */
    public AddAppendix createAddAppendix() {
        return new AddAppendix();
    }

    /**
     * Create an instance of {@link FindEBooks }
     * 
     */
    public FindEBooks createFindEBooks() {
        return new FindEBooks();
    }

    /**
     * Create an instance of {@link AddAppendixResponse }
     * 
     */
    public AddAppendixResponse createAddAppendixResponse() {
        return new AddAppendixResponse();
    }

    /**
     * Create an instance of {@link WelcomeMessage }
     * 
     */
    public WelcomeMessage createWelcomeMessage() {
        return new WelcomeMessage();
    }

    /**
     * Create an instance of {@link TakeBook }
     * 
     */
    public TakeBook createTakeBook() {
        return new TakeBook();
    }

    /**
     * Create an instance of {@link WelcomeMessageResponse }
     * 
     */
    public WelcomeMessageResponse createWelcomeMessageResponse() {
        return new WelcomeMessageResponse();
    }

    /**
     * Create an instance of {@link SaveBookResponse }
     * 
     */
    public SaveBookResponse createSaveBookResponse() {
        return new SaveBookResponse();
    }

    /**
     * Create an instance of {@link SaveBook }
     * 
     */
    public SaveBook createSaveBook() {
        return new SaveBook();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindEBooks }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://endpoint.jaxws.javaee7.org/", name = "findEBooks")
    public JAXBElement<FindEBooks> createFindEBooks(FindEBooks value) {
        return new JAXBElement<FindEBooks>(_FindEBooks_QNAME, FindEBooks.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveBookResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://endpoint.jaxws.javaee7.org/", name = "saveBookResponse")
    public JAXBElement<SaveBookResponse> createSaveBookResponse(SaveBookResponse value) {
        return new JAXBElement<SaveBookResponse>(_SaveBookResponse_QNAME, SaveBookResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WelcomeMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://endpoint.jaxws.javaee7.org/", name = "welcomeMessage")
    public JAXBElement<WelcomeMessage> createWelcomeMessage(WelcomeMessage value) {
        return new JAXBElement<WelcomeMessage>(_WelcomeMessage_QNAME, WelcomeMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindEBooksResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://endpoint.jaxws.javaee7.org/", name = "findEBooksResponse")
    public JAXBElement<FindEBooksResponse> createFindEBooksResponse(FindEBooksResponse value) {
        return new JAXBElement<FindEBooksResponse>(_FindEBooksResponse_QNAME, FindEBooksResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddAppendix }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://endpoint.jaxws.javaee7.org/", name = "addAppendix")
    public JAXBElement<AddAppendix> createAddAppendix(AddAppendix value) {
        return new JAXBElement<AddAppendix>(_AddAppendix_QNAME, AddAppendix.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddAppendixResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://endpoint.jaxws.javaee7.org/", name = "addAppendixResponse")
    public JAXBElement<AddAppendixResponse> createAddAppendixResponse(AddAppendixResponse value) {
        return new JAXBElement<AddAppendixResponse>(_AddAppendixResponse_QNAME, AddAppendixResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TakeBook }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://endpoint.jaxws.javaee7.org/", name = "takeBook")
    public JAXBElement<TakeBook> createTakeBook(TakeBook value) {
        return new JAXBElement<TakeBook>(_TakeBook_QNAME, TakeBook.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WelcomeMessageResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://endpoint.jaxws.javaee7.org/", name = "welcomeMessageResponse")
    public JAXBElement<WelcomeMessageResponse> createWelcomeMessageResponse(WelcomeMessageResponse value) {
        return new JAXBElement<WelcomeMessageResponse>(_WelcomeMessageResponse_QNAME, WelcomeMessageResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TakeBookResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://endpoint.jaxws.javaee7.org/", name = "takeBookResponse")
    public JAXBElement<TakeBookResponse> createTakeBookResponse(TakeBookResponse value) {
        return new JAXBElement<TakeBookResponse>(_TakeBookResponse_QNAME, TakeBookResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveBook }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://endpoint.jaxws.javaee7.org/", name = "saveBook")
    public JAXBElement<SaveBook> createSaveBook(SaveBook value) {
        return new JAXBElement<SaveBook>(_SaveBook_QNAME, SaveBook.class, null, value);
    }

}
