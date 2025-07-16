package com.FootbalStanding.Footbal.Standing;

import com.FootbalStanding.Footbal.Standing.dto.RequestDTO;
import com.FootbalStanding.Footbal.Standing.dto.ResponseDTO;
import com.FootbalStanding.Footbal.Standing.service.FootballService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
@SpringBootTest
public class FootballServiceTest {

    @Autowired
    private FootballService footballService;

    @Test
    public void testFetchStandingsOffline() {
        footballService.toggleOffline();

        RequestDTO req = new RequestDTO();
        req.setCountry("England");
        req.setLeague("Premier League");
        req.setTeam("Manchester City");

        List<ResponseDTO> res = footballService.fetchStandings(req);

        Assertions.assertFalse(res.isEmpty());
        Assertions.assertEquals("44", res.get(0).getCountryId());
        Assertions.assertEquals("Manchester City", res.get(0).getTeamName());
    }
}
