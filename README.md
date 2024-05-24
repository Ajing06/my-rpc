
原项目地址：[https://github.com/Veal98/RPC-FromScratch](https://github.com/Veal98/RPC-FromScratch)

## 功能列表

- [x] 使用 Spring 提供依赖注入与参数配置
- [x] 集成 Spring 通过注解注册服务
- [x] 集成 Spring 通过注解消费服务
- [x] 使用 Netty 进行网络传输
    - [x] 基于开源的序列化框架 Protostuff 实现消息对象的序列化/反序列化
        - [x] **可优化**：用户通过配置文件指定序列化方式，避免硬编码
    - [x] 自定义编解码器
    - [x] TCP 心跳机制
        - [ ] **可优化**：自定义应用层的 Netty 心跳机制
    - [x] 使用 JDK/CGLIB 动态代理机制调用远程方法

- [x] 使用 Zookeeper（Curator）实现服务注册和发现
    - [ ] 基于 SPI 机制使得用户可以通过配置文件指定注册与发现中心的实现方式，避免硬编码
    - [x] 支持三种负载均衡策略：一致性哈希、随机、轮询
    - [x] 使用SPI机制实现通过配置文件指定负载均衡策略