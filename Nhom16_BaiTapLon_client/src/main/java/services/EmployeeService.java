package services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import model.Employee;

public class EmployeeService {

	
	public List<Employee> getAllEmployeesFromServer() {
        List<Employee> employees = new ArrayList<>();

        try {
        	Socket socket = new Socket("localhost", 9000);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));            out.println("GET_ALL_EMPLOYEES");

            // Đọc phản hồi từ server
            String response = in.readLine();

            // Xử lý phản hồi từ server
            if (response.equals("EMPLOYEES_SIZE")) {
                // Đọc kích thước của danh sách nhân viên
                int size = Integer.parseInt(in.readLine());

                // Đọc từng nhân viên và thêm vào danh sách
                for (int i = 0; i < size; i++) {
                    int id = Integer.parseInt(in.readLine());
                    String name = in.readLine();
                    String phone = in.readLine();
                    boolean role = Boolean.parseBoolean(in.readLine());
                    String image = in.readLine();
                    String email = in.readLine();
                    boolean gender = Boolean.parseBoolean(in.readLine());
                    String password = in.readLine();
                    boolean status = Boolean.parseBoolean(in.readLine());

                    // Tạo đối tượng Employee và thêm vào danh sách
                    Employee employee = new Employee(id, name, phone, role, image, email, gender, password, status);
                    employees.add(employee);
                }
            } else if (response.equals("NO_EMPLOYEES")) {
                // Xử lý trường hợp không có nhân viên
                System.out.println("Không có nhân viên.");
            } else {
                // Xử lý trường hợp khác (nếu cần)
                System.out.println("Lỗi khi nhận phản hồi từ server.");
            }
        } catch (Exception e) {
            // Xử lý ngoại lệ khi gửi yêu cầu hoặc nhận phản hồi không thành công
            e.printStackTrace();
        }

        return employees;
    }
	
	
	public boolean addEmployeeToServer(Employee employee) {
        try {
            // Kết nối tới server
            Socket socket = new Socket("localhost", 9000);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Gửi yêu cầu thêm nhân viên mới tới server
            out.println("ADD_EMPLOYEE");
            out.println(employee.getName());
            out.println(employee.getPhone());
            out.println(employee.isRole());
            out.println(employee.getImage());
            out.println(employee.getEmail());
            out.println(employee.isGender());
            out.println(employee.getPassword());
            out.println(employee.isStatus());

            // Đọc phản hồi từ server
            String response = in.readLine();

            // Kiểm tra phản hồi từ server và trả về kết quả tương ứng
            return response.equals("ADD_EMPLOYEE_SUCCESS");
        } catch (Exception e) {
            // Xử lý ngoại lệ khi gửi yêu cầu hoặc nhận phản hồi không thành công
            e.printStackTrace();
            return false;
        }
    }
	
	public boolean updateEmployeeFromClient(Employee employee) {
	    try {
	    	
	    	Socket socket = new Socket("localhost", 9000);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        // Gửi yêu cầu cập nhật thông tin nhân viên tới server
	        out.println("UPDATE_EMPLOYEE");
	        out.println(employee.getId());
	        out.println(employee.getName());
	        out.println(employee.getPhone());
	        out.println(employee.isRole());
	        out.println(employee.getImage());
	        out.println(employee.getEmail());
	        out.println(employee.isGender());
	        out.println(employee.isStatus());

	        // Đọc phản hồi từ server
	        String response = in.readLine();

	        // Kiểm tra phản hồi từ server và trả về kết quả tương ứng
	        return response.equals("UPDATE_EMPLOYEE_SUCCESS");
	    } catch (Exception e) {
	        // Xử lý ngoại lệ khi gửi yêu cầu hoặc nhận phản hồi không thành công
	        e.printStackTrace();
	        return false;
	    }
	}

	public boolean updatePasswordEmployeeOnServer(int id, String newPassword) {
	    try {
	        // Kết nối tới server
	        Socket socket = new Socket("localhost", 9000);
	        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

	        // Gửi yêu cầu cập nhật mật khẩu nhân viên tới server
	        out.println("UPDATE_PASSWORD_EMPLOYEE");
	        out.println(id); // Gửi ID của nhân viên
	        out.println(newPassword); // Gửi mật khẩu mới

	        // Đọc phản hồi từ server
	        String response = in.readLine();
	        System.out.println(response);
	        // Kiểm tra phản hồi từ server và trả về kết quả tương ứng
	        return response.equals("UPDATE_PASSWORD_EMPLOYEE_SUCCESS");
	    } catch (Exception e) {
	        // Xử lý ngoại lệ khi gửi yêu cầu hoặc nhận phản hồi không thành công
	        e.printStackTrace();
	        return false;
	    }
	}

	
}
