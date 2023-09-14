package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.BoardDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;

@WebServlet("/board")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String action = request.getParameter("action");

		if("read".equals(action)) {
			System.out.println("[BoardController.read]");

			// 파라미터 값 추출
			int no = Integer.parseInt(request.getParameter("no"));

			// Dao 사용
			BoardDao boardDao = new BoardDao();

			// Dao 사용 --> 조회수 올리기
			boardDao.updateHit(no);

			// Dao 사용 --> 게시물 가져오기
			BoardVo boardVo = boardDao.getBoard(no);

			// 포워드 --> 데이터전달(요청문서의 바디(attributte))
			request.setAttribute("boardVo", boardVo);
			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");

		}else if("modifyForm".equals(action)) {
			System.out.println("[BoardController.modifyForm]");

			// 파라미터 값 추출
			int no = Integer.parseInt(request.getParameter("no"));

			// Dao 사용
			BoardDao boardDao = new BoardDao();
			BoardVo boardVo = boardDao.getBoard(no);

			// 포워드 --> 데이터전달(요청문서의 바디(attributte))
			request.setAttribute("boardVo", boardVo);
			WebUtil.forward(request, response, "/WEB-INF/views/board/modifyForm.jsp");

		}else if("modify".equals(action)) {
			System.out.println("[BoardController.modify]");

			// 파라미터 값 추출 --> vo만들기
			int no = Integer.parseInt(request.getParameter("no"));
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			BoardVo boardVo = new BoardVo(no, title, content);

			// Dao 사용
			BoardDao boardDao = new BoardDao();
			boardDao.modify(boardVo);

			// 리다이렉트
			WebUtil.redirect(request, response, "/mysite3/board");

		}else if("delete".equals(action)) {
			System.out.println("[BoardController.delete]");

			// 파라미터 값 추출
			int no = Integer.parseInt(request.getParameter("no"));

			// Dao 사용
			BoardDao boardDao = new BoardDao();
			boardDao.delete(no);

			// 리다이렉트
			WebUtil.redirect(request, response, "/mysite3/board");
			
		}else if("writeForm".equals(action)) {
			System.out.println("[BoardController.writeForm]");
			
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo) session.getAttribute("authUser");
			
			
			//글쓰기폼 요청시 로그인이 되어 있으면 글쓰기폼
			//                로그인이 되어 있지 않으면 로그인폼으로 이동
			if(authUser == null) { 
				WebUtil.redirect(request, response, "/mysite3/user?action=loginForm"	);
			} else {
				WebUtil.forward(request, response, "/WEB-INF/views/board/writeForm.jsp"	);
			}	
		}else if("write".equals(action)) {
			
			//파라미터 값 추출
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			//세션에서 로그인한 사용자 no값 추출
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo) session.getAttribute("authUser");
			
			//vo만들기
			BoardVo boardVo = new BoardVo();
			boardVo.setTitle(title);
			boardVo.setContent(content);
			boardVo.setUserNo(authUser.getNo());
			
			// Dao 사용
			BoardDao boardDao = new BoardDao();
			boardDao.boardWrite(boardVo);
			
			WebUtil.redirect(request, response, "/mysite3/board");
			
		}else { //action 파라미터가 없는 경우 리스트페이지가 출력되도록 했음
			System.out.println("[BoardController.list]");

			List<BoardVo> boardList;
			// 파라미터 값 추출
			String keyword = request.getParameter("keyword");
			System.out.println("--"+keyword+"--"); //공백"" 과 null 확인을 위해  앞뒤에 -- 붙임

			// Dao 사용
			BoardDao boardDao = new BoardDao();

			// Dao 사용-->글 가져오기
			boardList = boardDao.getBoardList(keyword);

			// Dao 사용-->글 갯수 가져오기
			int totalCount = boardDao.totalCount(keyword);

			// 포워드 --> 데이터전달(요청문서의 바디(attributte))
			request.setAttribute("boardList", boardList);
			request.setAttribute("totalCount", totalCount);

			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}