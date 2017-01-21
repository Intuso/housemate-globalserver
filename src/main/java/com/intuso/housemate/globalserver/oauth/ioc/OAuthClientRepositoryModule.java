package com.intuso.housemate.globalserver.oauth.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.intuso.housemate.globalserver.oauth.OAuthClientRepository;
import com.intuso.housemate.globalserver.oauth.OAuthClientRepositoryImpl;

/**
 * Created by tomc on 21/01/17.
 */
public class OAuthClientRepositoryModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(OAuthClientRepository.class).to(OAuthClientRepositoryImpl.class);
        bind(OAuthClientRepositoryImpl.class).in(Scopes.SINGLETON);
    }
}
