package om.koli.inout;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ListOnlineViewHolder extends RecyclerView.ViewHolder {
    public TextView txtEmail;
    public ListOnlineViewHolder (View itemView){


        super (itemView);
        txtEmail = (TextView)itemView.findViewById(R.id.txt_email);
    }
}