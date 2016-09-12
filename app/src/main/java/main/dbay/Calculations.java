package main.dbay;
import android.text.format.DateFormat;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by vladvidavsky on 11/04/15.
 */
public class Calculations {

    public static String FormatPrice(String price){
        double amount = Double.parseDouble(price);
        DecimalFormat formatter = new DecimalFormat("#,###");
        price = formatter.format(amount);
        return price;
    }

    public static String calculateLocalCurrency(String usdAmount){
        Double usdValue = Double.valueOf(usdAmount);
        usdValue = usdValue * MainActivity.currencyValue;
        DecimalFormat formatter = new DecimalFormat("#,###");
        String convertedAmount = formatter.format(usdValue);
        return convertedAmount + MainActivity.currencyAbriviation;
    }

    public static String formatPriceForSQL(String price){
        String cleanPrice = price.replace(",", "").replaceAll("\\s", "");
        cleanPrice = cleanPrice.substring(0,cleanPrice.length()-1);
        return cleanPrice;
    }

    public static boolean checkItemIsFavorite(String id) {
        for (String currentId : MainActivity.favoritesIds) {
            if (currentId.equals(id)) {
                return true;
            }
        }
        return false;
    }

    public static String convertTimestampToDate(String date){
        Long timestamp = Long.valueOf(date);
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        String newDate = DateFormat.format("dd/MM/yyyy", cal).toString();
        return newDate;
    }
}
