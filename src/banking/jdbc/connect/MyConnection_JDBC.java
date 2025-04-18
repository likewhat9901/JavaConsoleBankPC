package banking.jdbc.connect;

import java.sql.*;
import java.util.Scanner;

public abstract class MyConnection_JDBC{
	//멤버상수 : 오라클다라이버, 커넥션URL 선언
	//전역상수로 선언 -> 불변, 클래스 전체 공유
	public static final String ORACLE_DRIVER = "oracle.jdbc.OracleDriver";
	public static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1521:xe";
	
	//멤버변수
	protected Connection con; //DB연결
	protected ResultSet rs; //select의 실행결과 반환
	protected Statement stmt; //정적 쿼리문 실행
	protected PreparedStatement psmt; //동적 쿼리문 실행
	protected CallableStatement csmt; //프로시저 실행
	
	//생성자. 매개변수로 오라클 계정의 아이디, 비번을 전달받음
	public MyConnection_JDBC(String user, String pass) {
		try {
			//오라클 드라이버를 메모리에 로드(인터페이스 상수 사용)
			Class.forName(ORACLE_DRIVER);
			//오라클 연결
			con = DriverManager.getConnection(ORACLE_URL, user, pass);
			
			//오라클 연결여부 확인
			if (con!=null) {
				System.out.println("Oracle 연결성공");
			} else {
				System.out.println("Oracle 연결실패");
			}
		} catch (SQLException e) {
			System.out.println("SQL 예외발생");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Class 예외발생");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("미확인 예외발생");
			e.printStackTrace();
		}
	}
	
	/*
	 * IConnect 인터페이스를 구현했으므로 자식 클래스에서는 반드시
	 * 부모의 추상메서드를 오버라이딩해서 재정의해야 한다. 그렇지 않으면
	 * 에러가 발생된다.
	 */
	
	//오버라이딩의 목적으로 생성되어 실행부는 없다.
	public void dbExecute() {
		/* CRUD(DB의 기본적인 4가지 작업)는 자식 클래스에
		 * 처리되어야 하므로 부모클래스에서는 실행부를 정의할 수 없다.
		 * 따라서 실행부가 없는 메서드로 정의되었다. */
	}
	
	// JDBC작업을 위해 연결된 모든 자원을 해제
	public void dbClose() {
		try {
			//연결되어 사용중이라면 자원을 반납한다.
			if(con!=null) con.close();
			if(rs!=null) rs.close();
			if(stmt!=null) stmt.close();
			if(psmt!=null) psmt.close();
			if(csmt!=null) csmt.close();
			System.out.println("DB 자원 반납");
		} catch (Exception e) {
			System.out.println("DB 자원 반납시 예외발생");
			e.printStackTrace();
		}
	}
	
	//사용자로부터 입력을 받기 위해 정의
	public String inputValue(String title) {
		Scanner scan = new Scanner(System.in);
		System.out.print(title + "을(를) 입력(exit -> 종료):");
		String inputStr = scan.nextLine();
		/*
		 * equalsIgnoreCase() : equals()와 동일하게 문자열이 동일한지
		 * 비교하는 메서드로 대소문자를 구분하지 않고 비교한다.
		 * 즉, EXIT와 exit를 같은 문자열로 판단한다.
		 */
		if ("EXIT".equalsIgnoreCase(inputStr)) {
			System.out.println("프로그램을 종료합니다.");
			//자원반납
			dbClose();
			//프로그램 자체를 종료시킨다.
			System.exit(0);
		}
		//종료가 아니라면 입력한 값을 반환한다.
		return inputStr;
	}
	
	
	public static void main(String[] args) {
		
	}
}
