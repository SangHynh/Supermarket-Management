package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;

public class InvoiceManagementTab extends JPanel {
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

	public InvoiceManagementTab() {
    	setLayout(new GridBagLayout());

		// Search panel
		JPanel searchPanel = new JPanel(new BorderLayout());
		searchField = new JTextField();
		searchButton = new JButton("Tìm kiếm");

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
		DefaultTableModel mainTableModel = new DefaultTableModel(
				new String[] { "Mã SP", "Hình ảnh", "Tên SP", "Đơn giá", "Số lượng", "Nguồn" }, 0) {
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				if (columnIndex == 1) {
					return ImageIcon.class; // Use ImageIcon for image column
				} else {
					return Object.class; // Use default data type for other columns
				}
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Make all cells non-editable
			}
		};
		table.setModel(mainTableModel);
		table.setRowHeight(100);
		table.setFont(new Font("Arial", Font.PLAIN, 15));
//		loadInventoryData(mainTableModel);

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
		DefaultTableModel secondaryTableModel = new DefaultTableModel(
				new String[] { "Mã sản phẩm", "Hình ảnh", "Tên sản phẩm", "Đơn giá", "Số lượng", "Tổng" }, 0) {
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				if (columnIndex == 1) {
					return ImageIcon.class; // Use ImageIcon for image column
				} else {
					return Object.class; // Use default data type for other columns
				}
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Make all cells non-editable
			}
		};
		secondaryTable.setRowHeight(80);
		secondaryTable.setFont(new Font("Arial", Font.PLAIN, 15));
		secondaryTable.setModel(secondaryTableModel);
		secondaryTable.setFont(new Font("Arial", Font.PLAIN, 15));

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
		selectButton = new JButton("Chọn hàng");
		deleteButton = new JButton("Xóa hàng");
		payButton = new JButton("Thanh toán");
		cancelInvoiceButton = new JButton("Hủy hóa đơn");
		reloadButton = new JButton("Làm mới");
		increaseButton = new JButton("Thêm số lượng");
		decreaseButton = new JButton("Giảm số lượng");

		buttonPanel.add(selectButton);
		buttonPanel.add(payButton);
		buttonPanel.add(cancelInvoiceButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(reloadButton);
		buttonPanel.add(increaseButton);
		buttonPanel.add(decreaseButton);

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
    }
}
