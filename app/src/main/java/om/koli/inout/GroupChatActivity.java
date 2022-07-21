package om.koli.inout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;


public class GroupChatActivity extends AppCompatActivity {

    //private TextView displayTextMessages;
    private RecyclerView displayTextMessages;

    private String currentGroupName;

    private DatabaseReference GroupNameRef, GroupMessageKeyRef, database;
    private ScrollView mScrollView;



    @Override
    protected void onStart()
    {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), OpenPage.class);
                myIntent.putExtra("messageKey", currentGroupName);
                startActivity(myIntent);
                //Toast.makeText(GroupChatActivity.this, " You're on Floating Button.", Toast.LENGTH_SHORT).show();
            }
        });
        //DatabaseReference UserNameRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName).child("Members");
        super.onStart();


        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                GroupInfo();

            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        currentGroupName = getIntent().getExtras().get("groupName").toString();
        GroupNameRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName);

        InitializeFields();
        DisplayGroupName();
        DisplayGroupInfo();


    }

    private void InitializeFields()
    {
        RecyclerView recyclerView;
        DatabaseReference database;
        DatabaseReference database1;
        MyAdapter myAdapter;
        ArrayList<User> list;



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currentGroupName);
       // displayTextMessages = (RecyclerView) findViewById(R.id.userList);


        setContentView(R.layout.activity_group_chat);

        recyclerView = findViewById(R.id.userList);
        database = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName).child("Users");
        database1 = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName).child("States");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new MyAdapter(this, list);
        recyclerView.setAdapter(myAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    list.add(user);


                }
                myAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });




        database1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    list.add(user);


                }

               myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }






    private void DisplayGroupName()
    {
        database = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName);
        currentGroupName = getIntent().getExtras().get("groupName").toString();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currentGroupName);
    }




    private void GroupInfo()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(GroupChatActivity.this);
        builder.setTitle("Enter Group Name");

        final EditText groupNameField = new EditText(GroupChatActivity.this);
        groupNameField.setHint("Eg Group Name");
        builder.setView(groupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String groupInfo = groupNameField.getText().toString();
                if (TextUtils.isEmpty(groupInfo))
                {
                    Toast.makeText(GroupChatActivity.this, " PLEASE WRITE GROUP NAME", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    AddInfo(groupInfo);


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



    private void AddInfo(String groupInfo) {



        DatabaseReference RooTRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName).child("Info");
        RooTRef.setValue(groupInfo);

    }


    private void DisplayGroupInfo()
    {
        database = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName).child("Info");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

               String value = snapshot.getValue(String.class);

               if (value == null){
                   value = "Description is not yet provided";


               }
                getSupportActionBar().setSubtitle(value);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GroupChatActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }


        });


    }
}





