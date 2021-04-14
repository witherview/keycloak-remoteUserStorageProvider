package com.witherview.keycloak.oauth.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDTO {
    @Getter @Setter
    public static class LoginValidateDTO {
        private String userId;
        private String password;
    }
    @Getter @Setter
    public static class ResponseLogin {
        private String id;
        private String email;
        private String name;
        private String profileImg;
        private String mainIndustry;
        private String subIndustry;
        private String mainJob;
        private String subJob;
    }
}
