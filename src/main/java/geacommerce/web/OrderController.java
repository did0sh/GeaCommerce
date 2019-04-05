package geacommerce.web;

import geacommerce.domain.models.view.OrderViewModel;
import geacommerce.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class OrderController extends BaseController {

    private final OrderService orderService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderController(OrderService orderService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @RequestMapping("/orders")
    public ModelAndView inquiry(HttpSession session) {
        List<OrderViewModel> allOrders = this.orderService.findAllOrders()
                .stream().map(orderServiceModel -> {

                    OrderViewModel model = this.modelMapper.map(orderServiceModel, OrderViewModel.class);
                    model.setClientName(orderServiceModel.getBuyer().getFirstName().concat(" ").concat(orderServiceModel.getBuyer().getLastName()));
                    model.setClientEmail(orderServiceModel.getBuyer().getEmail());
                    Object[][] products = this.orderService.getOrderProducts();
                    Map<String, List<String>> formattedProducts = new LinkedHashMap<>();

                    for (Object[] product : products) {
                        String orderId = (String) product[0];
                        String productName = (String) product[1];
                        Integer productAmount = (Integer) product[2];

                        String formattedProduct = String.format("%s - %dбр.", productName, productAmount);

                        if (!formattedProducts.containsKey(orderId)){
                            formattedProducts.put(orderId, new ArrayList<>());
                        }

                        formattedProducts.get(orderId).add(formattedProduct);
                    }

                    model.setFormattedProducts(formattedProducts);
                    return model;
                }).collect(Collectors.toList());

        if(session.getAttribute("role") == "Admin"){
            return super.view("orders", "allOrders", allOrders);
        }

        return super.redirect("/");
    }

    @PostMapping(value = "/orders/{id}", params = "action=complete")
    public ModelAndView completeOrder(@PathVariable(name = "id") String orderId){
        this.orderService.completeOrder(orderId);
        return super.redirect("/orders");
    }
}
