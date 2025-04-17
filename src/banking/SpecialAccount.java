package banking;

public class SpecialAccount extends NormalAccount{
	private static final long serialVersionUID = 1L;

	private int DepositCnt = 0;
	private int specialMoney = 500;
	
	public SpecialAccount(int accType, String accNum, String owner, int balance, 
			int interest, int DepositCnt) {
		super(accType, accNum, owner, balance, interest);
		this.DepositCnt = DepositCnt;
	}
	
	@Override
	public String toString() {
		return "[특판계좌] " + super.toString() + ", 기본이자=" + super.getInterest() + "%" + "]";
	}

	public int getDepositCnt() {
		return DepositCnt;
	}


	public void setDepositCnt(int depositCnt) {
		DepositCnt = depositCnt;
	}


	public int getSpecialMoney() {
		return specialMoney;
	}

	@Override
	public void showAccInfo() {
		super.CallSuperShow();
		System.out.println("기본이자: "+ super.getInterest() + "%");
		System.out.println("누적 입금횟수(짝수번째 축하금 지급): "+ DepositCnt + "번 입금완료");
		System.out.println("-------------");
	}
	
}
