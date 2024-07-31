package com.example.queue_encoder_2024;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class FirstFragment extends Fragment {
    public ActionBar getActionBar() {
        return ((MainActivity) requireActivity()).getSupportActionBar();
    }
    private ProgressDialog pDialog;
    ImageButton Refresh_Button;
    ImageButton Settings_Button;
    ImageButton Add_New_ticket;

    TextView Total_Queue;
    TextView Regular_Queue;
    TextView PWD_Queue;
    TextView Last_Queue_Ticket;

    String json_message;
    String str_Last_Entry;
    String str_PWD_Count;
    String str_Reg_Count;
    String str_Total_Queue;

    String Debug_String;

    sqlite_helper myDB;

    String Settings_Server_IP;
    String Settings_Printer_IP;
    String Settings_Display_Port;



    @SuppressLint("Range")
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment



        View root = inflater.inflate(R.layout.fragment_first, container, false);
        Refresh_Button = root.findViewById(R.id.btnimg_Refresh_Stats);
        Settings_Button = root.findViewById(R.id.btnimg_Settings);

        Total_Queue = root.findViewById(R.id.txt_Total_Count);
        PWD_Queue = root.findViewById(R.id.txt_PWD_Count);
        Regular_Queue = root.findViewById(R.id.txt_Regular_Count);
        Last_Queue_Ticket = root.findViewById(R.id.txt_Last_Queue_Ticket);
        Add_New_ticket = root.findViewById(R.id.btnimg_Add_Ticket);


        if (ContextCompat.checkSelfPermission(MainActivity.getContextOfApplication(), android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.getContextOfApplication(), android.Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.getContextOfApplication(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.getContextOfApplication(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.getContextOfApplication(), android.Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.getContextOfApplication(), new String[]{
                    android.Manifest.permission.INTERNET,
                    android.Manifest.permission.ACCESS_NETWORK_STATE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.ACCESS_WIFI_STATE
            }, 100);

        }

        myDB = new sqlite_helper(getContext());
        Cursor res = myDB.Check_Data_Exists();

        if (res.getCount() != 0) {
            if (res.moveToFirst()){
                while(!res.isAfterLast()){


                    Settings_Server_IP = res.getString(res.getColumnIndex("Server_IP"));
                    Settings_Printer_IP = res.getString(res.getColumnIndex("Printer_IP"));
                    Settings_Display_Port = res.getString(res.getColumnIndex("Display_Port"));



                    // do what ever you want here
                    res.moveToNext();
                }
            }

            res.close();

            if (ContextCompat.checkSelfPermission(MainActivity.getContextOfApplication(), android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(MainActivity.getContextOfApplication(), android.Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED){

                new Get_Tickets_Stats().execute();

            }






            Refresh_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ContextCompat.checkSelfPermission(MainActivity.getContextOfApplication(), android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(MainActivity.getContextOfApplication(), Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED){

                        new Get_Tickets_Stats().execute();

                    }




                }
            });

            Add_New_ticket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);

                    frag_Select_Transaction nextFrag= new frag_Select_Transaction();
                    Bundle rec_bundle = new Bundle();
                    rec_bundle.putString("Server_IP", Settings_Server_IP);
                    rec_bundle.putString("Printer_IP", Settings_Printer_IP);
                    rec_bundle.putString("Display_Port", Settings_Display_Port);


                    nextFrag.setArguments(rec_bundle);

                    getChildFragmentManager().beginTransaction()
                            //.remove(H.this)
                            .replace(R.id.view_first, nextFrag, "findThisFragment")
                            .setCustomAnimations(androidx.navigation.ui.R.anim.nav_default_enter_anim, androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                            .addToBackStack(null)
                            .commit();
                    getActionBar().setTitle("Select Transaction");

                }
            });





        }else{

            Refresh_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    frag_Initial_Setup nextFrag= new frag_Initial_Setup();

                    Bundle rec_bundle = new Bundle();

                    rec_bundle.putString("FromButt", "Refresh");

                    nextFrag.setArguments(rec_bundle);

                    getChildFragmentManager().beginTransaction()
                            //.remove(H.this)
                            .replace(R.id.view_first, nextFrag, "findThisFragment")
                            .setCustomAnimations(androidx.navigation.ui.R.anim.nav_default_enter_anim, androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                            .addToBackStack(null)
                            .commit();
                    getActionBar().setTitle("Setup");




                }
            });

            Add_New_ticket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    frag_Initial_Setup nextFrag= new frag_Initial_Setup();

                    Bundle rec_bundle = new Bundle();

                    rec_bundle.putString("FromButt", "Add_Ticket");

                    nextFrag.setArguments(rec_bundle);

                    getChildFragmentManager().beginTransaction()
                            //.remove(H.this)
                            .replace(R.id.view_first, nextFrag, "findThisFragment")
                            .setCustomAnimations(androidx.navigation.ui.R.anim.nav_default_enter_anim, androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                            .addToBackStack(null)
                            .commit();
                    getActionBar().setTitle("Setup");

                }
            });

        }


        Settings_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                frag_Password_Settings nextFrag= new frag_Password_Settings();

                Bundle settings_bundle = new Bundle();
                settings_bundle.putString("Last_Ticket_Number", str_Last_Entry);
                settings_bundle.putString("Regular_Count", str_Reg_Count);
                settings_bundle.putString("PWD/Senior_Count", str_PWD_Count);
                settings_bundle.putString("Total_Queue", str_Total_Queue);

                nextFrag.setArguments(settings_bundle);


                getChildFragmentManager().beginTransaction()
                        .replace(R.id.view_first, nextFrag, "findThisFragment")
                        .setCustomAnimations(androidx.navigation.ui.R.anim.nav_default_enter_anim, androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                        .addToBackStack(null)
                        .commit();
                getActionBar().setTitle("Settings");



            }
        });



        return root;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }



    class Get_Tickets_Stats extends AsyncTask<String, String, String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Fetching stats. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {
            try {

                String link="http://" + Settings_Server_IP + ":8080/Queue_Encoder/InquireCounts.php";

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;
                // Read Server Response
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                reader.close();
                if (line != null){
                    Debug_String = line;
                    JSONArray jsonArray = new JSONArray(line);
                    JSONObject obj = jsonArray.getJSONObject(0);

                    json_message = obj.getString("message");
                    if(json_message.equals("Good") || json_message.equals("Init")){
                        str_PWD_Count = obj.getString("PWD_Count");
                        str_Reg_Count = obj.getString("Regular_Count");
                        str_Last_Entry = obj.getString("Last_Entry");
                        str_Total_Queue = obj.getString("Total_Queue");
                    }else{
                        str_PWD_Count = "----";
                        str_Reg_Count = "----";
                        str_Last_Entry = "----";
                        str_Total_Queue = "----";
                    }
                }else{
                    json_message = "No Response from server";
                }
            } catch (Exception e) {
                e.printStackTrace();
                json_message = new String("Exception: " + e.getMessage());
                //Toast.makeText(getContext(), new String("Exception: " + e.getMessage()), Toast.LENGTH_LONG).show();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            if(json_message.equals("Good") || json_message.equals("Init")){
                //Toast.makeText(getContext(), json_message_1 , Toast.LENGTH_LONG).show();
                Total_Queue.setText(str_Total_Queue);
                PWD_Queue.setText(str_PWD_Count);
                Regular_Queue.setText(str_Reg_Count);
                Last_Queue_Ticket.setText(str_Last_Entry);

            }else if(json_message.equals("None")){
                Total_Queue.setText("----");
                PWD_Queue.setText("----");
                Regular_Queue.setText("----");
                Last_Queue_Ticket.setText("----");

            }else{
                Toast.makeText(getContext(), json_message.replace(Settings_Server_IP + ":8080" , "Server") + "\n" + Debug_String , Toast.LENGTH_LONG).show();
            }
        }

    }

}