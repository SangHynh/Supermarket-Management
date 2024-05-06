package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.Employee;
import model.Inventory;
import model.InvoiceDetail;
import services.InventoryService;
import services.InvoiceDetailService;
import services.InvoiceService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvoicePaymentTab extends JPanel implements ActionListener {
	private JTable table;
	private JTable secondaryTable;
	private final InventoryService inventoryService = new InventoryService();
	private final InvoiceDetailService invoiceDetailService = new InvoiceDetailService();

	private JButton reloadButton;
	private JButton payButton;
	private JButton deleteButton;
	private JButton selectButton;
	private JPanel buttonPanel;
	private Employee employee;
	private final InvoiceService invoiceService = new InvoiceService();
	private JTextField customerNameField;
	private JButton increaseButton;
	private JButton decreaseButton;
	private JTextField totalField;
	private JButton cancelInvoiceButton;
	private JTextField searchField;
	private JButton searchButton;

	public InvoicePaymentTab(Employee employee) {
		setLayout(new GridBagLayout());
		this.employee = employee;

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
		loadInventoryData(mainTableModel);

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

		selectButton.addActionListener(this);
		deleteButton.addActionListener(this);
		payButton.addActionListener(this);
		reloadButton.addActionListener(this);
		increaseButton.addActionListener(this);
		decreaseButton.addActionListener(this);
		cancelInvoiceButton.addActionListener(this);

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

	private void loadInventoryData(DefaultTableModel model) {
		List<Inventory> inventoryList = inventoryService.getAllInventory();
		if (inventoryList != null) {
			for (Inventory inventory : inventoryList) {
				String imageName = inventory.getImage();
				String imagePath = "/data/" + imageName;

				InputStream inputStream = getClass().getResourceAsStream(imagePath);
				if (inputStream != null) {
					try {
						BufferedImage bufferedImage = ImageIO.read(inputStream);
						if (bufferedImage != null) {
							Image scaledImage = bufferedImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
							ImageIcon scaledIcon = new ImageIcon(scaledImage);
							model.addRow(new Object[] { inventory.generateInventoryId(inventory.getId()), scaledIcon,
									inventory.getName(), inventory.getPrice() + " VNĐ", inventory.getQuantity(),
									imageName });
						} else {
							System.out.println("Không tìm thấy hình ảnh: " + imageName);
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							inputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else {
					model.addRow(new Object[] { inventory.generateInventoryId(inventory.getId()), null,
							inventory.getName(), inventory.getPrice() + " VNĐ", inventory.getQuantity(), imageName });
				}
			}
		}
	}

	// hàm tính toán tổng tiền từng sản phẩm
	private void updateRowTotal(DefaultTableModel model, int row) {
		double price = Double.parseDouble(model.getValueAt(row, 3).toString().split(" ")[0]); // Lấy giá và loại bỏ ký
																								// tự VNĐ
		int quantity = Integer.parseInt(model.getValueAt(row, 4).toString());
		int total = (int) (price * quantity); // Ép kiểu từ double thành int
		model.setValueAt(total + " VNĐ", row, 5);
	}

	// Hàm tính toán tổng số tiền từ bảng phụ
	private int calculateTotal(DefaultTableModel model) {
		int total = 0;
		for (int i = 0; i < model.getRowCount(); i++) {
			String totalString = model.getValueAt(i, 5).toString(); // Lấy giá trị tổng từ cột 5 (đã tính ở hàm
																	// updateRowTotal)
			total += Integer.parseInt(totalString.split(" ")[0]); // Loại bỏ ký tự VNĐ và tính tổng
		}
		return total;
	}

	// làm mới dữ liệu bảng chính
	private void reloadMainTable(DefaultTableModel model) {
		model.setRowCount(0); // Xóa dữ liệu của bảng chính
		loadInventoryData(model); // Tải lại dữ liệu
	}

	// hàm thêm chi tiết hóa đơn
	private List<InvoiceDetail> getInvoiceDetailsFromTable(DefaultTableModel model) {
		List<InvoiceDetail> invoiceDetails = new ArrayList<>();
		for (int i = 0; i < model.getRowCount(); i++) {
			int inventoryId = Integer.parseInt(model.getValueAt(i, 0).toString().substring(2));
			int quantity = Integer.parseInt(model.getValueAt(i, 4).toString());
			double total = Double.parseDouble(model.getValueAt(i, 5).toString().split(" ")[0]); // Loại bỏ ký tự VNĐ
			InvoiceDetail invoiceDetail = new InvoiceDetail();
			invoiceDetail.setInventory(new Inventory(inventoryId));
			invoiceDetail.setQuantity(quantity);
			invoiceDetail.setTotal(total);
			invoiceDetails.add(invoiceDetail);
		}
		return invoiceDetails;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// hàm thêm hàng vào hóa đơn
		if (e.getSource() == selectButton) {
			// Lấy thông tin sản phẩm từ bảng chính
			int selectedRow = table.getSelectedRow();
			if (selectedRow != -1) {
				String productId = table.getValueAt(selectedRow, 0).toString();
				String productName = table.getValueAt(selectedRow, 2).toString();
				String productPrice = table.getValueAt(selectedRow, 3).toString();
				String productQuantity = table.getValueAt(selectedRow, 4).toString();
				String imageSource = table.getValueAt(selectedRow, 5).toString();

				// Kiểm tra xem sản phẩm đã tồn tại trong bảng phụ chưa
				boolean productExists = false;
				DefaultTableModel secondaryTableModel = (DefaultTableModel) secondaryTable.getModel();
				for (int i = 0; i < secondaryTableModel.getRowCount(); i++) {
					String id = secondaryTableModel.getValueAt(i, 0).toString();
					if (id.equals(productId)) {
						int quantity = Integer.parseInt(secondaryTableModel.getValueAt(i, 4).toString());
						secondaryTableModel.setValueAt(quantity + 1, i, 4);
						updateRowTotal(secondaryTableModel, i);
						productExists = true;
						break;
					}
				}
				if (!productExists) {
					InputStream inputStream = getClass().getResourceAsStream("/data/" + imageSource);
					try {
						if (inputStream != null) {
							BufferedImage bufferedImage = ImageIO.read(inputStream);
							if (bufferedImage != null) {
								Image scaledImage = bufferedImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
								ImageIcon scaledIcon = new ImageIcon(scaledImage);
								secondaryTableModel
										.addRow(new Object[] { productId, scaledIcon, productName, productPrice, 1 });
							} else {
								System.out.println("Không thể đọc hình ảnh");
								secondaryTableModel
										.addRow(new Object[] { productId, null, productName, productPrice, 1 });
							}
							updateRowTotal(secondaryTableModel, secondaryTableModel.getRowCount() - 1); // Cập nhật tổng
																										// cho hàng mới
																										// thêm
						} else {
							secondaryTableModel.addRow(new Object[] { productId, null, productName, productPrice, 1 });
							updateRowTotal(secondaryTableModel, secondaryTableModel.getRowCount() - 1); // Cập nhật tổng
																										// cho hàng mới
																										// thêm

						}
					} catch (IOException ex) {
						System.out.println("Lỗi khi tải hình ảnh");
					} finally {
						try {
							if (inputStream != null) {
								inputStream.close();
							}
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				}

			} else {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm từ bảng chính", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
			}
		}

		// hàng xóa hàng hóa
		if (e.getSource() == deleteButton) {
			int selectedRow = secondaryTable.getSelectedRow();
			if (selectedRow != -1) {
				DefaultTableModel secondaryTableModel = (DefaultTableModel) secondaryTable.getModel();
				String id = secondaryTableModel.getValueAt(selectedRow, 0).toString();
				if (!id.equals("Tổng cộng")) { // Kiểm tra xem hàng có phải là "Tổng cộng" không
					secondaryTableModel.removeRow(selectedRow);
				} else {
					JOptionPane.showMessageDialog(this, "Bạn không thể xóa hàng 'Tổng cộng'", "Thông báo",
							JOptionPane.WARNING_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm từ hóa đơn để xóa", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
			}
		}

		// Tăng số lượng sản phẩm
		if (e.getSource() == increaseButton) {
			int selectedRow = secondaryTable.getSelectedRow();

			if (selectedRow != -1) {
				DefaultTableModel secondaryTableModel = (DefaultTableModel) secondaryTable.getModel();
				int quantity = Integer.parseInt(secondaryTableModel.getValueAt(selectedRow, 4).toString());
				secondaryTableModel.setValueAt(quantity + 1, selectedRow, 4);
				updateRowTotal(secondaryTableModel, selectedRow); // Cập nhật tổng cho hàng vừa thay đổi
			} else {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm từ hóa đơn để tăng số lượng", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
			}
		}

		// Giảm số lượng sản phẩm
		if (e.getSource() == decreaseButton) {
			int selectedRow = secondaryTable.getSelectedRow();

			if (selectedRow != -1) {
				DefaultTableModel secondaryTableModel = (DefaultTableModel) secondaryTable.getModel();
				int quantity = Integer.parseInt(secondaryTableModel.getValueAt(selectedRow, 4).toString());
				if (quantity > 0) {
					secondaryTableModel.setValueAt(quantity - 1, selectedRow, 4);
					if (quantity - 1 == 0) {
						secondaryTableModel.removeRow(selectedRow);
					}
					updateRowTotal(secondaryTableModel, selectedRow); // Cập nhật tổng cho hàng vừa thay đổi
				}
			} else {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm từ hóa đơn để giảm số lượng", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
			}
		}

		if (e.getSource() == selectButton || e.getSource() == deleteButton || e.getSource() == increaseButton
				|| e.getSource() == decreaseButton) {
			// Tính toán tổng số tiền từ bảng phụ
			int totalAmount = calculateTotal((DefaultTableModel) secondaryTable.getModel());

			// Cập nhật ô tổng cộng với tổng số tiền mới
			totalField.setText(totalAmount + " VNĐ");
		}

		if (e.getSource() == reloadButton) {
			DefaultTableModel mainTableModel = (DefaultTableModel) table.getModel();
			reloadMainTable(mainTableModel); // Gọi hàm để làm mới bảng chính
			totalField.setText("0 VNĐ"); // Reset trường tổng cộng

		}

		if (e.getSource() == cancelInvoiceButton) {
			int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn hủy hóa đơn?", "Xác nhận hủy",
					JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				DefaultTableModel secondaryTableModel = (DefaultTableModel) secondaryTable.getModel();
				secondaryTableModel.setRowCount(0); // Xóa tất cả các hàng trong bảng phụ
				totalField.setText("0 VNĐ"); // Đặt lại trường tổng cộng về 0
				customerNameField.setText("");
			}
		}

		
		//hàm thanh toán
		if (e.getSource() == payButton) {
			// Kiểm tra nếu bảng phụ rỗng hoặc tên khách hàng chưa nhập
			DefaultTableModel secondaryTableModel = (DefaultTableModel) secondaryTable.getModel();
			String customerName = customerNameField.getText().trim(); // Lấy tên khách hàng và loại bỏ khoảng trắng đầu
																		// cuối
			if (secondaryTableModel.getRowCount() == 0) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm trước khi thanh toán!", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
				return; // Kết thúc phương thức và không tiếp tục thực hiện các hành động sau
			}

			if (customerName.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khách hàng!", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
				return; // Kết thúc phương thức và không tiếp tục thực hiện các hành động sau
			}

			// Hiển thị hộp thoại xác nhận cho khách hàng
			double total = Double.parseDouble(totalField.getText().split(" ")[0]);
			int confirm = JOptionPane.showConfirmDialog(this,
					"Xác nhận thanh toán cho " + customerName + " với tổng tiền " + total + " VNĐ?",
					"Xác nhận thanh toán", JOptionPane.YES_NO_OPTION);

			// Nếu khách hàng chấp nhận thanh toán
			if (confirm == JOptionPane.YES_OPTION) {
				// Lấy thông tin nhân viên và ngày hiện tại
				int employeeId = employee.getId();
				Date date = new Date();

				// Gọi service để thêm hóa đơn
				long invoiceId = invoiceService.addInvoice(employeeId, customerName, date, total);

				// Lấy danh sách chi tiết hóa đơn từ bảng phụ
				List<InvoiceDetail> invoiceDetails = getInvoiceDetailsFromTable(
						(DefaultTableModel) secondaryTable.getModel());

				// Gửi danh sách chi tiết hóa đơn đến server
				boolean isSuccess = invoiceDetailService.addInvoiceDetails(invoiceId, invoiceDetails);

				// Xử lý kết quả
				if (isSuccess) {
					// Thanh toán và thêm chi tiết hóa đơn thành công
					JOptionPane.showMessageDialog(this, "Thanh toán và thêm chi tiết hóa đơn thành công!", "Thông báo",
							JOptionPane.INFORMATION_MESSAGE);
					// Xóa dữ liệu khỏi bảng phụ và cập nhật tổng cộng
					((DefaultTableModel) secondaryTable.getModel()).setRowCount(0);
					totalField.setText("0 VNĐ");
					customerNameField.setText("");
				} else {
					// Nếu có lỗi xảy ra khi thêm chi tiết hóa đơn
					JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi thêm chi tiết hóa đơn. Vui lòng thử lại!",
							"Lỗi", JOptionPane.ERROR_MESSAGE);
				}

			} else {
				// Nếu thanh toán không thành công, xử lý theo ý của bạn (ví dụ: thông báo lỗi)
				JOptionPane.showMessageDialog(this, "Thanh toán không thành công. Vui lòng thử lại!", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
			}
			
		}
		
		else if (e.getSource() == searchButton) {
		    String keyword = searchField.getText();
		    System.out.println("keyword: "+ keyword);
		    if (keyword.isEmpty()) {
		    	DefaultTableModel mainTableModel = (DefaultTableModel) table.getModel();
				reloadMainTable(mainTableModel); 
		        return; 
		    }
		    List<Inventory> searchResult = inventoryService.searchInventory(keyword);
		    System.out.println("kết quả:" +searchResult);
		    DefaultTableModel model = (DefaultTableModel) table.getModel();
		    model.setRowCount(0); 
		    for (Inventory inventory : searchResult) {
		        String imagePath = "/data/" + inventory.getImage();
		        InputStream inputStream = getClass().getResourceAsStream(imagePath);
		        if (inputStream != null) {
		            try {
		                BufferedImage bufferedImage = ImageIO.read(inputStream);
		                if (bufferedImage != null) {
		                    Image scaledImage = bufferedImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		                    ImageIcon scaledIcon = new ImageIcon(scaledImage);
		                    model.addRow(new Object[]{inventory.generateInventoryId(inventory.getId()), scaledIcon, inventory.getName(), inventory.getPrice() + " VNĐ", inventory.getQuantity(), inventory.getImage()});
		                } else {
		                    System.out.println("Không tìm thấy hình ảnh: " + inventory.getImage());
		                }
		            } catch (IOException ex) {
		                ex.printStackTrace();
		            } finally {
		                try {
		                    inputStream.close();
		                } catch (IOException ex) {
		                    ex.printStackTrace();
		                }
		            }
		        } else {
		            model.addRow(new Object[]{inventory.generateInventoryId(inventory.getId()), null, inventory.getName(), inventory.getPrice() + " VNĐ", inventory.getQuantity(), inventory.getImage()});
		        }
		    }
		}
		
	}

}
