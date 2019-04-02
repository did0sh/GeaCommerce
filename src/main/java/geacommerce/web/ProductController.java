package geacommerce.web;

import geacommerce.domain.entities.Product;
import geacommerce.domain.entities.User;
import geacommerce.domain.models.binding.ProductAddBindingModel;
import geacommerce.domain.models.binding.ProductAddToCartBindingModel;
import geacommerce.domain.models.binding.ProductUpdateBindingModel;
import geacommerce.domain.models.service.CartServiceModel;
import geacommerce.domain.models.service.ProductServiceModel;
import geacommerce.domain.models.service.UserServiceModel;
import geacommerce.domain.models.view.ProductViewModel;
import geacommerce.service.CartService;
import geacommerce.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final CartService cartService;
    private final ModelMapper modelMapper;
    private final Validator validator;

    @Autowired
    public ProductController(ProductService productService, CartService cartService, ModelMapper modelMapper, Validator validator) {
        this.productService = productService;
        this.cartService = cartService;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }

    @RequestMapping("/all")
    public ModelAndView products() {
        List<ProductViewModel> allProducts =
                this.productService.findAllProducts()
                        .stream().map(productServiceModel -> this.modelMapper.map(productServiceModel, ProductViewModel.class))
                        .collect(Collectors.toList());

        return super.view("products-all", "allProducts", allProducts);
    }

    @RequestMapping("/add-product")
    public ModelAndView addProduct(HttpSession session, @ModelAttribute(name = "product") ProductAddBindingModel productAddBindingModel) {
        if (session.getAttribute("name") == null || session.getAttribute("role") == "Guest") {
            return super.redirect("/sign-in");
        }
        return super.view("add-product", "addProduct", productAddBindingModel);
    }

    @PostMapping("/add-product")
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
    public ModelAndView bearings() {
        List<ProductViewModel> allBearings =
                this.productService.findAllProducts()
                        .stream().map(productServiceModel -> this.modelMapper.map(productServiceModel, ProductViewModel.class))
                        .filter(productViewModel -> productViewModel.getType().equals("Лагер"))
                        .collect(Collectors.toList());

        return super.view("products-bearings", "allBearings", allBearings);
    }

    @RequestMapping("/belts")
    public ModelAndView belts() {
        List<ProductViewModel> allBelts =
                this.productService.findAllProducts()
                        .stream().map(productServiceModel -> this.modelMapper.map(productServiceModel, ProductViewModel.class))
                        .filter(productViewModel -> productViewModel.getType().equals("Ремък"))
                        .collect(Collectors.toList());

        return super.view("products-belts", "allBelts", allBelts);
    }

    @RequestMapping("/seals")
    public ModelAndView seals() {
        List<ProductViewModel> allSeals =
                this.productService.findAllProducts()
                        .stream().map(productServiceModel -> this.modelMapper.map(productServiceModel, ProductViewModel.class))
                        .filter(productViewModel -> productViewModel.getType().equals("Семеринг"))
                        .collect(Collectors.toList());

        return super.view("products-seals", "allSeals", allSeals);
    }

    @RequestMapping("/other")
    public ModelAndView other() {
        List<ProductViewModel> otherItems =
                this.productService.findAllProducts()
                        .stream().map(productServiceModel -> this.modelMapper.map(productServiceModel, ProductViewModel.class))
                        .filter(productViewModel -> productViewModel.getType().equals("Друг"))
                        .collect(Collectors.toList());

        return super.view("products-other", "otherItems", otherItems);
    }

    @RequestMapping("/details/{id}")
    public ModelAndView details(@PathVariable(name = "id") String productId) {
        ProductServiceModel productById = this.productService.findProductById(productId);
        ProductViewModel currentProduct =
                this.modelMapper.map(productById, ProductViewModel.class);

        return super.view("products-details", "product", currentProduct);
    }

    @PostMapping(value = "/details/{id}", params = "action=addItem")
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
    public ModelAndView deleteProduct(@PathVariable(name = "id") String productId) {
        this.productService.deleteProductById(productId);
        return super.redirect("/products/all");
    }

    @PostMapping(value = "/details/{id}", params = "action=update")
    public ModelAndView updateProduct(@PathVariable(name = "id") String productId) {
        return super.redirect("/products/update-product/" + productId);
    }

    @RequestMapping("/update-product/{id}")
    public ModelAndView update(@PathVariable(name = "id") String updateProductId, HttpSession session) {
        if (session.getAttribute("role") == "Admin") {
            ProductUpdateBindingModel model =
                    this.modelMapper.map(this.productService.findProductById(updateProductId), ProductUpdateBindingModel.class);
            return super.view("update-product", "product", model);
        }
        return super.redirect("/sign-in");
    }

    @PostMapping("/update-product/{id}")
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