package geacommerce.web;

import geacommerce.domain.models.binding.SearchBindingModel;
import geacommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class HomeController extends BaseController {

    @RequestMapping("")
    public ModelAndView index(@ModelAttribute(name = "searchModel") SearchBindingModel searchBindingModel) {
        return super.view("index", "searchModel", searchBindingModel);
    }

    @RequestMapping("info")
    public ModelAndView info() {
        return super.view("info");
    }

    @RequestMapping("our-shops")
    public ModelAndView shops() {
        return super.view("our-shops");
    }

    @RequestMapping("privacy")
    public ModelAndView privacy() {
        return super.view("privacy");
    }

    @PostMapping("index")
    public ModelAndView searchConfirm(@Valid @ModelAttribute(name = "searchModel") SearchBindingModel searchBindingModel,
                                      BindingResult result,
                                      HttpSession session) {

        if (result.hasErrors()){
            return super.view("index", "searchModel", searchBindingModel);
        }

        session.setAttribute("searched", true);
        session.setAttribute("input", searchBindingModel.getSearchValue());
        return super.redirect("/search");
    }
}
