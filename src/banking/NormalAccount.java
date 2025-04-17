package banking;

public class NormalAccount extends Account{
	private static final long serialVersionUID = 1L;
	
	private int interest;
	
	public NormalAccount(int accType, String accNum, String owner, int balance, 
			int interest) {
		super(accType, accNum, owner, balance);
		this.interest = interest;
	}

	@Override
	public String toString() {
		return "[보통계좌] " + super.toString() + ", 기본이자=" + interest + "%" + "]";
	}
	
	public int getInterest() {
		return interest;
	}
	


	@Override
	public void showAccInfo() {
		super.showAccInfo();
		System.out.println("기본이자: "+ interest + "%");
		System.out.println("-------------");
	}
	
	public void CallSuperShow() {
		super.showAccInfo();
	}
	

}
