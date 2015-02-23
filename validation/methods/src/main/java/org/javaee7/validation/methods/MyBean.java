package org.javaee7.validation.methods;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Arun Gupta
 */
@RequestScoped
public class MyBean {
    public String sayHello(@Size(max = 3) String name) {
        return "Hello " + name;
    }

    @Future
    public Date showDate(boolean correct) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, correct ? 5 : -5);
        return cal.getTime();
    }

    public String showList(@NotNull @Size(min = 1, max = 3) List<String> list, @NotNull String prefix) {
        StringBuilder builder = new StringBuilder();

        for (String s : list) {
            builder.append(prefix).append(s).append(" ");
        }

        return builder.toString();
    }

    //    @NotNull(validationAppliesTo=ConstraintType.PARAMETERS)
    //    @NotNull
    //    public void concat(String str1, String str2) {
    //        str1.concat(str2);
    //    }
}
