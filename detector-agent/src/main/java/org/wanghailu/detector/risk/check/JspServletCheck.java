package org.wanghailu.detector.risk.check;

/**
 * @author cdhuang
 * @date 2023/6/21
 */
public class JspServletCheck extends RiskMethodCheck{

    public JspServletCheck(Class clazz) {
        super(clazz, "_jspService","(Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;)V");
    }
}
