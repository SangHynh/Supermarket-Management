-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               11.3.2-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping structure for table supermarketdb.employee
CREATE TABLE IF NOT EXISTS `employee` (
  `gender` bit(1) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role` bit(1) NOT NULL,
  `status` bit(1) NOT NULL,
  `email` varchar(255) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table supermarketdb.employee: ~3 rows (approximately)
INSERT INTO `employee` (`gender`, `id`, `role`, `status`, `email`, `image`, `name`, `password`, `phone`) VALUES
	(b'1', 1, b'1', b'1', 'sang@gmail.com', 'null', 'Huynh Van Sang', '123456', '0343564321'),
	(b'1', 2, b'1', b'1', 'giau@gmail.com', 'null', 'Nguyen Van Giau', '123', '0123456789'),
	(b'1', 3, b'0', b'1', 'hiep@gmail.com', 'null', 'Thanh Hiep', '123', '0333333333');

-- Dumping structure for table supermarketdb.inventory
CREATE TABLE IF NOT EXISTS `inventory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `price` double NOT NULL,
  `quantity` int(11) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table supermarketdb.inventory: ~20 rows (approximately)
INSERT INTO `inventory` (`id`, `price`, `quantity`, `image`, `name`) VALUES
	(1, 15000, 5000, 'botgiataba.jpg', 'Bột giặt Aba'),
	(2, 20000, 5000, 'nuocruachen.jpg', 'Nước rửa chén sunlight'),
	(3, 10000, 5000, 'cocacola.jpg', 'Coca cola'),
	(4, 10000, 5000, 'suatuoikhongduong.jpg', 'Sữa tươi không đường'),
	(5, 250000, 2000, 'dauantruongan.jpg', 'Dầu ăn Trường An'),
	(6, 3000, 5000, 'mihaohao.png', 'Mì hảo hảo tôm chua cay'),
	(7, 20000, 1000, 'suaongtho.jpg', 'Sữa đặc Ông Thọ'),
	(8, 21000, 1000, 'tuongotchinsu.jpg', 'Tương ớt Chinsu'),
	(9, 18000, 3000, 'nuoctuongtamthaitu.jpg', 'Nước tương Tam Thái Tử'),
	(10, 26000, 4000, 'nuocmamnamngu.jpg', 'Nước mắm Nam Ngư'),
	(11, 15000, 1500, 'banhgaooneone.jpg', 'Bánh gạo One One'),
	(12, 10000, 3000, 'nuockhoanglavie.jpg', 'Nước khoáng Lavie'),
	(13, 10000, 5000, 'biasaigon.jpg', 'Bia Sài Gòn'),
	(14, 39000, 3000, 'daugoirejoice.jpg', 'Dầu gội Rẹoice'),
	(15, 80000, 3000, 'banhdanisa.jpg', 'Bánh Danisa'),
	(16, 15000, 3000, 'kemdanhrangps.jpg', 'Kem đánh răng PS'),
	(17, 30000, 500, 'nuocxavaidowny.jpg', 'Nước xả vải Downy'),
	(18, 16000, 600, 'banhquycosy.jpg', 'Bánh quy sữa Cosy'),
	(19, 6000, 5000, 'caphenestle.jpg', 'Cà phê Nestle'),
	(20, 12000, 1000, 'suahopmilo.jpg', 'Sữa hộp Milo');

-- Dumping structure for table supermarketdb.invoice
CREATE TABLE IF NOT EXISTS `invoice` (
  `date` date DEFAULT NULL,
  `employee_id` int(11) DEFAULT NULL,
  `total` double NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKau92vqwrrlsflir3v65262ucw` (`employee_id`),
  CONSTRAINT `FKau92vqwrrlsflir3v65262ucw` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table supermarketdb.invoice: ~25 rows (approximately)
INSERT INTO `invoice` (`date`, `employee_id`, `total`, `id`, `customer_name`) VALUES
	('2024-05-07', 1, 4, 1, 'sang'),
	('2024-05-07', 1, 6, 2, 's'),
	('2024-05-07', 1, 5, 3, '5'),
	('2024-05-07', 1, 8, 4, 'sang huỳnh văn'),
	('2024-05-07', 1, 2, 5, 'moi'),
	('2024-05-07', 1, 2, 6, '555'),
	('2024-05-07', 1, 0, 7, 's'),
	('2024-05-07', 1, 1411000, 8, 'đoán xem'),
	('2024-05-07', 1, 398000, 9, 'hahaha'),
	('2024-05-07', 1, 210000, 10, 'khashc đẹp trai'),
	('2024-05-07', 1, 6000, 11, 'mua cà phe'),
	('2024-05-07', 1, 500000, 12, 'bán dầu'),
	('2024-05-07', 1, 330000, 13, 'Thị Hương'),
	('2024-05-07', 1, 274000, 14, 'Ngọc Anh'),
	('2024-05-07', 1, 168000, 15, 'Hiệp gà'),
	('2024-05-07', 1, 235000, 16, 'Lộc lung linh'),
	('2024-05-07', 1, 1280000, 17, 'Sung sướng'),
	('2024-05-07', 1, 1250000, 18, 'Dong'),
	('2024-05-07', 1, 263000, 19, 'Dung'),
	('2024-05-07', 1, 52000, 20, 'John'),
	('2024-05-07', 1, 57000, 21, 'Quang'),
	('2024-05-07', 1, 42000, 22, 'Hoang Anh'),
	('2024-05-07', 1, 22000, 23, 'Kang'),
	('2024-05-07', 1, 270000, 24, 'Tu'),
	('2024-05-07', 1, 3000, 25, 'Quynh');

-- Dumping structure for table supermarketdb.invoice_detail
CREATE TABLE IF NOT EXISTS `invoice_detail` (
  `inventory_id` int(11) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `total` double DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `invoice_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfrl89yikwhmjeog1re93govni` (`inventory_id`),
  KEY `FKit1rbx4thcr6gx6bm3gxub3y4` (`invoice_id`),
  CONSTRAINT `FKfrl89yikwhmjeog1re93govni` FOREIGN KEY (`inventory_id`) REFERENCES `inventory` (`id`),
  CONSTRAINT `FKit1rbx4thcr6gx6bm3gxub3y4` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table supermarketdb.invoice_detail: ~55 rows (approximately)
INSERT INTO `invoice_detail` (`inventory_id`, `quantity`, `total`, `id`, `invoice_id`) VALUES
	(1, 6, 6, 2, 2),
	(1, 5, 5, 3, 3),
	(2, 3, 5, 4, 4),
	(2, 1, 1, 6, 5),
	(2, 1, 1, 8, 6),
	(1, 3, 45000, 12, 8),
	(5, 4, 1000000, 13, 8),
	(6, 3, 9000, 14, 8),
	(14, 3, 117000, 15, 8),
	(15, 3, 240000, 16, 8),
	(15, 3, 240000, 17, 9),
	(19, 2, 12000, 18, 9),
	(20, 2, 24000, 19, 9),
	(18, 2, 32000, 20, 9),
	(17, 3, 90000, 21, 9),
	(16, 3, 45000, 22, 10),
	(17, 2, 60000, 23, 10),
	(3, 3, 30000, 24, 10),
	(4, 3, 30000, 25, 10),
	(1, 3, 45000, 26, 10),
	(19, 1, 6000, 27, 11),
	(5, 2, 500000, 28, 12),
	(5, 1, 250000, 29, 13),
	(4, 1, 10000, 30, 13),
	(3, 1, 10000, 31, 13),
	(2, 3, 60000, 32, 13),
	(1, 1, 15000, 33, 14),
	(6, 3, 9000, 34, 14),
	(5, 1, 250000, 35, 14),
	(3, 3, 30000, 36, 15),
	(18, 3, 48000, 37, 15),
	(17, 3, 90000, 38, 15),
	(16, 6, 90000, 39, 16),
	(2, 2, 40000, 40, 16),
	(1, 3, 45000, 41, 16),
	(3, 6, 60000, 42, 16),
	(4, 3, 30000, 43, 17),
	(5, 5, 1250000, 44, 17),
	(5, 5, 1250000, 45, 18),
	(5, 1, 250000, 46, 19),
	(6, 1, 3000, 47, 19),
	(4, 1, 10000, 48, 19),
	(17, 1, 30000, 49, 20),
	(18, 1, 16000, 50, 20),
	(19, 1, 6000, 51, 20),
	(20, 1, 12000, 52, 21),
	(17, 1, 30000, 53, 21),
	(16, 1, 15000, 54, 21),
	(17, 1, 30000, 55, 22),
	(20, 1, 12000, 56, 22),
	(19, 1, 6000, 57, 23),
	(18, 1, 16000, 58, 23),
	(3, 2, 20000, 59, 24),
	(5, 1, 250000, 60, 24),
	(6, 1, 3000, 61, 25);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
