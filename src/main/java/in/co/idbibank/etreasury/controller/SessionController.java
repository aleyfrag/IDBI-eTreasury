package in.co.idbibank.etreasury.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SessionController {

    @GetMapping("/session-expired")
    public String sessionExpired(
            HttpServletRequest request,
            HttpServletResponse response) {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        response.setHeader(
                "Set-Cookie",
                "JSESSIONID=; Path=/; Max-Age=0; HttpOnly; SameSite=Strict"
        );

        return "redirect:/login?expired";
    }
}
