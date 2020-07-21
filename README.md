# 一、简介

## 1.1概述

dockeryard是一个镜像仓库管理系统，分为两部分：admin作为镜像的管理，proxy是http拦截器（可对镜像上传下载过程做安全审计）

## 1.2特性

1.管理员：获取环境列表并进行编辑、查看组织列表、查看用户列表

2.镜像管理：仓库列表、镜像列表（查看对应服务站点上传的镜像）

3.数据看板：查看镜像的操作日志

4.拦截http请求，获取镜像上传过程中的拉取和上传，并进行数据库层面的审计

5.根据不同环境配置的策略，进行镜像的自动清理

## 1.3环境

jdk 1.8、mysql5.7

## 1.4项目部署

#### 一：管理系统

服务依赖于**pauth**服务，做登录验证，请确保服务**pauth**是否启动

1.后端服务（admin）

2.前端服务（front）

```text
1.先进入front目录
2.执行npm install(下载较慢可以设置淘宝镜像源npm config set registry https://registry.npm.taobao.org)
3.npm run dev (注意webpack.config.js中的devServer端口为服务访问端口，后端地址为target)
```

3.http请求拦截器（proxy）







