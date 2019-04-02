package geacommerce.service;

import geacommerce.domain.models.service.CartServiceModel;
import geacommerce.domain.models.service.ProductServiceModel;

import java.util.List;

public interface ProductService {

    boolean saveProduct(ProductServiceModel productServiceModel);

    List<ProductServiceModel> findAllProducts();

    ProductServiceModel findProductById(String id);

    boolean deleteProductById(String id);

    void updateProduct(ProductServiceModel productServiceModel);

    CartServiceModel updateProductWithCart(ProductServiceModel productServiceModel, CartServiceModel cartServiceModel);
}
