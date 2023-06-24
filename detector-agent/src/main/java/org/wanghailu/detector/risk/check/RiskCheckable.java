package org.wanghailu.detector.risk.check;

import org.wanghailu.detector.model.DetectorContext;

/**
 * @author cdhuang
 * @date 2023/6/21
 */
public interface RiskCheckable {
    
    void check(DetectorContext detectorContext);
}
