package com.FootbalStanding.Footbal.Standing.dto;

public class ToggleModeResponse {
    private boolean offlineMode;

    public ToggleModeResponse() {}

    public ToggleModeResponse(boolean offlineMode) {
        this.offlineMode = offlineMode;
    }

    public boolean isOfflineMode() {
        return offlineMode;
    }

    public void setOfflineMode(boolean offlineMode) {
        this.offlineMode = offlineMode;
    }
}
