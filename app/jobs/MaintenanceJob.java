package jobs;

import models.DnsEntry;
import models.Domain;
import models.Ip;
import models.ResourceRecord;

public class MaintenanceJob implements Runnable {

	@Override
	public void run() {
		for(Domain domain : Domain.Find.all()) {
			Ip.getOrCrate(domain.ip);
			for(DnsEntry entry : domain.dnsEntries) {
				Ip.getOrCrate(entry.actualIp6);
				Ip.getOrCrate(entry.actualIp);
				ResourceRecord.getOrCreateFromDNSEntry(entry);
			}
		}
	}

}
