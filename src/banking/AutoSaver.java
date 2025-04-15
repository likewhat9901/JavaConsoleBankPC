package banking;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

public class AutoSaver extends Thread{

	private Set<Account> accounts = new HashSet<Account>();
	
	public AutoSaver(Set<Account> accounts) {
		this.accounts = accounts;
	}
	
	@Override
	public void run() {
		String autoFileName = "src/banking/file/AccountInfo.obj";
		for (int i = 0; i < 100; i++) {
			try (ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(autoFileName))) {
				
				System.out.println("\n" + accounts.size() + "개의 계좌를 저장합니다...");
	            out.writeObject(accounts);
				System.out.println("자동저장 완료");
				
				sleep(5000);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
