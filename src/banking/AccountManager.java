package banking;

import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

import banking.util.AccountUtil;

public class AccountManager implements ICustomDefine{
	Scanner scan = new Scanner(System.in);
	
	private Set<Account> accounts = new HashSet<Account>();

	void showMenu() {
		while (true) {
			System.out.println("\n-----Menu------");
			System.out.println("1.계좌개설");
			System.out.println("2.입 금");
			System.out.println("3.출 금");
			System.out.println("4.전체계좌정보출력");
			System.out.println("5.계좌정보삭제");
			System.out.println("6.계좌정보 파일저장");
			System.out.println("7.계좌정보 불러오기");
			System.out.println("8.자동저장 옵션");
			System.out.println("0.프로그램종료");
			System.out.print("선택: ");
			
			try {
				int choice = Integer.parseInt(scan.nextLine());
				
				if(choice < 0 || choice > AUTOSAVE) {
					throw new MenuSelectException("0~"+ AUTOSAVE + " 사이의 숫자를 입력하세요.");
				}
				
				switch (choice)	{
				case MAKE: makeAccount(); break;	
				case DEPOSIT: depositMoney();	break;
				case WITHDRAW: withdrawMoney(); break;
				case INQUIRE: showAccInfo(); break;
				case DELETE: deleteAcc(); break;
				case SAVE: saveAccount(); break;
				case LOAD: loadAccount(); break;
				case AUTOSAVE: saveOption(); break;
				case EXIT: {
					System.out.println("프로그램을 종료합니다.");
					System.exit(0);
					}
				}	
			} catch (NumberFormatException e) {
				System.out.println("문자 또는 잘못된 입력입니다. 숫자를 입력하세요.\n");
			} catch (MenuSelectException e) {
				System.out.println("[예외발생] "+ e.getMessage());
			}
		}
	}
	//1.계좌개설
	void makeAccount() {
		try {
	        System.out.println("\n***신규계좌개설***");
	        System.out.println("-----계좌선택------");
	        System.out.println("1.보통계좌");
	        System.out.println("2.신용신뢰계좌");
	        System.out.print("선택: ");
	        int accType = Integer.parseInt(scan.nextLine());
	        System.out.println("----------------");
	        if (accType != 1 && accType != 2) {
	            System.out.println("1 또는 2만 입력가능합니다.\n");
	            return;  // 실패 시 null 반환
	        }
			AccountUtil.createAccount(scan, accType, accounts);
			
		} catch (NumberFormatException e) {
	        System.out.println("숫자를 입력하세요.\n");
		}//try-catch
	}
	//2.입 금
	void depositMoney() {
		System.out.println("\n***입 금***\n" + "계좌번호와 입금할 금액을 입력하세요");
		String searchAcc = AccountUtil.inputLine(scan, "계좌번호: ");
		
		Account acc = AccountUtil.findAccount(accounts, searchAcc);
		
		if (acc != null) {
			System.out.println("##계좌를 찾았습니다.##");
			
			int deposit = AccountUtil.depositCheck(scan, acc);
			int interest = AccountUtil.calculateInterest(acc);
			
			acc.setBalance(acc.getBalance() + deposit + interest);
			System.out.printf("입금이 완료되었습니다. 잔액: %d (이자: %d)%n",acc.getBalance(), interest);
		} else {
			System.out.println("##찾는 계좌가 없습니다.##");
		}
	}
	//3.출 금
	void withdrawMoney() {
		System.out.println("\n***출   금***\n" + "계좌번호와 출금할 금액을 입력하세요");
		String searchAcc = AccountUtil.inputLine(scan, "계좌번호: ");
		
		Account acc = AccountUtil.findAccount(accounts, searchAcc);
		
		if (acc != null) {
			System.out.println("##계좌를 찾았습니다.##");
			
			int withdraw = AccountUtil.withdrawCheck(scan, acc);
			
			acc.setBalance(acc.getBalance() - withdraw);
			System.out.println("출금이 완료되었습니다. 잔액: " + acc.getBalance());
		} else {
			System.out.println("##찾는 계좌가 없습니다.##");
		}
	}
	//4.전체계좌정보출력
	void showAccInfo() {
		
		System.out.println("\n***계좌정보출력***");
		
		for(Account a : accounts) {
			a.showAccInfo();
		}
		System.out.println("전체계좌정보 출력이 완료되었습니다.\n"
				+ "출력된 계좌 수:" + accounts.size() + "개");
	}
	//5.계좌정보삭제
	void deleteAcc() {
		System.out.println("\n***계좌정보삭제***\n" + "삭제할 계좌번호를 입력하세요");
		String searchAcc = AccountUtil.inputLine(scan, "계좌번호: ");
		
		//계좌 찾기
		Account searchedAcc = AccountUtil.findAccount(accounts, searchAcc);
		
		if (searchedAcc != null) {
			System.out.println("##계좌를 찾았습니다.##");
			while (true) {
				String answer = AccountUtil.inputLine(scan, "정말로 삭제할까요?(Y or N): ");
				
				switch (answer.toUpperCase()) {
				case "Y" : AccountUtil.deleteAccount(searchAcc, searchedAcc, accounts);
				return;
				case "N" : System.out.println("삭제가 취소되었습니다.");
				return;
				default : System.out.println("(Y or N)을 입력하세요");
				continue;
				}
			}//while
		} else {
			System.out.println("##찾는 계좌가 없습니다.##");
		}
	}
	//6.계좌정보 파일저장
	void saveAccount() {
		System.out.println("\n***계좌파일저장(obj)***");
		while (true) {
			String answer = AccountUtil.inputLine(scan, "파일로 저장하시겠습니까?(Y or N): ");
			
			switch (answer.toUpperCase()) {
			case "Y": 
				AccountUtil.saveAccAsFile(accounts);
				return;
			case "N": 
				System.out.println("저장이 취소되었습니다.");
				return;
			default: System.out.println("(Y or N)을 입력하세요");
			continue;
			}
		}
	}
	//7.계좌정보 불러오기
	void loadAccount() {
		System.out.println("\n***계좌파일 불러오기(obj)***");
		while (true) {
			String answer = AccountUtil.inputLine(scan, "파일을 불러오시겠습니까?(Y or N): ");
			
			switch (answer.toUpperCase()) {
			case "Y": 
				Set<Account> loaded = AccountUtil.loadAccFile(scan);
				accounts.clear(); //기존내용 지우기
				accounts.addAll(loaded); //loaded 데이터 추가
				return;
			case "N": 
				System.out.println("불러오기가 취소되었습니다.");
				return;
			default: System.out.println("(Y or N)을 입력하세요");
			continue;
			}
		}
	}
	//8.저장옵션
	void saveOption() {
		AutoSaver autoTrd = new AutoSaver(accounts);
		
		System.out.println("\n***저장옵션***");
		System.out.println("저장옵션을 선택하세요\n" + "1.자동저장 On\n" + "2.자동저장 Off" );
		int answer = Integer.parseInt(AccountUtil.inputLine(scan, "선택: "));
		
		switch (answer) {
		case 1:
			System.out.println("자동저장을 실행합니다.");
			autoTrd.start();
			break;
		case 2:
			
			break;
		default:
			break;
		}

	}
	
	
}
