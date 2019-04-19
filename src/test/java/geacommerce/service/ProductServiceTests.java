package geacommerce.service;

import geacommerce.domain.entities.Cart;
import geacommerce.domain.entities.Product;
import geacommerce.domain.entities.ProductManufacturer;
import geacommerce.domain.models.service.ProductServiceModel;
import geacommerce.repository.CartRepository;
import geacommerce.repository.ProductRepository;
import geacommerce.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductServiceTests {

    @Autowired
    ProductService productService;

    @MockBean
    ProductRepository mockProductRepository;

    @MockBean
    CartRepository mockCartRepository;

    @MockBean
    UserRepository mockUserRepository;

    private ProductServiceModel productServiceModel;
    private Product product;
    private List<Product> productList;

    @Before
    public void setup(){

        this.productServiceModel = new ProductServiceModel(){{
            setId("1");
            setAmount(2);
            setCart(new Cart());
            setPrice(BigDecimal.TEN);
            setCountry("Test country");
            setManufacturer(ProductManufacturer.FAG);
            setName("Test product");
            setStatus("Test status");
            setType("Test type");
        }};

        this.product = new Product(){{
            setId(productServiceModel.getId());
            setAmount(productServiceModel.getAmount());
            setCarts(new ArrayList<>() {{
                add(new Cart());
            }});
            setPrice(productServiceModel.getPrice());
            setCountry(productServiceModel.getCountry());
            setManufacturer(productServiceModel.getManufacturer());
            setName(productServiceModel.getName());
            setStatus(productServiceModel.getStatus());
            setType(productServiceModel.getType());
            setCartAmount(1);
        }};

        this.productList = new ArrayList<>(){{
            add(product); add(product);
        }};

        when(mockProductRepository.findAll())
                .thenReturn(this.productList);

        when(mockProductRepository.findById(productServiceModel.getId()))
                .thenReturn(java.util.Optional.ofNullable(product));
    }

    @Test
    public void saveProduct_whenProductNull_ShouldReturnFalse(){
        boolean isSaved = this.productService.saveProduct(null);
        assertFalse(isSaved);
    }

    @Test
    public void saveProduct_whenProductValid_ShouldReturnTrue(){
        boolean isSaved = this.productService.saveProduct(productServiceModel);
        assertTrue(isSaved);
    }

//    @Test
//    public void findAllProducts_whenListEmpty_ShouldReturnIsEmpty(){
//        this.productList.clear();
//        List<ProductServiceModel> products = this.productService.findAllProducts();
//        assertTrue(products.isEmpty());
//    }
//
//    @Test
//    public void findAllProducts_whenListHasProducts_ShouldReturnCorrectSize(){
//        List<ProductServiceModel> products = this.productService.findAllProducts();
//        assertEquals(2, products.size());
//    }

    @Test
    public void findProductByID_whenIDIsDifferent_ShouldReturnNull(){
        this.productServiceModel.setId("False ID");
        ProductServiceModel model = this.productService.findProductById(productServiceModel.getId());
        assertNull(model);
    }

    @Test
    public void findProductByID_whenIDExists_ShouldReturnProduct(){
        ProductServiceModel model = this.productService.findProductById(productServiceModel.getId());
        assertEquals(model.getId(), productServiceModel.getId());
        assertEquals(model.getAmount(), productServiceModel.getAmount());
        assertEquals(model.getManufacturer(), productServiceModel.getManufacturer());
        assertEquals(model.getName(), productServiceModel.getName());
        assertEquals(model.getPrice(), productServiceModel.getPrice());
    }

    @Test
    public void deleteProductByID_whenIDExists_ShouldReturnTrue(){
        boolean isDeleted = this.productService.deleteProductById(productServiceModel.getId());
        assertTrue(isDeleted);
    }

    @Test
    public void updateProduct_whenProductNull_ShouldReturnFalse(){
        boolean isUpdated = this.productService.updateProduct(null);
        assertFalse(isUpdated);
    }

    @Test
    public void updateProduct_whenProductValid_ShouldReturnTrue(){
        boolean isUpdated = this.productService.updateProduct(productServiceModel);
        assertTrue(isUpdated);
    }

    @Test
    public void calculateTotalPrice_whenCartEmpty_ShouldReturnZero(){
        BigDecimal price = this.productService.calculateTotalPrice(new Cart());
        assertEquals(BigDecimal.ZERO, price);
    }

    @Test
    public void calculateTotalPrice_whenCartHasProducts_ShouldReturnCorrectPrice(){
        Cart cart = new Cart();
        Map<String, Product> products = new LinkedHashMap<>(){{
            put(product.getId(), product);
            put("test id", new Product(){{
                setPrice(BigDecimal.ONE);
                setCartAmount(2);
            }});
        }};
        cart.setProducts(products);

        BigDecimal price = this.productService.calculateTotalPrice(cart);
        assertEquals(BigDecimal.valueOf(12), price);
    }
}
