package banking.jdbc;

import java.sql.*;
import java.util.Scanner;

public class BankingSystem_JDBC {
	static Scanner scan = new Scanner(System.in);
	
	public static void main(String[] args) {
		Connection conn; //DB연결
		ResultSet rs; //select의 실행결과 반환
		PreparedStatement psmt; //동적 쿼리문 실행
		int result;
		
		try {
			//오라클 드라이버 로드
			Class.forName("oracle.jdbc.OracleDriver");
			//커넥션URL과 계정 아이디와 비번
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			String id = "banking";
			String pass = "1234";
			//연결에 성공하면 Connection 인스턴스를 반환한다.
			conn = DriverManager.getConnection(url, id, pass);
			
			System.out.println("계좌번호: ");
			int acc_num = Integer.parseInt(scan.nextLine());
			System.out.println("이름: ");
			String name = scan.nextLine();
			System.out.println("잔액: ");
			int balance = Integer.parseInt(scan.nextLine());
			System.out.println("이자율: ");
			int interest_rate = Integer.parseInt(scan.nextLine());
			
			String sql = "insert into banking" + " values "
					+ " (seq_banking_idx.nextval, ?, ?, ?, ?)";
			
			// setInt, setDate, setString
			psmt = conn.prepareStatement(sql);
			//쿼리문의 인파라미터를 입력값을 통해 설정함.
			psmt.setInt(1, acc_num);
			psmt.setString(2, name);
			psmt.setInt(3, balance);
			psmt.setInt(4, interest_rate);
			//쿼리문 실행 및 결과 반환
			result = psmt.executeUpdate();
			System.out.println("[psmt]"+ result + "행 입력됨");
			
		} catch (SQLException e) {
			System.out.println("SQL 오류 발생");
		} 
	}
}
