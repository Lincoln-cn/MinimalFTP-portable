# MinimalFTP Portable

A lightweight, configurable FTP server implementation based on Java, supporting JSON configuration files and multi-user authentication.

## Features

- 🚀 Lightweight FTP server implementation
- ⚙️ Support for JSON format configuration files
- 👥 Multi-user authentication support
- 🔐 User permission control (read-write/readonly)
- 📁 Independent root directory for each user
- 📝 Complete logging functionality
- 🛡️ Secure file access restrictions
- 💾 Single jar file deployment

## Quick Start

### Download

Download the latest version of the jar file from the [Releases](https://github.com/your-username/MinimalFTP-portable/releases) page.

### Run

```bash
java -jar PortableFTPServer-1.0.jar
```

The server will start on port 2121 by default.

### Default Account

On first run, a default configuration and account will be automatically generated:

- Username: `admin`
- Password: Auto-generated strong password (will be displayed in the console)

> ⚠️ Note: Please change the default password immediately after first login!

## Configuration

### Command Line Parameters

```bash
# Specify configuration file path
java -Dftp.config=/path/to/config.json -jar PortableFTPServer-1.0.jar

# Specify log file path
java -Dftp.log=/path/to/server.log -jar PortableFTPServer-1.0.jar

# Specify both configuration file and log file
java -Dftp.config=/path/to/config.json -Dftp.log=/path/to/server.log -jar PortableFTPServer-1.0.jar
```

### Configuration File Format

The default configuration file `ftp-config.json` has the following format:

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

#### Field Descriptions

- `port`: FTP server listening port
- `users`: User list
  - `username`: Username
  - `password`: Password (stored in plain text, please note security)
  - `root`: User root directory path
  - `readonly`: Whether the user is read-only (true/false)

## Build

### Requirements

- Java 8 or higher
- Maven 3.6 or higher

### Compile

```bash
mvn clean package
```

The compiled jar file is located in the `target/` directory (`PortableFTPServer-1.0.jar`).

## Use Cases

- File sharing in development environments
- File transfer in CI/CD pipelines
- Intranet file servers
- FTP service for testing environments

## Security Recommendations

1. Do not use weak passwords in configuration files
2. Use SSL/TLS encrypted connections in production environments
3. Restrict user root directory permissions
4. Regularly update and review user accounts
5. Monitor log files to detect abnormal activities

## Logging

The server logs to a file, defaulting to `ftp-server.log`. You can specify another log file path with the `-Dftp.log` parameter.

Logs include:
- Server start and stop events
- User authentication attempts (success/failure)
- File operation records
- Error and exception information

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

Issues and Pull Requests are welcome to improve this project!

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Acknowledgements

- [MinimalFTP](https://github.com/Guichaguri/MinimalFTP) - Underlying FTP library implementation