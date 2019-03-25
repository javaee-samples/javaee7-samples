package org.javaee7.jpa.listeners;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * The presence of a converter causes injection to fail in Eclipselink 2.7.4
 * @author Patrik Dudits
 */
@Converter(autoApply = true)
public class LanguageConverter implements AttributeConverter<Language, String> {
    @Override
    public String convertToDatabaseColumn(Language attribute) {
        if (attribute == null) {
            return "Unknown";
        }
        switch (attribute) {
            case ENGLISH:
                return "en";
            case GERMAN:
                return "de";
        }
        return null;
    }

    @Override
    public Language convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        switch (dbData) {
            case "en":
                return Language.ENGLISH;
            case "de":
                return Language.GERMAN;
            default:
                return null;
        }
    }
}
