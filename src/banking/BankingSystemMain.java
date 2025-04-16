package banking;


public class BankingSystemMain extends AccountManager{
	static AccountManager accMng = new AccountManager();

	public static void main(String[] args) {
		
		accMng.loadAccount();
		while (true) {
			accMng.showMenu();
		}
	}
}
