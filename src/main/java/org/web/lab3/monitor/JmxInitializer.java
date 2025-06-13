package org.web.lab3.monitor;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class JmxInitializer implements ServletContextListener {

    private ObjectName statsName;
    private ObjectName missName;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

            ClickStats stats = new ClickStats();
            statsName = new ObjectName("org.web.lab3.monitor:type=ClickStats");
            mbs.registerMBean(stats, statsName);

            MissPercentage miss = new MissPercentage(stats);
            missName = new ObjectName("org.web.lab3.monitor:type=MissPercentage");
            mbs.registerMBean(miss, missName);

            ServletContext ctx = sce.getServletContext();
            ctx.setAttribute("clickStats", stats);

        } catch (Exception e) {
            throw new RuntimeException("Cannot register JMX beans", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            if (statsName != null) mbs.unregisterMBean(statsName);
            if (missName  != null) mbs.unregisterMBean(missName);
        } catch (Exception ignored) { }
    }
}
