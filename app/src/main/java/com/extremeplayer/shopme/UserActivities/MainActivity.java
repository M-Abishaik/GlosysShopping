package com.extremeplayer.shopme.UserActivities;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.extremeplayer.shopme.Fragments.Chat;
import com.extremeplayer.shopme.Fragments.Dashboard;
import com.extremeplayer.shopme.Fragments.Profile;
import com.extremeplayer.shopme.Interface.MailInterface;
import com.extremeplayer.shopme.Interface.MyInterface;
import com.extremeplayer.shopme.R;

import java.io.IOException;
import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;


public class MainActivity extends AppCompatActivity implements MailInterface {

    NavigationTabBar navigationTabBar;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    ArrayList<NavigationTabBar.Model> barModel;

    String mail = "null", pass = "null", product = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        initNavBar();

        try {
            if (!isConnected()) {
                Toast.makeText(MainActivity.this, "Please connect to the Internet.", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException f) {

        } catch (IOException e) {

        }
    }

    public boolean isConnected() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        return (Runtime.getRuntime().exec(command).waitFor() == 0);
    }


    public void initNavBar() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        navigationTabBar = (NavigationTabBar) findViewById(R.id.nav_tb);

        barModel = new ArrayList<>();
        barModel.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_dashboard_black_24dp),
                        R.color.primary_dark)
                        .title("Dashboard")
                        .badgeTitle("NTB DASH")
                         .build()
        );


        barModel.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_chat_black_24dp),
                        R.color.primary_dark).
                        title("Chat")
                        .badgeTitle("NTB CHAT")
                        .build()
        );

        barModel.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_person_black_24dp),
                        R.color.primary_dark).
                        title("Profile")
                        .badgeTitle("NTB PROF")
                        .build()
        );
        navigationTabBar.setModels(barModel);
        navigationTabBar.setViewPager(viewPager, 0);

        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                MyInterface fragment = (MyInterface) pagerAdapter.instantiateItem(viewPager, position);
                navigationTabBar.getModels().get(position).hideBadge();
                fragment.fragmentNowVisible();
                Log.d("Debug", String.valueOf(position));
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }

        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Keep Shopping!!");
        builder.setMessage("Do you really want to exit the app?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Have a good day!", Toast.LENGTH_SHORT).show();
                moveTaskToBack(true);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Thank You!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    @Override
    public void getMail(String string) {
        mail = string;
    }

    @Override
    public String sendMail() {
        return mail;
    }

    @Override
    public void storePass(String string) {
        pass = string;
    }

    @Override
    public String getPass() {
        return pass;
    }

    public void openProduct(View view) {
        Log.d("Debug", "Products visible");
        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra("mail", mail);
        intent.putExtra("product", product);

        Log.d("TAG", product + mail);

        startActivity(intent);
    }

    public void openInstruction(View view) {
        Log.d("Debug", "Instruction visible");

        Intent intent = new Intent(this, InstructionActivity.class);
        startActivity(intent);

    }

    public void openWebsite(View view){
        Intent amazonIntent = new Intent(Intent.ACTION_VIEW);
        amazonIntent.setPackage("com.amazon.android");
        String amazonUrl = "https://www.amazon.com/";
        amazonIntent.setData(Uri.parse(amazonUrl));
        try {
            startActivity(amazonIntent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.amazon.com/")));
        }
    }

    private static class MyPagerAdapter extends FragmentPagerAdapter {

        private MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new Dashboard();
                case 1:
                    return new Chat();
                case 2:
                    return new Profile();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
