package com.atguigu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity//开启security
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //设置响应头,开启security框架
        //只要是同源,可以显示html,直接放行
        http.headers().frameOptions().sameOrigin();
        //放行静态资源
        http.authorizeRequests().antMatchers("/static/**", "/login").permitAll().anyRequest().authenticated();
        //设置登录页面
        http.formLogin().loginPage("/login").defaultSuccessUrl("/");
        //设置登出,设置登录失败页面
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/login");
        //关闭防火墙
        http.csrf().disable();
        //配置自定义异常
        http.exceptionHandling().accessDeniedHandler(new CustomAccessDeineHandler());
    }

    /*@Override//AuthenticationManagerBuilder : 授权管理
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("zhangsan").password(new BCryptPasswordEncoder().encode("2233")).roles("");
    }*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
