declare
  x varchar2(1000);
  c utl_tcp.connection;
  n number;
begin
  x := 'TestMsgXYZ';
  c := utl_tcp.open_connection('localhost',1234);
  n := utl_tcp.write_line(c, x); 
  n := utl_tcp.write_line(c);
  dbms_output.put_line(utl_tcp.get_line(c, true));
  utl_tcp.close_connection(c);
end;
/
