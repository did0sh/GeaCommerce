package geacommerce.service;

import geacommerce.domain.entities.Cart;
import geacommerce.domain.entities.Role;
import geacommerce.domain.entities.User;
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

import java.util.LinkedHashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTests {

    @Autowired
    UserService userService;
    @MockBean
    UserRepository mockUserRepository;
    @MockBean
    CartRepository mockCartRepository;

    private UserServiceModel userServiceModel;
    private User user;

    @Before
    public void setup(){
        this.userServiceModel = new UserServiceModel(){{
            setAddress("Test address");
            setEmail("test@email.com");
            setCart(new Cart(){{
                setId("cartId");
                setProducts(new LinkedHashMap<>());
            }});
            setFirstName("test");
            setLastName("testName");
            setGender("male");
            setId("1");
            setPassword("123");
            setPhoneNumber("+359888123123");
            setRole("Guest");
            setTown("Test town");
        }};

        this.user =  new User(){{
            setAddress("Test address");
            setEmail("test@email.com");
            setCart(new Cart(){{
                setId("cartId");
                setProducts(new LinkedHashMap<>());
            }});
            setFirstName("test");
            setLastName("testName");
            setGender("male");
            setId("1");
            setPassword(DigestUtils.sha256Hex("123"));
            setPhoneNumber("+359888123123");
            setRole(Role.Guest);
            setTown("Test town");
        }};

        when(mockUserRepository.findByEmail(userServiceModel.getEmail()))
                .thenReturn(user);

        when(mockUserRepository.findById(userServiceModel.getId()))
                .thenReturn(java.util.Optional.ofNullable(user));

    }

    @Test
    public void registerUser_whenUserNull_ShouldReturnFalse(){
        boolean isRegistered = this.userService.registerUser(null);
        assertFalse(isRegistered);
    }

    @Test
    public void registerUser_whenUserValid_ShouldReturnTrue(){
        boolean isRegistered = this.userService.registerUser(userServiceModel);
        assertTrue(isRegistered);
    }

    @Test(expected = Exception.class)
    public void loginUser_WhenUserNull_ShouldThrowException(){
        UserServiceModel model = this.userService.loginUser(null);
    }

    @Test
    public void loginUser_whenUserEmailDifferent_ShouldReturnNull(){
        userServiceModel.setEmail("Invalid Email");
        UserServiceModel model = this.userService.loginUser(userServiceModel);
        assertNull(model);
    }

    @Test
    public void loginUser_whenUserPasswordDifferent_ShouldReturnNull(){
        userServiceModel.setPassword("Not existing password");
        UserServiceModel model = this.userService.loginUser(userServiceModel);
        assertNull(model);
    }

    @Test
    public void loginUser_whenUserIsValid_ShouldReturnUserServiceModel(){
        UserServiceModel model = this.userService.loginUser(userServiceModel);

        assertEquals(model.getEmail(), user.getEmail());
        assertEquals(model.getId(), user.getId());
        assertEquals(model.getPassword(), user.getPassword());
        assertEquals(model.getFirstName(), user.getFirstName());
        assertEquals(model.getLastName(), user.getLastName());
    }

    @Test
    public void hasAlreadyRegisteredUser_whenUserEmailDifferent_ShouldReturnFalse(){
        userServiceModel.setEmail("Invalid email");
        boolean hasAlreadyRegistered = this.userService.hasAlreadyRegisteredUser(userServiceModel);
        assertFalse(hasAlreadyRegistered);
    }

    @Test
    public void hasAlreadyRegisteredUser_whenUserEmailExists_ShouldReturnTrue(){
        boolean hasAlreadyRegistered = this.userService.hasAlreadyRegisteredUser(userServiceModel);
        assertTrue(hasAlreadyRegistered);
    }

    @Test(expected = Exception.class)
    public void hasAlreadyRegisteredUser_whenUserNull_ShouldThrowException(){
        this.userService.hasAlreadyRegisteredUser(null);
    }

    @Test(expected = Exception.class)
    public void findUserById_whenIdDifferent_ShouldThrowException(){
        userServiceModel.setId("Invalid id");
        this.userService.findUserById(userServiceModel.getId());
    }

    @Test
    public void findUserById_whenIdExists_ShouldReturnUserServiceModel(){
        UserServiceModel userById = this.userService.findUserById(userServiceModel.getId());
        assertEquals(userById.getEmail(), user.getEmail());
        assertEquals(userById.getId(), user.getId());
        assertEquals(userById.getPassword(), user.getPassword());
        assertEquals(userById.getFirstName(), user.getFirstName());
        assertEquals(userById.getLastName(), user.getLastName());
    }

    @Test(expected = Exception.class)
    public void findUserByEmail_whenEmailDifferent_ShouldThrowException(){
        userServiceModel.setEmail("Invalid email");
        this.userService.findUserByEmail(userServiceModel.getEmail());
    }

    @Test
    public void findUserByEmail_whenEmailExists_ShouldReturnUserServiceModel(){
        UserServiceModel userByEmail = this.userService.findUserByEmail(userServiceModel.getEmail());

        assertEquals(userByEmail.getEmail(), user.getEmail());
        assertEquals(userByEmail.getId(), user.getId());
        assertEquals(userByEmail.getPassword(), user.getPassword());
        assertEquals(userByEmail.getFirstName(), user.getFirstName());
        assertEquals(userByEmail.getLastName(), user.getLastName());
    }

    @Test(expected = NullPointerException.class)
    public void removeCart_whenEmailDifferent_ShouldThrowException(){
        userServiceModel.setEmail("Invalid email");
        this.userService.removeCart(userServiceModel, this.userServiceModel.getCart().getId());
    }

    @Test
    public void removeCart_whenEmailExists_ShouldRemoveUserCart(){
        boolean isCartRemoved = this.userService.removeCart(userServiceModel, this.userServiceModel.getCart().getId());
        assertNull(user.getCart());
        assertTrue(isCartRemoved);
    }

    @Test
    public void removeCart_whenUserHasNoCart_ShouldReturnFalse(){
        user.setCart(null);
        boolean isCartRemoved = this.userService.removeCart(userServiceModel, this.userServiceModel.getCart().getId());
        assertFalse(isCartRemoved);
    }
}

