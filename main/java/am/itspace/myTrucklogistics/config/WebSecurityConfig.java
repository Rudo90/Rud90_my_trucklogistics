package am.itspace.myTrucklogistics.config;

import am.itspace.myTrucklogistics.security.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final UserDetailServiceImpl userDetailService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .formLogin()
                .loginPage("/loginToAccount")
                .defaultSuccessUrl("/myAccount", true)
                .and()
                .exceptionHandling().accessDeniedPage("/loginToAccount")
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/").permitAll()
                .antMatchers(HttpMethod.GET, "/register").permitAll()
                .antMatchers("/registerLogin").permitAll()
                .antMatchers(HttpMethod.GET, "/loginToAccount").permitAll()
                .antMatchers(HttpMethod.GET, "/myAccount").hasAnyAuthority("ADMIN", "BROKER", "CUSTOMER", "DRIVER")
                .antMatchers(HttpMethod.GET,"/myAccount/user/edit").hasAuthority("CUSTOMER")
                .antMatchers(HttpMethod.GET, "/myAccount/user/orders").hasAuthority("CUSTOMER")
                .antMatchers(HttpMethod.POST, "/myAccount/user/update").hasAuthority("CUSTOMER")
                .antMatchers(HttpMethod.POST, "/myAccount/user/orders").hasAuthority("CUSTOMER")
                .antMatchers(HttpMethod.GET, "/myAccount/user/myOrders").hasAuthority("CUSTOMER")
                .antMatchers(HttpMethod.POST, "/myAccount/allDrivers").hasAuthority("BROKER")
                .antMatchers(HttpMethod.GET, "/myAccount/orders").hasAuthority("BROKER")
                .antMatchers(HttpMethod.POST, "/myAccount/broker/orders").hasAuthority("BROKER");


    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
}