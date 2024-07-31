package com.example.queue_encoder_2024;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.epson.epos2.Epos2CallbackCode;
import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frag_Initial_Setup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag_Initial_Setup extends Fragment {
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

    Button butt_Test_Server;
    Button butt_Test_Printer;
    Button butt_Proceed;
    EditText new_Server_IP;
    EditText new_Printer_IP;
    EditText new_Display_Port;
    String Temp_Server_IP = "";
    String Temp_Printer_IP = "";
    String Temp_Display_Port = "";
    String Response_Server;
    private ProgressDialog pDialog_1;
    Printer printer;
    Bundle rec_bundle;
    String Prev_Trans;



    public frag_Initial_Setup() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_initial_setup.
     */
    // TODO: Rename and change types and number of parameters
    public static frag_Initial_Setup newInstance(String param1, String param2) {
        frag_Initial_Setup fragment = new frag_Initial_Setup();
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
        View root = inflater.inflate(R.layout.fragment_initial_setup, container, false);

        butt_Proceed = root.findViewById(R.id.btn_Proceed);
        butt_Test_Printer = root.findViewById(R.id.btn_Init_Test_Printer);
        butt_Test_Server = root.findViewById(R.id.btn_Init_Test_Server);
        new_Printer_IP = root.findViewById(R.id.txtin_Init_Printer_IP);
        new_Server_IP = root.findViewById(R.id.txtin_Init_Server_IP);
        new_Display_Port = root.findViewById(R.id.txtin_Init_Display_Port);

        butt_Proceed.setVisibility(View.INVISIBLE);

        new Prep_printer_data().execute();

        rec_bundle = this.getArguments();

        if (rec_bundle != null) {
            Prev_Trans = rec_bundle.getString("FromButt");
        }

        butt_Test_Server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Temp_Server_IP = new_Server_IP.getText().toString().trim();
                Temp_Display_Port = new_Display_Port.getText().toString().trim();
                if(Temp_Server_IP.isEmpty()) {
                    new_Server_IP.setError("Enter a valid IP Address");
                    new_Server_IP.requestFocus();
                    return;
                }else if(Temp_Display_Port.isEmpty()){
                    new_Display_Port.setError("Enter a valid Port Number");
                    new_Display_Port.requestFocus();
                    return;
                }else {

                    //TODO Call Server Test Function

                    new Test_Server_Conn().execute();

                    if(!Temp_Server_IP.equals("") && !Temp_Printer_IP.equals("") && !Temp_Display_Port.equals("")){
                        butt_Proceed.setVisibility(View.VISIBLE);
                    }


                }

            }
        });



        butt_Test_Printer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Temp_Printer_IP = new_Printer_IP.getText().toString().trim();
                if(Temp_Printer_IP.isEmpty()){
                    new_Printer_IP.setError("Enter a valid IP Address");
                    new_Printer_IP.requestFocus();
                    return;
                }else {

                    //TODO Call Printer Test Function

                    new Send_Printer_Data().execute();


                    if(!Temp_Server_IP.equals("") && !Temp_Printer_IP.equals("") && !Temp_Display_Port.equals("")){
                        butt_Proceed.setVisibility(View.VISIBLE);
                    }


                }
            }
        });


        butt_Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Call Save to SQLite Details Function

                new Save_to_SQLite_Data().execute();





            }
        });

        return root;
    }

    class Test_Server_Conn extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_1 = new ProgressDialog(getContext());
            pDialog_1.setMessage("Testing Server IP Address. Please wait...");
            pDialog_1.setIndeterminate(false);
            pDialog_1.setCancelable(false);
            pDialog_1.show();
        }


        protected String doInBackground(String... args) {
            try {
                String link="http://" + Temp_Server_IP + ":8080/Queue_Encoder/TestConn.php";
                //HashMap<String,String> data_1 = new HashMap<>();
                //data_1.put("New_Vis_ID_Token_Form", vis_ID_creation + "TOKENS");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                //OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                //wr.write( getPostDataString(data_1) );
                //wr.flush();

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
                    Response_Server = line;
                }else{
                    Response_Server = "No Response from server";
                }
            } catch (Exception e) {
                e.printStackTrace();
                Response_Server = new String("Exception: " + e.getMessage());
                //Toast.makeText(getContext(), new String("Exception: " + e.getMessage()), Toast.LENGTH_LONG).show();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog_1.dismiss();
            if(Response_Server.equals("Conn Good")){


                Toast.makeText(getContext(), "IP is Valid." , Toast.LENGTH_LONG).show();

            }else if(Response_Server.equals("No Response from server")){
                Toast.makeText(getContext(), Response_Server + ". Please check your internet connection or contact BILECO IT division for technical details.", Toast.LENGTH_LONG).show();

            }else {
                Toast.makeText(getContext(), Response_Server, Toast.LENGTH_LONG).show();
            }
        }

    }




    class Send_Printer_Data extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_1 = new ProgressDialog(getContext());
            pDialog_1.setMessage("Preparing printer data...");
            pDialog_1.setIndeterminate(false);
            pDialog_1.setCancelable(false);
            pDialog_1.show();
        }


        protected String doInBackground(String... args) {


            try {
                //Logo = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(MainActivity.getContextOfApplication().getResources(), R.drawable.uuuu), 110, 110, true);


                if(printer.getStatus().getConnection() == Printer.FALSE){
                    printer.connect("TCP:" + Temp_Printer_IP, Printer.PARAM_DEFAULT);

                }
                printer.sendData(Printer.PARAM_DEFAULT);



                //printer.clearCommandBuffer();
                Response_Server = "Request Sent.";

            }
            catch (Epos2Exception e) {
//Displays error messages
                int i = e.getErrorStatus();
                Response_Server = Integer.toString(i);
            }



            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog_1.dismiss();

            if(Response_Server.equals("Request Sent.")){

                Toast.makeText(getContext(), Response_Server, Toast.LENGTH_LONG).show();



            }else if(Response_Server.equals("1")){
                Toast.makeText(getContext(), "An invalid parameter was passed.", Toast.LENGTH_LONG).show();
                //new Disconnect_Printer().execute();
            }else if(Response_Server.equals("2")){
                Toast.makeText(getContext(), "Not enough memory on the printer allocated.", Toast.LENGTH_LONG).show();
                //new Disconnect_Printer().execute();
            }else if(Response_Server.equals("3")){
                Toast.makeText(getContext(), "An unknown error has occurred.", Toast.LENGTH_LONG).show();
                //new Disconnect_Printer().execute();
            }else{

                Toast.makeText(getContext(), Response_Server, Toast.LENGTH_LONG).show();
                //new Disconnect_Printer().execute();
            }
            //TODO Proceed to next process | Show Ticket Number and option to reprint

        }

    }



    class Prep_printer_data extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_1 = new ProgressDialog(getContext());
            pDialog_1.setMessage("Preparing printer data...");
            pDialog_1.setIndeterminate(false);
            pDialog_1.setCancelable(false);
            pDialog_1.show();
        }


        protected String doInBackground(String... args) {


            try {
                //Logo = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(MainActivity.getContextOfApplication().getResources(), R.drawable.uuuu), 110, 110, true);


                printer = null;

                printer = new Printer(Printer.TM_T88, Printer.MODEL_SOUTHASIA, getContext());
                printer.setReceiveEventListener(frag_Initial_Setup.this::onPtrReceive);

                //printer.beginTransaction();
                printer.addPageBegin();
                printer.addPageArea(0,0, 512, 300);


                printer.addTextSize(1,1);
                printer.addText("\n");
                printer.addHPosition(10);
                printer.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.TRUE, Printer.COLOR_1);
                printer.addTextFont(Printer.FONT_B);
                printer.addText("Test Print | Test Print | Test Print | Test Print");

//                    printer.addHPosition(1);
//                    printer.addLogo(32, 33);


                Date nuw = new Date();
                SimpleDateFormat temp3 = new SimpleDateFormat("MMM dd, yyyy | hh:mm a", Locale.getDefault());
                String Date_Reg = temp3.format(nuw);

                printer.addTextSize(1,1);
                printer.addText("\n");
                printer.addHPosition(10);
                printer.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.FALSE, Printer.COLOR_1);
                printer.addTextFont(Printer.FONT_B);
                printer.addText( "Date: ");

                printer.addTextSize(1,1);
                printer.addHPosition(10 + 120);
                printer.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.TRUE, Printer.COLOR_1);
                printer.addTextFont(Printer.FONT_B);
                printer.addText(  Date_Reg);




                //printer.addPageArea(0,0, 512, 1);


                printer.addPageEnd();

                printer.addCut(Printer.CUT_NO_FEED);
                // printer.addPageBegin();

                //printer.addPageEnd();




                //printer.clearCommandBuffer();
                Response_Server = "Done.";

            }
            catch (Epos2Exception e) {
//Displays error messages
                int i = e.getErrorStatus();
                Response_Server = Integer.toString(i);
            }



            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog_1.dismiss();

            if(Response_Server.equals("Done.")){

                Toast.makeText(getContext(), Response_Server, Toast.LENGTH_LONG).show();



            }else if(Response_Server.equals("1")){
                Toast.makeText(getContext(), "An invalid parameter was passed.", Toast.LENGTH_LONG).show();
                //new Disconnect_Printer().execute();
            }else if(Response_Server.equals("2")){
                Toast.makeText(getContext(), "Not enough memory on the printer allocated.", Toast.LENGTH_LONG).show();
                //new Disconnect_Printer().execute();
            }else if(Response_Server.equals("3")){
                Toast.makeText(getContext(), "An unknown error has occurred.", Toast.LENGTH_LONG).show();
                //new Disconnect_Printer().execute();
            }else{

                Toast.makeText(getContext(), Response_Server, Toast.LENGTH_LONG).show();
                //new Disconnect_Printer().execute();
            }
            //TODO Proceed to next process | Show Ticket Number and option to reprint

        }

    }


    public void onPtrReceive(final Printer printerObj, final int code, final PrinterStatusInfo status,
                             final String printJobId) {
        MainActivity.getContextOfApplication().runOnUiThread(new Runnable() {
            @Override
            public synchronized void run() {
                if (code == Epos2CallbackCode.CODE_SUCCESS) {
                    //Displays successful print messages
                    //printer.disconnect();
                    //printer.clearCommandBuffer();

                    //printer.setReceiveEventListener(null);


                    Toast.makeText(getContext(), "Printing Success!" , Toast.LENGTH_LONG).show();
                    //new Disconnect_Printer().execute();

                }
                else {

                    Toast.makeText(getContext(), "Printing Error!" , Toast.LENGTH_LONG).show();
                    //new Disconnect_Printer().execute();
                    //Displays error messages
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                //Abort process
//                try {
//                    printer.disconnect();
//                    //printer.clearCommandBuffer();
//                    //printer.setReceiveEventListener(null);
//                }
//                catch (Epos2Exception e) {
//                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//                    //Displays error messages
//                }
            }
        }).start();
    }


    class Save_to_SQLite_Data extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_1 = new ProgressDialog(getContext());
            pDialog_1.setMessage("Saving Settings...");
            pDialog_1.setIndeterminate(false);
            pDialog_1.setCancelable(false);
            pDialog_1.show();
        }


        protected String doInBackground(String... args) {
            try {
                sqlite_helper new_DB;
                new_DB = new sqlite_helper(MainActivity.getContextOfApplication());

                Response_Server = new_DB.Insert_New_Settings(Temp_Server_IP, Temp_Printer_IP,
                        Temp_Display_Port);




            } catch (Exception e) {

                e.printStackTrace();
                Response_Server = e.getMessage();
                //Toast.makeText(getContext(), "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog_1.dismiss();
            if(Response_Server.equals("Insert Success!")){


                if(Prev_Trans.equals("Refresh")){
                    FragmentManager manager = getChildFragmentManager();
                    FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
                    manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);

                }else if(Prev_Trans.equals("Add_Ticket")){

                    frag_Select_Transaction nextFrag= new frag_Select_Transaction();
                    Bundle new_bundle = new Bundle();

                    new_bundle.putString("Server_IP", Temp_Server_IP);
                    new_bundle.putString("Printer_IP", Temp_Printer_IP);
                    new_bundle.putString("Display_Port", Temp_Display_Port);


                    nextFrag.setArguments(new_bundle);

                    getChildFragmentManager().beginTransaction()
                            //.remove(H.this)
                            .replace(R.id.view_initial_setup, nextFrag, "findThisFragment")
                            .setCustomAnimations(androidx.navigation.ui.R.anim.nav_default_enter_anim, androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                            .addToBackStack(null)
                            .commit();
                    getActionBar().setTitle("Select Transaction");

                }





            }else{
                Toast.makeText(getContext(), Response_Server , Toast.LENGTH_LONG).show();
            }
        }

    }
}



