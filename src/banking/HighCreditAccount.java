package banking;

public class HighCreditAccount extends Account{

	private int interest;
	private String creditGrade;
	
	public HighCreditAccount(String accNum, String owner, int balance, int interest, String creditGrade) {
		super(accNum, owner, balance);
		this.interest = interest;
		this.creditGrade = creditGrade;
	}

	@Override
	public void showAccInfo() {
		super.showAccInfo();
		System.out.println("기본이자: "+ interest + "%");
		System.out.println("신용등급(A,B,C등급): "+ creditGrade);
		System.out.println("-------------");
	}

	

}
