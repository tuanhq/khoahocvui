CREATE DATABASE  IF NOT EXISTS `vuadaubep` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci */;
USE `vuadaubep`;
-- MySQL dump 10.13  Distrib 5.5.41, for debian-linux-gnu (i686)
--
-- Host: 127.0.0.1    Database: vuadaubep
-- ------------------------------------------------------
-- Server version	5.6.19-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `CDR_2015050X`
--

DROP TABLE IF EXISTS `CDR_2015050X`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CDR_2015050X` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `FEE` int(3) NOT NULL DEFAULT '0',
  `REASON` varchar(15) COLLATE utf8_unicode_ci NOT NULL COMMENT 'REGISTER, RE-REGISTER, RENEW',
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `TRANS_ID` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `MSISDN_INDEX` (`MSISDN`),
  KEY `REASON_INDEX` (`REASON`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='lưu các giao dịch charge cước thành công.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CDR_2015050X`
--

LOCK TABLES `CDR_2015050X` WRITE;
/*!40000 ALTER TABLE `CDR_2015050X` DISABLE KEYS */;
INSERT INTO `CDR_2015050X` VALUES (1,'841689972827',0,'REGISTER','2015-05-06 19:20:34','S23'),(2,'841689972827',0,'ANSWER','2015-05-06 19:26:03','W14'),(3,'841689972827',0,'ANSWER','2015-05-06 19:26:11','W15'),(4,'841689972827',0,'ANSWER','2015-05-06 19:26:14','W16'),(5,'841689972827',0,'ANSWER','2015-05-06 19:26:14','W17'),(6,'841689972827',0,'ANSWER','2015-05-06 19:26:15','W18'),(7,'841689972827',0,'ANSWER','2015-05-06 19:26:25','M87');
/*!40000 ALTER TABLE `CDR_2015050X` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CDR_2015051X`
--

DROP TABLE IF EXISTS `CDR_2015051X`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CDR_2015051X` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `FEE` int(3) NOT NULL DEFAULT '0',
  `REASON` varchar(15) COLLATE utf8_unicode_ci NOT NULL COMMENT 'REGISTER, RE-REGISTER, RENEW',
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `TRANS_ID` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `MSISDN_INDEX` (`MSISDN`),
  KEY `REASON_INDEX` (`REASON`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='lưu các giao dịch charge cước thành công.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CDR_2015051X`
--

LOCK TABLES `CDR_2015051X` WRITE;
/*!40000 ALTER TABLE `CDR_2015051X` DISABLE KEYS */;
/*!40000 ALTER TABLE `CDR_2015051X` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CDR_2015052X`
--

DROP TABLE IF EXISTS `CDR_2015052X`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CDR_2015052X` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `FEE` int(3) NOT NULL DEFAULT '0',
  `REASON` varchar(15) COLLATE utf8_unicode_ci NOT NULL COMMENT 'REGISTER, RE-REGISTER, RENEW',
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `TRANS_ID` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `MSISDN_INDEX` (`MSISDN`),
  KEY `REASON_INDEX` (`REASON`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='lưu các giao dịch charge cước thành công.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CDR_2015052X`
--

LOCK TABLES `CDR_2015052X` WRITE;
/*!40000 ALTER TABLE `CDR_2015052X` DISABLE KEYS */;
/*!40000 ALTER TABLE `CDR_2015052X` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CHARGE_REMINDER`
--

DROP TABLE IF EXISTS `CHARGE_REMINDER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CHARGE_REMINDER` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `ADD_SCORE` int(4) NOT NULL,
  `TYPE` int(4) NOT NULL DEFAULT '1',
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `TRANS_ID` varchar(15) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'INV',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CHARGE_REMINDER`
--

LOCK TABLES `CHARGE_REMINDER` WRITE;
/*!40000 ALTER TABLE `CHARGE_REMINDER` DISABLE KEYS */;
/*!40000 ALTER TABLE `CHARGE_REMINDER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MESSAGE`
--

DROP TABLE IF EXISTS `MESSAGE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MESSAGE` (
  `CODE` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `STATUS` int(3) NOT NULL DEFAULT '1' COMMENT '1=active\nother=inactive',
  `DESC` varchar(145) COLLATE utf8_unicode_ci NOT NULL,
  `CONTENT` varchar(450) COLLATE utf8_unicode_ci NOT NULL,
  `COMMENT` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Lưu các SMS MT kịch bản, module APS sẽ trả SMS về theo từng ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MESSAGE`
--

LOCK TABLES `MESSAGE` WRITE;
/*!40000 ALTER TABLE `MESSAGE` DISABLE KEYS */;
INSERT INTO `MESSAGE` VALUES ('1.1.1',1,'ĐK lần đầu thành công, MT-1','### Quy khach da dang ky thanh cong CT Vua Dau Bep Viet Nam. Ngay dau tien phi 0d, tu ngay thu 2 phi DV la 3.000d/ngay va duoc gia han hang ngay. De huy DV, soan HUY VDB gui 9154. De duoc huong dan, soan HD VDB gui 9154. Chi tiet tai http://m.donghanh.mobi hoac LH 19006430 (1.000d/phut).','MPS trả tin'),('1.1.2',1,'ĐK lần đầu thành công, MT-2','Chao mung Quy khach dang tham gia CT Vua Dau Bep Viet Nam. Quy khach duoc tang 20.000 diem. Hay tra loi cau hoi va tich luy diem de nhan duoc giai thuong co gia tri len toi 197 trieu dong tu CT. LH 19006430 (1.000d/phut) hoac truy cap: http://m.donghanh.mobi. Tran trong.',NULL),('1.2-1.3',1,'ĐK lại trong cùng ngày','### Chao mung Quy khach quay tro lai voi chuong trinh Vua dau bep Viet Nam! Quy khach co [TOTAL_SCORE] diem de tich diem trung thuong 197 trieu dong tu chuong trinh. Chi tiet goi 19006430 (1.000d/phut) hoac truy cap: http://m.donghanh.mobi.','MPS trả tin'),('1.4',1,'ĐK lại vào ngày hôm sau','### Chao mung Quy khach quay tro lai voi chuong trinh Vua dau bep Viet Nam! Quy khach duoc tang 3.000 diem va hien co [TOTAL_SCORE] diem de tich diem trung thuong. Dich vu se tu dong gia han voi muc cuoc 3.000d/ngay. De huy dich vu, soan HUY VDB gui 9154. Chi tiet goi 19006430 (1.000d/phut) hoac truy cap: http://m.donghanh.mobi. Tran trong.   ','MPS trả tin'),('1.5',1,'ĐK khi đang active','### Quy khach da dang ky CT Vua Dau Bep Viet Nam truoc do. De huy DV, soan HUY VDB gui 9154. De duoc huong dan, soan HD VDB gui 9154. Chi tiet tai http://m.donghanh.mobi hoac LH 19006430 (1.000d/phut).','MPS trả tin'),('100',1,'Trả lời, xem điểm ... khi chưa ĐK','Yeu cau cua quy khach khong thuc hien duoc do quy khach chua dang ky CT Vua Dau Bep Viet Nam. De dang ky, soan DK VDB gui 9154 (3.000d/ngay). Chi tiet tai http://m.donghanh.mobi hoac LH 19006430 (1.000d/phut).',NULL),('2.1',1,'Hủy thành công ','### Quy khach da huy thanh cong CT Vua Dau Bep Viet Nam. So diem Quy khach da tich luy van duoc bao luu va cong don cho lan dang ky sau. De dang ky lai, soan DK VDB gui 9154 ( 3.000d/ngay). Co hoi so huu giai thuong len den 197 trieu dong dang cho Quy khach. Chi tiet tai http://m.donghanh.mobi hoac LH 19006430 (1.000d/phut).','MPS trả tin'),('2.2',1,'Hủy khi đang không active','### Quy khach huy khong thanh cong do chua dang ky CT Vua Dau Bep Viet Nam. De dang ky, soan DK VDB gui 9154 (3.000d/ngay). Chi tiet tai http://m.donghanh.mobi hoac LH 19006430 (1.000d/phut).','MPS trả tin'),('4.1',1,'Trả lời đúng','Cau tra loi chinh xac. Quy khach nhan duoc 1.000 diem tu CT Vua Dau Bep Viet Nam, Quy khach hien co [TOTAL_SCORE] diem. Hay tiep tuc tra loi cau hoi va tich luy diem de nhan giai thuong 197 trieu dong tu chuong trinh.',NULL),('4.2',1,'Trả lời sai ','Cau tra loi chua chinh xac. Quy khach dang co [TOTAL_SCORE] diem tu CT Vua Dau Bep Viet Nam. Quy khach co the lam tot hon o cau hoi tiep theo. Dung nan chi, Hay co gang nhe.',NULL),('4.3',1,'Trả lời câu cuối, hoặc trả lời sau khi đã trả lời hết các câu',' Quy khach da tra loi het 05 cau hoi hom nay. Quy khach dang co [TOTAL_SCORE]. Hay tiep tuc tra loi cac cau hoi vao dang cach truy cap wapsite http://m.donghanh.mobi.  LH 19006430 (1.000d/phut).',NULL),('4.4',1,'Trả lời khi chưa nhận câu hỏi','Chuong trinh hom nay chua bat dau. Quy khach vui long doi them. De cap nhat cac CT Gameshow moi nhat, truy cap http://m.donghanh.mobi hoac LH 19006430 (1.000d/phut).',NULL),('5.10',1,'Hủy tự động do ko charge dc cước','CT Vua Dau Bep Viet Nam cua Quy khach bi huy do tai khoan khong du de su dung. Quy khach vui long nap them tien vao tai khoan va soan DK VDB gui 9154 (3000d/ngay) de tiep tuc su dung. Chi tiet tai http://m.donghanh.mobi hoac LH 19006430 (1.000d/phut).',NULL),('5.2',1,'Hướng dẫn sử dụng','Chao mung Quy khach den voi CT Vua Dau Bep Viet Nam. Hay tra loi cau hoi va tich luy diem de nhan duoc nhieu giai thuong hap dan. De dang ky, soan DK VDB (3.000d/ngay). De huy, soan HUY VDB. De tra loi cau hoi cua CT, soan 1 hoac 2. De tra diem soan DIEM VDB. Tat ca gui den 9154 (0d). Chi tiet tai http://m.donghanh.mobi hoac LH 19006430 (1.000d/phut).',NULL),('5.4',1,'Từ chối nhận câu hỏi','Quy khach da du choi nhan cau hoi hang ngay thanh cong. Diem cua Quy khach se duoc bao luu va xet thuong theo chu ky. Chuc Quy khach may man.',NULL),('5.5',1,'Tiếp tục đk nhận câu hỏi','Quy khach da kich hoat nhan cau hoi hang ngay. Chuc quy khach may man!',NULL),('5.6',1,'Xem điểm ','Diem tich luy thang [CUR_MONTH] cua Quy khach dang co [MONTH_SCORE] diem. Tong quy diem cua Quy khach dang co: [TOTAL_SCORE] diem. Moi Quy khach tiep tuc tra loi cau hoi moi ngay de tich luy kien thuc va co hoi so huu giai thuong len den 197 trieu dong. Chi tiet tai http://m.donghanh.mobi hoac LH 19006430 (1.000d/phut).',NULL),('5.8',1,'Sai cú pháp ','Tin nhan sai cu phap. De tra loi, soan 1 hoac 2 gui 9154 (mien phi). De biet them chi tiet ve dich vu goi 19006430 (1.000d/phut) hoac truy cap://m.donghanh.mobi.',NULL),('5.9',1,'Lỗi hệ thống','Hien tai he thong dang ban, Quy khach vui long thu lai sau. Chi tiet tai http://m.donghanh.mobi hoac LH 19006430 (1.000d/phut).',NULL);
/*!40000 ALTER TABLE `MESSAGE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MO_201505`
--

DROP TABLE IF EXISTS `MO_201505`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MO_201505` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `CONTENT` varchar(115) COLLATE utf8_unicode_ci NOT NULL,
  `COMMAND` varchar(25) COLLATE utf8_unicode_ci NOT NULL COMMENT 'REGISTER, UNREGISTER, ANSWER, LASTQUESTION, GUIDELINE, VIEWSCORE, WRONGSYNTAX ....',
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `TRANS_ID` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Trans id, map mới transid trong log ',
  PRIMARY KEY (`ID`),
  KEY `MSISDN_INDEX` (`MSISDN`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Lưu lịch sử MO';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MO_201505`
--

LOCK TABLES `MO_201505` WRITE;
/*!40000 ALTER TABLE `MO_201505` DISABLE KEYS */;
INSERT INTO `MO_201505` VALUES (1,'841689972827','#wap','last_question','2015-05-06 19:20:30','WG16'),(2,'841689972827','dk','register','2015-05-06 19:20:34','S23'),(3,'841689972827','#wap','answer','2015-05-06 19:20:39','W11'),(4,'841689972827','#wap','last_question','2015-05-06 19:20:42','WG17'),(5,'841689972827','#wap','answer','2015-05-06 19:21:47','W12'),(6,'841689972827','#wap','answer','2015-05-06 19:23:15','W13'),(7,'841689972827','#wap','last_question','2015-05-06 19:25:57','WG18'),(8,'841689972827','#wap','answer','2015-05-06 19:26:03','W14'),(9,'841689972827','#wap','answer','2015-05-06 19:26:11','W15'),(10,'841689972827','#wap','answer','2015-05-06 19:26:14','W16'),(11,'841689972827','#wap','answer','2015-05-06 19:26:14','W17'),(12,'841689972827','#wap','answer','2015-05-06 19:26:15','W18'),(13,'841689972827','#wap','answer','2015-05-06 19:26:16','W19'),(14,'841689972827','#wap','answer','2015-05-06 19:26:19','W20'),(15,'841689972827','1','answer','2015-05-06 19:26:25','M87');
/*!40000 ALTER TABLE `MO_201505` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MO_201506`
--

DROP TABLE IF EXISTS `MO_201506`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MO_201506` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `CONTENT` varchar(115) COLLATE utf8_unicode_ci NOT NULL,
  `COMMAND` varchar(25) COLLATE utf8_unicode_ci NOT NULL COMMENT 'REGISTER, UNREGISTER, ANSWER, LASTQUESTION, GUIDELINE, VIEWSCORE, WRONGSYNTAX ....',
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `TRANS_ID` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Trans id, map mới transid trong log ',
  PRIMARY KEY (`ID`),
  KEY `MSISDN_INDEX` (`MSISDN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Lưu lịch sử MO';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MO_201506`
--

LOCK TABLES `MO_201506` WRITE;
/*!40000 ALTER TABLE `MO_201506` DISABLE KEYS */;
/*!40000 ALTER TABLE `MO_201506` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MT_201505`
--

DROP TABLE IF EXISTS `MT_201505`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MT_201505` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `CONTENT` varchar(650) COLLATE utf8_unicode_ci NOT NULL,
  `MO_ID` int(11) NOT NULL,
  `COMMAND` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `SEND_RESULT` varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL,
  `TRANS_ID` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `MSISDN_INDEX` (`MSISDN`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Lưu lịch sử SMS MT';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MT_201505`
--

LOCK TABLES `MT_201505` WRITE;
/*!40000 ALTER TABLE `MT_201505` DISABLE KEYS */;
INSERT INTO `MT_201505` VALUES (1,'841689972827','Yeu cau cua quy khach khong thuc hien duoc do quy khach chua dang ky CT Vua Dau Bep Viet Nam. De dang ky, soan DK VDB gui 9154 (3.000d/ngay). Chi tiet tai http://m.donghanh.mobi hoac LH 19006430 (1.000d/phut).',1,'100','2015-05-06 19:20:30','0','WG16'),(2,'841689972827','### Quy khach da dang ky thanh cong CT Vua Dau Bep Viet Nam. Ngay dau tien phi 0d, tu ngay thu 2 phi DV la 3.000d/ngay va duoc gia han hang ngay. De huy DV, soan HUY VDB gui 9154. De duoc huong dan, soan HD VDB gui 9154. Chi tiet tai http://m.donghanh.mobi hoac LH 19006430 (1.000d/phut).',2,'1.1.1','2015-05-06 19:20:34','0','S23'),(3,'841689972827','Chuong trinh hom nay chua bat dau. Quy khach vui long doi them. De cap nhat cac CT Gameshow moi nhat, truy cap http://m.donghanh.mobi hoac LH 19006430 (1.000d/phut).',3,'4.4','2015-05-06 19:20:39','0','W11'),(4,'841689972827','Tinh gia tri bieu thuc: 3 - 2 = ?, \\n1) bang 1\\n2) bang 2',4,'QT-10','2015-05-06 19:20:42','0','WG17'),(5,'841689972827','Chao mung Quy khach dang tham gia CT Vua Dau Bep Viet Nam. Quy khach duoc tang 20.000 diem. Hay tra loi cau hoi va tich luy diem de nhan duoc giai thuong co gia tri len toi 197 trieu dong tu CT. LH 19006430 (1.000d/phut) hoac truy cap: http://m.donghanh.mobi. Tran trong.',2,'1.1.2','2015-05-06 19:20:42','0','S23'),(6,'841689972827','Tinh gia tri bieu thuc: 9 - 8 = ?, \\n1) bang 1\\n2) bang 2',2,'QT-16','2015-05-06 19:20:50','0','S23'),(7,'841689972827','Chuong trinh hom nay chua bat dau. Quy khach vui long doi them. De cap nhat cac CT Gameshow moi nhat, truy cap http://m.donghanh.mobi hoac LH 19006430 (1.000d/phut).',5,'4.4','2015-05-06 19:21:47','0','W12'),(8,'841689972827','Tinh gia tri bieu thuc: 6 - 5 = ?, \\n1) bang 1\\n2) bang 2',7,'QT-13','2015-05-06 19:25:57','0','WG18'),(9,'841689972827','Cau tra loi chua chinh xac. Quy khach dang co 2000 diem tu CT Vua Dau Bep Viet Nam. Quy khach co the lam tot hon o cau hoi tiep theo. Dung nan chi, Hay co gang nhe.',8,'4.2','2015-05-06 19:26:03','0','W14'),(10,'841689972827','Chuong trinh BNHV nam 2015 co bao nhieu Live Show? E= 10 ; F= 12',8,'QT-36','2015-05-06 19:26:11','0','W14'),(11,'841689972827','Sai roi, nam nay BNHV co 12 live show',9,'WR-36','2015-05-06 19:26:11','0','W15'),(12,'841689972827','Sai roi, BNHV duoc phat song tren VTV3 dai truyen hinh VN',10,'WR-38','2015-05-06 19:26:14','0','W16'),(13,'841689972827','Cau tra loi chua chinh xac. Quy khach dang co 2000 diem tu CT Vua Dau Bep Viet Nam. Quy khach co the lam tot hon o cau hoi tiep theo. Dung nan chi, Hay co gang nhe.',11,'4.2','2015-05-06 19:26:14','0','W17'),(14,'841689972827','Cau tra loi chua chinh xac. Quy khach dang co 2000 diem tu CT Vua Dau Bep Viet Nam. Quy khach co the lam tot hon o cau hoi tiep theo. Dung nan chi, Hay co gang nhe.',12,'4.2','2015-05-06 19:26:15','0','W18'),(15,'841689972827',' Quy khach da tra loi het 05 cau hoi hom nay. Quy khach dang co 2000. Hay tiep tuc tra loi cac cau hoi vao dang cach truy cap wapsite http://m.donghanh.mobi.  LH 19006430 (1.000d/phut).',13,'4.3','2015-05-06 19:26:16','0','W19'),(16,'841689972827',' Quy khach da tra loi het 05 cau hoi hom nay. Quy khach dang co 2000. Hay tiep tuc tra loi cac cau hoi vao dang cach truy cap wapsite http://m.donghanh.mobi.  LH 19006430 (1.000d/phut).',14,'4.3','2015-05-06 19:26:19','0','W20'),(17,'841689972827','BNHV 2015 duoc phat song tren kenh nao cua dai truyen hinh? E= VTV3 ; F= VTV1',9,'QT-38','2015-05-06 19:26:19','0','W15'),(18,'841689972827','Tinh gia tri bieu thuc: 5 - 4 = ?, \\n1) bang 1\\n2) bang 2',10,'QT-12','2015-05-06 19:26:22','0','W16'),(19,'841689972827','Tinh gia tri bieu thuc: 8 - 7 = ?, \\n1) bang 1\\n2) bang 2',11,'QT-15','2015-05-06 19:26:22','0','W17'),(20,'841689972827',' Quy khach da tra loi het 05 cau hoi hom nay. Quy khach dang co 2000. Hay tiep tuc tra loi cac cau hoi vao dang cach truy cap wapsite http://m.donghanh.mobi.  LH 19006430 (1.000d/phut).',12,'4.3','2015-05-06 19:26:23','0','W18'),(21,'841689972827','Cau tra loi chinh xac. Quy khach nhan duoc 1.000 diem tu CT Vua Dau Bep Viet Nam, Quy khach hien co 2000 diem. Hay tiep tuc tra loi cau hoi va tich luy diem de nhan giai thuong 197 trieu dong tu chuong trinh.',15,'4.1','2015-05-06 19:26:25','0','M87'),(22,'841689972827','Ban nhay cua Vuong Khang co ten la gi? E= Anna ; F= Zhivko',15,'QT-37','2015-05-06 19:26:33','0','M87');
/*!40000 ALTER TABLE `MT_201505` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MT_201506`
--

DROP TABLE IF EXISTS `MT_201506`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MT_201506` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `CONTENT` varchar(650) COLLATE utf8_unicode_ci NOT NULL,
  `MO_ID` int(11) NOT NULL,
  `COMMAND` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `SEND_RESULT` varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL,
  `TRANS_ID` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `MSISDN_INDEX` (`MSISDN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Lưu lịch sử SMS MT';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MT_201506`
--

LOCK TABLES `MT_201506` WRITE;
/*!40000 ALTER TABLE `MT_201506` DISABLE KEYS */;
/*!40000 ALTER TABLE `MT_201506` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MT_BROADCAST`
--

DROP TABLE IF EXISTS `MT_BROADCAST`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MT_BROADCAST` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `CONTENT` varchar(450) COLLATE utf8_unicode_ci NOT NULL,
  `SEND_TIME` timestamp NULL DEFAULT NULL COMMENT 'Thời gian gửi, nếu muốn gửi ngay lập tức có thể để null hoặc giá trị nhỏ hơn hiện tại.',
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `STATUS` int(2) NOT NULL DEFAULT '1' COMMENT 'Chỉ gửi những row có status = 1',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Bảng này để gửi SMS cho tập khách hàng, module APS quét bảng';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MT_BROADCAST`
--

LOCK TABLES `MT_BROADCAST` WRITE;
/*!40000 ALTER TABLE `MT_BROADCAST` DISABLE KEYS */;
/*!40000 ALTER TABLE `MT_BROADCAST` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QUESTION`
--

DROP TABLE IF EXISTS `QUESTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QUESTION` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STATUS` int(3) NOT NULL DEFAULT '1',
  `CONTENT` varchar(450) COLLATE utf8_unicode_ci NOT NULL,
  `CONFIRM_CORRECT_MT` varchar(145) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CONFIRM_WRONG_MT` varchar(145) COLLATE utf8_unicode_ci DEFAULT NULL,
  `RESULT` varchar(2) COLLATE utf8_unicode_ci NOT NULL,
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `VALID_ANSWER` varchar(45) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Ví dụ câu hỏi có 3 đáp án hợp lệ 1,2 & 3 => dữ liệu column = 1;2;3',
  `RETURN_DATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `QUESTION_ORDER` int(2) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Bảng lưu câu hỏi';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QUESTION`
--

LOCK TABLES `QUESTION` WRITE;
/*!40000 ALTER TABLE `QUESTION` DISABLE KEYS */;
INSERT INTO `QUESTION` VALUES (8,1,'Tinh gia tri bieu thuc: 1 - 0 = ?, \n1) bang 1\n2) bang 2',NULL,NULL,'1','2016-06-27 08:58:06','1;2;3','0000-00-00 00:00:00',1),(9,1,'Tinh gia tri bieu thuc: 2 - 1 = ?, \n1) bang 1\n2) bang 2',NULL,NULL,'1','2016-06-27 08:58:06','1;2;3','0000-00-00 00:00:00',1),(10,1,'Tinh gia tri bieu thuc: 3 - 2 = ?, \n1) bang 1\n2) bang 2',NULL,NULL,'1','2016-06-27 08:58:06','1;2;3','0000-00-00 00:00:00',1),(11,1,'Tinh gia tri bieu thuc: 4 - 3 = ?, \n1) bang 1\n2) bang 2',NULL,NULL,'1','2016-06-27 08:58:06','1;2;3','0000-00-00 00:00:00',1),(12,1,'Tinh gia tri bieu thuc: 5 - 4 = ?, \n1) bang 1\n2) bang 2',NULL,NULL,'1','2016-06-27 08:58:06','1;2;3','0000-00-00 00:00:00',1),(13,1,'Tinh gia tri bieu thuc: 6 - 5 = ?, \n1) bang 1\n2) bang 2',NULL,NULL,'1','2016-06-27 08:58:06','1;2;3','0000-00-00 00:00:00',1),(14,1,'Tinh gia tri bieu thuc: 7 - 6 = ?, \n1) bang 1\n2) bang 2',NULL,NULL,'1','2016-06-27 08:58:06','1;2;3','0000-00-00 00:00:00',1),(15,1,'Tinh gia tri bieu thuc: 8 - 7 = ?, \n1) bang 1\n2) bang 2',NULL,NULL,'1','2016-06-27 08:58:06','1;2;3','0000-00-00 00:00:00',1),(16,1,'Tinh gia tri bieu thuc: 9 - 8 = ?, \n1) bang 1\n2) bang 2',NULL,NULL,'1','2016-06-27 08:58:06','1;2;3','0000-00-00 00:00:00',1),(19,1,'Buoc nhay hoan vu nam 2015 co bao nhieu nghe si tham gia? E = 8 ; F = 6','Chinh xac, BNHV nam 2015 co so luong nghe si tham gia nhieu nhat so voi cac mua truoc la 8 nghe si','Sai roi, BNHV nam 2015 co su gop mat cua 8 nghe si','E','2014-12-27 08:16:41','E, F','0000-00-00 00:00:00',1),(20,1,'Nghe si nao khong tham gia buoc nhay hoan vu 2015? E= Truong Giang ; F= Su Duy Vuong','Chinh xac, trong BNHV 2015 Truong Giang khong co ten trong danh sach tham gia','Sai roi, Su Duy Vuong la thi sinh tham gia BNHV 2015','E','2014-12-27 08:20:28','E, F','0000-00-00 00:00:00',2),(21,1,'Angiela Phuong Trinh da tham gia thi BNHV lan thu bao nhieu? E= 2 ; F= 1','Chinh xac, day la lan dau tien Phuong Trinh tham gia BNHV','Sai roi, day la lan dau tien Phuong Trinh tham gia BNHV','F','2014-12-27 08:22:21','E, F','0000-00-00 00:00:00',3),(22,1,'Vuong Khang thi sinh BNHV 2015 la? E= Ca si ; F= Hot boy\r\n','Chinh xac, Vuong Khang la ca si gay an tuong voi moi nguoi bang than hinh ngoai co cua minh','Sai roi,  Vuong Khang la ca si gay an tuong voi moi nguoi bang than hinh ngoai co cua minh','E','2014-12-27 08:31:21','E, F','0000-00-00 00:00:00',4),(23,1,'Dumbo Nguyen thi sinh BNHV 2015 la? E= Truong nhom nhay Poreotics ;\r\nF= Truong nhom nhay big toe','Chinh xac, Dumbo Nguyen chinh la truong nhom nhay Poreotics noi tieng','Sai roi, Dumbo Nguyen chinh la truong nhom nhay Poreotics noi tieng','E','2014-12-27 08:34:27','E, F','0000-00-00 00:00:00',5),(24,1,'Tran Thanh la MC cua BNHV 2015 dung hay sai? E= Dung ; F= Sai\r\n','Chinh xac, Tran Thanh khong phai la MC cua BNHV nam 2015','Sai roi, Tran Thanh khong phai la MC cua BNHV nam 2015','F','2014-12-27 08:36:53','E, F','0000-00-00 00:00:00',6),(25,1,'Trac thuy Mieu co phai la 1 trong so nhung giam khao cua BNHV 2015 khong?\r\nE= Co ; F= Khong','Chinh xac, Trac thuy Mieu la 1 trong 5 vi giam khao cua BNHV 2015','Sai roi, Trac thuy Mieu la 1 trong 5 vi giam khao cua BNHV 2015','E','2014-12-27 08:38:49','E, F','0000-00-00 00:00:00',7),(26,1,'Ai khong co ten trong thanh phan ban giam khao cua BNHV 2015? E= Hoai Linh ; F= Hong Viet','Chinh xac, Hoai Linh khong phai la giam khao cua BNHV 2015','Sai roi, Hong Viet moi la giam khao cua BNHV 2015','F','2014-12-27 08:41:19','E, F','0000-00-00 00:00:00',8),(27,1,'Chi Anh co phai la 1 trong so nhung vi giam khao cua BNHV 2015 khong? E= Co ; F= Khong','Chinh xac, Chi Anh khong phai la giam khao cua BNHV 2015','Sai roi, Chi Anh khong phai la giam khao cua BNHV 2015','F','2014-12-27 08:43:05','E, F','0000-00-00 00:00:00',9),(28,1,'Tran Ly Ly - giam khao BNHV 2015 la? E= Bien dao mua ; F= Nha bao','Chinh xac, Tran Ly Ly la bien dao mua noi tieng cua Viet Nam','Sai roi, Tran Ly Ly la bien dao mua noi tieng cua Viet Nam','E','2014-12-27 08:53:58','E, F','0000-00-00 00:00:00',10),(29,1,'Ban nhay cua Angiela Phuong Trinh co ten la gi ? E= Kristian ; F= Georgi Naydenov','Chinh xac,  Kristian chinh la ban nhay cua Phuong Trinh','Sai roi, Kristian chinh la ban nhay cua Phuong Trinh','E','2014-12-27 08:56:08','E, F','0000-00-00 00:00:00',1),(30,1,'Daniel la ban nhay cua Lan Ngoc dung hay sai? E= Dung ; F= Sai','Chinh xac, Daniel chinh la ban nhay cung voi Lan Ngoc','Sai roi, Daniel chinh la ban nhay cung voi Lan Ngoc','E','2014-12-27 08:57:58','E, F','0000-00-00 00:00:00',2),(31,1,'Antoaneta la ban nhay cua Dumbo Nguyen dung hay sai? E= Dung ; F= Sai','Chinh xac, Antoaneta khong phai la ban nhay cua Dumbo Nguyen','Sai roi, Antoaneta khong phai la ban nhay cua Dumbo Nguyen','F','2014-12-27 08:59:36','E, F','0000-00-00 00:00:00',3),(32,1,'BNHV 2015 phat song so dau tien vao ngay bao nhieu? E= 3/1/2015 ; F= 5/1/2015','Chinh xac, so dau tien cua BNHV phat song vao ngay 3/1/2015','Sai roi, so dau tien cua BNHV phat song vao ngay 3/1/2015','E','2014-12-27 09:01:43','E, F','0000-00-00 00:00:00',4),(33,1,'Nghe si nao khong tham gia buoc nhay hoan vu 2015? E= Nha Phuong ; F=Ninh Duong Lan Ngoc','Chinh xac, Nha Phuong khong tham gia BNHV 2015','Sai roi, Nha Phuong khong tham gia BNHV 2015','E','2014-12-27 09:03:32','E, F','0000-00-00 00:00:00',5),(34,1,'Su Duy Vuong tham gia BNHV 2015 buoc ra tu cuoc thi nao? E= The X-Factor ; F= Viet Nam gotalent','Chinh xac, Su Duy Vuong xuat hien tu cuoc thi The X-Factor','Sai roi, Su Duy Vuong xuat hien tu cuoc thi The X-Factor','E','2014-12-27 09:05:16','E, F','0000-00-00 00:00:00',6),(35,1,'Chi Pu thi sinh BNHV 2015 la? E= Dien vien ; F= Bien dao mua','Chinh xac, Chi Pu la 1 dien vien, ca si noi tieng hien nay','Sai roi, Chi Pu la 1 dien vien, ca si noi tieng hien nay','E','2014-12-27 09:06:59','E, F','0000-00-00 00:00:00',7),(36,1,'Chuong trinh BNHV nam 2015 co bao nhieu Live Show? E= 10 ; F= 12','Chinh xac, nam nay BNHV co 12 live show','Sai roi, nam nay BNHV co 12 live show','F','2014-12-27 09:08:38','E, F','0000-00-00 00:00:00',8),(37,1,'Ban nhay cua Vuong Khang co ten la gi? E= Anna ; F= Zhivko','Chinh xac, Anna la ban nhay cung voi Vuong Khang','Sai roi, Anna la ban nhay cung voi Vuong Khang','E','2014-12-27 09:10:11','E, F','0000-00-00 00:00:00',9),(38,1,'BNHV 2015 duoc phat song tren kenh nao cua dai truyen hinh? E= VTV3 ; F= VTV1','Chinh xac, BNHV duoc phat song tren VTV3 dai truyen hinh VN','Sai roi, BNHV duoc phat song tren VTV3 dai truyen hinh VN','E','2014-12-27 09:35:37','E, F','0000-00-00 00:00:00',10);
/*!40000 ALTER TABLE `QUESTION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SCORE_HIS`
--

DROP TABLE IF EXISTS `SCORE_HIS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SCORE_HIS` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(15) COLLATE utf8_unicode_ci NOT NULL DEFAULT '0',
  `ADDED_SCORE` int(6) NOT NULL DEFAULT '0',
  `DAY_SCORE` int(6) NOT NULL DEFAULT '0',
  `WEEK_SCORE` int(6) NOT NULL DEFAULT '0',
  `MONTH_SCORE` int(6) NOT NULL DEFAULT '0',
  `TOTAL_SCORE` int(6) NOT NULL DEFAULT '0',
  `REASON` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `TRANS_ID` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Lịch sử cộng điểm cho khách hàng';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SCORE_HIS`
--

LOCK TABLES `SCORE_HIS` WRITE;
/*!40000 ALTER TABLE `SCORE_HIS` DISABLE KEYS */;
INSERT INTO `SCORE_HIS` VALUES (1,'841689972827',2000,2000,2000,2000,2000,'REGISTER','2015-05-06 19:20:34','S23'),(2,'841689972827',100,2000,2000,2000,2000,'ANSWER','2015-05-06 19:26:25','M87');
/*!40000 ALTER TABLE `SCORE_HIS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SUBSCRIBER`
--

DROP TABLE IF EXISTS `SUBSCRIBER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SUBSCRIBER` (
  `MSISDN` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `ACTIVE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `DEACTIVE_TIME` timestamp NULL DEFAULT NULL,
  `ACTIVE_CHANNEL` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `DEACTIVE_CHANNEL` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DAY_SCORE` int(6) NOT NULL DEFAULT '0',
  `WEEK_SCORE` int(6) NOT NULL DEFAULT '0',
  `MONTH_SCORE` int(6) NOT NULL DEFAULT '0',
  `TOTAL_SCORE` int(6) NOT NULL DEFAULT '0',
  `LAST_MODIFIED` timestamp NULL DEFAULT NULL,
  `STATUS` int(2) NOT NULL DEFAULT '1' COMMENT '1=active\n2=pending\n3=deactive ',
  `QUES_RECEIVED` varchar(5000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'lưu các id của các câu hỏi đã nhận',
  `LAST_QUES_SMS` int(8) NOT NULL DEFAULT '0',
  `EXPIRE_TIME` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `ANSWERED_SMS` int(3) NOT NULL DEFAULT '0',
  `IGNORE_QUESTION` int(1) NOT NULL DEFAULT '0',
  `ANSWERED_WAP` int(3) NOT NULL DEFAULT '0',
  `LAST_QUES_WAP` int(8) NOT NULL DEFAULT '0',
  `ANSWERED_WEB` int(3) NOT NULL DEFAULT '0',
  `LAST_QUES_WEB` int(8) NOT NULL DEFAULT '0',
  PRIMARY KEY (`MSISDN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SUBSCRIBER`
--

LOCK TABLES `SUBSCRIBER` WRITE;
/*!40000 ALTER TABLE `SUBSCRIBER` DISABLE KEYS */;
INSERT INTO `SUBSCRIBER` VALUES ('841689972827','2015-05-06 19:20:34',NULL,'SMS',NULL,2000,2000,2000,2000,'2015-05-06 19:20:34',1,';16;13;36;38;12;15;37;',37,'2015-05-07 16:59:59',1,0,5,15,0,0);
/*!40000 ALTER TABLE `SUBSCRIBER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `WINNER`
--

DROP TABLE IF EXISTS `WINNER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `WINNER` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSISDN` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `DAY_SCORE` int(6) NOT NULL DEFAULT '0',
  `WEEK_SCORE` int(6) NOT NULL DEFAULT '0',
  `MONTH_SCORE` int(6) NOT NULL DEFAULT '0',
  `TOTAL_SCORE` int(6) NOT NULL DEFAULT '0',
  `TYPE` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `SYNTHETIC_DATE` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Bảng lưu những người chiến thắng...';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `WINNER`
--

LOCK TABLES `WINNER` WRITE;
/*!40000 ALTER TABLE `WINNER` DISABLE KEYS */;
/*!40000 ALTER TABLE `WINNER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'vuadaubep'
--

--
-- Dumping routines for database 'vuadaubep'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-05-11 22:21:07
