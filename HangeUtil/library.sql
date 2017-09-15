/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : library

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2017-09-13 14:34:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bookname` varchar(50) DEFAULT NULL,
  `count` int(11) DEFAULT NULL COMMENT '库存',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES ('2', '西游记', '1');
INSERT INTO `book` VALUES ('3', '水浒传', '3');
INSERT INTO `book` VALUES ('4', '红楼梦', '1');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(10) DEFAULT NULL,
  `password` varchar(16) DEFAULT NULL,
  `role` varchar(1) DEFAULT '0' COMMENT '0:普通人员 1:图书管理员',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('2', 'root', '1', '1');
INSERT INTO `user` VALUES ('3', 'test', '1', '0');
INSERT INTO `user` VALUES ('4', 't', '520', '0');
INSERT INTO `user` VALUES ('5', 'ttt', '123', '0');

-- ----------------------------
-- Table structure for user_book
-- ----------------------------
DROP TABLE IF EXISTS `user_book`;
CREATE TABLE `user_book` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT NULL,
  `bookid` int(11) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL COMMENT '0:借出 1:已归还',
  `createtime` bigint(20) DEFAULT '0' COMMENT '借书时间',
  `returntime` bigint(20) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_book
-- ----------------------------
INSERT INTO `user_book` VALUES ('2', '3', '3', '1', '1505282272640', '1505283156684');
INSERT INTO `user_book` VALUES ('3', '3', '4', '0', '1505282287951', '0');
INSERT INTO `user_book` VALUES ('6', '4', '3', '0', '1505283695923', '0');
INSERT INTO `user_book` VALUES ('7', '4', '4', '0', '1505283699224', '0');
