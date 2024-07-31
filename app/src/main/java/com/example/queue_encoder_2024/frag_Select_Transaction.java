package com.example.queue_encoder_2024;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import java.util.Objects;


public class frag_Select_Transaction extends Fragment {

    public ActionBar getActionBar() {
        return ((MainActivity) requireActivity()).getSupportActionBar();
    }

    ImageView Pay_Bills;
    ImageView Complaints;
    ImageView Apply_Connection;
    ImageView Pay_Application;



    Bundle rec_bundle;




    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {


        getActionBar().setTitle("Select Transaction");
        rec_bundle = this.getArguments();
        View root = inflater.inflate(R.layout.fragment_select_transaction, container, false);
        // Inflate the layout for this fragment
        Pay_Bills = root.findViewById(R.id.btnimg_Pay_Bills);
        Complaints = root.findViewById(R.id.btnimg_Complaints);
        Apply_Connection = root.findViewById(R.id.btnimg_Apply_Connection);
        Pay_Application = root.findViewById(R.id.btnimg_Pay_Application_Fee);

        Pay_Bills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                frag_Customer_Type_Select nextFrag= new frag_Customer_Type_Select();

                rec_bundle.putString("Transaction", "Pay Bills");
                nextFrag.setArguments(rec_bundle);

                getChildFragmentManager().beginTransaction()
                        //.remove(H.this)
                        .replace(R.id.view_select_transaction, nextFrag, "findThisFragment")
                        .setCustomAnimations(androidx.navigation.ui.R.anim.nav_default_enter_anim, androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                        .addToBackStack(null)
                        .commit();
                getActionBar().setTitle("Select Customer Type");

            }
        });

        Complaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                frag_Customer_Type_Select nextFrag= new frag_Customer_Type_Select();

                rec_bundle.putString("Transaction", "Complaints");
                nextFrag.setArguments(rec_bundle);

                getChildFragmentManager().beginTransaction()
                        //.remove(H.this)
                        .replace(R.id.view_select_transaction, nextFrag, "findThisFragment")
                        .setCustomAnimations(androidx.navigation.ui.R.anim.nav_default_enter_anim, androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                        .addToBackStack(null)
                        .commit();
                getActionBar().setTitle("Select Customer Type");

            }
        });

        Apply_Connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                frag_Customer_Type_Select nextFrag= new frag_Customer_Type_Select();

                rec_bundle.putString("Transaction", "Apply Connection");
                nextFrag.setArguments(rec_bundle);

                getChildFragmentManager().beginTransaction()
                        //.remove(H.this)
                        .replace(R.id.view_select_transaction, nextFrag, "findThisFragment")
                        .setCustomAnimations(androidx.navigation.ui.R.anim.nav_default_enter_anim, androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                        .addToBackStack(null)
                        .commit();
                getActionBar().setTitle("Select Customer Type");

            }
        });


        Pay_Application.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                frag_Customer_Type_Select nextFrag= new frag_Customer_Type_Select();

                rec_bundle.putString("Transaction", "Pay Application Fee");
                nextFrag.setArguments(rec_bundle);

                getChildFragmentManager().beginTransaction()
                        //.remove(H.this)
                        .replace(R.id.view_select_transaction, nextFrag, "findThisFragment")
                        .setCustomAnimations(androidx.navigation.ui.R.anim.nav_default_enter_anim, androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                        .addToBackStack(null)
                        .commit();
                getActionBar().setTitle("Select Customer Type");
            }
        });
        return root;






    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActionBar().setTitle("Select Transaction");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}