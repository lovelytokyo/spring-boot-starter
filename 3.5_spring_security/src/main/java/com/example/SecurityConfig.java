package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebMvcSecurity // JavaConfigクラスに@EnableWebMvcSecurity付けることでSpring Securityの基本的な設定が行われる
public class SecurityConfig extends WebSecurityConfigurerAdapter  {
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/webjars/**", "/css/**");
    }

    @Override // 認可の設定やログイン・ログアウトに関する設定ができる
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/loginForm").permitAll() // 任意のユーザがアクセスできつようにする
                .anyRequest().authenticated();

        // ログインに関する設定
        http.formLogin() // フォーム認証を有効化
                .loginProcessingUrl("/login") // 認証処理パス
                .loginPage("/loginForm") // ログインフォーム表示ページ
                .failureUrl("/loginForm?error") // 認証失敗時遷移URL
                .defaultSuccessUrl("/customers", true) // 認証成功時遷移先
                .usernameParameter("username").passwordParameter("password") // ユーザ名・パスワードのパラメータ名
                .and();

        // ログアウトに関する設定
        // AntPathRequestMatcherを使わず文字列のパス指定の場合は、POSTでアクセスする必要がある
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout**")) // ログアウト処理のパス
                .logoutSuccessUrl("loginForm"); // ログアウト完了時遷移先
    }

    @Configuration
    static class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter { // 認証に関わる設定
        @Autowired
        UserDetailsService userDetailsService;

        @Bean
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder());
        }
    }
}
