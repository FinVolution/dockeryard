# Dockeryard
## 一：简介

### 1.1概述

dockeryard是一个镜像仓库管理系统，分为两部分：admin作为镜像的管理，proxy是http拦截器（可对镜像上传下载过程做安全审计）

### 1.2特性

1.管理员：获取环境列表并进行编辑、查看组织列表、查看用户列表

2.镜像管理：仓库列表、镜像列表（查看对应服务站点上传的镜像）

3.数据看板：查看镜像的操作日志

4.拦截http请求，获取镜像上传过程中的拉取和上传，并进行数据库层面的审计

5.根据不同环境配置的策略，进行镜像的自动清理

架构图：
![avatar](jiagou.jpeg)

## 环境准备

jdk 1.8、mysql5.7、node 12.16

## 二：部署
### 1 core
该目录为一些基础的依赖，需要提前在本地安装
进入该目录，执行命令
```xml
mvn install
```
### 2 front
dockeryard的前端项目，前端采用的vue实现的，需要本地安装npm，想关安装请参考官方文档
```xml
npm install
npm run dev
```
注意：为了更好的完成前后端部署，可以将前端文件打包到admin的resources下的static目录，可以在front目录直接执行命令
```xml
npm run build
```
该命令可以执行将前端部署文件打包进admin的文件夹
### 3 admin
该项目为镜像管理系统，提供了镜像的查看，清理等功能

编译和运行依赖的组件：JDK 8 + Maven 3.3.9 + MySQL5.7

分别把目录sql中的ppdai_dockeryard.sql导入到MySQL中
修改src/main/resources/application.properties中的数据库配置（spring.datasource.url, spring.datasource.username, spring.datasource.password）为正确值
执行如下命令打包
```xml
mvn clean package -DskipTests=true
```
运行命令启动服务
```xml
java -jar admin-0.0.1-SNAPSHOT.jar
```

### 4 proxy
该项目，需要和docker registry一起使用，拦截push 和pull相关的请求，并对请求进行请求审计，对用户进行权限校验，镜像审计等功能
执行打包命令：
```xml
mvn clean package -DskipTests=true
```
运行命令启动服务
```xml
java -jar proxy-0.0.1-SNAPSHOT.jar
```

### 5 docker registry安装部署

#### 1.安装docker

省略

#### 2.拉取镜像

```shell
docker pull registry
```

#### 3.运行(并指定5000端口)

```shell
mkdir /Users/chenlang/docker/registry
docker run -d -p 5000:5000 -v /Users/chenlang/docker/registry:/var/lib/registry registry
```

#### 4.更改配置

因为后续我们需要对上传到仓库的进行审计和其它操作，需要将请求经过proxy服务转发，所以需要修改配置daemon.json，作者使用的mac，可以直接修改vim ~/.docker/daemon.json 

```shell
vim ~/.docker/daemon.json 
```

内容：

```json
{
  "insecure-registries" : [
    "localhost:5050"
  ],
  "debug" : true,
  "experimental" : false,
  "registry-mirrors" : [
    "http://${ip}:5050"
  ]
}
```

```修改完后重启docker，并重启docker registry```



#### 5.打包镜像并推送

##### 一：运行proxy并设置好相关配置

```properties
# 拦截的端口好
proxyPort=5050

#docker registry
dockerRegistryHost=http://${ip}
dockerRegistryPort=5000

```

##### 二：打镜像并推送

```shell
docker pull hello-world
docker tag hello-world:latest localhost:5050/hello-world:latest
docker push localhost:5050/hello-world:latest
```






