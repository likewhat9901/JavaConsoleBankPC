package banking;

public class NormalAccount extends Account{
	private static final long serialVersionUID = 1L;
	
	private int interest, normDepositCnt;
	
	public NormalAccount(int accType, String accNum, String owner, int balance, 
			int interest, int normDepositCnt) {
		super(accType, accNum, owner, balance);
		this.interest = interest;
		this.normDepositCnt = normDepositCnt;
	}

	@Override
	public String toString() {
		return "[보통계좌] " + super.toString() + ", 기본이자=" + interest + "%" + "]";
	}
	
	public int getInterest() {
		return interest;
	}
	
	public int getDepositCount() {
		return normDepositCnt;
	}

	public void setDepositCount(int normDepositCnt) {
		this.normDepositCnt = normDepositCnt;
	}

	@Override
	public void showAccInfo() {
		super.showAccInfo();
		System.out.println("기본이자: "+ interest + "%");
		System.out.println("누적 입금횟수(짝수번째 축하금 지급): "+ normDepositCnt + "번 입금완료");
		System.out.println("-------------");
	}
	
	

}
