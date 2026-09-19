package org.phantom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ThreatTrackerTest {
    @BeforeEach
    void setUp() {}

    @Test
    void newIP_shouldNotBeDangerous() {
        assertFalse(ThreatTracker.isDangerous("/999.999.999.999"));
    }

    @Test
    void afterThreeRecords_shouldBeDangerous() {
        String testIP = "/test-ip-001";

        ThreatTracker.record(testIP);
        ThreatTracker.record(testIP);
        assertFalse(ThreatTracker.isDangerous(testIP));

        ThreatTracker.record(testIP);
        assertTrue(ThreatTracker.isDangerous(testIP));
    }

    @Test
    void dangerousIP_countShouldNotExceedThreshold() {
        String testIP = "/test-ip-002";

        for (int i = 0; i < 10; i++) {
            ThreatTracker.record(testIP);
        }

        assertTrue(ThreatTracker.getCount(testIP) >= 3);
        assertTrue(ThreatTracker.isDangerous(testIP));
    }
}
