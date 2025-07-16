package com.FootbalStanding.Footbal.Standing.service;

import com.FootbalStanding.Footbal.Standing.dto.RequestDTO;
import com.FootbalStanding.Footbal.Standing.dto.ResponseDTO;
import com.FootbalStanding.Footbal.Standing.exception.CustomException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class FootballService {

    @Value("${api.key}")
    private String apiKey;

    @Value("${api.base-url}")
    private String apiBaseUrl;

    @Value("${app.offline-mode:true}")
    private boolean offlineMode;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<ResponseDTO> fetchStandings(RequestDTO request) {
        if (offlineMode) {
            return List.of(getOfflineData(request));
        }
        try {
            String countryId = getCountryId(request.getCountry());
            String leagueId = getLeagueId(countryId, request.getLeague());
            return getAllTeamStandings(leagueId, request.getCountry(), request.getLeague(), request.getTeam());
        } catch (Exception e) {
            throw new CustomException("Error fetching standings: " + e.getMessage());
        }
    }

    private String getCountryId(String countryName) throws Exception {
        URI uri = UriComponentsBuilder.fromUriString(apiBaseUrl)
                .queryParam("action", "get_countries")
                .queryParam("APIkey", apiKey)
                .build().toUri();
        String resp = restTemplate.getForObject(uri, String.class);
        JsonNode json = objectMapper.readTree(resp);
        for (JsonNode cn : json) {
            if (cn.has("country_name") && cn.get("country_name").asText().equalsIgnoreCase(countryName)) {
                return cn.get("country_id").asText();
            }
        }
        throw new CustomException("Country not found: " + countryName);
    }

    private String getLeagueId(String countryId, String leagueName) throws Exception {
        URI uri = UriComponentsBuilder.fromUriString(apiBaseUrl)
                .queryParam("action", "get_leagues")
                .queryParam("country_id", countryId)
                .queryParam("APIkey", apiKey)
                .build().toUri();
        String resp = restTemplate.getForObject(uri, String.class);
        JsonNode json = objectMapper.readTree(resp);
        for (JsonNode ln : json) {
            if (ln.has("league_name") && ln.get("league_name").asText().equalsIgnoreCase(leagueName)) {
                return ln.get("league_id").asText();
            }
        }
        throw new CustomException("League not found: " + leagueName);
    }

    private List<ResponseDTO> getAllTeamStandings(String leagueId, String countryName, String leagueName, String inputTeamName) throws Exception {
        URI uri = UriComponentsBuilder.fromUriString(apiBaseUrl)
                .queryParam("action", "get_standings")
                .queryParam("league_id", leagueId)
                .queryParam("APIkey", apiKey)
                .build().toUri();
        String resp = restTemplate.getForObject(uri, String.class);
        JsonNode json = objectMapper.readTree(resp);

        List<ResponseDTO> list = new ArrayList<>();
        for (JsonNode teamNode : json) {
            if (!teamNode.has("team_name")) continue;
            String tn = teamNode.get("team_name").asText();
            if (inputTeamName != null && !tn.equalsIgnoreCase(inputTeamName)) continue;
            ResponseDTO dto = new ResponseDTO();
//            dto.setCountryId(teamNode.path("country_id").asText());
            dto.setCountryName(countryName);
            dto.setLeagueId(teamNode.path("league_id").asText());
            dto.setLeagueName(leagueName);
            dto.setTeamId(teamNode.path("team_id").asText());
            dto.setTeamName(tn);
            dto.setPosition(teamNode.path("overall_league_position").asText());
            list.add(dto);
        }
        if (list.isEmpty()) {
            throw new CustomException("Team not found: " + inputTeamName);
        }
        return list;
    }

    public String toggleOffline() {
        this.offlineMode = !this.offlineMode;
        return "Offline mode is now set to: " + this.offlineMode;
    }

    private ResponseDTO getOfflineData(RequestDTO request) {
        if ("England".equalsIgnoreCase(request.getCountry()) &&
                "Premier League".equalsIgnoreCase(request.getLeague()) &&
                "Manchester City".equalsIgnoreCase(request.getTeam())) {
            ResponseDTO dto = new ResponseDTO();
            dto.setCountryId("44");
            dto.setCountryName("England");
            dto.setLeagueId("152");
            dto.setLeagueName("Premier League");
            dto.setTeamId("2621");
            dto.setTeamName("Manchester City");
            dto.setPosition("1");
            return dto;
        }
        throw new CustomException("Offline data not found.");
    }
}