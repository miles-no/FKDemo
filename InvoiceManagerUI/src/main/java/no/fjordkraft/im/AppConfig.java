package no.fjordkraft.im;

import no.fjordkraft.security.filter.SecurityFilter;
import no.fjordkraft.security.springmvc.AuthorizationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by bhavi on 9/20/2017.
 */
@Configuration
public class AppConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private AuthorizationInterceptor authorizationInterceptor;

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor);
    }

    @Bean
    public FilterRegistrationBean myFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        SecurityFilter securityFilter = new SecurityFilter();
        beanFactory.autowireBean(securityFilter);
        registration.setFilter(securityFilter);
        registration.addUrlPatterns("/api/*");
        return registration;
    }
}
