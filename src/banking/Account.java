package banking;

import java.io.Serializable;
import java.util.Objects;

public abstract class Account implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected String accNum;
	protected String owner;
	protected int accType ,balance;
	
	public Account(int accType, String accNum, String owner, int balance) {
		this.accType = accType;
		this.accNum = accNum;
		this.owner = owner;
		this.balance = balance;
	}
	
	@Override
	public String toString() {
		return " [계좌번호=" + accNum + ", 이름=" + owner 
				+ ", 잔고=" + balance + "원";
	}
	
	public void showAccInfo() {
		System.out.println("-------------");
		System.out.println("계좌종류: "+ accTypeName());
		System.out.println("계좌번호: "+ accNum);
		System.out.println("고객이름: "+ owner);
		System.out.println("잔고: "+ balance + "원");
	}
	
	public String accTypeName() {
		switch (accType) {
		case 1:
			return "보통계좌";
		case 2: 
			return "신용신뢰계좌";
		case 3:
			return "특판계좌";
		default:
			return null;
		}
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
		if (!(obj instanceof Account)) {
			System.out.println("클래스 다름");
			return false;
		}
		Account other = (Account) obj;
		
		boolean isEqual = Objects.equals(accNum, other.accNum);
//		if (isEqual) System.out.println("accNum 중복됨:" + accNum);
		
		return isEqual;
	}
}
