package com.javaex.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
			
			//dao를 통해서 데이터 저장
			UserDao userDao = new UserDao();
			int count = userDao.userInsert(userVo);
			
			//가입성공 안내페이지 포워드
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/joinOK.jsp");
			rd.forward(request, response);
			
		}else if("loginForm".equals(action)) {
		
			//로그인폼
			System.out.println("action=loginForm");
			
			//로그인폼 포워드
			WebUtil.forward(request, response, "/WEB-INF/views/user/loginForm.jsp");
			
		}else if("login".equals(action)) {
			//로그인
			System.out.println("action=login");
			
			//파라미터 꺼내기
			String id = request.getParameter("id");
			String password = request.getParameter("pw");
			
			//vo로 묶기
			UserVo userVo = new UserVo();
			userVo.setId(id);
			userVo.setPassword(password);
			
			//Dao를 통해 로그인한 사용자가 있는지 확인한다
			UserDao userDao = new UserDao();
			UserVo authUser = userDao.userSelect(userVo); //id pw

			/*
			System.out.println(authUser);
			authUser null이면 --> 로그인실패
			authUser null이 아니면 --> 로그인성공
			*/
			
			if(authUser != null) { //로그인 성공
				//세션에 값 넣기 (로그인)
				HttpSession session = request.getSession();
				session.setAttribute("authUser", authUser);

				//메인으로 리다이렉트
				WebUtil.redirect(request, response, "/mysite3/main");
			
			}else {
				//로그폼 으로 리다이렉트
				WebUtil.redirect(request, response, "/mysite3/user?action=loginForm&result=fail");
			}
			
		}else if("logout".equals(action)) {
			//로그아웃
			System.out.println("action=logout");
			
			//세션의 모든 값을 지운다
			HttpSession session = request.getSession();
			session.invalidate();
			
			//메인으로 리다이렉트
			WebUtil.redirect(request, response, "/mysite3/main");
		
		}else if("modifyForm".equals(action)) {
			//수정폼
			System.out.println("action=modifyForm");
			
			//로그인한 사용자의 세션 주소를 받는다
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			
			//로그인한 사용자의 no를 구한다
			int no = authUser.getNo();
			
			//dao를 통해 데이터 가져오기 (로그인한 사용자의 번호로 정보를 가져온다)
			UserDao userDao = new UserDao();
			UserVo userVo = userDao.userSelect(no);
			
			//request의 어트리뷰트영역에 userVo를 넣고 포워드
			request.setAttribute("userVo", userVo);
			WebUtil.forward(request, response, "/WEB-INF/views/user/modifyForm.jsp");
		
		}else if("modify".equals(action)) {
			//수정
			System.out.println("action=modify");
			
			//로그인한 사용자의 세션 주소를 받는다
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			
			//로그인한 사용자의 no를 구한다
			int no = authUser.getNo();
			
			//파라미터에서 나머지 정보 가져오기
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");
			
			//vo로 묶기
			UserVo userVo = new UserVo();
			userVo.setNo(no);
			userVo.setPassword(password);
			userVo.setName(name);
			userVo.setGender(gender);
			
			//db수정
			UserDao userDao = new UserDao();
			int count = userDao.userUpdate(userVo);
			
			//세션수정
			authUser.setName(name);
			
			//리다이렉트 main
			WebUtil.redirect(request, response, "/mysite3/main");
		}
		
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
