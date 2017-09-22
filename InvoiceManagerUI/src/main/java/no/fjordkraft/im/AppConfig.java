package no.fjordkraft.im;

import no.fjordkraft.security.filter.SecurityFilter;
import no.fjordkraft.security.jpa.repository.UserRolesRepository;
import no.fjordkraft.security.springmvc.AuthorizationInterceptor;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;

/**
 * Created by bhavi on 9/20/2017.
 */
@Configuration
public class AppConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private UserRolesRepository userRolesRepository;

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Autowired
    private Environment environment;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*if(Arrays.stream(environment.getActiveProfiles()).noneMatch(s -> s.equals("dev"))) {
            registry.addInterceptor(new AuthorizationInterceptor("no.fjordkraft", userRolesRepository));
        }*/
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
