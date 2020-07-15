/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.21-20-log : Database - ppdai_dockeryard
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`ppdai_dockeryard` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `ppdai_dockeryard`;

/*Table structure for table `environment` */

DROP TABLE IF EXISTS `environment`;

CREATE TABLE `environment` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'pk',
  `name` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `url` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `insert_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建者',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新者',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '逻辑删除 0-表示已删除',
  PRIMARY KEY (`id`),
  KEY `idx_inserttime` (`insert_time`),
  KEY `idx_updatetime` (`update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='基础框架 Owner: zhangxiao04; Manager: yangbo';

/*Data for the table `environment` */


/*Table structure for table `event_log` */

DROP TABLE IF EXISTS `event_log`;

CREATE TABLE `event_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'pk',
  `event_name` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL,
  `event_addition` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'N/A',
  `option_type` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'create' COMMENT '操作类型 create push delete drop',
  `operator` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作人',
  `operate_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '逻辑删除 0-表示已删除',
  PRIMARY KEY (`id`),
  KEY `idx_inserttime` (`insert_time`),
  KEY `idx_updatetime` (`update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='基础框架 Owner: zhangxiao04; Manager: yangbo';

/*Data for the table `event_log` */


/*Table structure for table `image` */

DROP TABLE IF EXISTS `image`;

CREATE TABLE `image` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `guid` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '镜像的ID',
  `repo_name` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '仓库名称',
  `app_name` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '应用名称',
  `tag` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'latest' COMMENT '镜像的标签',
  `repo_id` bigint(20) NOT NULL COMMENT '仓库Id',
  `org_id` bigint(20) NOT NULL COMMENT '组织Id',
  `org_name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '组织名称',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `insert_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建者',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新者',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '逻辑删除 0-表示已删除',
  PRIMARY KEY (`id`),
  KEY `idx_inserttime` (`insert_time`),
  KEY `idx_updatetime` (`update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='基础框架 Owner: zhangxiao04; Manager: yangbo';

/*Data for the table `image` */


/*Table structure for table `image_extend` */

DROP TABLE IF EXISTS `image_extend`;

CREATE TABLE `image_extend` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'pk',
  `image_id` bigint(20) NOT NULL COMMENT 'image表的主键',
  `ikey` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标记键',
  `ivalue` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标记值',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `insert_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建者',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新者',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '逻辑删除 0-表示已删除',
  PRIMARY KEY (`id`),
  KEY `idx_inserttime` (`insert_time`),
  KEY `idx_updatetime` (`update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='基础框架 Owner: zhangxiao04; Manager: yangbo';

/*Data for the table `image_extend` */


/*Table structure for table `job_cleanup_log` */

DROP TABLE IF EXISTS `job_cleanup_log`;

CREATE TABLE `job_cleanup_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `job_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `instance` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '实例',
  `image_id` bigint(12) DEFAULT NULL,
  `app_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `repo_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '仓库名',
  `tag` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标签',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态：PENDING(待清理) SUCCESS(清理成功)',
  `insert_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT 'job',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT 'job',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_active` bigint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `idx_inserttime` (`insert_time`),
  KEY `idx_updatetime` (`update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `job_cleanup_log` */


/*Table structure for table `organization` */

DROP TABLE IF EXISTS `organization`;

CREATE TABLE `organization` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'pk',
  `name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL,
  `shortcode` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称缩写',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `insert_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建者',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新者',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '逻辑删除 0-表示已删除',
  PRIMARY KEY (`id`),
  KEY `idx_inserttime` (`insert_time`),
  KEY `idx_updatetime` (`update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='基础框架 Owner: zhangxiao04; Manager: yangbo';

/*Data for the table `organization` */


/*Table structure for table `repository` */

DROP TABLE IF EXISTS `repository`;

CREATE TABLE `repository` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'pk',
  `name` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '仓库名称',
  `org_id` bigint(20) NOT NULL COMMENT '组织Id',
  `org_name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '组织名称',
  `description` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `star_count` int(10) DEFAULT '0' COMMENT '星数',
  `download_count` int(10) DEFAULT '0' COMMENT '下载次数',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `insert_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建者',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新者',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '逻辑删除 0-表示已删除',
  PRIMARY KEY (`id`),
  KEY `idx_inserttime` (`insert_time`),
  KEY `idx_updatetime` (`update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='基础框架 Owner: zhangxiao04; Manager: yangbo';

/*Data for the table `repository` */


/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'pk',
  `name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户角色',
  `org_id` bigint(20) DEFAULT NULL COMMENT '所属组织Id',
  `org_name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属组织名称',
  `real_name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '真实名称',
  `email` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `work_number` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '工号',
  `department` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `insert_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建者',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新者',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '逻辑删除 0-表示已删除',
  PRIMARY KEY (`id`),
  KEY `idx_inserttime` (`insert_time`),
  KEY `idx_updatetime` (`update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='基础框架 Owner: zhangxiao04; Manager: yangbo';

/*Data for the table `user` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
