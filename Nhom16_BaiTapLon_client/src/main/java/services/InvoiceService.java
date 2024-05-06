package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InvoiceService {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Long addInvoice(int employeeId, String customerName, Date date, double total) {
        try {
            // Tạo một kết nối TCP tới server
            socket = new Socket("localhost", 9000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Gửi loại yêu cầu
            out.println("ADD_INVOICE");

            // Gửi thông tin hóa đơn
            out.println(employeeId);
            out.println(customerName);
            out.println(new SimpleDateFormat("yyyy-MM-dd").format(date)); 
            out.println(total);

            // Đọc phản hồi từ server
            String response = in.readLine();

            // Nếu phản hồi là thành công, đọc ID của hóa đơn từ luồng đầu vào
            if (response.equals("ADD_INVOICE_SUCCESS")) {
                Long invoiceId = Long.parseLong(in.readLine());
                return invoiceId;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                // Đóng kết nối và luồng
                socket.close();
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    
    
    
    
    
}
