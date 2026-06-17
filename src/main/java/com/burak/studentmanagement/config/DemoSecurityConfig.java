package com.burak.studentmanagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class DemoSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// 🔥 DATABASE KA LAFDA HI KHATAM: Direct Memory Me Users Bana Diye Demo Ke Liye
		auth.inMemoryAuthentication()
				.withUser("admin")
				.password(passwordEncoder().encode("1"))
				.roles("ADMIN")
				.and()
				.withUser("teacher")
				.password(passwordEncoder().encode("123456"))
				.roles("TEACHER")
				.and()
				.withUser("student")
				.password(passwordEncoder().encode("123456"))
				.roles("STUDENT");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/register/**").permitAll()
				.antMatchers("/").authenticated()
				.antMatchers("/admin/**").hasRole("ADMIN")
				.antMatchers("/student/**").hasRole("STUDENT")
				.antMatchers("/teacher/**").hasRole("TEACHER")
				.and()
				.formLogin()
				.loginPage("/showLoginPage")
				.loginProcessingUrl("/authenticateTheUser")
				.successHandler(customAuthenticationSuccessHandler)
				.permitAll()
				.and()
				.logout().permitAll()
				.and()
				.exceptionHandling().accessDeniedPage("/access-denied");
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}