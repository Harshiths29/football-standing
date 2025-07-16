package com.FootbalStanding.Footbal.Standing.util;

import com.FootbalStanding.Footbal.Standing.dto.RequestDTO;
import com.FootbalStanding.Footbal.Standing.dto.ResponseDTO;

public class OfflineDataUtil {
    public static ResponseDTO getOfflineData(RequestDTO request) {
        if ("England".equalsIgnoreCase(request.getCountry()) &&
                "Premier League".equalsIgnoreCase(request.getLeague()) &&
                "Manchester City".equalsIgnoreCase(request.getTeam())) {

            ResponseDTO res = new ResponseDTO();
            res.setCountryId("41");
            res.setCountryName("England");
            res.setLeagueId("149");
            res.setLeagueName("Premier League");
            res.setTeamId("2621");
            res.setTeamName("Manchester City");
            res.setPosition("1");
            return res;
        }
        throw new RuntimeException("Data not found in offline mode");
    }
}