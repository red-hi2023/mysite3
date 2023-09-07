package com.javaex.util;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebUtil {
	
	//필드 X
	
	//생성자 디폴트 생성자 사용 -->생략
	
	//메소드 gs  필드 없어서X
	
	//메소드 일반
	public static void forward(HttpServletRequest request, 
			            HttpServletResponse response,
			            String path) throws ServletException, IOException {
		
		RequestDispatcher rd = request.getRequestDispatcher(path);
		rd.forward(request, response);
		
	}
	
	public static void redirect(HttpServletRequest request,
			             HttpServletResponse response,
			             String url) throws IOException {
		response.sendRedirect(url);
		
	}
}
