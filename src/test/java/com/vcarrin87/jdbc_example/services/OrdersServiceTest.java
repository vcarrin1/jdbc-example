package com.vcarrin87.jdbc_example.services;

import com.vcarrin87.jdbc_example.models.OrderItems;
import com.vcarrin87.jdbc_example.models.Orders;
import com.vcarrin87.jdbc_example.repository.InventoryRepository;
import com.vcarrin87.jdbc_example.repository.OrderItemsRepository;
import com.vcarrin87.jdbc_example.repository.OrdersRepository;
import com.vcarrin87.jdbc_example.repository.PaymentsRepository;
import com.vcarrin87.jdbc_example.repository.ProductsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrdersServiceTest {

    @InjectMocks
    private OrdersService ordersService;

    @Mock
    private OrdersRepository ordersRepository;
    @Mock
    private OrderItemsRepository orderItemsRepository;
    @Mock
    private PaymentsRepository paymentsRepository;
    @Mock
    private ProductsRepository productsRepository;
    @Mock
    private InventoryRepository inventoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Inject mocks into the service
        ReflectionTestUtils.setField(ordersService, "ordersRepository", ordersRepository);
        ReflectionTestUtils.setField(ordersService, "orderItemsRepository", orderItemsRepository);
        ReflectionTestUtils.setField(ordersService, "paymentsRepository", paymentsRepository);
        ReflectionTestUtils.setField(ordersService, "productsRepository", productsRepository);
        ReflectionTestUtils.setField(ordersService, "inventoryRepository", inventoryRepository);
    }

    @Test
    void testPlaceOrder_SingleOrderItem() {
        int customerId = 1;
        String orderStatus = "NEW";
        Date deliveryDate = new Date(System.currentTimeMillis());
        int productId = 10;
        int quantity = 2;
        double price = 50.0;

        OrderItems item = new OrderItems();
        item.setProductId(productId);
        item.setQuantity(quantity);
        List<OrderItems> orderItems = Collections.singletonList(item);

        int generatedOrderId = 100;

        when(ordersRepository.saveWithGeneratedKey(any(Orders.class))).thenReturn(generatedOrderId);
        when(productsRepository.getProductPriceById(productId)).thenReturn(price);

        ordersService.placeOrder(customerId, orderStatus, deliveryDate, orderItems);

        verify(ordersRepository).saveWithGeneratedKey(any(Orders.class));
        verify(productsRepository).getProductPriceById(productId);
        verify(orderItemsRepository).save(generatedOrderId, productId, quantity, price * quantity);
        verify(inventoryRepository).updateInventory(productId, -quantity);
        verify(paymentsRepository).save(eq(generatedOrderId), eq(price * quantity), any(Timestamp.class), eq("CREDIT_CARD"));
    }

    @Test
    void testPlaceOrder_MultipleOrderItems() {
        int customerId = 2;
        String orderStatus = "PROCESSING";
        Date deliveryDate = new Date(System.currentTimeMillis());

        OrderItems item1 = new OrderItems();
        item1.setProductId(11);
        item1.setQuantity(1);

        OrderItems item2 = new OrderItems();
        item2.setProductId(12);
        item2.setQuantity(3);

        List<OrderItems> orderItems = Arrays.asList(item1, item2);

        int generatedOrderId = 200;
        double price1 = 20.0;
        double price2 = 15.0;

        when(ordersRepository.saveWithGeneratedKey(any(Orders.class))).thenReturn(generatedOrderId);
        when(productsRepository.getProductPriceById(11)).thenReturn(price1);
        when(productsRepository.getProductPriceById(12)).thenReturn(price2);

        ordersService.placeOrder(customerId, orderStatus, deliveryDate, orderItems);

        verify(ordersRepository).saveWithGeneratedKey(any(Orders.class));
        verify(productsRepository).getProductPriceById(11);
        verify(productsRepository).getProductPriceById(12);
        verify(orderItemsRepository).save(generatedOrderId, 11, 1, price1 * 1);
        verify(orderItemsRepository).save(generatedOrderId, 12, 3, price2 * 3);
        verify(inventoryRepository).updateInventory(11, -1);
        verify(inventoryRepository).updateInventory(12, -3);
        verify(paymentsRepository).save(eq(generatedOrderId), eq(price1 * 1 + price2 * 3), any(Timestamp.class), eq("CREDIT_CARD"));
    }

    @Test
    void testPlaceOrder_EmptyOrderItems() {
        int customerId = 3;
        String orderStatus = "NEW";
        Date deliveryDate = new Date(System.currentTimeMillis());
        List<OrderItems> orderItems = Collections.emptyList();
        int generatedOrderId = 300;

        when(ordersRepository.saveWithGeneratedKey(any(Orders.class))).thenReturn(generatedOrderId);

        ordersService.placeOrder(customerId, orderStatus, deliveryDate, orderItems);

        verify(ordersRepository).saveWithGeneratedKey(any(Orders.class));
        verifyNoInteractions(productsRepository);
        verifyNoInteractions(orderItemsRepository);
        verifyNoInteractions(inventoryRepository);
        verify(paymentsRepository).save(eq(generatedOrderId), eq(0.0), any(Timestamp.class), eq("CREDIT_CARD"));
    }
}