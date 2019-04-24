package geacommerce.service;

import geacommerce.common.Constants;
import geacommerce.domain.entities.Cart;
import geacommerce.domain.entities.Product;
import geacommerce.domain.models.service.CartServiceModel;
import geacommerce.domain.models.service.ProductServiceModel;
import geacommerce.domain.models.view.ProductViewModel;
import geacommerce.repository.CartRepository;
import geacommerce.repository.ProductRepository;
import geacommerce.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, CartRepository cartRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean saveProduct(ProductServiceModel productServiceModel) {
        try {
            Product product = this.modelMapper.map(productServiceModel, Product.class);
            this.productRepository.saveAndFlush(product);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public Page<Product> findAllProductsByType(Pageable pageable, String productType) {
        switch (productType){
            case Constants.BEARING_TYPE:
                return this.productRepository.findAll(pageable, Constants.BEARING_TYPE);
            case Constants.BELT_TYPE:
                return this.productRepository.findAll(pageable, Constants.BELT_TYPE);
            case Constants.SEAL_TYPE:
                return this.productRepository.findAll(pageable, Constants.SEAL_TYPE);
            case Constants.OTHER_TYPE:
                return this.productRepository.findAll(pageable, Constants.OTHER_TYPE);
                default:
                    return this.productRepository.findAll(pageable);
        }
    }

    @Override
    public Page<Product> findAllBySearch(Pageable pageable, String productName) {
        return this.productRepository.findAllBySearchInput(pageable, productName);
    }

    @Override
    public ProductServiceModel findProductById(String id) {
        Product productById = this.productRepository.findById(id).orElse(null);
        try {
            ProductServiceModel model = this.modelMapper
                    .map(productById, ProductServiceModel.class);
            return model;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean deleteProductById(String id) {
        try {
            this.productRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean updateProduct(ProductServiceModel productServiceModel) {
        try {
            Product product = this.modelMapper.map(productServiceModel, Product.class);
            this.productRepository.saveAndFlush(product);
        }catch (Exception e){
            return false;
        }

        return true;
    }
    
    @Override
    public CartServiceModel updateProductWithCart(ProductServiceModel productServiceModel, CartServiceModel cartServiceModel) {
        Product productForCart = this.modelMapper.map(productServiceModel, Product.class);
        Product productDatabase = this.productRepository.findById(productForCart.getId())
                .orElse(null);

        Cart cart = this.modelMapper.map(cartServiceModel, Cart.class);

        Cart userCart = this.cartRepository.findAll()
                .stream().filter(cart1 -> cart1.getUser().getId().equals(cart.getUser().getId()))
                .findFirst().orElse(null);

        if (userCart == null) {
           return this.setAndSaveUserWithNewCart(cart, productDatabase, productForCart);
        } else {
            return this.updateCartProductAmountAndSave(userCart, cart, productDatabase, productForCart);
        }
    }

    //set discount in every 24h
    @Scheduled(fixedRate = 86_400_000L)
    void generateDiscounts() {
        BigDecimal discount = BigDecimal.valueOf(0.95);
        List<Product> allProducts = this.productRepository.findAll();

        if (allProducts.size() != 0){
            Random random = new Random();
            int randomProductNumber = random.nextInt(allProducts.size());

            Product randomPickedProduct = allProducts.get(randomProductNumber);
            randomPickedProduct.setPrice(randomPickedProduct.getPrice().multiply(discount));

            this.productRepository.save(randomPickedProduct);
        }
    }

    @Override
    public BigDecimal calculateTotalPrice(Cart userCart) {
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Product userCartProduct : userCart.getProducts().values()) {
            totalPrice = totalPrice.add(userCartProduct.getPrice().multiply(BigDecimal.valueOf(userCartProduct.getCartAmount())));
        }

        return totalPrice;
    }

    private CartServiceModel setAndSaveUserWithNewCart(Cart cart, Product productDatabase, Product productForCart){
        //add cart for user
        cart.getUser().setCart(cart);

        //set product cart amount
        productDatabase.getCarts().add(cart);
        productDatabase.setCartAmount(productForCart.getAmount());

        //set cart total price
        cart.setTotalPrice(productForCart.getPrice().multiply(BigDecimal.valueOf(productForCart.getAmount())));

        //save cart & user
        this.cartRepository.saveAndFlush(cart);
        this.userRepository.save(cart.getUser());

        return this.modelMapper.map(cart, CartServiceModel.class);
    }

    private CartServiceModel updateCartProductAmountAndSave(Cart userCart, Cart cart, Product productDatabase, Product productForCart){
        //update cart product amount if product already exists

        if (userCart.getProducts().containsKey(productForCart.getId())) {
            Product foundProduct = userCart.getProducts().get(productForCart.getId());
            foundProduct.setCartAmount(foundProduct.getCartAmount() + productForCart.getAmount());
        } else {
            productDatabase.getCarts().add(cart);
            productDatabase.setCartAmount(productForCart.getAmount());
            userCart.getProducts().put(productDatabase.getId(), productDatabase);
        }

        //calculate cart total price
        BigDecimal totalPrice = this.calculateTotalPrice(userCart);

        //save cart & user
        cart.getUser().setCart(userCart);
        userCart.setTotalPrice(totalPrice);
        this.cartRepository.saveAndFlush(userCart);
        this.userRepository.save(cart.getUser());

        return this.modelMapper.map(userCart, CartServiceModel.class);
    }
}
