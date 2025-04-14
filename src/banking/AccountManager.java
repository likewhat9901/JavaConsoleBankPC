package banking;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class AccountManager implements ICustomDefine{
	Scanner scan = new Scanner(System.in);
	
	private Account[] account = new Account[50];
	private Set<Account> accounts = new HashSet<Account>();

	void showMenu() {
		while (true) {
			System.out.println("-----Menu------");
			System.out.println("1.계좌개설");
			System.out.println("2.입	금");
			System.out.println("3.출	금");
			System.out.println("4.전체계좌정보출력");
			System.out.println("5.계좌정보삭제");
			System.out.println("6.프로그램종료");
			System.out.print("선택: ");
			
			try {
				int choice = Integer.parseInt(scan.nextLine());
				System.out.println();
				
				if(choice < 1 || choice >5) {
					throw new MenuSelectException("1~5 사이의 숫자를 입력하세요.");
				}
				
				switch (choice)	{
				case MAKE: makeAccount(); break;	
				case DEPOSIT: depositMoney();	break;
				case WITHDRAW: withdrawMoney(); break;
				case INQUIRE: showAccInfo(); break;
				case DELETE:  break;
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
		String mAcc, mName, credit = "";
		int mBal, intRate, choice;
		try {
			System.out.println("***신규계좌개설***");
			System.out.println("-----계좌선택------");
			System.out.println("1.보통계좌");
			System.out.println("2.신용신뢰계좌");
			System.out.print("선택: ");
			choice = Integer.parseInt(scan.nextLine());
			
			if (choice != 1 && choice != 2) {
				System.out.println("1 또는 2만 입력가능합니다.\n");
				showMenu();
			}
			
			System.out.print("계좌번호: "); mAcc = scan.nextLine();
			System.out.print("고객이름: "); mName = scan.nextLine();
			System.out.print("잔고: "); mBal = Integer.parseInt(scan.nextLine());
			System.out.print("기본이자%(정수형태로입력): "); 
			intRate = Integer.parseInt(scan.nextLine());
			
			Account acc = null;
			
			if(choice == 1) {
				acc = new NormalAccount(mAcc, mName, mBal, intRate);
			} else if (choice == 2){
				System.out.print("신용등급(A,B,C등급): "); credit = scan.nextLine();
				acc = new HighCreditAccount(mAcc, mName, mBal, intRate, credit);
			}
			if(accounts.add(acc)) {
				System.out.println("계좌개설이 완료되었습니다.\n");
				System.out.println("현재 저장된 계좌 수: " + accounts.size());
			} else {
				System.out.println("이미 존재하는 계좌입니다.");
				System.out.print("덮어쓸까요?(y or n)");
				String answer = scan.nextLine().toUpperCase();
				
				if (answer.equals("Y")) {
					Account duplicate = null;
					for (Account a : accounts) {
						if (a.equals(acc)) {
							duplicate = a;
							break;
						}
					}
					if (duplicate != null) {
						accounts.remove(duplicate);
						accounts.add(acc);
						System.out.println("덮어쓰기 완료");
					}
				} else if(answer.equals("N")) {
					System.out.println("기존 계좌를 유지합니다.");
				} else
					System.out.println("(y or n)을 입력하세요");
			}
		} catch (NumberFormatException e) {
	        System.out.println("숫자를 입력하세요.\n");
		}
		
	}
	
	void depositMoney() {
		boolean isFind = false;
		
		System.out.println("***입   금***");
		System.out.println("계좌번호와 입금할 금액을 입력하세요");
		while (true) {
			
			System.out.print("계좌번호: ");
			String SearchAcc = scan.nextLine();
			for(int i=0 ; i<numOfAcc ; i++) {
				
				if(SearchAcc.compareTo(account[i].accNum)==0) {
					//검색할 이름과 일치하다면 전체 정보를 출력
					System.out.println("##계좌를 찾았습니다.##");
					while (true) {
						try {
							System.out.println("잔액: "+ account[i].balance);
							System.out.print("입금액: ");
							int deposit = Integer.parseInt(scan.nextLine());
							
							if(deposit < 0) {
								System.out.println("0 이상을 입력하세요.");
								continue;
							} else if(deposit % 500 != 0) {
								System.out.println("500원 단위로 입력하세요.");
								continue;
							}
							
							account[i].balance += deposit;
							System.out.println("잔액: "+ account[i].balance);
							System.out.println("입금이 완료되었습니다.\n");
							
							isFind = true;
							break;
						} catch (NumberFormatException e) {
							System.out.println("문자 또는 잘못된 입력입니다. 숫자를 입력하세요.\n");
						}
					}
					showMenu();
				}
			}
			if(isFind==false) {
				System.out.println("##찾는 계좌가 없습니다.##");
				continue;
			}
		}
	}
	
	void withdrawMoney() {
		boolean isFind = false;
		
		System.out.println("***출   금***");
		System.out.println("계좌번호와 출금할 금액을 입력하세요");
		while (true) {
			System.out.print("계좌번호: ");
			String SearchAcc = scan.nextLine();
			
			for(int i=0 ; i<numOfAcc ; i++) {
				
				if(SearchAcc.compareTo(account[i].accNum)==0) {
					//검색할 이름과 일치하다면 전체 정보를 출력
					System.out.println("##계좌를 찾았습니다.##");
					while (true) {
						try {
							System.out.println("잔액: "+ account[i].balance);
							System.out.print("출금액: ");
							int withdraw = Integer.parseInt(scan.nextLine());
							
							if(withdraw < 0) {
								System.out.println("0 이상을 입력하세요.");
								continue;
							} else if(withdraw > account[i].balance) {
								System.out.print("잔고가 부족합니다. 금액전체를 출금할까요?(YES / NO)");
								String withdrawAll = scan.nextLine();
								if (withdrawAll.equalsIgnoreCase("YES")) {
									withdraw = account[i].balance;
								} else if(withdrawAll.equalsIgnoreCase("NO")) {
									continue;
								} else {
									System.out.println("잘못된 입력입니다.");
									continue;
								}
							} else if(withdraw % 1000 != 0) {
								System.out.println("1000원 단위로 입력하세요.");
								continue;
							}
							
							account[i].balance -= withdraw;
							System.out.println("잔액: "+ account[i].balance);
							System.out.println("출금이 완료되었습니다.\n");
							isFind = true;
							break;
						} catch (NumberFormatException e) {
							System.out.println("문자 또는 잘못된 입력입니다. 숫자를 입력하세요.\n");
						}
					}
					showMenu();
				}
			}
			if(isFind==false) {
				System.out.println("##찾는 계좌가 없습니다.##");
				continue;
			}
		}
	}
	
	void showAccInfo() {
		
		System.out.println("***계좌정보출력***");
		
		for(Account a : accounts) {
			a.showAccInfo();
		}
		System.out.println("전체계좌정보 출력이 완료되었습니다.\n");
	}
	
}
