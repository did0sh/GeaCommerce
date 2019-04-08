package geacommerce.service;

import geacommerce.domain.models.service.UserServiceModel;

public interface UserService {

    boolean registerUser(UserServiceModel userServiceModel);

    UserServiceModel loginUser(UserServiceModel userServiceModel);

    boolean hasAlreadyRegisteredUser(UserServiceModel userServiceModel);

    UserServiceModel findUserById(String id);

    boolean removeCart(UserServiceModel userServiceModel, String cartId);

    UserServiceModel findUserByEmail(String email);
}
