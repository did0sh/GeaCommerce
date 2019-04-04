package geacommerce.web;

import geacommerce.domain.models.binding.InquiryBindingModel;
import geacommerce.domain.models.service.InquiryServiceModel;
import geacommerce.service.InquiryService;
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
    public ModelAndView inquiry(@ModelAttribute(name = "inquiryModel")InquiryBindingModel model, HttpSession session) {
        if(session.getAttribute("role") != "Admin"){
            return super.view("inquiry");
        }

        return super.redirect("/");
    }

    @PostMapping("/inquiry")
    public ModelAndView inquiryConfirm(@Valid @ModelAttribute(name = "inquiryModel")InquiryBindingModel model, BindingResult result) {
        if (!result.hasErrors()){
            InquiryServiceModel inquiryServiceModel = this.modelMapper.map(model, InquiryServiceModel.class);

            if (this.inquiryService.saveInquiry(inquiryServiceModel)){
                return super.view("inquiry", "sentInquiry", true);
            }
        }

        return super.view("inquiry", "sentInquiry", false);
    }
}
