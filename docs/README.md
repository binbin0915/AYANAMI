## 基于JavaSpringCloud实现的前后端分离商城
[![MyWebSite](https://img.shields.io/badge/我的站点-whoiszxl-blue.svg)](https://whoiszxl.github.io)
[![docs](https://img.shields.io/badge/docs-reference-green.svg)](http://zmall.whoiszxl.com)
[![teach](https://img.shields.io/badge/教程-BohemianRhapsody-orange.svg)](https://github.com/whoiszxl/AYANAMI)
[![email](https://img.shields.io/badge/email-whoiszxl@gmail.com-red.svg)](https://whoiszxl.github.io)


### 项目介绍
本项目是一套用于学习Java Web的项目，其是一套基于Java SpringCloud实现微服务商城，PC前台采用Vue实现，移动端则采用跨平台方案Flutter实现，小程序采用Taro实现多端，后台则基于Java SpringCloud Alibaba实现，通过Nacos作服务发现，Mybatis Plus作持久层。管理员后台则单独使用PHP技术实现，采用Laravel-admin框架构建。其中会引入一些非常规电商所用的技术，比如引入区块链商品竞拍，数字货币购买商品，谷歌验证器等功能，旨在用于学习与应用。

### 项目进度
▓▓░░░░░░░░░░░░░░░░░░ 5% Java接口部分完成

### 项目文档
文档地址: http://zmall.whoiszxl.com

### 项目架构
```
zmall
├── zmall-cms             -- 基础内容管理模块
├── zmall-common          -- 公共代码模块
├── zmall-gateway         -- 网关模块
├── zmall-message         -- 短信邮件发送模块
├── zmall-oauth           -- oauth登录模块
├── zmall-order           -- 订单模块
├── zmall-oss             -- oss文件上传模块
├── zmall-pay             -- 订单支付模块
├── zmall-product         -- 商品模块
├── zmall-search          -- 搜索模块
├── zmall-seata           -- 分布式事务模块
├── zmall-seckill         -- 秒杀模块
├── zmall-task            -- 定时任务模块
├── zmall-user            -- 用户模块
├── zmall-wallet          -- 区块链钱包
└── generator             -- 代码生成工具

docs                      -- 文档目录

zmall_flutter             -- flutter APP

zmall_taro                -- 小程序

zmall_pc                  -- PC前端

zmall_management          -- PHP管理后台
```


### 技术选型

#### 后端技术
1. 微服务开发的一站式解决方案: Spring Cloud Alibaba
2. ORM框架:Spring Data JPA
3. 文档生成工具:Swagger-UI
4. 内存高速缓存数据库:Redis
5. MQ队列:RocketMQ
6. 对象存储:OSS
7. 以太坊区块链:Ethereum
8. 身份认证令牌:JWT
9. 后台搭建框架:PHP Laravel-admin

#### 前端技术
1. 前端PC框架:Vue
2. 跨平台移动UI框架:Flutter
3. 小程序开发框架:Taro




#### 开发进度




### 项目部署

> Windows环境部署




> Linux环境部署




> Docker环境部署



#### 公众号
![公众号](https://oss.whoiszxl.com/qrcode_for_whoisc137_258.jpg)


## 许可证

[Apache License 2.0](https://github.com/whoiszxl/BohemianRhapsody/blob/master/LICENSE)

Copyright (c) 2018-2020 whoiszxl