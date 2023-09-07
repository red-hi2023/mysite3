package com.javaex.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.UserDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.UserVo;

@WebServlet("/user")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//업무구분용 파라미터 체크
		String action = request.getParameter("action");
		
		if("joinForm".equals(action)) {
			
			//회원가입폼
			System.out.println("action=joinForm");
			
			//포워드
			/*
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/joinForm.jsp");
			rd.forward(request, response);
			*/
			
			//포워드를 util 사용
			WebUtil.forward(request, response, "/WEB-INF/views/user/joinForm.jsp");
			
			
			/*
			 * 메모
			WebUtil webutil = new WebUtil();
			webutil.forward(request, response, "/WEB-INF/views/user/joinForm.jsp");
			 
			WebUtil webutil = new WebUtil();
			webutil.redirect(request, response, "/phonebook3/PhonebookController?action=list"); 
			 
			WebUtil.forward(request, response, "/WEB-INF/views/user/joinForm.jsp");
			WebUtil.redirect(request, response, "/phonebook3/PhonebookController?action=list")
			*/
					
			
		}else if("join".equals(action)) {
		
			//회원가입
			System.out.println("action=join");
			
			//파라미터 꺼내기
			String id = request.getParameter("id");
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String gender = request.getParameter("gender");
			 
			//1개로 묶기
			UserVo userVo = new UserVo(id, name, password, gender);
			System.out.println(userVo);
			
			//dao를 통해서 데이터 저장
			UserDao userDao = new UserDao();
			int count = userDao.userInsert(userVo);
			System.out.println(count);
			
			//가입성공 안내페이지 포워드
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/joinOK.jsp");
			rd.forward(request, response);
			
		}
		
		
		
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
