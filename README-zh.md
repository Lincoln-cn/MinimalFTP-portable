# MinimalFTP Portable

一个基于Java的轻量级、可配置的FTP服务器实现，支持JSON配置文件和多用户认证。

[English Version](README.md)

## 特性

- 🚀 轻量级FTP服务器实现
- ⚙️ 支持JSON格式的配置文件
- 👥 多用户认证支持
- 🔐 用户权限控制（读写/只读）
- 📁 每个用户独立的根目录
- 📝 完整的日志记录功能
- 🛡️ 安全的文件访问限制
- 💾 单jar文件部署

## 快速开始

### 下载

从[Releases](https://github.com/Lincoln-cn/MinimalFTP-portable/releases)页面下载最新版本的jar文件。

### 运行

```
java -jar PortableFTPServer-1.0.jar
```

服务器默认会在2121端口启动。

### 默认账户

首次运行会自动生成默认配置和账户：

- 用户名: `admin`
- 密码: 自动生成的高强度密码（会在控制台显示）



## 配置

### 命令行参数

```bash
# 指定配置文件路径
java -Dftp.config=/path/to/config.json -jar MinimalFTP-portable.jar

# 指定日志文件路径
java -Dftp.log=/path/to/server.log -jar MinimalFTP-portable.jar

# 同时指定配置文件和日志文件
java -Dftp.config=/path/to/config.json -Dftp.log=/path/to/server.log -jar MinimalFTP-portable.jar
```

### 配置文件格式

默认配置文件 `ftp-config.json` 格式如下：

```json
{
  "port": 2121,
  "users": [
    {
      "username": "dev",
      "password": "dev123",
      "root": "./projects",
      "readonly": false
    },
    {
      "username": "viewer",
      "password": "view456",
      "root": "./reports",
      "readonly": true
    }
  ]
}
```
> ⚠️ 注意：请在配置文件中使用的密码采用高复杂度随机生成的密码，避免被攻击者利用弱密码进行攻击。

#### 字段说明

- `port`: FTP服务器监听端口
- `users`: 用户列表
  - `username`: 用户名
  - `password`: 密码（明文存储，请注意安全）
  - `root`: 用户根目录路径
  - `readonly`: 是否为只读用户（true/false）

## 构建

### 环境要求

- Java 8 或更高版本
- Maven 3.6 或更高版本

### 编译

```bash
mvn clean package
```

编译后的jar文件位于 `target/` 目录下（`PortableFTPServer-1.0.jar`）。

## 使用场景

- 开发环境下的文件共享
- CI/CD流程中的文件传输
- 内网文件服务器
- 测试环境的FTP服务

## 安全建议

1. 不要在配置文件中使用弱密码
2. 生产环境中使用SSL/TLS加密连接
3. 限制用户根目录权限
4. 定期更新和审查用户账户
5. 监控日志文件以检测异常活动

## 日志

服务器会将日志记录到文件中，默认为 `ftp-server.log`。可以通过 `-Dftp.log` 参数指定其他日志文件路径。

日志包含：
- 服务器启动和停止事件
- 用户认证尝试（成功/失败）
- 文件操作记录
- 错误和异常信息

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 贡献

欢迎提交Issue和Pull Request来改进这个项目！

1. Fork 本仓库
2. 创建您的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交您的更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启一个Pull Request

## 致谢

- [MinimalFTP](https://github.com/Guichaguri/MinimalFTP) - 底层FTP库实现