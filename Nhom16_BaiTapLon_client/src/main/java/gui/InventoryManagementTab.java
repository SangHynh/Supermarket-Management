package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import model.Inventory;
import services.InventoryService;

public class InventoryManagementTab extends JPanel implements ActionListener {
	private final InventoryService inventoryService = new InventoryService();
	private final JTextField productIdField;
	private final JTextField productNameField;
	private final JTextField priceField;
	private final JTextField quantityField;
	private JButton addButton;
	private JButton deleteButton;
	private JButton updateButton;
	private JButton reloadButton;
	private JTable table;
	private JPanel imagePanel;
	private JLabel lblImage;
	private JButton selectImageButton;
	private JTextField imageField;
	private JTextField searchField;
	private JButton searchButton;

	public InventoryManagementTab() {
		setLayout(new GridBagLayout());

		// Left panel (Table)
		JPanel leftPanel = new JPanel(new BorderLayout());
		table = new JTable();
		DefaultTableModel model = new DefaultTableModel(
				new String[] { "Mã SP", "Hình ảnh", "Tên SP", "Đơn giá", "Số lượng", "Nguồn" }, 0) {
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				if (columnIndex == 1) {
					return ImageIcon.class; // Sử dụng ImageIcon cho cột Hình ảnh
				} else {
					return Object.class; // Sử dụng kiểu dữ liệu mặc định cho các cột khác
				}
			}
			@Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		};
		table.setModel(model);
		table.setRowHeight(100);
		table.setFont(new Font("Arial", Font.PLAIN, 15));

		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = table.rowAtPoint(evt.getPoint());
				int col = table.columnAtPoint(evt.getPoint());
				if (row >= 0 && col == 1) { // Kiểm tra nếu click vào cột chứa hình ảnh
					// Lấy đường dẫn của hình ảnh từ cột 1 (cột hình ảnh)
					String imagePath = (String) model.getValueAt(row, col);
					System.out.println("image path: " + imagePath);
					// Hiển thị đường dẫn của hình ảnh lên JTextField của hình ảnh
					imageField.setText(imagePath);
				} else if (row >= 0) {
					// Nếu không phải click vào cột hình ảnh, thực hiện các thao tác như hiển thị dữ
					// liệu của hàng tương ứng lên các JTextField khác
					productIdField.setText((String) model.getValueAt(row, 0));
					productNameField.setText((String) model.getValueAt(row, 2));
					// Hiển thị giá trị số thực trong trường priceField
					String priceValueAsString = (String) model.getValueAt(row, 3);
					// Loại bỏ chữ VNĐ nếu có
					priceValueAsString = priceValueAsString.replaceAll("[^\\d.]", "");
					double priceValue = Double.parseDouble(priceValueAsString);
					priceField.setText(String.format("%.2f", priceValue));
					quantityField.setText(String.valueOf(model.getValueAt(row, 4)));
					imageField.setText(String.valueOf(model.getValueAt(row, 5)));
				}
			}
		});

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

		// load data from table
		loadInventoryData(model);

		// Search panel
		JPanel searchPanel = new JPanel(new BorderLayout());
		searchField = new JTextField();
		searchButton = new JButton("Tìm kiếm");
		searchButton.addActionListener(this);
		searchPanel.add(searchField, BorderLayout.CENTER);
		searchPanel.add(searchButton, BorderLayout.EAST);

		GridBagConstraints gbcSearchPanel = new GridBagConstraints();
		gbcSearchPanel.gridx = 0;
		gbcSearchPanel.gridy = 1;
		gbcSearchPanel.weightx = 1.0;
		gbcSearchPanel.weighty = 0.05; // Giảm chiều cao của ô tìm kiếm
		gbcSearchPanel.fill = GridBagConstraints.BOTH;
		gbcSearchPanel.anchor = GridBagConstraints.NORTH;
		gbcSearchPanel.insets = new Insets(0, 5, 5, 5);
		add(searchPanel, gbcSearchPanel);

		// Right panel
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

