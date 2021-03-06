package geacommerce.service;

import geacommerce.domain.entities.Order;
import geacommerce.domain.models.service.OrderServiceModel;
import geacommerce.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<OrderServiceModel> findAllOrders() {
        return this.orderRepository.findAll()
                .stream()
                .map(order -> this.modelMapper.map(order, OrderServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public Object[][] getOrderProducts() {
        return this.orderRepository.getOrderProductAmountAndProductName();
    }

    @Override
    public boolean completeOrder(String orderId) {
        try {
            Order currentOrder = this.orderRepository.findById(orderId).orElse(null);
            currentOrder.setStatus("Изпълнена");
            this.orderRepository.save(currentOrder);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Scheduled(fixedRate = 172_800_000L)
    void removeDeliveredOrders() {
        List<Order> deliveredOrders = this.orderRepository.findAll()
                .stream().filter(order -> order.getStatus().equals("Изпълнена"))
                .collect(Collectors.toList());

        if (!deliveredOrders.isEmpty()){
            deliveredOrders.forEach(this.orderRepository::delete);
        }
    }
}
