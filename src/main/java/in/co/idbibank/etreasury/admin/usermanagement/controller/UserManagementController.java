package in.co.idbibank.etreasury.admin.usermanagement.controller;
import in.co.idbibank.etreasury.admin.menu.AdminMenuRoutes;
import in.co.idbibank.etreasury.admin.usermanagement.model.*;
import in.co.idbibank.etreasury.admin.usermanagement.service.UserManagementService;
import org.springframework.web.bind.annotation.*;
import java.security.Principal; import java.util.Map;
/** AJAX endpoints kept separate from the baseline AdminController. */
@RestController public class UserManagementController {
 private final UserManagementService service;
 public UserManagementController(UserManagementService service){this.service=service;}
 @GetMapping(AdminMenuRoutes.USER_MASTER_URL+"/lookup") public Map<String,Object> lookup(@RequestParam String value){UserRecord u=service.lookup(value);return Map.of("found",u!=null,"user",u==null?Map.of():u);}
 @PostMapping(AdminMenuRoutes.USER_MASTER_URL+"/action") public Map<String,Object> action(@ModelAttribute UserCommand command,Principal principal){return Map.of("success",true,"message",service.maintain(command,principal==null?null:principal.getName()));}
}
