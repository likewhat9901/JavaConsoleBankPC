package banking.jdbc;

public class BankingSystemMain_JDBC {
	static AccountManager_JDBC accMng = new AccountManager_JDBC("banking", "1234");

	public static void main(String[] args) {

		while (true) {
			accMng.showMenu();
		}
	}

}
