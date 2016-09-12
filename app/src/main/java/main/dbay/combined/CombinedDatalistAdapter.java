package main.dbay.combined;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import main.dbay.Calculations;
import main.dbay.MainActivity;
import main.dbay.R;

/**
 * Created by vladvidavsky on 24/04/15.
 */
public class CombinedDatalistAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<CombinedListItemModel> list;

    public CombinedDatalistAdapter(Activity activity, ArrayList<CombinedListItemModel> theList) {
        context = activity;
        list = theList;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.row_jewellery_comb, parent, false);
            vh.thumb = (ImageView) convertView.findViewById(R.id.item_thumb);
            vh.itemId = (TextView) convertView.findViewById(R.id.item_id);
            vh.metal = (TextView) convertView.findViewById(R.id.metal_txt);
            vh.details = (TextView) convertView.findViewById(R.id.details_txt);
            vh.certificate = (TextView) convertView.findViewById(R.id.certificate_txt);
            vh.price = (TextView) convertView.findViewById(R.id.price_txt);
            vh.convertedPrice = (TextView) convertView.findViewById(R.id.conv_price);
            vh.imageBig = (TextView) convertView.findViewById(R.id.image_big_link);
            vh.cameraImage = (TextView) convertView.findViewById(R.id.camera_image);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final CombinedListItemModel clim = list.get(position);
        int resourceId = context.getResources().getIdentifier(clim.getId().toLowerCase() + "_th", "drawable", context.getPackageName());
        vh.thumb.setImageResource(resourceId);
        vh.itemId.setText(clim.getId());
        vh.metal.setText(clim.getMetal());
        vh.details.setText(clim.getDetails());
        vh.certificate.setText(clim.getCertificate());
        vh.price.setText(clim.getPrice().equals("0") ? "по запросу" : Calculations.FormatPrice(clim.getPrice()) + "$");
        vh.imageBig.setText(clim.getImage_big());
        vh.cameraImage.setText(clim.getCam_image());
        if (MainActivity.currencyValue != 0.0 && !clim.getPrice().equals("0")) {
            vh.convertedPrice.setText("(" + Calculations.calculateLocalCurrency(clim.getPrice()) + ")");
        }

        return convertView;
    }

    public class ViewHolder {
        ImageView thumb;
        TextView itemId, metal, details, certificate, price, convertedPrice, imageBig, cameraImage;
    }
}
