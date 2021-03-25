# 堆内存

**堆内存划分**

Java 7及之前堆内存逻辑上分为三部分：新生代+老年代+**永久区**

| 新生代                                    | 老年代                  | 永久区          |
| ----------------------------------------- | ----------------------- | --------------- |
| Young Generation Space                    | Tenure generation space | Permanent Space |
| Young/New（又被划分为Eden区和Survivor区） | Old/Tenure              | Perm            |

Java 8及之后堆内存逻辑上分为三部分：新生代+老年代+**元空间**

| 新生代                                    | 老年代                  | 元空间     |
| ----------------------------------------- | ----------------------- | ---------- |
| Young Generation Space                    | Tenure generation space | Meta Space |
| Young/New（又被划分为Eden区和Survivor区） | Old/Tenure              | Meta       |

其中年轻代又可以划分为Eden空间、Survivor0空间和Survivor1空间（有时也叫做from区、to区）。

 <div align="center"> <img src="..\mission4\新生代与老年代.png" width="800px"></div>

**堆对象分配过程**

* new的对象先放Eden区。
* 当Eden区的空间填满时，程序还需创建对象，JVM的垃圾回收器将对Eden区进行垃圾回收（**MinorGC**，又称Young GC），将Eden区中的不再被其他对象所引用的对象进行销毁，再加载新的对象放到Eden区。
* 然后将Eden区中的幸存的对象移动到From区（Survivor From区）。

- 如果再次触发垃圾回收，此时Eden区和From区幸存下来的对象就会放到To区（Survivor To区）。
  - 此过程后From区对象都放到To区，故From区变To区，原To区变From区。

- 如果再次经历垃圾回收，此时Eden区对象会重新放回From区，接着再去To区。
- 啥时候能去养老区呢？当Survivor中的对象的年龄达到15的时候，将会触发一次 Promotion晋升的操作，对象晋升至养老区。可以设置次数：`-Xx:MaxTenuringThreshold= N`，**默认是15次**。
- 当养老区内存不足时，再次触发垃圾回收（**Major GC**），进行养老区的内存清理。
- 若养老区执行了Major GC之后，发现依然无法进行对象的保存，就会产生OOM异常。

 <div align="center"> <img src="..\mission4\堆对象分配过程.png" width="600px"></div>

**Minor GC、Major GC、Full GC**

- Minor GC：年轻代的GC。
- Major GC：老年代的GC。
- Full GC：整堆收集，收集整个Java堆和方法区的垃圾收集。

> Major GC 和 Full GC出现STW的时间，是Minor GC的10倍以上。

# GC垃圾回收器

截止JDK1.8，一共有7款不同的垃圾收集器：

| 垃圾收集器             | 分类 | 作用位置       | 算法                    | 特点         | 使用场景                               |
| ---------------------- | ---- | -------------- | ----------------------- | ------------ | -------------------------------------- |
| Serial                 | 串行 | 新生代         | 标记-复制               | 响应速度优先 | 单CPU环境下的client模式                |
| Serial Old             | 串行 | 老年代         | 标记-压缩               | 响应速度优先 | 单CPU环境下的client模式（CMS后备方案） |
| ParNew                 | 并行 | 新生代         | 标记-复制               | 响应速度优先 | 多CPU环境Server模式下与CMS配合使用     |
| Parallel <br/>Scavenge | 并行 | 新生代         | 标记-复制               | 吞吐量优先   | 后台运算而不需要太多交互的场景         |
| Parallel Old           | 并行 | 老年代         | 标记-压缩               | 吞吐量优先   | 后台运算而不需要太多交互的场景         |
| CMS                    | 并发 | 老年代         | 标记-清除               | 响应速度优先 | 互联网站或B/S业务                      |
| G1                     | 并发 | 新生代、老年代 | 标记-压缩<br/>标记-复制 | 响应速度优先 | 面向服务端应用，将来替换CMS            |

