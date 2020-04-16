package com.solomonl.system_property;

/**
 * @author liangchuan
 */
public class OSTest {

    private static String OS = System.getProperty("os.name").toLowerCase();

    public static void main(String[] args) {
        detectOS();
    }

    public static void detectOS() {
        if (isWindows()) {
            System.out.println("isWindows: " + OS);
        } else if (isMac()) {
            System.out.println("isMac: " + OS);
        } else if (isUnix()) {
            System.out.println("isMac: " + OS);
        }
    }

    private static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    private static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }

    private static boolean isUnix() {
        return (OS.indexOf("nux") >= 0);
    }

    /**
     *
     *
     * String osName = System.getProperty("os.name");
     String osNameMatch = osName.toLowerCase();
     if(osNameMatch.contains("linux")) {
     osType = OS_LINUX;
     }else if(osNameMatch.contains("windows")) {
     osType = OS_WINDOWS;
     }else if(osNameMatch.contains("solaris") || osNameMatch.contains("sunos")) {
     osType = OS_SOLARIS;
     }else if(osNameMatch.contains("mac os") || osNameMatch.contains("macos") || osNameMatch.contains("darwin")) {
     osType = OS_MAC_OS_X;
     }else {
     }
     */

}
