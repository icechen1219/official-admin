///:CORSConfiguration.java
package com.erhui.official.common;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 常规跨域请求配置类。如果有特殊要求需要定制，需要用CrosFilter来解决。
 *
 * @author icechen1219
 * @date 2019/02/04
 */
//@Configuration
public class CORSConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/api/**")
//                .allowedOrigins("http://localhost:63343")
//                //允许跨域请求中携带cookie
//                .allowCredentials(true)
//                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS")
//                .allowedHeaders("authorization", "Origin", "No-Cache", "X-Requested-With", "Content-Type")
//                .allowedHeaders("If-Modified-Since", "Last-Modified", "Cache-Control", "Expires", "X-E4M-With")
//                .maxAge(3600);
    }
}
///:CORSConfiguration.java