**垃圾收集器的组合关系**

 <div align="center"> <img src="..\mission4\20201015092110205.png" width="600px"></div>

新生代收集器：Serial、ParNew、Parallel Scavenge；老年代收集器：Serial Old、Parallel Old、CMS；整堆收集器：G1。

> 两个收集器间有连线，表明它们可以搭配使用：Serial/Serial Old、Serial/CMS、ParNew/Serial Old、ParNew/CMS、Parallel Scavenge/Serial Old、Parallel Scavenge/Parallel Old、G1。

**Serial回收器**（`Serial/Serial Old`组合）：串行GC对年轻代使用 **mark-copy（标记-复制）**算法，对老年代使用 **mark-sweep-compact（标记清除-整理）** 算法。两者都是单线程的垃圾收集器，不能进行并行处理，所以**都会触发全线暂停（STW）**，停止所有的应用线程。此回收器组合简单而高效，适合用户的桌面应用场景中，可用内存一般不大（几十MB至一两百MB）的场景。

* 启动参数：`-XX:+UseSerialGC`。

**ParNew回收器**（`ParNew/Serial Old` 组合）：ParNew收集器在年轻代中同样在年轻代使用**标记-复制（mark-copy）**算法 ，在老年代使用 **标记-清除整理（mark-sweep-compact）**算法 。年轻代和老年代的垃圾回收都会触发STW事件，暂停所有的应用线程来执行垃圾收集。并行垃圾收集器适用于多核服务器，主要目标是增加吞吐量。ParNew 是很多JVM运行在Server模式下新生代的默认垃圾收集器。

* 启动参数：`-XX:+UseParNewGC`。

**Parallel回收器**：Parallel Scavenge收集器同样也采用了**复制算法、并行回收和"Stop-The-World"机制**。在JDK1.6时提供了用于执行老年代垃圾收集的Parallel Old收集器，用来代替老年代的Serial Old收集器。Parallel Old收集器采用了标记-压缩算法，但同样也是基于并行回收和"Stop-The-World"机制。在程序吞吐量优先的应用场景中，Parallel收集器和Parallel Old收集器的组合，在Server模式下的内存回收性能很不错。

