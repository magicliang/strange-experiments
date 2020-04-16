package experiment.classloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;

interface IExample {
    String message();

}

/**
 * Created by liangchuan on 2017/6/8.
 */
public class ClassLoaderTest {

    public static void hello() {
        System.out.println("Hello, world");
    }

    // 似乎只有在这个类有 .java 或者 .class 的场景下，用 java 跑才会成功。因为类路径本身就是以应用程序根路径为起点。也就是应用程序的包内为起点。而不是target 文件夹。
    public static void main(String[] args) throws Exception {
        /**
         * 样例输出是:
         * file:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/resources.jar
         * file:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/rt.jar
         * file:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/sunrsasign.jar
         * file:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/jsse.jar
         * file:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/jce.jar
         * file:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/charsets.jar
         * file:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/jfr.jar
         * file:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/classes
         *
         * 即使我们的环境变量里面没有对 CLASS_PATH 指向 rt.jar。我们依然可以用 bootstrap 扫描器获得和加载这些包。
         * 值得注意的是， Bootstrap Classloader 本身是 JVM 自己实现的，和我们的根 ClassLoader是不一样的。
         *
         */
        URL[] urls = sun.misc.Launcher.getBootstrapClassPath().getURLs();
        for (int i = 0; i < urls.length; i++) {
            System.out.println(urls[i].toExternalForm());
        }
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        ClassLoader extClassLoader = systemClassLoader.getParent();
        ClassLoader bootstrapClassLoader = extClassLoader.getParent();

        System.out.println("systemClassLoader: " + systemClassLoader);
        /**
         * 不要随便放 jar 进这个类路径里面。
         * The extension class loader loads “standard extensions” from the jre/lib/ext directory.
         * You can drop JAR files into that directory, and the extension class loader will find the classes in them,
         * even without any class path. (Some people recommend this mechanism to avoid the “class path hell,” but see the next cautionary note.)
         */
        System.out.println("extClassLoader: " + extClassLoader);
        // 这一行返回 null，也就是说 bootstrap classloader  本身不能由 getParent 得到。
        System.out.println("bootstrapClassLoader: " + bootstrapClassLoader);
        // Java 的基本类型都是 java.lang 包里出来的，在 rt.jar 里被 bootstrap 被加载。
        bootstrapClassLoader = String.class.getClassLoader();
        // 这里更进一步证明了 bootstrap class loader 不能用平凡的 Java 对象获取。
        System.out.println("bootstrapClassLoader: " + bootstrapClassLoader);


        /**
         * output:
         * systemClassLoader: sun.misc.Launcher$AppClassLoader@18b4aac2
         * extClassLoader: sun.misc.Launcher$ExtClassLoader@1d44bcfa
         * bootstrapClassLoader: null
         * */
        try {
            // 这里就是类加载器的相对路径了。如果系统里有配置过 classpaht，就可以看到 classes 或者 libs，否则就是当前路径为类加载器的起点。
            Enumeration<URL> em1 = systemClassLoader.getResources("");
            while (em1.hasMoreElements()) {
                System.out.println(em1.nextElement());
            }
            // Output: file:/Users/magicliang/IdeaProjects/SolomonlReopository/BasicModules/target/classes/
            System.out.println("extClassLoader's loading path: " + System.getProperty("java.ext.dirs"));
            //output: extClassLoader's loading path: /Users/magicliang/Library/Java/Extensions:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/ext:/Library/Java/Extensions:/Network/Library/Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java


            Enumeration<URL> em2 = extClassLoader.getResources("");
            while (em2.hasMoreElements()) {
                System.out.println(em1.nextElement());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * Try to use experiment.classloader.CompileClassLoader to load myself's hello method.
         * 只有用 parent 为null 才可以调用自己的类加载器！
         * 另一个思路， class bytes 本身就是被加密过的。然后父加载器加载失败。就可以调用这个类加载器了。
         * 另一个思路，用来加载非 class 文件。这样父加载器也会加载失败。例如加载 java 文件。
         * 另一个思路，改写 loadClass 方法：
         *   URLClassLoader tmp =
         *   new URLClassLoader(new URL[] {getClassPath()}) {
         *   public Class loadClass(String name) {
         *   if ("example.Example".equals(name))
         *   return findClass(name);
         *   return super.loadClass(name);
         }
         };
         */
        //experiment.classloader.CompileClassLoader compileClassLoader = new experiment.classloader.CompileClassLoader(null);
        CompileClassLoader compileClassLoader = new CompileClassLoader();
        // 这是在用现成的父加载器加载这个类，注意，这个类因为 main 已经跑到这里了，必然在类加载器缓存里。
        // 一定要加完整限定名。
        Class<?> clazz = compileClassLoader.loadClass("experiment.classloader.ClassLoaderTest");

        /**
         * 为什么这里要把 capture 强转为目标类型？
         * 跨类加载器的对象可以通过成员方法通信，比如:
         * example2 = ExampleFactory.newInstance().copy(example2);
         * 这个 example2 的 classloader 和 ExampleFactory.newInstance() 生成对象使用的 classloader就不是同一个类加载器。
         * 但在这个线程里，example2 和 ExampleFactory.newInstance() 得到但对象是在同一个类加载器里，也就是说 clazz.newInstance() 是跨类加载器的。
         */
        ClassLoaderTest classLoaderTest = (ClassLoaderTest) clazz.newInstance();
        System.out.println("Create classLoaderTest instance: " + classLoaderTest);
        System.out.println("classLoader equality: " + ClassLoaderTest.class.getClassLoader()
                .equals(classLoaderTest.getClass().getClassLoader())
        );


        //把自定义的 类加载器设定成当前的类加载器，代替默认的系统类加载器。 这是在当前线程需要和其他类加载器中的Class 打交道，但又没有这些 Class 文件时的必然选择。
        Thread.currentThread().setContextClassLoader(compileClassLoader);

        //这是寻找无参方法和调用无参方法的一个例子。
        Method hello = clazz.getMethod("hello");
        hello.invoke(null, null);

        // test URLClassLoader, use current class path to find this class.
        URL[] urLs = {new URL("file:.")};// Keep in mind the url must have a protocol format as prefix.
        URLClassLoader urlClassLoader = new URLClassLoader(urls);
        clazz = urlClassLoader.loadClass("experiment.classloader.ClassLoaderTest");

        hello = clazz.getMethod("hello");
        hello.invoke(null, null);

        IExample iExample = (IExample) (urlClassLoader.loadClass("experiment.classloader.IExample")).newInstance();
        System.out.println(
                "Create interface instance without concrete class: " + iExample);// 不可能，因为没有init 方法。所以 concrete 实现类是必须的。

    }
}

/**
 * 可能可以实现的需求：
 * 执行代码前验证数字签名
 * 根据用户提供的密码解密代码，从而实现代码混淆器来反编译 class 文件。我的理解是，用户提供一个网络传输的加密过的字符串，实际上可以通过反混淆得到一个真正的 .class 文件，然后再加载。
 * 根据用户需求来动态加载类
 * 根据用户需求，让其他数据以字节码的形式加载到应用中
 * 我自己的理解：
 * 可以自己实现自己的 eval api 了！
 * 所有的 ExtentionClassLoader 和 SystemClassLoader 都是 URLClassLoader 的子类。
 */
class CompileClassLoader extends ClassLoader {


    CompileClassLoader() {

    }

    CompileClassLoader(ClassLoader parent) {
        super(parent);
    }

    private byte[] getBytes(String fileName) throws IOException {
        // Prefer path to file in Java later in 7.
        File file = new File(fileName);
        long len = file.length();
        byte[] raw = new byte[(int) len];
        try (FileInputStream fin = new FileInputStream(file)) {
            int r = fin.read(raw);
            if (r != len) {
                throw new IOException("无法读取文件： " + r + " != " + len);
            }
            return raw;
        }
    }

    private boolean compile(String javaFile) throws IOException {
        System.out.println("experiment.classloader.CompileClassLoader：正在编译 " + javaFile + "...");
        Process p = Runtime.getRuntime().exec("javac " + javaFile);
        try {
            // 这个方法会让本线程停止在此处直到这个子进程完成。
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int ret = p.exitValue();
        return ret == 0;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        System.out.println("Begin to find class via name: " + name);
        Path path = Paths.get(".");
        System.out.println(" path 的根路径是： " + path.getRoot());
        Path absolutePath = path.toAbsolutePath();
        System.out.println("absolutePath 的根路径：" + absolutePath.getRoot());
        System.out.println("absolutePath 包含的路径数量：" + absolutePath.getNameCount());
        // 注意，此时的运行时类路径，是这个项目的文件夹下的根路径，而不是 target 或者 build 或者 out 什么文件夹。
        try {
            System.out.println("List all files for path: " + path);
            Files.list(path)
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Class clazz = null;
        String fileStub = name.replace(".", "/");
        String javaFileName = fileStub + ".java";
        String classFileName = fileStub + ".class";
        File javaFile = new File(javaFileName);
        File classFile = new File(classFileName);

        // 1 Java 文件存在 2 类文件不存在或者 Java 文件的修改日期比类文件要晚
        if (javaFile.exists() && (!classFile.exists() || javaFile.lastModified() > classFile.lastModified())) {
            try {
                // 短路阻塞了？
                if (!compile(javaFileName) || !classFile.exists()) {
                    throw new ClassNotFoundException(" find class failed: " + name);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (classFile.exists()) {
            try {
                byte[] raw = getBytes(classFileName);
                // defineClass 虽然不能直接改写，但我们有了 bytes 依然可以在内存中动态定义类，不过是从 java 文件定义起，而且借助 javac 的帮助了。
                clazz = defineClass(name, raw, 0, raw.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (clazz == null) {
            clazz = super.findClass(name);
        }

        if (clazz == null) {
            throw new ClassNotFoundException(name);
        }
        return clazz;
    }
}
