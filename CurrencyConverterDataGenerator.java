package com.example.fx;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Random;

public class CurrencyConverterDataGenerator {
    public static void main(String args[]) throws Exception {
        String[] currencies = new String[]{"USD", "EUR", "SGD", "JPY", "INR", "RMB", "CHF"};
        String header = "FromCurrency, ToCurrency, FromAmount";
        PrintWriter writer = new PrintWriter(new FileWriter(new File("/home/debal/work/converter-data-billion.csv")));
        writer.println(header);
        for (int i = 0; i < 100000000; i++) {
            Random rand = new Random();
            StringBuilder builder = new StringBuilder();
            builder.append(currencies[rand.nextInt(7)]).append(",").append(currencies[rand.nextInt(7)]).append(",")
                    .append(rand.nextDouble());
            writer.println(builder.toString());
        }
        writer.close();
    }
}
