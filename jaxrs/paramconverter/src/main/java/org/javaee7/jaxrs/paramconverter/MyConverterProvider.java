package org.javaee7.jaxrs.paramconverter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

/**
 * @author Xavier Coulon
 *
 */
@Provider
public class MyConverterProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(final Class<T> rawType, final Type genericType,
        final Annotation[] annotations) {
        if (rawType.getName().equals(MyBean.class.getName())) {
            return new ParamConverter<T>() {

                @Override
                public T fromString(String value) {
                    MyBean myBean = new MyBean();
                    myBean.setValue(value);
                    return rawType.cast(myBean);
                }

                @Override
                public String toString(T myBean) {
                    if (myBean == null) {
                        return null;
                    }
                    return myBean.toString();
                }
            };
        }
        return null;
    }

}
