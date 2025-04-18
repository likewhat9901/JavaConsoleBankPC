package banking;

import banking.util.AccountUtil;

public class NormalAccount extends Account{
	private static final long serialVersionUID = 1L;
	
	private int interest;
	private int accType = 1;
	
	public NormalAccount(String accNum, String owner, int balance, 
			int interest) {
		super(accNum, owner, balance);
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
	public String getAccClass() {
		
		return "보통계좌";
	}
	
	@Override
	public int getAccType() {
		return accType;
	}

	@Override
	public void showAccInfo() {
		System.out.println("-------------");
		System.out.println("계좌종류: "+ getAccClass());
		super.showAccInfo();
		System.out.println("기본이자: "+ interest + "%");
		System.out.println("-------------");
	}
	
	public void CallSuperShow() {
		super.showAccInfo();
	}
	
	@Override
	public void deposit(int d_money) {
		int int_money = (int) (balance*interest/100.0);
		balance = balance + int_money + d_money;
		System.out.printf("입금이 완료되었습니다. 잔액: %d원 (이자: +%d원)%n", balance, int_money);
	}
	
	@Override
	public void withdraw(int w_money) {
		balance = balance - w_money;
		System.out.println("출금이 완료되었습니다. 잔액: " + balance);
	}
	
	@Override
	public void createAcc() {
        String accNum = AccountUtil.inputLine(AccountManager.scan, "계좌번호: ");
        String name = AccountUtil.inputLine(AccountManager.scan, "고객이름: ");
        int balance = Integer.parseInt(AccountUtil.inputLine(AccountManager.scan, "잔고: "));
        int intRate = Integer.parseInt(AccountUtil.inputLine(AccountManager.scan, "기본이자%(정수형태로입력): "));
        
        
		
	}
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	

}
