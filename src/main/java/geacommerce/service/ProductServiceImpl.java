package geacommerce.service;

import geacommerce.domain.entities.Cart;
import geacommerce.domain.entities.Product;
import geacommerce.domain.models.service.CartServiceModel;
import geacommerce.domain.models.service.ProductServiceModel;
import geacommerce.repository.CartRepository;
import geacommerce.repository.ProductRepository;
import geacommerce.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<ProductServiceModel> findAllProducts() {
        return this.productRepository.findAll()
                .stream()
                .map(product -> this.modelMapper.map(product, ProductServiceModel.class))
                .collect(Collectors.toList());
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
    public void deleteProductById(String id) {
        try {
            this.productRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProduct(ProductServiceModel productServiceModel) {
        Product product = this.modelMapper.map(productServiceModel, Product.class);
        this.productRepository.saveAndFlush(product);
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

        } else {

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

    //set discount in every 24h
    @Scheduled(fixedRate = 86_400_000L)
    void generateDiscounts() {
        BigDecimal discount = BigDecimal.valueOf(0.95);
        List<Product> allProducts = this.productRepository.findAll();
        Random random = new Random();
        int randomProductNumber = random.nextInt(allProducts.size());

        Product randomPickedProduct = allProducts.get(randomProductNumber);
        randomPickedProduct.setPrice(randomPickedProduct.getPrice().multiply(discount));

        this.productRepository.save(randomPickedProduct);
    }

    private BigDecimal calculateTotalPrice(Cart userCart) {
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Product userCartProduct : userCart.getProducts().values()) {
            totalPrice = totalPrice.add(userCartProduct.getPrice().multiply(BigDecimal.valueOf(userCartProduct.getCartAmount())));
        }

        return totalPrice;
    }
}
