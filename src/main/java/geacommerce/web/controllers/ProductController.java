package geacommerce.web.controllers;

import geacommerce.common.Constants;
import geacommerce.domain.entities.Product;
import geacommerce.domain.entities.User;
import geacommerce.domain.models.binding.ProductAddBindingModel;
import geacommerce.domain.models.binding.ProductAddToCartBindingModel;
import geacommerce.domain.models.binding.ProductUpdateBindingModel;
import geacommerce.domain.models.service.CartServiceModel;
import geacommerce.domain.models.service.ProductServiceModel;
import geacommerce.domain.models.service.UserServiceModel;
import geacommerce.domain.models.view.ProductViewModel;
import geacommerce.service.ProductService;
import geacommerce.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductController extends BaseController {

    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final Validator validator;

    @Autowired
    public ProductController(ProductService productService, ModelMapper modelMapper, Validator validator) {
        this.productService = productService;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }

    @RequestMapping("/all")
    @PageTitle(value = "Продукти")
    public ModelAndView products(Pageable pageable) {

        Page<Product> pages = this.productService.findAllProductsByType(pageable, Constants.ALL_TYPE);
        List<ProductViewModel> allProducts = pages.getContent()
                .stream().map(product -> this.modelMapper.map(product, ProductViewModel.class))
                .collect(Collectors.toList());

        return super.view("products-all", "allProducts", allProducts, "pages", pages);
    }

    @RequestMapping("/add-product")
    @PageTitle(value = "Добави продукт")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addProduct(HttpSession session, @ModelAttribute(name = "product") ProductAddBindingModel productAddBindingModel) {
        if (session.getAttribute("name") == null || session.getAttribute("role") == "Guest") {
            return super.redirect("/sign-in");
        }
        return super.view("add-product", "addProduct", productAddBindingModel);
    }

    @PostMapping("/add-product")
    @PageTitle(value = "Добави продукт")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addProductConfirm(@Valid @ModelAttribute(name = "product") ProductAddBindingModel productAddBindingModel, BindingResult result) {
        ProductServiceModel productServiceModel =
                this.modelMapper.map(productAddBindingModel, ProductServiceModel.class);

        if (this.validator.validate(productAddBindingModel).size() == 0
                && !result.hasErrors()
                && this.productService.saveProduct(productServiceModel)) {

            return super.view("add-product", "addedProduct", true, "addProduct", productAddBindingModel);
        }

        return super.view("add-product", "addedProduct", false, "addProduct", productAddBindingModel);
    }

    @RequestMapping("/bearings")
    @PageTitle(value = "Лагери")
    public ModelAndView bearings(Pageable pageable) {
        Page<Product> pages = this.productService.findAllProductsByType(pageable, Constants.BEARING_TYPE);

        List<ProductViewModel> allBearings =
                        pages.getContent()
                        .stream().map(productServiceModel -> this.modelMapper.map(productServiceModel, ProductViewModel.class))
                        .filter(productViewModel -> productViewModel.getType().equals(Constants.BEARING_TYPE))
                        .collect(Collectors.toList());

        return super.view("products-bearings", "allBearings", allBearings, "pages", pages);
    }

    @RequestMapping("/belts")
    @PageTitle(value = "Ремъци")
    public ModelAndView belts(Pageable pageable) {
        Page<Product> pages = this.productService.findAllProductsByType(pageable, Constants.BELT_TYPE);

        List<ProductViewModel> allBelts =
                        pages.getContent()
                        .stream().map(productServiceModel -> this.modelMapper.map(productServiceModel, ProductViewModel.class))
                        .filter(productViewModel -> productViewModel.getType().equals(Constants.BELT_TYPE))
                        .collect(Collectors.toList());

        return super.view("products-belts", "allBelts", allBelts, "pages", pages);
    }

    @RequestMapping("/seals")
    @PageTitle(value = "Семеринги")
    public ModelAndView seals(Pageable pageable) {
        Page<Product> pages = this.productService.findAllProductsByType(pageable, Constants.SEAL_TYPE);

        List<ProductViewModel> allSeals =
                        pages.getContent()
                        .stream().map(productServiceModel -> this.modelMapper.map(productServiceModel, ProductViewModel.class))
                        .filter(productViewModel -> productViewModel.getType().equals(Constants.SEAL_TYPE))
                        .collect(Collectors.toList());

        return super.view("products-seals", "allSeals", allSeals, "pages", pages);
    }

    @RequestMapping("/other")
    @PageTitle(value = "Други")
    public ModelAndView other(Pageable pageable) {
        Page<Product> pages = this.productService.findAllProductsByType(pageable, Constants.OTHER_TYPE);

        List<ProductViewModel> otherItems =
                        pages.getContent()
                        .stream().map(productServiceModel -> this.modelMapper.map(productServiceModel, ProductViewModel.class))
                        .filter(productViewModel -> productViewModel.getType().equals(Constants.OTHER_TYPE))
                        .collect(Collectors.toList());

        return super.view("products-other", "otherItems", otherItems, "pages", pages);
    }

    @RequestMapping("/details/{id}")
    @PageTitle(value = "Детайли")
    public ModelAndView details(@PathVariable(name = "id") String productId) {
        ProductServiceModel productById = this.productService.findProductById(productId);
        ProductViewModel currentProduct =
                this.modelMapper.map(productById, ProductViewModel.class);

        return super.view("products-details", "product", currentProduct);
    }

    @PostMapping(value = "/details/{id}", params = "action=addItem")
    @PageTitle(value = "Детайли")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addProductToCart(@PathVariable(name = "id") String productId,
                                         @Valid @ModelAttribute(name = "product") ProductAddToCartBindingModel model, BindingResult result,
                                         HttpSession session, RedirectAttributes redirectAttributes) {

        ProductServiceModel currentProduct = this.productService.findProductById(productId);
        ProductAddToCartBindingModel bindingModel = this.modelMapper.map(currentProduct, ProductAddToCartBindingModel.class);
        bindingModel.setAmount(model.getAmount());

        CartServiceModel cartServiceModel = (CartServiceModel) session.getAttribute("cart");

        if (result.hasErrors()) {
            return super.view("products-details", "product", bindingModel, "addedToCart", false);
        }

        if (currentProduct.getAmount() < bindingModel.getAmount()) {
            return super.view("products-details", "product", bindingModel, "onStock", false);
        }

        Product productToAdd = this.modelMapper.map(bindingModel, Product.class);
        Map<String, Product> cartProducts = cartServiceModel.getProducts();

        // update amount if already has this item in cart
        if (!cartProducts.containsKey(productId)) {
            cartProducts.put(productId, productToAdd);
        } else {
            cartProducts.get(productId)
                    .setAmount(cartProducts.get(productId).getAmount() + bindingModel.getAmount());
        }

        UserServiceModel userServiceModel = (UserServiceModel) session.getAttribute("user");
        if (userServiceModel != null){
            cartServiceModel.setUser(this.modelMapper.map(userServiceModel, User.class));
        }

        //save product with his cart
        ProductServiceModel productServiceModel = this.modelMapper.map(bindingModel, ProductServiceModel.class);
        CartServiceModel updatedCart = this.productService
                .updateProductWithCart(productServiceModel, cartServiceModel);

        session.setAttribute("user", userServiceModel);
        session.setAttribute("cartID", updatedCart.getId());
        return super.redirect("/cart/" + userServiceModel.getId());
    }

    @PostMapping(value = "/details/{id}", params = "action=delete")
    @PageTitle(value = "Детайли")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView deleteProduct(@PathVariable(name = "id") String productId) {
        this.productService.deleteProductById(productId);
        return super.redirect("/products/all");
    }

    @PostMapping(value = "/details/{id}", params = "action=update")
    @PageTitle(value = "Детайли")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView updateProduct(@PathVariable(name = "id") String productId) {
        return super.redirect("/products/update-product/" + productId);
    }

    @RequestMapping("/update-product/{id}")
    @PageTitle(value = "Актуализация")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView update(@PathVariable(name = "id") String updateProductId, HttpSession session) {
        if (session.getAttribute("role") == "Admin") {
            ProductUpdateBindingModel model =
                    this.modelMapper.map(this.productService.findProductById(updateProductId), ProductUpdateBindingModel.class);
            return super.view("update-product", "product", model);
        }
        return super.redirect("/sign-in");
    }

    @PostMapping("/update-product/{id}")
    @PageTitle(value = "Актуализация")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView updateConfirm(@PathVariable(name = "id") String updateProductId,
                                      @Valid @ModelAttribute(name = "product") ProductUpdateBindingModel productUpdateBindingModel,
                                      BindingResult result) {

        ProductServiceModel productToBeUpdated =
                this.productService.findProductById(updateProductId);

        if (result.hasErrors()) {
            return super.view("update-product", "updatedProduct", false, "product", productToBeUpdated);
        }

        productToBeUpdated.setPrice(productUpdateBindingModel.getPrice());
        productToBeUpdated.setAmount(productUpdateBindingModel.getAmount());

        this.productService.updateProduct(productToBeUpdated);
        return super.view("update-product", "updatedProduct", true);
    }
}
