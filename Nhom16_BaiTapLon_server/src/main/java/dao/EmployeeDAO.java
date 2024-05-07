package dao;


import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.Employee;

public class EmployeeDAO {
    
    private EntityManager entityManager;
    
    public EmployeeDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
    }
    
    //đăng nhập
    public Employee findEmployeeByEmployeeIdAndPassword(int id, String password) {
        try {
            entityManager.getTransaction().begin();
            Employee employee = (Employee) entityManager.createQuery("SELECT e FROM Employee e WHERE e.id = :id AND e.password = :password")
                                    .setParameter("id", id)
                                    .setParameter("password", password)
                                    .getSingleResult();
            entityManager.getTransaction().commit();
         // Kiểm tra nếu nhân viên có status là true (đang làm việc) thì trả về thông tin nhân viên, ngược lại trả về null
            if (employee != null && employee.isStatus()) {
                return employee;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Lấy tất cả danh sách nhân viên
    public List<Employee> getAllEmployees() {
        try {
            entityManager.getTransaction().begin();
            List<Employee> employees = entityManager.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();
            entityManager.getTransaction().commit();
            return employees;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Lấy tất cả danh sách nhân viên theo trạng thái status còn đi làm
    public List<Employee> getActiveEmployees() {
        try {
            entityManager.getTransaction().begin();
            List<Employee> activeEmployees = entityManager.createQuery("SELECT e FROM Employee e WHERE e.status = true", Employee.class).getResultList();
            entityManager.getTransaction().commit();
            return activeEmployees;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
 // Thêm nhân viên mới
    public boolean addEmployee(Employee employee) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(employee); // Thêm nhân viên mới vào cơ sở dữ liệu
            transaction.commit();
            return true; // Trả về true nếu thêm thành công
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback(); // Rollback nếu có lỗi xảy ra
            }
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi xảy ra
        }
    }
    
    
    public boolean updateEmployee(Employee employee) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            
            // Kiểm tra xem trường password có giá trị null không
            if (employee.getPassword() == null) {
                // Lấy thông tin của nhân viên từ cơ sở dữ liệu
                Employee existingEmployee = entityManager.find(Employee.class, employee.getId());
                // Gán lại password từ nhân viên trong cơ sở dữ liệu
                employee.setPassword(existingEmployee.getPassword());
            }
            
            entityManager.merge(employee); // Cập nhật thông tin của nhân viên trong cơ sở dữ liệu
            transaction.commit();
            return true; // Trả về true nếu cập nhật thành công
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback(); // Rollback nếu có lỗi xảy ra
            }
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi xảy ra
        }
    }

 // Cập nhật mật khẩu của nhân viên
    public boolean updateEmployeePassword(int id, String newPassword) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            
            // Lấy thông tin của nhân viên từ cơ sở dữ liệu
            Employee existingEmployee = entityManager.find(Employee.class, id);
            if (existingEmployee != null) {
                // Cập nhật mật khẩu mới cho nhân viên
                existingEmployee.setPassword(newPassword);
                entityManager.merge(existingEmployee);
                transaction.commit();
                return true; // Trả về true nếu cập nhật mật khẩu thành công
            } else {
                return false; // Trả về false nếu không tìm thấy nhân viên
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback(); // Rollback nếu có lỗi xảy ra
            }
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi xảy ra
        }
    }
    
    
}
