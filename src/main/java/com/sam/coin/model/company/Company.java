package com.sam.coin.model.company;

import com.sam.coin.model.CompanyMetadata;
import com.sam.coin.model.util.Exchange;
import com.sam.coin.model.util.Currency;
import com.sam.coin.model.company.exchange.TradeExchange;
import lombok.Value;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Document
@Value
public class Company {
    @Id
    private ObjectId id;
    private String companyName;
    private Map<Exchange,TradeExchange> tradeExchanges;
    private List<CompanyMetadata> companyMetadata;

    public Company(String companyName, Map<Exchange,TradeExchange> tradeExchanges) {
        this.id = ObjectId.get();
        this.companyName = companyName;
        this.tradeExchanges = tradeExchanges;
        this.companyMetadata = new ArrayList<>();
    }

    // Add getters and setters for all fields here
}