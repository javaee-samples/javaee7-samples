package org.javaee7.jsf.file.upload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.servlet.http.Part;

/**
 * @author Arun Gupta
 */
@Named
@RequestScoped
public class FileUploadBean {
    private Part file;
    private String text;

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        System.out.println("Got file ...");
        this.file = file;
        if (null != file) {
            System.out.println("... and trying to read it ...");
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
                String string = reader.readLine();
                StringBuilder builder = new StringBuilder();
                while (string != null) {
                    builder.append(string);
                    string = reader.readLine();
                }
                text = builder.toString();
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
            System.out.println("... completed reading file.");
        } else {
            System.out.println("... but its null.");
        }
    }

    public String getText() {
        System.out.println("Complete text: " + text);
        return text;
    }
}
