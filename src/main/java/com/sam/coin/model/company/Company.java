package com.sam.coin.model.company;

import com.mongodb.lang.Nullable;
import com.sam.coin.model.CompanyMetadata;
import com.sam.coin.model.company.exchange.TradeExchange;
import com.sam.coin.model.util.CompanyName;
import com.sam.coin.model.util.Exchange;
import lombok.*;
import lombok.experimental.NonFinal;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Null;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Document
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Company {
    @Id
    ObjectId id;
    CompanyName companyName;
    Map<Exchange, TradeExchange> tradeExchanges;
    Set<CompanyMetadata> companyMetadata;

    public Company(CompanyName companyName) {
        this.id = ObjectId.get();
        this.companyName = companyName;
        this.tradeExchanges = new HashMap<>();
        this.companyMetadata = new HashSet<>();
    }

    public Company(CompanyName companyName, Map<Exchange, TradeExchange> tradeExchanges) {
        this.id = ObjectId.get();
        this.companyName = companyName;
        this.tradeExchanges = tradeExchanges;
        this.companyMetadata = new HashSet<>();
    }

    public Company(CompanyName companyName, Map<Exchange, TradeExchange> tradeExchanges, Set<CompanyMetadata> companyMetadata) {
        this.id = ObjectId.get();
        this.companyName = companyName;
        this.tradeExchanges = tradeExchanges;
        this.companyMetadata = companyMetadata;
    }

}