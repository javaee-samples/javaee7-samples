package org.javaee7.jaxrs.angularjs;

import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Location("")
public class TodoPage {

    @FindBy(css = "#add-note")
    private WebElement addNote;

    @FindBy(css = "#edit-note-form .btn-primary")
    private WebElement save;

    @FindBy(css = "#edit-note-form textarea")
    private WebElement summaryInput;

    @FindBy(css = "#edit-note-form input")
    private WebElement tileInput;

    @FindBy(css = "#notes .note")
    private List<NoteItem> todos;

    public List<NoteItem> getTodos()
    {
        return todos;
    }

    public void addNote()
    {
        addNote.click();
    }

    public void save()
    {
        save.click();
    }

    public void typeSummary(String text)
    {
        summaryInput.sendKeys(text);
    }

    public void typeTitle(String text)
    {
        tileInput.sendKeys(text);
    }

    public static class NoteItem {

        @FindBy(className = "btn-danger")
        private WebElement removeButton;

        @FindBy(className = "summary")
        private WebElement summary;

        @FindBy(className = "title")
        private WebElement title;

        public String getSummary()
        {
            return summary.getText();
        }

        public String getTitle()
        {
            return title.getText();
        }

        public void remove()
        {
            removeButton.click();
        }
    }
}
