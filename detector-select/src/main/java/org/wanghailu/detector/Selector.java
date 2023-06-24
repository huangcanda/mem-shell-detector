package org.wanghailu.detector;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import org.apache.commons.cli.*;
import org.wanghailu.detector.constant.CommonConstant;
import org.wanghailu.detector.log.LogUtils;
import org.wanghailu.detector.model.MapArgs;
import org.wanghailu.detector.util.JavaVersionUtils;
import org.wanghailu.detector.util.PathUtils;
import org.wanghailu.detector.util.ProcessUtils;
import org.wanghailu.detector.util.ProjectVersionUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class Selector {

    public static File agent_work_directory = new File(PathUtils.getCurrentDirectory());

    public static void main(String[] args) throws Exception {

        List<String> argsList = Arrays.asList(args);

        boolean is_boot_start = argsList.contains("bootstart_flag");
        boolean is_greater_than_jre9 = argsList.contains("greater_than_jdk9_flag");

        if (is_boot_start || is_greater_than_jre9) {
            realMain(args);
        } else {
            MapArgs argsMap = new MapArgs();
            argsMap.put(CommonConstant.loggerPropertyKey, CommonConstant.writeTextLoggerType);
            //获得需要attach的pid，多个用逗号隔开
            String jvm_pid = getAttachPid(args, argsMap);
            LogUtils.info("Java version : " + JavaVersionUtils.javaVersionStr());
            HashMap<String, String> version_info = ProjectVersionUtils.get_version_info();
            LogUtils.info("Version      : " + version_info.get("Project-Version"));
            LogUtils.info("Build Time   : " + version_info.get("Build-Time"));
            LogUtils.info("getAttachPid : " + jvm_pid);
            // create cop agent work directory
            if (!agent_work_directory.exists()) {
                if (!PathUtils.createDirectory(agent_work_directory)) {
                    LogUtils.error("Create directory {} failed, use {}", agent_work_directory.getAbsolutePath(),
                            PathUtils.getTempDirectory().getAbsolutePath());
                    agent_work_directory = PathUtils.getTempDirectory();
                }
            }

            String current_jar_path = PathUtils.getCurrentJarPath();
            File attach_jar_path = new File(agent_work_directory, "detector-agent.jar");

            copyAgentResource(current_jar_path, attach_jar_path);

            /*
             * java <opts> -jar detector-select.jar <pid> </path/to/agent.jar> <argStr>
             * */
            List<String> opts = new ArrayList<String>();
            opts.add("-jar");
            opts.add(current_jar_path);
            opts.add(jvm_pid);
            opts.add(attach_jar_path.getAbsolutePath());
            opts.add(argsMap.toString());

            ProcessUtils.startProcess(jvm_pid, opts);
        }
        System.exit(0);
    }

    public static String getAttachPid(String[] args, MapArgs argsMap) {
        try {
            Options options = new Options();
            options.addOption("h", "help", false, "print options information");
            options.addOption("p", "pid", true, "attach jvm process pid");
            options.addOption("n", "name", true, "attach jvm process jarName");
            options.addOption("a", "all", false, "attach all pid");

            CommandLineParser parser = new DefaultParser();
            CommandLine cmdLine = parser.parse(options, args);
            if (cmdLine.hasOption("pid")) {
                String input_pid = cmdLine.getOptionValue("pid");
                for (String pid : input_pid.split(",")) {           //check long type
                    Long.parseLong(pid);
                }
                return input_pid;
            } else {
                String selectJarName = cmdLine.hasOption("name") ? cmdLine.getOptionValue("name") : null;
                boolean selectAll = cmdLine.hasOption("all");
                try {
                    String jvm_pid = ProcessUtils.select(selectAll, selectJarName);
                    if (jvm_pid == null) {
                        LogUtils.error("Please select an available pid.");
                        System.exit(1);
                    }
                    return jvm_pid;
                } catch (InputMismatchException e) {
                    LogUtils.error("Please input an integer to select pid.");
                    System.exit(1);
                }
            }
        } catch (Throwable e) {
            LogUtils.error("Failed to parse options\n" + e.getMessage());
            System.exit(0);
        }
        return null;
    }


    public static void realMain(String[] args) throws Exception {
        String jvm_pid = args[0];
        String attach_jar_path = args[1];
        String argStr = args[2];
        String[] pids = jvm_pid.split(",");
        if (pids.length == 1) {
            realAttach(jvm_pid, attach_jar_path, argStr);
        } else {
            List<Thread> threads = new ArrayList<>();
            for (String pid : pids) {
                Thread thread = new Thread(() -> {
                    realAttach(pid, attach_jar_path, argStr);
                });
                threads.add(thread);
                thread.start();
            }
            for (Thread thread : threads) {
                thread.join();
            }
        }
        System.exit(0);
    }

    public static void realAttach(String jvm_pid, String agent_jar_path, String argStr) {
        LogUtils.info("Try to attach process " + jvm_pid);
        MapArgs argsMap = MapArgs.getMapArgs(argStr);

        String fileName = "result-" + System.currentTimeMillis() + "-" + jvm_pid + ".txt";

        File file = new File(agent_work_directory, fileName);
        fileName = file.getPath();

        argsMap.put(CommonConstant.logFileNamePropertyKey, fileName);

        for (Map.Entry<String, String> entry : argsMap.getArgs().entrySet()) {
            LogUtils.info("Print arg , Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }

        VirtualMachine virtualMachine = null;
        VirtualMachineDescriptor virtualMachineDescriptor = null;
        for (VirtualMachineDescriptor descriptor : VirtualMachine.list()) {
            String pid = descriptor.id();
            if (pid.equals(jvm_pid)) {
                virtualMachineDescriptor = descriptor;
                break;
            }
        }
        try {
            LogUtils.info("Begin attach process " + jvm_pid + ", please wait a moment ...");
            if (null == virtualMachineDescriptor) {
                virtualMachine = VirtualMachine.attach(jvm_pid);
            } else {
                virtualMachine = VirtualMachine.attach(virtualMachineDescriptor);
            }
            Properties targetSystemProperties = virtualMachine.getSystemProperties();
            String targetJavaVersion = JavaVersionUtils.javaVersionStr(targetSystemProperties);
            String currentJavaVersion = JavaVersionUtils.javaVersionStr();
            if (targetJavaVersion != null && currentJavaVersion != null) {
                // TODO 用targetJavaVersion启动
                if (!targetJavaVersion.equals(currentJavaVersion)) {
                    LogUtils.info(
                            "Current VM java version: {} do not match target VM java version: {}, attach may fail.",
                            currentJavaVersion, targetJavaVersion);
                    LogUtils.info("Target VM JAVA_HOME is {}, JAVA_HOME is {}, try to set the same JAVA_HOME.",
                            targetSystemProperties.getProperty("java.home"), System.getProperty("java.home"));
                }
            }
            virtualMachine.loadAgent(agent_jar_path, argsMap.toString());
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (null != virtualMachine) {
                try {
                    virtualMachine.detach();
                } catch (IOException e) {
                    LogUtils.error(e);
                }
            }
        }
        LogUtils.info("Attach process {} finished .", jvm_pid);
        if (file.exists()) {
            LogUtils.info("Result store in : {}", file);
        } else {
            LogUtils.info("Result store failed. Try again...");
        }
    }

    public static void copyAgentResource(String current_jar_path, File attach_jar_path) throws IOException {
        if (Selector.class.getResource("/detector-agent.jar") != null) {
            PathUtils.copyResources(PathUtils.readResources("/detector-agent.jar"), attach_jar_path);
        } else {
            LogUtils.error(current_jar_path + " not find detector-agent.jar, exit...");
            System.exit(1);
        }
    }
}
