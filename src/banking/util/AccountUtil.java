package banking.util;

import java.util.Scanner;
import java.util.Set;

import banking.Account;
import banking.HighCreditAccount;
import banking.NormalAccount;


public class AccountUtil {
	
    public static String inputLine(Scanner scan, String msg) {
        System.out.print(msg);
        return scan.nextLine();
    }
	
    public static Account createAccount(Scanner scan) {
    	
        System.out.println("\n***신규계좌개설***");
        System.out.println("-----계좌선택------");
        System.out.println("1.보통계좌");
        System.out.println("2.신용신뢰계좌");
        System.out.print("선택: ");
        int accType = Integer.parseInt(scan.nextLine());
        System.out.println("----------------");

        if (accType != 1 && accType != 2) {
            System.out.println("1 또는 2만 입력가능합니다.\n");
            return null;  // 실패 시 null 반환
        }
    	
        String accNum = inputLine(scan, "계좌번호: ");
        String name = inputLine(scan, "고객이름: ");
        int balance = Integer.parseInt(inputLine(scan, "잔고: "));
        int intRate = Integer.parseInt(inputLine(scan, "기본이자%(정수형태로입력): "));

        if (accType == 1) {
            return new NormalAccount(accType, accNum, name, balance, intRate);
        } else if (accType == 2) {
        	while (true) {
        		String grade = inputLine(scan, "신용등급(A,B,C등급): ").toUpperCase();
        		switch (grade) {
        		case "A", "B", "C" : return new HighCreditAccount(accType, accNum, name, balance, intRate, grade);
        		default:
        			System.out.println("잘못된 입력입니다. A,B,C 중 하나를 입력하세요");
        		}
			}
        }
		return null;
    }
    
	public static Account findAccount(Set<Account> accounts, String accNum) {
		
		for (Account acc : accounts) {
			if (acc != null && acc.getAccNum().equals(accNum)) {
				return acc;
			}
		}
		return null;
	}
	
	public static void coverAccount(Scanner scan, Account origin, Account acc, Set<Account> accounts) {
		
		if(origin == null) {
			accounts.add(acc);
			System.out.println("계좌개설이 완료되었습니다.\n"
					+ "현재 저장된 계좌 수: " + accounts.size());
		} else {
			System.out.println("이미 존재하는 계좌입니다.");
			System.out.print("덮어쓸까요?(y or n)");
			String answer = scan.nextLine().trim().toUpperCase();
			
			if (answer.equals("Y")) {
				//기존 계좌 지우고 입력한 새로운 계좌(acc) 삽입
				accounts.remove(origin);
				accounts.add(acc);
				System.out.println("덮어쓰기 완료");
			} else if(answer.equals("N")) {
				System.out.println("기존 계좌를 유지합니다.");
			} else
				System.out.println("(y or n)을 입력하세요");
		}
	}
	
	public static int depositCheck (Scanner scan, Account acc) {
		while (true) {
			try {
				System.out.println("잔액: " + acc.getBalance());
				int deposit = Integer.parseInt(AccountUtil.inputLine(scan, "입금액: "));
				
				if(deposit < 0) {
					System.out.println("0 이상을 입력하세요.");
					continue;
				} else if(deposit % 500 != 0) {
					System.out.println("500원 단위로 입력하세요.");
					continue;
				} else {
					return deposit;
				}
			} catch (NumberFormatException e) {
				System.out.println("문자 또는 잘못된 입력입니다. 숫자를 입력하세요.\n");
			}
		}
	}
	
	public static int withdrawCheck (Scanner scan, Account acc) {
		while (true) {
			try {
				System.out.println("잔액: " + acc.getBalance());
				int withdraw = Integer.parseInt(AccountUtil.inputLine(scan, "출금액: "));
				
				if(withdraw < 0) {
					System.out.println("0 이상을 입력하세요.");
					continue;
				} else if(withdraw > acc.getBalance()) {
					String answer = inputLine(scan, "잔고가 부족합니다. 금액전체를 출금할까요?(YES / NO): ").trim();
					if (answer.equalsIgnoreCase("YES")) {
						withdraw = acc.getBalance();
						return withdraw;
					} else if(answer.equalsIgnoreCase("NO")) {
						continue;
					} else {
						System.out.println("잘못된 입력입니다.");
						continue;
					}
				}else if(withdraw % 1000 != 0) {
					System.out.println("1000원 단위로 입력하세요.");
					continue;
				} else {
					return withdraw;
				}
			} catch (NumberFormatException e) {
				System.out.println("문자 또는 잘못된 입력입니다. 숫자를 입력하세요.\n");
			}
		}
	}
	
	public static int calculateInterest(Account acc) {
		int interest = 0;
		
		switch (acc.getAccType()) {
		case 1: 
			NormalAccount normAcc = (NormalAccount) acc;
			interest = (int) (acc.getBalance() * normAcc.getInterest() / 100);
			return interest;
		case 2: 
			HighCreditAccount highAcc = (HighCreditAccount) acc;
			interest = (int) (acc.getBalance() * highAcc.getInterest() / 100);
			return interest;
		default:
			System.out.println("계좌타입을 알수 없습니다.");
			return interest;
		}
	}
	
	
	
	
	
}
