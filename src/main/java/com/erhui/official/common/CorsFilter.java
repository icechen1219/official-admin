///:CorsFilter.java
package com.erhui.official.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @author Bailin
 * <p>
 * Cors Cross domain access filter
 * <p>
 * Generate access control information based by Http request header
 * <p>
 * ================================================================
 * <p>
 * Request Headers：
 * <p>
 * OPTIONS /rules HTTP/1.1
 * Host: 192.168.1.155:1024
 * Connection: keep-alive
 * Access-Control-Request-Method: PUT
 * Origin: http://192.168.1.155:8080
 * User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36
 * Access-Control-Request-Headers: content-type,token
 * Accept: * / *
 * Accept-Encoding:gzip,deflate
 * Accept-Language:zh-CN,zh;q=0.9
 * <p>
 * ================================================================
 * <p>
 * Generate Response Headers:
 * <p>
 * HTTP/1.1 200 OK
 * Date: Wed, 07 Nov 2018 16:40:22 GMT
 * Access-Control-Allow-Origin: http://192.168.1.155:8080
 * Access-Control-Allow-Methods: PUT
 * Access-Control-Allow-Headers: content-type,token, Origin, Access-Control-Request-Method, Accept, Access-Control-Request-Headers, Connection, User-Agent, Host, Accept-Encoding, Accept-Language
 * Access-Control-Max-Age: 7200
 * Content-Type: application/json;charset=utf-8
 * Transfer-Encoding: chunked
 */
@Component
@WebFilter
@Order(Integer.MIN_VALUE)
public class CorsFilter implements Filter {
    private static final String PARAMS_SEPARATE = ", ";
    private static Logger logger = LoggerFactory.getLogger(CorsFilter.class);
    @Value("${cors.access.control.maxAge:3600}")
    String corsAccessControlMaxAge;

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = ((HttpServletResponse) servletResponse);

        String originDomain = httpServletRequest.getHeader(HttpHeaders.ORIGIN);
        logger.debug("filter cors request from: {}", originDomain);

        if(originDomain!=null) {
            httpServletResponse.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, originDomain);
        }
        httpServletResponse.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, getHeaders(httpServletRequest));
        httpServletResponse.addHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, corsAccessControlMaxAge);
        // Access-Control-Allow-Credentials:true 允许跨域请求中携带cookie
        httpServletResponse.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, Boolean.TRUE.toString());

        if (HttpMethod.OPTIONS.name().equalsIgnoreCase(httpServletRequest.getMethod())) {
            httpServletResponse.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, httpServletRequest.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD));
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());

        } else {
            httpServletResponse.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, httpServletRequest.getMethod());
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

    private String getHeaders(HttpServletRequest httpServletRequest) {
        StringBuilder params = new StringBuilder();
        String accessControlRequestHeaders = httpServletRequest.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS);
        if (!(accessControlRequestHeaders == null || accessControlRequestHeaders.isEmpty())) {
            params.append(accessControlRequestHeaders).append(PARAMS_SEPARATE);
        }

        Enumeration<String> names = httpServletRequest.getHeaderNames();
        while (names.hasMoreElements()) {
            params.append(names.nextElement()).append(PARAMS_SEPARATE);
        }
        params.setLength(params.length() - PARAMS_SEPARATE.length());
        return params.toString();
    }

    @Override
    public void destroy() {

    }
}
///:CorsFilter.java
