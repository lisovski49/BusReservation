package com.ra.busBooking.config;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

	private static final Logger logger = LoggerFactory.getLogger(CustomSuccessHandler.class);
	private static final String USER_ROLE = "USER";
	private static final String ADMIN_ROLE = "ADMIN";
	private static final String USER_DASHBOARD_URL = "/dashboard";
	private static final String ADMIN_DASHBOARD_URL = "/adminScreen";

	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException {

		String redirectUrl = determineRedirectUrl(authentication);

		if (redirectUrl == null) {
			logger.error("Authentication failed: no roles assigned to user {}", authentication.getName());
			throw new IllegalStateException("No appropriate redirect URL found for the authenticated user.");
		}

		logger.info("User {} authenticated successfully with role(s) {}. Redirecting to: {}",
				authentication.getName(), authentication.getAuthorities(), redirectUrl);

		redirectStrategy.sendRedirect(request, response, redirectUrl);
	}

	private String determineRedirectUrl(Authentication authentication) {
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		for (GrantedAuthority grantedAuthority : authorities) {
			String authority = grantedAuthority.getAuthority();
			if (USER_ROLE.equals(authority)) {
				return USER_DASHBOARD_URL;
			} else if (ADMIN_ROLE.equals(authority)) {
				return ADMIN_DASHBOARD_URL;
			}
		}

		return null;
	}
}
