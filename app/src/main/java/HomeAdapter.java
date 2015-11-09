import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bilkent.findnwear.R;

import java.util.ArrayList;

/**
 * Created by 1 on 05.11.2015.
 */
public class HomeAdapter extends ArrayAdapter{
    private ArrayList<String> photoExplanation, photoURLs;
    private static Activity context;
    public HomeAdapter(Activity context, ArrayList<String> aciklama, ArrayList<String> resimler) {
        super(context, R.layout.home_list_item, aciklama);
        photoExplanation = aciklama;
        photoURLs = resimler;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.home_list_item, null, true);
        //Views inside home_list_item
        TextView resim1 = (TextView)rowView.findViewById(R.id.aciklama1);
        TextView resim2 = (TextView)rowView.findViewById(R.id.aciklama2);
        TextView resim3 = (TextView)rowView.findViewById(R.id.aciklama3);
        ImageButton resimButton1 = (ImageButton) rowView.findViewById(R.id.resim1);
        ImageButton resimButton2 = (ImageButton) rowView.findViewById(R.id.resim2);
        ImageButton resimButton3 = (ImageButton) rowView.findViewById(R.id.resim3);
        resimButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        resimButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        resimButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return rowView;
    }
}
