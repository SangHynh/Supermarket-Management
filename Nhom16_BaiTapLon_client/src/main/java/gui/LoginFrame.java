package gui;

import javax.swing.*;

import model.Employee;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class LoginFrame extends JFrame {
	private JTextField userField;
	private JPasswordField passwordField;
	private static boolean isLoggedIn = false;
	private static LoginFrame loginFrameInstance;

	public LoginFrame() {
		loginFrameInstance = this;

		try {
			// Chọn Nimbus Look and Feel
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		setTitle("Đăng Nhập");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Kích thước hình ảnh thu nhỏ
		int avatarWidth = 350;
		int avatarHeight = 350;

		// Tạo hình ảnh thu nhỏ
		ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/logo.png"));
		Image originalImage = originalIcon.getImage();
		Image scaledImage = originalImage.getScaledInstance(avatarWidth, avatarHeight, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(scaledImage);

		// Thêm hình ảnh vào label
		JLabel avatarLabel = new JLabel(scaledIcon);

		// Panel chứa hình ảnh
		JPanel imagePanel = new JPanel();
		imagePanel.setBackground(Color.WHITE);
		imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));
		imagePanel.add(Box.createVerticalGlue());
		imagePanel.add(avatarLabel);
		imagePanel.add(Box.createVerticalGlue());

		// Panel chứa form đăng nhập
		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBackground(new Color(240, 240, 240));
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(20, 20, 20, 20);

		JLabel titleLabel = new JLabel("HỆ THỐNG QUẢN LÝ SIÊU THỊ");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		titleLabel.setForeground(new Color(70, 130, 180));
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		formPanel.add(titleLabel, constraints);

		JLabel userLabel = new JLabel("Mã nhân viên:");
		userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		formPanel.add(userLabel, constraints);

		userField = new JTextField(20);
		userField.setPreferredSize(new Dimension(0, 40)); // Đặt kích thước ô nhập
		userField.setBorder(BorderFactory.createCompoundBorder( // Sử dụng CompoundBorder để kết hợp LineBorder và
																// EmptyBorder
				BorderFactory.createLineBorder(new Color(70, 130, 180)), // LineBorder với màu xanh
				BorderFactory.createEmptyBorder(5, 15, 5, 15) // EmptyBorder để tạo khoảng trắng xung quanh
		));
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.WEST;
		formPanel.add(userField, constraints);

		JLabel passwordLabel = new JLabel("Mật khẩu:");
		passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.WEST;
		formPanel.add(passwordLabel, constraints);

		passwordField = new JPasswordField(20);
		passwordField.setPreferredSize(new Dimension(0, 40)); // Đặt kích thước ô nhập
		passwordField.setBorder(BorderFactory.createCompoundBorder( // Sử dụng CompoundBorder để kết hợp LineBorder và
																	// EmptyBorder
				BorderFactory.createLineBorder(new Color(70, 130, 180)), // LineBorder với màu xanh
				BorderFactory.createEmptyBorder(5, 15, 5, 15) // EmptyBorder để tạo khoảng trắng xung quanh
		));
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.WEST;
		formPanel.add(passwordField, constraints);

		JButton loginButton = new JButton("Đăng Nhập");
		loginButton.setFont(new Font("Arial", Font.BOLD, 14));
		loginButton.setForeground(Color.WHITE);
		loginButton.setBackground(new Color(75, 75, 75));
		loginButton.setFocusPainted(false);
		loginButton.setBorderPainted(false);
		loginButton.addActionListener(new LoginButtonListener());
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		formPanel.add(loginButton, constraints);

		// Panel chính chứa tất cả các thành phần
		JPanel mainPanel = new JPanel(new BorderLayout());
		add(mainPanel);

		// Thêm panel hình ảnh vào mainPanel bên trái, panel form vào mainPanel bên phải
		mainPanel.add(imagePanel, BorderLayout.WEST);
		mainPanel.add(formPanel, BorderLayout.CENTER);

		// Set kích thước và hiển thị frame
		setSize(850, 500);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	// Lớp nội danh để xử lý sự kiện của nút Đăng Nhập
	private class LoginButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			String usernameString = userField.getText();
			if (usernameString.startsWith("NV")) {

				int username = Integer.parseInt(usernameString.substring(2));
				System.out.println(username);
				String password = new String(passwordField.getPassword());
				System.out.println(password);

				// Gọi phương thức để mở kết nối với server và truyền tên người dùng và mật khẩu
				openConnectionWithServer(username, password);
			}else {
				JOptionPane.showMessageDialog(null, "Đăng nhập thất bại");
			}
		}
	}

	private static void openConnectionWithServer(int username, String password) {
		try {
			// Tạo socket kết nối với server trên cổng 9000
			Socket socket = new Socket("localhost", 9000);

			// Tạo luồng vào và ra cho server
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

			// Gửi yêu cầu đăng nhập đến server
			out.println("LOGIN_REQUEST");
			out.println(username);
			out.println(password);
			out.flush(); // Đảm bảo dữ liệu đã được gửi đi

			// Đọc và xử lý phản hồi từ server
			String response = in.readLine();
			if (response.equals("LOGIN_SUCCESSFUL")) {
				// Đăng nhập thành công
				JOptionPane.showMessageDialog(null, "Đăng nhập thành công!");
				// Đọc thông tin nhân viên từ server
				int id = Integer.parseInt(in.readLine());
				String name = in.readLine();
				String phone = in.readLine();
				boolean role = Boolean.parseBoolean(in.readLine());
				String image = in.readLine();
				String email = in.readLine();
				boolean gender = Boolean.parseBoolean(in.readLine());
				String passwordFromServer = in.readLine();
				// Tạo đối tượng Employee từ thông tin nhận được
				Employee loggedInEmployee = new Employee(id, name, phone, role, image, email, gender,
						passwordFromServer);
				System.out.println(loggedInEmployee.getId());
				System.out.println(loggedInEmployee.getPassword());

				System.out.println(loggedInEmployee.getName());

				MainFrame mainFrame = new MainFrame(loggedInEmployee);
				mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				mainFrame.setVisible(true);
				// Đặt biến isLoggedIn thành true
				isLoggedIn = true;

				// Đóng LoginFrame sau khi đăng nhập thành công
				loginFrameInstance.dispose();
			} else {
				// Đăng nhập thất bại
				JOptionPane.showMessageDialog(null, "Đăng nhập thất bại!");
			}
			// Đóng kết nối
			socket.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		// Sau khi xử lý phản hồi, kiểm tra nếu đăng nhập thành công thì đóng frame hiện
		// tại
	}

	public static void main(String[] args) {
		new LoginFrame();
	}
}
