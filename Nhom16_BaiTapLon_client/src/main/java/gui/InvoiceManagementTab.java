package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.Inventory;
import model.Invoice;
import services.InventoryService;
import services.InvoiceDetailService;
import services.InvoiceService;

import java.util.Date;
import java.util.List;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class InvoiceManagementTab extends JPanel implements ActionListener{
	private JTextField searchField;
	private JButton searchButton;
	private JTable table;
	private JTable secondaryTable;
	private JTextField customerNameField;
	private JTextField totalField;
	private JPanel buttonPanel;
	private JButton selectButton;
	private JButton deleteButton;
	private JButton payButton;
	private JButton cancelInvoiceButton;
	private JButton reloadButton;
	private JButton increaseButton;
	private JButton decreaseButton;
	private DefaultTableModel mainTableModel;
	private DefaultTableModel secondaryTableModel;
	private final InvoiceService invoiceService = new InvoiceService();
	private final InvoiceDetailService invoiceDetailService = new InvoiceDetailService();

	public InvoiceManagementTab() {
		setLayout(new GridBagLayout());

		// Search panel
		JPanel searchPanel = new JPanel(new BorderLayout());
		searchField = new JTextField();
		searchButton = new JButton("Tìm kiếm");
		searchButton.addActionListener(this);

		searchPanel.add(searchField, BorderLayout.CENTER);
		searchPanel.add(searchButton, BorderLayout.EAST);

		GridBagConstraints gbcSearchPanel = new GridBagConstraints();
		gbcSearchPanel.gridx = 0;
		gbcSearchPanel.gridy = 0;
		gbcSearchPanel.weightx = 1.0;
		gbcSearchPanel.weighty = 0.05;
		gbcSearchPanel.fill = GridBagConstraints.BOTH;
		gbcSearchPanel.anchor = GridBagConstraints.NORTH;
		gbcSearchPanel.insets = new Insets(5, 5, 5, 5);
		add(searchPanel, gbcSearchPanel);

		// Left panel (Main Table)
		JPanel mainTablePanel = new JPanel(new BorderLayout());
		table = new JTable();
		mainTableModel = new DefaultTableModel(new Object[][] {},
				new String[] { "Mã hóa đơn", "Nhân viên thanh toán", "Tên khách hàng", "Ngày", "Tổng tiền", }) {
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				// Đặt kiểu dữ liệu của mỗi cột là Object
				return Object.class;
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				// Không cho phép chỉnh sửa các ô trong bảng
				return false;
			}
		};

		table.setModel(mainTableModel);
		table.setRowHeight(20);
		table.setFont(new Font("Arial", Font.PLAIN, 12));

		JScrollPane mainScrollPane = new JScrollPane(table);
		mainTablePanel.add(mainScrollPane, BorderLayout.CENTER);

		GridBagConstraints gbcMainTablePanel = new GridBagConstraints();
		gbcMainTablePanel.gridx = 0;
		gbcMainTablePanel.gridy = 1;
		gbcMainTablePanel.weightx = 1.0;
		gbcMainTablePanel.weighty = 0.9;
		gbcMainTablePanel.fill = GridBagConstraints.BOTH;
		gbcMainTablePanel.anchor = GridBagConstraints.NORTH;
		gbcMainTablePanel.insets = new Insets(5, 5, 5, 5);
		add(mainTablePanel, gbcMainTablePanel);

		// Right panel (Input fields and Secondary Table)
		JPanel rightPanel = new JPanel(new BorderLayout());

		// Secondary Table
		secondaryTable = new JTable();
		secondaryTableModel = new DefaultTableModel(
				new String[] { "Mã hóa đơn", "Mã sản phẩm", "Tên sản phẩm", "Đơn giá", "Số lượng", "Tổng" }, 0) {
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return Object.class; // Use default data type for other columns
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Make all cells non-editable
			}
		};
		secondaryTable.setRowHeight(20);
		secondaryTable.setFont(new Font("Arial", Font.PLAIN, 12));
		secondaryTable.setModel(secondaryTableModel);
		secondaryTable.setFont(new Font("Arial", Font.PLAIN, 12));

		JScrollPane secondaryScrollPane = new JScrollPane(secondaryTable);
		rightPanel.add(secondaryScrollPane, BorderLayout.CENTER);

		// Input fields panel
		JPanel inputPanel = new JPanel(new GridLayout(0, 2, 5, 5)); // 0 hàng, 2 cột, khoảng cách giữa các thành phần là
																	// 5 pixel

		// Add text field for customer name
		JLabel customerNameLabel = new JLabel("Tên khách hàng:");
		customerNameField = new JTextField(20);
		inputPanel.add(customerNameLabel); // Thêm label vào panel
		inputPanel.add(customerNameField); // Thêm text field vào panel

		// Add label và text field for total
		JLabel totalLabel = new JLabel("Tổng cộng:");
		totalField = new JTextField(20); // Để hiển thị tổng cộng
		totalField.setEnabled(false); // Không cho phép chỉnh sửa trường tổng cộng
		totalField.setText("0 VNĐ");
		inputPanel.add(totalLabel); // Thêm label vào panel
		inputPanel.add(totalField); // Thêm text field vào panel

		rightPanel.add(inputPanel, BorderLayout.NORTH);

		// Buttons panel
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		deleteButton = new JButton("Xóa hàng");
		reloadButton = new JButton("Làm mới");

		buttonPanel.add(deleteButton);
		buttonPanel.add(reloadButton);
		reloadButton.addActionListener(this);
		rightPanel.add(buttonPanel, BorderLayout.SOUTH);

		GridBagConstraints gbcRightPanel = new GridBagConstraints();
		gbcRightPanel.gridx = 1;
		gbcRightPanel.gridy = 1;
		gbcRightPanel.weightx = 0.3;
		gbcRightPanel.weighty = 1.0;
		gbcRightPanel.fill = GridBagConstraints.BOTH;
		gbcRightPanel.anchor = GridBagConstraints.NORTH;
		gbcRightPanel.insets = new Insets(5, 0, 5, 5);
		add(rightPanel, gbcRightPanel);

		loadInvoiceData();

		table.addMouseListener(new java.awt.event.MouseAdapter() {

			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = table.rowAtPoint(evt.getPoint());
				setCustomerName((String) mainTableModel.getValueAt(row, 2));

				if (row >= 0) { // Kiểm tra nếu click vào cột đầu tiên (Mã hóa đơn)

					String invoiceIdString = (String) mainTableModel.getValueAt(row, 0);
					Long invoiceId = Long.parseLong(invoiceIdString.substring(2));

					// Gọi service để lấy chi tiết hóa đơn dựa trên mã hóa đơn
					List<Object[]> invoiceDetails = invoiceDetailService.getInvoiceDetailsWithProductInfo(invoiceId);

					// Cập nhật bảng phụ (secondaryTable) với các chi tiết hóa đơn
					updateSecondaryTable(invoiceDetails);
				}
			}
		});

		deleteButton.addActionListener(this);
		
	}

	// Phương thức để load danh sách hóa đơn kèm thông tin nhân viên vào bảng
	private void loadInvoiceData() {
		// Xóa dữ liệu cũ trong bảng
		mainTableModel.setRowCount(0);

		// Gọi service để lấy danh sách hóa đơn kèm thông tin nhân viên
		List<Object[]> invoices = invoiceService.getAllInvoicesWithEmployeeInfo();

		// Duyệt qua danh sách và thêm vào bảng
		for (Object[] invoice : invoices) {
			Long invoiceId = (Long) invoice[0];
			String invoiceIdString = String.format("HD%06d", invoiceId);
			String date = (String) invoice[1];
			String total = String.valueOf(invoice[2]);
			String customerName = (String) invoice[3];
			String employeeName = (String) invoice[4];

			// Thêm một hàng mới vào bảng với dữ liệu tương ứng
			mainTableModel.addRow(new Object[] { invoiceIdString, employeeName, customerName, date, total });
		}
	}

	private void updateSecondaryTable(List<Object[]> invoiceDetails) {
	    // Clear the existing data in the secondary table
	    ((DefaultTableModel) secondaryTable.getModel()).setRowCount(0);

	    double total = 0.0;

	    // Add new rows to the secondary table with the invoice details
	    for (Object[] detail : invoiceDetails) {
	        // Ensure that detail array contains all necessary information
	        if (detail.length >= 6) { // Assuming detail array has at least 6 elements
	            String invoiceId = String.valueOf(detail[0]);
	            int invoiceIdInt = Integer.parseInt(invoiceId);
	            String invoiceIdFormatted = String.format("HD%06d", invoiceIdInt);

	            String productId = String.valueOf(detail[1]);
	            int productIdInt = Integer.parseInt(productId);
	            String productIdFormatted = String.format("IN%06d", productIdInt);
	            String productName = String.valueOf(detail[2]);
	            String unitPrice = String.valueOf(detail[3]);
	            String quantity = String.valueOf(detail[4]);
	            String subtotal = String.valueOf(detail[5]);

	            // Add each detail as a new row to the secondary table
	            ((DefaultTableModel) secondaryTable.getModel())
	                    .addRow(new Object[] { invoiceIdFormatted, productIdFormatted, productName, unitPrice, quantity, subtotal });

	            // Calculate total and set customer name
	            total += Double.parseDouble(subtotal);
	        }
	    }

	    // Set customer name and total in the input fields panel
	    setTotal(String.valueOf(total));
	}

	public void setCustomerName(String customerName) {
	    customerNameField.setText(customerName);
	}

	public void setTotal(String total) {
	    totalField.setText(total);
	}
	
	// Phương thức để cập nhật bảng chính với kết quả tìm kiếm
    private void updateMainTable(List<Object[]> searchResult) {
        // Xóa dữ liệu cũ trong bảng chính
        mainTableModel.setRowCount(0);

        // Duyệt qua danh sách kết quả tìm kiếm và thêm vào bảng chính
        for (Object[] invoice : searchResult) {
            Long invoiceId = (Long) invoice[0];
            String invoiceIdString = String.format("HD%06d", invoiceId);
            String date = (String) invoice[1];
            String total = String.valueOf(invoice[2]);
            String customerName = (String) invoice[3];
            String employeeName = (String) invoice[4];

            // Thêm một hàng mới vào bảng chính với dữ liệu tương ứng
            mainTableModel.addRow(new Object[]{invoiceIdString, employeeName, customerName, date, total});
        }
    }

	@Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            // Xử lý khi nút tìm kiếm được nhấn
            String keyword = searchField.getText().trim(); // Lấy từ khóa từ trường tìm kiếm
            if (!keyword.isEmpty()) {
                // Gọi service để tìm kiếm hóa đơn theo từ khóa
                List<Object[]> searchResult = invoiceService.searchInvoicesByKeyword(keyword);
                
                if (searchResult != null) {
                    // Nếu có kết quả, cập nhật bảng chính với kết quả tìm kiếm
                    updateMainTable(searchResult);
                } else {
                    // Nếu không có kết quả, thông báo cho người dùng
                    JOptionPane.showMessageDialog(this, "Không tìm thấy kết quả nào.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                // Nếu trường tìm kiếm trống trả về bình thường
                loadInvoiceData();
            }
        }
        
        if (e.getSource()==reloadButton) {
            loadInvoiceData();
            setCustomerName("");
            setTotal("0 VNĐ");
            secondaryTableModel.setRowCount(0);
        }
        
        
        else if (e.getSource() == deleteButton) {
            // Xử lý khi nút xóa hàng được nhấn
            int selectedRow = secondaryTable.getSelectedRow(); // Lấy chỉ số hàng được chọn trong bảng chi tiết hóa đơn
            if (selectedRow != -1) { // Kiểm tra hàng được chọn
                String invoiceIdString = (String) secondaryTableModel.getValueAt(selectedRow, 0); // Lấy mã hóa đơn từ bảng chi tiết hóa đơn
                String productIdString = (String) secondaryTableModel.getValueAt(selectedRow, 1); // Lấy mã sản phẩm từ bảng chi tiết hóa đơn
                
                // Chuyển đổi chuỗi mã hóa đơn và mã sản phẩm thành dữ liệu số tương ứng
                long invoiceId = Long.parseLong(invoiceIdString.substring(2));
                int productId = Integer.parseInt(productIdString.substring(2));
                
                // Gọi phương thức để xóa chi tiết hóa đơn từ service
                boolean isSuccess = invoiceDetailService.deleteInvoiceDetailFromClient(invoiceId, productId);
                
                // Hiển thị thông báo kết quả cho người dùng
                if (isSuccess) {
                	
                	
                	
                    JOptionPane.showMessageDialog(this, "Đã xóa chi tiết hóa đơn thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    // Lấy giá trị tổng tiền hiện tại của hóa đơn
                    double currentTotal = Double.parseDouble(totalField.getText().replace(" VNĐ", ""));

                    // Lấy giá trị của chi tiết hóa đơn vừa xóa
                    String subtotalString = (String) secondaryTableModel.getValueAt(selectedRow, 5);
                    double deletedSubtotal = Double.parseDouble(subtotalString);

                    // Trừ giá trị của chi tiết hóa đơn vừa xóa khỏi tổng tiền của hóa đơn
                    double newTotal = currentTotal - deletedSubtotal;

                    // Gọi phương thức cập nhật tổng tiền của hóa đơn với giá trị mới tính được
                    boolean updateSuccess = invoiceService.updateInvoiceTotal(invoiceId, newTotal);
                    if (updateSuccess) {
                        setTotal(String.valueOf(newTotal) + " VNĐ");
                    } else {
                        JOptionPane.showMessageDialog(this, "Cập nhật tổng tiền hóa đơn thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                    secondaryTableModel.setRowCount(0);
                    loadInvoiceData();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa chi tiết hóa đơn thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một hàng để xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        } 
        
        
    }


	
}
