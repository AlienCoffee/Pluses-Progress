package tk.pluses.plusesprogress;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class IndexPage extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_index_page);

    }

    public static void login(View view){

    }

    public void register(View view){
        Intent intent = new Intent(this, RegisterPage.class);
        startActivity(intent);
    }

    public static void loginGuest(View view){

    }
}
