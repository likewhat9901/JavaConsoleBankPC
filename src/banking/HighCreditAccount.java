package banking;

public class HighCreditAccount extends Account{
	private static final long serialVersionUID = 1L;
	
	private int interest;
	private String creditGrade;
	
	public HighCreditAccount(int accType, String accNum, String owner, int balance,
					int interest, String creditGrade) {
		super(accType, accNum, owner, balance);
		this.interest = interest;
		this.creditGrade = creditGrade;
	}

	@Override
	public String toString() {
		return "[신용신뢰계좌] " + super.toString() + ", 기본이자=" + interest 
				+ "%" + ", 추가이자=" + getCreditIntereset() + "%" + "]";
	}

	public int getInterest() {
		return interest;
	}

	public int getCreditIntereset() {
		switch (creditGrade) {
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
		switch (creditGrade) {
		case "A":
			return creditGrade + "등급" + "(추가이자: 7%)";
		case "B":
			return creditGrade + "등급" + "(추가이자: 4%)";
		case "C":
			return creditGrade + "등급" + "(추가이자: 2%)";
		default:
			return "A,B,C등급이 아님";
		}
	}

	@Override
	public void showAccInfo() {
		super.showAccInfo();
		System.out.println("기본이자: "+ interest + "%");
		System.out.println("신용등급: "+ getCreditGrade());
		System.out.println("-------------");
	}

	

}
