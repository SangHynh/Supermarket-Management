package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import model.Inventory;

public class InventoryService {
    public List<Inventory> getAllInventory() {
        List<Inventory> inventoryList = new ArrayList<>();

        try {
            // Tạo một kết nối TCP tới server
            Socket socket = new Socket("localhost", 9000);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Gửi yêu cầu để lấy danh sách inventory
            System.out.println("Client: Sending request to get inventory list");
            out.println("GET_INVENTORY_LIST");

            // Đọc dữ liệu trả về từ server
            String response = in.readLine();
            System.out.println("Client: Received response: " + response);
            if (response.equals("INVENTORY_LIST_SIZE")) {
                // Đọc số lượng hàng tồn kho từ server
                int size = Integer.parseInt(in.readLine());

                // Đọc từng hàng tồn kho
                for (int i = 0; i < size; i++) {
                    int id = Integer.parseInt(in.readLine());
                    System.out.println("id: "+id);
                    String name = in.readLine();
                    System.out.println("name: "+name);
                    double price = Double.parseDouble(in.readLine());
                    System.out.println("price: "+price);
                    int quantity = Integer.parseInt(in.readLine());
                    System.out.println("quantity: "+quantity);
                    String image = in.readLine();
                    System.out.println("image: "+image);
                    Inventory inventory = new Inventory(id, name, price, quantity, image);
                    inventoryList.add(inventory);
                }
            }

            // Đóng kết nối
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return inventoryList;
    }
    
    public boolean addInventory(String productName, double price, int quantity, String image) {
        try {
            Socket socket = new Socket("localhost", 9000);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Gửi loại yêu cầu
            out.println("ADD_INVENTORY");

            // Gửi thông tin hàng tồn kho
            out.println(productName);
            out.println(price);
            out.println(quantity);
            out.println(image);

            // Đọc phản hồi từ server
            String response = in.readLine();
            if (response.equals("ADD_INVENTORY_SUCCESS")) {
                socket.close();
                return true;
            } else {
                socket.close();
                return false;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    
    public boolean deleteInventoryById(String id) {
        try {
            // Tạo một kết nối TCP tới server
            Socket socket = new Socket("localhost", 9000);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Gửi yêu cầu xóa inventory
            out.println("DELETE_INVENTORY");
            out.println(id); // Gửi ID của hàng tồn kho cần xóa

            // Đọc phản hồi từ server
            String response = in.readLine();

            // Đóng kết nối
            socket.close();
            
            // Trả về true nếu xóa thành công, ngược lại trả về false
            return response.equals("DELETE_INVENTORY_SUCCESS");
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    
    public boolean updateInventory(Inventory inventory) {
        try {
            // Tạo một kết nối TCP tới server
            Socket socket = new Socket("localhost", 9000);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Gửi loại yêu cầu
            out.println("UPDATE_INVENTORY");

            // Gửi thông tin hàng tồn kho cần cập nhật
            out.println(inventory.getId()); // Gửi ID của hàng tồn kho
            out.println(inventory.getName()); // Gửi tên sản phẩm
            out.println(inventory.getPrice()); // Gửi giá
            out.println(inventory.getQuantity()); // Gửi số lượng
            out.println(inventory.getImage()); // Gửi hình ảnh

            // Đọc phản hồi từ server
            String response = in.readLine();

            // Đóng kết nối
            socket.close();
            System.out.println("Server send to client: "+response);
            // Trả về true nếu cập nhật thành công, ngược lại trả về false
            return response.equals("UPDATE_INVENTORY_SUCCESS");
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<Inventory> searchInventory(String keyword) {
        List<Inventory> searchResult = new ArrayList<>();

        try {
            // Tạo một kết nối TCP tới server
            Socket socket = new Socket("localhost", 9000);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Gửi loại yêu cầu
            out.println("SEARCH_INVENTORY");

            // Gửi từ khóa tìm kiếm
            out.println(keyword);

            // Đọc phản hồi từ server
            String response = in.readLine();
            if (response.equals("SEARCH_RESULT_SIZE")) {
                // Đọc số lượng mục tìm kiếm từ server
                int size = Integer.parseInt(in.readLine());

                // Đọc từng mục tìm kiếm
                for (int i = 0; i < size; i++) {
                    int id = Integer.parseInt(in.readLine());
                    String name = in.readLine();
                    double price = Double.parseDouble(in.readLine());
                    int quantity = Integer.parseInt(in.readLine());
                    String image = in.readLine();
                    Inventory inventory = new Inventory(id, name, price, quantity, image);
                    searchResult.add(inventory);
                }
            }

            // Đóng kết nối
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return searchResult;
    }

    
    
}
