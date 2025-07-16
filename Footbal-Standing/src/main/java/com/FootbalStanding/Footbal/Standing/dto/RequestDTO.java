package com.FootbalStanding.Footbal.Standing.dto;

import lombok.Data;


public class RequestDTO {
    private String country;
    private String league;
    private String team;

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getLeague() { return league; }
    public void setLeague(String league) { this.league = league; }

    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }
}