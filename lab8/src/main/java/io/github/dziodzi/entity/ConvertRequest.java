package io.github.dziodzi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConvertRequest {

    private String fromCurrency;
    private String toCurrency;
    private Double amount;
}

