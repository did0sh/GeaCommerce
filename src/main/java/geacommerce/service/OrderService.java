package geacommerce.service;

import geacommerce.domain.models.service.OrderServiceModel;

public interface OrderService {

    boolean saveOrder(OrderServiceModel orderServiceModel);
}
