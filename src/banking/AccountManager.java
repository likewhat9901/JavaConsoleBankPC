package banking;

import java.util.Scanner;
import java.util.Set;
import java.util.zip.DataFormatException;
import java.util.HashSet;

import banking.util.AccountUtil;

public class AccountManager implements ICustomDefine{
	//스캐너 정적 클래스 변수로 선언(클래스 내에서 계속 사용하기 위함).
	public static Scanner scan = new Scanner(System.in);
	//HashSet 컬렉션 생성
	private Set<Account> accounts = new HashSet<Account>();
	//thread 객체 생성
	private AutoSaver autoTrd_daemon = new AutoSaver(accounts);

	//0.메뉴출력
	void showMenu() {
		System.out.println("\n-----Menu------");
		System.out.println("1.계좌개설");
		System.out.println("2.입 금");
		System.out.println("3.출 금");
		System.out.println("4.계좌정보출력");
		System.out.println("5.계좌정보삭제");
		System.out.println("6.계좌정보 수동저장");
		System.out.println("7.계좌정보 수동 불러오기");
		System.out.println("8.자동저장 옵션");
		System.out.println("0.프로그램종료");
		System.out.print("선택: ");
		
		try {
			//메뉴 선택
			int choice = Integer.parseInt(scan.nextLine());
			
			//메뉴 숫자가 아닌 숫자 입력시 사용자예외처리
			if(choice < 0 || choice > AUTOSAVE) {
				throw new MenuSelectException("0~" + AUTOSAVE + " 사이의 숫자를 입력하세요.");
			}
			
			//메뉴선택에 따른 기능 실행
			switch (choice)	{
			case MAKE: makeAccount(); break;	
			case DEPOSIT: depositMoney();	break;
			case WITHDRAW: withdrawMoney(); break;
			case INQUIRE: showAccInfo(); break;
			case DELETE: deleteAcc(); break;
			case SAVE: saveAccount(); break;
			case LOAD: loadAccount(); break;
			case AUTOSAVE: saveOption(); break;
			case EXIT: exitProgram(); break;
			}	
		} catch (NumberFormatException e) {
			System.out.println("[숫자가 아닙니다]");
		} catch (MenuSelectException e) {
			System.out.println("[메뉴 예외발생] "+ e.getMessage());
		}
	}
	//1.계좌개설
	void makeAccount() {
		try {
			//계좌타입 확인 시작
			//계좌타입 선택
	        System.out.println("\n-----계좌선택------");
	        System.out.println("1.보통계좌 / 2.신용신뢰계좌");
	        System.out.print("선택: ");
	        int accType = Integer.parseInt(scan.nextLine());
	        System.out.println("----------------");
	        
	        //1,2 이외의 값 입력시 메뉴복귀
	        if(!(accType == 1  || accType == 2)) {
				System.out.println("1 또는 2만 입력가능합니다.");
	        	return;
	        }
	        //(보통계좌 선택 시)특판계좌 가입여부
	        if(accType == 1) {
				while (true) {
					String answer = AccountUtil.inputLine(scan, "특판계좌 상품에 가입하시겠습니까?(y or n): ");
					switch (answer.toUpperCase()) {
					//Y 입력시, accType 3으로 변경
					case "Y":
						accType = 3;
						break;
					case "N":
						break;
					//잘못입력시, while문 다시시작
					default:
						System.out.println("y or n 을 입력하세요.");
						continue;
					}
					break;
				}
			}
	        //계좌타입 확인 끝
	        
	        //계좌개설 시작
	        //계좌개설에 필요한 기본항목들 입력
	        System.out.println("***신규계좌개설***");
	        String accNum = AccountUtil.inputLine(scan, "계좌번호: ");
	        String name = AccountUtil.inputLine(scan, "고객이름: ");
	        int balance = Integer.parseInt(AccountUtil.inputLine(scan, "잔고: "));
	        int intRate = Integer.parseInt(AccountUtil.inputLine(scan, "기본이자%(정수형태로입력): "));
	        
	        //계좌객체 선언
	        Account newAcc = null;
	        
	        //계좌타입에 따라 계좌객체 생성
	        switch (accType) {
	        //보통계좌 생성
			case 1:
				newAcc = new NormalAccount(accNum, name, balance, intRate);
				break;
			//신용신뢰계좌 생성
			case 2:
				//신용등급 확인
	    		while (true) {
	    			String CreditGrade = AccountUtil.inputLine(scan, "신용등급(A,B,C등급): ");
	    			switch (CreditGrade.toUpperCase()) {
	    			case "A", "B", "C" : 
	    				newAcc = new HighCreditAccount(accNum, name, balance, intRate, CreditGrade);
	            		break;
	    			default:
	    				//잘못입력시, while문 다시시작
	    				System.out.println("잘못된 입력입니다. A,B,C 중 하나를 입력하세요");
	    				continue;
	    			}
	    			break;
	    		}
	    		break;
	    	//특판계좌 생성
			case 3:
				int DepositCnt = 0;
				newAcc = new SpecialAccount(accNum, name, balance, intRate, DepositCnt);
				break;
			default:
	        	System.out.println("[계좌생성 예외발생]");
	        	return;
			}
	        
	        //HashSet에 계좌저장 & 중복계좌 처리
			while (true) {
				//이미 if 조건문 안에서 add 실행됨.(1번째 추가시도)
				//add가 true를 반환했을 경우(정상적으로 추가됨)
				if (accounts.add(newAcc)) {
					System.out.println("계좌개설 성공!");
					return;
				}
				//add가 false를 반환했을 경우(추가되지 않음)
				else {
					String answer = AccountUtil.inputLine(scan, "중복계좌 발견됨. 덮어쓰시겠습니까?(y or n): ");
					//덮어쓰기 여부 확인
					if (answer.equalsIgnoreCase("y")) {
						//기존계좌 삭제(계좌번호로 확인함)
						accounts.remove(newAcc);
						//새 계좌 생성(삭제 후 2번째 추가시도)
						accounts.add(newAcc);
						return;
					} else if (answer.equalsIgnoreCase("n")){
						//기존 계좌 유지
						System.out.println("계좌생성 취소");
						return;
					} else {
						//while문 다시시작
						System.out.println("y or n 을 입력하세요.");
						continue;
					}
				}
			}
		} //balance, interest를 문자열로 받아 Integer.parseInt 했을때 오류났을 경우
		catch (NumberFormatException e) {
	        System.out.println("숫자를 입력하세요.");
		}//try-catch 끝
	}//makeAccount 끝
	
