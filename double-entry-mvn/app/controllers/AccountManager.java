package controllers;

import java.util.List;

import exceptions.DataAccessException;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import play.db.jpa.*;
import views.html.*;
import models.*;

/**
 * Manage accounts
 * 
 * For a web or REST client the corresponding domain layer is exposed as
 * resource represented by URIs
 * 
 * @see con/routes for the account resource api
 */
public class AccountManager extends Controller {

	/**
	 * This result directly redirect to application home.
	 */
	public static Result GO_HOME = redirect(routes.AccountManager.listAccounts(
			0, "name", "asc", ""));

	/**
	 * Handle default path requests, redirect to accounts listAccounts
	 */
	public static Result index() {
		return GO_HOME;
	}

	/**
	 * Display the paginated listAccounts of accounts.
	 * 
	 * @param page
	 *            Current page number (starts from 0)
	 * @param sortBy
	 *            Column to be sorted
	 * @param order
	 *            Sort order (either asc or desc)
	 * @param filter
	 *            Filter applied on account names
	 * 
	 */
	@Transactional(readOnly = true)
	public static Result listAccounts(int page, String sortBy, String order,
			String filter) {

		return ok(listAccounts.render(
				AccountManager.page(page, 10, sortBy, order, filter), sortBy,
				order, filter));
	}

	/**
	 * Display the 'new account form'.
	 */
	@Transactional(readOnly = true)
	public static Result create() {

		Form<Account> accountForm = form(Account.class);

		return ok(createAccount.render(accountForm));
	}

	/**
	 * 
	 * Handle the 'new account form' submission
	 */
	@Transactional
	public static Result save() {
		Form<Account> accountForm = form(Account.class).bindFromRequest();

		if (accountForm.hasErrors()) {
			return badRequest(createAccount.render(accountForm));
		}
		try {
			accountForm.get().save();
		} catch (DataAccessException de) {
			// TODO log de and do something more meaningful
			flash("Error", "Could not create " + accountForm.get().name
					+ " Check that it does not already exist");
			return GO_HOME;
		}

		flash("success", "Account " + accountForm.get().name
				+ " has been created");
		return GO_HOME;
	}

	// TODO refactor - Extract out a shared controller Page class utility via
	// generics

	/**
	 * Return a "page" of of a given list entity
	 * 
	 * @param page
	 *            Page to display
	 * @param pageSize
	 *            Number of accounts per page
	 * @param sortBy
	 *            Account property used for sorting
	 * @param order
	 *            Sort order (either or asc or desc)
	 * @param filter
	 *            Filter applied on the name column
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Page page(int page, int pageSize, String sortBy,
			String order, String filter) {
		if (page < 1)
			page = 1;
		Long total = (Long) JPA
				.em()
				.createQuery(
						"select count(c) from Account c where lower(c.name) like ?")
				.setParameter(1, "%" + filter.toLowerCase() + "%")
				.getSingleResult();
		List<Account> data = JPA
				.em()
				.createQuery(
						"from Account c where lower(c.name) like ? order by c."
								+ sortBy + " " + order)
				.setParameter(1, "%" + filter.toLowerCase() + "%")
				.setFirstResult((page - 1) * pageSize).setMaxResults(pageSize)
				.getResultList();
		return new Page(data, total, page, pageSize);
	}

	/**
	 * Used to represent an accounts page.
	 */
	public static class Page {

		private final int pageSize;
		private final long totalRowCount;
		private final int pageIndex;
		private final List<Account> listAccounts;

		public Page(List<Account> data, long total, int page, int pageSize) {
			this.listAccounts = data;
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

		public List<Account> getList() {
			return listAccounts;
		}

		public boolean hasPrev() {
			return pageIndex > 1;
		}

		public boolean hasNext() {
			return (totalRowCount / pageSize) >= pageIndex;
		}

		public String getDisplayXtoYofZ() {
			int start = ((pageIndex - 1) * pageSize + 1);
			int end = start + Math.min(pageSize, listAccounts.size()) - 1;
			return start + " to " + end + " of " + totalRowCount;
		}

	}

}
