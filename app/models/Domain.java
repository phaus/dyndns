package models;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.db.ebean.Model;

@Entity
public class Domain extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	public String name;

	public String hostmaster;

	public String ip;

	public String context;
	
	@Column(columnDefinition = "TEXT")
	public String nameservers;
	
	public Boolean forceUpdate = false;

	@OneToMany(mappedBy = "domain")
	public List<DnsEntry> dnsEntries;

	@OneToMany(mappedBy = "domain")
	public List<ResourceRecord> resourceRecords;
	
	public static Finder<Long, Domain> Find = new Finder<Long, Domain>(Long.class, Domain.class);

	public Set<DnsEntry> findNeedsToChanged() {
		Set<DnsEntry> entries = new HashSet<DnsEntry>(); 
		for(DnsEntry entry : DnsEntry.Find.where().isNotNull("updatedIp").isNull("updated").eq("domain", this).findSet()) {
			entries.add(entry);
		}		
		for(DnsEntry entry : DnsEntry.Find.where().isNotNull("updatedIp6").isNull("updated").eq("domain", this).findSet()) {
			entries.add(entry);
		}
		Logger.info("found " + entries.size() + " Entries to update!");
		return entries;
	}
	
	public Set<DnsEntry> findValidEntries() {
		Set<DnsEntry> entries = new HashSet<DnsEntry>(); 
		for(DnsEntry entry : DnsEntry.Find.where().isNotNull("updatedIp").eq("domain", this).findSet()) {
			entries.add(entry);
		}
		for(DnsEntry entry : DnsEntry.Find.where().isNotNull("updatedIp6").eq("domain", this).findSet()) {
			entries.add(entry);
		}
		Logger.info("found " + entries.size() + " valid Entries!");
		return entries;
	}

	public List<ResourceRecord> getResourceRecords() {
		return ResourceRecord.Find.where().eq("domain", this).order("name DESC").order("value DESC").findList();
	}
	
	public void setNameservers(String... nameservers) {
		StringBuilder sb = new StringBuilder();
		for(String nameserver : nameservers) {
			sb.append(nameserver).append("\n");
		}
		this.nameservers = sb.toString();
	}
	
	public String[] getNameservers() {
		return StringUtils.split(this.nameservers, "\n");
	}
	
	public String toString() {
		return name;
	}

	public static Map<String, String> optionsFor(Account account) {
		LinkedHashMap<String, String> domains = new LinkedHashMap<String, String>();
		for (Domain d : Find.all()) {
			domains.put(d.id.toString(), d.name);
		}
		return domains;
	}

}
