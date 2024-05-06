package app;

import java.io.*;
import java.net.*;

import java.util.List;

import dao.InventoryDAO;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Employee;
import model.Inventory;
import services.LoginService;


public class TCPServer {
    public static void main(String[] args) {
        try {
            // Tạo socket server và lắng nghe cổng 9000
            ServerSocket serverSocket = new ServerSocket(9000);
            System.out.println("Server đang chờ kết nối...");
            while (true) {
                // Chấp nhận kết nối từ client
                Socket clientSocket = serverSocket.accept();
                System.out.println("Đã kết nối với client: " + clientSocket.getInetAddress());
                
                // Tạo một luồng riêng biệt để xử lý kết nối của client này
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            // Tạo luồng vào và ra cho client
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Đọc loại yêu cầu từ client
            String requestType = in.readLine();
            System.out.println("Client request: " + requestType);

            if (requestType.equals("LOGIN_REQUEST")) {
                // Đọc tên người dùng và mật khẩu từ client
                int username = Integer.parseInt(in.readLine());
                String password = in.readLine();

                // Kiểm tra đăng nhập bằng service
                EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("supermarket_server");
                LoginService loginService = new LoginService(entityManagerFactory.createEntityManager());
                Employee loggedInEmployee = loginService.login(username, password);

                // Gửi phản hồi về cho client
                if (loggedInEmployee != null) {
                    // Gửi thông tin của Employee đăng nhập thành công về client
                    out.println("LOGIN_SUCCESSFUL");
                    out.println(loggedInEmployee.getId());
                    out.println(loggedInEmployee.getName());
                    out.println(loggedInEmployee.getPhone());
                    out.println(loggedInEmployee.isRole());
                    out.println(loggedInEmployee.getImage());
                    out.println(loggedInEmployee.getEmail());
                    out.println(loggedInEmployee.isGender());
                    out.println(loggedInEmployee.getPassword());
                } else {
                    out.println("LOGIN_FAILED");
                }
            } else if (requestType.equals("GET_INVENTORY_LIST")) {
                // Xử lý yêu cầu lấy danh sách hàng tồn kho
                // Gọi InventoryDAO để lấy danh sách hàng tồn kho từ cơ sở dữ liệu
                EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("supermarket_server");
                InventoryDAO inventoryDAO = new InventoryDAO(entityManagerFactory.createEntityManager());
                List<Inventory> inventoryList = inventoryDAO.getAllInventory();

                // Gửi danh sách hàng về client
                if (inventoryList != null) {
                    // Gửi số lượng hàng tồn kho trước
                    out.println("INVENTORY_LIST_SIZE");
                    out.println(inventoryList.size());

                    // Gửi từng hàng tồn kho
                    for (Inventory inventory : inventoryList) {
                        out.println(inventory.getId());
                        out.println(inventory.getName());
                        out.println(inventory.getPrice());
                        out.println(inventory.getQuantity());
                        out.println(inventory.getImage());
                        
                        System.out.println("---------------------");
                        System.out.println("id: "+inventory.getId());
                        System.out.println("name: "+inventory.getName());
                        System.out.println("price: "+inventory.getPrice());
                        System.out.println("quantity: "+inventory.getQuantity());
                        System.out.println("image: "+inventory.getImage());

                    }
                    // Gửi kết thúc danh sách
                    out.println("END_INVENTORY_LIST");
                } else {
                    // Nếu không có hàng tồn kho, gửi về số lượng 0
                    out.println("INVENTORY_LIST_SIZE");
                    out.println(0);
                }
            }
            else if (requestType.equals("ADD_INVENTORY")) {
                // Đọc thông tin hàng tồn kho từ client
                String name = in.readLine();
                double price = Double.parseDouble(in.readLine());
                int quantity = Integer.parseInt(in.readLine());
                String image = in.readLine();

                // Tạo đối tượng Inventory từ thông tin nhận được
                Inventory newInventory = new Inventory(name, price, quantity, image);

                // Gọi InventoryDAO để thêm hàng tồn kho vào cơ sở dữ liệu
                EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("supermarket_server");
                InventoryDAO inventoryDAO = new InventoryDAO(entityManagerFactory.createEntityManager());
                boolean isSuccess = inventoryDAO.addInventory(newInventory);

                // Gửi phản hồi về cho client
                if (isSuccess) {
                    // Nếu thêm thành công, gửi thông báo về client
                    out.println("ADD_INVENTORY_SUCCESS");
                } else {
                    // Nếu thêm không thành công, gửi thông báo về client
                    out.println("ADD_INVENTORY_FAILED");
                }
            }
            
            else if (requestType.equals("DELETE_INVENTORY")) {
                // Đọc thông tin hàng tồn kho từ client
                String id = in.readLine();

                // Gọi InventoryDAO để thêm hàng tồn kho vào cơ sở dữ liệu
                EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("supermarket_server");
                InventoryDAO inventoryDAO = new InventoryDAO(entityManagerFactory.createEntityManager());
                boolean isSuccess = inventoryDAO.deleteInventoryById(id);

                // Gửi phản hồi về cho client
                if (isSuccess) {
                    // Nếu xóa thành công, gửi thông báo về client
                    out.println("DELETE_INVENTORY_SUCCESS");
                } else {
                    // Nếu xóa không thành công, gửi thông báo về client
                    out.println("DELETE_INVENTORY_FAILED");
                }
            }
            
            else if (requestType.equals("UPDATE_INVENTORY")) {
                // Đọc thông tin hàng tồn kho từ client
                int id = Integer.parseInt(in.readLine());
                String name = in.readLine();
                double price = Double.parseDouble(in.readLine());
                int quantity = Integer.parseInt(in.readLine());
                String image = in.readLine();

                // Tạo đối tượng Inventory từ thông tin nhận được
                Inventory newInventory = new Inventory(id, name, price, quantity, image);

                // Gọi InventoryDAO để thêm hàng tồn kho vào cơ sở dữ liệu
                EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("supermarket_server");
                InventoryDAO inventoryDAO = new InventoryDAO(entityManagerFactory.createEntityManager());
                boolean isSuccess = inventoryDAO.updateInventory(newInventory);

                // Gửi phản hồi về cho client
                if (isSuccess) {
                    // Nếu xóa thành công, gửi thông báo về client
                    out.println("UPDATE_INVENTORY_SUCCESS");
                } else {
                    // Nếu xóa không thành công, gửi thông báo về client
                    out.println("UPDATE_INVENTORY_FAILED");
                }
            }
            
            else if (requestType.equals("SEARCH_INVENTORY")) {
                // Đọc từ khóa tìm kiếm từ client
                String keyword = in.readLine();

                // Xử lý yêu cầu tìm kiếm hàng tồn kho
                // Gọi InventoryDAO để thực hiện tìm kiếm
                EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("supermarket_server");
                InventoryDAO inventoryDAO = new InventoryDAO(entityManagerFactory.createEntityManager());
                List<Inventory> searchResult = inventoryDAO.searchInventoryByName(keyword);

                // Gửi kết quả tìm kiếm về client
                if (searchResult != null) {
                    // Gửi số lượng hàng tìm được trước
                    out.println("SEARCH_RESULT_SIZE");
                    out.println(searchResult.size());

                    // Gửi từng mục tìm kiếm
                    for (Inventory inventory : searchResult) {
                        out.println(inventory.getId());
                        out.println(inventory.getName());
                        out.println(inventory.getPrice());
                        out.println(inventory.getQuantity());
                        out.println(inventory.getImage());
                    }
                    // Gửi kết thúc danh sách
                    out.println("END_SEARCH_RESULT");
                } else {
                    // Nếu không tìm thấy kết quả, gửi về số lượng 0
                    out.println("SEARCH_RESULT_SIZE");
                    out.println(0);
                }
            }


            else {
                // Nếu yêu cầu không hợp lệ, gửi phản hồi về client
                out.println("INVALID_REQUEST");
            }

            // Đóng kết nối
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

