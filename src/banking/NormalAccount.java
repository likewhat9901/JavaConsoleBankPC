package banking;

public class NormalAccount extends Account{
	private static final long serialVersionUID = 1L;
	
	private int interest, depositCount;
	
	public NormalAccount(int accType, String accNum, String owner, int balance, int interest, int depositCount) {
		super(accType, accNum, owner, balance);
		this.interest = interest;
		this.depositCount = depositCount;
	}

	public int getInterest() {
		return interest;
	}

	@Override
	public String toString() {
		return "[보통계좌] " + super.toString() + ", 기본이자=" + interest + "%" + "]";
	}

	@Override
	public void showAccInfo() {
		super.showAccInfo();
		System.out.println("기본이자: "+ interest + "%");
		System.out.println("-------------");
	}
	
	

}
