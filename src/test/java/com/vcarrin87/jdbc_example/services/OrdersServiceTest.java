package com.vcarrin87.jdbc_example.services;

import com.vcarrin87.jdbc_example.models.OrderItems;
import com.vcarrin87.jdbc_example.models.Orders;
import com.vcarrin87.jdbc_example.models.Products;
import com.vcarrin87.jdbc_example.repository.CustomerRepository;
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
    private CustomerRepository customerRepository;
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
        ReflectionTestUtils.setField(ordersService, "customerRepository", customerRepository);
        ReflectionTestUtils.setField(ordersService, "orderItemsRepository", orderItemsRepository);
        ReflectionTestUtils.setField(ordersService, "paymentsRepository", paymentsRepository);
        ReflectionTestUtils.setField(ordersService, "productsRepository", productsRepository);
        ReflectionTestUtils.setField(ordersService, "inventoryRepository", inventoryRepository);

        // getReferenceById normally returns a lazy proxy; a plain entity with just the id set is enough here
        when(ordersRepository.getReferenceById(anyInt())).thenAnswer(inv -> {
            Orders order = new Orders();
            order.setOrderId(inv.getArgument(0));
            return order;
        });
        when(productsRepository.getReferenceById(anyInt())).thenAnswer(inv -> {
            Products product = new Products();
            product.setProductId(inv.getArgument(0));
            return product;
        });
    }

    private static OrderItems orderItemFor(int productId, int quantity) {
        OrderItems item = new OrderItems();
        Products product = new Products();
        product.setProductId(productId);
        item.setProduct(product);
        item.setQuantity(quantity);
        return item;
    }

    @Test
    void testPlaceOrder_SingleOrderItem() {
        int customerId = 1;
        String orderStatus = "NEW";
        Date deliveryDate = new Date(System.currentTimeMillis());
        int productId = 10;
        int quantity = 2;
        double price = 50.0;

        List<OrderItems> orderItems = Collections.singletonList(orderItemFor(productId, quantity));

        int generatedOrderId = 100;

        when(ordersRepository.saveWithGeneratedKey(any(Orders.class))).thenReturn(generatedOrderId);
        when(productsRepository.getProductPriceById(productId)).thenReturn(price);

        ordersService.placeOrder(customerId, orderStatus, deliveryDate, orderItems);

        verify(ordersRepository).saveWithGeneratedKey(any(Orders.class));
        verify(productsRepository).getProductPriceById(productId);
        verify(orderItemsRepository).save(argThat(item ->
                item.getOrder().getOrderId() == generatedOrderId
                        && item.getProduct().getProductId() == productId
                        && item.getQuantity() == quantity
                        && item.getPrice() == price * quantity));
        verify(inventoryRepository).updateInventory(productId, -quantity);
        verify(paymentsRepository).save(argThat(payment ->
                payment.getOrder().getOrderId() == generatedOrderId
                        && payment.getAmount() == price * quantity
                        && "CREDIT_CARD".equals(payment.getPaymentMethod())));
    }

    @Test
    void testPlaceOrder_MultipleOrderItems() {
        int customerId = 2;
        String orderStatus = "PROCESSING";
        Date deliveryDate = new Date(System.currentTimeMillis());

        List<OrderItems> orderItems = Arrays.asList(orderItemFor(11, 1), orderItemFor(12, 3));

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
        verify(orderItemsRepository).save(argThat(item ->
                item.getOrder().getOrderId() == generatedOrderId
                        && item.getProduct().getProductId() == 11
                        && item.getQuantity() == 1
                        && item.getPrice() == price1 * 1));
        verify(orderItemsRepository).save(argThat(item ->
                item.getOrder().getOrderId() == generatedOrderId
                        && item.getProduct().getProductId() == 12
                        && item.getQuantity() == 3
                        && item.getPrice() == price2 * 3));
        verify(inventoryRepository).updateInventory(11, -1);
        verify(inventoryRepository).updateInventory(12, -3);
        verify(paymentsRepository).save(argThat(payment ->
                payment.getOrder().getOrderId() == generatedOrderId
                        && payment.getAmount() == price1 * 1 + price2 * 3
                        && "CREDIT_CARD".equals(payment.getPaymentMethod())));
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
        verify(paymentsRepository).save(argThat(payment ->
                payment.getOrder().getOrderId() == generatedOrderId
                        && payment.getAmount() == 0.0
                        && "CREDIT_CARD".equals(payment.getPaymentMethod())));
    }
}