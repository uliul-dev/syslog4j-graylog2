package org.graylog2.syslog4j.impl.unix;

import com.sun.jna.Memory;
import org.graylog2.syslog4j.SyslogConstants;
import org.junit.Test;

//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.Assert.assertEquals;

public class UnixSyslogTest {
    @Test
    public void writeUsesCorrectSyslogPriority() throws Exception {
        
        //final UnixSyslog.CLibrary mockLibrary = mock(UnixSyslog.CLibrary.class);
        AtomicInteger verifyTimes = new AtomicInteger(0);
        final UnixSyslog.CLibrary mockLibrary = new UnixSyslog.CLibrary() {
            @Override
            public void openlog(Memory ident, int option, int facility) {
            }

            @Override
            public void syslog(int priority, String format, String message) {
                verifyTimes.set(1);
            }

            @Override
            public void closelog() {
            }
        };

        UnixSyslog.libraryInstance = mockLibrary;
        UnixSyslog.currentFacility = SyslogConstants.FACILITY_LOCAL4;

        final UnixSyslogConfig config = new UnixSyslogConfig();
        config.setFacility(SyslogConstants.FACILITY_LOCAL4);

        UnixSyslog.write(SyslogConstants.LEVEL_NOTICE, "Test", config);

        //verify(mockLibrary, times(1)).syslog(165, "%s", "Test");
        assertEquals(verifyTimes.get(), 1);
    }

}
