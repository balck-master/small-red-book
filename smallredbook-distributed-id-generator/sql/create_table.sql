CREATE TABLE `leaf_alloc` (
  `biz_tag` varchar(128)  NOT NULL DEFAULT '' COMMENT "业务id",
  `max_id` bigint(20) NOT NULL DEFAULT '1' COMMENT "当前业务被分配的id号段最大值",
  `step` int(11) NOT NULL COMMENT "每次分配的号段长度",
  `description` varchar(256)  DEFAULT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`biz_tag`)
) ENGINE=InnoDB;

insert into leaf_alloc(biz_tag, max_id, step, description) values('leaf-segment-test', 1, 2000, 'Test leaf Segment Mode Get Id')

INSERT INTO `leaf`.`leaf_alloc` (`biz_tag`, `max_id`, `step`, `description`, `update_time`) VALUES ('leaf-segment-xiaohashu-id', 10100, 2000, '小哈书 ID', now());

INSERT INTO `leaf`.`leaf_alloc` (`biz_tag`, `max_id`, `step`, `description`, `update_time`) VALUES ('leaf-segment-user-id', 100, 2000, '用户 ID', now());
