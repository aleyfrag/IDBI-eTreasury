package in.co.idbibank.etreasury.admin.usermanagement.model;
public record UserCommand(String einNumber,String userCode,String solId,String status,
 boolean admin,String action) { }
