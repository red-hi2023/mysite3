package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.GuestbookDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.GuestbookVo;

@WebServlet("/gbc")
public class GuestbookController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 파라미터 action의 값을 꺼내온다 업무구분욜
		String action = request.getParameter("action");

		if ("addList".equals(action)) {
			System.out.println("action=addList");

			// 등록폼+리스트/////////////////////////////////////////////////////////////////////
			// 1.dao를 통해서 전체 리스트데이터 가져오기
			GuestbookDao guestbookDao = new GuestbookDao();
			List<GuestbookVo> guestbookList = guestbookDao.guestbookSelect();

			// System.out.println(guestbookList);

			// 리스트 화면(html) + data 응답을 해야된다
			// request data를 넣어둔다
			request.setAttribute("guestbookList", guestbookList);

			// addList.jsp 에게 시킨다 (포워드)
			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/addList.jsp");
			
			//RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/guestbook/addList.jsp"); // jsp파일 위치를
			//rd.forward(request, response);
			
			//////////////////////////////////////////////////////////////////////////////
		} else if ("add".equals(action)) {
			System.out.println("action=add");

			// 등록///////////////////////////////////////////////////////

			// 파라미터 꺼내기
			String name = request.getParameter("name");
			String password = request.getParameter("pass");
			String content = request.getParameter("content");

			// vo로 묶기
			GuestbookVo guestbookVo = new GuestbookVo(name, password, content);

			// dao를 통해서 저장하기
			GuestbookDao guestbookDao = new GuestbookDao();
			guestbookDao.guestbookInsert(guestbookVo);

			// 리스트화면 출력하기 --> 리다이렉트
			WebUtil.redirect(request, response, "/mysite3/gbc?action=addList");

			//response.sendRedirect("/mysite3/gbc?action=addList");

		} else if ("deleteForm".equals(action)) {
			System.out.println("action=deleteForm");

			// 삭제폼일때

			// no 값은 deleteForm.jsp에서 request.getParameter("no")로 꺼내쓴다

			// deleteForm.jsp 에게 시킨다 (포워드)
			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/deleteForm.jsp");
			
			//RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/guestbook/deleteForm.jsp"); // jsp파일 위치를
			//rd.forward(request, response);

		} else if ("delete".equals(action)) {
			System.out.println("action=delete");
			// 삭제일때

			// 파라미터 꺼내기
			int no = Integer.parseInt(request.getParameter("no"));
			String password = request.getParameter("pass");

			// vo 로 1개로 묶기
			GuestbookVo guestbookVo = new GuestbookVo();
			guestbookVo.setNo(no);
			guestbookVo.setPassword(password);

			// dao를 통해서 삭제하기
			GuestbookDao guestbookDao = new GuestbookDao();
			guestbookDao.guestbookDelete(guestbookVo);

			// 리스트 출력하기 -->포워드
			WebUtil.redirect(request, response, "/mysite3/gbc?action=addList");
			
			//response.sendRedirect("/mysite3/gbc?action=addList");

		} else {
			System.out.println("나머지");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
