package org.javaee7.jpa.extended.pc;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;

@SessionScoped
public class BigBangTheoryService implements Serializable {

    @Inject
    CharactersBean characters;

    public void addWilWheaton() {
        Character wil = new Character(8, "Wil Wheaton");
        characters.save(wil);
    }

    public void updateRaj() {
        for (Character characterzz : characters.get()) {
            if ("Raj".equals(characterzz.getName())) {
                characterzz.setName("Rajesh Ramayan");
                characters.save(characterzz);
            }
        }
    }

    public void proceed() {
        characters.commitChanges();
    }
}
