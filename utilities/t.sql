select * from iftb_trans_log

select * from iftb_msg_details

drop table iftb_msg_log_custom 

create table iftb_msg_log_custom 
(
internal_ref_no  varchar2(16), 
msg_in_time     timestamp,
msg_out_time    timestamp,
msg_status      varchar2(1),
external_system varchar2(50),
interface_code  varchar2(50),
rev_ref_no      varchar2(16),
id_frnno        varchar2(8),
id_frndat       varchar2(8),
id_chnid        varchar2(2),
id_frnseq       varchar2(6),
req_msg         clob,
resp_msg        clob
)

insert into iftb_msg_log_custom (msg_in_time) values (systimestamp)

select * from iftb_msg_log_custom 

create table t(x number)

insert into t values (1)

select * from t  for update

update t set x = 2

commit

rollback
