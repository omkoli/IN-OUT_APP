package om.koli.inout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    private final static int LOGIN_PERMISSION=1000;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_main);

        btnLogin = (Button) findViewById(R.id.btnSignIn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<AuthUI.IdpConfig> providers = Collections.singletonList(
                        new AuthUI.IdpConfig.EmailBuilder().setAllowNewAccounts(true).build());

                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        LOGIN_PERMISSION);

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_PERMISSION) {
            startNewActivity(resultCode, data);


        }
    }




    private void startNewActivity(int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
        {
            Intent intent = new Intent(MainActivity.this, ListOnline.class);
            startActivity(intent);
            finish();
            String currentUserID = mAuth.getCurrentUser().getUid();
            rootRef.child("Users").child(currentUserID).setValue("");
           // String currentUserName = mAuth.getCurrentUser().getDisplayName();
            //rootRef.child("UsersNAmes").child(currentUserName).setValue("");
        }

        else
        {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
        }



    }


}
