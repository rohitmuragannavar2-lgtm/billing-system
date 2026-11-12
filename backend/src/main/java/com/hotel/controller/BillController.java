
package com.hotel.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.model.Bill;
import com.hotel.model.BillItem;
import com.hotel.model.CustomerOrder;
import com.hotel.model.OrderItem;
import com.hotel.repo.BillItemRepo;
import com.hotel.repo.BillRepo;
import com.hotel.repo.OrderItemRepo;
import com.hotel.repo.OrderRepo;
import com.hotel.service.CurrentUser;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillRepo bills;
    private final BillItemRepo billItems;
    private final OrderRepo orders;
    private final OrderItemRepo orderItems;
    private final CurrentUser current;
    private final ObjectProvider<JavaMailSender> mailProvider;

    @Value("${app.mail.from:}")
    private String from;

    public BillController(
            BillRepo b,
            BillItemRepo bi,
            OrderRepo o,
            OrderItemRepo oi,
            CurrentUser c,
            ObjectProvider<JavaMailSender> m) {

        this.bills = b;
        this.billItems = bi;
        this.orders = o;
        this.orderItems = oi;
        this.current = c;
        this.mailProvider = m;
    }

    public Map<String, Object> generateForAdmin(Long orderId) {
        return create(orderId);
    }

    @Transactional
    private Map<String, Object> create(Long orderId) {

        Bill existing = bills.findByOrderId(orderId).orElse(null);

        if (existing != null) {
            return view(existing);
        }

        CustomerOrder order = orders.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<OrderItem> lines = orderItems.findByOrderId(orderId);

        BigDecimal subtotal = lines.stream()
                .map(x -> x.lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal tax = subtotal
                .multiply(new BigDecimal("0.05"))
                .setScale(2, RoundingMode.HALF_UP);

        Bill b = new Bill();
        b.order = order;
        b.billNumber = "HB-" + String.format("%06d", orderId);
        b.subtotal = subtotal;
        b.tax = tax;
        b.grandTotal = subtotal.add(tax);

        bills.save(b);

        for (OrderItem x : lines) {

            BillItem bi = new BillItem();

            bi.bill = b;
            bi.itemName = x.itemName;
            bi.quantity = x.quantity;
            bi.unitPrice = x.unitPrice;
            bi.lineTotal = x.lineTotal;

            billItems.save(bi);
        }

        order.status = "BILLED";
        orders.save(order);

        return view(b);
    }

    @GetMapping("/my")
    public List<Map<String, Object>> mine() {

        return bills.findAllByOrder_User_IdOrderByIdDesc(current.get().id)
                .stream()
                .map(this::view)
                .toList();
    }

    @GetMapping
    public List<Map<String, Object>> all() {

        return bills.findAllByOrderByIdDesc()
                .stream()
                .map(this::view)
                .toList();
    }

    @PostMapping("/{id}/email")
    public Map<String, Object> email(@PathVariable Long id) {

        Bill b = bills.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        String to = b.order.user.email;

        if (from == null || from.isBlank()) {
            throw new IllegalStateException(
                    "Configure MAIL_HOST, MAIL_USERNAME, MAIL_PASSWORD and MAIL_FROM to send email"
            );
        }

        StringBuilder body = new StringBuilder();

        body.append("Thank you for dining with us.\n");
        body.append("Bill: ").append(b.billNumber).append("\n\n");

        for (BillItem i : billItems.findByBillId(id)) {

            body.append(i.itemName)
                    .append(" x ")
                    .append(i.quantity)
                    .append(" = ")
                    .append(i.lineTotal)
                    .append("\n");
        }

        body.append("\nSubtotal: ").append(b.subtotal);
        body.append("\nTax (5%): ").append(b.tax);
        body.append("\nTotal: ").append(b.grandTotal);

        JavaMailSender mail = mailProvider.getIfAvailable();

        if (mail == null) {
            throw new IllegalStateException(
                    "Configure SMTP settings before sending email"
            );
        }

        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setFrom(from);
        msg.setTo(to);
        msg.setSubject("Your hotel bill " + b.billNumber);
        msg.setText(body.toString());

        mail.send(msg);

        b.emailedAt = LocalDateTime.now();

        bills.save(b);

        return view(b);
    }

    private Map<String, Object> view(Bill b) {

        List<Map<String, Object>> lines = billItems.findByBillId(b.id)
                .stream()
                .map(i -> Map.<String, Object>of(
                        "itemName", i.itemName,
                        "quantity", i.quantity,
                        "unitPrice", i.unitPrice,
                        "lineTotal", i.lineTotal
                ))
                .toList();

        Map<String, Object> result = new LinkedHashMap<>();

        result.put("id", b.id);
        result.put("billNumber", b.billNumber);
        result.put("orderId", b.order.id);
        result.put("customerName", b.order.user.name);
        result.put("customerEmail", b.order.user.email);
        result.put("subtotal", b.subtotal);
        result.put("tax", b.tax);
        result.put("grandTotal", b.grandTotal);
        result.put("generatedAt",
                b.generatedAt == null ? "" : b.generatedAt.toString());
        result.put("emailedAt",
                b.emailedAt == null ? "" : b.emailedAt.toString());
        result.put("items", lines);

        return result;
    }
}
