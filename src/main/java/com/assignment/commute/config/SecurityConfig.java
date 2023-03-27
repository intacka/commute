package com.assignment.commute.config;

import com.assignment.commute.controller.TaskImplementingLogoutHandler;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * PasswordEncoder를 Bean으로 등록
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }




    @Autowired
    private DataSource dataSource;
    private final AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()    // 요청에 대한 권한 지정. Security 처리에 HttpServletRequest를 이용한다는것을 의미.
                .antMatchers("/adminOnly").hasAuthority("ROLE_ADMIN")   // andMatchers:특정 URL을 지정.   hasAuthority:특정 권한을 갖고 있는사람만 접근.
                .antMatchers("/**").permitAll()  // 넓은 범위의 URL을 아래에 배치한다.
                .anyRequest().authenticated() // anyRuquest:설정한 경로 외에 모든 경로를 뜻함.   authenticated:인증된 사용자만이 접근가능 // 권한별 접속을 활성화
                .and()
                .formLogin()
                    .usernameParameter("memberId")
                    .passwordParameter("memberPw")
                    .loginPage("/login") // 사용자 정의 로그인 페이지
                    .defaultSuccessUrl("/main") // 로그인 성공 후 이동 페이지
                    .successHandler(customAuthenticationSuccessHandler)
                    .failureUrl("/login?error").permitAll() // 로그인 실패 후 이동 페이지
                    .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/test/logout"))
                .addLogoutHandler(new TaskImplementingLogoutHandler()).permitAll().logoutSuccessUrl("/main");
                    // 로그아웃 기본 url은 (/logout)
                    // 새로 설정하려면 .logoutUrl("url") 사용

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("admin").password(bCryptPasswordEncoder().encode("1234")).roles("ADMIN")
//                .and()
//                .withUser("guest").password(bCryptPasswordEncoder().encode("guest")).roles("GUEST");

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .rolePrefix("ROLE_")
                .usersByUsernameQuery("select member_id, replace(member_pw, '$2y', '$2a'), true from member where member_id = ?") // [사용자이름],[비밀번호],[Enabled]
                .authoritiesByUsernameQuery("select member_id, role from member where member_id = ?"); // 권한처리

    }




}
