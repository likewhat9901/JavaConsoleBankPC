package banking;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
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
			System.out.println("6.프로그램종료");
			System.out.print("선택: ");
			
			try {
				int choice = Integer.parseInt(scan.nextLine());
				
				if(choice < 1 || choice > 6) {
					throw new MenuSelectException("1~6 사이의 숫자를 입력하세요.");
				}
				
				switch (choice)	{
				case MAKE: makeAccount(); break;	
				case DEPOSIT: depositMoney();	break;
				case WITHDRAW: withdrawMoney(); break;
				case INQUIRE: showAccInfo(); break;
				case DELETE: deleteAcc(); break;
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
	
	void makeAccount() {
		try {
			Account acc = AccountUtil.createAccount(scan);
			if (acc == null) return;
			
			// 맞으면 origin = 찾은 원본계좌 / 틀리면 origin = null
			Account origin = AccountUtil.findAccount(accounts, acc.getAccNum());
			
			AccountUtil.coverAccount(scan, origin, acc, accounts);
			
		} catch (NumberFormatException e) {
	        System.out.println("숫자를 입력하세요.\n");
		}
	}
	
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
	
	void showAccInfo() {
		
		System.out.println("\n***계좌정보출력***");
		
		for(Account a : accounts) {
			a.showAccInfo();
		}
		System.out.println("전체계좌정보 출력이 완료되었습니다.");
	}
	
	void deleteAcc() {
		
	}
	
}
