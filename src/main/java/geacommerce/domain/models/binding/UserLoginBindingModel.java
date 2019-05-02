package geacommerce.domain.models.binding;

import geacommerce.common.Constants;
import geacommerce.domain.entities.Cart;

import javax.validation.constraints.*;

public class UserLoginBindingModel {

    private String email;
    private String password;
    private Cart cart;

    public UserLoginBindingModel() {
    }

    @NotNull(message = Constants.NOT_NULL_LOGIN_REGISTER_EMAIL_MESSAGE)
    @NotEmpty(message = Constants.NOT_EMPTY_LOGIN_REGISTER_EMAIL_MESSAGE)
    @Email(regexp = Constants.USER_REGISTER_LOGIN_EMAIL_PATTERN, message = Constants.USER_REGISTER_LOGIN_EMAIL_MESSAGE)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email.trim();
    }

    @Pattern(regexp = Constants.USER_LOGIN_REGISTER_PASSWORD_PATTERN, message = Constants.USER_LOGIN_REGISTER_PASSWORD_INVALID_MESSAGE)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Cart getCart() {
        return this.cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
