# 1 JDK、JRE、JVM区别与联系

* **JDK**（Java Development Kit） 是用于开发Java应用程序的软件工具集合，主要包括了Java运行时环境（JRE）和一些Java开发工具（例如解释器`java.exe`、编译器`javac.exe`、打包工具`jar.exe`、文档生成器`javadoc.exe`等）。
* **JRE**（Java Runtime Environmet）提供Java应用程序执行时所需的环境，由Java虚拟机（JVM）、核心类（lib）及支持文件组成。
* **JVM**（Java Virtual Machine）就即所谓的Java虚拟机，它的主要工作是解释Java 程序生成的字节码文件（`xxx.class`）并将其映射到本地的CPU的指令集或OS的系统调用中。Java语言之所以能跨平台运行，是因为不同的操作系统使用不同的JVM映射规则，让其与操作系统无关，完成了跨平台性。
  * Java虚拟机根本不关心运行在其内部的程序到底是使用何种编程语言编写的，它只关心**“字节码”文件**。也就是说Java虚拟机拥有语言无关性，并不会单纯地与Java语言“终身绑定”，**只要其他编程语言的编译结果满足并包含Java虚拟机的内部指令集、符号表以及其他的辅助信息，它就是一个有效的字节码文件，就能够被虚拟机所识别并装载运行**（例如Kotlin、Groovy等语言）。

**三者之间关系**：`JDK > JRE > JVM`。

```java
JDK = JRE + tools
JRE = JVM + libraries
```

<div align="center"> <img src="..\..\..\images\jvm\jvm.image" width="600px"> </div>

> 注意：jdk 9版本后就不存在jre了。

# 2 JVM体系结构

JVM主要被分为三个子系统：

* 类加载器子系统
* 运行时数据区
* 执行引擎

不同jdk版本JVM体系结构也有略微差别：

* jdk7 之前版本

<div align="center"> <img src="..\..\..\images\jvm\JVM体系结构-jdk7.png" width="800px"> </div>

* jdk8 之后版本

<div align="center"> <img src="..\..\..\images\jvm\JVM体系结构-jdk8.png" width="800px"> </div>

# 3 字节码

`Java bytecode` 由单字节（byte）的指令组成，理论上最多支持 256 个操作码（opcode）。 实际上 Java 只使用了200左右的操作码， 还有一些操作码则保留给调试操作。

生成字节码基本命令：

* 编译生成`.class`：`javac Hello.java`。
* 反编译字节码获取指令清单：`javap -c Hello.class`或`javap -c Hello`。
  * `javap -help`查看命令。例如还要查看常量池等附加信息：`javap -c -v Hello.class`。

按照指令性质，可以将JVM指令分为4类：

* 栈操作指令
* 程序流程控制指令
* 对象操作/方法调用指令
* 算术运算及类型转换指令

> 待编写... ...

**常见算数操作与类型转换**

![img](..\..\..\images\jvm\JVM字节码指令.png)

**方法调用指令**

**Invokestatic**：顾名思义，这个指令用于调用某个类的静态方法，这是方法调用指令中最快 的一个。 

**Invokespecial** ：用来调用构造函数，但也可以用于调用同一个类中的 private 方法, 以及可 见的超类方法。 

**invokevirtual** ：如果是具体类型的目标对象，invokevirtual 用于调用公共、受保护和 package 级的私有方法。

**invokeinterface** ：当通过接口引用来调用方法时，将会编译为 invokeinterface 指令。 

**invokedynamic** ： JDK7 新增加的指令，是实现“动态类型语言”（Dynamically Typed Language）支持而进行的升级改进，同时也是 JDK8 以后支持 lambda 表达式的实现基础。

