package geacommerce.web;

import geacommerce.domain.models.binding.UserLoginBindingModel;
import geacommerce.domain.models.binding.UserRegisterBindingModel;
import geacommerce.domain.models.service.CartServiceModel;
import geacommerce.domain.models.service.UserServiceModel;
import geacommerce.service.UserService;
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

@Controller
@RequestMapping("/")
public class UserController extends BaseController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final Validator validator;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper, Validator validator) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }

    @RequestMapping("create-account")
    public ModelAndView createAccount(HttpSession session,
                                      @ModelAttribute(name = "bindingModel") UserRegisterBindingModel userRegisterBindingModel) {
        session.removeAttribute("registered");
        if (session.getAttribute("name") != null) {
            return super.redirect("/");
        }

        return super.view("create-account", "bindingModel", userRegisterBindingModel);
    }


    @PostMapping("create-account")
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
    public ModelAndView signIn(HttpSession session, @ModelAttribute(name = "model") UserLoginBindingModel userLoginBindingModel) {
        if (session.getAttribute("name") != null) {
            return super.redirect("/");
        }
        return super.view("sign-in", "model", userLoginBindingModel);
    }

    @PostMapping("sign-in")
    public ModelAndView signInConfirm(@Valid @ModelAttribute(name = "model") UserLoginBindingModel userLoginBindingModel,
                                      BindingResult result,
                                      HttpSession session) {

        UserServiceModel userServiceModel = this.modelMapper.map(userLoginBindingModel, UserServiceModel.class);

        if (result.hasErrors() || (userServiceModel = this.userService.loginUser(userServiceModel)) == null){
            return super.view("sign-in", "model", userLoginBindingModel, "loggedIn", false);
        }

        session.setAttribute("user", userServiceModel);
        session.setAttribute("name", userServiceModel.getFirstName());
        session.setAttribute("lastName", userServiceModel.getLastName());
        session.setAttribute("address", userServiceModel.getAddress());
        session.setAttribute("email", userServiceModel.getEmail());
        session.setAttribute("role", userServiceModel.getRole());
        session.setAttribute("gender", userServiceModel.getGender());
        session.setAttribute("phone", userServiceModel.getPhoneNumber());
        session.setAttribute("town", userServiceModel.getTown());

        CartServiceModel cartServiceModel = new CartServiceModel();
        session.setAttribute("cart", cartServiceModel);
        return super.redirect("/");
    }

    @RequestMapping("logout")
    public ModelAndView logout(HttpSession session) {
        UserServiceModel user = (UserServiceModel) session.getAttribute("user");
        String cartID = (String) session.getAttribute("cartID");
        if (user.getRole() != null) {
            if (user.getRole().equals("Guest") && cartID != null){
                this.userService.removeCart(user, cartID);
            }
            session.invalidate();
            return super.redirect("/");
        }
        return super.redirect("/sign-in");
    }

    @RequestMapping("/user/profile")
    public ModelAndView profile(HttpSession session) {
        if (session.getAttribute("name") == null || session.getAttribute("role") == "Admin") {
            return super.redirect("/sign-in");
        }
        return super.view("profile");
    }

    @RequestMapping("/user/profile-admin")
    public ModelAndView profileAdmin(HttpSession session) {
        if (session.getAttribute("name") == null || session.getAttribute("role") == "Guest") {
            return super.redirect("/sign-in");
        }
        return super.view("profile-admin");
    }
}
