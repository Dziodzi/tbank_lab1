package io.github.dziodzi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConvertResponse {

    private String fromCurrency;
    private String toCurrency;
    private double amount;
    private double convertedAmount;
}
