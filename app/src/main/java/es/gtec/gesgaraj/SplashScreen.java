package es.gtec.gesgaraj;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;



public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_arriba);

        ImageView img_icono = findViewById(R.id.img_icono);
        img_icono.setAnimation(animation1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isFirstTimeUser()) {
                    Intent intent = new Intent(SplashScreen.this, OnboardingNavigation.class);
                    startActivity(intent);
                    finish();
                    SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
                    editor.putBoolean("firstTime", false);
                    editor.apply();

                } else {

                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 3000);



    }

    private boolean isFirstTimeUser() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return prefs.getBoolean("firstTime", true);
    }


}