package models;

import java.util.*;

import javax.persistence.*;

import play.data.validation.*;
import play.db.jpa.*;

/**
 * Account types according to the US Accounting rules:
 * Liabilities, Asset, Revenue, Expenses and Equity... 
 * 
 * AccountType entity managed by JPA
 */
@Entity 
public class AccountType {

    @Id
    public Long id;
    
    @Constraints.Required
    public String name;
    
    public static AccountType findById(Long id) {
        return JPA.em().find(AccountType.class, id);
    }

    public static Map<String,String> options() {
        @SuppressWarnings("unchecked")
		List<AccountType> types = JPA.em().createQuery("from AccountType order by name").getResultList();
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(AccountType c: types) {
            options.put(c.id.toString(), c.name);
        }
        return options;
    }

}

