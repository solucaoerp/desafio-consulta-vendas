package com.devsuperior.dsmeta.dtos;

public class SellerSalesSummaryDTO {

    private String sellerName;
    private Double total;

    public SellerSalesSummaryDTO(String sellerName, Double total) {
        this.sellerName = sellerName;
        this.total = total;
    }

    public String getSellerName() {
        return sellerName;
    }

    public Double getTotal() {
        return total;
    }
}
