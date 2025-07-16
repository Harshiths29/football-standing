package com.FootbalStanding.Footbal.Standing.controller;

import com.FootbalStanding.Footbal.Standing.dto.RequestDTO;
import com.FootbalStanding.Footbal.Standing.dto.ResponseDTO;
import com.FootbalStanding.Footbal.Standing.dto.ToggleModeResponse;
import com.FootbalStanding.Footbal.Standing.service.FootballService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@RestController
@RequestMapping("/api/football")
@CrossOrigin(origins = "*")
public class FootballController {

    @Autowired
    private FootballService footballService;

    @PostMapping("/standings")
    public CollectionModel<EntityModel<ResponseDTO>> getStandings(@RequestBody RequestDTO request) {
        List<ResponseDTO> responseList = footballService.fetchStandings(request);

        List<EntityModel<ResponseDTO>> entities = responseList.stream()
                .map(resp -> EntityModel.of(resp,
                        linkTo(FootballController.class).slash("standings").withSelfRel(),
                        linkTo(FootballController.class).slash("toggle-mode").withRel("toggle-mode")))
                .collect(Collectors.toList());

        return CollectionModel.of(entities);
    }
    @GetMapping("/toggle-mode")
    public String toggleMode() {
        return footballService.toggleOffline();
    }
}