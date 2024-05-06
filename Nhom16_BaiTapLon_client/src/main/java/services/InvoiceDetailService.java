package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import model.InvoiceDetail;

public class InvoiceDetailService {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;


    public boolean addInvoiceDetails(long invoiceId, List<InvoiceDetail> invoiceDetails) {
        try {
        	socket = new Socket("localhost", 9000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Gửi loại yêu cầu
            out.println("ADD_INVOICE_DETAILS");

            // Gửi thông tin hóa đơn
            out.println(invoiceId);
            out.println(invoiceDetails.size());

            // Gửi từng chi tiết hóa đơn
            for (InvoiceDetail detail : invoiceDetails) {
                out.println(detail.getInventory().getId());
                out.println(detail.getQuantity());
                out.println(detail.getTotal());
            }

            // Đọc phản hồi từ server
            String response = in.readLine();

            // Trả về true nếu thêm thành công, ngược lại trả về false
            return response.equals("ADD_INVOICE_DETAILS_SUCCESS");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    public List<Object[]> getInvoiceDetailsWithProductInfo(long invoiceId) {
        List<Object[]> invoiceDetails = new ArrayList<>();
        try {
        	socket = new Socket("localhost", 9000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Gửi yêu cầu lấy thông tin chi tiết hóa đơn với thông tin sản phẩm tới server
            out.println("GET_INVOICE_DETAILS_WITH_PRODUCT_INFO");
            out.println(invoiceId);

            // Đọc phản hồi từ server
            String response = in.readLine();
            if (response.equals("GET_INVOICE_DETAILS_WITH_PRODUCT_INFO_SUCCESS")) {
                // Đọc số lượng chi tiết hóa đơn từ server
                int numberOfDetails = Integer.parseInt(in.readLine());
                // Đọc từng chi tiết hóa đơn từ server và thêm vào danh sách
                for (int i = 0; i < numberOfDetails; i++) {
                    long invoiceIdResponse = Long.parseLong(in.readLine());
                    int productId = Integer.parseInt(in.readLine());
                    String productName = in.readLine();
                    double price = Double.parseDouble(in.readLine());
                    int quantity = Integer.parseInt(in.readLine());
                    double total = Double.parseDouble(in.readLine());
                    Object[] detail = {invoiceIdResponse, productId, productName, price, quantity, total};
                    invoiceDetails.add(detail);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return invoiceDetails;
    }
    
}
