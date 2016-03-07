/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.6.16 : Database - multimediaprototype
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`multimediaprototype` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `multimediaprototype`;

/*Table structure for table `mediamapping` */

DROP TABLE IF EXISTS `mediamapping`;

CREATE TABLE `mediamapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `media_id` bigint(20) DEFAULT NULL COMMENT '源视频id',
  `pic_id` bigint(20) DEFAULT NULL COMMENT '视频封面id',
  `description` varchar(255) DEFAULT 'default desc' COMMENT '视频描述',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '状态',
  `gmt_created` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '0:待审核，1:通过，2:不通过，3:转码中 4:转码失败 5:待转码',
  `title` varchar(100) DEFAULT 'default title' COMMENT '视频标题',
  `father` bigint(20) DEFAULT '0' COMMENT '父节点id，0表示是源文件',
  `gmt_modified` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=339 DEFAULT CHARSET=utf8;

/*Table structure for table `mts_job_history` */

DROP TABLE IF EXISTS `mts_job_history`;

CREATE TABLE `mts_job_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `job_id` varchar(32) DEFAULT NULL COMMENT 'mts作业id',
  `job_action` varchar(20) DEFAULT NULL COMMENT '操作类型,SubmitAnalysisJob,SubmitAnalysisJob',
  `input_url` varchar(100) DEFAULT NULL COMMENT '源文件',
  `output_url` varchar(100) DEFAULT NULL COMMENT '目标文件',
  `status` varchar(20) DEFAULT NULL COMMENT '任务状态，轮询后更新该状态',
  `gmt_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '任务创建时间',
  `gmt_modified` timestamp NULL DEFAULT NULL COMMENT '任务更新时间',
  `deleted` int(11) DEFAULT '0' COMMENT '逻辑删除',
  `transcode_template_id` varchar(32) DEFAULT NULL COMMENT '转码模板id',
  `watermark_count` int(11) DEFAULT NULL COMMENT '水印个数',
  `gmt_notified` timestamp NULL DEFAULT NULL COMMENT '收到通知时间',
  `status_notified` varchar(20) DEFAULT NULL COMMENT '收到通知状态',
  `pipeline_id` varchar(50) DEFAULT NULL COMMENT '管道id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `oss_file_id` bigint(20) DEFAULT NULL COMMENT 'OSS文件ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=679 DEFAULT CHARSET=utf8;

/*Table structure for table `mts_job_template` */

DROP TABLE IF EXISTS `mts_job_template`;

CREATE TABLE `mts_job_template` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(60) DEFAULT NULL,
  `desc` text,
  `outputs` longtext,
  `pipelineId` varchar(64) DEFAULT NULL,
  `adminId` int(20) unsigned DEFAULT NULL,
  `lastUpdate` int(11) unsigned DEFAULT NULL,
  `using` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `outputBucket` varchar(60) DEFAULT NULL,
  `outputLocation` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

/*Table structure for table `oss_file` */

DROP TABLE IF EXISTS `oss_file`;

CREATE TABLE `oss_file` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `userid` bigint(10) NOT NULL COMMENT '用户id',
  `object_url` varchar(255) DEFAULT NULL COMMENT 'oss的url',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否删除',
  `gmt_created` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `object_name` varchar(255) DEFAULT NULL COMMENT '文件名',
  `gmt_modify` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `md5_value` varchar(10) DEFAULT NULL COMMENT 'md5值',
  `bucket_name` varchar(255) DEFAULT NULL COMMENT 'oss的bucket名',
  `location` varchar(20) DEFAULT NULL COMMENT 'oss bucket的location',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=648 DEFAULT CHARSET=utf8;

/*Table structure for table `site_user` */

DROP TABLE IF EXISTS `site_user`;

CREATE TABLE `site_user` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(60) NOT NULL DEFAULT '',
  `password` varchar(64) NOT NULL DEFAULT '',
  `authorities` varchar(64) NOT NULL DEFAULT '',
  `enabled` tinyint(1) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

insert into `site_user` (`id`, `username`, `password`, `authorities`, `enabled`) values('1','admin','$2a$10$OrpLq15eTfeLbPQQJF.K2e4p3VuymG3L6R//k2mxPrWfphk8/iVkq','ROLE_USER,ROLE_ADMIN','1');
insert into `site_user` (`id`, `username`, `password`, `authorities`, `enabled`) values('7','test','$2a$10$/Wg14ibHKr09jDGSLDnltOibRWkHH/p9o00WCXSV88vA4gQtaOjXi','ROLE_USER','1');
