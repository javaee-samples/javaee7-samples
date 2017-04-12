package org.javaee7.jsf.simple.facelets.test;

import java.util.ArrayList;
import java.util.List;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 *
 * @author Juraj Huska
 */
@Location("")
public class SimpleFaceletPage {

    @FindBy(tagName = "td")
    private List<WebElement> names;

    public List<String> getNames() {
        List<String> result = new ArrayList<String>();
        for (WebElement nameElement : names) {
            result.add(nameElement.getText());
        }
        return result;
    }
}
