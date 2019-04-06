package geacommerce.service;

import geacommerce.domain.entities.Cart;
import geacommerce.domain.entities.User;
import geacommerce.domain.models.service.CartServiceModel;
import geacommerce.domain.models.service.UserServiceModel;
import geacommerce.repository.CartRepository;
import geacommerce.repository.ProductRepository;
import geacommerce.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, ModelMapper modelMapper, ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public boolean saveCart(CartServiceModel cartServiceModel) {
        try {
            Cart cart = this.modelMapper.map(cartServiceModel, Cart.class);
            this.cartRepository.saveAndFlush(cart);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public void deleteCartProduct(String productId, String cartId, UserServiceModel userServiceModel) {
        try {
            this.cartRepository.deleteCartProduct(productId);

            if (this.cartRepository.getCountOfProductsInCart(cartId) == 0){
                User user = this.userRepository.findById(userServiceModel.getId())
                        .orElse(null);
                user.getCart().setProducts(null);
                user.setCart(null);
                this.userRepository.save(user);
                this.cartRepository.deleteById(cartId);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public CartServiceModel findCartById(String id) {
        Cart cart = this.cartRepository.findById(id).orElse(null);
        return this.modelMapper.map(cart, CartServiceModel.class);
    }
}
