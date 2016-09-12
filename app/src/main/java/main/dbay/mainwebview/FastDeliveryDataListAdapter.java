package main.dbay.mainwebview;

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
 * Created by vladvidavsky on 2/05/15.
 */
public class FastDeliveryDataListAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<FastDeliveryListItemModel> list;

    public FastDeliveryDataListAdapter(Activity act, ArrayList<FastDeliveryListItemModel> theList){
        context = act;
        list = theList;
        BitmapManager.INSTANCE.setPlaceholder(BitmapFactory.decodeResource(context.getResources(), R.drawable.image_placeholder));
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
            convertView = inflater.inflate(R.layout.row_fast_delivery, parent, false);
            vh.ffast_image = (ImageView) convertView.findViewById(R.id.fast_image);
            vh.ffast_id = (TextView) convertView.findViewById(R.id.fast_id);
            vh.ffast_description = (TextView) convertView.findViewById(R.id.fast_description);
            vh.ffast_metal = (TextView) convertView.findViewById(R.id.fast_metal);
            vh.ffast_certificate = (TextView) convertView.findViewById(R.id.fast_certificate);
            vh.ffast_price = (TextView) convertView.findViewById(R.id.fast_price);
            vh.ffast_converted_price = (TextView) convertView.findViewById(R.id.fast_converted);

            convertView.setTag(vh);

        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        final FastDeliveryListItemModel fdlim = list.get(position);
        BitmapManager.INSTANCE.loadBitmap(Constants.FAST_DELIVERY_IMAGES_URL + fdlim.getImage(), vh.ffast_image, 400, 280, true);
        vh.ffast_id.setText(fdlim.getId());
        vh.ffast_metal.setText(fdlim.getMetal());
        vh.ffast_description.setText(fdlim.getDescription());
        vh.ffast_certificate.setText(fdlim.getCertificate());
        vh.ffast_price.setText(Calculations.FormatPrice(fdlim.getPrice()) + "$");
        if (MainActivity.currencyValue != 0.0 || !fdlim.getPrice().equals("0")) {
            vh.ffast_converted_price.setText("(" + Calculations.calculateLocalCurrency(fdlim.getPrice()) + ")");
        }
        return convertView;
    }

    public class ViewHolder {
        TextView ffast_id, ffast_metal, ffast_description, ffast_certificate, ffast_price, ffast_converted_price;
        ImageView ffast_image;
    }
}
