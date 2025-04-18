package banking;

import java.io.Serializable;
import java.util.Objects;

public abstract class Account implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected String accNum, owner;
	protected int balance;
	
	public Account(String accNum, String owner, int balance) {
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
		System.out.println("계좌번호: "+ accNum);
		System.out.println("고객이름: "+ owner);
		System.out.println("잔고: "+ balance + "원");
	}
	
	public String getAccClass() {
		return null;
	}
	
	public int getAccType() {
		
		return 0;
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
	
	public void deposit(int money) {
	}
	
	public int withdraw() {
		return 0;
	}
	
	public void createAcc() {
		
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
