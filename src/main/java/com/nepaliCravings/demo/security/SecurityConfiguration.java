package com.nepaliCravings.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.nepaliCravings.demo.service.AdminService;
import com.nepaliCravings.demo.service.RestaurantService;
import com.nepaliCravings.demo.service.UserService;

@Configuration

public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;

	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	CustomSuccessHandler customSuccessHandler;

	@Autowired
	private AdminService adminService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/").permitAll().antMatchers("/registration").permitAll()
				.antMatchers("/restaurant/**").permitAll()
				.antMatchers("/adminregistration").permitAll().antMatchers("/restaurantregistration").permitAll()
				.antMatchers("/css/**", "/image/**").permitAll().anyRequest().authenticated().and().exceptionHandling()
				.accessDeniedPage("/accessdenied").and().formLogin().loginPage("/login").permitAll()
				.successHandler(customSuccessHandler).and().logout().invalidateHttpSession(true)
				.clearAuthentication(true).logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout").permitAll().deleteCookies("JSESSIONID");
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider restaurantAuthenticationProviders() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(restaurantService);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;

	}

	@Bean
	public DaoAuthenticationProvider userAuthenticationProviders() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userService);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}

	@Bean
	public DaoAuthenticationProvider adminAuthenticationProviders() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(adminService);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(userAuthenticationProviders());
		auth.authenticationProvider(adminAuthenticationProviders());
		auth.authenticationProvider(restaurantAuthenticationProviders());
	}

}