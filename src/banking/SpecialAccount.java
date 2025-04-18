package banking;

public class SpecialAccount extends NormalAccount{
	private static final long serialVersionUID = 1L;

	private int DepositCnt = 0;
	private int specialMoney = 500;
	private int accType = 3;
	
	public SpecialAccount(String accNum, String owner, int balance, int interest, int DepositCnt) {
		super(accNum, owner, balance, interest);
		this.DepositCnt = DepositCnt;
	}
	
	@Override
	public String toString() {
		return "[특판계좌] " + super.toString() + ", 기본이자=" + super.getInterest() + "%" + "]";
	}

	@Override
	public int getAccType() {
		return accType;
	}
	
	@Override
	public String getAccClass() {
		
		return "특판계좌";
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
		System.out.println("-------------");
		System.out.println("계좌종류: "+ getAccClass());
		super.CallSuperShow();
		System.out.println("기본이자: "+ super.getInterest() + "%");
		System.out.println("누적 입금횟수(짝수번째 축하금 지급): "+ DepositCnt + "번 입금완료");
		System.out.println("-------------");
	}
	
	@Override
	public void deposit(int d_money) {
		int int_money = (int) (balance*super.getInterest()/100.0);
		DepositCnt++;
		
		if(DepositCnt%2 == 1) {
			balance = balance + int_money + d_money;
			System.out.printf("입금이 완료되었습니다. 잔액: %d원 (이자: +%d원)%n", balance, int_money);
		} else {
			balance = balance + int_money + d_money + specialMoney;
			System.out.printf("입금이 완료되었습니다. 잔액: %d원 (이자: +%d원)(축하금: +%d원)%n", 
					balance, int_money, specialMoney);
		}
	}
	
	@Override
	public void withdraw(int w_money) {
		balance = balance - w_money;
		System.out.println("출금이 완료되었습니다. 잔액: " + balance);
	}
	
}
