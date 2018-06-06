package revenue_express.citythai.adapter;

/**
 * Created by chaleamsuk on 12/25/2016.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import revenue_express.citythai.activity.DishDetailsActivity;
import revenue_express.citythai.R;
import revenue_express.citythai.dao.OptionDataDao;
import revenue_express.citythai.model.OptionAddExtraList;
import revenue_express.citythai.model.OptionList;

import static revenue_express.citythai.activity.DishDetailsActivity.setTv_price_total;
import static revenue_express.citythai.activity.SplashScreen.ColorPrimary;
import static revenue_express.citythai.activity.SplashScreen.TextColorPrimary;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.MyViewHolder>{

    ArrayList<OptionDataDao> list;
    Context context;
    Realm realm;
    public static JSONArray jsonArrayOption = null;
    public static JSONObject jsonObjectOptionSize = null;
    private ArrayList<String> arrayList_option = null;
    String option_id_item,option_name_item,Option_ID,type;
    Double option_price;
    Double priceA = 0.00;

    public OptionAdapter(DishDetailsActivity context, ArrayList<OptionDataDao> optionDataDaoArrayList) {
        this.list = optionDataDaoArrayList;
        this.context = context;
    }

    @Override
    public OptionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.option_card_layout, parent, false);

        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(config);

        return new OptionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OptionAdapter.MyViewHolder holder, int position) {

        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                context.getResources().getString(R.string.SukhumvitSet_Medium));

        holder.tv_option.setTypeface(tf);

        holder.card_layout.setBackgroundColor(Color.parseColor(ColorPrimary));
        holder.spn_option.setVisibility(View.VISIBLE);
        arrayList_option = new ArrayList<String>();
        arrayList_option.clear();
        Option_ID = list.get(position).getOption_id();
        type = list.get(position).getOption_type();
        holder.tv_option.setText(list.get(position).getOption_name());
        String option_default = list.get(position).getOption_default();
        int position_default = 0;
        jsonArrayOption = null;
        jsonArrayOption = list.get(position).getJsonArray();
        final List<String> stringList=new ArrayList<>();
//        stringList.clear();

        for (int i = 0;i<jsonArrayOption.length();i++){
            try {
                jsonObjectOptionSize = jsonArrayOption.getJSONObject(i);
                String mID = jsonObjectOptionSize.getString("id");
                if (mID.equals(option_default)){
                    position_default = i;
                }
                stringList.add(jsonObjectOptionSize.getString("name")+"   + $"+jsonObjectOptionSize.getString("price")+","+Option_ID+","+jsonObjectOptionSize.getString("id")+","+jsonObjectOptionSize.getString("name")+","+jsonObjectOptionSize.getString("price"));
//                list.get(position).getOption_name()+" "+
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        List<String> name_option_List=new ArrayList<>();
        final List<String> id_option_List=new ArrayList<>();
        final List<String> item_option_id_List=new ArrayList<>();
        final List<String> item_option_name_List=new ArrayList<>();
        final List<String> item_option_price_List=new ArrayList<>();

        for(int i=0;i<stringList.size();i++){
            String CurrentString = stringList.get(i);
            String[] separated = CurrentString.split(",");

            separated[0] = separated[0].trim();
            separated[1] = separated[1].trim();
            separated[2] = separated[2].trim();
            separated[3] = separated[3].trim();

            String tital = separated[0];
            String op_id = separated[1];
            String item_op_id = separated[2];
            String item_op_name = separated[3];
            String item_op_price = separated[4];

            name_option_List.add(tital);
            id_option_List.add(op_id);
            item_option_id_List.add(item_op_id);
            item_option_name_List.add(item_op_name);
            item_option_price_List.add(item_op_price);
        }

        if (type .equals("0")){
            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, name_option_List);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(R.layout.spinner_size_item);

            // attaching data adapter to spinner
            holder.spn_option.setAdapter(dataAdapter);
            holder.spn_option.setSelection(position_default);

            holder.spn_option.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    updateNewCard(id_option_List.get(position),item_option_id_List.get(position),item_option_name_List.get(position),item_option_price_List.get(position),type);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }else {

            holder.spn_option.setVisibility(View.INVISIBLE);

            for (int i = 0; i < name_option_List.size(); i++) {
                CheckBox checkedTextView=new CheckBox(context);
                checkedTextView.setText(name_option_List.get(i));
                checkedTextView.setTextColor(Color.parseColor(TextColorPrimary));
                checkedTextView.setTextSize(22);
                checkedTextView.setId(i);
                holder.checkbox_layout.addView(checkedTextView);
                final int finalI = i;
//                checkedTextView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //is chkIos checked?
//                        if (((CheckBox) v).isChecked()) {
//                            //Case 1
//                            final RealmResults<OptionAddExtraList> toEdit = realm.where(OptionAddExtraList.class).equalTo("item_id",String.valueOf(item_option_id_List.get(finalI))).findAll();
//                            toEdit.deleteLastFromRealm();
////                            realm.commitTransaction();
//                            executeRealmOptionAddExtra(id_option_List.get(finalI),item_option_id_List.get(finalI),item_option_price_List.get(finalI));
//                        }
//                        else{
//                            //case 2
//                            final RealmResults<OptionAddExtraList> toEdit = realm.where(OptionAddExtraList.class).equalTo("item_id",String.valueOf(item_option_id_List.get(finalI))).findAll();
//                            toEdit.deleteLastFromRealm();
//                        }
//
//                    }
//                });
                checkedTextView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        RealmResults<OptionAddExtraList> toEdit = realm.where(OptionAddExtraList.class).equalTo("item_id",String.valueOf(item_option_id_List.get(finalI))).findAll();
                        if (isChecked==true){
//                            realm.beginTransaction();
//                            realm.commitTransaction();
                            updateNewCard(id_option_List.get(finalI),item_option_id_List.get(finalI),item_option_name_List.get(finalI),item_option_price_List.get(finalI),type);
                        }else {
                            DeleteCard(id_option_List.get(finalI),item_option_id_List.get(finalI),item_option_price_List.get(finalI),type);
                        }

                    }
                });

            }

//            for (int i = 0; i < name_option_List.size(); i++)
//            {
//                TableRow row =new TableRow(context);
//                row.setId(i);
//                row.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.FILL_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
//                CheckBox checkBox = new CheckBox(context);
//                checkBox.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) context);
//                checkBox.setId(i);
//                checkBox.setText(name_option_List.get(i));
//                row.addView(checkBox);
//                holder.checkbox_layout.addView(row);
//            }
        }

    }
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_option;
        public Spinner spn_option;
        public LinearLayout card_layout,checkbox_layout;

        public MyViewHolder(View view) {
            super(view);

            tv_option = (TextView) view.findViewById(R.id.tv_option);
            card_layout = (LinearLayout) view.findViewById(R.id.card_layout);
            spn_option = (Spinner) view.findViewById(R.id.spn_option);
            checkbox_layout = (LinearLayout)view.findViewById(R.id.checkbox_layout);

        }
    }

    //Doing the same with this method as we did with getOptionNameItem()
    private String getOptionNameItem(int position){
        String sname="";
        try {
            JSONObject job_sizes = null;
            for (int i = 0;i<jsonArrayOption.length();i++){
                job_sizes = jsonArrayOption.getJSONObject(i);

                if (i==position){
                    sname = job_sizes.getString("name");
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sname;
    }

    //Doing the same with this method as we did with getOptionIDItem()
    private String getOptionIDItem(int position){
        String id="";
        try {
            JSONObject job_sizes = null;
            for (int i = 0;i<jsonArrayOption.length();i++){
                job_sizes = jsonArrayOption.getJSONObject(i);

                if (i==position){
                    id = job_sizes.getString("id");
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }

    //Doing the same with this method as we did with getOptionPrice()
    private String getOptionPrice(int position){
        String price="0.00";
        try {
            JSONObject job_sizes = null;
            for (int i = 0;i<jsonArrayOption.length();i++){
                job_sizes = jsonArrayOption.getJSONObject(i);

                if (i==position){
                    price = job_sizes.getString("price");
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return price;
    }

    private void executeRealmOption(final String option_id, final String item_id , final String item_name , final String price,final String type) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                OptionList Option = realm.createObject(OptionList.class);
                Option.setOption_id(option_id);
                Option.setItem_id(item_id);
                Option.setItem_name(item_name);
                Option.setPrice(price);
                Option.setType(type);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(context,"Create Success", Toast.LENGTH_SHORT).show();
                setTv_price_total();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(context,"Create Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void executeRealmOptionAddExtra(final String option_id, final String item_id , final String price) {
//        realm.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//
//                OptionAddExtraList Option = realm.createObject(OptionAddExtraList.class);
//                Option.setOption_id(option_id);
//                Option.setItem_id(item_id);
//                Option.setPrice(price);
//            }
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//                Toast.makeText(context,"Create Success", Toast.LENGTH_SHORT).show();
//                setTv_price_total();
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                Toast.makeText(context,"Create Error", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


    public void updateNewCard(final String option_id, final String item_id, final String item_name, final String price,final String type) {

        if (type.equals("0")){
            realm.beginTransaction();
            final RealmResults<OptionList> toEdit = realm.where(OptionList.class).equalTo("option_id",option_id).findAll();
            toEdit.deleteLastFromRealm();
            realm.commitTransaction();

            executeRealmOption(option_id,item_id,item_name,price,type);
        }else {
            realm.beginTransaction();
            final RealmResults<OptionList> toEdit = realm.where(OptionList.class).equalTo("item_id",item_id).findAll();
            toEdit.deleteLastFromRealm();
            realm.commitTransaction();

            executeRealmOption(option_id,item_id,item_name,price,type);
        }

    }

//    public void updateNewCardExtra(final String option_id, final String item_id, final String price) {
//
//        realm.beginTransaction();
//        RealmResults<OptionAddExtraList> toEdit = realm.where(OptionAddExtraList.class).equalTo("item_id",item_id).findAll();
//        toEdit.deleteLastFromRealm();
//        realm.commitTransaction();
//
//        executeRealmOptionAddExtra(option_id,item_id,price);
//    }

    public void DeleteCard(final String option_id, final String item_id, final String price,final String type) {

        realm.beginTransaction();
        RealmResults<OptionList> toEdit = realm.where(OptionList.class).equalTo("item_id",item_id).findAll();
        toEdit.deleteLastFromRealm();
        realm.commitTransaction();
        setTv_price_total();

//        executeRealmOptionAddExtra(option_id,item_id,price);
    }
}
