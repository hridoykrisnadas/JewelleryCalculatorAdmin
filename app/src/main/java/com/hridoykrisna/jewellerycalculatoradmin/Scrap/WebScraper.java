package com.hridoykrisna.jewellerycalculatoradmin.Scrap;

import android.os.AsyncTask;
import android.util.Log;

import com.hridoykrisna.jewellerycalculatoradmin.LivePrice;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebScraper {

    static int gold_gram_22;
    static int gold_gram_21;
    static int gold_gram_18;
    static int gold_traditional;

    static int silver_gram_22;
    static int silver_gram_21;
    static int silver_gram_18;
    static int silver_traditional;

    public static LivePrice runTask() {
        LivePrice livePrice1 = new LivePrice();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                String url = "https://www.bajus.org/gold-price";
                Document doc = Jsoup.connect(url).get();
//                System.out.println(doc.html()); // Print the entire HTML to check if it's loaded

                // Extract the gold table
                Element goldTable = doc.selectFirst("table.gold-table");
                if (goldTable != null) {
                    System.out.println("Gold Table Data:");
                    LivePrice livePrice2 = extractGoldTableData(goldTable);
                    livePrice1.setGold_gram_22(livePrice2.getGold_gram_22());
                    livePrice1.setGold_gram_21(livePrice2.getGold_gram_21());
                    livePrice1.setGold_gram_18(livePrice2.getGold_gram_18());
                    livePrice1.setGold_gram_sonaton(livePrice2.getGold_gram_sonaton());
                }

                // Extract the silver table
                Element silverTable = doc.selectFirst("table.silver-table");
                if (silverTable != null) {
                    System.out.println("\nSilver Table Data:");
                    LivePrice livePrice2 = extractSiverTableData(silverTable);
                    livePrice1.setSilver_gram_22(livePrice2.getSilver_gram_22());
                    livePrice1.setSilver_gram_21(livePrice2.getSilver_gram_21());
                    livePrice1.setSilver_gram_18(livePrice2.getSilver_gram_18());
                    livePrice1.setSilver_gram_sonaton(livePrice2.getSilver_gram_sonaton());
                }


                Element field = doc.select("css-selector-for-field").first();
                String result = field != null ? field.text() : null;

                if (result != null) {
                    Log.d("WebScrapData", "WebScrapData: "+result);
                } else {
                    Log.d("WebScrapData", "WebScrapData: "+result);

                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("WebScrapData", "WebScrapData Catch: "+e.getMessage());
            }
        });
        return livePrice1;
    }

    private static LivePrice extractSiverTableData(Element silverTable) {
        LivePrice livePrice = new LivePrice();
        // Extract table rows
        Elements rows = silverTable.select("tbody > tr");
        for (Element row : rows) {
            // Extract individual columns
            String product = row.select("th").text(); // Product name
            String description = row.select("td.align-middle > p").text(); // Description
            String price = row.select("td.text-center > span.price").text(); // Price

            // Print the extracted data
            System.out.println("Product: " + product);
            System.out.println("Description: " + description);
            System.out.println("Price: " + price);
            System.out.println();

            if (product.equals("22 KARAT Silver")){
                silver_gram_22 = PriceConverter(price);
                livePrice.setSilver_gram_22(silver_gram_22);
            }
            if (product.equals("21 KARAT Silver")){
                silver_gram_21 = PriceConverter(price);
                livePrice.setSilver_gram_21(silver_gram_21);
            }
            if (product.equals("18 KARAT Silver")){
                silver_gram_18 = PriceConverter(price);
                livePrice.setSilver_gram_18(silver_gram_18);
            }
            if (product.equals("TRADITIONAL Silver")){
                silver_traditional = PriceConverter(price);
                livePrice.setSilver_gram_sonaton(silver_traditional);
            }
        }
        return livePrice;
    }

    private static LivePrice extractGoldTableData(Element goldTable) {
        LivePrice livePrice = new LivePrice();
        // Extract table rows
        Elements rows = goldTable.select("tbody > tr");
        for (Element row : rows) {
            // Extract individual columns
            String product = row.select("th").text(); // Product name
            String description = row.select("td.align-middle > p").text(); // Description
            String price = row.select("td.text-center > span.price").text(); // Price

            // Print the extracted data
            System.out.println("Product: " + product);
            System.out.println("Description: " + description);
            System.out.println("Price: " + price);
            System.out.println();

            if (product.equals("22 KARAT Gold")){
                gold_gram_22 = PriceConverter(price);
                livePrice.setGold_gram_22(gold_gram_22);
            }
            if (product.equals("21 KARAT Gold")){
                gold_gram_21 = PriceConverter(price);
                livePrice.setGold_gram_21(gold_gram_21);
            }
            if (product.equals("18 KARAT Gold")){
                gold_gram_18 = PriceConverter(price);
                livePrice.setGold_gram_18(gold_gram_18);
            }
            if (product.equals("TRADITIONAL Gold")){
                gold_traditional = PriceConverter(price);
                livePrice.setGold_gram_sonaton(gold_traditional);
            }
        }
        return livePrice;
    }

    public static int PriceConverter(String price){
        String numericPart = price.replaceAll("[^0-9,]", "");

        // Step 2: Remove the comma
        numericPart = numericPart.replace(",", "");

        // Output the result
        System.out.println("Converted Price: " + Integer.parseInt(numericPart));

        // Step 3: Convert to integer
        return Integer.parseInt(numericPart);
    }




}

