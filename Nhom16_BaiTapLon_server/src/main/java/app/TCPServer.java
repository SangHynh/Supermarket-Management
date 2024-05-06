package app;

import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.InventoryDAO;
import dao.InvoiceDAO;
import dao.InvoiceDetailDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Employee;
import model.Inventory;
import model.Invoice;
import model.InvoiceDetail;
import services.LoginService;


public class TCPServer {
    public static void main(String[] args) {
        try {
            // Tạo socket server và lắng nghe cổng 9000
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("localhost", 9000));
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
            else if (requestType.equals("SEARCH_INVENTORY")) {
                // Đọc từ khóa tìm kiếm từ client
                String keyword = in.readLine();

                // Gọi InventoryDAO để tìm kiếm hàng tồn kho trong cơ sở dữ liệu
                EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("supermarket_server");
                InventoryDAO inventoryDAO = new InventoryDAO(entityManagerFactory.createEntityManager());
                List<Inventory> searchResult = inventoryDAO.searchInventoryByName(keyword);

                // Gửi phản hồi về cho client
                if (!searchResult.isEmpty()) {
                    // Gửi kích thước của kết quả tìm kiếm
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
                } else {
                    // Gửi thông báo không có kết quả tìm kiếm
                    out.println("NO_SEARCH_RESULT");
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
            
            else if (requestType.equals("ADD_INVOICE")) {
                try {
                    // Đọc thông tin hóa đơn từ client
                    int employeeId = Integer.parseInt(in.readLine()); 
                    Employee employee = new Employee(employeeId);
                    String customerName = in.readLine();
                    // Đọc ngày từ client
                    String dateString = in.readLine();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = dateFormat.parse(dateString);
                    double total = Double.parseDouble(in.readLine());

                    // Tạo đối tượng Invoice từ thông tin nhận được
                    Invoice newInvoice = new Invoice();
                    newInvoice.setEmployee(employee); 
                    newInvoice.setDate(date);
                    newInvoice.setCustomerName(customerName);
                    newInvoice.setTotal(total);

                    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("supermarket_server");

                    // Tạo đối tượng InvoiceDAO từ EntityManager
                    InvoiceDAO invoiceDAO = new InvoiceDAO(entityManagerFactory.createEntityManager());

                    // Gọi phương thức addInvoice để thêm hóa đơn vào cơ sở dữ liệu
                    boolean isSuccess = invoiceDAO.addInvoice(newInvoice);

                    // Gửi phản hồi về cho client
                    if (isSuccess) {
                        // Nếu thêm hóa đơn thành công, gửi thông báo về client
                        out.println("ADD_INVOICE_SUCCESS");
                        out.println(newInvoice.getId());                       
                        
                    } else {
                        // Nếu thêm hóa đơn không thành công, gửi thông báo về client
                        out.println("ADD_INVOICE_FAILED");
                    }
                } catch (IOException | ParseException | NumberFormatException e) {
                    // Xử lý ngoại lệ khi đọc dữ liệu từ client hoặc chuyển đổi dữ liệu không thành công
                    out.println("ADD_INVOICE_FAILED");
                    e.printStackTrace();
                }
            }
            
            else if (requestType.equals("ADD_INVOICE_DETAILS")) {
                try {
                    // Đọc thông tin từ client
                    long invoiceId = Integer.parseInt(in.readLine());
                    int numberOfDetails = Integer.parseInt(in.readLine());

                    List<InvoiceDetail> invoiceDetails = new ArrayList<>();

                    // Đọc từng chi tiết hóa đơn từ client và thêm vào danh sách
                    for (int i = 0; i < numberOfDetails; i++) {
                    	int inventoryId = Integer.parseInt(in.readLine());
                        int quantity = Integer.parseInt(in.readLine());
                        double total = Double.parseDouble(in.readLine());

                        // Tạo đối tượng InvoiceDetail từ thông tin nhận được
                        InvoiceDetail invoiceDetail = new InvoiceDetail();
                        invoiceDetail.setInvoice(new Invoice(invoiceId)); 
                        invoiceDetail.setInventory(new Inventory(inventoryId));
                        invoiceDetail.setQuantity(quantity);
                        invoiceDetail.setTotal(total);

                        invoiceDetails.add(invoiceDetail);
                    }

                    // Gọi service để thêm danh sách chi tiết hóa đơn
                    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("supermarket_server");
                    InvoiceDetailDAO invoiceDetailDAO = new InvoiceDetailDAO(entityManagerFactory.createEntityManager());
                    boolean isSuccess = invoiceDetailDAO.addInvoiceDetails(invoiceDetails);

                    // Gửi phản hồi về cho client
                    if (isSuccess) {
                        // Nếu thêm thành công, gửi thông báo về client
                        out.println("ADD_INVOICE_DETAILS_SUCCESS");
                    } else {
                        // Nếu thêm không thành công, gửi thông báo về client
                        out.println("ADD_INVOICE_DETAILS_FAILED");
                    }
                } catch (IOException | NumberFormatException e) {
                    // Xử lý ngoại lệ khi đọc dữ liệu từ client hoặc chuyển đổi dữ liệu không thành công
                    out.println("ADD_INVOICE_DETAILS_FAILED");
                    e.printStackTrace();
                }
            }

            else if (requestType.equals("GET_INVOICE_DETAILS_WITH_PRODUCT_INFO")) {
                try {
                    // Đọc thông tin từ client
                    long invoiceId = Long.parseLong(in.readLine());

                    // Gọi DAO để lấy danh sách chi tiết hóa đơn với thông tin sản phẩm
                    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("supermarket_server");
                    InvoiceDetailDAO invoiceDetailDAO = new InvoiceDetailDAO(entityManagerFactory.createEntityManager());
                    List<Object[]> invoiceDetails = invoiceDetailDAO.getInvoiceDetailsWithProductInfoByInvoiceId(invoiceId);

                    // Gửi phản hồi về cho client
                    if (invoiceDetails != null && !invoiceDetails.isEmpty()) {
                        // Nếu có kết quả, gửi danh sách chi tiết hóa đơn về client
                        out.println("GET_INVOICE_DETAILS_WITH_PRODUCT_INFO_SUCCESS");
                        // Gửi số lượng chi tiết hóa đơn về client
                        out.println(invoiceDetails.size());
                        // Gửi từng chi tiết hóa đơn về client
                        for (Object[] detail : invoiceDetails) {
                            // Chi tiết hóa đơn gồm mã hóa đơn, mã sản phẩm, tên sản phẩm, đơn giá, số lượng và tổng
                            out.println(detail[0]); // Mã hóa đơn
                            out.println(detail[1]); // Mã sản phẩm
                            out.println(detail[2]); // Tên sản phẩm
                            out.println(detail[3]); // Đơn giá
                            out.println(detail[4]); // Số lượng
                            out.println(detail[5]); // Tổng
                        }
                    } else {
                        // Nếu không có kết quả, gửi thông báo về client
                        out.println("GET_INVOICE_DETAILS_WITH_PRODUCT_INFO_FAILED");
                    }
                } catch (IOException | NumberFormatException e) {
                    // Xử lý ngoại lệ khi đọc dữ liệu từ client hoặc chuyển đổi dữ liệu không thành công
                    out.println("GET_INVOICE_DETAILS_WITH_PRODUCT_INFO_FAILED");
                    e.printStackTrace();
                }
            }

            
            else if (requestType.equals("GET_ALL_INVOICES_WITH_EMPLOYEE_INFO")) {
                try {
                    // Gọi DAO để lấy danh sách các hóa đơn với thông tin của nhân viên
                	EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("supermarket_server");
                    InvoiceDAO invoiceDAO = new InvoiceDAO(entityManagerFactory.createEntityManager());
                    List<Object[]> invoicesWithEmployeeInfo = invoiceDAO.getAllInvoicesWithEmployeeInfo();

                    // Gửi số lượng hóa đơn về cho client
                    out.println("INVOICES_WITH_EMPLOYEE_INFO_SIZE");
                    out.println(invoicesWithEmployeeInfo.size());

                    // Gửi từng hóa đơn kèm thông tin của nhân viên tương ứng
                    for (Object[] invoice : invoicesWithEmployeeInfo) {
                        out.println(invoice[0]); // Mã hóa đơn
                        out.println(invoice[1]); // Ngày
                        out.println(invoice[2]); // Tổng tiền
                        out.println(invoice[3]); // Tên khách hàng
                        out.println(invoice[4]); // Tên nhân viên
                    }
                } catch (Exception ex) {
                    // Xử lý ngoại lệ khi có lỗi xảy ra
                    ex.printStackTrace();
                    out.println("GET_ALL_INVOICES_WITH_EMPLOYEE_INFO_FAILED");
                }
            }
            
            
            else if (requestType.equals("SEARCH_INVOICE")) {
                try {
                    // Đọc từ khóa tìm kiếm từ client
                    String keyword = in.readLine();

                    // Gọi InvoiceDAO để tìm kiếm hóa đơn trong cơ sở dữ liệu
                    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("supermarket_server");
                    InvoiceDAO invoiceDAO = new InvoiceDAO(entityManagerFactory.createEntityManager());
                    List<Object[]> searchResult = invoiceDAO.searchInvoicesByKeyword(keyword);

                    // Gửi phản hồi về cho client
                    if (!searchResult.isEmpty()) {
                        // Gửi kích thước của kết quả tìm kiếm
                        out.println("SEARCH_RESULT_SIZE");
                        out.println(searchResult.size());

                        // Gửi từng mục tìm kiếm
                        for (Object[] invoice : searchResult) {
                            out.println(invoice[0]); // Mã hóa đơn
                            out.println(invoice[1]); // Ngày
                            out.println(invoice[2]); // Tổng tiền
                            out.println(invoice[3]); // Tên khách hàng
                            out.println(invoice[4]); // Tên nhân viên
                        }
                    } else {
                        // Gửi thông báo không có kết quả tìm kiếm
                        out.println("NO_SEARCH_RESULT");
                    }
                } catch (IOException e) {
                    // Xử lý ngoại lệ khi đọc dữ liệu từ client hoặc gửi dữ liệu không thành công
                    out.println("SEARCH_INVOICE_FAILED");
                    e.printStackTrace();
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

