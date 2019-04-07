package geacommerce.web.controllers;


import geacommerce.domain.models.view.ProductViewModel;
import geacommerce.service.ProductService;
import geacommerce.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/search")
public class SearchController extends BaseController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public SearchController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @RequestMapping("")
    @PageTitle(value = "Търсене")
    public ModelAndView search(HttpSession session) {
        if (session.getAttribute("searched") != null){

            String formattedInput = String.valueOf(session.getAttribute("input")).trim().toLowerCase();
            String regexToSearch = ".*" + formattedInput.replaceAll("\\s+", ".*") +
                    ".*";

            List<ProductViewModel> matchedProducts = this.productService.findAllProducts()
                    .stream()
                    .map(productServiceModel -> this.modelMapper.map(productServiceModel, ProductViewModel.class))
                    .filter(productViewModel ->
                            productViewModel.getName().toLowerCase().matches(regexToSearch))
                    .collect(Collectors.toList());

            return super.view("search", "matchedProducts", matchedProducts);
        }

        return super.redirect("/");
    }
}
