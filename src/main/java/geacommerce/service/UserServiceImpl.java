package geacommerce.service;

import geacommerce.domain.entities.Cart;
import geacommerce.domain.entities.Role;
import geacommerce.domain.entities.User;
import geacommerce.domain.models.service.UserServiceModel;
import geacommerce.repository.CartRepository;
import geacommerce.repository.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CartRepository cartRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean registerUser(UserServiceModel userServiceModel) {
        try {
            User user = this.modelMapper.map(userServiceModel, User.class);
            user.setPassword(DigestUtils.sha256Hex(userServiceModel.getPassword()));

            if (this.userRepository.count() == 0) {
                user.setRole(Role.Admin);
            } else {
                user.setRole(Role.Guest);
            }

            this.userRepository.saveAndFlush(user);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public UserServiceModel loginUser(UserServiceModel userServiceModel) {
        User user = this.userRepository.findByEmail(userServiceModel.getEmail());

        if (user == null || !user.getPassword().equals(DigestUtils.sha256Hex(userServiceModel.getPassword()))) {
            return null;
        }

        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public boolean hasAlreadyRegisteredUser(UserServiceModel userServiceModel) {
        User databaseUser = this.userRepository.findByEmail(userServiceModel.getEmail());
        return databaseUser != null;
    }

    @Override
    public UserServiceModel findUserById(String id) {
        User foundUser = this.userRepository.findById(id).orElse(null);
        return this.modelMapper.map(foundUser, UserServiceModel.class);
    }

    @Override
    public void removeCart(UserServiceModel userServiceModel, String cartId) {
        User user = this.userRepository.findByEmail(userServiceModel.getEmail());
        if (user.getCart() != null) {
            user.getCart().setProducts(null);
            user.setCart(null);
            this.userRepository.save(user);
            this.cartRepository.deleteById(cartId);
        }
    }


}
