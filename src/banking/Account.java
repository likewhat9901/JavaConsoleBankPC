package banking;

import java.io.Serializable;
import java.util.Objects;

public abstract class Account implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected String accNum;
	protected String owner;
	protected int accType ,balance;
	
	public Account(int accType, String accNum, String owner, int balance) {
		super();
		this.accType = accType;
		this.accNum = accNum;
		this.owner = owner;
		this.balance = balance;
	}
	
	@Override
	public String toString() {
		return "Account [계좌번호=" + accNum + "]";
	}

	public void showAccInfo() {
		System.out.println("-------------");
		System.out.println("계좌종류(1:보통계좌 / 2:신용신뢰계좌): "+ accType);
		System.out.println("계좌번호: "+ accNum);
		System.out.println("고객이름: "+ owner);
		System.out.println("잔고: "+ balance + "원");
	}

	public int getAccType() {
		return accType;
	}

	public String getAccNum() {
		return accNum;
	}

	public int getBalance() {
		return balance;
	}
	
	public void setBalance(int balance) {
		this.balance = balance;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accNum);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) {
			System.out.println("클래스 다름");
			return false;
		}
		Account other = (Account) obj;
		
		boolean isEqual = Objects.equals(accNum, other.accNum);
//		if (isEqual) System.out.println("accNum 중복됨:" + accNum);
		
		return isEqual;
	}
}
