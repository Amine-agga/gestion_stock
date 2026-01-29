package com.example.ms_user.security;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class GatewayHeaderFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String requestURI = httpRequest.getRequestURI();
        if(requestURI.equals("/api/auth/login") || requestURI.equals("/api/auth/register")){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        String userEmail = httpRequest.getHeader("X-User-Email");
        String userRole = httpRequest.getHeader("X-User-Role");
        if(userEmail == null || userRole == null){
            System.out.println("Acces direct refuse Headers Gateways manquants pour : "+requestURI);
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.getWriter().write("{\"error\":\"Direct access forbidden. Use Gateway.\"}");
            return;
        }
        System.out.println("Requete validee via gateway - User : "+userEmail+", Role : "+userRole);
        filterChain.doFilter(httpRequest,httpResponse);
    }
}
