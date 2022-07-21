package om.koli.inout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Iterator;



public class OpenPage extends AppCompatActivity {

    private TextView displayTextMessages;

    private String currentGroupName;

    private DatabaseReference GroupNameRef, GroupMessageKeyRef;
    private ScrollView mScrollView;



    private FirebaseAuth mAuth;

    @Override
    protected void onStart()
    {
        DatabaseReference UserNameRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName).child("Messages").child("Messages");
        super.onStart();
        UserNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName)
            {
                if(dataSnapshot.exists()){

                    DisplayMessages(dataSnapshot);

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){

                    DisplayMessages(dataSnapshot);

                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_page);

        currentGroupName = getIntent().getExtras().get("messageKey").toString();
        GroupNameRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName).child("Messages").child("Messages");

        setContentView(R.layout.activity_open_page);
        SwitchCompat switchCompat;
        switchCompat = findViewById(R.id.switch_compat);

        SharedPreferences sharedPreferences=getSharedPreferences("save",MODE_PRIVATE);
        switchCompat.setChecked(sharedPreferences.getBoolean("value",true));
        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchCompat.isChecked())
                {
                    // When switch checked
                    SharedPreferences.Editor editor=getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",true);
                    editor.apply();
                    switchCompat.setChecked(true);
                    saveMessageInfoToDatabase();
                }
                else
                {
                    // When switch unchecked
                    SharedPreferences.Editor editor=getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",false);
                    editor.apply();
                    switchCompat.setChecked(false);
                    UndoMessageInfoToDatabase();
                }
            }

        });
        InitializeFields();

    }




    private void DisplayMessages(DataSnapshot dataSnapshot) {
        Iterator iterator = dataSnapshot.getChildren().iterator();

        while(iterator.hasNext()) {
            String chatMessage = (String) ((DataSnapshot)iterator.next()).getValue();

            displayTextMessages.append(chatMessage + "\n");
        }
    }


    private void saveMessageInfoToDatabase() {


        String email = user.getDisplayName();
        String message = email + " is In";
        String messageKey = GroupNameRef.push().getKey();

        HashMap<String, Object> GroupMessageKey = new HashMap<>();
        GroupNameRef.updateChildren(GroupMessageKey);



        GroupMessageKeyRef = GroupNameRef.child(messageKey);


        HashMap<String, Object> MessageInfoMap = new HashMap<>();
        MessageInfoMap.put("message" ,message);



        GroupMessageKeyRef.updateChildren(MessageInfoMap);



        mAuth = FirebaseAuth.getInstance();
        String CurrentUserName = mAuth.getCurrentUser().getDisplayName();
       DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName).child("States").child(CurrentUserName);
        RootRef.child("UserName").setValue(CurrentUserName);
        RootRef.child("Value").setValue("IN");
        mScrollView.fullScroll(ScrollView.FOCUS_DOWN);

    }







    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private void UndoMessageInfoToDatabase() {
        String email = user.getDisplayName();
        String message = email + " is Out";
        String messageKey = GroupNameRef.push().getKey();
        HashMap<String, Object> GroupMessageKey = new HashMap<>();
       GroupNameRef.updateChildren(GroupMessageKey);



        GroupMessageKeyRef = GroupNameRef.child(messageKey);


        HashMap<String, Object> MessageInfoMap = new HashMap<>();
        MessageInfoMap.put("message" ,message);
        GroupMessageKeyRef.updateChildren(MessageInfoMap);



        mAuth = FirebaseAuth.getInstance();
        String CurrentUserName = mAuth.getCurrentUser().getDisplayName();
        DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName).child("States").child(CurrentUserName);
        RootRef.child("UserName").setValue(CurrentUserName);
        RootRef.child("Value").setValue("OUT");
        mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }


    private void InitializeFields()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currentGroupName);
        displayTextMessages = (TextView) findViewById(R.id.userMessages);

        mScrollView = (ScrollView) findViewById(R.id.SCROLLER_ID);
        mScrollView.fullScroll(ScrollView.FOCUS_DOWN);



    }


}