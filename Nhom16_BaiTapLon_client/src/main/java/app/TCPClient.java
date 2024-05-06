package app;

import java.io.*;
import java.net.*;

public class TCPClient {
    public static void main(String[] args) {
        try {
            // Tạo socket kết nối với server trên cổng 9000
            Socket socket = new Socket("localhost", 9000);

            // Tạo luồng vào và ra cho server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Gửi dữ liệu đến server
            out.println("Xin chào từ client!");

            // Đọc dữ liệu từ server và hiển thị
            String serverMessage = in.readLine();
            System.out.println("Dữ liệu từ server: " + serverMessage);

            // Đóng kết nối
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
