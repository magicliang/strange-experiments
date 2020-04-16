package experiment;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by liangchuan on 2017/6/9.
 */

public class ScriptingTest {
    public static void main(String[] args) {
        ScriptEngineManager manager = new ScriptEngineManager();
        System.out.println("Available factories:");
        manager.getEngineFactories().forEach((f) -> System.out.println(f.getEngineName()));
        /**
         * Output:
         * Available factories:
         * Oracle Nashorn
         *
         * Only nashorn?
         */

        String language = "nashorn";

        final ScriptEngine engine = manager.getEngineByName(language);
        try {
            engine.put("a", 123);
            engine.eval("var a=1 + 2;");
            System.out.println("Now a is: " + engine.get("a"));

            engine.eval("function greet(how, whom){ return how + ',' + whom + '!'}");
            String result = (String) ((Invocable) engine).invokeFunction("greet", "hello", "world");
            System.out.println("Invocation result is: " + result);

            // Like JNI, get interface by javascript. Can also manipulate javascript object in Java object, like COM and V8 mechanism.
            Welcome welcome = ((Invocable) engine).getInterface(Welcome.class);
            System.out.println("Interface result is: " + welcome.greet("s", "1"));

            // There are also compile api like node.
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
