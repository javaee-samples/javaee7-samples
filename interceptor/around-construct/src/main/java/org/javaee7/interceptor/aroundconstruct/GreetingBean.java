package org.javaee7.interceptor.aroundconstruct;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * @author Radim Hanus
 */
@MyInterceptorBinding
public class GreetingBean implements Greeting {
    private boolean constructed = false;
    private boolean initialized = false;

    private Param param;

    @Inject
    public GreetingBean(Param param) {
        this.param = param;
        constructed = true;
    }

    @PostConstruct
    void onPostConstruct() {
        initialized = true;
    }

    @Override
    public boolean isConstructed() {
        return constructed;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public Param getParam() {
        return param;
    }
}
