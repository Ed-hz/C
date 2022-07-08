/*
 Navicat Premium Data Transfer

 Source Server         : 腾讯云
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : 81.70.102.35:3306
 Source Schema         : ca

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 14/12/2021 10:45:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_ca
-- ----------------------------
DROP TABLE IF EXISTS `t_ca`;
CREATE TABLE `t_ca`  (
  `cid` int NOT NULL AUTO_INCREMENT COMMENT '证书id',
  `uid` int NULL DEFAULT NULL COMMENT '证书持有者id',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '证书在证书库中密码',
  `created_user` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '日志-创建人',
  `created_time` datetime NULL DEFAULT NULL,
  `modified_user` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `modified_time` datetime NULL DEFAULT NULL,
  `type` int NULL DEFAULT NULL COMMENT '用户类型：1为个人版，2为商业版（授权商家和银行）',
  `description` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '证书说明描述',
  `start_time` datetime NULL DEFAULT NULL COMMENT '证书生效时间',
  `valid_time` int NULL DEFAULT NULL COMMENT '证书有效期，单位：天',
  `state` int NULL DEFAULT NULL COMMENT '证书状态：0-已申请，等待审核，1-过审核，已生成证书 2-驳回 3-召回',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '拥有者用户名',
  `alias` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '证书别名（用户角度的ID）',
  `c` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '国家',
  `cn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '拥有者名称',
  `l` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '市',
  `o` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组织',
  `ou` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组织单位',
  `st` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省',
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`cid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_ca
-- ----------------------------
INSERT INTO `t_ca` VALUES (20, 31, 'xINh4mdzKvikA5In//OHxTCvH0AvAXBDPAzMUTtU98yVbg0/Z5pIlHUsnPU/eLSf', 'user1234', '2021-11-27 13:08:19', 'user1234', '2021-12-14 08:03:24', 1, 'K7iku9gI4zhLUgTzvyoxCQ==', '2021-11-30 00:00:00', 9999, 3, 'XUJXmOb/v65lH4g//AZndQ==', 'SDpC6OG75nJbTPXa6Vg4sdTa3UT24hKicY6UJ5J++lQgFZMilT8OmSTRKWVRa6bh', '中国', '王斌', '哈尔滨', '哈工大', '信息安全', '黑龙江', '2016144844');
INSERT INTO `t_ca` VALUES (21, 36, 'ymIgi6O7CF4TbKBzMmxextlLDMfc8G8MVH8fNDGrON6U2rSbPMOZErO3MnfO9RNL', 'aaaaaaaa', '2021-11-27 15:34:07', 'root', '2021-11-27 20:16:52', 1, 'DB9vz4xXp+hNqrud/pYz1Q==', '2021-11-30 00:00:00', 666, 3, 'yBpLS8UkSHH0GkJ4z3qnCg==', 'fc8EJ5H3WX+QP/OCaJXmsNhicNKJRMzmDNJy1BFa3TnH/1/yIhwhUTK6BFpVfbdU', 'aaa', 'aaa', 'aaa', 'aaa', 'aa', 'aaa', '621077056');
INSERT INTO `t_ca` VALUES (22, 31, 'YENJGJW+XRpTtEKujpAf47i3vptGfxuXbYICJl0AxjP+Tlc+aOCHnBuj5MxB/Tcm', 'user1234', '2021-11-27 15:34:59', 'su_root', '2021-12-14 08:16:39', 2, 'GQlLuT+Szyqe4owLJ7IiJQ==', '2021-11-30 00:00:00', 6665, 3, 'XUJXmOb/v65lH4g//AZndQ==', 'Ze+q9n4fTSC+Zd0HVbasMxUGN24LkSpON7/ZDLehhx4fBVzc3DUW8tLZ+N0LiRU4', 'ss', 'ss', 'ss', 'ss', 'ss', 'ss', '1931812522');
INSERT INTO `t_ca` VALUES (23, 31, NULL, 'user1234', '2021-11-27 16:58:04', 'user1234', '2021-12-09 10:42:39', 2, 'MaD/D7RxblJoQtXWJp2vbA==', '2021-11-30 00:00:00', 123, 2, 'XUJXmOb/v65lH4g//AZndQ==', NULL, 'z', 'z', 'z', 'z', 'z', 'z', NULL);
INSERT INTO `t_ca` VALUES (24, 36, NULL, 'aaaaaaaa', '2021-11-27 16:58:47', 'aaaaaaaa', '2021-11-27 18:04:01', 1, 'l1SzsFT7j7mdB0nh26dCAA==', '2021-11-30 00:00:00', 123, 2, 'yBpLS8UkSHH0GkJ4z3qnCg==', NULL, 'w', 'w', 'w', 'w', 'w', 'w', NULL);
INSERT INTO `t_ca` VALUES (25, 31, 'xlt/rrUIZ1+Km/RA47KF1IWXoNKuHx15DidLAoYGZ2vx8JrhUsTB5LbOhPcGGiXV', 'user1234', '2021-11-27 20:21:22', 'user1234', '2021-12-14 00:21:07', 2, '3E6FaRvcINq0BQork8pk1UupRwW6Fyk7vA7UHvbjwh0=', '2021-11-27 00:00:00', 9999, 3, 'XUJXmOb/v65lH4g//AZndQ==', 'hqK4fO8ItsXaNNwfV6hKB1ZIrucfg6XUpBw8Vrt2zji5OsJyp9KjW0a+JWxHWPj5', '中国', '胡宇康', '大连市', '大连理工大学', '信息安全', '辽宁省', '1976142774');
INSERT INTO `t_ca` VALUES (26, 31, NULL, 'user1234', '2021-11-27 22:20:42', 'user1234', '2021-12-08 15:36:19', 2, 'T4DTGQQhrXimBDs7CXVW2w==', '2021-11-23 00:00:00', 100, 2, 'XUJXmOb/v65lH4g//AZndQ==', NULL, '祖国', 'binw', '合并', '哈工大', '信息安全', '红领巾', NULL);
INSERT INTO `t_ca` VALUES (27, 37, NULL, 'xufannannan', '2021-11-27 22:49:51', 'xufannannan', '2021-11-27 22:49:51', 1, 'wFYeLU99p2x0R6JB9KEGQg==', '2021-11-10 00:00:00', 12, 2, 'uTPP+5XI/cKK6Qv3MtBLiw==', NULL, 'asd', 'asd', 'asd', 'ads', 'asd', 'asd', NULL);
INSERT INTO `t_ca` VALUES (28, 38, '5W2UdXpq1MmJwEQf+qW8RH9s+v6XK71U4BUOrZbPKeb4wFGwzEFZqjrscsB5uGv/', 'ZhanXianyou', '2021-11-28 14:17:15', 'su_root', '2021-12-14 08:17:44', 2, 'L8+9eeJxpDicbUW9JBDolA==', '2021-11-28 00:00:00', 9999, 1, 'z46fuFc46BmbS4msy+zS6Q==', 'PTRi2ssnUkhFz03x8UFBkn4G55AINHetXRjr2iQFS81uKpuZQdy19UVtJbhdyxn2', '中国', 'bank', '哈尔滨', '哈尔滨工业大学', '计算学部', '黑龙江', '1591614936');
INSERT INTO `t_ca` VALUES (29, 39, 'VxUQXJz3/FpdEUMNYMRGGthPkEYwSZgClrWxeV7GAD7y9k6Jpk0PVWa3fskYa1Ad', '11111', '2021-11-28 14:51:24', 'su_root', '2021-12-14 08:17:46', 2, 'ahfzg8OymRuKlt6afp3JSQ==', '2021-11-27 00:00:00', 11, 3, '9o8mnt4+0M7juNM2/4iZKw==', 'ZmdCg6mmy6mWRyoVnVpbVGT6RNGYMZkpX6j2EHVeuQ9VyrwAMMCcm3IO4DRkOgCr', '1111', '1111', '111', '111', '111', '111', '1061635982');
INSERT INTO `t_ca` VALUES (30, 31, NULL, 'user1234', '2021-12-10 22:46:39', 'user1234', '2021-12-10 22:46:39', 1, 'Qmf9POdoqdvdyuQf0JsLYQ==', '2021-12-28 00:00:00', 1234, 2, 'XUJXmOb/v65lH4g//AZndQ==', NULL, 'x', 'x', 'x', 'x', 'x', 'x', NULL);
INSERT INTO `t_ca` VALUES (31, 2, '+hxPx+ed8GuUQRG0Kp4J+iQPZxJaWIJg0o/XBxxS82R/du9gkDi9pdGdO+Vmn8kV', 'test1', '2021-12-11 10:07:49', 'su_root', '2021-12-14 08:17:28', 2, 'EKGliWMmYaYWBj0gaYVA1A==', '2021-12-29 00:00:00', 523, 1, 'EESpSr5OPHmyqgdkxTFTkg==', '81uWplChzQjJByVYs1lsyP7n5HJQqa7uQkG0/IzVHLnCQUCuVBqGvqirT08xlKEz', '中国', 'test1', '哈尔滨', '1230506226', '156260', '黑龙江', '1824769092');
INSERT INTO `t_ca` VALUES (32, 2, NULL, 'test1', '2021-12-11 10:29:35', 'test1', '2021-12-11 10:29:35', 1, 'svPC3QN3wr8JeuYF3VGyjQ==', '2021-12-29 00:00:00', 123, 2, 'EESpSr5OPHmyqgdkxTFTkg==', NULL, 'z', 'z', 'z', 'z', 'z', 'z', NULL);
INSERT INTO `t_ca` VALUES (33, 40, '1VMiqZEjvBvo1jEO70ynRSEm1SF5A5jeJTCnLIAve7of/oiG3/2B/Fu4wiDVevja', 'cacenter', '2021-12-11 18:34:06', 'su_root', '2021-12-14 10:44:11', 1, '+CErWLLO/dJIuhzCab9D2jx/chrP/yQU+S5YF6M8l4/C97fOPeYLjifi7HjTuvJ2NmGAetKTvGnCXNGOX0AO6Q==', '2021-12-12 00:00:00', 365, 1, 'GYPep5cymIcmx/rx0WB/NQ==', '5a6WQ1fJZfsX0hUUDBhm96QMafYUTZ0Lktu6jPdiavnNS9B+wljGUifqg57mswZV', '中国', '证书中心个人版测试证书', '哈尔滨', '哈工大', '信息安全', '黑龙江', '1963492380');
INSERT INTO `t_ca` VALUES (34, 40, 'RO4ZCdo5sLMjevJL9SrHNK4gmkapGI98yTR9roIFngIQO90A4rDefOIT5VJgh9KO', 'cacenter', '2021-12-11 18:37:05', 'root', '2021-12-11 19:03:39', 2, 'U3jr1MiNoZZ/VQgqQ89FE6dhZM7Uj5rbw2jkyTl0rRfUFH3OWV5Qsc9hg+pFN4ZYKc3NtXWp9Ax8SILJJt+8kA==', '2021-12-12 00:00:00', 365, 1, 'GYPep5cymIcmx/rx0WB/NQ==', '5C7wqZCnAYO4h9u+yx8aX+/746HQcdRBm3g0PxWhznumUt3+tVBnFjZZPNsEy2li', '中国', '证书中心商业版测试证书', '哈尔滨', '哈工大', '信息安全', '黑龙江', '1902646007');
INSERT INTO `t_ca` VALUES (35, 1, 'qYD+xCNby0rg/xkEnbhi9RT6wCA76DitVGDjq4g14p5tQGUPJN4iwyxNxsFWX3ox', 'tim', '2021-12-12 15:26:55', 'root', '2021-12-12 15:27:05', 1, 'JhnoSbh8iGyax7afpHlC/w==', '2021-12-27 00:00:00', 11, 1, 'zSIkk7EkBn4nUPVP86dApw==', 'c444jryEh5QX0nhLkhz6yZJDkSecMYojtJBe3j/f6a/tDX+S1m00s00E+sKZsZts', '123', '233', '11', '11', '11', '123', '607643574');
INSERT INTO `t_ca` VALUES (36, 31, 'aRfq9exCdRHtU6h2zhPtVskhe3JJwYlrtOaAO4ph4v/OquNkH6ft+DdoQrgf3+Z/', 'user1234', '2021-12-14 00:24:06', 'root', '2021-12-14 00:24:16', 1, '8bK61i2Lh0YCDcFNcs7Zkg==', '2021-12-28 00:00:00', 123, 1, 'XUJXmOb/v65lH4g//AZndQ==', 'b+FEmfAVkznxP3q34756v8mvyNG5OLTybwo8305VsVy5aQGSPni4jGFj9qE6tXJY', 'cc', 'cc', 'cc', 'cc', 'cc', 'cc', '1779900705');
INSERT INTO `t_ca` VALUES (37, 31, '0i86nMVTrmrfjMQlkQhdLdK5BHd9ix/sFfQGrbANYgATlw5JRWWjm7ew3L89F8br', 'user1234', '2021-12-14 08:11:00', 'su_root', '2021-12-14 08:17:51', 1, 'svPC3QN3wr8JeuYF3VGyjQ==', '2021-12-28 00:00:00', 123, 3, 'XUJXmOb/v65lH4g//AZndQ==', '5KM7ja0usZtBfygPkiMA4nFG/aREy9SHT+W/xdk7VLSivjTOHc4bnrPJFZAr0A2j', 'vv', 'vv', 'vv', 'vv', 'vv', 'vv', '2089661815');

-- ----------------------------
-- Table structure for t_real
-- ----------------------------
DROP TABLE IF EXISTS `t_real`;
CREATE TABLE `t_real`  (
  `rid` int NOT NULL AUTO_INCREMENT COMMENT '事务编号',
  `uid` int NOT NULL COMMENT '用户uid',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '真实姓名',
  `id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证号',
  `state` int NOT NULL COMMENT '事务状态 0-待提交 1-待审核 2- 审核通过',
  PRIMARY KEY (`rid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_real
-- ----------------------------
INSERT INTO `t_real` VALUES (1, 1, '2', 'NqO1fIYF9I1kLmUI4ZzeNA==', 'snmn1fIpYDHn0LLRJrxX5A==', 1);
INSERT INTO `t_real` VALUES (2, 41, 'fffff', 'WtRiXZBNKq0x2fkIahfZiA==', 'iaCYdu42pzfesKeD+TPFP/vdN+OfKxV8AY4YBUd0v64=', 2);
INSERT INTO `t_real` VALUES (3, 42, 'klkkk', 'sg4ZiLHlAf13i8IOE6ph/A==', 'wWupILJ2wQHNQwu+Gx1HYDepCUPrQwIsm9d/wuTelY4=', 2);

-- ----------------------------
-- Table structure for t_su
-- ----------------------------
DROP TABLE IF EXISTS `t_su`;
CREATE TABLE `t_su`  (
  `uid` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `salt` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盐值',
  `phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `gender` int NULL DEFAULT NULL COMMENT '性别:0-女，1-男',
  `avatar` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `is_delete` int NULL DEFAULT NULL COMMENT '是否删除：0-未删除，1-已删除',
  `created_user` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志-创建人',
  `created_time` datetime NULL DEFAULT NULL COMMENT '日志-创建时间',
  `modified_user` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志-最后修改执行人',
  `modified_time` datetime NULL DEFAULT NULL COMMENT '日志-最后修改时间',
  PRIMARY KEY (`uid`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_su
-- ----------------------------
INSERT INTO `t_su` VALUES (13, 'root', '68C80B814EA6C03348DEC39491B62F6B', '8C9DC4D5-1AD9-4251-816E-C3A27524A109', '15683141454', '1907058805@qq.com', 1, 'http://localhost:8080/img/user/be2dc4e322674ae49006d93dad3a38283qweq.jpg', 0, 'root', '2021-11-24 14:38:30', 'root', '2021-11-24 14:38:30');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `uid` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `salt` char(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盐值',
  `phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `gender` int NULL DEFAULT NULL COMMENT '性别:0-女，1-男',
  `avatar` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `is_delete` int NULL DEFAULT NULL COMMENT '是否删除：0-未删除，1-已删除',
  `created_user` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志-创建人',
  `created_time` datetime NULL DEFAULT NULL COMMENT '日志-创建时间',
  `modified_user` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志-最后修改执行人',
  `modified_time` datetime NULL DEFAULT NULL COMMENT '日志-最后修改时间',
  `utype` int NULL DEFAULT NULL COMMENT '用户类型：0为游客，无法申请证书，完成实名认证后为1，1为个人版，2为商业版（授权商家和银行）',
  PRIMARY KEY (`uid`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, 'tim', '5047FDD6E19E4914D7D5842A8535348B', 'C20C82FB-83DD-4EB6-BBE3-D15738008AE9', 'VZBj/zbwMpeP0/SntMQU/g==', 'SmfJV8kOslE9B/nD+8lnBw==', 1, '', 0, 'tim', NULL, 'su_root', '2021-12-14 08:16:59', 2);
INSERT INTO `t_user` VALUES (2, 'test1', '7A0C3D96994B5683039BB0F6F9ED5DDE', '2E9DAEFA-3362-4305-954C-E5C159F1D80A', 'kuyPJHUxkUZ9XsFQ+lSSyA==', 'sW5vkNsVZEmsZYEZFcgu6Q==', 0, '', 0, 'test1', '2021-10-07 23:41:48', 'su_root', '2021-12-11 10:06:05', 2);
INSERT INTO `t_user` VALUES (3, 'test2', '4C677D1112D295555B41D14A730CFBF6', NULL, NULL, NULL, 0, '', 0, 'test2', '2021-10-08 00:05:39', 'root', '2021-12-08 12:20:20', 1);
INSERT INTO `t_user` VALUES (4, 'test3', '19FE4D6652FC8D1FCEE95C72CED1659F', '070579EC-1F03-443A-819C-10388A00EF10', NULL, NULL, 0, '', 0, 'test3', '2021-10-08 00:07:42', 'root', '2021-12-08 12:20:44', 2);
INSERT INTO `t_user` VALUES (5, 'tim1', '123', NULL, NULL, NULL, 1, NULL, 0, 'tim1', NULL, 'root', '2021-12-08 12:21:21', 2);
INSERT INTO `t_user` VALUES (6, 'test4', 'C8FCF1DB99D30BD7B4DD03B2060FD66D', '43113495-6B60-4A4D-8A05-8766842439BB', NULL, NULL, 1, NULL, 0, 'test4', '2021-10-08 08:52:05', 'test4', '2021-10-08 08:52:05', NULL);
INSERT INTO `t_user` VALUES (7, 'test5', '784BE1C6D882AD44AA0C5CC5D641CBA9', '6A3C058E-6EB8-489D-88E6-3A895FC1155B', NULL, NULL, 0, NULL, 0, 'test5', '2021-10-08 17:59:10', 'test5', '2021-10-08 17:59:10', NULL);
INSERT INTO `t_user` VALUES (8, 'test6', '55C3C1C6719CCFF946DC099EDBA9F73A', '92BF83C4-932C-4F2F-A7C9-93FFBFCB7419', NULL, NULL, 0, NULL, 0, 'test6', '2021-10-08 18:00:38', 'test6', '2021-10-08 18:00:38', NULL);
INSERT INTO `t_user` VALUES (9, 'test7', '41183365A8FD1B657109A1FC889A7633', '3A9BCA3F-2BBA-4CB4-A1FF-75A18D16776B', NULL, NULL, NULL, NULL, 0, 'test7', '2021-10-08 18:01:20', 'test7', '2021-10-08 18:01:20', NULL);
INSERT INTO `t_user` VALUES (10, 'test8', '40E89C16905D6A657C787CA58476856A', '322708F2-15A3-458E-9413-00EE43D5B6F6', NULL, NULL, NULL, NULL, 0, 'test8', '2021-10-08 18:12:32', 'test8', '2021-10-08 18:12:32', NULL);
INSERT INTO `t_user` VALUES (12, 'test9', '8D5B5320856D15F2433353977762DEAA', '639986EA-61F9-46B9-B562-75B7FE6EB1AF', NULL, NULL, NULL, NULL, 0, 'test9', '2021-10-15 09:23:02', 'test9', '2021-10-15 09:23:02', NULL);
INSERT INTO `t_user` VALUES (13, 'bin', 'F78763AAFCED6944DFFDD57545776FF8', '05BAFF25-B550-40D4-A8D9-5ED605AEB403', 'ioe4jDVgKnC3asiZIl8l6A==', 'PtfGNKRglAvSkBsEUIyHOG7ttK1MSv92XlEN0ILl3Pc=', NULL, NULL, 0, 'bin', '2021-10-23 00:33:49', 'bin', '2021-10-23 00:33:49', NULL);
INSERT INTO `t_user` VALUES (14, 'bin1', 'A8ACB4D6C448E08CB3E698B7B910893F', '4AE5B4F8-9834-43A8-97E8-B5ACFFCA6231', 'ioe4jDVgKnC3asiZIl8l6A==', 'PtfGNKRglAvSkBsEUIyHOG7ttK1MSv92XlEN0ILl3Pc=', NULL, NULL, 0, 'bin1', '2021-10-23 00:36:43', 'bin1', '2021-10-23 00:36:43', NULL);
INSERT INTO `t_user` VALUES (15, 'bin2', '4A76F95571AF76A74561B8A62DE25AC9', '8AFA2F68-C842-4108-A6BD-1CA603407C64', 'ioe4jDVgKnC3asiZIl8l6A==', 'PtfGNKRglAvSkBsEUIyHOG7ttK1MSv92XlEN0ILl3Pc=', 1, NULL, 0, 'bin2', '2021-10-23 00:37:10', 'bin2', '2021-10-23 00:37:10', NULL);
INSERT INTO `t_user` VALUES (18, '1', '17A6CC29F706A05429AEE6567C994BAB', '9F51B4F3-8358-43A7-8BCB-2D6CCF77074E', 'I90bL1b/Fv0PYHE5zrJVag==', 'I90bL1b/Fv0PYHE5zrJVag==', 1, NULL, 0, '1', '2021-10-23 16:18:45', '1', '2021-10-23 16:18:45', NULL);
INSERT INTO `t_user` VALUES (19, '12343', '935946480F9FE0B376A80297DA699719', '7CD715C1-E7DA-438B-8CDC-B0177F44529F', 'I90bL1b/Fv0PYHE5zrJVag==', 'I90bL1b/Fv0PYHE5zrJVag==', 1, NULL, 0, '12343', '2021-10-23 16:27:50', '12343', '2021-10-23 16:27:50', NULL);
INSERT INTO `t_user` VALUES (20, '111111', '16970D61ACA2BA3A523FBD49D5AB97C6', 'B0765D6D-2EE0-4AD8-A1B9-3DB7687B65FE', 'ioe4jDVgKnC3asiZIl8l6A==', 'PtfGNKRglAvSkBsEUIyHOG7ttK1MSv92XlEN0ILl3Pc=', 1, NULL, 0, '111111', '2021-10-23 20:15:13', '111111', '2021-10-23 20:15:13', NULL);
INSERT INTO `t_user` VALUES (22, '1111111', 'DA311C09111D9AA3E1DD012B6B51CA4C', '5B9BFC4C-CE8B-4450-BF9A-B1B896FA9F84', 'ioe4jDVgKnC3asiZIl8l6A==', 'PtfGNKRglAvSkBsEUIyHOG7ttK1MSv92XlEN0ILl3Pc=', 1, NULL, 0, '1111111', '2021-10-23 20:15:30', '1111111', '2021-10-23 20:15:30', NULL);
INSERT INTO `t_user` VALUES (23, 'xufannan', 'EDD59091977168DD60C61B5E98B1913A', '04986190-B32B-4F82-A83E-85691E3C67BC', 'ioe4jDVgKnC3asiZIl8l6A==', 'PtfGNKRglAvSkBsEUIyHOG7ttK1MSv92XlEN0ILl3Pc=', 1, NULL, 0, 'xufannan', '2021-10-23 20:16:31', 'xufannan', '2021-10-23 20:16:31', NULL);
INSERT INTO `t_user` VALUES (24, 'qwer1234', 'F5C1F031103C8E5A0FC3781474C15079', 'EAFA46FD-5795-404F-9077-CC7728E0F816', 'ahfzg8OymRuKlt6afp3JSQ==', 'ahfzg8OymRuKlt6afp3JSQ==', 1, NULL, 0, 'qwer1234', '2021-10-24 12:43:58', 'qwer1234', '2021-10-24 12:43:58', NULL);
INSERT INTO `t_user` VALUES (25, 'qwer1231', '13920477F6A515DD84135BB940F08B89', '8856AE45-7824-481B-A3B7-F94D3BB6EA53', 'ahfzg8OymRuKlt6afp3JSQ==', 'ahfzg8OymRuKlt6afp3JSQ==', 0, NULL, 0, 'qwer1231', '2021-10-24 12:44:42', 'qwer1231', '2021-10-24 12:44:42', NULL);
INSERT INTO `t_user` VALUES (26, '111222', 'BDD07A3C233A6AF08DEF9070CE166AA8', 'D552A2CB-D6D5-4F77-A90C-ECCF884F91CB', 'I90bL1b/Fv0PYHE5zrJVag==', 'I90bL1b/Fv0PYHE5zrJVag==', 1, NULL, 0, '111222', '2021-10-24 12:46:18', '111222', '2021-10-24 12:46:18', NULL);
INSERT INTO `t_user` VALUES (27, 'qqqqqq', '858E1964AB29E8C16AC5DCA90179148F', '1217790F-2BED-42F0-BC41-9E1CEDF7942F', '7CQ2ZW0+ZbwU3kYoTcb9fA==', '7CQ2ZW0+ZbwU3kYoTcb9fA==', 0, NULL, 0, 'qqqqqq', '2021-10-24 13:57:13', 'qqqqqq', '2021-10-24 13:57:13', NULL);
INSERT INTO `t_user` VALUES (28, 'QWERT', '76375B9C276CC77560554A06C50A976A', '185A78C1-1D9B-4DD0-859A-206489105298', 'ahfzg8OymRuKlt6afp3JSQ==', 'ahfzg8OymRuKlt6afp3JSQ==', 0, NULL, 0, 'QWERT', '2021-10-24 13:59:06', 'QWERT', '2021-10-24 13:59:06', NULL);
INSERT INTO `t_user` VALUES (29, '1234567', 'BB39707C72F44E9224105957DD89EC3A', '012B5E4C-3300-4E02-9D2F-55C808FD8B6F', 'JhnoSbh8iGyax7afpHlC/w==', 'JhnoSbh8iGyax7afpHlC/w==', 0, NULL, 0, '1234567', '2021-10-24 13:59:57', '1234567', '2021-10-24 13:59:57', NULL);
INSERT INTO `t_user` VALUES (30, '12345678', '4700DDF37FA082B0C81133E0275772B0', '5668F18B-5606-4E04-8F26-BE49902852D6', 'ahfzg8OymRuKlt6afp3JSQ==', 'ahfzg8OymRuKlt6afp3JSQ==', 1, NULL, 0, '12345678', '2021-10-24 15:00:16', '12345678', '2021-10-24 15:00:16', NULL);
INSERT INTO `t_user` VALUES (31, 'user1234', '6C2FD160CCD1B91FAD0AAFFE1D832922', 'B066F48A-78C3-4170-A722-DFCA156FF588', 'kLAKEPUUhpZhGNGjCbsybQ==', 'WiH9BDuP6W4PL1P6qbpzAwLQ8F1SpiH9yypfOzE+PZE=', 0, 'http://localhost:8080/img/user/59faf25be7a044088507db90c61e0b027_4c6a5d4c2f4a9.jpg', 0, 'user1234', '2021-10-30 00:17:28', 'su_root', '2021-12-14 00:23:28', 2);
INSERT INTO `t_user` VALUES (32, 'tim2333', '123', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_user` VALUES (34, 'qqqqqqq', 'C03A2FB72C813A4C5161CF0B7F2FD97A', 'E9AE8BD6-BC72-4231-84C1-29D49928B994', 'd86tDz/N3fDBKxOM64iF4A==', 'qHoZl2jJjdyyhl/qpWJ+Vw==', 1, '', 0, 'qqqqqqq', '2021-11-27 15:31:38', 'qqqqqqq', '2021-11-27 15:31:38', 1);
INSERT INTO `t_user` VALUES (35, 'qqqqqqqq', '89DA79736BE584B806B1F7B9582BFA7B', '4E577A2B-9295-47E9-A596-622BA6BCB6DC', 'd86tDz/N3fDBKxOM64iF4A==', 'qHoZl2jJjdyyhl/qpWJ+Vw==', 1, '', 0, 'qqqqqqqq', '2021-11-27 15:32:44', 'qqqqqqqq', '2021-11-27 15:32:44', 1);
INSERT INTO `t_user` VALUES (36, 'aaaaaaaa', '1CE7AEECFA622CF12C7E888E4F4D1424', 'E7515887-7825-416B-94FF-8FFCC7F5468F', 'ZgXatgaGua5YmTqEFp7vjQ==', 'PYPV1M/KdKv3wlhwBK0IiA==', 1, 'http://localhost:8080/img/user/8bb9a9466c184693ae456fc6e0ecba637_4c13878393251.jpg', 0, 'aaaaaaaa', '2021-11-27 15:33:36', 'root', '2021-12-08 22:19:02', 1);
INSERT INTO `t_user` VALUES (37, 'xufannannan', '26D89DB134EC18502CB542215D3BE60E', '2C092747-A205-453C-8DB9-1672F31F0ACC', 'jiBTXo9ZNyLfjVBRWKt7OA==', 'm6s7S5OG6JNBy6+xFxaSWQ1dOD2NXriN7Fn6XIL6PP4=', 1, '', 0, 'xufannannan', '2021-11-27 22:48:49', 'root', '2021-12-08 12:21:41', 1);
INSERT INTO `t_user` VALUES (38, 'ZhanXianyou', '37CAA4EC5AF990F3CCA1FE64A4271AE5', '4D9C3BF4-B45C-4DA2-BAF2-8AB6B11F5710', 'Q69mI1cLM9u28BcR+j8XRw==', 'rLY1vcZxK+q6iw2Il+OHEW4IyAhVeskXqAGQIn25tb0=', 1, '', 0, 'ZhanXianyou', '2021-11-28 14:14:12', 'root', '2021-11-28 14:15:30', 2);
INSERT INTO `t_user` VALUES (39, '11111', '4BD4E6DC9220571ADE10E19277A50F66', 'D9685DC7-6961-43B9-8380-81FD2C85A883', 'ahfzg8OymRuKlt6afp3JSQ==', 'ahfzg8OymRuKlt6afp3JSQ==', 1, '', 0, '11111', '2021-11-28 14:46:55', 'root', '2021-11-28 14:50:03', 2);
INSERT INTO `t_user` VALUES (40, 'cacenter', '8119811F96A9CA0602C4C3D4AF7CE3C5', '8A358C3A-DAB2-4B46-A25D-5BEB179853B6', 'ioe4jDVgKnC3asiZIl8l6A==', 'PtfGNKRglAvSkBsEUIyHOG7ttK1MSv92XlEN0ILl3Pc=', 0, 'http://localhost:8080/img/user/34eeffb4e6a34ecabafdad4db63a35123qweq.jpg', 0, 'cacenter', '2021-12-11 18:27:20', 'cacenter', '2021-12-11 18:53:49', 2);
INSERT INTO `t_user` VALUES (41, 'fffff', '9040D5C9C957B26F6788C7524845AB81', '492021F4-2676-42B9-B82E-8B0021AF374B', '//xEQ6UA0fQ5PBjXfViJ5w==', 'kLAKEPUUhpZhGNGjCbsybQ==', 1, '', 0, 'fffff', '2021-12-14 00:25:23', 'fffff', '2021-12-14 00:26:04', 1);
INSERT INTO `t_user` VALUES (42, 'klkkk', '2D9E2200AB6F1CFDC053E826BCEC8586', 'F7A686F8-0BDB-4889-BD86-AD2A3FD6A2CC', 'X9cOUG1nCBuuvUDd00+aow==', 'ShM5qAqQ7U4x2VpSbxqbnw==', 1, '', 0, 'klkkk', '2021-12-14 10:20:06', 'klkkk', '2021-12-14 10:20:06', 1);

SET FOREIGN_KEY_CHECKS = 1;
