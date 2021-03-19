JVM体系结构

<div align="center"> <img src="..\..\images\jvm\JVM体系结构.png" width="500px"> </div>

JVM 运行时数据区

<div align="center"> <img src="..\..\images\jvm\Runtime Data Area.png" width="800px"> </div>

JVM启动参数

JVM启动参数可分为三类：

* **标准参数**（`-`），所有的 JVM 都要实现这些 参数，并且向后兼容。 `-D`设置系统属性。
* **非标准参数**（`-X`）， 默认 JVM 实现这些参数的功能，但并不保证所 有 JVM 实现都满足，且不保证向后兼容。 可以使 用`java -X`命令来查看当前 JVM 支持的非标准参数。
* **非稳定参数**（`-XX`），专门用于控制 JVM的 行为，跟具体的 JVM 实现有关，随时可能会在下个版本取消。 
  * `-XX：+-Flags`形式, `+- `是对布尔值进行开关。
  * ` -XX：key=value`形式, 指定某个选项的值。

<div align="center"> <img src="..\..\images\jvm\JVM启动参数.png" width="800px"> </div>

