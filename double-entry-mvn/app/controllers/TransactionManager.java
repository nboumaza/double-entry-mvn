package controllers;

import java.util.Date;
import java.util.List;

import controllers.AccountManager.Page;
import exceptions.DataAccessException;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import play.db.jpa.*;
import views.html.*;
import models.*;

/**
 * Manage account transaction resource
 * 
 * For a web or REST client the corresponding domain layer is exposed 
 * as resource represented by URIs
 * @see con/routes for the account resource api
 */  
public class TransactionManager extends Controller {

	/**
	 * This result directly redirect to application home.
	 */
	public static Result GO_HOME = redirect(routes.TransactionManager
			.listTransactions(0, "description", "asc", ""));

	/**
	 * Defines a form wrapping a Transaction class.
	 */
	final static Form<Transaction> transactionForm = form(Transaction.class);

	/**
	 * Display the paginated list of account Transactions.
	 * 
	 * @param page
	 *            Current page number (starts from 0)
	 * @param sortBy
	 *            Column to be sorted
	 * @param order
	 *            Sort order (either asc or desc)
	 * @param filter
	 *            Filter applied on account names
	 */
	@Transactional(readOnly = true)
	public static Result listTransactions(int page, String sortBy,
			String order, String filter) {
		
		return ok(listTransactions.render(
				  TransactionManager.page(page, sortBy, order, filter), sortBy, order,
				filter));
		
	}
	
	/**
	 * retrieves the transaction activities associated 
	 * with a given account id
	 * 
	 * @param id
	 *            : account id
	 * @param list
	 *            of transaction entries
	 */
	@Transactional(readOnly = true)
	public static Result listAccountTransactions(Long id) {
		//TODO
		return ok(); 
	}

	/**
	 * Display the 'new transaction form'.
	 */
	@Transactional(readOnly = true)
	public static Result create() {
		
		Form<Transaction> transactionForm = form(Transaction.class);			
		
		return ok(createTransaction.render(transactionForm));
	}

	/**
	 * Handle the 'new transaction form' submission action, i.e
	 * 
	 * Creates a transaction that "posts" immediately according to the following
	 * accounting rules:
	 * 
	 * 1- fromAccount is debited by the specified value amount
	 * 2- toAccount is credited with the specified value amount
	 * 3- a new bookkeeping entry is persisted for potential reconciliation or audit
	 * 
	 * This method is relative to an action transaction context - Alternatively spring
	 * transaction can be used. 
	 * 
	 */
	@Transactional
	public static Result save() {
		Form<Transaction> transactionForm = form(Transaction.class)
				.bindFromRequest();

		if (transactionForm.hasErrors()) {
			return badRequest(createTransaction.render(transactionForm));
		}
		
		Transaction transaction = transactionForm.get();
		transaction.date = new Date();
		
		if (!transaction.processFundTransfer(transaction.fromAccount, 
					transaction.toAccount, transaction.value) )
		{
			flash("Error", "Could not create a new transaction" );
			return GO_HOME;
		}
		
		flash("success", "Transaction " + transactionForm.get().id
				+ " has been created");
		return GO_HOME;
	}

	// TODO refactor - Extract out a shared controller Page class utility via
	// generics

	/**
	 * Return a page of transactions
	 * 
	 * @param page
	 *            Page to display
	 * @param pageSize
	 *            Number of transactions per page
	 * @param sortBy
	 *            Account property used for sorting
	 * @param order
	 *            Sort order (either or asc or desc)
	 * @param filter
	 *            Filter applied on the name column
	 */
	public static Page page(int page, String sortBy, String order, String filter) {
		if (page < 1)
			page = 1;
		int pageSize = 10;
		Long total = (Long) JPA.em()
				.createQuery("select count(c) from Transaction c")			
				.getSingleResult();
		@SuppressWarnings("unchecked")
		List<Transaction> data = JPA.em().createQuery("from Transaction ")

				.setFirstResult((page - 1) * pageSize).setMaxResults(pageSize)
				.getResultList();
		return new Page(data, total, page, pageSize);
	}

	/**
	 * Used to represent account transactions page.
	 */
	public static class Page {

		private final int pageSize;
		private final long totalRowCount;
		private final int pageIndex;
		private final List<Transaction> listTransactions;

		public Page(List<Transaction> data, long total, int page, int pageSize) {
			this.listTransactions = data;
			this.totalRowCount = total;
			this.pageIndex = page;
			this.pageSize = pageSize;
		}

		public long getTotalRowCount() {
			return totalRowCount;
		}

		public int getPageIndex() {
			return pageIndex;
		}

		public List<Transaction> getList() {
			return listTransactions;
		}

		public boolean hasPrev() {
			return pageIndex > 1;
		}

		public boolean hasNext() {
			return (totalRowCount / pageSize) >= pageIndex;
		}

		public String getDisplayXtoYofZ() {
			int start = ((pageIndex - 1) * pageSize + 1);
			int end = start + Math.min(pageSize, listTransactions.size()) - 1;
			return start + " to " + end + " of " + totalRowCount;
		}

	}

}
