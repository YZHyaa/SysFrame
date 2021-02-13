package com.mdt.listener;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

@Component
public class PropertyListener implements InitializingBean,
        EmbeddedValueResolverAware {

    private static StringValueResolver stringValueResolver;

    public static String getPropertyValue(String name) {
        return stringValueResolver.resolveStringValue(name);
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        // TODO Auto-generated method stub
        this.stringValueResolver = resolver;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub

    }

}
