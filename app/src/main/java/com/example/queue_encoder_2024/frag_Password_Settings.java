package com.example.queue_encoder_2024;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frag_Password_Settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag_Password_Settings extends Fragment {

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


    Button Confirm;
    Button Cancel;

    TextView Password;

    Bundle rec_Bundle;



    public frag_Password_Settings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_password_settings.
     */
    // TODO: Rename and change types and number of parameters
    public static frag_Password_Settings newInstance(String param1, String param2) {
        frag_Password_Settings fragment = new frag_Password_Settings();
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
        View root = inflater.inflate(R.layout.fragment_password_settings, container, false);

        Confirm = root.findViewById(R.id.btn_Admin_Confirm);
        Cancel = root.findViewById(R.id.btn_Admin_Cancel);
        Password = root.findViewById(R.id.txtin_Admin_Password);
        rec_Bundle = this.getArguments();

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
                manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });


        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String p_word_Entered = Password.getText().toString().trim();
                if(p_word_Entered.isEmpty()){
                    Password.setError("Enter a valid password");
                    Password.requestFocus();
                    return;
                }else {
                    if(p_word_Entered.equals("12345")){
                        frag_Settings nextFrag= new frag_Settings();


                        nextFrag.setArguments(rec_Bundle);


                        getChildFragmentManager().beginTransaction()
                                //.remove(H.this)
                                .replace(R.id.view_password_settings, nextFrag, nextFrag.getClass().getName())
                                .setCustomAnimations(androidx.fragment.R.animator.fragment_fade_enter, androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                                .addToBackStack(null)
                                .commit();
                        getActionBar().setTitle("Settings");



                    }else{
                        Toast.makeText(getContext(), "Wrong Password." , Toast.LENGTH_LONG).show();

                    }


                }



            }
        });

        return root;
    }
}