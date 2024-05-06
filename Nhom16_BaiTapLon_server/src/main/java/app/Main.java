package app;

import java.util.Date;
import java.util.List;

import dao.InvoiceDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        // Khởi tạo EntityManagerFactory từ persistence.xml
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("supermarket_server");
        EntityManager entityManager = emf.createEntityManager();

        // Tạo một đối tượng InvoiceDAO với EntityManager đã tạo
        InvoiceDAO invoiceDAO = new InvoiceDAO(entityManager);

        // Lấy danh sách hóa đơn kèm thông tin nhân viên từ InvoiceDAO
        List<Object[]> invoicesWithEmployeeInfo = invoiceDAO.getAllInvoicesWithEmployeeInfo();

        // In ra thông tin của từng hóa đơn kèm thông tin nhân viên
        for (Object[] invoice : invoicesWithEmployeeInfo) {
            long invoiceId = (long) invoice[0];
            Date date = (Date) invoice[1];
            double total = (double) invoice[2];
            String customerName = (String) invoice[3];
            String employeeName = (String) invoice[4];

            System.out.println("Hóa đơn #" + invoiceId + " - Ngày: " + date + " - Tổng tiền: " + total + " - Khách hàng: " + customerName + " - Nhân viên: " + employeeName);
        }

        // Đóng EntityManager khi không cần nữa
        entityManager.close();
        emf.close();
    }
}
