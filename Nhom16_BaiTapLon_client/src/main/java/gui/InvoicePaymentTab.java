package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.Inventory;
import services.InventoryService;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class InvoicePaymentTab extends JPanel {
    private JTable table;
    private JTable secondaryTable;
	private final InventoryService inventoryService = new InventoryService();

    public InvoicePaymentTab() {
        setLayout(new GridBagLayout());

        // Left panel (Main Table)
        JPanel mainTablePanel = new JPanel(new BorderLayout());
        table = new JTable();
        DefaultTableModel mainTableModel = new DefaultTableModel(
                new String[]{"Mã SP", "Hình ảnh", "Tên SP", "Đơn giá", "Số lượng", "Nguồn"}, 0) {
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
		       //all cells false
		       return false;
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
        gbcMainTablePanel.gridy = 0;
        gbcMainTablePanel.weightx = 1.0;
        gbcMainTablePanel.weighty = 0.9;
        gbcMainTablePanel.fill = GridBagConstraints.BOTH;
        gbcMainTablePanel.anchor = GridBagConstraints.NORTH;
        gbcMainTablePanel.insets = new Insets(5, 5, 5, 5);
        add(mainTablePanel, gbcMainTablePanel);

        // Search panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Tìm kiếm");

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

        // Right panel (Input fields and Secondary Table)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        // Input fields panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcInputPanel = new GridBagConstraints();
        gbcInputPanel.gridx = 0;
        gbcInputPanel.gridy = 0;
        gbcInputPanel.weightx = 1.0;
        gbcInputPanel.weighty = 1.0;
        gbcInputPanel.fill = GridBagConstraints.BOTH;
        gbcInputPanel.anchor = GridBagConstraints.NORTH;
        gbcInputPanel.insets = new Insets(5, 0, 5, 5);

        // Secondary Table
        secondaryTable = new JTable();
        DefaultTableModel secondaryTableModel = new DefaultTableModel(
                new String[]{"ID", "Tên sản phẩm", "Đơn giá", "Số lượng"}, 0);
        secondaryTable.setModel(secondaryTableModel);
        secondaryTable.setFont(new Font("Arial", Font.PLAIN, 15));

        JScrollPane secondaryScrollPane = new JScrollPane(secondaryTable);
        GridBagConstraints gbcSecondaryScrollPane = new GridBagConstraints();
        gbcSecondaryScrollPane.gridx = 0;
        gbcSecondaryScrollPane.gridy = 0;
        gbcSecondaryScrollPane.weightx = 1.0;
        gbcSecondaryScrollPane.weighty = 1.0;
        gbcSecondaryScrollPane.fill = GridBagConstraints.BOTH;
        gbcSecondaryScrollPane.anchor = GridBagConstraints.NORTH;
        gbcSecondaryScrollPane.insets = new Insets(5, 5, 5, 5);
        inputPanel.add(secondaryScrollPane, gbcSecondaryScrollPane);

        rightPanel.add(inputPanel);

        GridBagConstraints gbcRightPanel = new GridBagConstraints();
        gbcRightPanel.gridx = 1;
        gbcRightPanel.gridy = 0;
        gbcRightPanel.weightx = 0.3;
        gbcRightPanel.weighty = 1.0;
        gbcRightPanel.fill = GridBagConstraints.BOTH;
        gbcRightPanel.anchor = GridBagConstraints.NORTH;
        gbcRightPanel.insets = new Insets(5, 0, 5, 5);
        add(rightPanel, gbcRightPanel);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton selectButton = new JButton("Chọn hàng");
        JButton deleteButton = new JButton("Xóa hàng");
        JButton payButton = new JButton("Thanh toán");
        JButton reloadButton = new JButton("Làm mới");

        buttonPanel.add(selectButton);
        buttonPanel.add(payButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(reloadButton);

        GridBagConstraints gbcButtonPanel = new GridBagConstraints();
        gbcButtonPanel.gridx = 0;
        gbcButtonPanel.gridy = 1;
        gbcButtonPanel.weightx = 1.0;
        gbcButtonPanel.weighty = 0.1;
        gbcButtonPanel.fill = GridBagConstraints.HORIZONTAL;
        gbcButtonPanel.anchor = GridBagConstraints.SOUTH;
        gbcButtonPanel.insets = new Insets(0, 5, 5, 5);
        inputPanel.add(buttonPanel, gbcButtonPanel);
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
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Invoice Payment");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new InvoicePaymentTab());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
