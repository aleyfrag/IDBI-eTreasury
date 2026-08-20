package in.co.idbibank.etreasury.admin.usermanagement.service;
import in.co.idbibank.etreasury.admin.usermanagement.model.*;
import in.co.idbibank.etreasury.admin.usermanagement.repository.UserManagementRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service public class UserManagementService {
 private final UserManagementRepository repo;
 public UserManagementService(UserManagementRepository repo){this.repo=repo;}
 public UserRecord lookup(String v){return repo.lookup(v);} public List<SolOption> sols(){return repo.sols();}
 public List<UserRecord> users(){return repo.users();} public String maintain(UserCommand c,String a){return repo.maintain(c,a);}
}
