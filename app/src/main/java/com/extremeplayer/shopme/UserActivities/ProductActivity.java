package com.extremeplayer.shopme.UserActivities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import com.extremeplayer.shopme.R;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {

    ResideMenu resideMenu;
    String titles[] = {"Apparel", "Books", "Electronic", "Fashion", "Gadgets", "Groceries", "Home & Kitchen", "Personal Care"};
    int icon[] = {R.drawable.ic_account_circle_black_24dp};
    ResideMenuItem app, buk, elec, fas, gad, gro, hak, pc;

    String string, mail;

    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_product);
        Toast.makeText(this, "Under development.", Toast.LENGTH_SHORT).show();

        Bundle bundle = getIntent().getExtras();

        string = bundle.getString("product");
        mail = bundle.getString("mail");

        Log.d("TAG-", string + mail);

        if (mail.equals("null")) {
            Log.d("TAG--", mail);
        } else {
            Log.d("TAG***", mail);
        }

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);

        initMenu();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        }, 250);

    }

    public void initMenu() {

        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.profile_gradient);
        resideMenu.attachToActivity(this);

        app = new ResideMenuItem(this, icon[0], titles[0]);
        buk = new ResideMenuItem(this, icon[0], titles[1]);
        elec = new ResideMenuItem(this, icon[0], titles[2]);
        fas = new ResideMenuItem(this, icon[0], titles[3]);
        gad = new ResideMenuItem(this, icon[0], titles[4]);
        gro = new ResideMenuItem(this, icon[0], titles[5]);
        hak = new ResideMenuItem(this, icon[0], titles[6]);
        pc = new ResideMenuItem(this, icon[0], titles[7]);

        app.setOnClickListener(this);
        buk.setOnClickListener(this);
        elec.setOnClickListener(this);
        fas.setOnClickListener(this);
        gad.setOnClickListener(this);
        gro.setOnClickListener(this);
        hak.setOnClickListener(this);
        pc.setOnClickListener(this);

        resideMenu.addMenuItem(app, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(buk, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(elec, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(fas, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(gad, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(gro, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(hak, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(pc, ResideMenu.DIRECTION_LEFT);


        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
    }

    public void openMenu(View view) {
        resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        if (view == app) {
            intent = new Intent(ProductActivity.this, GadgetActivity.class);
            intent.putExtra("mail", mail);
            intent.putExtra("type","Apparel");
            resideMenu.closeMenu();
        } else if (view == buk) {
            intent = new Intent(ProductActivity.this, GadgetActivity.class);
            intent.putExtra("mail", mail);
            intent.putExtra("type","Book");
            resideMenu.closeMenu();
        } else if (view == elec) {
            intent = new Intent(ProductActivity.this, GadgetActivity.class);
            intent.putExtra("mail", mail);
            intent.putExtra("type","Electronic");
            resideMenu.closeMenu();
        } else if (view == fas) {
            intent = new Intent(ProductActivity.this, GadgetActivity.class);
            intent.putExtra("mail", mail);
            intent.putExtra("type","Fashion");
            resideMenu.closeMenu();
        } else if (view == gad) {
            intent = new Intent(ProductActivity.this, GadgetActivity.class);
            intent.putExtra("mail", mail);
            intent.putExtra("type","Gadget");
            resideMenu.closeMenu();
        } else if (view == gro) {
            intent = new Intent(ProductActivity.this, GadgetActivity.class);
            intent.putExtra("mail", mail);
            intent.putExtra("type","Grocery");
            resideMenu.closeMenu();
        } else if (view == hak) {
            intent = new Intent(ProductActivity.this, GadgetActivity.class);
            intent.putExtra("mail", mail);
            intent.putExtra("type","Home");
            resideMenu.closeMenu();
        } else if (view == pc) {
            intent = new Intent(ProductActivity.this, GadgetActivity.class);
            intent.putExtra("mail", mail);
            intent.putExtra("type","Personal");
            resideMenu.closeMenu();
        }
        startActivity(intent);
    }
}
