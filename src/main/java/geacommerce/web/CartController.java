package geacommerce.web;

import geacommerce.domain.entities.Product;
import geacommerce.domain.entities.User;
import geacommerce.domain.models.service.CartServiceModel;
import geacommerce.domain.models.service.UserServiceModel;
import geacommerce.service.CartService;
import geacommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Map;

@Controller
public class CartController extends BaseController {

    private final CartService cartService;
    private final UserService userService;

    @Autowired
    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @RequestMapping("/cart/{id}")
    @SuppressWarnings("unchecked")
    public ModelAndView cart(@PathVariable(name = "id") String userId, HttpSession session) {
        if (session.getAttribute("role") != "Guest"){
            return super.redirect("/");
        }

        UserServiceModel currentUser = this.userService.findUserById(userId);
        if (currentUser.getCart() != null){
            Map<String, Product>  userProducts = currentUser.getCart().getProducts();
            BigDecimal totalPrice = BigDecimal.ZERO;
            for (Product product : userProducts.values()) {
                totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(product.getCartAmount())));
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
}
