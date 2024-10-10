package io.github.dziodzi.controller;

import io.github.dziodzi.entity.ConvertRequest;
import io.github.dziodzi.entity.ConvertResponse;
import io.github.dziodzi.entity.RateResponse;
import io.github.dziodzi.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/currencies")
@Tag(name = "CurrencyController", description = "API for processing currency rates and conversion")
@Log4j2
public class CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/rates/{code}")
    @Operation(summary = "Get currency rate", description = "Returns the current currency rate by code")
    public ResponseEntity<RateResponse> getCurrencyExchangeRate(
            @Valid @PathVariable("code") String code) {

        double currencyValue = currencyService.getValueOfCurrencyByCode(code);
        RateResponse response = new RateResponse(code, currencyValue);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/convert")
    @Operation(summary = "Convert currency", description = "Converts one currency to another based on current rates")
    public ResponseEntity<ConvertResponse> convertCurrency(
            @Valid @RequestBody ConvertRequest request) {

        double convertedAmount = currencyService.convertCurrency(request);
        ConvertResponse response = new ConvertResponse(
                request.getFromCurrency(),
                request.getToCurrency(),
                request.getAmount(),
                convertedAmount
        );

        return ResponseEntity.ok(response);
    }
}
