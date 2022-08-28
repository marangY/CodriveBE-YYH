package com.example.board18.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //로그인이 되지 않은 유저가 이동할수 있는 페이지 설정
                .authorizeRequests()
                    .antMatchers("/",  "/css/**", "/board/List", "/account/register").permitAll()
                    .anyRequest().authenticated()
                    .and()
                // 로그인 페이지 설정(로그인이 되어있지 않은 유저가 로그인을 시도하면 자동으로 이동)
                .formLogin()
                    .loginPage("/account/login") // 로그인 페이지
                // 해당 페이지의 접근 권한을 모든 유저로
                    .permitAll()
                    .and()
                // 로그아웃 설정
                .logout()
                    .permitAll();


    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        // 데이터 소스를 넘겨줘서 스프링에서 인증 처리를 해줌

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery("select userID, password, enable "
                        + "from user "
                        + "where userID = ?")
                .authoritiesByUsernameQuery("select u.userID, r.name "
                        + "from user_role ur inner join user u on ur.id = u.id "
                        + "inner join role r on ur.role_id = r.id "
                        + "where u.userID = ?");
    }
    // Authentication 로그인 인증 권한
    // Authroization 권한

    // 패스워드 인코더(인증 처리에 필요) 해당 인코더 오류로 인해 교체
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}