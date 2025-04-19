package banking.jdbc;

import java.util.Scanner;
import java.sql.*;

import banking.MenuSelectException;
import banking.jdbc.connect.MyConnection_JDBC;

public class AccountManager_JDBC extends MyConnection_JDBC implements ICustomDefine_JDBC{
	private String user;
	private String pass;
	
	public AccountManager_JDBC(String user, String pass) {
		super(user, pass);
		this.user = user;
		this.pass = pass;
	}
	
	private static Scanner scan = new Scanner(System.in);
	
	
	void showMenu() {
		System.out.println("\n-----Menu------");
		System.out.println("1.계좌개설");
		System.out.println("2.입 금");
		System.out.println("3.출 금");
		System.out.println("4.전체계좌정보출력");
		System.out.println("5.지정계좌정보출력");
		System.out.println("6.계좌삭제");
		System.out.println("0.프로그램종료");
		System.out.print("선택: ");
		
		try {
			//메뉴 선택
			int choice = Integer.parseInt(scan.nextLine());
			
			//메뉴 숫자가 아닌 숫자 입력시 사용자예외처리
			if(choice < 0 || choice > DELETE) {
				throw new MenuSelectException("0~"+ DELETE + " 사이의 숫자를 입력하세요.");
			}
			
			//메뉴선택에 따른 기능 실행
			switch (choice)	{
			case MAKE: makeAccount(); break;	
			case DEPOSIT: depositMoney(); break;
			case WITHDRAW: withdrawMoney(); break;
			case INQUIRE: showAllInfo(); break;
			case INQUIRE_SPECIFIC: showSpecificInfo(); break;
			case DELETE: deleteAcc(); break;
			case EXIT: exitProgram(); break;
			}	
		} catch (NumberFormatException e) {
			System.out.println("문자 또는 잘못된 입력입니다. 숫자를 입력하세요.\n");
		} catch (MenuSelectException e) {
			System.out.println("[예외발생] "+ e.getMessage());
		} 
	}
	
	void makeAccount() {
		try {
			System.out.print("계좌번호: ");
			int acc_num = Integer.parseInt(scan.nextLine());
			System.out.print("이름: ");
			String name = scan.nextLine();
			System.out.print("잔액: ");
			int balance = Integer.parseInt(scan.nextLine());
			System.out.print("이자율: ");
			int interest_rate = Integer.parseInt(scan.nextLine());
			
			String sql = "INSERT INTO banking" + " VALUES "
					+ " (seq_banking_idx.nextval, ?, ?, ?, ?)";
			
			// setInt, setDate, setString
			psmt = con.prepareStatement(sql);
			
			//쿼리문의 인파라미터를 입력값을 통해 설정함.
			psmt.setInt(1, acc_num);
			psmt.setString(2, name);
			psmt.setInt(3, balance);
			psmt.setInt(4, interest_rate);
			
			//쿼리문 실행 및 결과 반환
			int result = psmt.executeUpdate();
			System.out.println("[psmt]"+ result + "행 입력됨");
			
		} catch (SQLException e) {
			System.out.println("SQL 예외 발생");
		} 
	}
	