> 其余指令可查看[JVM指令手册](https://blog.csdn.net/KAIZ_LEARN/article/details/109156774)

# 4 类加载器

## 4.1 类生命周期与加载过程

<div align="center"> <img src="..\..\..\images\jvm\类的生命周期.png" width="500px"> </div>

> 待编写.. ..

## 4.2 类加载器分类

<div align="center"> <img src="..\..\..\images\jvm\类加载类别.png" width="400px"> </div>

* **启动类加载器**（Bootstrap ClassLoader）: 它用来加载 Java 的核心类，是用原生**C/C++**代码来实现的，负责加载JDK中`jre/lib/rt.jar`里所有的class。其并不继承自 `java.lang.ClassLoader`，它可以看做是JVM自带的，我们在代码层面无法直接获取到启动类加载器的引用，所以不允许直接操作它， 如果打印出来就是个 null 。举例来说，`java.lang.String`是由启动类加载器加载的，所以`String.class.getClassLoader()`就会返回null。但是后面可以看到可以通过命令行参数影响它加载什么。
* **扩展类加载器**（Extension ClassLoader）：**Java**语言编写，它负责加载JRE的扩展目录，`lib/ext` 或者由`java.ext.dirs`系统属性指定的目录中的jar包的类，代码里直接获取它的父 类加载器为null（因为无法拿到启动类加载器）。
* **应用类加载器**（AppClassLoader）：它负责在JVM启动时加载来自Java命令的classpath或者­cp选项、`java.class.path`系统属性指定的jar包和类路径。在应用程序代码里可以通过ClassLoader的静态方法`getSystemClassLoader()`来获取应用 类加载器。如果没有特别指定，则在没有使用自定义类加载器情况下，用户自定 义的类都由此加载器加载。

* **ClassLoader类**：所有的类加载器都继承自ClassLoader（**除了**启动类加载器）。

| 方法名称                                               | 描述                                                         |
| ------------------------------------------------------ | ------------------------------------------------------------ |
| `getParent()`                                          | 返回该类加载器的超类加载器                                   |
| `loadClass(String name)`                               | 加载名称为name的类，返回结果为java.lang.Class类的实例        |
| `findClass(String name)`                               | 查找名称为name的类，返回结果为java.lang.Class类的实例        |
| `findLoadedClass(String name)`                         | 查找名称为name的已经被加载过的类，返回结果为java.lang.Class类的实例 |
| `defineClass(String name, byte[] b, int off, int len)` | 把字节数组b中的内容转换为一个Java类，返回结果为java.lang.Class类的实例 |
| `resolveClass(Class<?> c)`                             | 连接指定的一个Java类                                         |

获取ClassLoader的途径：

- 获取当前ClassLoader：`clazz.getClassLoader()`
- 获取当前线程上下文的ClassLoader：`Thread.currentThread().getContextClassLoader()`
- 获取系统的ClassLoader：`ClassLoader.getSystemClassLoader()`
- 获取调用者的ClassLoader：`DriverManager.getCallerClassLoader()`

## 4.3 类加载机制

**双亲委托**：如果一个类加载器收到了类加载请求，它并不会自己先去加载，而是把这个请求委托给父类的加载器去执行。如果父类加载器还存在其父类加载器，则进一步向上委托，依次递归，请求最终将到达顶层的启动类加载器。如果父类加载器可以完成类加载任务，就成功返回，倘若父类加载器无法完成此加载任务，子加载器才会尝试自己去加载，这就是双亲委派模式。（注意，此处的父类、子类不是继承中父类、子类的概念。）

<div align="center"> <img src="..\..\..\images\jvm\双亲委托.png" width="600px"> </div>

**负责依赖**：如果一个加载器在加载某个类的时候，发现这个类依赖于另外几个类 或接口，也会去尝试加载这些依赖项。

**缓存加载**：为了提升加载效率，消除重复加载，一旦某个类被一个类加载器加 载，那么它会缓存这个加载结果，不会重复加载。

# 5 运行时数据区

# 6 执行引擎

# 7 JVM启动参数

JVM启动参数可分为三类：

* **标准参数**（`-`），所有的 JVM 都要实现这些 参数，并且向后兼容。 `-D`设置系统属性。
* **非标准参数**（`-X`）， 默认 JVM 实现这些参数的功能，但并不保证所 有 JVM 实现都满足，且不保证向后兼容。 可以使 用`java -X`命令来查看当前 JVM 支持的非标准参数。
* **非稳定参数**（`-XX`），专门用于控制 JVM的 行为，跟具体的 JVM 实现有关，随时可能会在下个版本取消。 
  * `-XX：+-Flags`形式, `+- `是对布尔值进行开关。
  * ` -XX：key=value`形式, 指定某个选项的值。

<div align="center"> <img src="..\..\..\images\jvm\JVM启动参数.png" width="1000px"> </div>

## 7.1 系统属性参数

## 7.2 运行模式参数

**`-server`**：设置 JVM 使用 server 模式，特点是启动速度比较慢，但运行时性能和内存管理效率 很高，适用于生产环境。在具有 64 位能力的 JDK 环境下将默认启用该模式，而忽略 -client 参 数。

**` -client`** ：JDK1.7 之前在32位的 x86 机器上的默认值是 -client 选项。设置 JVM 使用 client 模 式，特点是启动速度比较快，但运行时性能和内存管理效率不高，通常用于客户端应用程序或 者 PC 应用开发和调试。此外，我们知道 JVM 加载字节码后，可以解释执行，也可以编译成本 地代码再执行，所以可以配置 JVM 对字节码的处理模式。 

**`-Xint`**：在解释模式（interpreted mode）下运行，-Xint 标记会强制 JVM 解释执行所有的字节 码，这当然会降低运行速度，通常低10倍或更多。

**` -Xcomp`**：-Xcomp 参数与-Xint 正好相反，JVM 在第一次使用时会把所有的字节码编译成本地 代码，从而带来最大程度的优化。【注意预热】 

**` -Xmixed`**：-Xmixed 是混合模式，将解释模式和编译模式进行混合使用，有 JVM 自己决定，这 是 JVM 的默认模式，也是推荐模式。 我们使用 java -version 可以看到 mixed mode 等信息。

## 7.3 堆内存设置参数

`-Xmx`, 指定最大堆内存。 如 `-Xmx4g`。这只是限制了 Heap 部分的最大值为 4g。这个内存不包括栈内存，也不包括堆外使用的内存。 

`-Xms`, 指定堆内存空间的初始大小。 如 `-Xms4g`。 而且指定的内存大小，并 不是操作系统实际分配的初始值，而是GC先规划好，用到才分配。 专用服务 器上需要保持 `–Xms` 和 `–Xmx` 一致，否则应用刚启动可能就有好几个 FullGC。 当两者配置不一致时，堆内存扩容可能会导致性能抖动。 

`-Xmn`, 等价于 `-XX:NewSize`，使用 G1 垃圾收集器 不应该 设置该选项，在其他的某些业务场景下可以设置。官方建议设置为 `-Xmx` 的 `1/2 ~ 1/4`。 

`-XX：MaxPermSize=size`, 这是 JDK1.7 之前使用的。Java8 默认允许的 Meta空间无限大，此参数无效。 

`-XX：MaxMetaspaceSize=size`， Java8 默认不限制 Meta 空间，一般不允许 设置该选项。 

`-XX：MaxDirectMemorySize=size`，系统可以使用的最大堆外内存，这个参 数跟 `-Dsun.nio.MaxDirectMemorySize` 效果相同。 

`-Xss`， 设置每个线程栈的字节数，影响栈的深度。 例如 `-Xss1m` 指定线程栈为 1MB，与`-XX:ThreadStackSize=1m` 等价。

## 7.4 GC设置参数

`-XX：+UseG1GC`：使用 G1 垃圾回收器 

`-XX：+UseConcMarkSweepGC`：使用 CMS 垃圾回收器 

`-XX：+UseSerialGC`：使用串行垃圾回收器 

`-XX：+UseParallelGC`：使用并行垃圾回收器 

// Java 11+ `-XX：+UnlockExperimentalVMOptions  -XX:+UseZGC` 

// Java 12+ `-XX：+UnlockExperimentalVMOptions -XX:+UseShenandoahGC`

## 7.5 分析诊断参数

`-XX：+-HeapDumpOnOutOfMemoryError` 选项，当 OutOfMemoryError 产生，即内存溢出（堆内存或持久代) 时，自动 Dump 堆内存。 示例用法： java -XX:+HeapDumpOnOutOfMemoryError -Xmx256m ConsumeHeap 

