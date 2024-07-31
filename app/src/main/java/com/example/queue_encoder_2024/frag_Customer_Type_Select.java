package com.example.queue_encoder_2024;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frag_Customer_Type_Select#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag_Customer_Type_Select extends Fragment {

    public ActionBar getActionBar() {
        return ((MainActivity) requireActivity()).getSupportActionBar();
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    ImageButton Regular_Customer;
    ImageButton PWD_Customer;
    ImageButton Senior_Button;

    Bundle rec_bundle;


    public frag_Customer_Type_Select() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_customer_type_select.
     */
    // TODO: Rename and change types and number of parameters
    public static frag_Customer_Type_Select newInstance(String param1, String param2) {
        frag_Customer_Type_Select fragment = new frag_Customer_Type_Select();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActionBar().setTitle("Select Customer Type");

        View root = inflater.inflate(R.layout.fragment_customer_type_select, container, false);

        Regular_Customer = root.findViewById(R.id.btnimg_Regular);
        PWD_Customer = root.findViewById(R.id.btnimg_PWD);
        Senior_Button = root.findViewById(R.id.btnimg_Senior);

        rec_bundle = this.getArguments();


        Regular_Customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frag_Enter_Mobile_Number nextFrag= new frag_Enter_Mobile_Number();

                rec_bundle.putString("Customer", "Regular");
                nextFrag.setArguments(rec_bundle);

                getChildFragmentManager().beginTransaction()
                        //.remove(H.this)
                        .replace(R.id.view_customer_type_select, nextFrag, "findThisFragment")
                        .setCustomAnimations(androidx.navigation.ui.R.anim.nav_default_enter_anim, androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                        .addToBackStack(null)
                        .commit();
                getActionBar().setTitle("Enter Mobile Number");
            }
        });

        PWD_Customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frag_Enter_Mobile_Number nextFrag= new frag_Enter_Mobile_Number();

                rec_bundle.putString("Customer", "PWD");
                nextFrag.setArguments(rec_bundle);

                getChildFragmentManager().beginTransaction()
                        //.remove(H.this)
                        .replace(R.id.view_customer_type_select, nextFrag, "findThisFragment")
                        .setCustomAnimations(androidx.navigation.ui.R.anim.nav_default_enter_anim, androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                        .addToBackStack(null)
                        .commit();
                getActionBar().setTitle("Enter Mobile Number");
            }
        });

        Senior_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frag_Enter_Mobile_Number nextFrag= new frag_Enter_Mobile_Number();

                rec_bundle.putString("Customer", "Senior");
                nextFrag.setArguments(rec_bundle);

                getChildFragmentManager().beginTransaction()
                        //.remove(H.this)
                        .replace(R.id.view_customer_type_select, nextFrag, "findThisFragment")
                        .setCustomAnimations(androidx.navigation.ui.R.anim.nav_default_enter_anim, androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                        .addToBackStack(null)
                        .commit();
                getActionBar().setTitle("Enter Mobile Number");
            }
        });

        return root;
    }
}