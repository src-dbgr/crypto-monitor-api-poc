package com.sam.coin.service;

import com.sam.coin.model.company.exchange.currency.candle.Candle;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CsvService {

    private String formatNumber(double number) {
        double absNumber = Math.abs(number);
        // if absNumber is greater or equal than 10000000 OR
        // if absNumber is greater than 0 and less than or equal to 1e-7 (0.0000001)
        // write it in scientific notation
        if (absNumber >= 1e7 || (absNumber > 0 && absNumber <= 1e-7)) {
            return String.format("%e", number);
        } else {
            return String.format("%f", number);
        }
    }

    public String convertCandleDataToCSV(Set<Candle> candles) {
        StringBuilder csv = new StringBuilder("index,timestamp,open,high,low,close,volume\n");

        for (Candle candle : candles) {
            List<Number> data = candle.getCandleValues();
            if (data.size() == 6) {
                csv.append(String.format("%d,%d,%s,%s,%s,%s,%s%n",
                        candle.getIndex(),
                        data.get(0).longValue(),
                        formatNumber(data.get(1).doubleValue()),
                        formatNumber(data.get(2).doubleValue()),
                        formatNumber(data.get(3).doubleValue()),
                        formatNumber(data.get(4).doubleValue()),
                        formatNumber(data.get(5).doubleValue())));
                continue;
            }
            csv.append(String.format("%d,%d,%s,%s,%s,%s%n",
                    candle.getIndex(),
                    data.get(0).longValue(),
                    formatNumber(data.get(1).doubleValue()),
                    formatNumber(data.get(2).doubleValue()),
                    formatNumber(data.get(3).doubleValue()),
                    formatNumber(data.get(4).doubleValue())));
        }

        return csv.toString();
    }
}
