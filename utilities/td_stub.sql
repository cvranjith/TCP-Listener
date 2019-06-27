declare
l_xml varchar2(32767):= '<?xml version="1.0" encoding="UTF-8"?>
<FCUBS_REQ_ENV xmlns="http://fcubs.iflex.com/service/FCUBSAccService">
    <FCUBS_HEADER>
       <SOURCE>MBCH</SOURCE>
       <UBSCOMP>FCUBS</UBSCOMP>
       <MSGID>'||to_char(sysdate,'YYDDDHH24MISS')||'</MSGID>
       <USERID>SYSTEM</USERID>
       <BRANCH>041</BRANCH>
       <MODULEID>CO</MODULEID>
       <FUNCTIONID>STDCUSAC</FUNCTIONID>
       <SERVICE>FCUBSAccService</SERVICE>
       <OPERATION>CreateCustAcc</OPERATION>
       <SOURCE_OPERATION>FLEXSERVICE</SOURCE_OPERATION>
       <SOURCE_USERID>SYSTEM</SOURCE_USERID>
       <ACTION>NEW</ACTION>
    </FCUBS_HEADER>
    <FCUBS_BODY>
       <CUST-ACCOUNT-IO>
          <BRN>041</BRN>
          <ACC>00000000000000000000</ACC>
          <CUSTNO>006105293</CUSTNO>
          <ACCLS>DD5003</ACCLS>
          <CCY>CNY</CCY>
          <ADESC>WEBSERVICE</ADESC>
          <ACCOPENDT>2015-01-08</ACCOPENDT>
          <TDPAYINDETAILS>
             <SEQNO>1</SEQNO>
             <OFFSACC>4103061059100042148</OFFSACC>
             <OFFBRN>041</OFFBRN>
             <PERCENTAGE>100</PERCENTAGE>
             <MMPAYOPT>S</MMPAYOPT>
          </TDPAYINDETAILS>
          <TDPAYOUTDETAILS>
             <SEQNO>1</SEQNO>
             <OFFSACC>4103061059100042148</OFFSACC>
             <OFFBRN>041</OFFBRN>
             <PERCENTAGE>100</PERCENTAGE>
          </TDPAYOUTDETAILS>
          <TDDETAILS>
             <TDAMT>1000.00</TDAMT>
          </TDDETAILS>
       </CUST-ACCOUNT-IO>
    </FCUBS_BODY>
</FCUBS_REQ_ENV>';
p_instr_xml varchar2(32767):=l_xml;
p_is_res_clob varchar2(1);
p_res_xml_str varchar2(32767);
p_res_xml_clob CLOB;
p_req_xml_clob CLOB;
l_msg_stat varchar2(1000);
l_acc      varchar2(1000);
l_ecode    varchar2(1000);
l_edesc    varchar2(1000);
l_res_xml  xmltype;
L_POS1      number;
L_POS2      number;
L_POS3      number;
L_NAMESPACE varchar2(1000);

begin
  debug.pr_close;
  global.pr_init ('041','SYSTEM');
  debug.Pr_Debug('AC','Before pr_process_req_msg');
  gwpks_msg_router.pr_process_req_msg  ('N'
                  ,l_xml
                  ,p_req_xml_clob       
                  ,p_instr_xml          
                  ,p_is_res_clob        
                  ,p_res_xml_str        
                  ,p_res_xml_clob
                  ); 
  debug.Pr_Debug('AC','After pr_process_req_msg');    
  debug.pr_cdebug('AC','p_res_xml_clob '||p_res_xml_clob); 
  debug.pr_cdebug('AC','p_res_xml_str '||p_res_xml_str); 
  if p_is_res_clob = 'Y'
  then
    l_res_xml := xmltype(p_res_xml_clob);
    L_POS1      := DBMS_LOB.INSTR(p_res_xml_clob, 'xmlns', 1, 1);
    L_POS2      := DBMS_LOB.INSTR(p_res_xml_clob, '"', L_POS1, 1);
    L_POS3      := DBMS_LOB.INSTR(p_res_xml_clob, '"', L_POS1, 2);
    L_NAMESPACE := DBMS_LOB.SUBSTR(p_res_xml_clob, L_POS3 - (L_POS2 + 1), L_POS2 + 1);
  else
    l_res_xml := xmltype(p_res_xml_str);
    L_POS1      := INSTR(p_res_xml_str, 'xmlns', 1, 1);
    L_POS2      := INSTR(p_res_xml_str, '"', L_POS1, 1);
    L_POS3      := INSTR(p_res_xml_str, '"', L_POS1, 2);
    L_NAMESPACE := SUBSTR(p_res_xml_str, L_POS2 + 1, L_POS3 - (L_POS2 + 1));
  end if;
  select extractvalue(l_res_xml,'/FCUBS_RES_ENV/FCUBS_HEADER/MSGSTAT','xmlns="'||L_NAMESPACE||'"'),
         extractvalue(l_res_xml,'/FCUBS_RES_ENV/FCUBS_BODY/CUST-ACCOUNT-FULL/ACC','xmlns="'||L_NAMESPACE||'"'),
         extractvalue(l_res_xml,'/FCUBS_RES_ENV/FCUBS_BODY/FCUBS_ERROR_RESP/ERROR[1]/ECODE','xmlns="'||L_NAMESPACE||'"'),
         extractvalue(l_res_xml,'/FCUBS_RES_ENV/FCUBS_BODY/FCUBS_ERROR_RESP/ERROR[1]/EDESC','xmlns="'||L_NAMESPACE||'"')
  into   l_msg_stat,
         l_acc,
         l_ecode,
         l_edesc
  from dual;
  
    
  debug.pr_debug('AC','p_is_res_clob = '||p_is_res_clob);
  debug.pr_debug('AC','MSGSTAT = '||l_msg_stat);
  debug.pr_debug('AC','l_acc = '||l_acc);
  debug.pr_debug('AC','l_ecode = '||l_ecode);
  debug.pr_debug('AC','l_edesc = '||l_edesc);
end;

rollback