`-XX：HeapDumpPath` 选项，与 HeapDumpOnOutOfMemoryError 搭配使用，指定内存溢出时 Dump 文件的 目录。 如果没有指定则默认为启动 Java 程序的工作目录。 示例用法： java -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/usr/local/ ConsumeHeap 自动 Dump 的 hprof 文件会存储到 /usr/local/ 目录下。 

`-XX：OnError` 选项，发生致命错误时（fatal error）执行的脚本。 例如, 写一个脚本来记录出错时间, 执行一些命令，或者 curl 一下某个在线报警的 url。 示例用法：java -XX:OnError="gdb - %p" MyApp 可以发现有一个 %p 的格式化字符串，表示进程 PID。 

`-XX：OnOutOfMemoryError` 选项，抛出 OutOfMemoryError 错误时执行的脚本。 -XX：ErrorFile=filename 选项，致命错误的日志文件名,绝对路径或者相对路径。 

`-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=1506`，远程调试。

## 7.6 JavaAgent参数

`-agentlib:libname[=options] `启用 native 方式的 agent，参考 LD_LIBRARY_PATH 路径。

 `-agentpath:pathname[=options]` 启用 native 方式的 agent。 

`-javaagent:jarpath[=options]` 启用外部的 agent 库，比如 pinpoint.jar 等等。 

