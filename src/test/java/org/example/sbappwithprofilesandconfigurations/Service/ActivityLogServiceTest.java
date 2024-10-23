package org.example.sbappwithprofilesandconfigurations.Service;

import org.example.sbappwithprofilesandconfigurations.Model.ActivityLog;
import org.example.sbappwithprofilesandconfigurations.Repo.ActivityLogRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ActivityLogServiceTest {
    @Mock
    private ActivityLogRepo activityLogRepo;
    @InjectMocks
    private ActivityLogService activityLogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void ActivityLogService() {
        String username = "testUsername";
        String action = "testAction";
        String ipAddress = "testIpAddress";


        when(activityLogRepo.save(any(ActivityLog.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        ActivityLog activityLog = activityLogService.logActivity(username, action, ipAddress);

        assertEquals(username, activityLog.getUsername());
        assertEquals(action, activityLog.getAction());
        assertEquals(username, activityLog.getUsername());
        verify(activityLogRepo, times(1)).save(any(ActivityLog.class));
    }
}
