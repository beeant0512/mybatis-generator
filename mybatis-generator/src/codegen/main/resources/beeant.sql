/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50626
Source Host           : localhost:3306
Source Database       : beeant

Target Server Type    : MYSQL
Target Server Version : 50626
File Encoding         : 65001

Date: 2016-03-09 14:09:45
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for app_role
-- ----------------------------
DROP TABLE IF EXISTS `app_role`;
CREATE TABLE `app_role` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `root_id` varchar(36) DEFAULT '0' COMMENT '外部节点ID',
  `pid` varchar(36) DEFAULT '0' COMMENT '父ID',
  `full_path` varchar(1000) DEFAULT '' COMMENT '全路径',
  `grade` int(8) unsigned DEFAULT '1' COMMENT '深度',
  `sort_num` int(8) unsigned DEFAULT '0' COMMENT '排序值',
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `description` varchar(255) DEFAULT '' COMMENT '描述',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` enum('1','0') DEFAULT '0' COMMENT '删除',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of app_role
-- ----------------------------
INSERT INTO `app_role` VALUES ('daed3e5fa2ca499ca6aa45a0e67f697e', '0', '0', '', '1', '0', '超级管理员', '超级管理员', '2016-02-14 17:48:31', '1');

-- ----------------------------
-- Table structure for app_user
-- ----------------------------
DROP TABLE IF EXISTS `app_user`;
CREATE TABLE `app_user` (
  `user_id` varchar(36) NOT NULL COMMENT '用户ID',
  `user_name` varchar(255) NOT NULL COMMENT '用户名',
  `user_nickname` varchar(255) NOT NULL DEFAULT '' COMMENT '昵称',
  `user_fullname` varchar(255) NOT NULL DEFAULT '' COMMENT '姓名',
  `password` varchar(64) NOT NULL COMMENT '访问密码',
  `mobile` varchar(20) NOT NULL DEFAULT '' COMMENT '手机',
  `email` varchar(255) NOT NULL DEFAULT '' COMMENT '邮箱',
  `gender` enum('m','f') NOT NULL DEFAULT 'm' COMMENT '性别',
  `status` enum('active','inactive') NOT NULL DEFAULT 'active' COMMENT '状态:停用inactive/启用active',
  `birthday` date NOT NULL DEFAULT '0000-00-00' COMMENT '生日',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` enum('0','1') NOT NULL DEFAULT '0' COMMENT '标记删除',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员账户表';

-- ----------------------------
-- Records of app_user
-- ----------------------------
INSERT INTO `app_user` VALUES ('123123', 'admin', '管理员', '管理员', 'e10adc3949ba59abbe56e057f20f883e', '', '', 'm', 'active', '0000-00-00', '2016-02-17 16:35:56', '0');

-- ----------------------------
-- Table structure for app_user_role
-- ----------------------------
DROP TABLE IF EXISTS `app_user_role`;
CREATE TABLE `app_user_role` (
  `user_role_id` varchar(36) NOT NULL,
  `user_id` varchar(36) DEFAULT NULL,
  `role_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`user_role_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of app_user_role
-- ----------------------------

-- ----------------------------
-- Procedure structure for myproc
-- ----------------------------
DROP PROCEDURE IF EXISTS `myproc`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `myproc`()
BEGIN
DECLARE num INT;
DECLARE loopNum INT;
SET num = 1;
SET loopNum = 100;
WHILE num < loopNum DO
	INSERT INTO sys_role (id, full_path, `name`, description)
VALUES
	(
		num,
		CONCAT(num,'-'),
		CONCAT('name ', num),
		CONCAT('description ', num)
	);
SET num = num + 1;
END
WHILE;
END
;;
DELIMITER ;
