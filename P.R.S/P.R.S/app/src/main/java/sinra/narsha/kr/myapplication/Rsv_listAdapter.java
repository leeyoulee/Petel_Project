package sinra.narsha.kr.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.List;

public class Rsv_listAdapter extends BaseAdapter {

    private SwipeMenuListView rsvListView;
    private Context context;
    private List<Rsv_list> RsvList;

    public Rsv_listAdapter(Context context, List<Rsv_list> RsvList) {
        this.context = context;
        this.RsvList = RsvList;
    }

    @Override
    public int getCount() { return RsvList.size(); }

    @Override
    public Object getItem(int i) { return RsvList.get(i); }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.rsv_list, null);
        TextView MARKET_NAME = (TextView) v.findViewById(R.id.MARKET_NAME);
        TextView PET_NAME = (TextView) v.findViewById(R.id.PET_NAME);
        TextView START_TIME = (TextView) v.findViewById(R.id.START_TIME);
        TextView END_TIME = (TextView) v.findViewById(R.id.END_TIME);

            MARKET_NAME.setText(RsvList.get(i).getMARKET_NAME() + "");
            PET_NAME.setText(RsvList.get(i).getPET_NAME() + "");
            START_TIME.setText(RsvList.get(i).getSTART_TIME() + "");
            END_TIME.setText(RsvList.get(i).getEND_TIME() + "");

        return v;
    }

}
