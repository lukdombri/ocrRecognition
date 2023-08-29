package pl.budgetapp.ocrrecognition.reader.dto;

import java.time.LocalDate;

public record Receipt (
        String shop,
        LocalDate purchaseDate,
        Double totalPrice
){
}
