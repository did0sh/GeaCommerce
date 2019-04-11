package geacommerce.service;

import geacommerce.domain.entities.Cart;
import geacommerce.domain.entities.Product;
import geacommerce.domain.entities.Role;
import geacommerce.domain.entities.User;
import geacommerce.domain.models.service.CartServiceModel;
import geacommerce.domain.models.service.UserServiceModel;
import geacommerce.repository.CartRepository;
import geacommerce.repository.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CartServiceTests {

    @Autowired
    CartService cartService;

    @MockBean
    CartRepository mockCartRepository;

    @MockBean
    UserRepository mockUserRepository;

    private CartServiceModel cartServiceModel;

    private Cart cart;

    private UserServiceModel userServiceModel;

    private User user;

    @Before
    public void setup(){
        this.cartServiceModel = new CartServiceModel(){{
            setId("test id");
            setProducts(new LinkedHashMap<>());
            setUser(new User());
            setTotalPrice(BigDecimal.TEN);
        }};

        this.cart = new Cart(){{
            setId(cartServiceModel.getId());
            setProducts(cartServiceModel.getProducts());
            setUser(cartServiceModel.getUser());
            setTotalPrice(cartServiceModel.getTotalPrice());
        }};

        this.userServiceModel = new UserServiceModel(){{
            setId("1");
            setAddress("Test address");
            setEmail("test@email.com");
            setCart(new Cart(){{
                setId("cartId");
                setProducts(new LinkedHashMap<>());
            }});
            setFirstName("test");
            setLastName("testName");
            setGender("male");
            setPassword("123");
            setPhoneNumber("+359888123123");
            setRole("Guest");
            setTown("Test town");
        }};

        this.user =  new User(){{
            setId(userServiceModel.getId());
            setAddress(userServiceModel.getAddress());
            setEmail(userServiceModel.getEmail());
            setCart(userServiceModel.getCart());
            setFirstName(userServiceModel.getFirstName());
            setLastName(userServiceModel.getLastName());
            setGender(userServiceModel.getGender());
            setPassword(DigestUtils.sha256Hex(userServiceModel.getPassword()));
            setPhoneNumber(userServiceModel.getPhoneNumber());
            setRole(Role.Guest);
            setTown(userServiceModel.getTown());
        }};

        when(mockCartRepository.getCountOfProductsInCart(cart.getId()))
                .thenReturn(cart.getProducts().size());

        when(mockUserRepository.findById(userServiceModel.getId()))
                .thenReturn(java.util.Optional.ofNullable(user));

        when(mockCartRepository.findById(cartServiceModel.getId()))
                .thenReturn(java.util.Optional.ofNullable(cart));
    }

    @Test
    public void saveCart_whenCartNull_ShouldReturnFalse(){
        boolean isSaved = this.cartService.saveCart(null);
        assertFalse(isSaved);
    }

    @Test
    public void saveCart_whenCartValid_ShouldReturnTrue(){
        boolean isSaved = this.cartService.saveCart(cartServiceModel);
        assertTrue(isSaved);
    }

    @Test
    public void deleteCartProduct_whenProductsInCartNotZero_ShouldRemoveUserCart(){
        this.cartService.deleteCartProduct("test product id", cart.getId(), this.userServiceModel);
        assertNull(user.getCart());
    }

    @Test
    public void deleteCartProduct_whenProductsInCartZero_ShouldDeleteOnlyCartProduct(){
        cart.setProducts(new LinkedHashMap<>(){{
            put("test product id", new Product());
        }});
        user.getCart().setProducts(new LinkedHashMap<>(){{
            put("test user product id", new Product());
        }});

        when(mockCartRepository.getCountOfProductsInCart(cart.getId()))
                .thenReturn(cart.getProducts().size());

        this.cartService.deleteCartProduct("test product id", cart.getId(), this.userServiceModel);
        assertNotNull(user.getCart());
        assertEquals(1, user.getCart().getProducts().size());
    }

    @Test(expected = Exception.class)
    public void findCartByID_whenIDIsDifferent_ShouldThrowException(){
        cartServiceModel.setId("false id");
        CartServiceModel model = this.cartService.findCartById(cartServiceModel.getId());
    }

    @Test
    public void findCartByID_whenIDExists_ShouldReturnCorrectModel(){
        CartServiceModel model = this.cartService.findCartById(cartServiceModel.getId());
        assertEquals(model.getId(), cartServiceModel.getId());
        assertEquals(model.getTotalPrice(), cartServiceModel.getTotalPrice());
        assertEquals(model.getProducts().size(), cartServiceModel.getProducts().size());
        assertEquals(model.getUser(), cartServiceModel.getUser());
    }
}
