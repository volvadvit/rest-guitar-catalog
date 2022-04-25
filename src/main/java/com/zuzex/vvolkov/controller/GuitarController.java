package com.zuzex.vvolkov.controller;

import com.zuzex.vvolkov.components.UserAssembler;
import com.zuzex.vvolkov.constants.ResponseMapper;
import com.zuzex.vvolkov.model.guitar.Features;
import com.zuzex.vvolkov.model.guitar.Guitar;
import com.zuzex.vvolkov.model.guitar.Manufacturer;
import com.zuzex.vvolkov.model.user.AppUser;
import com.zuzex.vvolkov.repositories.GuitarRepo;
import com.zuzex.vvolkov.service.FeaturesService;
import com.zuzex.vvolkov.service.GuitarService;
import com.zuzex.vvolkov.service.ManufacturerService;
import com.zuzex.vvolkov.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/guitar")
@RequiredArgsConstructor
public class GuitarController {

    private final GuitarRepo guitarRepo;
    private final GuitarService guitarService;
    private final FeaturesService featureService;
    private final ManufacturerService manufacturerService;

    private final UserService userService;
    private final UserAssembler userAssembler;

    @PostMapping("/add")
    public ResponseEntity<ResponseMapper> addNewGuitar(
            @RequestBody @Valid Guitar guitar)
    {
        guitar = guitarService.validate(guitar);
        Features features = featureService.validate(guitar.getFeatures());

        Manufacturer manufacturer = guitar.getManufacturer();
        manufacturer = manufacturerService.add(manufacturer);

        return ResponseEntity.ok().body(
                new ResponseMapper(HttpStatus.CREATED.value(),
                        "guitar " + guitar.getModel() + " added",
                        guitarService.add(
                                new Guitar(guitar.getModel(), guitar.getYear(),
                                        guitar.getPrice(), guitar.getPhotoUrl(), guitar.getDescription(),
                                        features, guitar.getType(), guitar.getDesign(), manufacturer)))
        );
    }


    @GetMapping("/search")
    public @ResponseBody ResponseEntity<ResponseMapper> searchGuitarsByManufacturer(
            @RequestParam(name = "model", required = false) String model,
            @RequestParam(name = "price_from", required = false) Double priceFrom,
            @RequestParam(name = "price_to", required = false) Double priceTo,
            @RequestParam(name = "year_from", required = false) Integer yearFrom,
            @RequestParam(name = "year_to", required = false) Integer yearTo,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "design", required = false) String design,
            @RequestParam(name = "body", required = false) String body,
            @RequestParam(name = "colour", required = false) String colour,
            @RequestParam(name = "fretsNumber_from", required = false) Integer fretsNumberFrom,
            @RequestParam(name = "fretsNumber_to", required = false) Integer fretsNumberTo,
            @RequestParam(name = "stringsNumber_from", required = false) Integer stringsNumberFrom,
            @RequestParam(name = "stringsNumber_to", required = false) Integer stringsNumberTo,
            @RequestParam(name = "pickupsNumber_from", required = false) Integer pickupsNumberFrom,
            @RequestParam(name = "pickupsNumber_to", required = false) Integer pickupsNumberTo,
            @RequestParam(name = "manufacturer", required = false) String manufacturer
    ) {
        return ResponseEntity.ok().body(
                new ResponseMapper(HttpStatus.OK.value(),
                        "list of found guitars", guitarService.searchByParameters(
                        model, priceFrom, priceTo, yearFrom, yearTo,
                        type, design, body, colour, fretsNumberFrom, fretsNumberTo, stringsNumberFrom,
                        stringsNumberTo, pickupsNumberFrom, pickupsNumberTo, manufacturer)));
    }

    @GetMapping("/{guitar_id}")
    public ResponseEntity<ResponseMapper> show(@PathVariable("guitar_id") Long id) {
        return ResponseEntity.ok().body(new ResponseMapper(HttpStatus.OK.value(),
                "guitar found", guitarService.getById(id)));
    }

    @GetMapping("/all")
    public @ResponseBody ResponseEntity<ResponseMapper> getAllGuitars() {
        return ResponseEntity.ok().body(new ResponseMapper(HttpStatus.OK.value(),
                "list of all guitars", guitarRepo.findAll()));
    }

    @GetMapping("/compare/{guitar_ids}")
    public ResponseEntity<ResponseMapper> compareMany(
            @PathVariable("guitar_ids") List<Long> guitarIds)
    {
        return ResponseEntity.ok(new ResponseMapper(HttpStatus.OK.value(),
                "list of diff", guitarService.compareGuitars(guitarIds)));
    }

    @GetMapping("/{guitar_id}/users")
    public ResponseEntity<ResponseMapper> getGuitarUsersById(@PathVariable("guitar_id") Long guitarId) {

        List<AppUser> userList = guitarService.findUsersByGuitarId(guitarId);

        if (userList != null) {
            userList = userService.addAllUsers(userList).stream().map(userAssembler::toUserVO).collect(Collectors.toList());
        }

        return ResponseEntity.ok(new ResponseMapper(HttpStatus.OK.value(),
                "users list of guitar: " + guitarId, userList));
    }
}