	//2.입 금
	void depositMoney() {
		System.out.println("\n***입 금***\n" + "계좌번호와 입금할 금액을 입력하세요");
		
		//계좌번호 입력
		String searchAcc = AccountUtil.inputLine(scan, "계좌번호: ");
		//계좌찾아서 acc에 저장. 없으면 null.
		Account acc = AccountUtil.findAccount(accounts, searchAcc);
		
		//계좌가 있을경우(null이 아닐경우) 입금처리 진행.
		if (acc != null) {
			System.out.println("##계좌를 찾았습니다.##");
			
			//입금조건 확인하고 문제없을경우 d_money 변수에 입금액 저장.
			int d_money = AccountUtil.depositCheck(scan, acc);
			
			//d_money만큼 입금처리 진행
			acc.deposit(d_money);
			
		} else {
			//acc가 null일 경우
			System.out.println("##찾는 계좌가 없습니다.##");
		}
	}
	//3.출 금
	void withdrawMoney() {
		System.out.println("\n***출   금***\n" + "계좌번호와 출금할 금액을 입력하세요");
		
		//계좌번호 입력
		String searchAcc = AccountUtil.inputLine(scan, "계좌번호: ");
		//계좌찾아서 acc에 저장. 없으면 null.
		Account acc = AccountUtil.findAccount(accounts, searchAcc);
		
		//계좌가 있을경우(null이 아닐경우) 출금처리 진행.
		if (acc != null) {
			System.out.println("##계좌를 찾았습니다.##");
			
			//출금조건 확인하고 문제없을경우 w_money 변수에 출금액 저장.
			int w_money = AccountUtil.withdrawCheck(scan, acc);
			
			//w_money만큼 출금처리 진행
			acc.withdraw(w_money);
		} else {
			//acc가 null일 경우
			System.out.println("##찾는 계좌가 없습니다.##");
		}
	}
	//4.전체계좌정보출력
	void showAccInfo() {
		
		System.out.println("\n***계좌정보출력***");
		
		//for each 문으로 순회하며 데이터 읽기
		for(Account a : accounts) {
			//오버라이딩한 showAccInfo(보통계좌는 보통계좌에서, 신용신뢰계좌는 신용신뢰계좌에서)
			a.showAccInfo();
		}
		
		//출력된 계좌 개수
		System.out.println("전체계좌정보 출력이 완료되었습니다.\n"
				+ "출력된 계좌 수: " + accounts.size() + "개");
	}
	//5.계좌정보삭제
	void deleteAcc() {
		System.out.println("\n***계좌정보삭제***\n" + "삭제할 계좌번호를 입력하세요");
		
		//계좌번호 입력
		String searchAcc = AccountUtil.inputLine(scan, "계좌번호: ");
		//입력한 계좌 찾기
		Account searchedAcc = AccountUtil.findAccount(accounts, searchAcc);
		
		//계좌가 있을때 삭제 진행
		if (searchedAcc != null) {
			System.out.println("##계좌를 찾았습니다.##");
			while (true) {
				//삭제 여부 2차 확인
				String answer = AccountUtil.inputLine(scan, "정말로 삭제할까요?(Y or N): ").toUpperCase();
				
				switch (answer) {
				//삭제 진행
				case "Y" : AccountUtil.deleteAccount(searchAcc, searchedAcc, accounts);
				return;
				//삭제 취소
				case "N" : System.out.println("삭제가 취소되었습니다.");
				return;
				//잘못된 입력
				default : System.out.println("(Y or N)을 입력하세요");
				continue;
				}
			}//while
		} 
		//계좌가 없을때
		else {
			System.out.println("##찾는 계좌가 없습니다.##");
		}
	}
	//6.계좌정보 수동저장
	void saveAccount() {
		System.out.println("\n***계좌정보 수동저장(obj)***");
		while (true) {
			//계좌저장 여부 2차 확인
			String answer = AccountUtil.inputLine(scan, "파일로 저장하시겠습니까?(Y or N): ");
			
			switch (answer.toUpperCase()) {
			case "Y": 
				//계좌정보(accounts) obj 파일로 저장
				AccountUtil.saveAccAsFile(accounts);
				return;
			case "N": 
				System.out.println("저장이 취소되었습니다.");
				return;
			default:
				//while문 처음부터 다시
				System.out.println("(Y or N)을 입력하세요");
				continue;
			}
		}
	}
	//7.계좌정보 불러오기
	void loadAccount() {
		System.out.println("\n***계좌파일 불러오기(obj)***");
		while (true) {
			//불러오기 여부 2차 확인
			String answer = AccountUtil.inputLine(scan, "파일을 불러오시겠습니까?(Y or N): ");
			
			switch (answer.toUpperCase()) {
			case "Y": 
				//불러오기 실행
				AccountUtil.loadAccFile(scan, accounts);
				return;
			case "N": 
				//불러오기 취소
				System.out.println("불러오기가 취소되었습니다.");
				return;
			default: 
				//continue로 while문 다시 실행
				System.out.println("(Y or N)을 입력하세요");
				continue;
			}
		}
	}
	//8.저장옵션
	void saveOption() {
		System.out.println("\n***저장옵션***");
		System.out.println("저장옵션을 선택하세요\n" + "1.자동저장 On\n" + "2.자동저장 Off" );
		System.out.println("3.메뉴로 돌아가기");
		
		int answer = 0;
		
		try {
			//저장옵션 선택 입력
			answer = Integer.parseInt(AccountUtil.inputLine(scan, "선택: ").trim());
		}
		//숫자가 아닌 입력은 예외처리
		catch (NumberFormatException e) {
			System.out.println("문자 또는 잘못된 입력입니다. 숫자를 입력하세요.\n");
		}
		
		switch (answer) {
		case 1:
			//autoTrd가 존재하는지 and 실행중인지 확인
			if (autoTrd_daemon != null && autoTrd_daemon.isAlive()) {
				System.out.println("이미 자동저장이 실행중입니다");
				return;
			} else {
				//데몬쓰레드로 변경
				autoTrd_daemon.setDaemon(true);
				//쓰레드 실행
				autoTrd_daemon.start();
				System.out.println("***자동저장을 시작합니다.***");
				return;
			}
		//자동저장 중지
		case 2:
			//autoTrd가 존재하는지 and 실행중인지 확인
			if (autoTrd_daemon != null && autoTrd_daemon.isAlive()) {
				//쓰레드 중지요청(적절한 위치에서 작업 종료)
				autoTrd_daemon.interrupt();
				return;
			} else {
				System.out.println("자동저장이 중지중입니다.");
				return;
			}
		case 3:
			return;
		default:
			System.out.println("1 or 2를 입력하세요.");
			saveOption();
			return;
		}
	}
	//0.프로그램 종료
	void exitProgram() {
		//종료 전, obj파일로 저장.
		AccountUtil.saveAccAsFile(accounts);
		//프로그램 종료
		System.out.println("프로그램을 종료합니다.");
		System.exit(0);
	}
	
	
}
