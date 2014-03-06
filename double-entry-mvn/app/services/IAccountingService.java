package services;
import java.util.Date;
import java.util.List;

import models.traits.IAccount;
import models.traits.ITransaction;


/**
 * Wrapper service around Account inceptions and the transaction activities
 * that they participate in.
 * 
 * The implementation of this service can be exposed as SOAP or any other form 
 * of consumable client service for other clients than REST and Web
 * The impl.service can be injected via spring or any other framework that integrates 
 * with play
 *
 */
public interface IAccountingService {
	  
	 public static enum Type { DEBIT, CREDIT };
	  
      /**
       *  
       * @return all accounts
       */
      List<IAccount> getAccountList();
      
      /**
       * @param startDate start date of range
       * @param endDate   end date of range
       * @return transaction activities from startDate to endDate range
       */
      List<ITransaction> getTransactionHistory(Date startDate, Date endDate);
      
      /**
       * 
       * @param startDate start date of range
       * @param endDate   end date of range
       * @param type
       * @return all transaction activities from startDate to endDate range for the specified type
       *         for all accounts
       */
      List<ITransaction> getTransactionHistory(Date startDate, Date endDate, Type type);
      
      
      /**
       * 
       * @param acctNum account number
       * @param startDate start date of range
       * @param endDate   end date of range
       * @return all transaction activities from startDate to endDate range for the specified type
       *         for account acctNum
       */
      List<ITransaction> getTransactionHistory(String acctNum, Date startDate, Date endDate);
      
      /**
       * 
       * @param acctNum account number
       * @param startDate start date of range
       * @param endDate   end date of range
       * @param type type of transaction
       * @return all transaction activities of the provided type from startDate to endDate range for the specified type
       *         for account acctNum
       */
      List<ITransaction> getTransactionHistory(String acctNum, Date startDate, Date endDate, Type type);
      
      /**
       * 
       * @return list of unpaired transaction - missing leg :) 
       */
      List<ITransaction> getUnpairedTransactions();
      
      
}
