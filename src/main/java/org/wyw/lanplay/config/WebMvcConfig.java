package org.wyw.lanplay.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.wyw.lanplay.interceptor.AuthorityInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private AuthorityInterceptor authorityInterceptor;

    public WebMvcConfig(AuthorityInterceptor authorityInterceptor) {
        this.authorityInterceptor = authorityInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorityInterceptor).addPathPatterns("/**");
    }

}
