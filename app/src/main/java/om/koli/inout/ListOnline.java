

package om.koli.inout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;


public class ListOnline extends AppCompatActivity {
    private android.view.View groupFragmentView;
    private ListView list_view;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups = new ArrayList<>();
    private DatabaseReference GroupRef;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabsAccessorAdapter myTabsAccessorAdapter;
    private FirebaseAuth mAuth;




    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_online);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("IN OUT");
        setSupportActionBar(toolbar);
        myViewPager = (ViewPager) findViewById(R.id.main_tabs_pager);
        myTabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAccessorAdapter);

        myTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);


    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;


    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.action_create)
        {
            RequestNewGroup();
        }


        if(item.getItemId() == R.id.action_join)
        {
            JoinNewGroup();
        }
        return false;

    }

    private void JoinNewGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListOnline.this);
        builder.setTitle("Enter Group Name ");

        EditText groupNammeField = new EditText(ListOnline.this);
        groupNammeField.setHint("Eg Group Name");
        builder.setView(groupNammeField);




        AlertDialog.Builder builder1 = new AlertDialog.Builder(ListOnline.this);
        builder1.setTitle("Enter Secret Key");

        EditText groupKeyField = new EditText(ListOnline.this);
        groupKeyField.setHint("Eg Secret Key");
        builder1.setView(groupKeyField);



        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String groupName = groupNammeField.getText().toString();
                String groupKey = groupKeyField.getText().toString();

                if (TextUtils.isEmpty(groupName))
                {
                    Toast.makeText(ListOnline.this, " PLEASE WRITE GROUP NAME", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    builder1.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            String groupKey = groupKeyField.getText().toString();

                            if (TextUtils.isEmpty(groupKey))
                            {
                                Toast.makeText(ListOnline.this, " PLEASE WRITE GROUP NAME", Toast.LENGTH_SHORT).show();

                            }
                            else{
                                RequestNewJoinGroup(groupName, groupKey);
                            }


                        }
                    });
                    builder1.show();

                     //RequestNewJoinGroup(groupName, groupKey);
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }


    private void RequestNewJoinGroup(String groupName, String groupKey) {




        mAuth = FirebaseAuth.getInstance();
        String CurrentUserName = mAuth.getCurrentUser().getDisplayName();

        DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference().child("UsersNAmes").child(CurrentUserName).child("Groups").child(groupKey);


        RootRef.child("GroupName").setValue(groupName);
        RootRef.child("GroupKey").setValue(groupKey);

        DatabaseReference RooTRef = FirebaseDatabase.getInstance().getReference().child("UsersNAmes").child(CurrentUserName).child("GroupNames").child(groupKey);
        RooTRef.setValue(groupName);




    }


    private void RequestNewGroup()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListOnline.this);
        builder.setTitle("Enter Group Name");

        final EditText groupNameField = new EditText(ListOnline.this);
        groupNameField.setHint("Eg Group Name");
        builder.setView(groupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String groupName = groupNameField.getText().toString();
                if (TextUtils.isEmpty(groupName))
                {
                    Toast.makeText(ListOnline.this, " PLEASE WRITE GROUP NAME", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    CreateNewGroup(groupName);


                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void CreateNewGroup(String groupName) {
        mAuth = FirebaseAuth.getInstance();
        String CurrentUserName = mAuth.getCurrentUser().getDisplayName();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupName).child("Users");

        //String currentUserName = mAuth.getCurrentUser().getDisplayName();
        //rootRef.child("Groups").child(groupName).child("Members").child("Name").setValue(currentUserName);

        String messageKey = rootRef.push().getKey();

        HashMap<String, Object> GroupMessageKey = new HashMap<>();
        rootRef.updateChildren(GroupMessageKey);

        //DatabaseReference GroupMessageKeyRef = rootRef.child(messageKey);
        DatabaseReference GroupMessageKeyRef = rootRef.child(messageKey);

        HashMap<String, Object> MessageInfoMap = new HashMap<>();
       // MessageInfoMap.put("Name" ,CurrentUserName);


        GroupMessageKeyRef.updateChildren(MessageInfoMap);

        DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference().child("UsersNAmes").child(CurrentUserName).child("Groups").child(messageKey);

        RootRef.child("GroupName").setValue(groupName);
        RootRef.child("GroupKey").setValue(messageKey);


        DatabaseReference RooTRef = FirebaseDatabase.getInstance().getReference().child("UsersNAmes").child(CurrentUserName).child("GroupNames").child(messageKey);
        RooTRef.setValue(groupName);

    }




}
