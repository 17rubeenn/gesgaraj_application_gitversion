package es.gtec.gesgaraj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainPage extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    SettingsFragment settingsFragment = new SettingsFragment();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
        bottomNavigationView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                } else if (itemId == R.id.settings) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, settingsFragment).commit();
                } else if (itemId == R.id.logout) {

                    finishAffinity();
                    System.exit(0);    //finish(); logout
                }
            }
        });
    }

}