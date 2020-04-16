package experiment;

public class MemoryTracking {

    // VM options: -XX:NativeMemoryTracking=summary
    // jcmd 14548 VM.native_memory summary
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(500000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
