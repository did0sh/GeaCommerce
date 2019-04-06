package geacommerce.service;

import geacommerce.domain.models.service.CartServiceModel;
import geacommerce.domain.models.service.UserServiceModel;

public interface CartService {

    boolean saveCart(CartServiceModel cartServiceModel);

    void deleteCartProduct(String productId, String cartId, UserServiceModel userServiceModel);

    CartServiceModel findCartById(String id);
}
