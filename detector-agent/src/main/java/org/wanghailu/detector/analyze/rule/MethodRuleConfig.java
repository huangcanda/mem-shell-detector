package org.wanghailu.detector.analyze.rule;

import org.wanghailu.detector.model.MethodRule;
import org.wanghailu.detector.util.SerializeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cdhuang
 * @date 2023/6/12
 */
public class MethodRuleConfig {
    
    public static List<MethodRule> ruleList;
    
    public static List<MethodRule> sinkRuleList;
    
    static {
        ruleList = new ArrayList<>();
        sinkRuleList = new ArrayList<>();
        String ruleConfig = "rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAACVdwQAAACVc3IAJ29yZy53YW5naGFpbHUuZGV0ZWN0b3IubW9kZWwuTWV0aG9kUnVsZaJqCPfs90joAgAHSQAFc2NvcmVJAAR0eXBlTAASYXJndW1lbnREZXNjcmlwdG9ydAASTGphdmEvbGFuZy9TdHJpbmc7TAAJY2xhc3NOYW1lcQB+AANMAAptZXRob2ROYW1lcQB+AANMAAZzb3VyY2VxAH4AA0wABnRhcmdldHEAfgADeHAAAAA8AAAAAXQAFChMamF2YS9sYW5nL1N0cmluZzspdAA4b3JnL2FwYWNoZS9zdHJ1dHMyL2Rpc3BhdGNoZXIvbXVsdGlwYXJ0L011bHRpUGFydFJlcXVlc3R0ABJnZXRQYXJhbWV0ZXJWYWx1ZXN0AAJQMXQAAVJzcQB+AAIAAAA8AAAAAXQAAigpdAAcamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdHQADmdldElucHV0U3RyZWFtdAABT3QAAVJzcQB+AAIAAAA8AAAAAXQAFChMamF2YS9sYW5nL1N0cmluZzspdAAcamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdHQADGdldFBhcmFtZXRlcnQAAlAxdAABUnNxAH4AAgAAADwAAAABdAACKCl0ABxqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0dAARZ2V0UGFyYW1ldGVyTmFtZXN0AAFPdAABUnNxAH4AAgAAADwAAAABdAAUKExqYXZhL2xhbmcvU3RyaW5nOyl0ABxqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0dAASZ2V0UGFyYW1ldGVyVmFsdWVzdAACUDF0AAFSc3EAfgACAAAAPAAAAAF0AAIoKXQAHGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3R0AA9nZXRQYXJhbWV0ZXJNYXB0AAFPdAABUnNxAH4AAgAAADwAAAABdAACKCl0ABxqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0dAAJZ2V0UmVhZGVydAABT3QAAVJzcQB+AAIAAAA8AAAAAXQAFChMamF2YS9sYW5nL1N0cmluZzspdAAlamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdHQACWdldEhlYWRlcnQAAlAxdAABUnNxAH4AAgAAADwAAAABdAACKCl0ACVqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0dAAOZ2V0SGVhZGVyTmFtZXN0AAFPdAABUnNxAH4AAgAAADwAAAABdAACKCl0ACVqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0dAAIZ2V0UGFydHN0AAFPdAABUnNxAH4AAgAAADwAAAABdAAUKExqYXZhL2xhbmcvU3RyaW5nOyl0ACVqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0dAAHZ2V0UGFydHQAAlAxdAABUnNxAH4AAgAAADwAAAABdADfKExvcmcvc3ByaW5nZnJhbWV3b3JrL2NvcmUvTWV0aG9kUGFyYW1ldGVyO0xvcmcvc3ByaW5nZnJhbWV3b3JrL3dlYi9tZXRob2Qvc3VwcG9ydC9Nb2RlbEFuZFZpZXdDb250YWluZXI7TG9yZy9zcHJpbmdmcmFtZXdvcmsvd2ViL2NvbnRleHQvcmVxdWVzdC9OYXRpdmVXZWJSZXF1ZXN0O0xvcmcvc3ByaW5nZnJhbWV3b3JrL3dlYi9iaW5kL3N1cHBvcnQvV2ViRGF0YUJpbmRlckZhY3Rvcnk7KXQARG9yZy9zcHJpbmdmcmFtZXdvcmsvd2ViL21ldGhvZC9zdXBwb3J0L0hhbmRsZXJNZXRob2RBcmd1bWVudFJlc29sdmVydAAPcmVzb2x2ZUFyZ3VtZW50dAACUDF0AAFSc3EAfgACAAAAPAAAAAF0AAIoKXQAJWphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3R0AApnZXRDb29raWVzdAABT3QAAVJzcQB+AAIAAAA8AAAAAXQAFChMamF2YS9sYW5nL1N0cmluZzspdAAlamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdHQACmdldEhlYWRlcnN0AAJQMXQAAVJzcQB+AAIAAAA8AAAAAXQAAigpdAAlamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdHQADmdldFF1ZXJ5U3RyaW5ndAABT3QAAVJzcQB+AAIAAAA8AAAAAXQAAigpdAAlamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdHQADmdldElucHV0U3RyZWFtdAABT3QAAVJzcQB+AAIAAAA8AAAAAXQAFChMamF2YS9sYW5nL1N0cmluZzspdAAlamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdHQADGdldFBhcmFtZXRlcnQAAlAxdAABUnNxAH4AAgAAADwAAAABdAACKCl0ACVqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0dAARZ2V0UGFyYW1ldGVyTmFtZXN0AAFPdAABUnNxAH4AAgAAADwAAAABdAAUKExqYXZhL2xhbmcvU3RyaW5nOyl0ACVqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0dAASZ2V0UGFyYW1ldGVyVmFsdWVzdAACUDF0AAFSc3EAfgACAAAAPAAAAAF0AAIoKXQAJWphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3R0AA9nZXRQYXJhbWV0ZXJNYXB0AAFPdAABUnNxAH4AAgAAADwAAAABdAACKCl0ACVqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0dAAJZ2V0UmVhZGVydAABT3QAAVJzcQB+AAIAAAA8AAAAAXQACChbYnl0ZTspdAAvb3JnL2FwYWNoZS9jYXRhbGluYS9jb25uZWN0b3IvQ295b3RlSW5wdXRTdHJlYW10AARyZWFkdAABT3QAAlAxc3EAfgACAAAAPAAAAAF0AAooW2J5dGU7SUkpdAAvb3JnL2FwYWNoZS9jYXRhbGluYS9jb25uZWN0b3IvQ295b3RlSW5wdXRTdHJlYW10AARyZWFkdAABT3QAAlAxc3EAfgACAAAAPAAAAAF0ABcoTGphdmEvbmlvL0J5dGVCdWZmZXI7KXQAL29yZy9hcGFjaGUvY2F0YWxpbmEvY29ubmVjdG9yL0NveW90ZUlucHV0U3RyZWFtdAAEcmVhZHQAAU90AAJQMXNxAH4AAgAAADwAAAABdAACKCl0ABlqYXZheC9zZXJ2bGV0L2h0dHAvQ29va2lldAAIZ2V0VmFsdWV0AAFPdAABUnNxAH4AAgAAADwAAAABdAAmKExqYXZhL2xhbmcvU3RyaW5nO0xqYXZhL2xhbmcvU3RyaW5nOyl0ABlqYXZheC9zZXJ2bGV0L2h0dHAvQ29va2lldAAGPGluaXQ+dAAEUDEsMnQAAU9zcQB+AAIAAAA8AAAAAnQACChbYnl0ZTspdAAYamF2YS91dGlsL0Jhc2U2NCREZWNvZGVydAAGZGVjb2RldAACUDF0AAFSc3EAfgACAAAAPAAAAAJ0ABQoTGphdmEvbGFuZy9TdHJpbmc7KXQAGGphdmEvdXRpbC9CYXNlNjQkRGVjb2RlcnQABmRlY29kZXQAAlAxdAABUnNxAH4AAgAAADwAAAACdAAXKExqYXZhL25pby9CeXRlQnVmZmVyOyl0ABhqYXZhL3V0aWwvQmFzZTY0JERlY29kZXJ0AAZkZWNvZGV0AAJQMXQAAVJzcQB+AAIAAAA8AAAAAnQADihbYnl0ZTtbYnl0ZTspdAAYamF2YS91dGlsL0Jhc2U2NCREZWNvZGVydAAGZGVjb2RldAACUDF0AAJQMnNxAH4AAgAAADwAAAACdAAUKExqYXZhL2xhbmcvU3RyaW5nOyl0ACZvcmcvYXBhY2hlL2NvbW1vbnMvY29kZWMvYmluYXJ5L0Jhc2U2NHQADGRlY29kZUJhc2U2NHQAAlAxdAABUnNxAH4AAgAAADwAAAACdAAIKFtieXRlOyl0ACZvcmcvYXBhY2hlL2NvbW1vbnMvY29kZWMvYmluYXJ5L0Jhc2U2NHQADGRlY29kZUJhc2U2NHQAAlAxdAABUnNxAH4AAgAAADwAAAACdAAIKFtieXRlOyl0ACZvcmcvYXBhY2hlL2NvbW1vbnMvY29kZWMvYmluYXJ5L0Jhc2U2NHQADWRlY29kZUludGVnZXJ0AAJQMXQAAVJzcQB+AAIAAAA8AAAAAnQAFChMamF2YS9sYW5nL09iamVjdDspdAAqb3JnL2FwYWNoZS9jb21tb25zL2NvZGVjL2JpbmFyeS9CYXNlTkNvZGVjdAAGZGVjb2RldAACUDF0AAFSc3EAfgACAAAAPAAAAAJ0AAgoW2J5dGU7KXQAKm9yZy9hcGFjaGUvY29tbW9ucy9jb2RlYy9iaW5hcnkvQmFzZU5Db2RlY3QABmRlY29kZXQAAlAxdAABUnNxAH4AAgAAADwAAAACdAAUKExqYXZhL2xhbmcvU3RyaW5nOyl0ACpvcmcvYXBhY2hlL2NvbW1vbnMvY29kZWMvYmluYXJ5L0Jhc2VOQ29kZWN0AAZkZWNvZGV0AAJQMXQAAVJzcQB+AAIAAAA8AAAAAnQAPihbYnl0ZTtJSUxvcmcvYXBhY2hlL2NvbW1vbnMvY29kZWMvYmluYXJ5L0Jhc2VOQ29kZWMkQ29udGV4dDspdAAmb3JnL2FwYWNoZS9jb21tb25zL2NvZGVjL2JpbmFyeS9CYXNlNjR0AAZkZWNvZGV0AAJQMXQAAVJzcQB+AAIAAAA8AAAAAnQACChbYnl0ZTspdAAYamF2YS91dGlsL0Jhc2U2NCRFbmNvZGVydAAGZW5jb2RldAACUDF0AAFSc3EAfgACAAAAPAAAAAJ0AAgoW2J5dGU7KXQAGGphdmEvdXRpbC9CYXNlNjQkRW5jb2RlcnQADmVuY29kZVRvU3RyaW5ndAACUDF0AAFSc3EAfgACAAAAPAAAAAJ0ABcoTGphdmEvbmlvL0J5dGVCdWZmZXI7KXQAGGphdmEvdXRpbC9CYXNlNjQkRW5jb2RlcnQABmVuY29kZXQAAlAxdAABUnNxAH4AAgAAADwAAAACdAAOKFtieXRlO1tieXRlOyl0ABhqYXZhL3V0aWwvQmFzZTY0JEVuY29kZXJ0AAZlbmNvZGV0AAJQMXQAAlAyc3EAfgACAAAAPAAAAAJ0AAgoW2J5dGU7KXQAKm9yZy9hcGFjaGUvY29tbW9ucy9jb2RlYy9iaW5hcnkvQmFzZU5Db2RlY3QABmVuY29kZXQAAlAxdAABUnNxAH4AAgAAADwAAAACdAAKKFtieXRlO0lJKXQAKm9yZy9hcGFjaGUvY29tbW9ucy9jb2RlYy9iaW5hcnkvQmFzZU5Db2RlY3QABmVuY29kZXQAAlAxdAABUnNxAH4AAgAAADwAAAACdAAUKExqYXZhL2xhbmcvT2JqZWN0Oyl0ACpvcmcvYXBhY2hlL2NvbW1vbnMvY29kZWMvYmluYXJ5L0Jhc2VOQ29kZWN0AAZlbmNvZGV0AAJQMXQAAVJzcQB+AAIAAAA8AAAAAnQACChbYnl0ZTspdAAqb3JnL2FwYWNoZS9jb21tb25zL2NvZGVjL2JpbmFyeS9CYXNlTkNvZGVjdAAOZW5jb2RlQXNTdHJpbmd0AAJQMXQAAVJzcQB+AAIAAAA8AAAAAnQACChbYnl0ZTspdAAqb3JnL2FwYWNoZS9jb21tb25zL2NvZGVjL2JpbmFyeS9CYXNlTkNvZGVjdAAOZW5jb2RlVG9TdHJpbmd0AAJQMXQAAVJzcQB+AAIAAAA8AAAAAnQACChbYnl0ZTspdAAmb3JnL2FwYWNoZS9jb21tb25zL2NvZGVjL2JpbmFyeS9CYXNlNjR0AAxlbmNvZGVCYXNlNjR0AAJQMXQAAVJzcQB+AAIAAAA8AAAAAnQACShbYnl0ZTtaKXQAJm9yZy9hcGFjaGUvY29tbW9ucy9jb2RlYy9iaW5hcnkvQmFzZTY0dAAMZW5jb2RlQmFzZTY0dAACUDF0AAFSc3EAfgACAAAAPAAAAAJ0AAooW2J5dGU7WlopdAAmb3JnL2FwYWNoZS9jb21tb25zL2NvZGVjL2JpbmFyeS9CYXNlNjR0AAxlbmNvZGVCYXNlNjR0AAJQMXQAAVJzcQB+AAIAAAA8AAAAAnQACyhbYnl0ZTtaWkkpdAAmb3JnL2FwYWNoZS9jb21tb25zL2NvZGVjL2JpbmFyeS9CYXNlNjR0AAxlbmNvZGVCYXNlNjR0AAJQMXQAAVJzcQB+AAIAAAA8AAAAA3QAFihJSUxqYXZhL2xhbmcvU3RyaW5nOyl0ABdqYXZhL2xhbmcvU3RyaW5nQnVpbGRlcnQAB3JlcGxhY2V0AAFPdAABUnNxAH4AAgAAADwAAAADdAACKCl0ABdqYXZhL2xhbmcvU3RyaW5nQnVpbGRlcnQACHRvU3RyaW5ndAABT3QAAVJzcQB+AAIAAAA8AAAAA3QAFChMamF2YS9sYW5nL1N0cmluZzspdAAXamF2YS9sYW5nL1N0cmluZ0J1aWxkZXJ0AAY8aW5pdD50AAJQMXQAAU9zcQB+AAIAAAA8AAAAA3QAGihMamF2YS9sYW5nL0NoYXJTZXF1ZW5jZTspdAAXamF2YS9sYW5nL1N0cmluZ0J1aWxkZXJ0AAY8aW5pdD50AAJQMXQAAU9zcQB+AAIAAAA8AAAAA3QAFChMamF2YS9sYW5nL09iamVjdDspdAAXamF2YS9sYW5nL1N0cmluZ0J1aWxkZXJ0AAZhcHBlbmR0AAJQMXQAAU9zcQB+AAIAAAA8AAAAA3QAFChMamF2YS9sYW5nL1N0cmluZzspdAAXamF2YS9sYW5nL1N0cmluZ0J1aWxkZXJ0AAZhcHBlbmR0AAJQMXQAAU9zcQB+AAIAAAA8AAAAA3QAGihMamF2YS9sYW5nL1N0cmluZ0J1ZmZlcjspdAAXamF2YS9sYW5nL1N0cmluZ0J1aWxkZXJ0AAZhcHBlbmR0AAJQMXQAAU9zcQB+AAIAAAA8AAAAA3QAGihMamF2YS9sYW5nL0NoYXJTZXF1ZW5jZTspdAAXamF2YS9sYW5nL1N0cmluZ0J1aWxkZXJ0AAZhcHBlbmR0AAJQMXQAAU9zcQB+AAIAAAA8AAAAA3QAHChMamF2YS9sYW5nL0NoYXJTZXF1ZW5jZTtJSSl0ABdqYXZhL2xhbmcvU3RyaW5nQnVpbGRlcnQABmFwcGVuZHQAAlAxdAABT3NxAH4AAgAAADwAAAADdAAKKFtjaGFyO0lJKXQAF2phdmEvbGFuZy9TdHJpbmdCdWlsZGVydAAGYXBwZW5kdAACUDF0AAFPc3EAfgACAAAAPAAAAAN0AAQoSUkpdAAXamF2YS9sYW5nL1N0cmluZ0J1aWxkZXJ0AAZkZWxldGV0AAFPdAABT3NxAH4AAgAAADwAAAADdAAmKExqYXZhL2xhbmcvU3RyaW5nO0xqYXZhL2xhbmcvU3RyaW5nOyl0ACtjb20vZ2l0aHViL3BhZ2VoZWxwZXIvcGFyc2VyL0NvdW50U3FsUGFyc2VydAAQZ2V0U21hcnRDb3VudFNxbHQAAlAxdAABUnNxAH4AAgAAADwAAAADdAAEKElJKXQAF2phdmEvbGFuZy9TdHJpbmdCdWlsZGVydAAJc3Vic3RyaW5ndAABT3QAAVJzcQB+AAIAAAA8AAAAA3QACyhJSVtjaGFyO0kpdAAXamF2YS9sYW5nL1N0cmluZ0J1aWxkZXJ0AAhnZXRDaGFyc3QAAU90AAJQM3NxAH4AAgAAADwAAAADdAAUKExqYXZhL2xhbmcvU3RyaW5nOyl0ABBqYXZhL2xhbmcvU3RyaW5ndAAGPGluaXQ+dAACUDF0AAFPc3EAfgACAAAAPAAAAAN0ABooTGphdmEvbGFuZy9TdHJpbmdCdWZmZXI7KXQAEGphdmEvbGFuZy9TdHJpbmd0AAY8aW5pdD50AAJQMXQAAU9zcQB+AAIAAAA8AAAAA3QAGyhMamF2YS9sYW5nL1N0cmluZ0J1aWxkZXI7KXQAEGphdmEvbGFuZy9TdHJpbmd0AAY8aW5pdD50AAJQMXQAAU9zcQB+AAIAAAA8AAAAA3QACChbYnl0ZTspdAAQamF2YS9sYW5nL1N0cmluZ3QABjxpbml0PnQAAlAxdAABT3NxAH4AAgAAADwAAAADdAAKKFtieXRlO0lJKXQAEGphdmEvbGFuZy9TdHJpbmd0AAY8aW5pdD50AAJQMXQAAU9zcQB+AAIAAAA8AAAAA3QACyhbYnl0ZTtJSUkpdAAQamF2YS9sYW5nL1N0cmluZ3QABjxpbml0PnQAAlAxdAABT3NxAH4AAgAAADwAAAADdAAkKFtieXRlO0lJTGphdmEvbmlvL2NoYXJzZXQvQ2hhcnNldDspdAAQamF2YS9sYW5nL1N0cmluZ3QABjxpbml0PnQAAlAxdAABT3NxAH4AAgAAADwAAAADdAAcKFtieXRlO0lJTGphdmEvbGFuZy9TdHJpbmc7KXQAEGphdmEvbGFuZy9TdHJpbmd0AAY8aW5pdD50AAJQMXQAAU9zcQB+AAIAAAA8AAAAA3QACChbY2hhcjspdAAQamF2YS9sYW5nL1N0cmluZ3QABjxpbml0PnQAAlAxdAABT3NxAH4AAgAAADwAAAADdAAKKFtjaGFyO0lJKXQAEGphdmEvbGFuZy9TdHJpbmd0AAY8aW5pdD50AAJQMXQAAU9zcQB+AAIAAAA8AAAAA3QAMihMamF2YS9sYW5nL0NoYXJTZXF1ZW5jZTtMamF2YS9sYW5nL0NoYXJTZXF1ZW5jZTspdAAQamF2YS9sYW5nL1N0cmluZ3QAB3JlcGxhY2V0AAJQMnQAAU9zcQB+AAIAAAA8AAAAA3QAFChMamF2YS91dGlsL0xvY2FsZTspdAAQamF2YS9sYW5nL1N0cmluZ3QAC3RvTG93ZXJDYXNldAABT3QAAVJzcQB+AAIAAAA8AAAAA3QAFChMamF2YS91dGlsL0xvY2FsZTspdAAQamF2YS9sYW5nL1N0cmluZ3QAC3RvVXBwZXJDYXNldAABT3QAAVJzcQB+AAIAAAA8AAAAA3QAAigpdAAQamF2YS9sYW5nL1N0cmluZ3QACGdldEJ5dGVzdAABT3QAAVJzcQB+AAIAAAA8AAAAA3QAFChMamF2YS9sYW5nL1N0cmluZzspdAAQamF2YS9sYW5nL1N0cmluZ3QACGdldEJ5dGVzdAABT3QAAVJzcQB+AAIAAAA8AAAAA3QAHChMamF2YS9uaW8vY2hhcnNldC9DaGFyc2V0Oyl0ABBqYXZhL2xhbmcvU3RyaW5ndAAIZ2V0Qnl0ZXN0AAJQMXQAAVJzcQB+AAIAAAA8AAAAA3QACyhJSVtieXRlO0kpdAAQamF2YS9sYW5nL1N0cmluZ3QACGdldEJ5dGVzdAABT3QAAlAzc3EAfgACAAAAPAAAAAN0AAsoSUlbY2hhcjtJKXQAEGphdmEvbGFuZy9TdHJpbmd0AAhnZXRDaGFyc3QAAU90AAJQM3NxAH4AAgAAADwAAAADdAAUKExqYXZhL2xhbmcvU3RyaW5nOyl0ABBqYXZhL2xhbmcvU3RyaW5ndAAGY29uY2F0dAAET3xQMXQAAVJzcQB+AAIAAAA8AAAAA3QABChJSSl0ABBqYXZhL2xhbmcvU3RyaW5ndAAJc3Vic3RyaW5ndAABT3QAAVJzcQB+AAIAAAA8AAAAA3QAAyhJKXQAEGphdmEvbGFuZy9TdHJpbmd0AAlzdWJzdHJpbmd0AAFPdAABUnNxAH4AAgAAADwAAAADdAACKCl0ABBqYXZhL2xhbmcvU3RyaW5ndAALdG9DaGFyQXJyYXl0AAFPdAABUnNxAH4AAgAAADwAAAADdAALKFtjaGFyO0lJWil0ABBqYXZhL2xhbmcvU3RyaW5ndAAGPGluaXQ+dAACUDF0AAFPc3EAfgACAAAAPAAAAAN0ABQoTGphdmEvbGFuZy9TdHJpbmc7KXQAEGphdmEvbGFuZy9TdHJpbmd0AAVzcGxpdHQAAU90AAFSc3EAfgACAAAAPAAAAAN0ABooTGphdmEvbGFuZy9TdHJpbmc7TCBpbnQ7KXQAEGphdmEvbGFuZy9TdHJpbmd0AAVzcGxpdHQAAU90AAFSc3EAfgACAAAAPAAAAAN0AAooW2J5dGU7SUkpdAAVamF2YS9sYW5nL1N0cmluZ1VURjE2dAAJbmV3U3RyaW5ndAACUDF0AAFSc3EAfgACAAAAPAAAAAN0AAooW2J5dGU7SUkpdAAWamF2YS9sYW5nL1N0cmluZ0xhdGluMXQACW5ld1N0cmluZ3QAAlAxdAABUnNxAH4AAgAAADwAAAADdAAKKFtieXRlO0lCKXQAHGphdmEvbGFuZy9TdHJpbmdDb25jYXRIZWxwZXJ0AAluZXdTdHJpbmd0AAJQMXQAAVJzcQB+AAIAAAA8AAAAA3QAHChJW2J5dGU7QkxqYXZhL2xhbmcvU3RyaW5nOyl0ABxqYXZhL2xhbmcvU3RyaW5nQ29uY2F0SGVscGVydAAHcHJlcGVuZHQAAlA0dAACUDJzcQB+AAIAAAA8AAAAA3QACihbYnl0ZTtJQil0ABBqYXZhL2xhbmcvU3RyaW5ndAAIZ2V0Qnl0ZXN0AAFPdAACUDFzcQB+AAIAAAA8AAAAA3QACShbYnl0ZTtKKXQAHGphdmEvbGFuZy9TdHJpbmdDb25jYXRIZWxwZXJ0AAluZXdTdHJpbmd0AAJQMXQAAVJzcQB+AAIAAAA8AAAAA3QAHChKW2J5dGU7QkxqYXZhL2xhbmcvU3RyaW5nOyl0ABxqYXZhL2xhbmcvU3RyaW5nQ29uY2F0SGVscGVydAAHcHJlcGVuZHQAAlA0dAACUDJzcQB+AAIAAAA8AAAAA3QAFChMamF2YS9sYW5nL09iamVjdDspdAAQamF2YS9sYW5nL1N0cmluZ3QAB3ZhbHVlT2Z0AAJQMXQAAVJzcQB+AAIAAAA8AAAAA3QAGChMamF2YS91dGlsL0NvbGxlY3Rpb247KXQADmphdmEvdXRpbC9MaXN0dAAGPGluaXQ+dAACUDF0AAFPc3EAfgACAAAAPAAAAAN0ABUoSUxqYXZhL2xhbmcvT2JqZWN0Oyl0AA5qYXZhL3V0aWwvTGlzdHQAA2FkZHQAAlAydAABT3NxAH4AAgAAADwAAAADdAAUKExqYXZhL2xhbmcvT2JqZWN0Oyl0AA5qYXZhL3V0aWwvTGlzdHQAA2FkZHQAAlAxdAABT3NxAH4AAgAAADwAAAADdAAZKElMamF2YS91dGlsL0NvbGxlY3Rpb247KXQADmphdmEvdXRpbC9MaXN0dAAGYWRkQWxsdAACUDJ0AAJQMnNxAH4AAgAAADwAAAADdAAYKExqYXZhL3V0aWwvQ29sbGVjdGlvbjspdAAOamF2YS91dGlsL0xpc3R0AAZhZGRBbGx0AAJQMXQAAU9zcQB+AAIAAAA8AAAAA3QAFShJTGphdmEvbGFuZy9PYmplY3Q7KXQADmphdmEvdXRpbC9MaXN0dAADc2V0dAACUDJ0AAFPc3EAfgACAAAAPAAAAAN0AAMoSSl0AA5qYXZhL3V0aWwvTGlzdHQAA2dldHQAA08mUnQAAVJzcQB+AAIAAAA8AAAAA3QAAigpdAAOamF2YS91dGlsL0xpc3R0AAVjbG9uZXQAAU90AAFSc3EAfgACAAAAPAAAAAN0AAIoKXQADmphdmEvdXRpbC9MaXN0dAAHdG9BcnJheXQAAU90AAFSc3EAfgACAAAAPAAAAAN0ABQoW2phdmEvbGFuZy9PYmplY3Q7KXQADmphdmEvdXRpbC9MaXN0dAAHdG9BcnJheXQAAU90AAFSc3EAfgACAAAAPAAAAAN0ABQoTGphdmEvbGFuZy9PYmplY3Q7KXQADWphdmEvdXRpbC9NYXB0AANnZXR0AANPJlJ0AAFSc3EAfgACAAAAPAAAAAN0ACYoTGphdmEvbGFuZy9PYmplY3Q7TGphdmEvbGFuZy9PYmplY3Q7KXQADWphdmEvdXRpbC9NYXB0AAxnZXRPckRlZmF1bHR0AANPJlJ0AAFSc3EAfgACAAAAPAAAAAN0ACYoTGphdmEvbGFuZy9PYmplY3Q7TGphdmEvbGFuZy9PYmplY3Q7KXQADWphdmEvdXRpbC9NYXB0AANwdXR0AARQMSwydAABT3NxAH4AAgAAADwAAAADdAARKExqYXZhL3V0aWwvTWFwOyl0AA1qYXZhL3V0aWwvTWFwdAAGcHV0QWxsdAACUDF0AAFPc3EAfgACAAAAPAAAAAN0ACYoTGphdmEvbGFuZy9PYmplY3Q7TGphdmEvbGFuZy9PYmplY3Q7KXQADWphdmEvdXRpbC9NYXB0AAtwdXRJZkFic2VudHQABFAxLDJ0AAFPc3EAfgACAAAAPAAAAAR0ABUoTGphdmEvbGFuZy9TdHJpbmc7SSl0AA9qYXZhL25ldC9Tb2NrZXR0AAY8aW5pdD50AAJQMXQAAU9zcQB+AAIAAAA8AAAABHQAAigpdAAPamF2YS9uZXQvU29ja2V0dAAPZ2V0T3V0cHV0U3RyZWFtdAABT3QAAVJzcQB+AAIAAAA8AAAABHQAEihMamF2YS9pby9SZWFkZXI7KXQAFmphdmEvaW8vQnVmZmVyZWRSZWFkZXJ0AAY8aW5pdD50AAJQMXQAAU9zcQB+AAIAAAA8AAAABHQAEyhMamF2YS9pby9SZWFkZXI7SSl0ABZqYXZhL2lvL0J1ZmZlcmVkUmVhZGVydAAGPGluaXQ+dAACUDF0AAFPc3EAfgACAAAAPAAAAAR0ABYoTGphdmEvaW8vRmlsZVJlYWRlcjspdAAWamF2YS9pby9CdWZmZXJlZFJlYWRlcnQABjxpbml0PnQAAlAxdAABT3NxAH4AAgAAADwAAAAEdAAdKExqYXZhL2lvL0lucHV0U3RyZWFtUmVhZGVyOyl0ABZqYXZhL2lvL0J1ZmZlcmVkUmVhZGVydAAGPGluaXQ+dAACUDF0AAFPc3EAfgACAAAAPAAAAAR0AAIoKXQAFmphdmEvaW8vQnVmZmVyZWRSZWFkZXJ0AAhyZWFkTGluZXQAAU90AAFSc3EAfgACAAAAPAAAAAR0ABcoTGphdmEvaW8vSW5wdXRTdHJlYW07KXQAGWphdmEvaW8vSW5wdXRTdHJlYW1SZWFkZXJ0AAY8aW5pdD50AAJQMXQAAU9zcQB+AAIAAAA8AAAABHQAMShMamF2YS9pby9JbnB1dFN0cmVhbTtMamF2YS9uaW8vY2hhcnNldC9DaGFyc2V0Oyl0ABlqYXZhL2lvL0lucHV0U3RyZWFtUmVhZGVydAAGPGluaXQ+dAACUDF0AAFPc3EAfgACAAAAPAAAAAR0ABQoTGphdmEvbGFuZy9TdHJpbmc7KXQAFGphdmEvaW8vU3RyaW5nUmVhZGVydAAGPGluaXQ+dAACUDF0AAFPc3EAfgACAAAAPAAAAAR0ABAoTGphdmEvaW8vRmlsZTspdAAXamF2YS9pby9GaWxlSW5wdXRTdHJlYW10AAY8aW5pdD50AAJQMXQAAU9zcQB+AAIAAAA8AAAABHQAFChMamF2YS9sYW5nL1N0cmluZzspdAAXamF2YS9pby9GaWxlSW5wdXRTdHJlYW10AAY8aW5pdD50AAJQMXQAAU9zcQB+AAIAAAA8AAAABHQAEChMamF2YS9pby9GaWxlOyl0ABJqYXZhL2lvL0ZpbGVSZWFkZXJ0AAY8aW5pdD50AAJQMXQAAU9zcQB+AAIAAAA8AAAABHQACihbY2hhcjtJSSl0ABlqYXZhL2lvL0lucHV0U3RyZWFtUmVhZGVydAAEcmVhZHQAAU90AAJQMXNxAH4AAgAAADwAAAAEdAAIKFtieXRlOyl0ABxqYXZhL2lvL0J5dGVBcnJheUlucHV0U3RyZWFtdAAGPGluaXQ+dAACUDF0AAFPc3EAfgACAAAAPAAAAAR0AAooW2J5dGU7SUkpdAAcamF2YS9pby9CeXRlQXJyYXlJbnB1dFN0cmVhbXQABjxpbml0PnQAAlAxdAABT3NxAH4AAgAAADwAAAAEdAAKKFtieXRlO0lJKXQAE2phdmEvaW8vSW5wdXRTdHJlYW10AARyZWFkdAABT3QAAlAxc3EAfgACAAAAPAAAAAR0ABgoTGphdmEvaW8vSW5wdXRTdHJlYW07SSl0ABtqYXZhL2lvL1B1c2hiYWNrSW5wdXRTdHJlYW10AAY8aW5pdD50AAJQMXQAAU9zcQB+AAIAAAA8AAAABHQAFyhMamF2YS9pby9JbnB1dFN0cmVhbTspdAATamF2YS9pby9JbnB1dFN0cmVhbXQABjxpbml0PnQAAlAxdAABT3NxAH4AAgAAADwAAAAEdAAXKExqYXZhL2lvL0lucHV0U3RyZWFtOyl0ABlqYXZhL2lvL09iamVjdElucHV0U3RyZWFtdAAGPGluaXQ+dAACUDF0AAFPc3EAfgACAAAAPAAAAAR0AAgoW2NoYXI7KXQAF2phdmEvaW8vQ2hhckFycmF5UmVhZGVydAAGPGluaXQ+dAACUDF0AAFPc3EAfgACAAAAPAAAAAR0AAooW2NoYXI7SUkpdAAXamF2YS9pby9DaGFyQXJyYXlSZWFkZXJ0AAY8aW5pdD50AAJQMXQAAU9zcQB+AAIAAAA8AAAABHQACihbY2hhcjtJSSl0ABdqYXZhL2lvL0NoYXJBcnJheVJlYWRlcnQABHJlYWR0AAFPdAACUDFzcQB+AAIAAAA8AAAABHQACihbYnl0ZTtJSSl0AB1qYXZhL2lvL0J5dGVBcnJheU91dHB1dFN0cmVhbXQABXdyaXRldAACUDF0AAFPc3EAfgACAAAAPAAAAAR0AAIoKXQAHWphdmEvaW8vQnl0ZUFycmF5T3V0cHV0U3RyZWFtdAALdG9CeXRlQXJyYXl0AAFPdAABUnNxAH4AAgAAADwAAAAEdAACKCl0AB1qYXZhL2lvL0J5dGVBcnJheU91dHB1dFN0cmVhbXQABXJlc2V0dAABT3QAAU9zcQB+AAIAAAA8AAAABHQAFChMamF2YS9sYW5nL1N0cmluZzspdAAMamF2YS9uZXQvVVJJdAAGPGluaXQ+dAACUDF0AAFPc3EAfgACAAAAPAAAAAR0ADgoTGphdmEvbGFuZy9TdHJpbmc7TGphdmEvbGFuZy9TdHJpbmc7TGphdmEvbGFuZy9TdHJpbmc7KXQADGphdmEvbmV0L1VSSXQABjxpbml0PnQABlAxLDIsM3QAAU9zcQB+AAIAAAA8AAAABHQAXChMamF2YS9sYW5nL1N0cmluZztMamF2YS9sYW5nL1N0cmluZztMamF2YS9sYW5nL1N0cmluZztMamF2YS9sYW5nL1N0cmluZztMamF2YS9sYW5nL1N0cmluZzspdAAMamF2YS9uZXQvVVJJdAAGPGluaXQ+dAAIUDIsMyw0LDV0AAFPc3EAfgACAAAAPAAAAAR0AAIoKXQADGphdmEvbmV0L1VSSXQABXRvVVJMdAABT3QAAVJzcQB+AAIAAAA8AAAABHQAHihMamF2YS9sYW5nL1N0cmluZztMIGJvb2xlYW47KXQAIW9yZy9hcGFjaGUvY29tbW9ucy9odHRwY2xpZW50L1VSSXQAEXBhcnNlVXJpUmVmZXJlbmNldAACUDF0AAFPc3EAfgACAAAAPAAAAAR0ABQoTGphdmEvbGFuZy9TdHJpbmc7KXQADGphdmEvbmV0L1VSTHQABjxpbml0PnQAAlAxdAABT3NxAH4AAgAAADwAAAAEdAA5KExqYXZhL2xhbmcvU3RyaW5nO0xqYXZhL2xhbmcvU3RyaW5nO0lMamF2YS9sYW5nL1N0cmluZzspdAAMamF2YS9uZXQvVVJMdAAGPGluaXQ+dAAGUDEsMiw0dAABT3NxAH4AAgAAADwAAAAEdABUKExqYXZhL2xhbmcvU3RyaW5nO0xqYXZhL2xhbmcvU3RyaW5nO0lMamF2YS9sYW5nL1N0cmluZztMamF2YS9uZXQvVVJMU3RyZWFtSGFuZGxlcjspdAAMamF2YS9uZXQvVVJMdAAGPGluaXQ+dAAGUDEsMiw0dAABT3NxAH4AAgAAADwAAAAEdAA4KExqYXZhL2xhbmcvU3RyaW5nO0xqYXZhL2xhbmcvU3RyaW5nO0xqYXZhL2xhbmcvU3RyaW5nOyl0AAxqYXZhL25ldC9VUkx0AAY8aW5pdD50AAZQMSwyLDN0AAFPc3EAfgACAAAAPAAAAAR0ACIoTGphdmEvbmV0L1VSTDtMamF2YS9sYW5nL1N0cmluZzspdAAMamF2YS9uZXQvVVJMdAAGPGluaXQ+dAAEUDEsMnQAAU9zcQB+AAIAAAA8AAAABHQAJihMamF2YS9sYW5nL1N0cmluZztMamF2YS9sYW5nL1N0cmluZzspdAATamF2YS9uZXQvVVJMRGVjb2RlcnQABmRlY29kZXQAAlAxdAABUng=";
        initRule(ruleConfig);
        String sinkConfig1 = "rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAAddwQAAAAdc3IAJ29yZy53YW5naGFpbHUuZGV0ZWN0b3IubW9kZWwuTWV0aG9kUnVsZaJqCPfs90joAgAHSQAFc2NvcmVJAAR0eXBlTAASYXJndW1lbnREZXNjcmlwdG9ydAASTGphdmEvbGFuZy9TdHJpbmc7TAAJY2xhc3NOYW1lcQB+AANMAAptZXRob2ROYW1lcQB+AANMAAZzb3VyY2VxAH4AA0wABnRhcmdldHEAfgADeHAAAAA8AAAABHQAXyhMamF2YS9sYW5nL1N0cmluZztMamF2YS9sYW5nL1N0cmluZztbamF2YS9sYW5nL09iamVjdDtMamF2YXgvbmFtaW5nL2RpcmVjdG9yeS9TZWFyY2hDb250cm9sczspdAAhamF2YXgvbmFtaW5nL2RpcmVjdG9yeS9EaXJDb250ZXh0dAAGc2VhcmNodAACUDJ0AABzcQB+AAIAAAA8AAAABHQATShMamF2YS9sYW5nL1N0cmluZztMamF2YS9sYW5nL1N0cmluZztMamF2YXgvbmFtaW5nL2RpcmVjdG9yeS9TZWFyY2hDb250cm9sczspdAAhamF2YXgvbmFtaW5nL2RpcmVjdG9yeS9EaXJDb250ZXh0dAAGc2VhcmNodAACUDJxAH4ACXNxAH4AAgAAADwAAAAEdABNKExqYXZhL2xhbmcvU3RyaW5nO0xqYXZhL2xhbmcvU3RyaW5nO0xqYXZheC9uYW1pbmcvZGlyZWN0b3J5L1NlYXJjaENvbnRyb2xzOyl0ACFqYXZheC9uYW1pbmcvZGlyZWN0b3J5L0RpckNvbnRleHR0AAZzZWFyY2h0AAJQMnEAfgAJc3EAfgACAAAAPAAAAAR0AF8oTGphdmEvbGFuZy9TdHJpbmc7TGphdmEvbGFuZy9TdHJpbmc7W2phdmEvbGFuZy9PYmplY3Q7TGphdmF4L25hbWluZy9kaXJlY3RvcnkvU2VhcmNoQ29udHJvbHM7KXQAKGphdmF4L25hbWluZy9kaXJlY3RvcnkvSW5pdGlhbERpckNvbnRleHR0AAZzZWFyY2h0AAJQMnEAfgAJc3EAfgACAAAAPAAAAAR0AE0oTGphdmEvbGFuZy9TdHJpbmc7TGphdmEvbGFuZy9TdHJpbmc7TGphdmF4L25hbWluZy9kaXJlY3RvcnkvU2VhcmNoQ29udHJvbHM7KXQAKGphdmF4L25hbWluZy9kaXJlY3RvcnkvSW5pdGlhbERpckNvbnRleHR0AAZzZWFyY2h0AAJQMnEAfgAJc3EAfgACAAAAPAAAAAR0AE0oTGphdmEvbGFuZy9TdHJpbmc7TGphdmEvbGFuZy9TdHJpbmc7TGphdmF4L25hbWluZy9kaXJlY3RvcnkvU2VhcmNoQ29udHJvbHM7KXQAKGphdmF4L25hbWluZy9kaXJlY3RvcnkvSW5pdGlhbERpckNvbnRleHR0AAZzZWFyY2h0AAJQMnEAfgAJc3EAfgACAAAAPAAAAAR0AHEoTGphdmEvbGFuZy9TdHJpbmc7TGphdmEvbGFuZy9DbGFzcztMamF2YXgvc2VydmxldC9qc3AvZWwvVmFyaWFibGVSZXNvbHZlcjtMamF2YXgvc2VydmxldC9qc3AvZWwvRnVuY3Rpb25NYXBwZXI7KXQAKGphdmF4L3NlcnZsZXQvanNwL2VsL0V4cHJlc3Npb25FdmFsdWF0b3J0AAhldmFsdWF0ZXQAAlAxcQB+AAlzcQB+AAIAAAA8AAAABHQANShMamF2YS9sYW5nL09iamVjdDtMamF2YS91dGlsL01hcDtMamF2YS9sYW5nL09iamVjdDspdAAJb2dubC9PZ25sdAAIZ2V0VmFsdWV0AAJQMXEAfgAJc3EAfgACAAAAPAAAAAR0AEYoTGphdmEvbGFuZy9PYmplY3Q7TGphdmEvdXRpbC9NYXA7TGphdmEvbGFuZy9PYmplY3Q7TGphdmEvbGFuZy9DbGFzczspdAAJb2dubC9PZ25sdAAIZ2V0VmFsdWV0AAJQMXEAfgAJc3EAfgACAAAAPAAAAAR0ADUoTGphdmEvbGFuZy9TdHJpbmc7TGphdmEvdXRpbC9NYXA7TGphdmEvbGFuZy9PYmplY3Q7KXQACW9nbmwvT2dubHQACGdldFZhbHVldAACUDFxAH4ACXNxAH4AAgAAADwAAAAEdABGKExqYXZhL2xhbmcvU3RyaW5nO0xqYXZhL3V0aWwvTWFwO0xqYXZhL2xhbmcvT2JqZWN0O0xqYXZhL2xhbmcvQ2xhc3M7KXQACW9nbmwvT2dubHQACGdldFZhbHVldAACUDFxAH4ACXNxAH4AAgAAADwAAAAEdAAmKExqYXZhL2xhbmcvT2JqZWN0O0xqYXZhL2xhbmcvT2JqZWN0Oyl0AAlvZ25sL09nbmx0AAhnZXRWYWx1ZXQAAlAxcQB+AAlzcQB+AAIAAAA8AAAABHQANyhMamF2YS9sYW5nL09iamVjdDtMamF2YS9sYW5nL09iamVjdDtMamF2YS9sYW5nL0NsYXNzOyl0AAlvZ25sL09nbmx0AAhnZXRWYWx1ZXQAAlAxcQB+AAlzcQB+AAIAAAA8AAAABHQAJihMamF2YS9sYW5nL1N0cmluZztMamF2YS9sYW5nL09iamVjdDspdAAJb2dubC9PZ25sdAAIZ2V0VmFsdWV0AAJQMXEAfgAJc3EAfgACAAAAPAAAAAR0ADcoTGphdmEvbGFuZy9TdHJpbmc7TGphdmEvbGFuZy9PYmplY3Q7TGphdmEvbGFuZy9DbGFzczspdAAJb2dubC9PZ25sdAAIZ2V0VmFsdWV0AAJQMXEAfgAJc3EAfgACAAAAPAAAAAR0ABQoTGphdmEvbGFuZy9TdHJpbmc7KXQAHG9yZy9hcGFjaGUvY29tbW9ucy9vZ25sL09nbmx0AA9wYXJzZUV4cHJlc3Npb250AAJQMXEAfgAJc3EAfgACAAAAPAAAAAR0AAIoKXQAKW9yZy9zcHJpbmdmcmFtZXdvcmsvZXhwcmVzc2lvbi9FeHByZXNzaW9udAAIZ2V0VmFsdWV0AAFPcQB+AAlzcQB+AAIAAAA8AAAABHQAEyhMamF2YS9sYW5nL0NsYXNzOyl0AClvcmcvc3ByaW5nZnJhbWV3b3JrL2V4cHJlc3Npb24vRXhwcmVzc2lvbnQACGdldFZhbHVldAABT3EAfgAJc3EAfgACAAAAPAAAAAR0ADQoTG9yZy9zcHJpbmdmcmFtZXdvcmsvZXhwcmVzc2lvbi9FdmFsdWF0aW9uQ29udGV4dDspdAApb3JnL3NwcmluZ2ZyYW1ld29yay9leHByZXNzaW9uL0V4cHJlc3Npb250AAhnZXRWYWx1ZXQAAU9xAH4ACXNxAH4AAgAAADwAAAAEdABFKExvcmcvc3ByaW5nZnJhbWV3b3JrL2V4cHJlc3Npb24vRXZhbHVhdGlvbkNvbnRleHQ7TGphdmEvbGFuZy9DbGFzczspdAApb3JnL3NwcmluZ2ZyYW1ld29yay9leHByZXNzaW9uL0V4cHJlc3Npb250AAhnZXRWYWx1ZXQAAU9xAH4ACXNxAH4AAgAAADwAAAAEdABXKExvcmcvc3ByaW5nZnJhbWV3b3JrL2V4cHJlc3Npb24vRXZhbHVhdGlvbkNvbnRleHQ7TGphdmEvbGFuZy9PYmplY3Q7TGphdmEvbGFuZy9DbGFzczspdAApb3JnL3NwcmluZ2ZyYW1ld29yay9leHByZXNzaW9uL0V4cHJlc3Npb250AAhnZXRWYWx1ZXQAAU9xAH4ACXNxAH4AAgAAADwAAAAEdABGKExvcmcvc3ByaW5nZnJhbWV3b3JrL2V4cHJlc3Npb24vRXZhbHVhdGlvbkNvbnRleHQ7TGphdmEvbGFuZy9PYmplY3Q7KXQAKW9yZy9zcHJpbmdmcmFtZXdvcmsvZXhwcmVzc2lvbi9FeHByZXNzaW9udAAIZ2V0VmFsdWV0AAFPcQB+AAlzcQB+AAIAAAA8AAAABHQAFChMamF2YS9sYW5nL09iamVjdDspdAApb3JnL3NwcmluZ2ZyYW1ld29yay9leHByZXNzaW9uL0V4cHJlc3Npb250AAhnZXRWYWx1ZXQAAU9xAH4ACXNxAH4AAgAAADwAAAAEdAAlKExqYXZhL2xhbmcvT2JqZWN0O0xqYXZhL2xhbmcvQ2xhc3M7KXQAKW9yZy9zcHJpbmdmcmFtZXdvcmsvZXhwcmVzc2lvbi9FeHByZXNzaW9udAAIZ2V0VmFsdWV0AAFPcQB+AAlzcQB+AAIAAAA8AAAABHQAFChMamF2YS9sYW5nL09iamVjdDspdAApb3JnL3NwcmluZ2ZyYW1ld29yay9leHByZXNzaW9uL0V4cHJlc3Npb250ABZnZXRWYWx1ZVR5cGVEZXNjcmlwdG9ydAABT3EAfgAJc3EAfgACAAAAPAAAAAR0AAIoKXQAKW9yZy9zcHJpbmdmcmFtZXdvcmsvZXhwcmVzc2lvbi9FeHByZXNzaW9udAAWZ2V0VmFsdWVUeXBlRGVzY3JpcHRvcnQAAU9xAH4ACXNxAH4AAgAAADwAAAAEdABGKExvcmcvc3ByaW5nZnJhbWV3b3JrL2V4cHJlc3Npb24vRXZhbHVhdGlvbkNvbnRleHQ7TGphdmEvbGFuZy9PYmplY3Q7KXQAKW9yZy9zcHJpbmdmcmFtZXdvcmsvZXhwcmVzc2lvbi9FeHByZXNzaW9udAAWZ2V0VmFsdWVUeXBlRGVzY3JpcHRvcnQAAU9xAH4ACXNxAH4AAgAAADwAAAAEdAA0KExvcmcvc3ByaW5nZnJhbWV3b3JrL2V4cHJlc3Npb24vRXZhbHVhdGlvbkNvbnRleHQ7KXQAKW9yZy9zcHJpbmdmcmFtZXdvcmsvZXhwcmVzc2lvbi9FeHByZXNzaW9udAAWZ2V0VmFsdWVUeXBlRGVzY3JpcHRvcnQAAU9xAH4ACXNxAH4AAgAAADwAAAAEdAAUKExqYXZhL2xhbmcvU3RyaW5nOyl0ABRqYXZheC9uYW1pbmcvQ29udGV4dHQABmxvb2t1cHQAAlAxcQB+AAl4";
        String sinkCOnfig2 = "rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAAIdwQAAAAIc3IAJ29yZy53YW5naGFpbHUuZGV0ZWN0b3IubW9kZWwuTWV0aG9kUnVsZaJqCPfs90joAgAHSQAFc2NvcmVJAAR0eXBlTAASYXJndW1lbnREZXNjcmlwdG9ydAASTGphdmEvbGFuZy9TdHJpbmc7TAAJY2xhc3NOYW1lcQB+AANMAAptZXRob2ROYW1lcQB+AANMAAZzb3VyY2VxAH4AA0wABnRhcmdldHEAfgADeHAAAAH0AAAABHB0ABFqYXZhL2xhbmcvUnVudGltZXQABGV4ZWN0AAJQMXBzcQB+AAIAAAH0AAAABHB0ABVqYXZhL2xhbmcvUHJvY2Vzc0ltcGx0AAVzdGFydHEAfgAHcHNxAH4AAgAAAfQAAAAEcHQAGGphdmEvbGFuZy9Qcm9jZXNzQnVpbGRlcnEAfgAKcQB+AAdwc3EAfgACAAAAZAAAAARwdAAQamF2YS9sYW5nL1N5c3RlbXQAC2xvYWRMaWJyYXJ5cQB+AAdwc3EAfgACAAAAZAAAAARwcQB+AA50AARsb2FkcQB+AAdwc3EAfgACAAAAZAAAAARwcQB+AAVxAH4AEXEAfgAHcHNxAH4AAgAAAMgAAAAEcHQAFWphdmEvbGFuZy9DbGFzc0xvYWRlcnQAC2RlZmluZUNsYXNzcQB+AAdwc3EAfgACAAAAyAAAAARwdAAlamF2YS9sYW5nL2ludm9rZS9NZXRob2RIYW5kbGVzL0xvb2t1cHQAEWRlZmluZUhpZGRlbkNsYXNzcQB+AAdweA==";
        initSinkRule(sinkConfig1, sinkCOnfig2);
    }
    
    private static void initRule(String... ruleConfigs) {
        for (String ruleConfig : ruleConfigs) {
            List<MethodRule> list = SerializeUtils.deserializeFromString(ruleConfig);
            ruleList.addAll(list);
        }
    }
    
    private static void initSinkRule(String... ruleConfigs) {
        for (String ruleConfig : ruleConfigs) {
            List<MethodRule> list = SerializeUtils.deserializeFromString(ruleConfig);
            sinkRuleList.addAll(list);
        }
    }
    
}
