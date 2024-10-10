package io.github.dziodzi.service;

import io.github.dziodzi.entity.ConvertRequest;
import io.github.dziodzi.exception.ConvertingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.text.DecimalFormat;

@Service
@Slf4j
@Validated
public class CurrencyService {
    private final ApiService apiService;
    private final CurrencyParserService parserService;
    private final DecimalFormat df = new DecimalFormat("###.###");

    @Autowired
    public CurrencyService(ApiService apiService, CurrencyParserService parserService) {
        this.apiService = apiService;
        this.parserService = parserService;
    }

    public double getValueOfCurrencyByCode(String code) {

        String currencyData;
        try {
            currencyData = apiService.fetchCurrencyDataWithCode(code);
        } catch (Exception e) {
            throw new ConvertingException(e.getMessage());
        }
        String result = parserService.getCurrencyValueByCode(code, currencyData);
        log.info("Currency code {}, value {}", code, result);
        return parseCurrencyValue(result);
    }

    public double convertCurrency(ConvertRequest request) {
        validateRequest(request);

        String currencyData;
        try {
            currencyData = apiService.fetchCurrencyDataWithRequest(request);
        } catch (Exception e) {
            throw new ConvertingException(e.getMessage());
        }
        log.info("Successfully converted currency from {}, {}, {}", request.getFromCurrency(), request.getToCurrency(), request.getAmount());
        return calculateConversion(request, currencyData);
    }

    private void validateRequest(ConvertRequest request) {
        if (request.getFromCurrency() == null) {
            throw new ConvertingException("From currency cannot be null!");
        }
        if (request.getToCurrency() == null) {
            throw new ConvertingException("To currency cannot be null!");
        }
    }

    private double parseCurrencyValue(String value) {
        return Double.parseDouble(value.replace(",", "."));
    }

    private double calculateConversion(ConvertRequest request, String currencyData) {
        double amount = request.getAmount();
        double fromValue = parseCurrencyValue(parserService.getCurrencyValueByCode(request.getFromCurrency(), currencyData));

        if ("RUB".equals(request.getToCurrency())) {
            return fromValue * amount;
        } else {
            double toValue = parseCurrencyValue(parserService.getCurrencyValueByCode(request.getToCurrency(), currencyData));
            return Double.parseDouble(df.format((fromValue / toValue) * amount).replace(",", "."));
        }
    }
}
