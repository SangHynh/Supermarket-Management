package app;
import java.util.Date;
import java.util.List;

import dao.InvoiceDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Employee;
import model.Invoice;

public class Main {
    public static void main(String[] args) {
        // Khởi tạo EntityManagerFactory từ persistence unit được định nghĩa trong file persistence.xml
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("supermarket_server");

        // Khởi tạo EntityManager từ EntityManagerFactory
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // Khởi tạo InvoiceDAO với EntityManager
        InvoiceDAO invoiceDAO = new InvoiceDAO(entityManager);

        // Lấy danh sách tất cả hóa đơn
        List<Invoice> invoices = invoiceDAO.getAllInvoices();
        System.out.println("Danh sách tất cả hóa đơn:");
        for (Invoice invoice : invoices) {
            System.out.println(invoice.getId());
        }

        // Thêm một hóa đơn mới
        Employee employee = new Employee(1,"1", "1", true, "1", "1", true, "1");
        Invoice newInvoice = new Invoice(employee, new Date());
        // Cài đặt thông tin cho hóa đơn mới
        // newInvoice.setXXX();
        boolean added = invoiceDAO.addInvoice(newInvoice);
        if (added) {
            System.out.println("Hóa đơn đã được thêm thành công.");
        } else {
            System.out.println("Đã xảy ra lỗi khi thêm hóa đơn.");
        }

        // Đóng EntityManager và EntityManagerFactory khi đã sử dụng xong
        entityManager.close();
        entityManagerFactory.close();
    }
}
