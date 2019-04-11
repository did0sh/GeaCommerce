package geacommerce.service;

import geacommerce.domain.entities.Order;
import geacommerce.domain.entities.User;
import geacommerce.domain.models.service.OrderServiceModel;
import geacommerce.repository.OrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderServiceTests {

    @Autowired
    OrderService orderService;

    @MockBean
    OrderRepository mockOrderRepository;

    private OrderServiceModel orderServiceModel;

    private Order order;

    private List<Order> orderList;

    @Before
    public void setup(){
        this.orderServiceModel = new OrderServiceModel(){{
            setId("1");
            setOrderPrice(BigDecimal.valueOf(25));
            setStatus("Test status");
            setOrderDate(LocalDateTime.of(2019, 2, 2, 22, 22, 22));
            setDeliveryAddress("Test address");
            setBuyer(new User());
            setBoughtProducts(new ArrayList<>());
        }};

        this.order = new Order(){{
            setId(orderServiceModel.getId());
            setOrderPrice(orderServiceModel.getOrderPrice());
            setStatus(orderServiceModel.getStatus());
            setOrderDate(orderServiceModel.getOrderDate());
            setDeliveryAddress(orderServiceModel.getDeliveryAddress());
            setBuyer(orderServiceModel.getBuyer());
            setBoughtProducts(orderServiceModel.getBoughtProducts());
        }};

        this.orderList = new ArrayList<>(){{
            add(order); add(order);
        }};

        when(mockOrderRepository.findAll())
                .thenReturn(this.orderList);
    }

    @Test
    public void saveOrder_whenOrderNull_ShouldReturnFalse(){
        boolean isSaved = this.orderService.saveOrder(null);
        assertFalse(isSaved);
    }

    @Test
    public void saveOrder_whenOrderValid_ShouldReturnTrue(){
        boolean isSaved = this.orderService.saveOrder(orderServiceModel);
        assertTrue(isSaved);
    }

    @Test
    public void findAllOrders_whenListEmpty_ShouldReturnEmptyList(){
        this.orderList.clear();
        List<OrderServiceModel> orders = this.orderService.findAllOrders();
        assertTrue(orders.isEmpty());
    }

    @Test
    public void findAllOrders_whenListHasOrders_ShouldReturnCorrectSize(){
        List<OrderServiceModel> orders = this.orderService.findAllOrders();
        assertEquals(2, orders.size());
    }

    @Test
    public void completeOrder_whenIdIsCorrect_ShouldReturnTrue(){
        boolean isDeleted = this.orderService.completeOrder(orderServiceModel.getId());
        assertTrue(isDeleted);
    }
}
