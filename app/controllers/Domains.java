package controllers;

import java.util.List;

import fileauth.actions.BasicAuth;
import models.Account;
import models.DnsEntry;
import models.Domain;
import models.SubDomain;
import play.Logger;
import static play.data.Form.*;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.dyndns.*;

@BasicAuth
public class Domains extends Application {
	final static Form<DnsEntry> ENTRY_FORM = form(DnsEntry.class);

	public static Result index() {
		Account account = Account.geAccountOrCreate(request().username());
		List<DnsEntry> entries = DnsEntry.find.where().eq("account", account).eq("toDelete", false).findList();
		return ok(index.render(entries, ENTRY_FORM));
	}

	public static Result delete(Long id){
		DnsEntry entry = DnsEntry.find.byId(id);
		if(entry != null){
			entry.markToDelete();
			entry.save();
		}
		return redirect(routes.Domains.index());		
	}
	
	public static Result save() {
		Form<DnsEntry> entryForm = ENTRY_FORM.bindFromRequest();
		Account account = Account.geAccountOrCreate(request().username());
		if (entryForm.hasErrors()) {
			List<DnsEntry> entries = account.dnsEntries;
			return ok(index.render(entries, entryForm));
		} else {
			DnsEntry entry = entryForm.get();
			if (entry != null && !DnsEntry.exists(entry) && entry.checkName()) {
				SubDomain sd = SubDomain.find.byId(entry.subDomain.id);
				entry.domain = Domain.find.byId(sd.domain.id);				
				entry.save();
			}
			return redirect(routes.Domains.index());
		}
	}
}
