# 一个 springboot3 项目的后端模板

## 介绍

这是一个 springboot3 的项目模板，已经编写好了一下常用的包和类：
- team.project
  - base：
    - controller：
      - Response：统一的后端响应封装
      - QueryParam：自定义注解，以支持使用对象来接收查询参数
    - exceptionhandler：统一异常处理
    - model：
      - PageQueryReq：分页查询请求的封装
      - PageVO：分页查询响应的封装
    - service：
      - exception：自定义业务异常
      - status：自定义业务状态码
  - config：各配置
  - module：各业务模块
    - _template：“模块内部各包如何划分”的参考，不包含任何业务逻辑
    - auth：角色鉴权模块
    - util：业务相关工具类
      - filestorage：文件存储
      - email：邮件服务
  - util 业务无关的简单工具类

已经配置好了一些常用的依赖：
- 入参校验：jsr303
- 角色鉴权：Sa-Token
- 数据存储：mysql、caffeine、aliyunOSS
- 数据访问：mybatis-plus
- 切面：aop
- 接口文档：swagger3
- 工具：lombok、apache commons

> 如果你不使用 aliyunOSS，你需要自行修改 filestorage 中的代码

## 快速开始

1. 修改 pom.xml 中的 artifactId 和 name，改为你的项目名字
2. 按照 resources/config_tmpl/readme.txt 修改各配置文件
3. 启动 team.project 包下的 Application
