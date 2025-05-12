package org.web.lab3.beans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class PointBeanTest {
    private PointBean bean;
    private Method isWithinArea;

    @BeforeEach
    void setUp() throws Exception {
        bean = new PointBean();
        isWithinArea = PointBean.class
                .getDeclaredMethod("isWithinArea", double.class, double.class, double.class);
        isWithinArea.setAccessible(true);
    }

    @Test
    void testIsWithinArea_Q2() throws Exception {
        double r = 2.0;
        // Quadrant II: x<=0, y>=0; условие x >= -r && y <= r
        assertTrue((Boolean) isWithinArea.invoke(bean, -1.0, 1.0, r));
        assertFalse((Boolean) isWithinArea.invoke(bean, -3.0, 1.0, r));  // x < -r → вне
        assertFalse((Boolean) isWithinArea.invoke(bean, -1.0, 3.0, r));  // y > r  → вне
    }

    @Test
    void testIsWithinArea_Q4() throws Exception {
        double r = 4.0;
        // Quadrant IV: x>=0, y<=0; условие x<=r && y>=-r/2 && (x+r)>=-y
        assertTrue((Boolean) isWithinArea.invoke(bean, 1.0, -1.0, r));
        assertTrue((Boolean) isWithinArea.invoke(bean, 2.0, -1.0, r));  // по формуле внутри
        assertFalse((Boolean) isWithinArea.invoke(bean, 5.0, -1.0, r)); // x > r → вне
        assertFalse((Boolean) isWithinArea.invoke(bean, 1.0, -3.0, r)); // y < -r/2 → вне
    }

    @Test
    void testIsWithinArea_Q1() throws Exception {
        double r = 2.0;
        // Quadrant I: x>=0, y>=0; условие x^2+y^2 <= r^2/4
        assertTrue((Boolean) isWithinArea.invoke(bean, 0.5, 0.5, r));  // 0.5²+0.5²=0.5 <=1
        assertFalse((Boolean) isWithinArea.invoke(bean, 1.0, 1.0, r)); // 1+1=2 >1 → вне
    }

    @Test
    void testIsWithinArea_Q3() throws Exception {
        double r = 3.0;
        // Quadrant III: метод возвращает false всегда
        assertFalse((Boolean) isWithinArea.invoke(bean, -1.0, -1.0, r));
        assertFalse((Boolean) isWithinArea.invoke(bean, -2.0, -2.0, r));
    }

    @Test
    void testSetInvalidRadius() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> bean.setRadius(0)
        );
        assertEquals("Radius should be more than 0", ex.getMessage());
    }
}
