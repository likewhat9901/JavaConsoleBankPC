package banking;


public class BankingSystemMain extends AccountManager{

	
	
	public static void main(String[] args) {

		AccountManager accMng = new AccountManager();
		
		accMng.loadAccount();
		while (true) {
			
			accMng.showMenu();
		}
		
	}

}
