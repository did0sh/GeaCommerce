package geacommerce.domain.models.binding;

import geacommerce.common.Constants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class InquiryBindingModel {

    private String userName;
    private String userEmail;
    private String message;

    public InquiryBindingModel() {
    }

    @NotNull(message = Constants.NOT_NULL_REGISTER_NAME_MESSAGE)
    @NotEmpty(message = Constants.NOT_EMPTY_REGISTER_NAME_MESSAGE)
    @Size(min = Constants.INQUIRY_MESSAGE_USERNAME_MINIMUM_SIZE, max = Constants.INQUIRY_MESSAGE_USERNAME_MAXIMUM_SIZE, message = Constants.USER_REGISTER_NAME_SIZE_MESSAGE)
    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName.trim();
    }

    @NotNull(message = Constants.NOT_NULL_LOGIN_REGISTER_EMAIL_MESSAGE)
    @NotEmpty(message = Constants.NOT_EMPTY_LOGIN_REGISTER_EMAIL_MESSAGE)
    @Email(regexp = Constants.USER_REGISTER_LOGIN_EMAIL_PATTERN, message = Constants.USER_REGISTER_LOGIN_EMAIL_MESSAGE)
    public String getUserEmail() {
        return this.userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail.trim();
    }

    @NotNull(message = Constants.NOT_NULL_INQUIRY_MESSAGE)
    @NotEmpty(message = Constants.NOT_EMPTY_INQUIRY_MESSAGE)
    @Size(max = Constants.INQUIRY_MESSAGE_MAXIMUM_SIZE, message = Constants.INQUIRY_MESSAGE_SIZE)
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message.trim();
    }
}
