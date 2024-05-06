package dao;


import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Inventory;

public class InventoryDAO {
    private EntityManager entityManager;

    
    public InventoryDAO() {
    }
    
    public InventoryDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
    }

    public List<Inventory> getAllInventory() {
        String jpql = "SELECT i FROM Inventory i";
        TypedQuery<Inventory> query = entityManager.createQuery(jpql, Inventory.class);
        return query.getResultList();
    }
    
    public boolean addInventory(Inventory inventory) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(inventory);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteInventoryById(String idString) {
        try {
            // Loại bỏ hai ký tự đầu tiên ("IN") và sau đó chuyển đổi chuỗi thành số nguyên
            int id = Integer.parseInt(idString.substring(2));

            entityManager.getTransaction().begin();
            Inventory inventory = entityManager.find(Inventory.class, id);
            if (inventory != null) {
                entityManager.remove(inventory);
                entityManager.getTransaction().commit();
                return true;
            } else {
                // Không tìm thấy bản ghi có ID tương ứng
                entityManager.getTransaction().rollback();
                return false;
            }
        } catch (NumberFormatException ex) {
            // Nếu không thể chuyển đổi chuỗi thành số nguyên
            System.err.println("Invalid ID format: " + idString);
            ex.printStackTrace();
            return false;
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean updateInventory(Inventory updatedInventory) {
        try {
            entityManager.getTransaction().begin();
            Inventory existingInventory = entityManager.find(Inventory.class, updatedInventory.getId());
            if (existingInventory != null) {
                // Update thông tin của existingInventory bằng thông tin từ updatedInventory
                existingInventory.setName(updatedInventory.getName());
                existingInventory.setPrice(updatedInventory.getPrice());
                existingInventory.setQuantity(updatedInventory.getQuantity());
                existingInventory.setImage(updatedInventory.getImage());

                entityManager.getTransaction().commit();
                return true;
            } else {
                // Không tìm thấy bản ghi có ID tương ứng
                entityManager.getTransaction().rollback();
                return false;
            }
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            ex.printStackTrace();
            return false;
        }
    }

    public List<Inventory> searchInventoryByName(String keyword) {
        try {
            // Tạo câu truy vấn JPQL để tìm kiếm Inventory theo tên
            String jpql = "SELECT i FROM Inventory i WHERE i.name LIKE :keyword";
            TypedQuery<Inventory> query = entityManager.createQuery(jpql, Inventory.class);
            query.setParameter("keyword", "%" + keyword + "%");

            // Thực hiện truy vấn và trả về kết quả
            return query.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }




}
