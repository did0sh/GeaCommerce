package geacommerce.domain.models.binding;

import geacommerce.common.Constants;

import javax.validation.constraints.*;

public class UserRegisterBindingModel {

    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String phoneNumber;
    private String address;
    private String town;
    private String password;
    private String confirmPassword;

    public UserRegisterBindingModel() {
    }

    @NotNull(message = Constants.NOT_NULL_REGISTER_NAME_MESSAGE)
    @NotEmpty(message = Constants.NOT_EMPTY_REGISTER_NAME_MESSAGE)
    @Size(min = Constants.USER_REGISTER_LOGIN_NAME_MINIMUM_SIZE,
            max = Constants.USER_REGISTER_LOGIN_NAME_MAXIMUM_SIZE,
            message = Constants.USER_REGISTER_NAME_SIZE_MESSAGE)
    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    @NotNull(message = Constants.NOT_NULL_REGISTER_LAST_NAME_MESSAGE)
    @NotEmpty(message = Constants.NOT_EMPTY_REGISTER_LAST_NAME_MESSAGE)
    @Size(min = Constants.USER_REGISTER_LOGIN_NAME_MINIMUM_SIZE,
            max = Constants.USER_REGISTER_LOGIN_NAME_MAXIMUM_SIZE, message = Constants.USER_REGISTER_LAST_NAME_SIZE_MESSAGE)
    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    @NotNull(message = Constants.NOT_NULL_REGISTER_PHONE_MESSAGE)
    @NotEmpty(message = Constants.NOT_EMPTY_REGISTER_PHONE_MESSAGE)
    @Pattern(regexp = Constants.USER_REGISTER_PHONE_PATTERN, message = Constants.USER_REGISTER_PHONE_SIZE_MESSAGE)
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @NotNull(message = Constants.NOT_NULL_REGISTER_ADDRESS_MESSAGE)
    @NotEmpty(message = Constants.NOT_EMPTY_REGISTER_ADDRESS_MESSAGE)
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address.trim();
    }

    @NotNull(message = Constants.NOT_NULL_REGISTER_TOWN_MESSAGE)
    @NotEmpty(message = Constants.NOT_EMPTY_REGISTER_TOWN_MESSAGE)
    public String getTown() {
        return this.town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    @Pattern(regexp = Constants.USER_LOGIN_REGISTER_PASSWORD_PATTERN, message = Constants.USER_LOGIN_REGISTER_PASSWORD_INVALID_MESSAGE)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return this.confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
