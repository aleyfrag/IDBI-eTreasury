package in.co.idbibank.etreasury.admin.usermanagement.repository;

import in.co.idbibank.etreasury.admin.usermanagement.exception.UserManagementException;
import in.co.idbibank.etreasury.admin.usermanagement.model.*;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/** Oracle access for Admin User Management only. */
@Repository
public class UserManagementRepository {
 private static final String PKG="EB_ADMIN_SLAB_NEW", CUR="P_CURSOR", STS="P_STATUS", MSG="P_MESSAGE";
 private final SimpleJdbcCall lookup, sols, list, maintain;
 public UserManagementRepository(JdbcTemplate jdbc){
  lookup=base(jdbc,"GET_USER_BY_EIN").declareParameters(new SqlParameter("P_SEARCH_VALUE",Types.VARCHAR),
   new SqlOutParameter(CUR,Types.REF_CURSOR,this::mapUser),out(STS),out(MSG));
  sols=base(jdbc,"GET_ACTIVE_SOLS").declareParameters(
   new SqlOutParameter(CUR,Types.REF_CURSOR,this::mapSol),out(STS),out(MSG));
  list=base(jdbc,"GET_USER_MASTER_LIST").declareParameters(
   new SqlOutParameter(CUR,Types.REF_CURSOR,this::mapUser),out(STS),out(MSG));
  maintain=base(jdbc,"MAINTAIN_USER_MASTER").declareParameters(
   in("P_ACTION"),in("P_EIN_NO"),in("P_USER_CODE"),in("P_SOL_ID"),in("P_USER_STATUS"),
   in("P_RIGHTS"),in("P_ACTION_BY"),out(STS),out(MSG));
 }
 public UserRecord lookup(String value){List<UserRecord> r=cursor(run(lookup,new MapSqlParameterSource("P_SEARCH_VALUE",req(value,"EIN Number"))),UserRecord.class);return r.isEmpty()?null:r.getFirst();}
 public List<SolOption> sols(){return cursor(run(sols,new MapSqlParameterSource()),SolOption.class);}
 public List<UserRecord> users(){return cursor(run(list,new MapSqlParameterSource()),UserRecord.class);}
 public String maintain(UserCommand c,String actor){
  String action=req(c.action(),"Action").toUpperCase(Locale.ROOT), status=req(c.status(),"Status").toUpperCase(Locale.ROOT);
  if(!List.of("SAVE","UPDATE","DELETE","REJECT").contains(action))throw new UserManagementException("Invalid action");
  if(!List.of("A","D").contains(status))throw new UserManagementException("Invalid status");
  MapSqlParameterSource p=new MapSqlParameterSource().addValue("P_ACTION",action)
   .addValue("P_EIN_NO",req(c.einNumber(),"EIN Number")).addValue("P_USER_CODE",req(c.userCode(),"User Code"))
   .addValue("P_SOL_ID",req(c.solId(),"SOL ID")).addValue("P_USER_STATUS",status)
   .addValue("P_RIGHTS",c.admin()?"ADM":null).addValue("P_ACTION_BY",req(actor,"Logged-in user"));
  return String.valueOf(run(maintain,p).get(MSG));
 }
 private SimpleJdbcCall base(JdbcTemplate j,String n){return new SimpleJdbcCall(j).withCatalogName(PKG).withProcedureName(n).withoutProcedureColumnMetaDataAccess();}
 private SqlParameter in(String n){return new SqlParameter(n,Types.VARCHAR);} private SqlOutParameter out(String n){return new SqlOutParameter(n,Types.VARCHAR);}
 private Map<String,Object> run(SimpleJdbcCall c,MapSqlParameterSource p){try{Map<String,Object> r=c.execute(p);if(!"SUCCESS".equalsIgnoreCase(String.valueOf(r.get(STS))))throw new UserManagementException(String.valueOf(r.get(MSG)));return r;}catch(DataAccessException e){throw new UserManagementException("Unable to process User Management data",e);}}
 private String req(String v,String n){if(v==null||v.isBlank())throw new UserManagementException(n+" is required");return v.trim();}
 private <T> List<T> cursor(Map<String,Object> r,Class<T> t){Object v=r.get(CUR);if(!(v instanceof List<?> x))throw new UserManagementException("Oracle cursor is missing");return x.stream().map(t::cast).toList();}
 private SolOption mapSol(ResultSet r,int n)throws SQLException{return new SolOption(r.getString("SOL_ID"),r.getString("SOL_DESC"));}
 private UserRecord mapUser(ResultSet r,int n)throws SQLException{return new UserRecord(r.getString("USER_CODE"),r.getString("EIN_NO"),r.getString("SOL_ID"),r.getString("ROLE"),r.getString("STATUS"),r.getString("RIGHTS"),r.getString("ISBULK_UPLOAD"),r.getString("CREATED_BY"),dt(r,"CREATION_TIME"),r.getString("MODIFIED_BY"),dt(r,"MODIFIED_ON"),dt(r,"APPR_RJCT_ON"),r.getString("APPR_RJCT_BY"));}
 private LocalDateTime dt(ResultSet r,String c)throws SQLException{Timestamp t=r.getTimestamp(c);return t==null?null:t.toLocalDateTime();}
}
