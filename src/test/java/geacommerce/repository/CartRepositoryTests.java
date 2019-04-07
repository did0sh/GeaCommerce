package geacommerce.repository;

import geacommerce.domain.entities.Cart;
import geacommerce.domain.entities.Product;
import geacommerce.domain.entities.ProductManufacturer;
import geacommerce.domain.entities.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CartRepositoryTests {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    private Cart cart;

    private Product product;

    @Before
    public void init(){
        product = new Product(){{
            setId("3");
            setCartAmount(1);
            setAmount(1);
            setCarts(new ArrayList<>());
            setCountry("BG");
            setManufacturer(ProductManufacturer.ARCANOL);
            setName("Arcanol");
            setPrice(BigDecimal.TEN);
            setStatus("new");
            setType("grease");
        }};

        Map<String, Product> productMap = new LinkedHashMap<>();
        productMap.put(product.getId(), product);

        cart = new Cart(){{
            setId("1");
            setTotalPrice(BigDecimal.ONE);
            setUser(new User());
            setProducts(productMap);
        }};
    }


    @Test
    public void cartRepository_deleteCartProduct_ShouldDeleteProduct(){
        this.cartRepository.saveAndFlush(cart);
        Assert.assertEquals(this.cartRepository.count(), 1);
    }
}
