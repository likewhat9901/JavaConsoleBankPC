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
import banking.SpecialAccount;

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
		
		for (Account acc : accounts) {
			if (acc.getAccNum().equals(accNum)) {
				return acc;
			}
		}
		
		return null;
	}
	
	public static int selectAccType(Scanner scan) {
		System.out.println("\n-----계좌선택------");
        System.out.println("1.보통계좌 / 2.신용신뢰계좌");
        System.out.print("선택: ");
        int accType = Integer.parseInt(scan.nextLine());
        System.out.println("----------------");
        
        //1,2 이외의 값 입력시 메뉴복귀
        if(!(accType == 1  || accType == 2)) {
			System.out.println("1 또는 2만 입력가능합니다.");
			return 0;
        }
        //(보통계좌 선택 시)특판계좌 가입여부
        if(accType == 1) {
			while (true) {
				String answer = AccountUtil.inputLine(scan, "특판계좌 상품에 가입하시겠습니까?(y or n): ");
				switch (answer.toUpperCase()) {
				//Y 입력시, accType 3으로 변경
				case "Y":
					accType = 3;
					return accType;
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
        return accType;
	}
	
	public static Account createNewAcc(Scanner scan, int accType) {
        //2.계좌개설 준비
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
			newAcc = new SpecialAccount(accNum, name, balance, intRate);
			break;
		default:
        	System.out.println("[계좌생성 예외발생]");
        	return null;
		}
        
		return newAcc;
	}
	
	public static void saveNewAcc(Scanner scan, Account newAcc, Set<Account> accounts) {
        //3.실제계좌 개설 (HashSet에 계좌저장 & 중복계좌 처리)
        if (accounts.add(newAcc)) {
        	System.out.println("계좌개설 성공!");
        	return;
        }
        //add가 false를 반환했을 경우(추가되지 않음)
        else {
			while (true) {
				//이미 if 조건문 안에서 add 실행됨.(1번째 추가시도)
				//add가 true를 반환했을 경우(정상적으로 추가됨)
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
				System.out.println("[입금조건 예외발생]");
			}
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
					String answer = inputLine(scan, "잔고가 부족합니다. 금액전체를 출금할까요?(y / n): ").trim();
					
					//입력한 출금액을 잔액으로 치환하고 출금액 반환.
					if (answer.equalsIgnoreCase("Y")) {
						withdraw = acc.getBalance();
						return withdraw;
					} 
					//출금액 다시 입력. while문 처음부터 다시
					else if(answer.equalsIgnoreCase("N")) {
						continue;
					} 
					//잘못된 입력. while문 처음부터 다시
					else {
						System.out.println("y or n를 입력하세요.");
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
				System.out.println("[withdrawCheck] 숫자를 입력하세요.\n");
			}
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
			System.out.println("파일을 찾을 수 없습니다.");
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
	
}
