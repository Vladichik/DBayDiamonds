package main.dbay.bazar;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.dbay.CallHomeToVerify;
import main.dbay.Constants;
import main.dbay.DialogCustom;
import main.dbay.JSONParserPost;
import main.dbay.MainActivity;
import main.dbay.R;
import main.dbay.ToastCustom;

/**
 * Created by vladvidavsky on 9/15/15.
 */
public class BazarActivity extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private MainActivity mac = new MainActivity();
    private ListView listview = null;
    private Button jewlsTab, stonesTab, watchesTab;
    public static String collectionType;
    private LinearLayout bazarPreloader, filterMenuLayout;
    private BazarStageActivity bazarStage;
    private String[] countries = {"все страны", "Россия", "Украина", "Беларусь", "Литва", "Латвия", "Эстония", "Молдавия", "Грузия", "Армения", "Узбекистан", "Казахстан", "Азербайджан", "Киргизия", "Таджикистан", "Туркменистан"};
    private List<String> cities, items;
    private boolean filterMenuIsOpen;
    Spinner countriesPicker, citiesPicker, itemsPicker;
    private static Activity activ;
    private DialogCustom dc;

    //Техт и Стринги инициализирующиеся при нажатии на list row
    private TextView rowItem, rowAdvertId, rowMetal, rowCentStone, rowShape, rowWeight, rowColor, rowClarity, rowTotal, rowCertif, rowModel, rowBox, rowDocs, rowDescription, rowSellername, rowSellerphone, rowSellermail, rowSkype, rowCity, rowImage1, rowImage2, rowImage3, rowPrice, rowClearPrice;
    private String row_item, row_metal, row_cent_stone, row_shape, row_weight, row_color, row_clarity,
            row_total, row_certif, row_model, row_box, row_docs, row_description, row_sellername, row_sellphone, row_sellmail, row_skype, row_city, row_i1, row_i2, row_i3, row_price,
            row_cprice, filterCountry = "", filterCity = "", filterItem = "";

    public static String  phone_collection, phone_item, phone_metal, phone_centStone, phone_shape, phone_weight, phone_color, phone_clarity, phone_total, phone_certif, phone_model,
            phone_box, phone_docs, phone_desc, phone_sellername, phone_phone, phone_mail, phone_skype, phone_city, phone_i1, phone_i2, phone_i3, phone_price, phone_cprice;
    public static Activity phone_act;
    private JSONObject filterData;

    //Переменные используемые для сбора объекта данных об объявленни и отправки данных на сервер для UPDATEEXPOSURE
    private static final String urlForUpdatingAdvertExposure = Constants.WEBSITE_URL + "/updateexposure";
    JSONObject dataObject = new JSONObject();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //CLEARS PREVIOUS FRAGMENT IF IT EXISTS
        if (container != null) {
            container.removeAllViews();
        }
        View bazarView = inflater.inflate(R.layout.bazar, container, false);
        bazarPreloader = (LinearLayout) bazarView.findViewById(R.id.bazar_preloader);
        filterMenuLayout = (LinearLayout) bazarView.findViewById(R.id.expandable);
        jewlsTab = (Button) bazarView.findViewById(R.id.jewls_tab);
        stonesTab = (Button) bazarView.findViewById(R.id.stones_tab);
        watchesTab = (Button) bazarView.findViewById(R.id.watches_tab);
        Button filterRequestBtn = (Button) bazarView.findViewById(R.id.send_filter_request);
        Button filterMenuToggle = (Button) bazarView.findViewById(R.id.expand_toggle);

        jewlsTab.setOnClickListener(this);
        stonesTab.setOnClickListener(this);
        watchesTab.setOnClickListener(this);
        filterRequestBtn.setOnClickListener(this);
        filterMenuToggle.setOnClickListener(this);

        listview = (ListView) bazarView.findViewById(android.R.id.list);
        new getAdvertsData().execute("jewls");
        jewlsTab.setActivated(true);
        bazarStage = new BazarStageActivity();

        /**
         * Инициализируем дефолтивные параметры фильтра
         */
        getFilterData();
        getCitiesAndCollectionDataForFilter("Россия");
        getItemsListForFilter("jewls");

        countriesPicker = (Spinner) bazarView.findViewById(R.id.country_picker);
        citiesPicker = (Spinner) bazarView.findViewById(R.id.cities_picker);
        itemsPicker = (Spinner) bazarView.findViewById(R.id.item_picker);
        countriesPicker.setAdapter(new SpinnerAdapter(getActivity(), R.layout.row_filter_spinner, countries));
        citiesPicker.setAdapter(new SpinnerAdapterCities(getActivity(), R.layout.row_filter_spinner, cities));
        itemsPicker.setAdapter(new SpinnerAdapterItems(getActivity(), R.layout.row_filter_spinner, items));
        countriesPicker.setOnItemSelectedListener(this);
        citiesPicker.setOnItemSelectedListener(this);
        itemsPicker.setOnItemSelectedListener(this);
        activ = getActivity();
        dc = new DialogCustom(getActivity());
        return bazarView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.jewls_tab:
                performTabChangeTasks("jewls", false);
                jewlsTab.setActivated(true);
                getItemsListForFilter("jewls");
                break;
            case R.id.stones_tab:
                performTabChangeTasks("stones", false);
                stonesTab.setActivated(true);
                getItemsListForFilter("stones");
                break;
            case R.id.watches_tab:
                performTabChangeTasks("watches", true);
                watchesTab.setActivated(true);
                break;
            case R.id.expand_toggle:
                if (filterMenuLayout.getVisibility() == View.GONE) {
                    expandFilterMenu();
                } else {
                    collapseFilterMenu();
                }
                break;
            case R.id.send_filter_request:
                bazarPreloader.setVisibility(View.VISIBLE);
                collapseFilterMenu();
                new getAdvertsData().execute(collectionType);
                break;
        }
    }

    /**
     * Метод выполняющий разные операции по изменению элементов UI после каждого нажатия пользователем
     * на таб Изделия/Камни/Часы. А так же посылает запрос на сервер, чтобы получить нужную коллекцию
     * объявлений.
     *
     * @param itemCollection  стринг используемый для получения нужной коллекции объявлений
     * @param hideItemSpinner булеан для того чтобы прятать/показывать спиннер товаров в меню фильтра
     *                        в зависемости от коллекции.
     */
    private void performTabChangeTasks(String itemCollection, boolean hideItemSpinner) {
        bazarPreloader.setVisibility(View.VISIBLE);
        jewlsTab.setEnabled(false);
        stonesTab.setEnabled(false);
        watchesTab.setEnabled(false);
        jewlsTab.setActivated(false);
        stonesTab.setActivated(false);
        watchesTab.setActivated(false);
        countriesPicker.setSelection(0);
        citiesPicker.setSelection(0);
        itemsPicker.setSelection(0);
        filterCountry = "";
        filterCity = "";
        filterItem = "";
        itemsPicker.setVisibility(hideItemSpinner ? View.GONE : View.VISIBLE);
        collapseFilterMenu();
        new getAdvertsData().execute(itemCollection);
    }

    /**
     * Метод посылающий запрос с параметрами на сервер и получающий список объявлений.
     */
    public class getAdvertsData extends AsyncTask<String, String, JSONArray> {
        String url = "http://d-bay.co.il/gadverts";


        @Override
        protected JSONArray doInBackground(String... params) {
            collectionType = params[0];
            JSONParserPost jParser = new JSONParserPost();
            JSONArray json = jParser.queryRESTurl(url, collectionType, filterCountry, filterCity, filterItem);
            return json;
        }

        @Override
        protected void onPostExecute(JSONArray receivedAdverts) {
            ArrayList<BazarListItemModel> bazarArray = new ArrayList<BazarListItemModel>();
            if (receivedAdverts != null && receivedAdverts.length() > 0) {
                for (int i = 0; i < receivedAdverts.length(); i++) {
                    try {
                        String metal = null, central_stone = null, center = null, shape = null, color = null, clarity = null, total = null, certificate = null, brand = null, model = null, box = null, documents = null;
                        switch (collectionType) {
                            case "jewls":
                                metal = receivedAdverts.getJSONObject(i).getString("metal");
                                central_stone = receivedAdverts.getJSONObject(i).getString("central_stone");
                                center = receivedAdverts.getJSONObject(i).getString("center");
                                shape = receivedAdverts.getJSONObject(i).getString("shape");
                                color = receivedAdverts.getJSONObject(i).getString("color");
                                clarity = receivedAdverts.getJSONObject(i).getString("clarity");
                                total = receivedAdverts.getJSONObject(i).getString("total");
                                certificate = receivedAdverts.getJSONObject(i).getString("certificate");
                                break;
                            case "stones":
                                center = receivedAdverts.getJSONObject(i).getString("stone_weight");
                                shape = receivedAdverts.getJSONObject(i).getString("shape");
                                color = receivedAdverts.getJSONObject(i).getString("color");
                                clarity = receivedAdverts.getJSONObject(i).getString("clarity");
                                certificate = receivedAdverts.getJSONObject(i).getString("certificate");
                                break;
                            case "watches":
                                metal = receivedAdverts.getJSONObject(i).getString("metal");
                                brand = receivedAdverts.getJSONObject(i).getString("brand");
                                model = receivedAdverts.getJSONObject(i).getString("model");
                                box = receivedAdverts.getJSONObject(i).getString("box");
                                documents = receivedAdverts.getJSONObject(i).getString("documents");
                                break;
                        }
                        String item = receivedAdverts.getJSONObject(i).getString("item");
                        String advertid = receivedAdverts.getJSONObject(i).getString("advertid");
                        String dateupdated = receivedAdverts.getJSONObject(i).getString("dateupdated");
                        String description = receivedAdverts.getJSONObject(i).getString("description");
                        String sellername = receivedAdverts.getJSONObject(i).getString("name");
                        String sellerphone = receivedAdverts.getJSONObject(i).getString("phone");
                        String sellermail = receivedAdverts.getJSONObject(i).getString("mail");
                        String sellerskype = receivedAdverts.getJSONObject(i).getString("skype");
                        String sellercountry = receivedAdverts.getJSONObject(i).getString("country");
                        String sellercity = receivedAdverts.getJSONObject(i).getString("city");
                        String price = receivedAdverts.getJSONObject(i).getString("price");
                        String image1 = null, image2 = null, image3 = null;
                        int imglength = receivedAdverts.getJSONObject(i).getJSONArray("images").length();
                        switch (imglength) {
                            case 1:
                                image1 = receivedAdverts.getJSONObject(i).getJSONArray("images").getString(0);
                                break;
                            case 2:
                                image1 = receivedAdverts.getJSONObject(i).getJSONArray("images").getString(0);
                                image2 = receivedAdverts.getJSONObject(i).getJSONArray("images").getString(1);
                                break;
                            case 3:
                                image1 = receivedAdverts.getJSONObject(i).getJSONArray("images").getString(0);
                                image2 = receivedAdverts.getJSONObject(i).getJSONArray("images").getString(1);
                                image3 = receivedAdverts.getJSONObject(i).getJSONArray("images").getString(2);
                                break;
                        }
                        bazarArray.add(new BazarListItemModel(item, dateupdated, metal, central_stone, center, shape, color, clarity, total, certificate, description, sellername, sellerphone, sellermail, sellerskype, sellercountry, sellercity, price, image1, image2, image3, brand, model, box, documents, advertid));
                        /**
                         * Сортировка массива по дате в порядке от новых к старым
                         */
                        Collections.sort(bazarArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                BazarDatalistAdapter bazarAdapter = new BazarDatalistAdapter(getActivity(), bazarArray);
                listview.setAdapter(bazarAdapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        rowItem = (TextView) view.findViewById(R.id.item_name);
                        rowAdvertId = (TextView) view.findViewById(R.id.advert_id);
                        rowDescription = (TextView) view.findViewById(R.id.item_description);
                        rowSellername = (TextView) view.findViewById(R.id.item_sellername);
                        rowSellerphone = (TextView) view.findViewById(R.id.item_sellerphone);
                        rowSellermail = (TextView) view.findViewById(R.id.item_sellermail);
                        rowCity = (TextView) view.findViewById(R.id.item_city);
                        rowSkype = (TextView) view.findViewById(R.id.item_sellerskype);
                        rowImage1 = (TextView) view.findViewById(R.id.item_image1);
                        rowImage1 = (TextView) view.findViewById(R.id.item_image1);
                        rowImage2 = (TextView) view.findViewById(R.id.item_image2);
                        rowImage3 = (TextView) view.findViewById(R.id.item_image3);
                        rowPrice = (TextView) view.findViewById(R.id.item_price);
                        rowClearPrice = (TextView) view.findViewById(R.id.clear_price);
                        row_item = rowItem.getText().toString();
                        row_description = rowDescription.getText().toString();
                        row_sellername = rowSellername.getText().toString();
                        row_sellphone = rowSellerphone.getText().toString();
                        row_sellmail = rowSellermail.getText().toString();
                        row_city = rowCity.getText().toString();
                        row_skype = rowSkype.getText().toString();
                        row_price = rowPrice.getText().toString();
                        row_cprice = rowClearPrice.getText().toString();
                        row_i1 = rowImage1.getText().toString() == "" ? null : rowImage1.getText().toString();
                        row_i2 = rowImage2.getText().toString() == "" ? null : rowImage2.getText().toString();
                        row_i3 = rowImage3.getText().toString() == "" ? null : rowImage3.getText().toString();

                        switch (collectionType) {
                            case "jewls":
                                rowMetal = (TextView) view.findViewById(R.id.item_metal);
                                rowCentStone = (TextView) view.findViewById(R.id.item_central_stone);
                                rowShape = (TextView) view.findViewById(R.id.item_shape);
                                rowWeight = (TextView) view.findViewById(R.id.item_weight);
                                rowColor = (TextView) view.findViewById(R.id.item_color);
                                rowClarity = (TextView) view.findViewById(R.id.item_clarity);
                                rowTotal = (TextView) view.findViewById(R.id.item_total);
                                rowCertif = (TextView) view.findViewById(R.id.item_certificate);
                                row_metal = rowMetal.getText().toString();
                                row_cent_stone = rowCentStone.getText().toString();
                                row_shape = rowShape.getText().toString();
                                row_weight = rowWeight.getText().toString();
                                row_color = rowColor.getText().toString();
                                row_clarity = rowClarity.getText().toString();
                                row_total = rowTotal.getText().toString();
                                row_certif = rowCertif.getText().toString();
                                break;
                            case "stones":
                                rowShape = (TextView) view.findViewById(R.id.item_shape);
                                rowWeight = (TextView) view.findViewById(R.id.item_weight);
                                rowColor = (TextView) view.findViewById(R.id.item_color);
                                rowClarity = (TextView) view.findViewById(R.id.item_clarity);
                                rowCertif = (TextView) view.findViewById(R.id.item_certificate);
                                row_shape = rowShape.getText().toString();
                                row_weight = rowWeight.getText().toString();
                                row_color = rowColor.getText().toString();
                                row_clarity = rowClarity.getText().toString();
                                row_certif = rowCertif.getText().toString();
                                break;
                            case "watches":
                                rowMetal = (TextView) view.findViewById(R.id.item_metal);
                                rowModel = (TextView) view.findViewById(R.id.item_model);
                                rowBox = (TextView) view.findViewById(R.id.item_box);
                                rowDocs = (TextView) view.findViewById(R.id.item_documents);
                                row_metal = rowMetal.getText().toString();
                                row_model = rowModel.getText().toString();
                                row_box = rowBox.getText().toString();
                                row_docs = rowDocs.getText().toString();
                                break;
                        }

                        if (MainActivity.iAmOnTablet) {
                            bazarStage.populateBazarStage(collectionType, getActivity(), row_item, row_metal, row_cent_stone, row_shape, row_weight, row_color, row_clarity, row_total,
                                    row_certif, row_model, row_box, row_docs, row_description, row_sellername, row_sellphone, row_sellmail, row_skype, row_city, row_i1, row_i2, row_i3,
                                    row_price, row_cprice);
                        } else {
                            phone_collection = collectionType;
                            phone_item = row_item;
                            phone_metal = row_metal;
                            phone_centStone = row_cent_stone;
                            phone_shape = row_shape;
                            phone_weight = row_weight;
                            phone_color = row_color;
                            phone_clarity = row_clarity;
                            phone_total = row_total;
                            phone_certif = row_certif;
                            phone_model = row_model;
                            phone_box = row_box;
                            phone_docs = row_docs;
                            phone_desc = row_description;
                            phone_sellername = row_sellername;
                            phone_phone = row_sellphone;
                            phone_mail = row_sellmail;
                            phone_skype = row_skype;
                            phone_city = row_city;
                            phone_i1 = row_i1;
                            phone_i2 = row_i2;
                            phone_i3 = row_i3;
                            phone_price = row_price;
                            phone_cprice = row_cprice;
                            dc.showAdvertDetailsDialog();
                        }

                        /**
                         * Метод собирающий в объект данные нажатого объявления для того, чтобы
                         * отправить на сервер и инкрементировать счетчик показов объявления.
                         */
                        try {
                            dataObject.put("advertid", rowAdvertId.getText().toString()).put("group", collectionType).put("ismob", "true");
                            String postDataString = dataObject.toString();
                            CallHomeToVerify.SendDataHome(getActivity(), urlForUpdatingAdvertExposure, postDataString);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

                listview.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        if (filterMenuIsOpen) {
                            collapseFilterMenu();
                        }
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    }
                });

                if (MainActivity.iAmOnTablet) {
                    listview.performItemClick(listview.getAdapter().getView(0, null, null), 0, listview.getItemIdAtPosition(0));
                }
                bazarPreloader.setVisibility(View.GONE);
                jewlsTab.setEnabled(true);
                stonesTab.setEnabled(true);
                watchesTab.setEnabled(true);
                FlurryAgent.logEvent("Adverts tab selected:" + collectionType);
            } else {
                ToastCustom tc = new ToastCustom(activ);
                tc.showCustomToast(9);
                bazarPreloader.setVisibility(View.GONE);
            }
        }
    }


    /**
     * Метод получающий параметры фильтра из фаила JSON и инициализирующий полученые данные
     * в объект для дальнейшего использования
     */
    private JSONObject getFilterData() {
        String json = null;
        try {
            InputStream is = getActivity().getResources().openRawResource(R.raw.filter_data);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            try {
                filterData = new JSONObject(json);
            } catch (Exception ex) {

            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return filterData;
    }


    /**
     * Метод изменяющий список городов в Пикере в соответствие с изменением выбранной страны
     *
     * @param country стринг Страна параметр по которому выбирается список городов
     */
    private void getCitiesAndCollectionDataForFilter(String country) {
        List<String> temp = new ArrayList<String>();
        try {
            JSONArray cit = filterData.getJSONArray(country);
            for (int i = 0; i < cit.length(); i++) {
                temp.add(cit.getString(i));
            }
            cities = temp;
            /**
             * Метод обновляет список городов в спинере после того как пользователь меняет страну.
             */
            if (citiesPicker != null) {
                citiesPicker.setAdapter(new SpinnerAdapterCities(getActivity(), R.layout.row_filter_spinner, cities));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getItemsListForFilter(String type) {
        List<String> temp = new ArrayList<String>();
        try {
            JSONArray cit = filterData.getJSONArray(type);
            for (int i = 0; i < cit.length(); i++) {
                temp.add(cit.getString(i));
            }
            items = temp;
            /**
             * Метод подставляет нужный список товаров в спинере после того как пользователь переходит
             * таб Изделия/Камни/Часы.
             */
            if (itemsPicker != null) {
                itemsPicker.setAdapter(new SpinnerAdapterItems(getActivity(), R.layout.row_filter_spinner, items));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Event Listner для всех трех спинеров в меню фильтра. Реагирует на изменения выбранного параметра
     * в каждом спинере и вызывает метод строющий объект данных для запроса фильтрования
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int spinerid = parent.getId();
        switch (spinerid) {
            case R.id.country_picker:
                getCitiesAndCollectionDataForFilter(countries[position]);
                filterCountry = countries[position].contains("все страны") ? "" : countries[position];
                filterCity = "";
                break;
            case R.id.cities_picker:
                filterCity = cities.get(position).contains("все города") ? "" : cities.get(position);
                break;
            case R.id.item_picker:
                filterItem = items.get(position).contains("все позиции") ? "" : items.get(position);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    /**
     * ***************************  ALL SPINNER ADAPTERS ***********************************
     */
    private class SpinnerAdapter extends ArrayAdapter<String> {
        public SpinnerAdapter(Context context, int resource, String[] object) {
            super(context, resource, object);
        }

        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getCustomView(position, cnvtView, prnt);

        }

        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater(null);
            View mySpinner = inflater.inflate(R.layout.row_filter_spinner, parent, false);
            TextView spinerCountriesRow = (TextView) mySpinner.findViewById(R.id.spinner_row);
            spinerCountriesRow.setText(countries[position]);
            return mySpinner;
        }
    }

    private class SpinnerAdapterCities extends ArrayAdapter<String> {
        public SpinnerAdapterCities(Context context, int resource, List<String> object) {
            super(context, resource, object);
        }

        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getCustomView(position, cnvtView, prnt);

        }

        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater(null);
            View mySpinner = inflater.inflate(R.layout.row_filter_spinner, parent, false);
            TextView spinerCitiesRow = (TextView) mySpinner.findViewById(R.id.spinner_row);
            spinerCitiesRow.setText(cities.get(position));
            return mySpinner;
        }

    }

    private class SpinnerAdapterItems extends ArrayAdapter<String> {
        public SpinnerAdapterItems(Context context, int resource, List<String> object) {
            super(context, resource, object);
        }

        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getCustomView(position, cnvtView, prnt);

        }

        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater(null);
            View mySpinner = inflater.inflate(R.layout.row_filter_spinner, parent, false);
            TextView spinerCountriesRow = (TextView) mySpinner.findViewById(R.id.spinner_row);
            spinerCountriesRow.setText(items.get(position));
            return mySpinner;
        }
    }


    /**
     * Метод разворачивающий меню фильтра
     */
    private void expandFilterMenu() {
        filterMenuIsOpen = true;
        filterMenuLayout.setVisibility(View.VISIBLE);
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        filterMenuLayout.measure(widthSpec, heightSpec);
        ValueAnimator mAnimator = slideAnimator(0, filterMenuLayout.getMeasuredHeight());
        mAnimator.start();
    }

    /**
     * Метод сворачивающий меню фильтра
     */
    private void collapseFilterMenu() {
        filterMenuIsOpen = false;
        int finalHeight = filterMenuLayout.getHeight();
        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                filterMenuLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }

    /**
     * Метод вычисляющий анимацию скольжения меню
     */
    private ValueAnimator slideAnimator(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = filterMenuLayout.getLayoutParams();
                layoutParams.height = value;
                filterMenuLayout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }
}

