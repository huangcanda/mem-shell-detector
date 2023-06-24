package org.wanghailu.detector;

import org.wanghailu.detector.log.LogUtils;
import org.wanghailu.detector.model.MapArgs;
import org.wanghailu.detector.util.CleanAllUtil;

import java.lang.instrument.Instrumentation;


public class Agent {
    
    public static void agentmain(String agentArgs, Instrumentation instrumentation) throws Exception {
        MapArgs mapArgs= MapArgs.getMapArgs(agentArgs);
        LogUtils.start(mapArgs);
        long startTime = System.currentTimeMillis();
        try {
            MemShellDetector memShellDetector = new MemShellDetector(instrumentation,mapArgs);
            memShellDetector.doDetect();
        } catch (Throwable e) {
            LogUtils.error(e);
        } finally {
            long costTimes = System.currentTimeMillis() - startTime;
            LogUtils.info("处理完成，总耗时为" + costTimes + "ms!");
            CleanAllUtil.close();
        }
    }
}
