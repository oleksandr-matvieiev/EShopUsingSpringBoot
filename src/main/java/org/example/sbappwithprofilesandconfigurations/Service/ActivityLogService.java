package org.example.sbappwithprofilesandconfigurations.Service;

import org.example.sbappwithprofilesandconfigurations.Model.ActivityLog;
import org.example.sbappwithprofilesandconfigurations.Repo.ActivityLogRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityLogService {
    private final ActivityLogRepo logRepo;


    public ActivityLogService(ActivityLogRepo logRepo) {
        this.logRepo = logRepo;
    }

    public ActivityLog logActivity(String username, String action, String ipAddress) {
        ActivityLog log = new ActivityLog(username, action, ipAddress);
        return logRepo.save(log);
    }

    public List<ActivityLog> getAllLogs() {
        return logRepo.findAll();
    }
}
