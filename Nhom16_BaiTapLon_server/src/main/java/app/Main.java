package app;

import java.util.ArrayList;
import java.util.List;
import dao.InvoiceDetailDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Inventory;
import model.Invoice;
import model.InvoiceDetail;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("supermarket_server");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // Khởi tạo DAO
        InvoiceDetailDAO invoiceDetailDAO = new InvoiceDetailDAO(entityManager);

        // Tạo danh sách InvoiceDetail
        List<InvoiceDetail> invoiceDetails = new ArrayList<>();

        // Thêm các InvoiceDetail vào danh sách
        Invoice invoice = new Invoice(1l);
        Inventory inventory = new Inventory(1);
        invoiceDetails.add(new InvoiceDetail(invoice, inventory, 5, 100)); 
        invoiceDetails.add(new InvoiceDetail(invoice, inventory, 15, 100)); 
        invoiceDetails.add(new InvoiceDetail(invoice, inventory, 25, 100)); 
        invoiceDetails.add(new InvoiceDetail(invoice, inventory, 35, 100)); 


        // Thêm danh sách InvoiceDetail vào cơ sở dữ liệu
        boolean success = invoiceDetailDAO.addInvoiceDetails(invoiceDetails);

        if (success) {
            System.out.println("Thêm thành công các chi tiết hóa đơn.");
        } else {
            System.out.println("Thêm chi tiết hóa đơn không thành công.");
        }

        entityManager.close();
        entityManagerFactory.close();
    }
}


