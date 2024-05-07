package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.Employee;
import services.EmployeeService;
import services.InventoryService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.List;

public class StaffManagementTab extends JPanel implements ActionListener{
	private JTable table;
	private JTextField searchField;
	private JButton searchButton;
	private JTextField staffIdField;
	private JTextField staffNameField;
	private JTextField phoneNumberField;
	private JTextField emailField;
	private ButtonGroup roleGroup;
	private JRadioButton managerRadioButton;
	private JRadioButton cashierRadioButton;
	private ButtonGroup genderGroup;
	private JRadioButton maleRadioButton;
	private JRadioButton femaleRadioButton;
	private ButtonGroup statusGroup;
	private JRadioButton workingRadioButton;
	private JRadioButton resignedRadioButton;
	private JTextField imageField;
	private JButton addButton;
	private JButton updateButton;
	private JButton reloadButton;
	private JButton selectImageButton;
	private final EmployeeService employeeService = new EmployeeService();

	public StaffManagementTab() {
		setLayout(new GridBagLayout());

		// Left panel (Table)
		JPanel leftPanel = new JPanel(new BorderLayout());
		table = new JTable();
		DefaultTableModel model = new DefaultTableModel(
				new String[] { "Mã NV", "Tên", "SĐT", "Email", "Vai trò", "Giới tính", "Trạng thái" }, 0) {
			@Override
			public Class<?> getColumnClass(int columnIndex) {

				return Object.class; // Sử dụng kiểu dữ liệu mặc định cho các cột khác

			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setModel(model);
		table.setRowHeight(30);
		table.setFont(new Font("Arial", Font.PLAIN, 12));

		JScrollPane scrollPane = new JScrollPane(table);
		leftPanel.add(scrollPane, BorderLayout.CENTER);

		GridBagConstraints gbcLeftPanel = new GridBagConstraints();
		gbcLeftPanel.gridx = 0;
		gbcLeftPanel.gridy = 0;
		gbcLeftPanel.weightx = 1.0;
		gbcLeftPanel.weighty = 0.9;
		gbcLeftPanel.fill = GridBagConstraints.BOTH;
		gbcLeftPanel.anchor = GridBagConstraints.NORTH;
		gbcLeftPanel.insets = new Insets(5, 5, 5, 5);
		add(leftPanel, gbcLeftPanel);

		// Search panel
		JPanel searchPanel = new JPanel(new BorderLayout());
		searchField = new JTextField();
		searchButton = new JButton("Tìm kiếm");
		searchPanel.add(searchField, BorderLayout.CENTER);
		searchPanel.add(searchButton, BorderLayout.EAST);

		GridBagConstraints gbcSearchPanel = new GridBagConstraints();
		gbcSearchPanel.gridx = 0;
		gbcSearchPanel.gridy = 1;
		gbcSearchPanel.weightx = 1.0;
		gbcSearchPanel.weighty = 0.05;
		gbcSearchPanel.fill = GridBagConstraints.BOTH;
		gbcSearchPanel.anchor = GridBagConstraints.NORTH;
		gbcSearchPanel.insets = new Insets(0, 5, 5, 5);
		add(searchPanel, gbcSearchPanel);

		// Right panel
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

		// Input fields panel
		JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 5));
		staffIdField = new JTextField();
		staffIdField.setEnabled(false);
		inputPanel.add(new JLabel("Mã nhân viên:"));
		inputPanel.add(staffIdField);

		staffNameField = new JTextField();
		inputPanel.add(new JLabel("Tên nhân viên:"));
		inputPanel.add(staffNameField);

		phoneNumberField = new JTextField();
		inputPanel.add(new JLabel("Số điện thoại:"));
		inputPanel.add(phoneNumberField);

		emailField = new JTextField();
		inputPanel.add(new JLabel("Email:"));
		inputPanel.add(emailField);

//		inputPanel.add(new JLabel("Hình ảnh:"));
//		imageField = new JTextField();
//		imageField.setEnabled(false);
//		imageField.setText("null");
//		inputPanel.add(imageField);

		// Role radio buttons
		JPanel rolePanel = new JPanel(new GridLayout(0, 1));
		rolePanel.setBorder(BorderFactory.createTitledBorder("Vai trò"));
		roleGroup = new ButtonGroup();
		managerRadioButton = new JRadioButton("Quản lý");
		cashierRadioButton = new JRadioButton("Thu ngân");
		roleGroup.add(managerRadioButton);
		roleGroup.add(cashierRadioButton);
		rolePanel.add(managerRadioButton);
		rolePanel.add(cashierRadioButton);
		inputPanel.add(rolePanel);

		// Gender radio buttons
		JPanel genderPanel = new JPanel(new GridLayout(0, 1));
		genderPanel.setBorder(BorderFactory.createTitledBorder("Giới tính"));
		genderGroup = new ButtonGroup();
		maleRadioButton = new JRadioButton("Nam");
		femaleRadioButton = new JRadioButton("Nữ");
		genderGroup.add(maleRadioButton);
		genderGroup.add(femaleRadioButton);
		genderPanel.add(maleRadioButton);
		genderPanel.add(femaleRadioButton);
		inputPanel.add(genderPanel);

		// Status radio buttons
		JPanel statusPanel = new JPanel(new GridLayout(0, 1));
		statusPanel.setBorder(BorderFactory.createTitledBorder("Trạng thái"));
		statusGroup = new ButtonGroup();
		workingRadioButton = new JRadioButton("Đang làm việc");
		resignedRadioButton = new JRadioButton("Đã nghỉ việc");
		statusGroup.add(workingRadioButton);
		statusGroup.add(resignedRadioButton);
		statusPanel.add(workingRadioButton);
		statusPanel.add(resignedRadioButton);
		inputPanel.add(statusPanel);

		rightPanel.add(inputPanel);

		// Buttons panel
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		addButton = new JButton("Thêm nhân viên");
		updateButton = new JButton("Cập nhật");
		reloadButton = new JButton("Làm mới");
//		selectImageButton = new JButton("Chọn hình ảnh");

		buttonPanel.add(addButton);
		buttonPanel.add(updateButton);
		buttonPanel.add(reloadButton);
//		buttonPanel.add(selectImageButton);
		rightPanel.add(buttonPanel);

		GridBagConstraints gbcRightPanel = new GridBagConstraints();
		gbcRightPanel.gridx = 1;
		gbcRightPanel.gridy = 0;
		gbcRightPanel.weightx = 0.3;
		gbcRightPanel.weighty = 1.0;
		gbcRightPanel.fill = GridBagConstraints.BOTH;
		gbcRightPanel.anchor = GridBagConstraints.NORTH;
		gbcRightPanel.insets = new Insets(5, 0, 5, 5);
		add(rightPanel, gbcRightPanel);

		femaleRadioButton.setSelected(true);
		workingRadioButton.setSelected(true);
		cashierRadioButton.setSelected(true);

		loadEmployees();
		
		
		table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                // Lấy chỉ số của hàng được click
                int selectedRow = table.getSelectedRow();
                // Nếu có hàng được click và không phải hàng header
                if (selectedRow >= 0 && selectedRow < table.getRowCount()) {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    // Lấy dữ liệu từ hàng được click và hiển thị lên các trường nhập và chọn radio buttons tương ứng
                    staffIdField.setText(model.getValueAt(selectedRow, 0).toString());
                    staffNameField.setText(model.getValueAt(selectedRow, 1).toString());
                    phoneNumberField.setText(model.getValueAt(selectedRow, 2).toString());
                    emailField.setText(model.getValueAt(selectedRow, 3).toString());
                    String role = model.getValueAt(selectedRow, 4).toString();
                    setSelectedRole(role);
                    String gender = model.getValueAt(selectedRow, 5).toString();
                    setSelectedGender(gender);
                    String status = model.getValueAt(selectedRow, 6).toString();
                    setSelectedStatus(status);
                }
            }
        });
		
		reloadButton.addActionListener(this);
		addButton.addActionListener(this);
		updateButton.addActionListener(this);
	}

	// Phương thức để lấy giá trị được chọn từ các radio buttons
	public String getSelectedRole() {
		if (managerRadioButton.isSelected()) {
			return "Quản lý";
		} else if (cashierRadioButton.isSelected()) {
			return "Thu ngân";
		} else {
			return "";
		}
	}

	public String getSelectedGender() {
		if (maleRadioButton.isSelected()) {
			return "Nam";
		} else if (femaleRadioButton.isSelected()) {
			return "Nữ";
		} else {
			return "";
		}
	}

	public String getSelectedStatus() {
		if (workingRadioButton.isSelected()) {
			return "Đang làm việc";
		} else if (resignedRadioButton.isSelected()) {
			return "Đã nghỉ việc";
		} else {
			return "";
		}
	}

	// Phương thức để đặt giá trị cho các radio buttons
	public void setSelectedRole(String role) {
		if (role.equals("Quản lý")) {
			managerRadioButton.setSelected(true);
		} else if (role.equals("Thu ngân")) {
			cashierRadioButton.setSelected(true);
		}
	}

	public void setSelectedGender(String gender) {
		if (gender.equals("Nam")) {
			maleRadioButton.setSelected(true);
		} else if (gender.equals("Nữ")) {
			femaleRadioButton.setSelected(true);
		}
	}

	public void setSelectedStatus(String status) {
		if (status.equals("Đang làm việc")) {
			workingRadioButton.setSelected(true);
		} else if (status.equals("Đã nghỉ việc")) {
			resignedRadioButton.setSelected(true);
		}
	}

	// Phương thức để xóa lựa chọn của tất cả các radio buttons
	public void clearSelection() {
		roleGroup.clearSelection();
		genderGroup.clearSelection();
		statusGroup.clearSelection();
	}

	private void loadEmployees() {
		List<Employee> employees = employeeService.getAllEmployeesFromServer();
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		// Xóa tất cả các dòng trong bảng
		model.setRowCount(0);
		// Thêm các dòng mới từ danh sách nhân viên
		for (Employee employee : employees) {
			model.addRow(new Object[] { employee.getId(), employee.getName(), employee.getPhone(), employee.getEmail(),
					employee.isRole() ? "Quản lý" : "Thu ngân", employee.isGender() ? "Nam" : "Nữ",
					employee.isStatus() ? "Đang làm việc" : "Đã nghỉ việc",
					// Cần xử lý hình ảnh ở đây nếu bạn muốn hiển thị
			});
		}
	}
	
	public boolean isValidEmail(String email) {
	    // Kiểm tra email có đúng định dạng không
	    String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
	    return email.matches(regex);
	}

	public boolean isValidPhoneNumber(String phoneNumber) {
	    // Kiểm tra số điện thoại có 10 chữ số và bắt đầu bằng số 0 không
	    String regex = "^0\\d{9}$";
	    return phoneNumber.matches(regex);
	}

	public boolean isValidName(String name) {
	    // Kiểm tra tên không chứa số hoặc kí tự đặc biệt
	    return name.matches("[a-zA-Z\\s]+");
	}

	private boolean isValidInput() {
	    // Kiểm tra các trường nhập có hợp lệ không
	    if (staffNameField.getText().isEmpty() || phoneNumberField.getText().isEmpty() || emailField.getText().isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	        return false;
	    } else if (!isValidName(staffNameField.getText())) {
	        JOptionPane.showMessageDialog(this, "Tên không được chứa số và kí tự đặc biệt!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	        return false;
	    } else if (!isValidPhoneNumber(phoneNumberField.getText())) {
	        JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ! Phải có 10 số và bắt đầu bằng số 0.", "Lỗi", JOptionPane.ERROR_MESSAGE);
	        return false;
	    } else if (!isValidEmail(emailField.getText())) {
	        JOptionPane.showMessageDialog(this, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	        return false;
	    }
	    return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==reloadButton) {
			loadEmployees();
			femaleRadioButton.setSelected(true);
			workingRadioButton.setSelected(true);
			cashierRadioButton.setSelected(true);
			staffIdField.setText("");
		    staffNameField.setText("");
		    phoneNumberField.setText("");
		    emailField.setText("");
		}
		else if (e.getSource() == addButton) {
			
			if (!isValidInput()) {
	            return; // Nếu dữ liệu không hợp lệ, không thêm nhân viên
	        }
	        // Lấy dữ liệu từ các trường nhập
	        int id = Integer.parseInt(staffIdField.getText());
	        String name = staffNameField.getText();
	        String phone = phoneNumberField.getText();
	        String email = emailField.getText();
	        boolean role = managerRadioButton.isSelected(); // true nếu là quản lý, false nếu là thu ngân
	        boolean gender = maleRadioButton.isSelected(); // true nếu là nam, false nếu là nữ

	        // Tạo đối tượng Employee từ dữ liệu đã lấy được
	        Employee newEmployee = new Employee(name, phone, role, null, email, gender, "123",true);

	        // Gọi service để thêm nhân viên
	        boolean success = employeeService.addEmployeeToServer(newEmployee);

	        // Kiểm tra kết quả và thông báo cho người dùng
	        if (success) {
	            JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công! Mật khẩu cho nhân viên là 123", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	            // Sau khi thêm thành công, làm mới danh sách nhân viên
	            loadEmployees();
	        } else {
	            JOptionPane.showMessageDialog(this, "Thêm nhân viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	        }

	        // Sau khi thêm nhân viên, xóa trắng các trường nhập
	        staffIdField.setText("");
	        staffNameField.setText("");
	        phoneNumberField.setText("");
	        emailField.setText("");
	        femaleRadioButton.setSelected(true);
			workingRadioButton.setSelected(true);
			cashierRadioButton.setSelected(true);
	    }
		
		
		else if (e.getSource() == updateButton) {
		    try {
		    	if (!isValidInput()) {
			        return; // Nếu dữ liệu không hợp lệ, không cập nhật nhân viên
			    }
			    // Lấy dữ liệu từ các trường nhập
			    int id = Integer.parseInt(staffIdField.getText());
			    String name = staffNameField.getText();
			    String phone = phoneNumberField.getText();
			    String email = emailField.getText();
			    boolean role = managerRadioButton.isSelected(); // true nếu là quản lý, false nếu là thu ngân
			    boolean gender = maleRadioButton.isSelected(); // true nếu là nam, false nếu là nữ
			    boolean status = workingRadioButton.isSelected();

			    // Tạo đối tượng Employee từ dữ liệu đã lấy được
			    Employee updatedEmployee = new Employee(id, name, phone, role, null, email, gender, status);

			    // Gọi service để cập nhật thông tin nhân viên
			    boolean success = employeeService.updateEmployeeFromClient(updatedEmployee);

			    // Kiểm tra kết quả và thông báo cho người dùng
			    if (success) {
			        JOptionPane.showMessageDialog(this, "Cập nhật thông tin nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			        // Sau khi cập nhật thành công, làm mới danh sách nhân viên
			        loadEmployees();
			    } else {
			        JOptionPane.showMessageDialog(this, "Cập nhật thông tin nhân viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			    }

			    // Sau khi cập nhật thông tin nhân viên, xóa trắng các trường nhập
			    staffIdField.setText("");
			    staffNameField.setText("");
			    phoneNumberField.setText("");
			    emailField.setText("");
			    femaleRadioButton.setSelected(true);
			    workingRadioButton.setSelected(true);
			    cashierRadioButton.setSelected(true);
			} catch (Exception e2) {
				JOptionPane.showMessageDialog(null, "Chưa chọn nhân viên để cập nhật");
			}
		}

	}

}
