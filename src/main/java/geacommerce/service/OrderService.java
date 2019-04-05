package geacommerce.service;

import geacommerce.domain.models.service.OrderServiceModel;

import java.util.List;

public interface OrderService {

    boolean saveOrder(OrderServiceModel orderServiceModel);

    List<OrderServiceModel> findAllOrders();

    Object[][] getOrderProducts();

    void completeOrder(String orderId);
}
