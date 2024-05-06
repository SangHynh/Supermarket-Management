package gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.TitledBorder;

import model.Employee;

import javax.swing.border.EtchedBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AccountInfoFrame extends JFrame {
    public AccountInfoFrame(Employee employee) {
    	
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Đóng frame này mà không ảnh hưởng đến ứng dụng chính
        setSize(788, 443); // Đặt kích thước cho frame
        setLocationRelativeTo(null); // Hiển thị frame ở giữa màn hình
        setResizable(false);
        getContentPane().setLayout(null);
        
        // Tạo một JLabel để chứa hình nền
        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setIcon(new ImageIcon(getClass().getResource("/images/backgroundapp.jpg")));
        backgroundLabel.setBounds(0, 0, 764, 401); // Đặt kích thước của label bằng kích thước của frame
        getContentPane().add(backgroundLabel);

        // Tạo panel chứa thông tin nhân viên
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null);
        infoPanel.setBounds(29, 28, 714, 350);
        infoPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Thông tin nhân viên", TitledBorder.LEFT, TitledBorder.TOP, null, Color.GRAY));
        infoPanel.setBackground(new Color(0, 0, 0, 0)); // Đặt màu nền của panel là trong suốt
        backgroundLabel.add(infoPanel); // Thêm panel làm con của backgroundLabel

        // Tạo label chứa avatar
        JLabel avatarLabel = new JLabel();
        avatarLabel.setBounds(20, 28, 206, 263);
        avatarLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        infoPanel.add(avatarLabel);

        // Tạo các label cho thông tin nhân viên
        JLabel maNVLabel = new JLabel("Mã nhân viên:");
        maNVLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        maNVLabel.setBounds(250, 40, 100, 30);
        infoPanel.add(maNVLabel);

        JLabel maNVData = new JLabel();
        maNVData.setFont(new Font("Tahoma", Font.PLAIN, 15));
        maNVData.setBounds(370, 40, 300, 30);
        maNVData.setText(String.valueOf(employee.generateEmployeeId(employee.getId())));
        infoPanel.add(maNVData);

        JLabel hoTenLabel = new JLabel("Họ và tên:");
        hoTenLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        hoTenLabel.setBounds(250, 80, 100, 30);
        infoPanel.add(hoTenLabel);

        JLabel hoTenData = new JLabel();
        hoTenData.setFont(new Font("Tahoma", Font.PLAIN, 15));
        hoTenData.setBounds(370, 80, 300, 30);
        hoTenData.setText(employee.getName());
        infoPanel.add(hoTenData);

        JLabel chucVuLabel = new JLabel("Chức vụ:");
        chucVuLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        chucVuLabel.setBounds(250, 120, 100, 30);
        infoPanel.add(chucVuLabel);

        JLabel chucVuData = new JLabel();
        chucVuData.setFont(new Font("Tahoma", Font.PLAIN, 15));
        chucVuData.setBounds(370, 120, 300, 30);
        String role = employee.isRole()==false ? "thu ngân" : "quản lý";
        chucVuData.setText(role);
        infoPanel.add(chucVuData);

        JLabel soDienThoaiLabel = new JLabel("Số điện thoại:");
        soDienThoaiLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        soDienThoaiLabel.setBounds(250, 160, 100, 30);
        infoPanel.add(soDienThoaiLabel);

        JLabel soDienThoaiData = new JLabel();
        soDienThoaiData.setFont(new Font("Tahoma", Font.PLAIN, 15));
        soDienThoaiData.setBounds(370, 160, 300, 30);
        soDienThoaiData.setText(employee.getPhone());
        infoPanel.add(soDienThoaiData);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        emailLabel.setBounds(250, 200, 100, 30);
        infoPanel.add(emailLabel);

        JLabel emailData = new JLabel();
        emailData.setFont(new Font("Tahoma", Font.PLAIN, 15));
        emailData.setBounds(370, 200, 300, 30);
        emailData.setText(employee.getEmail());
        infoPanel.add(emailData);

        JLabel gioiTinhLabel = new JLabel("Giới tính:");
        gioiTinhLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        gioiTinhLabel.setBounds(250, 240, 100, 30);
        infoPanel.add(gioiTinhLabel);

        JLabel gioiTinhData = new JLabel();
        gioiTinhData.setFont(new Font("Tahoma", Font.PLAIN, 15));
        gioiTinhData.setBounds(370, 240, 300, 30);
        String gender = employee.isGender()==false ? "nữ" : "nam";
        gioiTinhData.setText(gender);
        infoPanel.add(gioiTinhData);
        
        JButton btnDoiMatKhau = new JButton("Đổi mật khẩu");
        btnDoiMatKhau.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnDoiMatKhau.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnDoiMatKhau.setBounds(309, 303, 161, 37);
        infoPanel.add(btnDoiMatKhau);
    }
}
