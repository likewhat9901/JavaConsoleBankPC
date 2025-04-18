package banking;

public class HighCreditAccount extends Account{
	private static final long serialVersionUID = 1L;
	
	private int interest;
	private int accType = 2;
	private String creditGrade;
	
	public HighCreditAccount(String accNum, String owner, int balance,
					int interest, String creditGrade) {
		super(accNum, owner, balance);
		this.interest = interest;
		this.creditGrade = creditGrade;
	}

	@Override
	public String toString() {
		return "[신용신뢰계좌] " + super.toString() + ", 기본이자=" + interest 
				+ "%" + ", 추가이자=" + getCreditIntereset() + "%" + "]";
	}
	
	@Override
	public int getAccType() {
		return accType;
	}
	
	@Override
	public String getAccClass() {
		
		return "신용신뢰계좌";
	}

	public int getInterest() {
		return interest;
	}

	public int getCreditIntereset() {
		switch (creditGrade.toUpperCase()) {
		case "A":
			return 7;
		case "B":
			return 4;
		case "C":
			return 2;
		default:
			return 0;
		}
	}

	public String getCreditGrade() {
		switch (creditGrade.toUpperCase()) {
		case "A":
			return creditGrade.toUpperCase() + "등급 (추가이자: 7%)";
		case "B":
			return creditGrade.toUpperCase() + "등급 (추가이자: 4%)";
		case "C":
			return creditGrade.toUpperCase() + "등급 (추가이자: 2%)";
		default:
			return "A,B,C등급이 아님";
		}
	}

	@Override
	public void showAccInfo() {
		System.out.println("-------------");
		System.out.println("계좌종류: "+ getAccClass());
		super.showAccInfo();
		System.out.println("기본이자: "+ interest + "%");
		System.out.println("신용등급: "+ getCreditGrade());
		System.out.println("-------------");
	}
	
	@Override
	public void deposit(int d_money) {
		int int_money = (int) (balance*(interest + getCreditIntereset())/100.0);
		balance = balance + int_money + d_money;
		System.out.printf("입금이 완료되었습니다. 잔액: %d원 (이자: +%d원)%n", balance, int_money);
	}
	
	@Override
	public void withdraw(int w_money) {
		balance = balance - w_money;
		System.out.println("출금이 완료되었습니다. 잔액: " + balance);
	}

	

}
