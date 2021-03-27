package com.witherview.keycloak.oauth.remoteuserstorage;

import com.witherview.keycloak.oauth.account.AccountApiService;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.credential.UserCredentialStore;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.adapter.AbstractUserAdapter;
import org.keycloak.storage.user.UserLookupProvider;

public class RemoteUserStorageProvider implements UserStorageProvider, UserLookupProvider, CredentialInputValidator {
    private KeycloakSession session;
    private ComponentModel model;
    private AccountApiService accountApiService;

    public RemoteUserStorageProvider(KeycloakSession session, ComponentModel model, AccountApiService accountApiService) {
        this.session = session;
        this.model = model;
        this.accountApiService = accountApiService;
    }

    @Override
    public void close() {

    }

    @Override
    public UserModel getUserById(String id, RealmModel realm) {
        StorageId storageId = new StorageId(id);
        String email = storageId.getExternalId();
        return getUserByEmail(email, realm);
    }

    @Override
    public UserModel getUserByUsername(String username, RealmModel realm) {
        return null;
    }

    @Override
    public UserModel getUserByEmail(String email, RealmModel realm) {
        // 사용자가 있는지 api 호출 -> 있을 경우 createUserModel메소드 실행.
        UserModel returnValue = null;
        var user = accountApiService.getUserDetails(email);
        if (user != null) {
            returnValue = createUserModel(email, realm);
        }
        return returnValue;
    }

    private UserModel createUserModel(String email, RealmModel realm) {
        return new AbstractUserAdapter(session, realm, model) {
            @Override
            public String getUsername() { return email; }
            @Override
            public String getEmail(){ return email; }
        };
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        // password 타입의 credential을 지원하는지 여부. return True.
        return PasswordCredentialModel.TYPE.equals(credentialType);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        if (!supportsCredentialType(credentialType))
            return false;
        var result = getCredentialStore()
                .getStoredCredentialsStream(realm, user)
                .findAny().isPresent();
        System.out.println(result);
        return !result;
    }
    private UserCredentialStore getCredentialStore() {
        return session.userCredentialManager();
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput credentialInput) {
        // 기존 서비스 로직상, userId / password가 다를 경우 이 메소드까지 오지 않고 Exception처리가 된다.
        return true;
    }
}
