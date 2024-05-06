package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
}
