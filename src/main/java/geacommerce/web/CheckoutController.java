package geacommerce.web;

import geacommerce.domain.entities.Product;
import geacommerce.domain.models.service.CartServiceModel;
import geacommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Map;

@Controller
public class CheckoutController extends BaseController {

    private final CartService cartService;

    @Autowired
    public CheckoutController(CartService cartService) {
        this.cartService = cartService;
    }

    @RequestMapping("/checkout")
    public ModelAndView modelAndView(HttpSession session){
        String cartID = (String) session.getAttribute("cartID");
        Integer productsInCart = (Integer) session.getAttribute("cartItems");
        Map<String, Product> checkoutProducts =
                this.cartService.findCartById(cartID).getProducts();

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Product product : checkoutProducts.values()) {
            totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(product.getCartAmount())));
        }

        if (session.getAttribute("role") == "Guest" && productsInCart != null){
            return super.view("checkout", "checkoutProducts", checkoutProducts, "totalPrice", totalPrice);
        }

        return super.redirect("/");
    }
}
