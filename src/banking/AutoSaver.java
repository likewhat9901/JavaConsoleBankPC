package banking;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class AutoSaver extends Thread{

	private Set<Account> accounts = new HashSet<Account>();
	
	//accounts HashSet을 매개변수로 받아서 this.accounts로 저장.
	public AutoSaver(Set<Account> accounts) {
		this.accounts = accounts;
	}
	
	@Override
	public void run() {
		String autoFileName = "src/banking/file/AutoSaveAccount.txt";
		
		File file = new File(autoFileName);
		
		while (true) {
			if (file.exists()) {
				file.delete(); 
			} 
			
			try (PrintWriter printOut = new PrintWriter(
					new FileWriter(autoFileName))) 
			{	
			    for (Account acc : accounts) {
			        printOut.println(acc.toString()); // toString을 재정의해두면 깔끔함
			    }
				System.out.println("\n" + accounts.size() + "개의 계좌를 저장합니다...");
				System.out.println("계좌정보가 [AutoSaveAccount.txt] 파일로 자동저장되었습니다.");
				
				sleep(5000);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			} catch (InterruptedException e) {
				System.out.println(e);
				System.out.println("자동저장을 종료합니다.");
				return;
			}
		}
	}
	
}
