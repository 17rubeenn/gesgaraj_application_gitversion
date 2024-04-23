package es.gtec.gesgaraj;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class OnboardingNavigation extends AppCompatActivity {
    ViewPager slideViewPager;
    LinearLayout dotIndicator;
    Button btn_atras, btn_siguiente, btn_omitir;
    TextView[] dots;
    OnboardingAdapter onboardingAdapter;
    ViewPager.OnPageChangeListener viewPagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }
        @Override
        public void onPageSelected(int position) {
            setDotIndicator(position);
            if (position > 0) {
                btn_atras.setVisibility(View.VISIBLE);
            } else {
                btn_atras.setVisibility(View.INVISIBLE);
            }
            if (position == 2){
                btn_siguiente.setText("FINALIZAR");
            } else {
                btn_siguiente.setText("SIGUIENTE");
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding_navigation);
        btn_atras = findViewById(R.id.btn_atras);
        btn_siguiente = findViewById(R.id.btn_siguiente);
        btn_omitir = findViewById(R.id.btn_omitir);
        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItem(0) > 0) {
                    slideViewPager.setCurrentItem(getItem(-1), true);
                }
            }
        });
        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItem(0) < 2)
                    slideViewPager.setCurrentItem(getItem(1), true);
                else {
                    Intent i = new Intent(OnboardingNavigation.this, OnboardingStarted.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        btn_omitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OnboardingNavigation.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        slideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        dotIndicator = (LinearLayout) findViewById(R.id.dotIndicator);
        onboardingAdapter = new OnboardingAdapter(this);
        slideViewPager.setAdapter(onboardingAdapter);
        setDotIndicator(0);
        slideViewPager.addOnPageChangeListener(viewPagerListener);
    }
    public void setDotIndicator(int position) {
        dots = new TextView[3];
        dotIndicator.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226", Html.FROM_HTML_MODE_LEGACY));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.black, getApplicationContext().getTheme()));
            dotIndicator.addView(dots[i]);
        }
        dots[position].setTextColor(getResources().getColor(R.color.black, getApplicationContext().getTheme()));
    }
    private int getItem(int i) {
        return slideViewPager.getCurrentItem() + i;
    }
}