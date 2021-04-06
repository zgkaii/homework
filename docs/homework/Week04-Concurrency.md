- [作业2](#作业2)
  - [继承Thread类创建普通线程](#继承thread类创建普通线程)
  - [实现Runnable接口创建普通线程](#实现runnable接口创建普通线程)
  - [匿名内部类创建普通线程](#匿名内部类创建普通线程)
  - [创建守护线程](#创建守护线程)
- [作业6](#作业6)
## 作业2

**题目描述**：思考有多少种方式，在 main 函数启动一个新线程，运行一个方法，拿到这个方法的返回值后，退出主线程? 写出所有方法。

### 继承Thread类创建普通线程

```java
public class ThreadTest1 {
    public static void main(String[] args) {
        System.out.println("主线程开始执行");
        // 创建自定义线程对象
        MyThreadOne mt = new MyThreadOne("子线程");
        // 启动新线程
        mt.start();
    }
}
```

```java
public class MyThreadOne extends Thread {
    public MyThreadOne(String name) {
        super(name);
    }

    @Override
    public void run() {
        Mark.mark();
        System.out.println(getName() + "正在执行");
    }
}
```

其中调用Mark.mark()方法：

```java
public class Mark {
    public static void mark() {
        System.out.println("--------Mark--------");
    }
}
```

运行结果：

```html
主线程开始执行
--------Mark--------
子线程正在执行
```

### 实现Runnable接口创建普通线程

```java
public class ThreadTest2 {
    public static void main(String[] args) {
        System.out.println("主线程开始执行");
        MyRunnable mr = new MyRunnable();
        Thread t = new Thread(mr, "子线程");
        // 启动新线程
        t.start();
    }
}
```

```java
public class MyRunnable implements Runnable {
    @Override
    public void run() {
        Mark.mark();
        System.out.println(Thread.currentThread().getName() + "正在执行");
    }
}
```

运行结果：

```html
主线程开始执行
--------Mark--------
子线程正在执行
```

### 匿名内部类创建普通线程

```java
public class ThreadTest3 {
    public static void main(String[] args) {
        // 1.继承Thread类
        new Thread() {
            @Override
            public void run() {
                Mark.mark();
                System.out.println(getName() + "正在执行: " + this);
            }
        }.start();
        // 2.实现Runnable接口
        new Thread(new Runnable() {
            @Override
            public void run() {
                Mark.mark();
                System.out.println(Thread.currentThread().getName() + "正在执行");
            }
        }).start();        
    }
}
```

### 创建守护线程

```java
public class DaemonThreadTest {
    public static void main(String[] args) {
        Thread thread = new Thread(new DaemonRunner(), "DaemonRunner");
        thread.setDaemon(true);// 调用setDaemon(true)把该线程标记为守护线程
        thread.start();
    }

    static class DaemonRunner implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("DaemonThread finally run ...");
            }
        }
    }
}
```

## 作业6

**题目描述**：梳理多线程和并发相关知识，画一个脑图，截图上传到 GitHub 上。 可选工具:xmind，百度脑图，wps，MindManage等。

