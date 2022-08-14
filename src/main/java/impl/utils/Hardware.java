package impl.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Hardware {

    private static Logger logger = LoggerFactory.getLogger(Hardware.class);

    public static final String CPU_PROCESS  = "getProcessCpuLoad";
    public static final String CPU_SYSTEM  = "getSystemCpuLoad";
    public static final String CPU_TIME = "getProcessCpuTime";

    public static final String RAM_FREE  = "getFreePhysicalMemorySize";
    public static final String RAM_TOTAL  = "getTotalPhysicalMemorySize";

    public static final String DISK_TOTAL = "getTotalPhysicalMemorySize";
    public static final String DISK_FREE = "getFreePhysicalMemorySize";

    public static String getUsage(String hardware) {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();

        for(Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
            method.setAccessible(true);
            if(method.getName().equals(hardware) && Modifier.isPublic(method.getModifiers())) {
                Object value;
                try {
                    value = method.invoke(operatingSystemMXBean);

                    double val;
                    if(hardware.equalsIgnoreCase( CPU_PROCESS) || hardware.equalsIgnoreCase(CPU_SYSTEM)) {
                        val = (double) Math.round(Double.parseDouble(value.toString()) * 100) / 100;
                        return String.valueOf(val);
                    }

                    return value.toString();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return "";
    }
}
