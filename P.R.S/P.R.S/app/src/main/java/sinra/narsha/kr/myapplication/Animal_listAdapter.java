package sinra.narsha.kr.myapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class Animal_listAdapter extends BaseAdapter {

    private ListView AnimalListView;
    private Context context;
    private List<Animal_list> AnimalList;

    public Animal_listAdapter(Context context, List<Animal_list> AnimalList) {
        this.context = context;
        this.AnimalList = AnimalList;
    }

    @Override
    public int getCount() { return AnimalList.size(); }

    @Override
    public Object getItem(int i) { return AnimalList.get(i); }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.animal_list, null);
        TextView petType = (TextView) v.findViewById(R.id.petType);
        TextView petName = (TextView) v.findViewById(R.id.petName);
        TextView petAge = (TextView) v.findViewById(R.id.petAge);
        TextView petGender = (TextView) v.findViewById(R.id.petGender);

        petType.setText(AnimalList.get(i).getPetType() + "");
        petName.setText(AnimalList.get(i).getPetName() + "");
        petAge.setText(AnimalList.get(i).getPetAge() + "");
        petGender.setText(AnimalList.get(i).getPetGender() + "");

        return v;
    }

}