`-Xnoagent` 则是禁用所有 agent。 以下示例开启 CPU 使用时间抽样分析： 

* `JAVA_OPTS="-agentlib:hprof=cpu=samples,file=cpu.samples.log"`

# 8 JVM工具

## JVM内置命令行工具

| 工具           | 简介                                                         |
| -------------- | ------------------------------------------------------------ |
| java           | Java 应用的启动程序                                          |
| javac          | JDK 内置的编译工具                                           |
| javap          | 反编译 class 文件的工具                                      |
| javadoc        | 根据 Java 代码和标准注释,自动生成相关的 API 说明文档         |
| javah          | JNI 开发时, 根据 java 代码生成需要的 .h文件                  |
| extcheck       | 检查某个 jar 文件和运行时扩展 jar 有没有版本冲突，很少使用   |
| jdb            | Java Debugger ; 可以调试本地和远端程序，属于 JPDA 中的一个 demo 实现，供其 他调试器参考。开发时很少使用 |
| jdeps          | 探测 class 或 jar 包需要的依赖                               |
| jar            | 打包工具，可以将文件和目录打包成为 .jar 文件；.jar 文件本质上就是 zip 文件, 只 是后缀不同。使用时按顺序对应好选项和参数即可 |
| keytool        | 安全证书和密钥的管理工具; （支持生成、导入、导出等操作）     |
| jarsigner      | JAR 文件签名和验证工具                                       |
| policytool     | 实际上这是一款图形界面工具, 管理本机的 Java 安全策略         |
| jps/jinfo      | 查看 java 进程                                               |
| jstat          | 查看 JVM 内部 gc 相关信息                                    |
| jmap           | 查看 heap 或类占用空间统计                                   |
| jstack         | 查看线程信息                                                 |
| jcmd           | 执行 JVM 相关分析命令（整合命令）                            |
| jrunscript/jjs | 执行 js 命令                                                 |

**jstat**

```shell
> jstat -options 
-class 类加载(Class loader)信息统计 
-compiler JIT 即时编译器相关的统计信息 
-gc GC 相关的堆内存信息。 用法: jstat -gc -h 10 -t 864 1s 20 
-gccapacity 各个内存池分代空间的容量 
-gccause 看上次 GC，本次 GC（如果正在 GC 中）的原因， 其他 输出和 -gcutil 选项一致 
-gcnew 年轻代的统计信息。（New = Young = Eden + S0 + S1） 
-gcnewcapacity 年轻代空间大小统计 
-gcold 老年代和元数据区的行为统计 
-gcoldcapacity old 空间大小统计 
-gcmetacapacity meta 区大小统计 
-gcutil GC 相关区域的使用率（utilization）统计 
-printcompilation 打印 JVM 编译统计信息
```

**jmap**

```shell
-heap 打印堆内存（/内存池）的配置和 使用信息。
-histo 看哪些类占用的空间最多, 直方图。
-dump:format=b,file=xxxx.hprof Dump 堆内存
```

**jstack**

```shell
-F 强制执行 thread dump，可在 Java 进程卡死 （hung 住）时使用，此选项可能需要系统权限。 
-m 混合模式（mixed mode)，将 Java 帧和 native 帧一起输出，此选项可能需要系统权限。 
-l 长列表模式，将线程相关的 locks 信息一起输 出，比如持有的锁，等待的锁。
```

**jcmd**

```shell
jcmd pid VM.version
jcmd pid VM.flags
jcmd pid VM.command_line
jcmd pid VM.system_properties
jcmd pid Thread.print
jcmd pid GC.class_histogram
jcmd pid GC.heap_info
```

**jrunscript/jjs**

```shell
当 curl 命令用：
jrunscript -e "cat('http://www.baidu.com')"
执行 js 脚本片段
jrunscript -e "print('hello,kk.jvm'+1)"
执行 js 文件
jrunscript -l js -f /XXX/XXX/test.js
```

## JDK 内置图形化工具

**jconsole**

**jvisualvm**

**VisualGC**

**jmc**