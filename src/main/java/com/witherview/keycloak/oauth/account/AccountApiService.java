package com.witherview.keycloak.oauth.account;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
public interface AccountApiService {
    @GET
    @Path("/oauth/user/{email}")
    AccountDTO.ResponseLogin getUserDetails(@PathParam("email") String email);
}
