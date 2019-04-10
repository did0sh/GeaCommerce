package geacommerce.service;

import geacommerce.domain.entities.Cart;
import geacommerce.domain.models.service.CartServiceModel;
import geacommerce.domain.models.service.ProductServiceModel;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    boolean saveProduct(ProductServiceModel productServiceModel);

    List<ProductServiceModel> findAllProducts();

    ProductServiceModel findProductById(String id);

    boolean deleteProductById(String id);

    boolean updateProduct(ProductServiceModel productServiceModel);

    CartServiceModel updateProductWithCart(ProductServiceModel productServiceModel, CartServiceModel cartServiceModel);

    BigDecimal calculateTotalPrice(Cart cart);
}
