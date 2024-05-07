package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    
    
 // Phương thức để gửi yêu cầu lấy danh sách hóa đơn kèm thông tin nhân viên từ server
    public List<Object[]> getAllInvoicesWithEmployeeInfo() {
        try {
            Socket socket = new Socket("localhost", 9000);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // Gửi yêu cầu tới server
            out.println("GET_ALL_INVOICES_WITH_EMPLOYEE_INFO");

            // Đọc phản hồi từ server
            String response = in.readLine();
            List<Object[]> invoicesWithEmployeeInfo = new ArrayList<>();

            if (response.equals("INVOICES_WITH_EMPLOYEE_INFO_SIZE")) {
                // Đọc số lượng hóa đơn từ server
                int size = Integer.parseInt(in.readLine());

                // Đọc từng hóa đơn kèm thông tin nhân viên
                for (int i = 0; i < size; i++) {
                    long invoiceId = Long.parseLong(in.readLine());
                    String date = in.readLine();
                    double total = Double.parseDouble(in.readLine());
                    String customerName = in.readLine();
                    String employeeName = in.readLine();

                    Object[] invoiceInfo = new Object[] { invoiceId, date, total, customerName, employeeName };
                    invoicesWithEmployeeInfo.add(invoiceInfo);
                }
            } else {
                System.out.println("Lấy danh sách hóa đơn kèm thông tin nhân viên không thành công");
            }

            return invoicesWithEmployeeInfo;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

 // Phương thức để gửi yêu cầu tìm kiếm hóa đơn theo từ khóa từ server
    public List<Object[]> searchInvoicesByKeyword(String keyword) {
        try {
            Socket socket = new Socket("localhost", 9000);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // Gửi yêu cầu tới server
            out.println("SEARCH_INVOICE");
            // Gửi từ khóa tìm kiếm
            out.println(keyword);

            // Đọc phản hồi từ server
            String response = in.readLine();
            List<Object[]> searchResult = new ArrayList<>();

            if (response.equals("SEARCH_RESULT_SIZE")) {
                // Đọc số lượng kết quả từ server
                int size = Integer.parseInt(in.readLine());

                // Đọc từng hóa đơn tìm kiếm
                for (int i = 0; i < size; i++) {
                    long invoiceId = Long.parseLong(in.readLine());
                    String date = in.readLine();
                    double total = Double.parseDouble(in.readLine());
                    String customerName = in.readLine();
                    String employeeName = in.readLine();

                    Object[] invoiceInfo = new Object[] { invoiceId, date, total, customerName, employeeName };
                    searchResult.add(invoiceInfo);
                }
            } else if (response.equals("NO_SEARCH_RESULT")) {
                System.out.println("Không có kết quả tìm kiếm.");
            } else {
                System.out.println("Tìm kiếm hóa đơn không thành công.");
            }

            return searchResult;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    //cập nhật tổng tiền hóa đơn sau khi xóa chi tiết hóa đơn
    public boolean updateInvoiceTotal(long invoiceId, double total) {
        try {
            Socket socket = new Socket("localhost", 9000);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Gửi yêu cầu tới server
            out.println("UPDATE_TOTAL_FOR_INVOICE");
            // Gửi ID của hóa đơn và tổng tiền mới
            out.println(invoiceId);
            out.println(total);

            // Đọc phản hồi từ server
            String response = in.readLine();
            System.out.println(response);

            // Trả về true nếu cập nhật thành công, ngược lại trả về false
            return response.equals("UPDATE_TOTAL_FOR_INVOICE_SUCCESS");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }




}
