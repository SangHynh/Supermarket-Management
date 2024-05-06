package gui;

import javax.swing.*;

import model.Employee;
import services.InventoryService;

import java.awt.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import model.Inventory;
import java.util.List;

public class MainFrame extends JFrame {
	private JPanel menuPanel;
	private JPanel contentPanel;
	private CardLayout cardLayout;
	private JLabel userInfoLabel;
	private JButton lastSelectedButton; // Biến để lưu trữ menu item được chọn gần nhất
	private AccountInfoFrame accountInfoFrame; // Biến để lưu trữ tham chiếu đến frame thông tin tài khoản
	private Employee employee;

	public MainFrame(Employee employee) {
		try {
			// Chọn Nimbus Look and Feel
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.employee = employee;
		setTitle("Quản Lý Siêu Thị");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// Logo label
		JLabel logoLabel = new JLabel();
		String logoImagePath = "/images/logomain.png";
		try {
			InputStream inputStream = getClass().getResourceAsStream(logoImagePath);
			BufferedImage logoImage = ImageIO.read(inputStream);
			int newWidth = logoImage.getWidth() / 2;
			int newHeight = logoImage.getHeight() / 2;
			BufferedImage resizedLogoImage = resizeImage(logoImage, newWidth, newHeight);
			logoLabel.setIcon(new ImageIcon(resizedLogoImage));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Vertical menu panel
		menuPanel = new JPanel(new GridLayout(0, 1)); // 0 hàng, 1 cột để nó nằm dọc
		menuPanel.setBackground(new Color(75, 75, 75));

		JButton button1 = createMenuButton("Thông Tin Tài Khoản", "/images/account.png");
		JButton button2 = createMenuButton("Thanh Toán Hóa Đơn", "/images/payment.png");
		JButton button3 = createMenuButton("Quản Lý Hàng Hóa", "/images/inventory.png");
		JButton button4 = createMenuButton("Quản Lý Hóa Đơn", "/images/bill.png");
		JButton button5 = createMenuButton("Quản Lý Nhân Viên", "/images/staff.png");
		JButton button6 = createMenuButton("Đăng xuất", "/images/logout.png");

		menuPanel.add(button1);
		menuPanel.add(button2);
		menuPanel.add(button3);
		menuPanel.add(button4);
		menuPanel.add(button5);
		menuPanel.add(button6);

		// Add logo and menuPanel to the frame's WEST
		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(logoLabel, BorderLayout.NORTH);
		leftPanel.add(menuPanel, BorderLayout.CENTER); // Thay vì thêm menuPanel vào JScrollPane, ta thêm trực tiếp vào
														// leftPanel
		add(leftPanel, BorderLayout.WEST);

		// Right panel for welcome message, role, and contentPanel
		JPanel rightPanel = new JPanel(new BorderLayout());

		// User information label
		userInfoLabel = new JLabel();
		String roleString = employee.isRole() == false ? " thu ngân" : " quản lý";
		String welcomeMessage = "<html> <b>Xin chào: </b> " + employee.getName() + "<br> <b> Chức vụ: </b>" + roleString
				+ "</html>";
		userInfoLabel.setText(welcomeMessage);
		userInfoLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		userInfoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		userInfoLabel.setBackground(new Color(75, 75, 75));
		userInfoLabel.setForeground(new Color(225, 225, 225));
		userInfoLabel.setOpaque(true);

		// Add userInfoLabel to the right panel's NORTH
		rightPanel.add(userInfoLabel, BorderLayout.NORTH);

		// Content panel for tabs
		contentPanel = new JPanel();
		cardLayout = new CardLayout();
		contentPanel.setLayout(cardLayout);

		// Add tabs to contentPanel
		contentPanel.add(new InvoicePaymentTab(), "Thanh Toán Hóa Đơn");
		contentPanel.add(new InventoryManagementTab(), "Quản Lý Hàng Hóa");
		contentPanel.add(new InvoiceManagementTab(), "Quản Lý Hóa Đơn");
		contentPanel.add(new StaffManagementTab(), "Quản Lý Nhân Viên");

		// Add contentPanel to the right panel's CENTER
		rightPanel.add(contentPanel, BorderLayout.CENTER);

		// Add rightPanel to the frame's EAST
		add(rightPanel, BorderLayout.CENTER);

		// Set frame size and visibility
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
	}

	private JButton createMenuButton(String text, String iconPath) {
		JButton button = new JButton(text);
		button.setBackground(new Color(75, 75, 75));
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 1),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		Font buttonFont = new Font("Arial", Font.PLAIN, 16);
		button.setFont(buttonFont);

		// Set icon for the button
		try {
			InputStream inputStream = getClass().getResourceAsStream(iconPath);
			BufferedImage icon = ImageIO.read(inputStream);
			// Resize the image to one-third of its original size
			int newWidth = icon.getWidth() / 2;
			int newHeight = icon.getHeight() / 2;
			BufferedImage resizedIcon = resizeImage(icon, newWidth, newHeight);
			button.setIcon(new ImageIcon(resizedIcon));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Căn văn bản và biểu tượng ảnh về phía trái
		button.setHorizontalAlignment(SwingConstants.LEFT);
		button.setHorizontalTextPosition(SwingConstants.RIGHT);

		button.addActionListener(e -> {
			if (text.equals("Thông Tin Tài Khoản")) {
				if (accountInfoFrame == null || !accountInfoFrame.isVisible()) {
					// Nếu frame thông tin tài khoản chưa được tạo hoặc không hiển thị, tạo mới nó
					// và hiển thị
					accountInfoFrame = new AccountInfoFrame(employee);
					accountInfoFrame.setVisible(true);
				} else {
					// Nếu frame thông tin tài khoản đã hiển thị, đưa nó lên trước màn hình
					accountInfoFrame.toFront();
				}
			} else if (!text.equals("Đăng xuất")) { // Kiểm tra nếu không phải là nút "Đăng xuất"
				if (lastSelectedButton != null) {
					lastSelectedButton.setBackground(new Color(75, 75, 75));
				}
				button.setBackground(new Color(0, 153, 153)); // Màu nền mới khi menu được chọn
				lastSelectedButton = button;

				// Xử lý khi nhấn vào nút "Quản Lý Hàng Hóa"
				if (text.equals("Quản Lý Hàng Hóa")) {
		            // Gọi service để lấy danh sách inventory
		            InventoryService inventoryService = new InventoryService();
		            List<Inventory> inventoryList = inventoryService.getAllInventory();
		            
		            // Xử lý dữ liệu danh sách inventory ở đây (ví dụ: hiển thị trong một table)
		            // Ví dụ:
		            for (Inventory inventory : inventoryList) {
		                System.out.println("ID: " + inventory.getId() + ", Inventory ID: " + inventory.generateInventoryId(inventory.getId())+ ", Name: " + inventory.getName() + ", Price: " + inventory.getPrice() + ", Quantity: " + inventory.getQuantity());
		            }
		        }
				
				
			}
			String command = e.getActionCommand();
			cardLayout.show(contentPanel, command);
		});

		// Kiểm tra vai trò của nhân viên và ẩn hoặc hiển thị các menu phù hợp
		if (text.equals("Quản Lý Hàng Hóa") || text.equals("Quản Lý Hóa Đơn") || text.equals("Quản Lý Nhân Viên")) {
			if (!employee.isRole()) { // Nếu là thu ngân
				button.setVisible(false); // Ẩn menu
			}
		}

		return button;
	}

	// Method to resize an image
	private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
		BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
		g.dispose();
		return resizedImage;
	}

	public static void main(String[] args) {
		// Tạo một đối tượng Employee (đây là một ví dụ, bạn cần có thông tin nhân viên
		// từ server)
		Employee loggedInEmployee = new Employee(/* Thông tin của nhân viên */);

		// Truyền thông tin nhân viên vào MainFrame khi tạo
		SwingUtilities.invokeLater(() -> new MainFrame(loggedInEmployee));
	}

}
