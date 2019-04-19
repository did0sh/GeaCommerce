package geacommerce.service;

import geacommerce.domain.entities.Cart;
import geacommerce.domain.entities.Product;
import geacommerce.domain.models.service.CartServiceModel;
import geacommerce.domain.models.service.ProductServiceModel;
import geacommerce.domain.models.view.ProductViewModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ProductService {

    boolean saveProduct(ProductServiceModel productServiceModel);

    Page<Product> findAllProductsByType(Pageable pageable, String productType);

    Page<Product> findAllBySearch(Pageable pageable, String productName);

    ProductServiceModel findProductById(String id);

    boolean deleteProductById(String id);

    boolean updateProduct(ProductServiceModel productServiceModel);

    CartServiceModel updateProductWithCart(ProductServiceModel productServiceModel, CartServiceModel cartServiceModel);

    BigDecimal calculateTotalPrice(Cart cart);
}
