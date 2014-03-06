package models;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.*;

import models.traits.ITransaction;
import exceptions.DataAccessException;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

/**
 * Transaction is the class representing a Debit and Credit sides of funds
 * transfer between accounts.
 * 
 */
@Entity
@SequenceGenerator(name = "trx_seq", sequenceName = "trx_seq")
public class Transaction implements ITransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trx_seq")
	public Long id;

	@Formats.DateTime(pattern = "yyyy-MM-dd")
	public Date date;

	@Constraints.Required
	public String description;

	@Constraints.Required
	public double value;

	@Constraints.Required
	@OneToOne(cascade = CascadeType.ALL)
	public Account fromAccount;

	@Constraints.Required
	@OneToOne(cascade = CascadeType.ALL)
	public Account toAccount;

	/**
	 * Find by id.
	 */
	public static Transaction findById(Long id) throws DataAccessException {
		return JPA.em().find(Transaction.class, id);
	}

	/**
	 * Insert this new transaction.
	 */
	protected void save() throws DataAccessException {
		JPA.em().persist(this);
	}

	@Override
	@Transactional
	public boolean processFundTransfer(Account fromAccount, Account toAccount,
			double amount) {

		try {
			Account act1 = Account.findById(fromAccount.id);
			// debit amount
			act1.debitAccount(amount);

			Account act2 = Account.findById(toAccount.id);
			// credit amount
			act2.creditAccount(amount);

			// persist this transaction
			this.fromAccount = act1;
			this.toAccount = act2;
			save();

		} catch (DataAccessException e) {
			// TODO log and do something useful
			return false;
		}

		return true;
	}

	/*
	 * retrieve list of account names
	 */
	public static Map<String, String> options() {
		@SuppressWarnings("unchecked")
		List<Account> accounts = JPA.em()
				.createQuery("from Account order by name").getResultList();
		LinkedHashMap<String, String> options = new LinkedHashMap<String, String>();
		for (Account c : accounts) {
			options.put(c.id.toString(), c.name);
		}
		return options;
	}

	/**
	 * retrieve the list of transaction this account participated in (debit or credit)
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public static List<Transaction> fetchAccountTransactions(Long id)
			throws DataAccessException {
		@SuppressWarnings("unchecked")
		List<Transaction> data = JPA
				.em()
				.createQuery(
						"from Transaction c where c.fromAccount.id = ? or c.toAccount.id = ? ")
				.setParameter(1, id).setParameter(2, id).setMaxResults(10)
				.getResultList();
		return data;
	}

	@Override
	public List<Transaction> getTransactionHistory(Date beginDate, Date endDate) {
		// TODO
		return null;
	}

	@Override
	public List<Transaction> fetchAccountTransactions(String accountNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Transaction> fetchAccountTransactions(String accountNumber,
			Type type) {
		// TODO Auto-generated method stub
		return null;
	}

}
