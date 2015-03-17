package com.cloud.ocs.portal.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.cloud.ocs.portal.common.bean.Employee;
import com.cloud.ocs.portal.core.auth.constant.LoginUserConstant;

public class LoginFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

        
        String url = request.getServletPath();  
        
        if ( url.equals("") ) 
        	url += "/";  
        if ( !url.equals("/") && !url.equals("/login") && !url.startsWith("/front") ) {
        	Employee user=(Employee)request.getSession().getAttribute(LoginUserConstant.LOGIN_USER_SESSTION_ATTRIBUTE_NAME);  
            if(user == null){
            	response.sendRedirect("login"); 
                return;          
            }        
        }  
        filterChain.doFilter(request, response);  
	}
}
