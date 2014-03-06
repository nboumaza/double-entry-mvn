package models.traits;

import java.util.Date;
import java.util.List;

import models.Transaction;
import models.Account;

public interface ITransaction {
	public static enum Type {
		DEBIT, CREDIT
	};

	/**
	 * @param beginDate
	 * @param endDate
	 * @return transaction list which occurred between begin and end dates
	 */
	List<Transaction> getTransactionHistory(Date beginDate, Date endDate);

	/**
	 * 
	 * @param accountNumber
	 *            unique account number
	 * @return list of transaction (debit and credit) that this account
	 *         participated in
	 */
	List<Transaction> fetchAccountTransactions(String accountNumber);

	/**
	 * 
	 * @param accountNumber
	 * @param type
	 * @return return the list of transaction where this account participated in
	 *         as the transaction "type" side
	 */
	List<Transaction> fetchAccountTransactions(String accountNumber, Type type);

	/**
	 * Creates a transaction that "posts" immediately according to the following
	 * accounting rules: 1- fromAccount is debited by the specified value amount
	 * 2- toAccount is credited with the specified value amount 3- a new
	 * bookkeeping entry is persisted for potential reconciliation or audit
	 * 
	 * @param fromAccount
	 * @param toAccount
	 * @param amount
	 * @param desc
	 * @return true if the transfer is successful, false otherwise
	 */
	boolean processFundTransfer(Account fromAccount, Account toAccount,
			double amount);
}
