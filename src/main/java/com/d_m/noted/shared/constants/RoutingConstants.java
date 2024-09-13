package com.d_m.noted.shared.constants;

public class RoutingConstants {
    public static String[] LOGGED_OUT_ROUTES = {
            "/api/v1/auth/sign-in",
            "/api/v1/auth/sign-up",
            "/api/v1/auth/change-password",
            "/oauth2/authorization/github"
    };

    public static String[] ADMIN_ROUTES = {
            "/h2-console"
    };
}
