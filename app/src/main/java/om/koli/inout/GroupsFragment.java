package om.koli.inout;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GroupsFragment extends Fragment {

    private android.view.View groupFragmentView;
    private ListView list_view;
    private ArrayAdapter<Object> arrayAdapter;
    private ArrayList<Object> list_of_groups = new ArrayList<>();
    private DatabaseReference GroupRef, GRoupREF;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        groupFragmentView = inflater.inflate(R.layout.activity_list_online, container, false);



        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String CurrentUserName = mAuth.getCurrentUser().getDisplayName();
        GroupRef = FirebaseDatabase.getInstance().getReference().child("UsersNAmes").child(CurrentUserName).child("Groups");
        InitializeFields();
        RetrieveAndDisplayGroups();


        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String currentGroupName = adapterView.getItemAtPosition(position).toString();
                Intent groupChatIntent = new Intent(getContext(), GroupChatActivity.class);
                groupChatIntent.putExtra("groupName", currentGroupName);
                startActivity(groupChatIntent);
            }
        });

        return groupFragmentView;

    }





   private void RetrieveAndDisplayGroups() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String CurrentUserName = mAuth.getCurrentUser().getDisplayName();
        GRoupREF =  FirebaseDatabase.getInstance().getReference().child("UsersNAmes").child(CurrentUserName).child("GroupNames");
        GRoupREF.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        Set<Object> set = new HashSet<>();
                        Iterator iterator = snapshot.getChildren().iterator();

                        while(iterator.hasNext())
                        {

                            set.add(((DataSnapshot)iterator.next()).getValue());

                            list_of_groups.clear();
                            list_of_groups.addAll(set);
                            arrayAdapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });
    }


    private void InitializeFields()
    {
        list_view = (ListView) groupFragmentView.findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<Object>(groupFragmentView.getContext(), android.R.layout.simple_list_item_1, list_of_groups);
        list_view.setAdapter(arrayAdapter);

    }


}