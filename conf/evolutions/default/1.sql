# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table account (
  id                        bigint auto_increment not null,
  username                  varchar(255),
  constraint pk_account primary key (id))
;

create table dns_entry (
  id                        bigint auto_increment not null,
  created                   datetime,
  changed                   datetime,
  updated                   datetime,
  updated6                  datetime,
  updated_ip                varchar(255),
  actual_ip                 varchar(255),
  updated_ip6               varchar(255),
  actual_ip6                varchar(255),
  name                      varchar(255),
  api_key                   varchar(255),
  to_delete                 tinyint(1) default 0,
  account_id                bigint,
  domain_id                 bigint,
  sub_domain_id             bigint,
  constraint pk_dns_entry primary key (id))
;

create table domain (
  id                        bigint auto_increment not null,
  created                   datetime,
  updated                   datetime,
  name                      varchar(255),
  soa_email                 varchar(255),
  soa_refresh               integer,
  soa_retry                 integer,
  soa_expire                integer,
  soa_ttl                   integer,
  soa_default               integer,
  ip                        varchar(255),
  ns_action                 varchar(255),
  nameservers               TEXT,
  system_ns                 varchar(255),
  www_include               tinyint(1) default 0,
  force_update              tinyint(1) default 0,
  domainsafe                tinyint(1) default 0,
  constraint pk_domain primary key (id))
;

create table ip (
  id                        bigint auto_increment not null,
  value                     varchar(255),
  type                      varchar(255),
  constraint pk_ip primary key (id))
;

create table resource_record (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  type                      varchar(255),
  value                     varchar(255),
  ttl                       integer,
  pref                      integer,
  domain_id                 bigint,
  constraint pk_resource_record primary key (id))
;

create table sub_domain (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  domain_id                 bigint,
  constraint pk_sub_domain primary key (id))
;

alter table dns_entry add constraint fk_dns_entry_account_1 foreign key (account_id) references account (id) on delete restrict on update restrict;
create index ix_dns_entry_account_1 on dns_entry (account_id);
alter table dns_entry add constraint fk_dns_entry_domain_2 foreign key (domain_id) references domain (id) on delete restrict on update restrict;
create index ix_dns_entry_domain_2 on dns_entry (domain_id);
alter table dns_entry add constraint fk_dns_entry_subDomain_3 foreign key (sub_domain_id) references sub_domain (id) on delete restrict on update restrict;
create index ix_dns_entry_subDomain_3 on dns_entry (sub_domain_id);
alter table resource_record add constraint fk_resource_record_domain_4 foreign key (domain_id) references domain (id) on delete restrict on update restrict;
create index ix_resource_record_domain_4 on resource_record (domain_id);
alter table sub_domain add constraint fk_sub_domain_domain_5 foreign key (domain_id) references domain (id) on delete restrict on update restrict;
create index ix_sub_domain_domain_5 on sub_domain (domain_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table account;

drop table dns_entry;

drop table domain;

drop table ip;

drop table resource_record;

drop table sub_domain;

SET FOREIGN_KEY_CHECKS=1;

