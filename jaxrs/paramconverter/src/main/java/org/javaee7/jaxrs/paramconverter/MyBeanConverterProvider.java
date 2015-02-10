package org.javaee7.jaxrs.paramconverter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

/**
 * @author Xavier Coulon
 */
@Provider
public class MyBeanConverterProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> clazz, Type type, Annotation[] annotations) {
        if (clazz.getName().equals(MyBean.class.getName())) {

            return new ParamConverter<T>() {

                @SuppressWarnings("unchecked")
                @Override
                public T fromString(String value) {
                    MyBean bean = new MyBean();
                    bean.setValue(value);
                    return (T) bean;
                }

                @Override
                public String toString(T bean) {
                    return ((MyBean) bean).getValue();
                }

            };
        }
        return null;
    }

}
