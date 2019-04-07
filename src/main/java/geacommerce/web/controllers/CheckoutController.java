package geacommerce.web.controllers;

import geacommerce.common.Constants;
import geacommerce.domain.entities.Product;
import geacommerce.domain.entities.User;
import geacommerce.domain.models.service.CartServiceModel;
import geacommerce.domain.models.service.OrderServiceModel;
import geacommerce.domain.models.service.ProductServiceModel;
import geacommerce.domain.models.service.UserServiceModel;
import geacommerce.service.CartService;
import geacommerce.service.OrderService;
import geacommerce.service.ProductService;
import geacommerce.service.UserService;
import geacommerce.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class CheckoutController extends BaseController {

    private final CartService cartService;
    private final UserService userService;
    private final OrderService orderService;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public CheckoutController(CartService cartService, UserService userService, OrderService orderService, ProductService productService, ModelMapper modelMapper) {
        this.cartService = cartService;
        this.userService = userService;
        this.orderService = orderService;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @RequestMapping("/checkout")
    @PageTitle(value = "Потвърждаване на поръчка")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView checkout(HttpSession session){
        if (session.getAttribute("role") == "Guest" && session.getAttribute("cartID") != null && session.getAttribute("checkoutClicked") != null){
            String cartID = (String) session.getAttribute("cartID");
            Map<String, Product> checkoutProducts =
                    this.cartService.findCartById(cartID).getProducts();

            BigDecimal totalPrice = this.calculateTotalPrice(checkoutProducts);

            return super.view("checkout", "checkoutProducts", checkoutProducts, "totalPrice", totalPrice);
        }

        return super.redirect("/");
    }

    @PostMapping("/checkout")
    @PageTitle(value = "Потвърждаване на поръчка")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView checkoutConfirm(HttpSession session){
        String cartID = (String) session.getAttribute("cartID");
        String userEmail = (String) session.getAttribute("email");
        OrderServiceModel order = new OrderServiceModel();
        UserServiceModel user = this.userService.findUserByEmail(userEmail);

        Map<String, Product> checkoutProducts =
                this.cartService.findCartById(cartID).getProducts();

        BigDecimal totalPrice = this.calculateTotalPrice(checkoutProducts);
        List<Product> boughtProducts = new ArrayList<>(checkoutProducts.values());

        order.setOrderPrice(totalPrice);
        order.setBuyer(this.modelMapper.map(user, User.class));
        order.setDeliveryAddress(user.getAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Constants.ORDER_STATUS_CONFIRMED);
        order.setBoughtProducts(boughtProducts);

        if (this.orderService.saveOrder(order)){
            this.userService.removeCart(user, cartID);
            this.saveProductsWithNewAmounts(boughtProducts);
            session.setAttribute("cartItems", 0);
            CartServiceModel cartServiceModel = new CartServiceModel();
            session.setAttribute("cart", cartServiceModel);
            return super.view("checkout", "orderSuccessful", true);
        }

        return super.redirect("/checkout");
    }

    private BigDecimal calculateTotalPrice(Map<String, Product> products){
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Product product : products.values()) {
            totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(product.getCartAmount())));
        }

        return totalPrice;
    }

    private void saveProductsWithNewAmounts(List<Product> boughtProducts){
        for (Product boughtProduct : boughtProducts) {
            ProductServiceModel dbProduct = this.productService.findProductById(boughtProduct.getId());
            dbProduct.setAmount(dbProduct.getAmount() - boughtProduct.getCartAmount());
            this.productService.saveProduct(dbProduct);
        }
    }
}
