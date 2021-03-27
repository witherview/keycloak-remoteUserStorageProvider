package com.witherview.keycloak.oauth.remoteuserstorage;

import com.witherview.keycloak.oauth.account.AccountApiService;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;
import org.springframework.beans.factory.annotation.Value;

public class RemoteUserStorageProviderFactory implements UserStorageProviderFactory<RemoteUserStorageProvider> {
    public static final String PROVIDER_NAME = "witherview-MySQL";

//    @Value("${connect.server}")
    private String serviceUri = "http://localhost:8080";

    @Override
    public RemoteUserStorageProvider create(KeycloakSession session, ComponentModel model) {
        return new RemoteUserStorageProvider(session, model, buildHttpClient(serviceUri));
    }

    @Override
    public String getId() { return PROVIDER_NAME; }

    private AccountApiService buildHttpClient(String uri) {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(uri);
        return target.proxyBuilder(AccountApiService.class)
                .classloader(AccountApiService.class.getClassLoader())
                .build();
    }
}
