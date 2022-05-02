package com.zuzex.vvolkov.config;

import com.zuzex.vvolkov.filter.AuthenticationFilter;
import com.zuzex.vvolkov.filter.AuthorizationFilter;
import com.zuzex.vvolkov.model.user.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManagerBean());
        authenticationFilter.setFilterProcessesUrl("/api/login");

        http
                .cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/login").permitAll()
                .antMatchers(
                        "/webjars/**",
                        "/swagger-ui/**",
                        "/swagger-ui/index.html",
                        "/swagger-resources/**",
                        "/v2/api-docs/**").permitAll()
                .antMatchers(POST, "/admin/role/update/**").hasAnyAuthority(Role.ADMIN.name())
                .antMatchers(POST, "/admin/guitars/add/**").hasAnyAuthority(Role.ADMIN.name())

                .antMatchers(POST, "/guitar/add/**").hasAnyAuthority(Role.MANAGER.name())
                .antMatchers(POST, "/guitar/search/**").hasAnyAuthority(Role.USER.name())
                .antMatchers(GET, "/guitar/*/users/**").hasAnyAuthority(Role.USER.name())
                .antMatchers(GET, "/guitar/all/").permitAll()
                .antMatchers(GET, "/guitar/compare/").permitAll()
                .antMatchers(GET, "/guitar/**").permitAll()

                .antMatchers("/user/save/**").permitAll()
                .antMatchers(POST, "/user/guitars/add").hasAnyAuthority(Role.USER.name())
                .antMatchers(GET, "/user/*/guitars").permitAll()
                .antMatchers(GET, "/user/token/refresh/**").permitAll()
                .antMatchers(GET, "/api/user/all/**").hasAnyAuthority(Role.MANAGER.name())
                .anyRequest().permitAll()
                .and()
                .addFilter(authenticationFilter)
                .addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