* 启动参数：``-XX:+UseParallelGC -XX:+UseParallelOldGC`。

**CMS回收器**：其对年轻代采用并行STW方式的 **mark-copy (标记-复制)**算法 ，对老年代主要使用并发 **mark-sweep (标记-清除)**算法 ，**它第一次实现了让垃圾收集线程与用户线程同时工作**。**很大一部分的Java应用集中在互联网站或者B/S系统的服务端上，这类应用尤其重视服务的响应速度，希望系统停顿时间最短**。CMS收集器就非常符合这类应用的需求。CMS整个过程比之前的收集器要复杂，整个过程分为6个主要阶段：

（1）初始标记（Initial-Mark）：因为**STW**机制而出现短暂的暂停，这个阶段的主要任务仅仅只是**标记出GC Roots能直接关联到的对象**。

（2）并发标记（Concurrent-Mark）：CMS GC 遍历老年代，标记所有的存活对象。

（3）并发预处理（Concurrent-Preclean）：如果在并发标记过程中 引用关系发生了变化，JVM 会通过“Card（卡片）”的方式将发生了改变的区域标记为“脏”区，这就是所谓的卡片标记（Card Marking）。

（4）最终标记（Final-Remark）阶段：**第二次（也是最后一次）STW 停顿**，本阶段的目标是完成老年代中所有存活对象的标记。

（5）并发清除（Concurrent-Sweep）：JVM 在此 阶段删除不再使用的对象，并回收他们占用的内存空间。

（6）并发重置（Concurrent Reset）:重置 CMS 算法相关的内部 数据，为下一次 GC 循环做准备。

* 启动参数：`-XX:+UseConcMarkSweepGC`。

**G1回收器**：G1 GC有计划地避免在整个Java堆中进行全区域的垃圾收集。G1跟踪各个Region里面的垃圾堆积的价值大小（回收所获得的空间大小以及回收所需时间的经验值），在后台维护一个优先列表，**每次根据允许的收集时间，优先回收价值最大的Region**。内存的回收是以region作为基本单位的。**Region之间是复制算法**，但整体上实际可看作是**标记-压缩（Mark-Compact）算法**。G1（Garbage-First）是一款面向服务端应用的垃圾收集器，主要针对配备多核CPU及大容量内存的机器，以极高概率满足GC停顿时间的同时，还兼具高吞吐量的性能特征。

 <div align="center"> <img src="..\mission4\20201015092110555.png" width="600px"></div>

G1 GC的垃圾回收过程主要包括如下三个环节：

- **年轻代模式转移暂停（Evacuation Pause）**：当年轻代空间用满后，应用线程会被暂停，年轻代内存块中的存活对象被拷贝 到存活区。如果还没有存活区，则任意选择一部分空闲的内存块作为存活区。 拷贝的过程称为转移(Evacuation)，这和其他年轻代收集器是一样的工作原理。
- **并发标记（Concurrent Marking）**： G1并发标记的过程与CMS基本上是一样的，分为6个主要阶段。
- **转移暂停: 混合模式（Evacuation Pause (mixed)）**：不只清理年轻代，还将一部分老年代区域也加入到回收集中。混合模式的转移暂停不一定紧跟并发标记阶段。

* 启动参数：`-XX:+UseG1GC`。

JDK11与JDK12起，分别开始支持ZGC与Shennandoah GC：

* ZGC（Z Garbage Collector）: 通过着色指针和读屏障，实现几乎全部的并发执行，几毫秒级别的延迟，线性可扩展；
* Shenandoah: G1的改进版本，跟ZGC类似。

可以看出GC算法和实现的演进路线：

* `串行 -> 并行`: 重复利用多核CPU的优势，大幅降低GC暂停时间，提升吞吐量。
* `并行 -> 并发`: 不只开多个GC线程并行回收，还将GC操作拆分为多个步骤，让很多繁重的任务和应用线 程一起并发执行，减少了单次GC暂停持续的时间，这能有效降低业务系统的延迟。
* `CMS -> G1`: G1可以说是在CMS基础上进行迭代和优化开发出来的。修正了CMS一些存在的问题，而 且在GC思想上有了重大进步，也就是划分为多个小堆块进行增量回收，这样就更进一步地降低了单次 GC暂停的时间。 可以发现，随着硬件性能的提升，业界对延迟的需求也越来越迫切。
* `G1 -> ZGC`: ZGC号称无停顿垃圾收集器，这又是一次极大的改进。ZGC和G1有一些相似的地方，但是 底层的算法和思想又有了全新的突破。 ZGC把一部分GC工作，通过读屏障触发陷阱处理程序的方式， 让业务线程也可以帮忙进行GC。这样业务线程会有一点点工作量，但是不用等，延迟也被极大地降下 来了。

**GC选择**

选择正确的 GC，唯一可行的方式就是去尝试，一般性的指导原则： 

* 如果系统考虑吞吐优先，CPU 资源都用来最大程度处理业务，用 Parallel GC；
* 如果系统考虑低延迟有限，每次 GC 时间尽量短，用 CMS GC；
* 如果系统内存堆较大，同时希望整体来看平均 GC 时间可控，使用 G1 GC。 

对于内存大小的考量： 

* 一般 4G 以上，算是比较大，用 G1 的性价比较高。 
* 一般超过 8G，比如 16G-64G 内存，非常推荐使用 G1 GC。

最后需要明确一个观点：

- 没有最好的收集器，更没有万能的收集器。
- 调优永远是针对特定场景、特定需求，不存在一劳永逸的收集器。