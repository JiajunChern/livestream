本项目是仿直播平台后台部分代码。

本课程涉及的相关技术栈介绍：
- Dubbo 3.2
- SpringBoot 3.0.4
- MyBatis-Plus 3.5.3
- MySQL 8.0
- Redis 6.0
- RocketMQ 4.8.0
- Nacos 2.2.1
- ShardingJDBC 5.3.2
- Gateway 4.0.6
- Netty 3.7.0
- Docker 容器部署
- Docker-Compose 容器集群管理

架构方式：
Dubbo集群作为服务提供者，多个API服务作为服务调用方

- nacos配置文件均放在nacos-config目录下。

- sql目录下的文件是建立分表使用的存储过程。

部署流程：
- Docker 启动 nacos

模块启动顺序：
- ImProviderApplication -> ImCoreServerApplication -> ImRouterProviderApplication -> MsgProviderApplication -> ImClientApplication -> ImClientApplication2