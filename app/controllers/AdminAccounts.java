package controllers;

import java.util.List;

import fileauth.actions.BasicAuth;
import models.Account;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.AdminAccounts.index;

@BasicAuth
public class AdminAccounts extends Controller {
	
	public static Result index() {
		Account account = Account.geAccountOrCreate(request().username());
		if(!account.isAdmin()) {
			return forbidden();
		}		
		List<Account> accounts = Account.Find.all();
		return ok(index.render(accounts));
	}
	
}