//		// Image panel
//		imagePanel = new JPanel();
//		imagePanel.setBackground(Color.LIGHT_GRAY);
//		imagePanel.setPreferredSize(new Dimension(150, 150));
//		rightPanel.add(imagePanel);
//		imagePanel.setLayout(null);
//		
//		lblImage = new JLabel();
//		lblImage.setBounds(76, 10, 194, 154);
//		imagePanel.add(lblImage);

		// Select image button

		// Input fields panel
		JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 5));
		productIdField = new JTextField();
		productIdField.setEnabled(false);
		inputPanel.add(new JLabel("Mã sản phẩm:"));
		inputPanel.add(productIdField);

		productNameField = new JTextField();
		inputPanel.add(new JLabel("Tên sản phẩm:"));
		inputPanel.add(productNameField);

		priceField = new JTextField();
		inputPanel.add(new JLabel("Đơn giá:"));
		inputPanel.add(priceField);

		quantityField = new JTextField(); // Số lượng
		inputPanel.add(new JLabel("Số lượng:"));
		inputPanel.add(quantityField);

		inputPanel.add(new JLabel("Hình ảnh:"));
		imageField = new JTextField();
		imageField.setEnabled(false);
		imageField.setText("null");
		inputPanel.add(imageField);

		rightPanel.add(inputPanel);

		// Buttons panel
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		addButton = new JButton("Thêm");
		deleteButton = new JButton("Xóa");
		updateButton = new JButton("Cập nhật");
		reloadButton = new JButton("Làm mới");
		selectImageButton = new JButton("Chọn hình ảnh");

		addButton.addActionListener(this);
		deleteButton.addActionListener(this);
		updateButton.addActionListener(this);
		reloadButton.addActionListener(this);
		selectImageButton.addActionListener(this);

		buttonPanel.add(addButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(updateButton);
		buttonPanel.add(reloadButton);
		buttonPanel.add(selectImageButton);
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
	}

	private void loadInventoryData(DefaultTableModel model) {
		List<Inventory> inventoryList = inventoryService.getAllInventory();
		if (inventoryList != null) {
			for (Inventory inventory : inventoryList) {
				String imageName = inventory.getImage();
				String imagePath = "/data/" + imageName;
				System.out.println(imagePath);

				// Đọc hình ảnh từ resources bằng InputStream
				InputStream inputStream = getClass().getResourceAsStream(imagePath);
				if (inputStream != null) {
					try {
						// Tạo một BufferedImage từ InputStream của hình ảnh
						BufferedImage bufferedImage = ImageIO.read(inputStream);
						if (bufferedImage != null) {
							// Scale hình ảnh để phù hợp với kích thước của JLabel
							Image scaledImage = bufferedImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
							// Tạo một ImageIcon từ hình ảnh đã scale
							ImageIcon scaledIcon = new ImageIcon(scaledImage);
							// Thêm các dữ liệu vào hàng mới của bảng
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
	
	
	private boolean validateInput(String productName, String priceText, String quantityText) {
		// Kiểm tra xem các trường có trống không
		if (productName.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin sản phẩm.");
			return false; // Dừng việc thực hiện tiếp theo nếu có trường nào trống
		}

		// Kiểm tra xem số lượng có phải là số nguyên không
		if (!quantityText.matches("\\d+")) {
			JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên dương.");
			return false; // Dừng việc thực hiện tiếp theo nếu số lượng không hợp lệ
		}

		// Kiểm tra xem đơn giá có phải là số thực không
		if (!priceText.matches("\\d+(\\.\\d+)?")) {
			JOptionPane.showMessageDialog(this, "Đơn giá phải là số thực.");
			return false; // Dừng việc thực hiện tiếp theo nếu đơn giá không hợp lệ
		}

		// Nếu các điều kiện đều đúng, trả về true
		return true;
	}

	private void reloadTable() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0); // Xóa bảng hiện tại

		// Cập nhật bảng mới từ dữ liệu
		loadInventoryData(model);

		// Xóa rỗng các trường nhập liệu
		productIdField.setText("");
		productNameField.setText("");
		priceField.setText("");
		quantityField.setText("");
		imageField.setText("null");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addButton) {
			// Xử lý sự kiện khi nút "Thêm" được nhấn
			String productName = productNameField.getText();
			String priceText = priceField.getText();
			String quantityText = quantityField.getText();

			String imageText = imageField.getText();

			boolean isValidated = validateInput(productNameField.getText(), priceField.getText(),
					quantityField.getText());
			System.out.println("isValidated: " + isValidated);

			if (validateInput(productName, priceText, quantityText)) {
				double price = Double.parseDouble(priceText);
				int quantity = Integer.parseInt(quantityText);
				// Gọi phương thức addInventory từ InventoryService
				boolean isSuccess = inventoryService.addInventory(productName, price, quantity, imageText);

				// Xử lý phản hồi từ phương thức addInventory
				if (isSuccess) {
					// Thêm thành công, làm gì đó (ví dụ: thông báo, làm mới danh sách, ...)
					System.out.println("Thêm hàng thành công.");
					JOptionPane.showMessageDialog(this, "Thêm hàng thành công.");

					reloadTable();
				} else {
					// Thêm không thành công, làm gì đó (ví dụ: thông báo lỗi)
					System.out.println("Thêm hàng thất bại.");
					JOptionPane.showMessageDialog(this, "Thêm hàng thất bại!");
				}
			}
		} else if (e.getSource() == deleteButton) {
			String productId = productIdField.getText();
			System.out.println(productId);
			// Gọi phương thức để xóa hàng tồn kho
			boolean isDeleted = inventoryService.deleteInventoryById(productId);

			// Hiển thị thông báo cho người dùng
			if (isDeleted) {
				JOptionPane.showMessageDialog(this, "Đã xóa sản phẩm có ID: " + productId);
				reloadTable(); // Làm mới bảng sau khi xóa
			} else {
				JOptionPane.showMessageDialog(this, "Xóa sản phẩm thất bại!");
			}
		} else if (e.getSource() == updateButton) {
			// Xử lý sự kiện khi nút "Cập nhật" được nhấn
			String productId = productIdField.getText();
			String productName = productNameField.getText();
			String priceText = priceField.getText();
			String quantityText = quantityField.getText();
			String imageText = imageField.getText();

			// Kiểm tra tính hợp lệ của dữ liệu
			if (!validateInput(productName, priceText, quantityText)) {
				return; // Dừng việc thực hiện nếu dữ liệu không hợp lệ
			}

			// Chuyển đổi giá và số lượng thành dạng số
			double price = Double.parseDouble(priceText);
			int quantity = Integer.parseInt(quantityText);

			int id = Integer.parseInt(productId.substring(2));

			Inventory inv = new Inventory(id, productName, price, quantity, imageText);

			// Gọi phương thức cập nhật từ InventoryService
			boolean isUpdated = inventoryService.updateInventory(inv);
			System.out.println(isUpdated);
			// Xử lý phản hồi từ dịch vụ
			if (isUpdated) {
				// Cập nhật thành công, hiển thị thông báo và làm mới bảng
				JOptionPane.showMessageDialog(this, "Cập nhật thông tin sản phẩm thành công.");
				reloadTable();
			} else {
				// Cập nhật không thành công, hiển thị thông báo lỗi
				JOptionPane.showMessageDialog(this, "Cập nhật thông tin sản phẩm thất bại.");
			}
		} else if (e.getSource() == reloadButton) {
			// Xử lý sự kiện khi nút "Reload" được nhấn
			reloadTable(); // Gọi phương thức reloadTable để cập nhật bảng
		} else if (e.getSource() == selectImageButton) {
			// Tạo file chooser
			JFileChooser fileChooser = new JFileChooser("src/main/resources/data");

			// Tạo bộ lọc cho tệp tin chỉ cho phép jpg và png
			FileNameExtensionFilter filter = new FileNameExtensionFilter("JPEG & PNG Images", "jpg", "jpeg", "png");
			fileChooser.setFileFilter(filter);

			// Đặt thư mục hiện tại của file chooser là thư mục chỉ định
			fileChooser.setCurrentDirectory(new File("src/main/resources/data"));

			// Hiển thị file chooser
			int result = fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				// Lấy tên của tập tin được chọn
				String imageName = selectedFile.getName();
				System.out.println("đã chọn: " + imageName);
				imageField.setText(imageName);
			}
		}
		
		else if (e.getSource() == searchButton) {
		    String keyword = searchField.getText();
		    System.out.println("keyword: "+ keyword);
		    if (keyword.isEmpty()) {
		    	reloadTable();
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

	

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Quản lý hàng hóa");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().add(new InventoryManagementTab());
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		});
	}
}
