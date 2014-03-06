package exceptions;

/**
 * Used by the Accounting service
 */
@SuppressWarnings("serial")
public class AccountingException extends Exception {
	/**
	 * Instantiates a new bussiness exception.
	 */
	public AccountingException() {
	}

	/**
	 * Instantiates a new bussiness exception.
	 * 
	 * @param message
	 *            the message
	 */
	public AccountingException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new bussiness exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public AccountingException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new bussiness exception.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public AccountingException(String message, Throwable cause) {
		super(message, cause);
	}

}
