package org.wanghailu.detector.util;

import org.wanghailu.detector.log.LogUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;


/**
 * Referer https://github.com/alibaba/arthas/blob/master/boot/src/main/java/com/taobao/arthas/boot/ProcessUtils.java
 */

public class ProcessUtils {

    private static String currentPidStr = "-1";
    private static long currentPid = -1;

    static {
        // https://stackoverflow.com/a/7690178
        String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        int index = jvmName.indexOf('@');

        if (index > 0) {
            try {
                currentPidStr = Long.toString(Long.parseLong(jvmName.substring(0, index)));
                currentPid = Long.parseLong(currentPidStr);
            } catch (Throwable e) {
                // ignore
            }
        }
    }

    private static String FOUND_JAVA_HOME = null;

    public static String select(boolean selectAll, String selectName) throws InputMismatchException {
        Map<Long, String> processMap = listProcessByJps();

        if (processMap.isEmpty()) {
            LogUtils.info("Can not find java process. Try to pass <pid> in command line.");
            return null;
        }

        if (selectName != null && !selectName.trim().isEmpty()) {       // selectName not null ，match class name
            List<Long> selectPids = new ArrayList<>();
            for (Map.Entry<Long, String> entry : processMap.entrySet()) {
                if (entry.getValue().contains(selectName)) {
                    selectPids.add(entry.getKey());
                }
            }
            if (selectPids.size() > 0) {
                return selectPids.stream().map(x -> x.toString()).collect(Collectors.joining(","));
            } else {
                LogUtils.info("Found With selectName key " + selectName + ", but select nothing.");
            }
        }

        if (selectAll) {
            return processMap.keySet().stream().map(x -> x.toString()).collect(Collectors.joining(","));
        }

        LogUtils.info("Found existing java process, please choose one serial number and hit RETURN , Multiple separated by commas.");
        // print list
        int count = 1;
        for (String process : processMap.values()) {
            if (count == 1) {
                System.out.println("* [" + count + "]: " + process);
            } else {
                System.out.println("  [" + count + "]: " + process);
            }
            count++;
        }

        // read choice
        String line = new Scanner(System.in).nextLine();
        if (line.trim().isEmpty()) {
            return null;
        }

        String choiceStr = new Scanner(line).next();
        String[] choices = choiceStr.split(",");
        List<Integer> choiceSerial = new ArrayList<>();
        for (String choice : choices) {
            try {
                choiceSerial.add(Integer.parseInt(choice));
            } catch (Exception ignored) {

            }
        }
        List<Long> selectPids = new ArrayList<>();

        int index = 1;
        for (Long pid : processMap.keySet()) {
            if (choiceSerial.contains(index)) {
                selectPids.add(pid);
            }
            index++;
        }
        if (selectPids.size() > 0) {
            return selectPids.stream().map(x -> x.toString()).collect(Collectors.joining(","));
        } else {
            return null;
        }
    }

    private static Map<Long, String> listProcessByJps() {
        Map<Long, String> result = new LinkedHashMap<Long, String>();

        String jps = "jps";
        File jpsFile = findJps();
        if (jpsFile != null) {
            jps = jpsFile.getAbsolutePath();
        }
        LogUtils.debug("Try use jps to list java process, jps: " + jps);

        String[] command = new String[]{jps, "-l"};

        List<String> lines = runCmd(command);

        LogUtils.debug("jps result: " + lines);

        for (String line : lines) {
            String[] strings = line.trim().split("\\s+");
            if (strings.length < 1) {
                continue;
            }
            try {
                long pid = Long.parseLong(strings[0]);
                if (pid == currentPid) {    //skip self
                    continue;
                }
                if (strings.length >= 2 && isJpsProcess(strings[1])) { // skip jps
                    continue;
                }

                result.put(pid, line);
            } catch (Throwable e) {
                // https://github.com/alibaba/arthas/issues/970
                // ignore
            }
        }

        return result;
    }