	void depositMoney() {
		try {
			con.setAutoCommit(false);
			
			System.out.print("계좌번호: ");
			int acc_num = Integer.parseInt(scan.nextLine().trim());
			
			//잔액,이자율 rs로 가져오기
			String sql_balance = "SELECT balance, interest_rate FROM banking "
					+ " WHERE acc_id = ?";
			psmt = con.prepareStatement(sql_balance);
			psmt.setInt(1, acc_num);
			rs = psmt.executeQuery();
			
			if(!rs.next()) {
				System.out.println("해당 계좌가 존재하지 않습니다.");
				return;
			}
			
			//잔액,이자율
			int balance = rs.getInt("balance");
			int interest_rate = rs.getInt("interest_rate");
			
			System.out.println("잔액: "+ balance + "원");
			System.out.print("입금액: ");
			int deposit = Integer.parseInt(scan.nextLine().trim());

			//이자액 계산
			int interest = deposit * interest_rate / 100;
			int new_balance = balance + deposit + interest;
			
			//잔액 업데이트
			String sql_update = "UPDATE banking SET balance = ? "
					+ " WHERE acc_id = ?";
			psmt = con.prepareStatement(sql_update);
			psmt.setInt(1, new_balance);
			psmt.setInt(2, acc_num);
			
			//쿼리문 실행 및 결과 반환
			int updated = psmt.executeUpdate();
			
			if(updated > 0) {
				con.commit();
				System.out.println("입금완료!");
				System.out.printf("잔액: %d (이자: %d)%n", new_balance, interest);
			} else {
				con.rollback();
				System.out.println("입금 실패");
				return;
			}
			
			System.out.println("[psmt]"+ updated + "행 업데이트 됨");
			
		} catch (SQLException e) {
			try {
				con.rollback();
				System.out.println("SQL 예외 발생" + e.getMessage());
			} catch (SQLException e2) {
				System.out.println("롤백 예외 발생" + e2.getMessage());
			}
		} catch (NumberFormatException e) {
			System.out.println("숫자가 아닌 형식이 입력되었습니다." + e.getMessage());
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
	}
	
	void withdrawMoney() {
		try {
			System.out.print("계좌번호: ");
			int acc_num = Integer.parseInt(scan.nextLine().trim());
			
			//잔액,이자율 rs로 가져오기
			String sql_balance = "SELECT balance, interest_rate"
					+ " FROM banking "
					+ " WHERE acc_id = ?";
			psmt = con.prepareStatement(sql_balance);
			psmt.setInt(1, acc_num);
			rs = psmt.executeQuery();
			
			if(!rs.next()) {
				System.out.println("해당 계좌가 존재하지 않습니다.");
				return;
			}
			
			int balance = rs.getInt("balance");
			System.out.println("잔액: "+ balance + "원");
			
			int withdraw = 0;
			while (true) {
				System.out.print("출금액: ");
				withdraw = Integer.parseInt(scan.nextLine());
				
				if(withdraw <= 0) {
					System.out.println("0보다 큰 금액을 입력하세요");
					continue;
				} else if(withdraw > balance) {
					System.out.println("잔액이 부족합니다.");
					continue;
				} else {
					break;
				}
			}
			int new_balance = balance - withdraw;
			
			//잔액 업데이트
			String sql_update = "UPDATE banking" + " SET balance = ? "
					+ " WHERE acc_id = ?";
			psmt = con.prepareStatement(sql_update);
			psmt.setInt(1, new_balance);
			psmt.setInt(2, acc_num);
			
			//쿼리문 실행 및 결과 반환
			int updated = psmt.executeUpdate();
			
			if(updated > 0) {
				System.out.println("출금완료!");
				System.out.printf("잔액: %d원%n", new_balance);
			} else {
				System.out.println("출금 실패");
				return;
			}
			
			System.out.println("[psmt]"+ updated + "행 업데이트 됨");
			
		} catch (SQLException e) {
			System.out.println("SQL 예외 발생" + e.getMessage());
		} catch (NumberFormatException e) {
			System.out.println("입력값이 숫자가 아닙니다." + e.getMessage());
		} finally {
			
		}
	}
	
	//전체계좌정보출력
	void showAllInfo() {
		try {
			String sql_accInfo = "SELECT serial_num, acc_id, name, balance, interest_rate"
					+ " FROM banking "
					+ " ORDER BY serial_num";
			psmt = con.prepareStatement(sql_accInfo);
			rs = psmt.executeQuery();
			
			int serial_num, acc_id, balance, interest_rate;
			String name;
			
			while(true) {
				if(rs.next()) {
					serial_num = rs.getInt("serial_num");
					acc_id = rs.getInt("acc_id");
					name = rs.getString("name");
					balance = rs.getInt("balance");
					interest_rate = rs.getInt("interest_rate");
					
					System.out.printf("[%d] 계좌번호: %d, 계좌주: %s, 잔액: %d, 이자율: %d%n", 
							serial_num, acc_id, name, balance, interest_rate);
					
				} else {
					break;
				}
			}
			int updated = psmt.executeUpdate();
			System.out.println("[psmt]"+ updated + "행 출력됨");
			
		} catch (SQLException e) {
			System.out.println("SQL 예외 발생" + e.getMessage());
		}
	}
	
	//지정계좌정보출력
	void showSpecificInfo() {
		try {
			System.out.print("계좌번호: ");
			int acc_num = Integer.parseInt(scan.nextLine().trim());
			
			String sql_accInfo = "SELECT serial_num, acc_id, name, balance, interest_rate"
					+ " FROM banking"
					+ " WHERE acc_id = ?";
			psmt = con.prepareStatement(sql_accInfo);
			psmt.setInt(1, acc_num);
			rs = psmt.executeQuery();
			
			if(!rs.next()) {
				System.out.println("해당 계좌가 존재하지 않습니다.");
				return;
			}
			
			int serial_num, acc_id, balance, interest_rate;
			String name;
			
			serial_num = rs.getInt("serial_num");
			acc_id = rs.getInt("acc_id");
			name = rs.getString("name");
			balance = rs.getInt("balance");
			interest_rate = rs.getInt("interest_rate");
			
			System.out.printf("[%d] 계좌번호: %d, 계좌주: %s, 잔액: %d, 이자율: %d%n", 
					serial_num, acc_id, name, balance, interest_rate);
			
			int updated = psmt.executeUpdate();
			System.out.println("[psmt]"+ updated + "행 출력됨");
			
		} catch (SQLException e) {
			System.out.println("SQL 예외 발생" + e.getMessage());
		}
	}
	
	//계좌삭제
	void deleteAcc() {
		try {
			csmt = con.prepareCall("{call DeleteAccount(?, ?)}");
			csmt.setString(1, inputValue("계좌번호"));
			csmt.registerOutParameter(2, Types.VARCHAR);
			csmt.execute();
			System.out.println("삭제프로시저 실행결과:");
			System.out.println(csmt.getString(2));
			
		} catch (SQLException e) {
			System.out.println("SQL 예외 발생" + e.getMessage());
		}
	}
	
	
	void exitProgram() {
		//프로그램 종료
		dbClose();
		System.out.println("프로그램을 종료합니다.");
		System.exit(0);
	}
	
	
	
}
