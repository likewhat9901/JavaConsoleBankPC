package banking;

import java.util.Objects;

public abstract class Account {
	
	protected String accNum;
	protected String owner;
	protected int balance;
	
	public Account(String accNum, String owner, int balance) {
		super();
		this.accNum = accNum;
		this.owner = owner;
		this.balance = balance;
	}
	
	public void showAccInfo() {
		System.out.println("-------------");
		System.out.println("계좌번호: "+ accNum);
		System.out.println("고객이름: "+ owner);
		System.out.println("잔고: "+ balance);
	}

	@Override
	public int hashCode() {
		return Objects.hash(accNum);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		return Objects.equals(accNum, other.accNum);
	}
}
