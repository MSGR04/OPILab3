package org.web.lab3.beans;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClockBeanTest {
    @Test
    void testGetCurrentTimeFormat() {
        ClockBean bean = new ClockBean();
        String time = bean.getCurrentTime();
        assertNotNull(time);
        assertTrue(time.matches("\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2}:\\d{2}"));
    }
}