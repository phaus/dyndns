package controllers;

import java.util.List;

import controllers.routes;
import models.Account;
import models.DnsEntry;
import models.Domain;
import fileauth.actions.BasicAuth;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.Admin.*;

@BasicAuth
public class AdminDynDNS extends Application {
	public static Result index() {
		Account account = Account.geAccountOrCreate(request().username());
		if(!account.isAdmin()) {
			return forbidden();
		}
		List<DnsEntry> entries = DnsEntry.find.all();
		List<Account> accounts = Account.find.all();
		return ok(index.render(entries, accounts));
	}

	public static Result update(Long id) {
		Account account = Account.geAccountOrCreate(request().username());
		if(!account.isAdmin()) {
			return forbidden();
		}
		Form<DnsEntry> entryForm = Form.form(DnsEntry.class).bindFromRequest();
		DnsEntry entry = entryForm.get();
		entry.updated = null;
		entry.update(id);
		return redirect(routes.AdminDynDNS.index());
	}

	public static Result delete(Long id) {
		Account account = Account.geAccountOrCreate(request().username());
		if(!account.isAdmin()) {
			return forbidden();
		}
		DnsEntry entry = DnsEntry.find.byId(id);
		if (entry != null) {
			Domain domain = Domain.find.byId(entry.domain.id);
			entry.delete();
			domain.forceUpdate = true;
			domain.save();
		}
		return redirect(routes.AdminDynDNS.index());
	}

	public static Result resetApiKey(Long id) {
		Account account = Account.geAccountOrCreate(request().username());
		if(!account.isAdmin()) {
			return forbidden();
		}
		DnsEntry entry = DnsEntry.find.byId(id);
		if (entry != null) {
			entry.apiKey = DnsEntry.generateApiKey();
			entry.update();
		}
		return redirect(routes.AdminDynDNS.index());
	}
}
