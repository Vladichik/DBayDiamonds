package main.dbay.diamonds;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.ArrayList;
import main.dbay.R;
import main.dbay.ToastCustom;

/**
 * Created by vladvidavsky on 5/04/15.
 */
public class DiamondsDataListAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<DiamondsListItemModel> list;
    boolean[] checkboxState;
    DiamondsActivity dAct;
    ToastCustom tc;

    public DiamondsDataListAdapter(Activity act, ArrayList<DiamondsListItemModel> theList) {
        context = act;
        list = theList;
        checkboxState = new boolean[theList.size()];
        dAct = new DiamondsActivity();
        dAct.pickedDiamondsArray = new ArrayList();
        tc = new ToastCustom(context);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.row_diamonds, parent, false);
            vh.shape = (TextView) convertView.findViewById(R.id.diam_shape);
            vh.weight = (TextView) convertView.findViewById(R.id.diam_weight);
            vh.color = (TextView) convertView.findViewById(R.id.diam_color);
            vh.clarity = (TextView) convertView.findViewById(R.id.diam_clarity);
            vh.polish = (TextView) convertView.findViewById(R.id.diam_polish);
            vh.symmetry = (TextView) convertView.findViewById(R.id.diam_symmetry);
            vh.fluoricence = (TextView) convertView.findViewById(R.id.diam_flur);
            vh.certificate = (TextView) convertView.findViewById(R.id.diam_certificate);
            vh.dimmentions = (TextView) convertView.findViewById(R.id.diam_dimmensions);
            vh.rowChecker = (CheckBox) convertView.findViewById(R.id.row_checker);
            convertView.setTag(vh);


        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final DiamondsListItemModel dlim = list.get(position);
        vh.shape.setText(dlim.getShape());
        vh.weight.setText(dlim.getWeight() + "ct.");
        vh.color.setText(dlim.getColor());
        vh.clarity.setText(dlim.getClarity());
        vh.polish.setText(dlim.getPolish());
        vh.symmetry.setText(dlim.getPolish());
        vh.fluoricence.setText(dlim.getFluoricence());
        vh.certificate.setText(dlim.getCertificate());
        vh.dimmentions.setText(dlim.getDimmentions());
        vh.rowChecker.setChecked(checkboxState[position]);


        vh.rowChecker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    if (dAct.pickedDiamondsArray.size() < 5) {
                        checkboxState[position] = true;
                        String diamondData = dlim.getShape() + " " + dlim.getWeight() + " " + dlim.getColor() + " " + dlim.getClarity() + " " + dlim.getPolish() + " " + dlim.getSymmetry() + " " + dlim.getFluoricence() + " " + dlim.getDimmentions();
                        dAct.pickedDiamondsArray.add(diamondData);
                        //System.out.println(dAct.pickedDiamondsArray);
                    }else{
                        ((CheckBox) v).setChecked(false);
                        tc.showCustomToast(2);
                    }
                } else {
                    String diamToRemove = dlim.getShape() + " " + dlim.getWeight() + " " + dlim.getColor() + " " + dlim.getClarity() + " " + dlim.getPolish() + " " + dlim.getSymmetry() + " " + dlim.getFluoricence() + " " + dlim.getDimmentions();
                    checkboxState[position] = false;
                    dAct.pickedDiamondsArray.remove(diamToRemove);
                }
            }
        });

        return convertView;
    }

    public class ViewHolder {
        TextView shape, weight, color, clarity, polish, symmetry, fluoricence, certificate, dimmentions;
        CheckBox rowChecker;
    }
}
