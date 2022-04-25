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

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/v2/api-docs/**");
//        web.ignoring().antMatchers("/swagger.json");
//        web.ignoring().antMatchers("/swagger-ui.html");
//        web.ignoring().antMatchers("/swagger-resources/**");
//        web.ignoring().antMatchers("/webjars/**");
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManagerBean());
        authenticationFilter.setFilterProcessesUrl("/login");

        http
                .cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
//                .authorizeRequests()
//                .antMatchers("/login/**").permitAll()
//                .antMatchers("/webjars/**").permitAll()
//                .antMatchers("/swagger-ui/**").permitAll()
//                .antMatchers("/swagger-resources/**").permitAll()
//                .antMatchers("/v2/**").permitAll()
//                .antMatchers(POST, "/admin/role/update/**").hasAnyAuthority(Role.ADMIN.name())
//                .antMatchers(POST, "/admin/guitars/add/**").hasAnyAuthority(Role.ADMIN.name())
//
//                .antMatchers(POST, "/guitar/add/**").hasAnyAuthority(Role.MANAGER.name())
//                .antMatchers(POST, "/guitar/search/**").hasAnyAuthority(Role.USER.name())
//                .antMatchers(GET, "/guitar/*/users/**").hasAnyAuthority(Role.USER.name())
//                .antMatchers(GET, "/guitar/all/").permitAll()
//                .antMatchers(GET, "/guitar/compare/").permitAll()
//                .antMatchers(GET, "/guitar/**").permitAll()
//
//                .antMatchers("/user/save/**").permitAll()
//                .antMatchers(POST, "/user/guitars/add").hasAnyAuthority(Role.USER.name())
//                .antMatchers(GET, "/user/*/guitars").permitAll()
//                .antMatchers(GET, "/user/token/refresh/**").permitAll()
//                .antMatchers(GET, "/user/all/**").hasAnyAuthority(Role.MANAGER.name())
                .authorizeRequests()
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
