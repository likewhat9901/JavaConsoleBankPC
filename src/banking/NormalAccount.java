package banking;

public class NormalAccount extends Account{

	private int interest;
	
	public NormalAccount(int accType, String accNum, String owner, int balance, int interest) {
		super(accType, accNum, owner, balance);
		this.interest = interest;
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
	
	

}