    /**
     * Executes a command on the native command line and returns the result line by
     * line.
     *
     * @param cmdToRunWithArgs Command to run and args, in an array
     * @return A list of Strings representing the result of the command, or empty
     * string if the command failed
     */
    public static List<String> runCmd(String[] cmdToRunWithArgs) {
        Process p;
        try {
            p = Runtime.getRuntime().exec(cmdToRunWithArgs);
        } catch (SecurityException e) {
            LogUtils.info("Couldn't run command {}:", Arrays.toString(cmdToRunWithArgs));
            LogUtils.info(e);
            return new ArrayList<String>(0);
        } catch (IOException e) {
            LogUtils.info("Couldn't run command {}:", Arrays.toString(cmdToRunWithArgs));
            LogUtils.info(e);
            return new ArrayList<String>(0);
        }

        ArrayList<String> sa = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                sa.add(line);
            }
            p.waitFor();
        } catch (IOException e) {
            LogUtils.info("Problem reading output from {}:", Arrays.toString(cmdToRunWithArgs));
            LogUtils.info(e);
            return new ArrayList<String>(0);
        } catch (InterruptedException ie) {
            LogUtils.info("Problem reading output from {}:", Arrays.toString(cmdToRunWithArgs));
            LogUtils.info(ie);
            Thread.currentThread().interrupt();
        } finally {
            TruckUtil.streamClose(reader);
        }
        return sa;
    }

    private static File findJps() {
        // Try to find jps under java.home and System env JAVA_HOME
        String javaHome = System.getProperty("java.home");
        String[] paths = {"bin/jps", "bin/jps.exe", "../bin/jps", "../bin/jps.exe"};

        List<File> jpsList = new ArrayList<File>();
        for (String path : paths) {
            File jpsFile = new File(javaHome, path);
            if (jpsFile.exists()) {
                LogUtils.debug("Found jps: " + jpsFile.getAbsolutePath());
                jpsList.add(jpsFile);
            }
        }

        if (jpsList.isEmpty()) {
            LogUtils.debug("Can not find jps under :" + javaHome);
            String javaHomeEnv = System.getenv("JAVA_HOME");
            LogUtils.debug("Try to find jps under env JAVA_HOME :" + javaHomeEnv);
            for (String path : paths) {
                File jpsFile = new File(javaHomeEnv, path);
                if (jpsFile.exists()) {
                    LogUtils.debug("Found jps: " + jpsFile.getAbsolutePath());
                    jpsList.add(jpsFile);
                }
            }
        }

        if (jpsList.isEmpty()) {
            LogUtils.debug("Can not find jps under current java home: " + javaHome);
            return null;
        }

        // find the shortest path, jre path longer than jdk path
        if (jpsList.size() > 1) {
            Collections.sort(jpsList, new Comparator<File>() {
                @Override
                public int compare(File file1, File file2) {
                    try {
                        return file1.getCanonicalPath().length() - file2.getCanonicalPath().length();
                    } catch (IOException e) {
                        // ignore
                    }
                    return -1;
                }
            });
        }
        return jpsList.get(0);
    }

    private static boolean isJpsProcess(String mainClassName) {
        return "sun.tools.jps.Jps".equals(mainClassName) || "jdk.jcmd/sun.tools.jps.Jps".equals(mainClassName);
    }

    /**
     * <pre>
     * 1. Try to find java home from System Property java.home
     * 2. If jdk > 8, FOUND_JAVA_HOME set to java.home
     * 3. If jdk <= 8, try to find tools.jar under java.home
     * 4. If tools.jar do not exists under java.home, try to find System env JAVA_HOME
     * 5. If jdk <= 8 and tools.jar do not exists under JAVA_HOME, throw IllegalArgumentException
     * </pre>
     *
     * @return
     */
    public static String findJavaHome() {
        if (FOUND_JAVA_HOME != null) {
            return FOUND_JAVA_HOME;
        }

        String javaHome = System.getProperty("java.home");

        if (JavaVersionUtils.isLessThanJava9()) {
            File toolsJar = new File(javaHome, "lib/tools.jar");
            if (!toolsJar.exists()) {
                toolsJar = new File(javaHome, "../lib/tools.jar");
            }
            if (!toolsJar.exists()) {
                // maybe jre
                toolsJar = new File(javaHome, "../../lib/tools.jar");
            }

            if (toolsJar.exists()) {
                FOUND_JAVA_HOME = javaHome;
                return FOUND_JAVA_HOME;
            }

            if (!toolsJar.exists()) {
                LogUtils.debug("Can not find tools.jar under java.home: " + javaHome);
                String javaHomeEnv = System.getenv("JAVA_HOME");
                if (javaHomeEnv != null && !javaHomeEnv.isEmpty()) {
                    LogUtils.debug("Try to find tools.jar in System Env JAVA_HOME: " + javaHomeEnv);
                    // $JAVA_HOME/lib/tools.jar
                    toolsJar = new File(javaHomeEnv, "lib/tools.jar");
                    if (!toolsJar.exists()) {
                        // maybe jre
                        toolsJar = new File(javaHomeEnv, "../lib/tools.jar");
                    }
                }

                if (toolsJar.exists()) {
                    LogUtils.info("Found java home from System Env JAVA_HOME: " + javaHomeEnv);
                    FOUND_JAVA_HOME = javaHomeEnv;
                    return FOUND_JAVA_HOME;
                }

                throw new IllegalArgumentException("Can not find tools.jar under java home: " + javaHome
                        + ", please try to start arthas-boot with full path java. Such as /opt/jdk/bin/java -jar arthas-boot.jar");
            }
        } else {
            FOUND_JAVA_HOME = javaHome;
        }
        return FOUND_JAVA_HOME;
    }

    private static File findJava() {
        String javaHome = findJavaHome();
        String[] paths = {"bin/java", "bin/java.exe", "../bin/java", "../bin/java.exe"};

        List<File> javaList = new ArrayList<File>();
        for (String path : paths) {
            File javaFile = new File(javaHome, path);
            if (javaFile.exists()) {
                LogUtils.debug("Found java: " + javaFile.getAbsolutePath());
                javaList.add(javaFile);
            }
        }

        if (javaList.isEmpty()) {
            LogUtils.debug("Can not find java/java.exe under current java home: " + javaHome);
            return null;
        }

        // find the shortest path, jre path longer than jdk path
        if (javaList.size() > 1) {
            Collections.sort(javaList, new Comparator<File>() {
                @Override
                public int compare(File file1, File file2) {
                    try {
                        return file1.getCanonicalPath().length() - file2.getCanonicalPath().length();
                    } catch (IOException e) {
                        // ignore
                    }
                    return -1;
                }
            });
        }
        return javaList.get(0);
    }

    private static File findToolsJar() {
        if (JavaVersionUtils.isGreaterThanJava8()) {
            return null;
        }

        String javaHome = findJavaHome();
        File toolsJar = new File(javaHome, "lib/tools.jar");
        if (!toolsJar.exists()) {
            toolsJar = new File(javaHome, "../lib/tools.jar");
        }
        if (!toolsJar.exists()) {
            // maybe jre
            toolsJar = new File(javaHome, "../../lib/tools.jar");
        }

        if (!toolsJar.exists()) {
            throw new IllegalArgumentException("Can not find tools.jar under java home: " + javaHome);
        }

        LogUtils.debug("Found tools.jar: " + toolsJar.getAbsolutePath());
        return toolsJar;
    }

    //real start
    public static void startProcess(String targetPid, List<String> attachArgs) {
        // find java/java.exe, then try to find tools.jar
        String javaHome = findJavaHome();

        // find java/java.exe
        File javaPath = findJava();
        if (javaPath == null) {
            throw new IllegalArgumentException(
                    "Can not find java/java.exe executable file under java home: " + javaHome);
        }

        File toolsJar = findToolsJar();

        if (JavaVersionUtils.isLessThanJava9()) {
            if (toolsJar == null || !toolsJar.exists()) {
                throw new IllegalArgumentException("Can not find tools.jar under java home: " + javaHome);
            }
        }

        List<String> command = new ArrayList<String>();
        command.add(javaPath.getAbsolutePath());

        if (toolsJar != null && toolsJar.exists()) {
            // solve tools.jar com.sun.tools.attach.VirtualMachine Class not found exception
            command.add("-Xbootclasspath/a:" + toolsJar.getAbsolutePath());
        }

        command.addAll(attachArgs);

        if (!JavaVersionUtils.isLessThanJava9()) {
            command.add("greater_than_jdk9_flag");
        }
        if (toolsJar != null && toolsJar.exists()) {
            command.add("bootstart_flag");
        }

        ProcessBuilder pb = new ProcessBuilder(command);

        try {
            final Process proc = pb.start();
            Thread redirectStdout = new Thread(new Runnable() {
                @Override
                public void run() {
                    InputStream inputStream = proc.getInputStream();
                    try {
                        TruckUtil.streamCopy(inputStream, System.out);
                    } catch (IOException e) {
                        TruckUtil.streamClose(inputStream);
                    }

                }
            });

            Thread redirectStderr = new Thread(new Runnable() {
                @Override
                public void run() {
                    InputStream inputStream = proc.getErrorStream();
                    try {
                        TruckUtil.streamCopy(inputStream, System.err);
                    } catch (IOException e) {
                        TruckUtil.streamClose(inputStream);
                    }

                }
            });
            redirectStdout.start();
            redirectStderr.start();
            redirectStdout.join();
            redirectStderr.join();

            int exitValue = proc.exitValue();
            if (exitValue != 0) {
                LogUtils.error("attach fail, targetPid: " + targetPid);
                System.exit(1);
            }
        } catch (Throwable e) {
            // ignore
        }
    }

}