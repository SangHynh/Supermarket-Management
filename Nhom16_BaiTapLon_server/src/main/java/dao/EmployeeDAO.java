package dao;


import jakarta.persistence.EntityManager;
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
            return employee;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
