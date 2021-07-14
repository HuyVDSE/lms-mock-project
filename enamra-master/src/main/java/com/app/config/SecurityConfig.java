package com.app.config;

import com.app.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService());

        return authenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/files/**", "/academic/**","/professional/**","/general/**",
                        "**/img/**","**/img/logo.png","**/img/favicon.ico","/v/**",
                        "/error","/user/logout",
                        "/g/topic/**","/g/**", "/signin-google", "/user/signup","/user/login", "/user/settings/**", "/user/verify",
                        "/","/entry","/blog/**", "/user/login?error=true",
                        "/media/files/blog/img/**",
                        "**/webjars/**","/webjars/**","/resources/static/**").permitAll()


                .antMatchers("/resources/**","/resources/static/upload_files",
                        "/resources/static/upload_files/**","/resources/static/img/**",
                        "/resources/static/user_files/**", "/img/**", "/fonts/**",
                        "/css/**","/js/**").permitAll()

                .antMatchers("/home").fullyAuthenticated()

                .antMatchers("/admin_Dashboard","/admin","/admin/**").hasAnyAuthority("ADMIN")
                //   .anyRequest().authenticated()
                .antMatchers("/hr/**").hasAnyAuthority("ADMIN","HR")// "/hr_Dashboard"
                .antMatchers("/manager/**","/manager_dashboard").hasAnyAuthority("ADMIN","MANAGER")
                .antMatchers("/comment/create","/comment/**","/blog/file_upload").hasAnyRole("USER","ADMIN")
                .anyRequest().authenticated()
                .antMatchers("/your_Dashboard","/user","/user/**").hasAuthority("USER")
                //.anyRequest().authenticated()
//                .and()
//                .oauth2Login().failureUrl("/user/login?error=true")
//                .defaultSuccessUrl("/home", true)
                .and()
                .csrf().disable()
                .formLogin().loginPage("/user/login").failureForwardUrl("/user/settings/checkVerify")
                .defaultSuccessUrl("/home", true)
                .usernameParameter("email")
                .passwordParameter("password")
                .and().logout().logoutUrl("/user/logout").logoutSuccessUrl("/");
    }


}

