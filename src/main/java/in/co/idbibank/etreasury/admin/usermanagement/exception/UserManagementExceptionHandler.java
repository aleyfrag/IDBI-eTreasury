package in.co.idbibank.etreasury.admin.usermanagement.exception;
import in.co.idbibank.etreasury.admin.usermanagement.controller.UserManagementController;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.Map;
@RestControllerAdvice(assignableTypes=UserManagementController.class)
public class UserManagementExceptionHandler {
 @ExceptionHandler(UserManagementException.class) ResponseEntity<Map<String,Object>> handle(UserManagementException e){return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success",false,"message",e.getMessage()));}
}
