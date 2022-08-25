package com.admin.tool.common.config.security;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.authentication.AuthenticationManagerBeanDefinitionParser;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private RestDeniedHandler restDeniedHandler;
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private AuthenticationTokenProcessingFilter authenticationTokenProcessingFilter;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .disable()
                .csrf()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .accessDeniedHandler(restDeniedHandler)
                .and()
                .headers().frameOptions().disable()
                .and()

                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .permitAll()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .disable()
                .addFilterBefore(authenticationTokenProcessingFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(new AuthenticationManagerBeanDefinitionParser.NullAuthenticationProvider());
        return http.build();
    }
    /*
        LDAP EXAMPLE
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http, LdapAuthenticator authenticator) throws Exception {
            // ...
            http.authenticationProvider(
                    new LdapAuthenticationProvider(authenticator, ldapAuthoritiesPopulator));
            // ...
            return http.build();
        }

        @Bean
        BindAuthenticator authenticator(BaseLdapPathContextSource contextSource) {
            BindAuthenticator authenticator = new BindAuthenticator(contextSource);
            authenticator.setUserSearch(
                    new FilterBasedLdapUserSearch("ou=people", "(uid={0})", contextSource));
            return authenticator;
        }
     */
}