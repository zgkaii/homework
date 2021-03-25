# java-homework

## week  - 01

- [ ] **1.（选做）**自己写一个简单的 Hello.java，里面需要涉及基本类型，四则运行，if 和 for，然后自己分析一下对应的字节码。
- [x] **2.（必做）**自定义一个 Classloader，加载一个 Hello.xlass 文件，执行 hello 方法，此文件内容是一个 Hello.class 文件所有字节（x=255-x）处理后的文件。
- [x] **3.（必做）**画一张图，展示 Xmx、Xms、Xmn、Meta、DirectMemory、Xss 这些内存参数的关系。
- [ ] **4.（选做）**检查一下自己维护的业务系统的 JVM 参数配置，用 jstat 和 jstack、jmap 查看一下详情，并且自己独立分析一下大概情况，思考有没有不合理的地方，如何改进。如果没有线上系统，可以自己 run 一个 web/java 项目。
- [ ] **5.（选做）**本机使用 G1 GC 启动一个程序，分析一下 JVM 情况。

## week - 02

- [x] **1.（选做）**使用 GCLogAnalysis.java 自己演练一遍串行 / 并行 / CMS / G1 的案例。
- [x] **2.（选做）**使用压测工具（wrk 或 sb），演练 gateway-server-0.0.1-SNAPSHOT.jar 示例。
- [ ] **3.（选做）**如果自己本地有可以运行的项目，可以按照 2 的方式进行演练。
- [x] **4.（必做）**根据上述自己对于 1 和 2 的演示，写一段对于不同 GC 和堆内存的总结，提交到 GitHub。
- [ ] **5.（选做）**运行课上的例子，以及 Netty 的例子，分析相关现象。
- [ ] **6.（必做）**写一段代码，使用 HttpClient 或 OkHttp 访问 [http://localhost:8801 ](http://localhost:8801/)，代码提交到 GitHub。