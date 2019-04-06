package geacommerce.web;

import geacommerce.domain.models.binding.InquiryBindingModel;
import geacommerce.domain.models.service.InquiryServiceModel;
import geacommerce.domain.models.view.InquiryViewModel;
import geacommerce.service.InquiryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class InquiryController extends BaseController {

    private final InquiryService inquiryService;
    private final ModelMapper modelMapper;

    @Autowired
    public InquiryController(InquiryService inquiryService, ModelMapper modelMapper) {
        this.inquiryService = inquiryService;
        this.modelMapper = modelMapper;
    }

    @RequestMapping("/inquiry")
    public ModelAndView inquiry(@ModelAttribute(name = "inquiryModel") InquiryBindingModel model, HttpSession session) {
        if (session.getAttribute("role") == "Guest") {
            this.setDisabledFieldsForGuestUser(session, model);
            return super.view("inquiry", "inquiryModel", model);

        } else if (session.getAttribute("role") == null) {
            return super.view("inquiry");
        }

        return super.redirect("/");
    }

    @PostMapping("/inquiry")
    public ModelAndView inquiryConfirm(@Valid @ModelAttribute(name = "inquiryModel") InquiryBindingModel model, BindingResult result, HttpSession session) {
        if (!result.hasErrors()) {
            InquiryServiceModel inquiryServiceModel = this.modelMapper.map(model, InquiryServiceModel.class);

            if (this.inquiryService.saveInquiry(inquiryServiceModel)) {
                return super.view("inquiry", "sentInquiry", true);
            }
        }

        return super.view("inquiry", "sentInquiry", false);
    }

    @RequestMapping("/inquiries/details")
    public ModelAndView inquiriesDetails(HttpSession session) {
        List<InquiryViewModel> allInquiries = this.inquiryService.findAllInquiries()
                .stream().map(inquiryServiceModel -> this.modelMapper.map(inquiryServiceModel, InquiryViewModel.class))
                .collect(Collectors.toList());

        if (session.getAttribute("role") == "Admin") {
            session.setAttribute("inquiries", allInquiries.size());
            return super.view("inquiries-details", "allInquiries", allInquiries);
        }

        return super.redirect("/");
    }

    @PostMapping(value = "/inquiries/details/{id}", params = "action=read")
    public ModelAndView inquiriesReadConfirm(@PathVariable(name = "id") String messageId) {
        this.inquiryService.readInquiry(messageId);
        return super.redirect("/inquiries/details");

    }

    private void setDisabledFieldsForGuestUser(HttpSession session, InquiryBindingModel model) {
        String firstName = (String) session.getAttribute("name");
        String lastName = (String) session.getAttribute("lastName");
        String email = (String) session.getAttribute("email");

        String userName = firstName.concat(" ").concat(lastName);
        model.setUserName(userName);
        model.setUserEmail(email);
    }

}
