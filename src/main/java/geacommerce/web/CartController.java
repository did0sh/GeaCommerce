package geacommerce.web;

import geacommerce.domain.entities.Product;
import geacommerce.domain.models.service.CartServiceModel;
import geacommerce.domain.models.service.ProductServiceModel;
import geacommerce.domain.models.service.UserServiceModel;
import geacommerce.service.CartService;
import geacommerce.service.ProductService;
import geacommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Map;

@Controller
public class CartController extends BaseController {

    private final CartService cartService;
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public CartController(CartService cartService, UserService userService, ProductService productService) {
        this.cartService = cartService;
        this.userService = userService;
        this.productService = productService;
    }

    @RequestMapping("/cart/{id}")
    @SuppressWarnings("unchecked")
    public ModelAndView cart(@PathVariable(name = "id") String userId, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("role") != "Guest"){
            return super.redirect("/");
        }

        UserServiceModel currentUser = this.userService.findUserById(userId);
        if (currentUser.getCart() != null){
            Map<String, Product>  userProducts = currentUser.getCart().getProducts();
            BigDecimal totalPrice = this.calculateTotalPrice(userProducts);

            if (!this.areProductsAvailable(userProducts)){
                session.removeAttribute("checkoutClicked");
                return super.view("cart", "products", userProducts, "total", totalPrice, "areProductsAvailable", false);
            }

            session.setAttribute("cartItems", userProducts.size());
            return super.view("cart", "products", userProducts, "total", totalPrice);
        }

        session.setAttribute("cartItems", 0);
        return super.view("cart", "products", null);
    }

    @PostMapping(value = "/cart/{id}", params = "action=delete")
    @SuppressWarnings("unchecked")
    public ModelAndView deleteCartProduct(@PathVariable(name = "id") String productId, HttpSession session){
        UserServiceModel userModel = (UserServiceModel)session.getAttribute("user");
        String userId = userModel.getId();
        String cartId = (String) session.getAttribute("cartID");
        this.cartService.deleteCartProduct(productId, cartId, userModel);

        session.setAttribute("cart", new CartServiceModel());
        return super.redirect("/cart/" + userId);
    }

    @PostMapping(value = "/cart/{id}", params = "action=checkout")
    public ModelAndView checkout(@PathVariable(name = "id") String userId, HttpSession session){
        session.setAttribute("checkoutClicked", Boolean.TRUE);
        return super.redirect("/checkout");
    }

    private BigDecimal calculateTotalPrice(Map<String, Product> products){
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Product product : products.values()) {
            totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(product.getCartAmount())));
        }

        return totalPrice;
    }

    private boolean areProductsAvailable(Map<String, Product> productMap){
        for (Product product : productMap.values()) {
            ProductServiceModel databaseProduct = this.productService.findProductById(product.getId());
            if (databaseProduct.getAmount() < product.getCartAmount()){
                return false;
            }
        }

        return true;
    }
}
