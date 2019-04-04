package geacommerce.web;

import geacommerce.domain.models.binding.InquiryBindingModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class OrderController extends BaseController {

    @RequestMapping("/orders")
    public ModelAndView inquiry(HttpSession session) {
        if(session.getAttribute("role") == "Admin"){
            return super.view("orders");
        }

        return super.redirect("/");
    }
}
