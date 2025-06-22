package com.vcarrin87.jdbc_example.repository;

import com.vcarrin87.jdbc_example.models.Customer;
import com.vcarrin87.jdbc_example.models.OrderItems;
import com.vcarrin87.jdbc_example.models.Orders;
import com.vcarrin87.jdbc_example.models.Payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Customer> customerRowMapper = new RowMapper<Customer>() {
        @Override
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            Customer customer = new Customer();
            customer.setCustomerId(rs.getInt("customer_id"));
            customer.setName(rs.getString("name"));
            customer.setEmail(rs.getString("email"));
            customer.setAddress(rs.getString("address"));
            return customer;
        }
    };

    public List<Customer> findAll() {
        String sql = "SELECT * FROM customers";
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    public Customer findById(Long id) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        return jdbcTemplate.queryForObject(sql, customerRowMapper, id);
    }

    public int save(Customer customer) {
        String sql = "INSERT INTO customers (name, email, address) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAddress());
    }

    public int update(Customer customer) {
        String sql = "UPDATE customers SET name = ?, email = ?, address = ? WHERE customer_id = ?";
        return jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAddress(), customer.getCustomerId());
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<Customer> getCustomerWithOrdersAndPayments(int customerId) {
        // This method retrieves customers along with their orders and payments.
        String sql = """
            SELECT c.customer_id AS c_customer_id, c.name, c.email, c.address, 
               o.customer_id AS o_customer_id, o.order_id, o.delivery_date, o.order_status,
               p.payment_id, p.order_id AS p_order_id, p.amount, p.payment_date, p.payment_method,
               oi.orderitem_id, oi.order_id AS oi_order_id, oi.product_id, oi.quantity, oi.price
            FROM customers c 
            LEFT JOIN orders o ON c.customer_id = o.customer_id 
            LEFT JOIN payments p ON o.order_id = p.order_id 
            LEFT JOIN order_items oi ON o.order_id = oi.order_id
            WHERE c.customer_id = ?""";

       return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Customer customer = new Customer();
            customer.setCustomerId(rs.getInt("c_customer_id"));
            customer.setName(rs.getString("name"));
            customer.setEmail(rs.getString("email"));
            customer.setAddress(rs.getString("address"));

            List<Orders> orders = new ArrayList<>();
            do {
                Orders order = new Orders();
                order.setOrderId(rs.getInt("order_id"));
                order.setCustomerId(rs.getInt("o_customer_id"));
                order.setOrderStatus(rs.getString("order_status"));
                order.setDeliveryDate(rs.getDate("delivery_date"));
                orders.add(order);

                OrderItems orderItem = new OrderItems();
                orderItem.setOrderId(rs.getInt("oi_order_id"));
                orderItem.setOrderItemId(rs.getInt("orderitem_id"));
                orderItem.setProductId(rs.getInt("product_id"));
                orderItem.setQuantity(rs.getInt("quantity"));
                orderItem.setPrice(rs.getDouble("price"));
                order.setOrderItems(List.of(orderItem));

                Payments payment = new Payments();
                payment.setPaymentId(rs.getInt("payment_id"));
                payment.setOrderId(rs.getInt("p_order_id"));
                payment.setAmount(rs.getDouble("amount"));
                payment.setPaymentDate(rs.getTimestamp("payment_date"));
                payment.setPaymentMethod(rs.getString("payment_method"));
                order.setPayments(List.of(payment));
            } while (rs.next() && rs.getInt("c_customer_id") == customer.getCustomerId());

            customer.setOrders(orders);
            return customer;
        }, customerId);
    }
}
