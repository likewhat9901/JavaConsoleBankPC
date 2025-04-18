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
