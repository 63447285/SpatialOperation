package com.example.spatialoperation;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 使用过滤器增加响应头（以 X-Content-Type-Options:nosniff 为例）
 * @description: AddResponseHeaderFilter
 * @author: 庄霸.liziye
 * @create: 2022-03-03 15:21
 **/
@Component
public class AddResponseHeaderFilter extends OncePerRequestFilter {
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}

