package com.witherview.keycloak.oauth.remoteuserstorage;

import com.witherview.keycloak.oauth.account.AccountApiService;
import com.witherview.keycloak.oauth.account.AccountDTO;
import lombok.NoArgsConstructor;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.credential.UserCredentialStore;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.adapter.AbstractUserAdapter;
import org.keycloak.storage.user.UserLookupProvider;

@NoArgsConstructor
public class RemoteUserStorageProvider implements UserStorageProvider, UserLookupProvider, CredentialInputValidator
{
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
        return null;
//        System.out.println("getUserById");
//        StorageId storageId = new StorageId(id);
//        String email = storageId.getExternalId();
//        return getUserByEmail(email, realm);
    }

    @Override
    public UserModel getUserByUsername(String username, RealmModel realm) {
        return null;
    }

    @Override
    public UserModel getUserByEmail(String email, RealmModel realm) {
        UserModel returnValue = null;
        var user = accountApiService.getUserDetails(email);
        if (user != null) {
            returnValue = createUserModel(user.getId(), realm);
        }
        return returnValue;
    }

    private UserModel createUserModel(String id, RealmModel realm) {
        UserModel user = new AbstractUserAdapter(session, realm, model) {
            @Override
            public String getUsername() { return id; }
        };
        return user;
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
        return !result;
    }
    private UserCredentialStore getCredentialStore() {
        return session.userCredentialManager();
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput credentialInput) {
        var userId = user.getId().split(":")[2];
        AccountDTO.LoginValidateDTO requestBody = new AccountDTO.LoginValidateDTO();
        requestBody.setUserId(userId);
        requestBody.setPassword(credentialInput.getChallengeResponse());
        var result = accountApiService.isValidPassword(requestBody);
        return result;
    }
}
