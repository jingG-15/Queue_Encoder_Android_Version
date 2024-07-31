package com.example.queue_encoder_2024;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frag_SMS_Service_Consent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag_SMS_Service_Consent extends Fragment {

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

    Button I_Agree;
    Button Not_Agree;
    Bundle rec_bundle;

    public frag_SMS_Service_Consent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_SMS_Service_Consent.
     */
    // TODO: Rename and change types and number of parameters
    public static frag_SMS_Service_Consent newInstance(String param1, String param2) {
        frag_SMS_Service_Consent fragment = new frag_SMS_Service_Consent();
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
        rec_bundle = this.getArguments();
        getActionBar().setTitle("Warning");
        View root = inflater.inflate(R.layout.fragment_sms_service_consent, container, false);

        I_Agree = root.findViewById(R.id.btn_I_Agree);
        Not_Agree = root.findViewById(R.id.btn_Not_Agree);

        I_Agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frag_Entry_Summary nextFrag= new frag_Entry_Summary();
                rec_bundle.putString("SMS_Agree", "Yes");
                nextFrag.setArguments(rec_bundle);

                getChildFragmentManager().beginTransaction()
                        //.remove(H.this)
                        .replace(R.id.view_sms_service_consent, nextFrag, "findThisFragment")
                        .setCustomAnimations(androidx.navigation.ui.R.anim.nav_default_enter_anim, androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                        .addToBackStack(null)
                        .commit();
                getActionBar().setTitle("Confirm Entries");
            }
        });

        Not_Agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frag_Entry_Summary nextFrag= new frag_Entry_Summary();
                rec_bundle.putString("SMS_Agree", "No");
                nextFrag.setArguments(rec_bundle);

                getChildFragmentManager().beginTransaction()
                        //.remove(H.this)
                        .replace(R.id.view_sms_service_consent, nextFrag, "findThisFragment")
                        .setCustomAnimations(androidx.navigation.ui.R.anim.nav_default_enter_anim, androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                        .addToBackStack(null)
                        .commit();
                getActionBar().setTitle("Confirm Entries");
            }
        });
        return root;
    }
}