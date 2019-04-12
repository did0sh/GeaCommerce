package geacommerce.web.controllers;

import geacommerce.domain.models.binding.UserLoginBindingModel;
import geacommerce.domain.models.binding.UserRegisterBindingModel;
import geacommerce.domain.models.service.CartServiceModel;
import geacommerce.domain.models.service.UserServiceModel;
import geacommerce.service.InquiryService;
import geacommerce.service.UserService;
import geacommerce.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class UserController extends BaseController {

    private final UserService userService;
    private final InquiryService inquiryService;
    private final ModelMapper modelMapper;
    private final Validator validator;

    @Autowired
    public UserController(UserService userService, InquiryService inquiryService, ModelMapper modelMapper, Validator validator) {
        this.userService = userService;
        this.inquiryService = inquiryService;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }

    @RequestMapping("create-account")
    @PageTitle(value = "Създай профил")
    public ModelAndView createAccount(HttpSession session,
                                      @ModelAttribute(name = "bindingModel") UserRegisterBindingModel userRegisterBindingModel) {
        session.removeAttribute("registered");
        if (session.getAttribute("name") != null) {
            return super.redirect("/");
        }

        return super.view("create-account", "bindingModel", userRegisterBindingModel);
    }


    @PostMapping("create-account")
    @PageTitle(value = "Създай профил")
    public ModelAndView createAccountConfirm(@Valid @ModelAttribute(name = "bindingModel") UserRegisterBindingModel userRegisterBindingModel,
                                             BindingResult result) {

        UserServiceModel userServiceModel =
                this.modelMapper.map(userRegisterBindingModel, UserServiceModel.class);


        if (userServiceModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())
                && this.validator.validate(userRegisterBindingModel).size() == 0
                && !result.hasErrors()
                && !this.userService.hasAlreadyRegisteredUser(userServiceModel)) {

            this.userService.registerUser(userServiceModel);
            return super.view("/create-account", "registered", true);
        }


        return super.view("/create-account", "bindingModel", userRegisterBindingModel,
                "registered", false);
    }

    @RequestMapping("sign-in")
    @PageTitle(value = "Вход")
    public ModelAndView signIn(HttpSession session, @ModelAttribute(name = "model") UserLoginBindingModel userLoginBindingModel) {
        if (session.getAttribute("name") != null) {
            return super.redirect("/");
        }
        return super.view("sign-in", "model", userLoginBindingModel);
    }

    @PostMapping("sign-in")
    @PageTitle(value = "Вход")
    public ModelAndView signInConfirm(@Valid @ModelAttribute(name = "model") UserLoginBindingModel userLoginBindingModel,
                                      BindingResult result,
                                      HttpSession session) {

        UserServiceModel userServiceModel = this.modelMapper.map(userLoginBindingModel, UserServiceModel.class);

        if (result.hasErrors() || (userServiceModel = this.userService.loginUser(userServiceModel)) == null) {
            return super.view("sign-in", "model", userLoginBindingModel, "loggedIn", false);
        }

        this.addSessionAttributes(userServiceModel, session);
        session.setAttribute("user", userServiceModel);
        CartServiceModel cartServiceModel = new CartServiceModel();
        session.setAttribute("cart", cartServiceModel);

        session.setAttribute("inquiries", this.inquiryService.findAllInquiries().size());
        return super.view("sign-in", "loggedIn", true);
    }

    @RequestMapping("logout")
    public ModelAndView logout(HttpSession session) {
        UserServiceModel user = (UserServiceModel) session.getAttribute("user");
        String cartID = (String) session.getAttribute("cartID");
        if (user.getRole() != null) {
            if (user.getRole().equals("Guest") && cartID != null) {
                this.userService.removeCart(user, cartID);
            }
            session.invalidate();
            return super.redirect("/");
        }
        return super.redirect("/sign-in");
    }

    @RequestMapping("/user/profile")
    @PageTitle(value = "Профил")
    public ModelAndView profile(HttpSession session) {
        if (session.getAttribute("name") == null || session.getAttribute("role") == "Admin") {
            return super.redirect("/sign-in");
        }
        return super.view("profile");
    }

    @RequestMapping("/user/profile-admin")
    @PageTitle(value = "Профил")
    public ModelAndView profileAdmin(HttpSession session) {
        if (session.getAttribute("name") == null || session.getAttribute("role") == "Guest") {
            return super.redirect("/sign-in");
        }
        return super.view("profile-admin");
    }

    private void addSessionAttributes(UserServiceModel userServiceModel, HttpSession session) {
        Map<String, String> sessionMap = new LinkedHashMap<>() {{
            put("name", userServiceModel.getFirstName());
            put("lastName", userServiceModel.getLastName());
            put("address", userServiceModel.getAddress());
            put("email", userServiceModel.getEmail());
            put("role", userServiceModel.getRole());
            put("gender", userServiceModel.getGender());
            put("phone", userServiceModel.getPhoneNumber());
            put("town", userServiceModel.getTown());
        }};

        for (Map.Entry<String, String> entry : sessionMap.entrySet()) {
            session.setAttribute(entry.getKey(), entry.getValue());
        }
    }
}
