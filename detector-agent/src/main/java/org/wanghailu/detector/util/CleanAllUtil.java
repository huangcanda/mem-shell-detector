package org.wanghailu.detector.util;

import org.wanghailu.detector.analyze.AnalysisCodeContext;
import org.wanghailu.detector.log.LogUtils;
import org.wanghailu.detector.model.ClassByteHolder;

/**
 * @author cdhuang
 * @date 2023/6/21
 */
public class CleanAllUtil {
    
    public static void close(){
        ClassByteHolder.finallyClean();
        AnalysisCodeContext.finallyClean();
        LogUtils.close();
    }
}
