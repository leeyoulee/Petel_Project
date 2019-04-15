package sinra.narsha.kr.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Shop_listAdapter extends BaseAdapter {

    private ListView shopListView;
    private Context context;
    private List<Shop_list> ShopList;

    public Shop_listAdapter(Context context, List<Shop_list> ShopList) {
        this.context = context;
        this.ShopList = ShopList;
    }

    @Override
    public int getCount() { return ShopList.size(); }

    @Override
    public Object getItem(int i) { return ShopList.get(i); }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.shop_list, null);
        TextView MARKET_NAME = (TextView) v.findViewById(R.id.MARKET_NAME);
        TextView PETS = (TextView) v.findViewById(R.id.PETS);
        TextView CLASS = (TextView) v.findViewById(R.id.CLASS);
        TextView SERVICE = (TextView) v.findViewById(R.id.SERVICE);

            MARKET_NAME.setText(ShopList.get(i).getMARKET_NAME() + "");
            PETS.setText(ShopList.get(i).getPETS() + "");
            CLASS.setText(ShopList.get(i).getCLASS() + "");
            SERVICE.setText(ShopList.get(i).getSERVICE() + "");

        return v;
    }

}
