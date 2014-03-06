package models;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import models.traits.IAccount;
import play.data.validation.Constraints;
import play.db.jpa.JPA;
import exceptions.DataAccessException;

/**
 * Account entity For this exercise and for the sake of keeping this play
 * example simple to follow other potential properties such as accountNumber and
 * such have been omitted from this base Account object
 * 
 * For this demo account names are unique across all types
 * 
 */
@Entity
@SequenceGenerator(name = "account_seq", sequenceName = "account_seq")
public class Account implements IAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
	public Long id;

	@Column(unique = true)
	@Constraints.Required
	public String name;

	@Constraints.Required
	public double value;

	@Constraints.Required
	@ManyToOne(cascade = CascadeType.MERGE)
	public AccountType accountType;

	/**
	 * Find an account by id.
	 */
	public static Account findById(Long id) throws DataAccessException {
		return JPA.em().find(Account.class, id);
	}

	/**
	 * Update this account.
	 */
	public void update(Long id) throws DataAccessException {
		if (this.accountType.id == null) {
			this.accountType = null;
		} else {
			this.accountType = AccountType.findById(accountType.id);
		}
		this.id = id;
		JPA.em().merge(this);
	}

	/**
	 * persist to store this new account.
	 */
	public void save() throws DataAccessException {
		if (this.accountType.id == null) {
			this.accountType = null;
		} else {
			this.accountType = AccountType.findById(accountType.id);
		}
		JPA.em().persist(this);
	}

	/**
	 * Delete this account.
	 */
	public void delete() {
		JPA.em().remove(this);
	}

	@Override
	public double getCurrentValue() {
		return value;
	}

	/**
	 * Subtracts value amount from the current value of this account
	 * 
	 * @param value
	 * @param description
	 *            description associated with this transaction
	 * @throws DataAccessException
	 */
	public void debitAccount(double amount) throws DataAccessException {
		
		value = value - amount;
		save();	
	}

	/**
	 * Adds value amount to the current value of this account
	 * 
	 * @param value
	 * @param description
	 *            description associated with this transaction
	 * @throws DataAccessException
	 */
	public void creditAccount(double amount) throws DataAccessException {
		value = value + amount;
		save();
	}

}
