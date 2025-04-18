package banking;


public class BankingSystemMain extends AccountManager{

	public static void main(String[] args) {
		//AccountManager 객체 생성
		AccountManager accMng = new AccountManager();
		
		//최초실행 시, 계좌파일 읽어옴
		accMng.loadAccount();
		//showMenu 반복. 프로그램 종료로만 빠져나올 수 있음.
		while (true) {
			accMng.showMenu();
		}
	}
}
