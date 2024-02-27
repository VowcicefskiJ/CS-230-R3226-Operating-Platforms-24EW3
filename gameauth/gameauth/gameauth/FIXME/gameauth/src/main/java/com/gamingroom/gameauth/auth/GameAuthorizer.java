package com.gamingroom.gameauth.auth;

import io.dropwizard.auth.Authorizer;

public class GameAuthorizer implements Authorizer<GameUser> {
    @Override
    public boolean authorize(GameUser user, String role) {
        // Authorize user based on roles
        return user.getRoles().contains(role);
    }
}
