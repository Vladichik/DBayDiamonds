package main.dbay;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by vladvidavsky on 9/04/15.
 */
public class JewelleryDatalistAdapter extends BaseAdapter {
    private Activity context;
    private ArrayList<JewelleryListItemModel> list;

    public JewelleryDatalistAdapter (Activity act, ArrayList<JewelleryListItemModel> theList){
        context = act;
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
        if(convertView == null){
            vh = new ViewHolder();
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.row_jewellery, parent, false);
            vh.thumb = (ImageView)convertView.findViewById(R.id.item_thumb);
            vh.itemId = (TextView)convertView.findViewById(R.id.item_id);
            vh.metal = (TextView)convertView.findViewById(R.id.metal_txt);
            vh.total = (TextView)convertView.findViewById(R.id.total_txt);
            vh.center = (TextView)convertView.findViewById(R.id.center_txt);
            vh.diamColor = (TextView)convertView.findViewById(R.id.color_txt);
            vh.clarity = (TextView)convertView.findViewById(R.id.clarity_txt);
            vh.certificate = (TextView)convertView.findViewById(R.id.certificate_txt);
            vh.price = (TextView)convertView.findViewById(R.id.price_txt);
            vh.convertedPrice = (TextView)convertView.findViewById(R.id.conv_price);
            vh.imageBig = (TextView) convertView.findViewById(R.id.image_big_link);
            vh.cameraImage = (TextView) convertView.findViewById(R.id.camera_image);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        final JewelleryListItemModel jlim = list.get(position);
        int resourceId = context.getResources().getIdentifier(jlim.getItemId().toLowerCase() + "_th", "drawable", context.getPackageName());
        vh.thumb.setImageResource(resourceId);
        vh.itemId.setText(jlim.getItemId());
        vh.metal.setText(jlim.getMetal());
        vh.total.setText(jlim.getTotal().equals("---") ? jlim.getTotal() : jlim.getTotal() + "ct.");
        vh.center.setText(jlim.getCenter().equals("---") ? jlim.getCenter() : jlim.getCenter() + "ct.");
        vh.diamColor.setText(jlim.getDiamColor());
        vh.clarity.setText(jlim.getClarity());
        vh.certificate.setText(jlim.getCertificate());
        vh.price.setText(jlim.getPrice().equals("0") ? "по запросу" : Calculations.FormatPrice(jlim.getPrice()) + "$");
        vh.imageBig.setText(jlim.getImage());
        vh.cameraImage.setText(jlim.getCam_image());
        if (MainActivity.currencyValue != 0.0 && !jlim.getPrice().equals("0")) {
           vh.convertedPrice.setText("(" + Calculations.calculateLocalCurrency(jlim.getPrice()) + ")");
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView thumb;
        TextView itemId, metal, total, center, diamColor, clarity, certificate, price, convertedPrice, imageBig, cameraImage;
    }
}
