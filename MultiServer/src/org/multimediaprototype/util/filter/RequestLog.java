package org.multimediaprototype.util.filter;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by dx.yang on 15/11/27.
 */
public class RequestLog implements Filter {

    private static final String URL_FILTER_PARAM = "url-filter";

    private Logger logger;
    private String urlFilter;

    @Override
    public void init(FilterConfig config) throws ServletException {
        logger = LogManager.getLogger(RequestLog.class);
        urlFilter = config.getInitParameter(URL_FILTER_PARAM);

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        long startTime;
        long endTime;

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 获取访问路径
        String path = httpRequest.getServletPath();

        // 获取当前的spring.profile
        String activeProfile = httpRequest.getServletContext().getInitParameter("spring.profiles.active");

        if (activeProfile.equals("dev") || urlFilter == null || path.matches(urlFilter)) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            String username = (auth != null) ? auth.getName() : "null";

            String method = httpRequest.getMethod();
            String addr = request.getRemoteAddr();
            String url = httpRequest.getRequestURL().toString();


            startTime = System.currentTimeMillis();
            chain.doFilter(request, response);
            endTime = System.currentTimeMillis();

            logger.info(
                    "from " + username + " - " + addr +
                    " | " + method + " " + url + " | " + (endTime - startTime) + " ms"
            );
        }
        else {
            chain.doFilter(request, response);
        }

    }

    @Override
    public void destroy() {

    }
}
