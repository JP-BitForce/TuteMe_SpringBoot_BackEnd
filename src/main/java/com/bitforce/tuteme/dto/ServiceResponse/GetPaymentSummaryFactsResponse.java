package com.bitforce.tuteme.dto.ServiceResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPaymentSummaryFactsResponse {
    private String plan;
    private List<String> bankAccounts;
    private List<Card> cards;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Card {
        private Long id;
        private Long cardNo;
        private String type;
    }
}
