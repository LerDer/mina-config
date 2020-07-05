CREATE TABLE `message`
(
    `id`             bigint(20) unsigned                                          NOT NULL AUTO_INCREMENT COMMENT '消息体id',
    `project_name`   varchar(200)                                                          DEFAULT NULL COMMENT '项目名称',
    `env_value`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'local' COMMENT '环境，dev日常，gray灰度，online线上，local本地',
    `property_value` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         DEFAULT NULL COMMENT 'preperties 中的value',
    `config_value`   varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         DEFAULT NULL COMMENT '配置中心配置的需要注入的值',
    `remote_address` varchar(100)                                                          DEFAULT NULL COMMENT '客户端 session key',
    `creator`        bigint(20)                                                            DEFAULT NULL COMMENT '创建人',
    `gmt_create`     datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modifier`       bigint(20)                                                            DEFAULT NULL COMMENT '修改人',
    `gmt_modify`     datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `config_version` int(11)                                                      NOT NULL DEFAULT '1' COMMENT '乐观锁，版本',
    `is_deleted`     int(1)                                                       NOT NULL DEFAULT '0' COMMENT '逻辑删除，0未删除，1已删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 16
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='消息体';