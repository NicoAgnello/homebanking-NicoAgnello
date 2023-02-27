package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@EnableWebSecurity

@Configuration
public class WebAuthorization extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()

                .antMatchers(HttpMethod.POST, "/api/clients").permitAll()
                .antMatchers("/web/index.html", "/web/assets/**","/web/login-register.html" ).permitAll()
                .antMatchers("/manager.html", "/api/clients", "/h2-console","/h2-console/**","/rest/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/clients/current/accounts", "/api/clients/current/cards").hasAuthority("CLIENT")
                .antMatchers("/api/clients/current", "/web/accounts.html", "/web/account.html", "/web/cards.html","/web/create-cards.html").hasAuthority("CLIENT");


        http.formLogin()

                .usernameParameter("email")

                .passwordParameter("password")

                .loginPage("/api/login");


        http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID");

        http.csrf().disable();

        //disabling frameOptions so h2-console can be accessed

        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response

        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        //redirect when a user with the wrong role enters a place that we do not want

//        http.exceptionHandling().accessDeniedHandler((req, res, ex) ->{
//
//            if(req.getRequestURI().contains("web")){
//
//                res.sendRedirect("/web/index.html");
//
//            } else {
//
//                res.sendError(HttpServletResponse.SC_FORBIDDEN);
//            }
//
//        });

        // if login is successful, just clear the flags asking for authentication

        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response

        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response

        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }

    }

    }


