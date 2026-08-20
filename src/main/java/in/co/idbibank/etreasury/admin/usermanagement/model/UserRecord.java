package in.co.idbibank.etreasury.admin.usermanagement.model;
import java.time.LocalDateTime;
public record UserRecord(String userCode,String einNumber,String solId,String role,String status,
 String rights,String bulkUpload,String createdBy,LocalDateTime creationTime,String modifiedBy,
 LocalDateTime modifiedOn,LocalDateTime approvedRejectedOn,String approvedRejectedBy) { }
