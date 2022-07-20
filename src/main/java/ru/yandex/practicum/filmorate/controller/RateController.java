package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rate;
import ru.yandex.practicum.filmorate.model.RateName;
import ru.yandex.practicum.filmorate.service.RateService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class RateController {
    private final RateService rateService;

    @Autowired
    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    @GetMapping
    public List<Rate> getAllRates() {
        return rateService.getAllRates();
    }

    @GetMapping("/{id}")
    public Rate getRateById(@PathVariable int id) {
        return rateService.getRateById(id);
    }
}