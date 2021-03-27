package com.witherview.keycloak.oauth.account;

import lombok.Getter;
import lombok.Setter;

public class AccountDTO {
    @Getter @Setter
    public static class ResponseLogin {
        private String email;
        private String name;
        private String profileImg;
        private String mainIndustry;
        private String subIndustry;
        private String mainJob;
        private String subJob;
    }
}
