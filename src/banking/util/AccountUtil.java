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
	
	private AccountUtil() {
		// 객체생성 방지용
	}
	
	//기타
    public static String inputLine(Scanner scan, String msg) {
        System.out.print(msg);
        return scan.nextLine().trim();
    }
    
	public static Account findAccount(Set<Account> accounts, String accNum) {
		Account dummy = new NormalAccount(1, accNum, "", 0, 0, 0);
		
		if (accounts.contains(dummy)) {
			for (Account acc : accounts) {
				if (acc.equals(dummy)) {
					return acc;
				}
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
        //보통계좌 count = 0 으로 설정
        int normDepositCnt = 0;
        
        //계좌정보 저장할 Account 객체 생성 
        Account NewAcc = null;
        
        //계좌타입에 따라 계좌생성
        if (accType == 1) {
        	NewAcc = new NormalAccount
        			(accType, accNum, name, balance, intRate, normDepositCnt);
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
        		System.out.println("계좌개설 취소");
        	} else {
        		System.out.println("y or n 을 입력하세요.");
        	}
        } else {
        	accounts.add(NewAcc);
        	System.out.println("계좌 추가완료!");
        }
    }
    
    public static void createAccount2(Scanner scan, int accType, Set<Account> accounts) {
        String accNum = inputLine(scan, "계좌번호: ");
        Account dummy = new NormalAccount(accType, accNum, " ", 0, 0, 0);
        
        //중복확인
        while (true) {
        	if (accounts.contains(dummy)) {
        		String answer = inputLine(scan, "중복계좌 발견됨. 덮어쓰시겠습니까?(y or n): ");
        		
        		if (answer.equalsIgnoreCase("y")) {
        			accounts.remove(dummy);
        			System.out.println("추가정보를 입력해주세요.");
        			break;
        		} else if (answer.equalsIgnoreCase("n")){
        			System.out.println("계좌생성 취소");
        			return;
        		} else {
        			System.out.println("y or n 을 입력하세요.");
        			continue;
        		}
        	} else {
        		break;
        	}
		}
        
        //추가정보 입력
        String name = inputLine(scan, "고객이름: ");
        int balance = Integer.parseInt(inputLine(scan, "잔고: "));
        int intRate = Integer.parseInt(inputLine(scan, "기본이자%(정수형태로입력): "));
        
        //계좌타입에 따라 계좌생성
        if (accType == 1) {
        	//보통계좌 count = 0 으로 설정
        	int normDepositCnt = 0;
        	Account NewAcc = new NormalAccount
        			(accType, accNum, name, balance, intRate, normDepositCnt);
        	accounts.add(NewAcc);
        } else if (accType == 2) {
    		while (true) {
    			String CreditGrade = inputLine(scan, "신용등급(A,B,C등급): ").toUpperCase();
    			switch (CreditGrade) {
    			case "A", "B", "C" : 
    				Account NewAcc = new HighCreditAccount
    				(accType, accNum, name, balance, intRate, CreditGrade);
            		accounts.add(NewAcc);
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
			return 0;
		}
	}
	
	public static int specialDep(Account acc) {
		int specialMoney = 500;
		NormalAccount normAcc = (NormalAccount) acc;
		
		switch (acc.getAccType()) {
		case 1: 
			normAcc.setDepositCount(normAcc.getDepositCount() + 1);
			System.out.println("입금횟수: "+ normAcc.getDepositCount());
			
			return specialMoney;
		case 2:
			
			return 0;
		default:
			System.out.println("계좌타입을 알수 없습니다.");
			return 0;
		}
	}
	
	//3.출 금
	public static int withdrawCheck (Scanner scan, Account acc) {
		while (true) {
			try {
				System.out.println("잔액: " + acc.getBalance());
				int withdraw = Integer.parseInt(AccountUtil.inputLine(scan, "출금액: "));
				
				//출금액이 0보다 작을경우
				if(withdraw < 0) {
					System.out.println("0 이상을 입력하세요.");
					continue;
				} 
				//잔액보다 출금액이 클 경우
				else if(withdraw > acc.getBalance()) {
					String answer = inputLine(scan, "잔고가 부족합니다. 금액전체를 출금할까요?(YES / NO): ").trim();
					
					//입력한 출금액을 잔액으로 치환하고 출금액 반환.
					if (answer.equalsIgnoreCase("YES")) {
						withdraw = acc.getBalance();
						return withdraw;
					} 
					//출금액 다시 입력. while문 처음부터 다시
					else if(answer.equalsIgnoreCase("NO")) {
						continue;
					} 
					//잘못된 입력. while문 처음부터 다시
					else {
						System.out.println("잘못된 입력입니다.");
						continue;
					}
				}
				//1000원 단위가 아닐경우, while문 처음부터 다시
				else if(withdraw % 1000 != 0) {
					System.out.println("1000원 단위로 입력하세요.");
					continue;
				} 
				//이상없을 경우, 출금액 반환
				else {
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
		//파일을 저장할 경로, 이름 지정
		String fileroot = "src/banking/file/AccountInfo.obj";
		//참조용 파일 객체 생성(파일을 저장할 준비)(지정한 경로를 참조)
		File file = new File(fileroot);
		
		//기존 파일이 있으면 삭제
		if (file.exists()) {
			file.delete();
		} 
		
		//new FileOutputStream(fileroot) : fileroot 경로에 파일을 생성(바이트 단위 생성)
		//new ObjectOutputStream : FileOutputStream에 객체를 직렬화해서 넘길 수 있도록 기능구현.
		//ObjectOutputStream : 기계가 이해할 수 있도록 바이트 데이터로 바꿔주는 번역기
		try (ObjectOutputStream out = new ObjectOutputStream(
				new FileOutputStream(fileroot)))
		{	//저장하는 계좌 수 출력
			System.out.println(accounts.size() + "개의 계좌를 저장합니다...");
			
			//accounts : Set<Account> 자료형(데이터 타입) 객체 (제네릭 컬렉션)
			//Set<Account> : Set 타입 / 안에 들어가는 데이터는 Account 타입
			//out : ObjectOutputStream 객체
			//writeObject() : ()안의 객체를 파일에 저장
			out.writeObject(accounts);
			System.out.println("AccountInfo.obj 파일로 저장되었습니다.");
			
		} catch (IOException e) {
            System.out.println("파일저장 실패..");
			e.printStackTrace();
		}
	}
	
	//7.계좌정보 불러오기
	public static void loadAccFile(Scanner scan, Set<Account>accounts) {
		//불러올 파일 경로, 이름 지정
		String filename = "src/banking/file/AccountInfo.obj";
		
		//파일이 없을 경우, return으로 종료
		if (!new File(filename).exists()) {
			System.out.println("불러올 파일이 존재하지 않습니다.");
			return;
		}
		//파일을 읽어오기 위한 ObjectInputStream 스트림 객체 생성
		try (ObjectInputStream in = new ObjectInputStream
				(new FileInputStream(filename)))
		{	
			/*
			readObject()는 Object 타입을 리턴하기 때문에 안의 데이터가 어떤 타입인지 모름.
			따라서 (HashSet<Account>)처럼 강제형변환 필요.
			컴퓨터는 강제형변환 시, "unchecked 형변환" 경고를 띄움.(unchecked cast warning)
			경고를 무시하기 위해 @SuppressWarnings("unchecked") 작성
			 */
			@SuppressWarnings("unchecked")
			//byte형태의 객체를 Object타입으로 읽어온 뒤, HashSet<Account>으로 강제 형변환.
			//상위 인터페이스 타입인 Set<Account>타입의 loaded 변수에 저장.
			Set<Account> loaded = (HashSet<Account>) in.readObject();
			accounts.clear(); //기존내용 지우기
			accounts.addAll(loaded); //loaded 데이터 추가
			
			//불러온 계좌 수 출력 후, return으로 종료
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
