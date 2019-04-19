package geacommerce.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class SessionTimerInterceptor extends HandlerInterceptorAdapter {

    private static final long MAX_INACTIVE_SESSION_TIME_1DAY_IN_MILLISECONDS = 86_400_000;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        //logging out only for guests, admins have unlimited session
        if (session.getAttribute("role") == "Guest") {
            if (System.currentTimeMillis() - session.getLastAccessedTime() > MAX_INACTIVE_SESSION_TIME_1DAY_IN_MILLISECONDS) {
                System.out.println("Logging out, due to inactive session!");
                request.logout();
                response.sendRedirect("/logout");
            }
        }
        return true;
    }
}
