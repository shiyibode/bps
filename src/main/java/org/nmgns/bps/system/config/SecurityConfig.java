package org.nmgns.bps.system.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.nmgns.bps.system.utils.base.ResponseJson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity(prePostEnabled = true) //启用@PreAuthorize注解须配置该项
public class SecurityConfig {

    @Value("${reverse-proxy-address}")
    private String reverseProxyAddress;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf
                .ignoringRequestMatchers("/*")
                .disable()
        );

        String url;
        if (StrUtil.isBlank(reverseProxyAddress)) url = "";
        else url = "/" + reverseProxyAddress;

        http.formLogin(form -> form
                .loginProcessingUrl("/login").permitAll()
                .defaultSuccessUrl(url + "/loginsuccess").permitAll()
                .failureForwardUrl(url + "/loginfailure").permitAll()
        );

        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/login").permitAll()
                .requestMatchers("/loginsuccess").permitAll()
                .requestMatchers("/loginfailure").permitAll()
                .requestMatchers("/logoutsuccess").permitAll()
                .requestMatchers("/test").permitAll()
                .anyRequest().authenticated()
        );

        http.logout(logout -> logout
                        .logoutUrl("/logout")
//                .logoutSuccessUrl("/logoutsuccess")
                        .permitAll()
//                        .addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(COOKIES)))
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            if (authentication != null){    //authentication=null表示用户由于超时已经退出了登录
                                //在这里记录登出的用户、登出的时间
                                String username = authentication.getName();
                                System.out.println("logout success handler -- " + username);
                                ResponseJson responseJson = new ResponseJson();
                                responseJson.setSuccess(true);
                                response.getWriter().write(JSONUtil.toJsonStr(responseJson));
                            }
                        })
        );

        // 未登录时返回json
        http.exceptionHandling(except -> except
                .authenticationEntryPoint(myAuthenticationEntryPoint())
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    //在这里返回用户访问资源被拒绝时的json字符串
                    response.getWriter().write("access denied");
                }));

        //限制用户可以登录的终端数
        http.sessionManagement(session -> session
                .maximumSessions(3)
        );

        //Spring Security默认是将’X-Frame-Options’ 设置为 ‘DENY’，X-Frame-Options HTTP 响应头是用来给浏览器指示允许一个页面可否在frame中展现的标记。
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    /**
     * 密码控件
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 自定义未登录时返回json
     * @return
     */
    @Bean
    public AuthenticationEntryPoint myAuthenticationEntryPoint() {
        return new MyAuthenticationEntryPoint();
    }

    /**
     * 用来配合上面的sessionManager来控制同时在线用户的个数（同一客户）
     * @return
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    /**
     * 认证之后会发送的事件，可以在监听器（AuthenticationEvents类）中配置认证成功或失败后的处理代码
     * @param applicationEventPublisher
     * @return
     */
    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher
    (ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
    }

    /**
     * 角色的默认前缀
     * @return
     */
//    @Bean
//    static GrantedAuthorityDefaults grantedAuthorityDefaults() {
//        return new GrantedAuthorityDefaults("MYPREFIX_");
//    }
}
