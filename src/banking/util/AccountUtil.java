package banking.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

import banking.Account;
import banking.HighCreditAccount;
import banking.NormalAccount;


public class AccountUtil {
	
	//기타
    public static String inputLine(Scanner scan, String msg) {
        System.out.print(msg);
        return scan.nextLine().trim();
    }
    
	public static Account findAccount(Set<Account> accounts, String accNum) {
		
		for (Account acc : accounts) {
			if (acc != null && acc.getAccNum().equals(accNum)) {
				return acc;
			}
		}
		return null;
	}
	
    //1.계좌개설
    public static void createAccount(Scanner scan, int accType, Set<Account> accounts) {
    	
        String accNum = inputLine(scan, "계좌번호: ");
        String name = inputLine(scan, "고객이름: ");
        int balance = Integer.parseInt(inputLine(scan, "잔고: "));
        int intRate = Integer.parseInt(inputLine(scan, "기본이자%(정수형태로입력): "));
        int depositCount = 0;
        
        //계좌정보 저장할 Account 객체 생성 
        Account NewAcc = null;
        
        //계좌타입에 따라 계좌생성
        if (accType == 1) {
        	NewAcc = new NormalAccount(accType, accNum, name, balance, intRate, depositCount);
        } else if (accType == 2) {
    		while (true) {
    			String CreditGrade = inputLine(scan, "신용등급(A,B,C등급): ").toUpperCase();
    			switch (CreditGrade) {
    			case "A", "B", "C" : 
    				NewAcc = new HighCreditAccount
    				(accType, accNum, name, balance, intRate, CreditGrade);
    				break;
    			default:
    				System.out.println("잘못된 입력입니다. A,B,C 중 하나를 입력하세요");
    				continue;
    			}
    			break;
    		}
        } else {
        	System.out.println("뭔가 잘못됐습니다.");
        	return;
        }
        
        //중복확인
        if (accounts.contains(NewAcc)) {
        	String answer = inputLine(scan, "중복계좌 발견됨. 덮어쓸까요?(y or n): ");
        	
        	if (answer.equalsIgnoreCase("y")) {
        		System.out.println("기존 계좌 삭제됨 : " + accounts.remove(NewAcc));
        		System.out.println("새로운 계좌 생성됨 : " + accounts.add(NewAcc));
        		System.out.println("덮어쓰기 완료!");
        	} else if (answer.equalsIgnoreCase("n")){
        		System.out.println("계좌추가 취소");
        	} else {
        		System.out.println("y or n 을 입력하세요.");
        	}
        } else {
        	accounts.add(NewAcc);
        	System.out.println("계좌 추가완료!");
        }
    }
	
	//2.입 금
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
	public static int calculateInterest(Account acc) {
		int interest = 0;
		
		switch (acc.getAccType()) {
		case 1: 
			NormalAccount normAcc = (NormalAccount) acc;
			interest = (int) (acc.getBalance() * normAcc.getInterest() / 100);
			return interest;
		case 2: 
			HighCreditAccount highAcc = (HighCreditAccount) acc;
			interest = (int) (acc.getBalance() 
					* (highAcc.getInterest() + highAcc.getCreditIntereset()) / 100);
			return interest;
		default:
			System.out.println("계좌타입을 알수 없습니다.");
			return interest;
		}
	}
	
	//3.출 금
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
	
	//5.계좌정보삭제
	public static void deleteAccount(String searchAcc, Account searchedAcc, Set<Account> accounts) {
		//삭제 실행
		accounts.remove(searchedAcc);
		//삭제 확인
		Account deleteCheck = findAccount(accounts, searchAcc);
		//확인 결과
		if(deleteCheck == null) {
			System.out.println("삭제 성공!");
		} else {
			System.out.println("삭제 실패..");
		}
	}
	
	//6.계좌정보 파일저장
	public static void saveAccAsFile(Set<Account> accounts) {
		String fileroot = "src/banking/file/AccountInfo.obj";
		File file = new File(fileroot);
		
		if (file.exists()) {
			file.delete();
		} 
		
		try (ObjectOutputStream out = new ObjectOutputStream(
				new FileOutputStream(fileroot)))
		{	
			System.out.println(accounts.size() + "개의 계좌를 저장합니다...");
			out.writeObject(accounts);
			System.out.println("AccountInfo.obj 파일로 저장되었습니다.");
			
		} catch (IOException e) {
            System.out.println("파일저장 실패..");
			e.printStackTrace();
		}
	}
	
	//7.계좌정보 불러오기
	public static void loadAccFile(Scanner scan, Set<Account>accounts) {
		String filename = "src/banking/file/AccountInfo.obj";
		
		if (!new File(filename).exists()) {
			System.out.println("불러올 파일이 존재하지 않습니다.");
			return;
		}
		
		try (ObjectInputStream in = new ObjectInputStream
				(new FileInputStream(filename)))
		{	
			@SuppressWarnings("unchecked")
			Set<Account> loaded = (HashSet<Account>) in.readObject();
			accounts.clear(); //기존내용 지우기
			accounts.addAll(loaded); //loaded 데이터 추가
			
			System.out.println("AccountInfo.obj 복원완료");
			System.out.println(loaded.size() + "개의 계좌를 불러왔습니다.");
            return;
		} catch (IOException | ClassNotFoundException e) {
            System.out.println("계좌정보 불러오기 실패..");
			e.printStackTrace();
			return;
		}
	}
	
	public static void autoSave(Set<Account> accounts) {
		
		
	}
	
}
