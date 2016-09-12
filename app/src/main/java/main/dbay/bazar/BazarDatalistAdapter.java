package main.dbay.bazar;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import main.dbay.BitmapManager;
import main.dbay.Calculations;
import main.dbay.Constants;
import main.dbay.MainActivity;
import main.dbay.R;

/**
 * Created by vladvidavsky on 9/18/15.
 */
public class BazarDatalistAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<BazarListItemModel> list;

    public BazarDatalistAdapter(Activity act, ArrayList<BazarListItemModel> theList) {
        context = act;
        list = theList;
        BitmapManager.INSTANCE.setPlaceholder(BitmapFactory.decodeResource(act.getResources(), R.drawable.hourglass));
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder vh;
        if(convertView == null){
            vh = new ViewHolder();
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.row_bazar, viewGroup, false);
            vh.rowImage = (ImageView)convertView.findViewById(R.id.row_image);
            vh.item = (TextView)convertView.findViewById(R.id.item_name);
            vh.advertId = (TextView)convertView.findViewById(R.id.advert_id);
            vh.dateupdated = (TextView)convertView.findViewById(R.id.item_date);
            vh.city = (TextView)convertView.findViewById(R.id.item_city);
            vh.price = (TextView)convertView.findViewById(R.id.item_price);
            vh.convertedPrice = (TextView)convertView.findViewById(R.id.conv_price);

            vh.metal = (TextView)convertView.findViewById(R.id.item_metal);
            vh.central_stone = (TextView)convertView.findViewById(R.id.item_central_stone);
            vh.shape = (TextView)convertView.findViewById(R.id.item_shape);
            vh.weight = (TextView)convertView.findViewById(R.id.item_weight);
            vh.color = (TextView)convertView.findViewById(R.id.item_color);
            vh.clarity = (TextView)convertView.findViewById(R.id.item_clarity);
            vh.total = (TextView)convertView.findViewById(R.id.item_total);
            vh.certificate = (TextView)convertView.findViewById(R.id.item_certificate);

            vh.model = (TextView)convertView.findViewById(R.id.item_model);
            vh.box = (TextView)convertView.findViewById(R.id.item_box);
            vh.documents = (TextView)convertView.findViewById(R.id.item_documents);
            vh.description = (TextView)convertView.findViewById(R.id.item_description);
            vh.sellername = (TextView)convertView.findViewById(R.id.item_sellername);
            vh.sellerphone = (TextView)convertView.findViewById(R.id.item_sellerphone);
            vh.sellermail = (TextView)convertView.findViewById(R.id.item_sellermail);
            vh.sellerskype = (TextView)convertView.findViewById(R.id.item_sellerskype);

            vh.image1 = (TextView)convertView.findViewById(R.id.item_image1);
            vh.image2 = (TextView)convertView.findViewById(R.id.item_image2);
            vh.image3 = (TextView)convertView.findViewById(R.id.item_image3);

            vh.clearPrice = (TextView)convertView.findViewById(R.id.clear_price);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        final BazarListItemModel blim = list.get(i);
        switch (BazarActivity.collectionType) {
            case "jewls":
                vh.item.setText(blim.getItem());
                vh.metal.setText(blim.getMetal());
                vh.central_stone.setText(blim.getCentral_stone());
                vh.shape.setText(blim.getShape());
                vh.weight.setText(blim.getCenter());
                vh.color.setText(blim.getColor());
                vh.clarity.setText(blim.getClarity());
                vh.total.setText(blim.getTotal());
                vh.certificate.setText(blim.getCertificate());
                break;
            case "stones":
                vh.item.setText(blim.getItem());
                vh.shape.setText(blim.getShape());
                vh.weight.setText(blim.getCenter());
                vh.color.setText(blim.getColor());
                vh.clarity.setText(blim.getClarity());
                vh.certificate.setText(blim.getCertificate());
                break;
            case "watches":
                vh.item.setText(blim.getBrand());
                vh.metal.setText(blim.getMetal());
                vh.model.setText(blim.getModel());
                vh.box.setText(blim.getBox());
                vh.documents.setText(blim.getDocument());
                break;
        }

        vh.advertId.setText(blim.getAdvertid());
        vh.dateupdated.setText(Calculations.convertTimestampToDate(blim.getDateupdated()));
        vh.city.setText(blim.getSellercity());
        vh.price.setText(blim.getPrice().equals("0") ? "по запросу" : Calculations.FormatPrice(blim.getPrice()) + "$");
        vh.clearPrice.setText(blim.getPrice());
        if (MainActivity.currencyValue != 0.0 && !blim.getPrice().equals("0")) {
            vh.convertedPrice.setText("(" + Calculations.calculateLocalCurrency(blim.getPrice()) + ")");
        }
        BitmapManager.INSTANCE.loadBitmap(blim.getImage1(), vh.rowImage, 180, 150, true);
        vh.description.setText(blim.getDescription());
        vh.sellername.setText(blim.getSellername());
        vh.sellermail.setText(blim.getSellermail());
        vh.sellerphone.setText(blim.getSellerphone() == "" ? "---" : blim.getSellerphone());
        vh.sellerskype.setText(blim.getSellerskype() == "" ? "---" : blim.getSellerskype());
        vh.image1.setText(blim.getImage1() == "" ? "" : blim.getImage1());
        vh.image2.setText(blim.getImage2() == "" ? "" : blim.getImage2());
        vh.image3.setText(blim.getImage3() == "" ? "" : blim.getImage3());

        if(blim.getImage1() == null){
            int resourceId = context.getResources().getIdentifier("missphoto", "drawable", context.getPackageName());
            vh.rowImage.setImageResource(resourceId);
        }

        return convertView;
    }

    private class ViewHolder {
        TextView item, dateupdated, city, price, convertedPrice , metal, central_stone, shape, weight, color, clarity, total, certificate, model, box, documents, description, sellername, sellerphone, sellermail, sellerskype, image1, image2, image3, clearPrice, advertId;
        ImageView rowImage;
    }

}