package no.fjordkraft.im.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import no.fjordkraft.security.jpa.domain.User;
import no.fjordkraft.security.services.UserRolesService;
import no.fjordkraft.security.services.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.newrelic.api.agent.NewRelic;

@Component(value = "requestLoggingFilter")
public class RequestLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());
        logger.debug("requested path ::"+ path);

        if(path.contains("layout")){
            Enumeration headerNames = req.getHeaderNames();
            while(headerNames.hasMoreElements()) {
                String headerName = (String)headerNames.nextElement();
                logger.debug("" + headerName);
                logger.debug("" + req.getHeader(headerName));
            }
        }

        Enumeration<String> params = request.getParameterNames();
        while(params.hasMoreElements()){
            String paramName = params.nextElement();
            logger.debug("Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

}
