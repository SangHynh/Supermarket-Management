package services;

import dao.EmployeeDAO;
import jakarta.persistence.EntityManager;
import model.Employee;

public class LoginService {
    
    private EntityManager entityManager;
    
    public LoginService(EntityManager entityManager) {
		this.entityManager = entityManager;
    }
    
    public Employee login(int employeeId, String password) {
        try {
            // Tạo một đối tượng EmployeeDAO với EntityManager đã được cung cấp
            EmployeeDAO employeeDAO = new EmployeeDAO(entityManager);
            
            // Gọi phương thức findEmployeeByEmployeeIdAndPassword từ EmployeeDAO
            Employee employee = employeeDAO.findEmployeeByEmployeeIdAndPassword(employeeId, password);
            
            // Kiểm tra xem kết quả trả về có khớp với thông tin đăng nhập không
            if (employee != null) {
                return employee;
            } else {
                // Đăng nhập thất bại
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
