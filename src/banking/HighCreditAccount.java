package banking;

public class HighCreditAccount extends Account{

	private int interest;
	private String creditGrade;
	
	public HighCreditAccount(int accType, String accNum, String owner, int balance,
					int interest, String creditGrade) {
		super(accType, accNum, owner, balance);
		this.interest = interest;
		this.creditGrade = creditGrade;
	}

	public int getInterest() {
		switch (creditGrade) {
		case "A":
			return interest + 7;
		case "B":
			return interest + 4;
		case "C":
			return interest + 2;
		default:
			return interest;
		}
	}

	@Override
	public void showAccInfo() {
		super.showAccInfo();
		System.out.println("기본이자: "+ interest + "%");
		System.out.println("신용등급(A,B,C등급): "+ creditGrade + "등급");
		System.out.println("-------------");
	}

	

}
