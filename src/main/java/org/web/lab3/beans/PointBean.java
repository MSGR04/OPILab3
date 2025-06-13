package org.web.lab3.beans;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import org.web.lab3.models.Point;
import org.web.lab3.models.Result;
import org.web.lab3.monitor.ClickStats;
import org.web.lab3.utils.DatabaseManager;
import jakarta.annotation.PostConstruct;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

@Named
@RequestScoped
public class PointBean {

    private double x;
    private double y;
    private double radius;
    private ClickStats clickStats;

    @PostConstruct
    public void init() {
        try {
            // Получаем ClickStats из контекста вместо создания нового
            ExternalContext extCtx = FacesContext.getCurrentInstance().getExternalContext();
            ServletContext servletCtx = (ServletContext) extCtx.getContext();
            this.clickStats = (ClickStats) servletCtx.getAttribute("clickStats");

            if (clickStats == null) {
                System.err.println("ERROR: ClickStats not found in ServletContext!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerMBean() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = new ObjectName("org.web.lab3.monitor:type=ClickStats");
            if (!mbs.isRegistered(name)) {
                mbs.registerMBean(clickStats, name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ------------ Геттеры и сеттеры ------------- */
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }

    public double getRadius() {
        return radius;
    }
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /* --------------- Бизнес-логика -------------- */
    public void checkHit() {
        boolean insideArea = isInsideArea(x, y, radius);

        if (clickStats != null) {
            System.out.println("Registering point: (" + x + ", " + y + ")");
            clickStats.register(x, y, insideArea);
        } else {
            System.err.println("ClickStats is NULL! Cannot register point");
        }

        // 3. Сохраняем результат в базу
        Point p = new Point(x, y, radius);
        Result r = new Result(p, insideArea);
        DatabaseManager.saveResult(r);
    }

    /* ----- Метод расчёта попадания точки в заданную область ----- */
    private boolean isInsideArea(double x, double y, double r) {
        // 1) Квадрат: x ∈ [-r, 0], y ∈ [-r, 0]
        if (x >= -r && x <= 0 && y >= -r && y <= 0) {
            return true;
        }
        // 2) Четверть окружности радиуса r/2 в I квадранте
        if (x >= 0 && y >= 0 && x * x + y * y <= (r / 2) * (r / 2)) {
            return true;
        }
        // 3) Прямоугольный треугольник: x ∈ [-r, 0], y ≥ 0, y ≤ x + r
        return (x <= 0 && y >= 0 && y <= x + r);
    }
}
