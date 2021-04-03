package com.witherview.keycloak.oauth.account;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
public interface AccountApiService {
    @GET
    @Path("/oauth/user/{email}")
    AccountDTO.ResponseLogin getUserDetails(@PathParam("email") String email);

    @POST
    @Path("/oauth/user")
    boolean isValidPassword(AccountDTO.LoginValidateDTO login);
}
