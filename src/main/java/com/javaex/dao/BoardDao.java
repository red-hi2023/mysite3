package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.BoardVo;

public class BoardDao {

	// 0. import java.sql.*;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";

	// connection 얻어오기 메소드
	private void getConnection() {
		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			Class.forName(driver);

			// 2. Connection 얻어오기
			conn = DriverManager.getConnection(url, id, pw);
			// System.out.println("접속성공");

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	// 자원정리 메소드
	private void close() {
		// 5. 자원정리
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	// 게시판 전체글 가져오기 메소드
	public List<BoardVo> getBoardList(String keyword) {
		List<BoardVo> bList = new ArrayList<>();

		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " SELECT BO.no ";
			query += "      , BO.title ";
			query += "      , BO.content ";
			query += "      , US.name ";
			query += "      , BO.hit ";
			query += "      , TO_CHAR(BO.reg_date, 'YYYY-MM-DD HH:MI') regDate ";
			query += "		, US.no userNo ";
			query += " FROM board BO, users US ";
			query += " WHERE  BO.user_no = US.no ";
			
			if (keyword == null || keyword.equals("")) { // 키워드가 없을때 
				query += " order by reg_date desc  ";
				pstmt = conn.prepareStatement(query);

			} else { // 키워드가 있을때
				query += " and  BO.title like ? ";
				query += " order by reg_date desc  ";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, '%' + keyword + '%');
			}

			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String title = rs.getString("title");
				String content = rs.getString("content");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("regDate");
				int userNo = rs.getInt("userNo");
				String userName = rs.getString("name");

				BoardVo vo = new BoardVo(no, title, content, hit, regDate, userNo, userName);
				bList.add(vo);
			}

			System.out.println("boardDao.getList(): " + bList.toString());

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

		return bList;
	}

	// 게시판 글쓰기 메소드
	public int boardWrite(BoardVo vo) {
		int count = 0;
		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " INSERT INTO board ";
			query += " VALUES (seq_board_no.NEXTVAL, ?, ?, 0, SYSDATE, ?) ";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setInt(3, vo.getUserNo());

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println("boardDao.write(): " + count + "건 게시판 글 저장");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

		return count;
	}

	// 게시판 글 1개 가져오기 메소드
	public BoardVo getBoard(int no) {
		BoardVo boardVo = null;

		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " SELECT BO.no ";
			query += "      , BO.title ";
			query += "      , BO.content ";
			query += "      , US.name ";
			query += "      , BO.hit ";
			query += "      , TO_CHAR(BO.reg_date, 'YYYY-MM-DD HH:MI') regDate ";
			query += "      , US.no userNo";
			query += " FROM board BO, users US ";
			query += " WHERE  BO.user_no = US.no ";
			query += "  AND	BO.no = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);

			// 4.결과처리
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int rno = rs.getInt("no");
				String title = rs.getString("title");
				String content = rs.getString("content");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("regDate");
				int userNo = rs.getInt("userNo");
				String userName = rs.getString("name");

				boardVo = new BoardVo(rno, title, content, hit, regDate, userNo, userName);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return boardVo;
	}

	
	// 게시판글 hit수 1개 올리기 메소드
	public int updateHit(int no) {
		int count = 0;

		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " UPDATE board ";
			query += " SET hit = hit + 1 ";
			query += " WHERE no = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println("boardDao.updateHit(): " + no + " 글 hit 증가");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

		return count;
	}
	
	
	// 게시판글 삭제 메소드
	public int delete(int no) {
		int count = 0;

		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " DELETE board ";
			query += " WHERE no = ? ";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println("boardDao.delete(): " + count + " 건 삭제");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

		return count;
	}

	// 게시판글 수정 메소드
	public int modify(BoardVo vo) {
		int count = 0;

		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " UPDATE board ";
			query += " SET 	title = ? ";
			query += "	  , content = ? ";
			query += " WHERE no = ? ";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setInt(3, vo.getNo());

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println("boardDao.modify(): " + count + " 건 수정");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

		return count;

	}

	// 게시글 전체 개수 구하는 메소드
	public int totalCount(String keyword) {
		int totalCount = 0;

		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " SELECT count(*) totalCount ";
			query += " FROM board ";
		
			if (keyword == null || keyword == "") { // 키워드가 없을때
				pstmt = conn.prepareStatement(query);

			} else { // 키워드가 있을때
				query += " where title like ? ";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, '%' + keyword + '%');
			}

			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				totalCount = rs.getInt("totalCount");
			}


		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

		return totalCount;

	}

}