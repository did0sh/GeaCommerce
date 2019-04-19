package geacommerce.web.controllers;

import geacommerce.domain.models.binding.SearchBindingModel;
import geacommerce.web.annotations.PageTitle;
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
    @PageTitle(value = "Начало")
    public ModelAndView index(@ModelAttribute(name = "searchModel") SearchBindingModel searchBindingModel) {
        return super.view("index", "searchModel", searchBindingModel);
    }

    @RequestMapping("info")
    @PageTitle(value = "За нас")
    public ModelAndView info() {
        return super.view("info");
    }

    @RequestMapping("our-shops")
    @PageTitle(value = "Магазини")
    public ModelAndView shops() {
        return super.view("our-shops");
    }

    @RequestMapping("privacy")
    @PageTitle(value = "Права и задължения")
    public ModelAndView privacy() {
        return super.view("privacy");
    }

    @PostMapping("index")
    @PageTitle(value = "Начало")
    public ModelAndView searchConfirm(@Valid @ModelAttribute(name = "searchModel") SearchBindingModel searchBindingModel,
                                      BindingResult result,
                                      HttpSession session) {

        if (result.hasErrors()){
            return super.view("index", "searchModel", searchBindingModel);
        }

        session.setAttribute("searched", true);
        session.setAttribute("input", searchBindingModel.getSearchValue());
        return super.redirect("/search?page=1");
    }
}
