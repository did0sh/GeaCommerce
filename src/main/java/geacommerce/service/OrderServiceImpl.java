package geacommerce.service;

import geacommerce.domain.entities.Order;
import geacommerce.domain.models.service.OrderServiceModel;
import geacommerce.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean saveOrder(OrderServiceModel orderServiceModel) {
        try {
            Order order = this.modelMapper.map(orderServiceModel, Order.class);
            this.orderRepository.save(order);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }
}