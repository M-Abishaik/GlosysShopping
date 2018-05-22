package com.extremeplayer.shopme.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.extremeplayer.shopme.Interface.MailInterface;
import com.extremeplayer.shopme.Interface.MyInterface;
import com.extremeplayer.shopme.R;
import com.extremeplayer.shopme.UserActivities.GadgetActivity;

public class Dashboard extends Fragment implements MyInterface {

    ImageButton button1, button2, button3, button4, button5;
    Button registerButton;
    MailInterface mailInterface;

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {
            Intent intent;

            String mail = mailInterface.sendMail();

            switch (v.getId()) {
                case R.id.product1:
                    intent = new Intent(getActivity(), GadgetActivity.class);
                    intent.putExtra("mail", mail);
                    intent.putExtra("type", "Book");
                    startActivity(intent);
                    break;
                case R.id.product2:
                    intent = new Intent(getActivity(), GadgetActivity.class);
                    intent.putExtra("mail", mail);
                    intent.putExtra("type", "Electronic");
                    startActivity(intent);
                    break;
                case R.id.product3:
                    intent = new Intent(getActivity(), GadgetActivity.class);
                    intent.putExtra("mail", mail);
                    intent.putExtra("type", "Grocery");
                    startActivity(intent);
                    break;
                case R.id.product4:
                    intent = new Intent(getActivity(), GadgetActivity.class);
                    intent.putExtra("mail", mail);
                    intent.putExtra("type", "Sports");
                    startActivity(intent);
                    break;
                case R.id.product5:
                    intent = new Intent(getActivity(), GadgetActivity.class);
                    intent.putExtra("mail", mail);
                    intent.putExtra("type", "Veggies");
                    startActivity(intent);
                    break;
                case R.id.register_button:
                    Toast.makeText(getActivity(), "Payment under Alpha Testing.", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };


    public Dashboard() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mailInterface = (MailInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onFragmentChangeListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);


        button1 = (ImageButton) view.findViewById(R.id.product1);
        button2 = (ImageButton) view.findViewById(R.id.product2);
        button3 = (ImageButton) view.findViewById(R.id.product3);
        button4 = (ImageButton) view.findViewById(R.id.product4);
        button5 = (ImageButton) view.findViewById(R.id.product5);
        registerButton = (Button) view.findViewById(R.id.register_button);

        button1.setOnClickListener(onClickListener);
        button2.setOnClickListener(onClickListener);
        button3.setOnClickListener(onClickListener);
        button4.setOnClickListener(onClickListener);
        button5.setOnClickListener(onClickListener);
        registerButton.setOnClickListener(onClickListener);

        return view;
    }

    @Override
    public void fragmentNowVisible() {
        Log.d("Debug", "Dashboard visible");
    }
}
