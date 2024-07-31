package com.example.queue_encoder_2024;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frag_Settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag_Settings extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    Button butt_Test_Server;
    Button butt_Test_Printer;
    Button butt_save_Settings;
    Button butt_Reset_Queue;

    EditText info_Server_new;
    EditText info_Printer_new;
    EditText info_Display_Port_new;

    private ProgressDialog pDialog_1;

    Printer printer;

    String Response_Server;

    String Temp_Server_IP = "";
    String Temp_Printer_IP = "";
    String Temp_Display_Port = "";

    sqlite_helper myDB;

    Bundle rec_bundle;

    String Reg_Count;
    String PWD_Count;
    String Total_Count;
    String Last_Num;




    public frag_Settings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_Settings.
     */
    // TODO: Rename and change types and number of parameters
    public static frag_Settings newInstance(String param1, String param2) {
        frag_Settings fragment = new frag_Settings();
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

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        butt_Reset_Queue = root.findViewById(R.id.btn_Reset_Queue);
        butt_save_Settings = root.findViewById(R.id.btn_Save);
        butt_Test_Printer = root.findViewById(R.id.btn_Test_Printer);
        butt_Test_Server = root.findViewById(R.id.btn_Test_Server);

        info_Display_Port_new = root.findViewById(R.id.txtin_Display_Port);
        info_Printer_new = root.findViewById(R.id.txtin_Printer_IP);
        info_Server_new = root.findViewById(R.id.txtin_Server_IP);


        new Prep_printer_data().execute();

        rec_bundle = this.getArguments();

        if (rec_bundle != null) {

            Last_Num = rec_bundle.getString("Last_Ticket_Number");
            Reg_Count = rec_bundle.getString("Regular_Count");
            PWD_Count = rec_bundle.getString("PWD/Senior_Count");
            Total_Count = rec_bundle.getString("Total_Queue");
        }

        myDB = new sqlite_helper(getContext());
        Cursor res = myDB.Check_Data_Exists();
        if (res.getCount() != 0) {
            if (res.moveToFirst()) {
                while (!res.isAfterLast()) {
                    Temp_Server_IP = res.getString(res.getColumnIndex("Server_IP"));
                    Temp_Printer_IP = res.getString(res.getColumnIndex("Printer_IP"));
                    Temp_Display_Port = res.getString(res.getColumnIndex("Display_Port"));

                    info_Server_new.setText(Temp_Server_IP);
                    info_Printer_new.setText(Temp_Printer_IP);
                    info_Display_Port_new.setText(Temp_Display_Port);


                    // do what ever you want here
                    res.moveToNext();
                }
            }
        }
        res.close();




        butt_Test_Server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Temp_Server_IP = info_Server_new.getText().toString().trim();
                Temp_Display_Port = info_Display_Port_new.getText().toString().trim();
                if(Temp_Server_IP.isEmpty()) {
                    info_Server_new.setError("Enter a valid IP Address");
                    info_Server_new.requestFocus();
                    return;
                }else if(Temp_Display_Port.isEmpty()){
                    info_Display_Port_new.setError("Enter a valid Port Number");
                    info_Display_Port_new.requestFocus();
                    return;
                }else {

                    //TODO Call Server Test Function

                    new Test_Server_Conn().execute();

                    if(!Temp_Server_IP.equals("") && !Temp_Printer_IP.equals("") && !Temp_Display_Port.equals("")){
                        butt_save_Settings.setVisibility(View.VISIBLE);
                    }


                }


            }
        });

        butt_Test_Printer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Temp_Printer_IP = info_Printer_new.getText().toString().trim();
                if(Temp_Printer_IP.isEmpty()){
                    info_Printer_new.setError("Enter a valid IP Address");
                    info_Printer_new.requestFocus();
                    return;
                }else {

                    //TODO Call Printer Test Function

                    new Send_Printer_Data().execute();


                    if(!Temp_Server_IP.equals("") && !Temp_Printer_IP.equals("") && !Temp_Display_Port.equals("")){
                        butt_save_Settings.setVisibility(View.VISIBLE);
                    }


                }


            }
        });


        butt_save_Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Save_to_SQLite_Data().execute();




            }
        });

        butt_Reset_Queue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.getContextOfApplication());

                builder.setTitle("Are you sure to Reset Queue Count?");
                builder.setMessage("\nLast Ticket Number: " + Last_Num + "\n" +
                        "Regular Customers: " + Reg_Count + "\n" +
                        "PWD/Senior Customers: " + PWD_Count + "\n\n" +
                        "Total Customers: " + Total_Count + "\n\n" +
                        "All existing tickets as of the moment will not be valid anymore.");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        if (ContextCompat.checkSelfPermission(MainActivity.getContextOfApplication(), android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(MainActivity.getContextOfApplication(), android.Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(MainActivity.getContextOfApplication(), android.Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED
                        ){
                            //Toast.makeText(getContext(), getLocalIpAddress(), Toast.LENGTH_LONG).show();
                            new Send_Reset_Socket().execute();


                        }else{
                            ActivityCompat.requestPermissions(MainActivity.getContextOfApplication(), new String[]{
                                    android.Manifest.permission.INTERNET,
                                    android.Manifest.permission.ACCESS_NETWORK_STATE,
                                    Manifest.permission.ACCESS_WIFI_STATE
                            }, 100);
                        }



                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                builder.create();
                builder.show();


            }
        });

        return root;
    }



    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = (NetworkInterface) ((Enumeration) en).nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
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
                printer.setReceiveEventListener(frag_Settings.this::onPtrReceive);

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
                printer.clearCommandBuffer();

                printer.setReceiveEventListener(null);
                if(printer.getStatus().getConnection() == Printer.TRUE){


                    try {
                        printer.disconnect();
                    } catch (Epos2Exception e) {
                        e.printStackTrace();
                    }
                }



//                FragmentManager manager = getParentFragmentManager();
//                FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
//                manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);

//                getParentFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Toast.makeText(getContext(), "Settings Saved" , Toast.LENGTH_LONG).show();
                requireActivity().getOnBackPressedDispatcher().onBackPressed();

            }else{
                Toast.makeText(getContext(), Response_Server , Toast.LENGTH_LONG).show();
            }
        }

    }


    class Send_Reset_Socket extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog_1 = new ProgressDialog(getContext());
            pDialog_1.setMessage("Sending Reset Command. Please wait...");
            pDialog_1.setIndeterminate(false);
            pDialog_1.setCancelable(false);
            pDialog_1.show();
        }


        protected String doInBackground(String... args) {



            try {
                String messageStr = "00" + "RSET" + getLocalIpAddress();
                int server_port = Integer.parseInt(Temp_Display_Port);
                InetAddress local = InetAddress.getByName(Temp_Server_IP);
                int msg_length = messageStr.length();
                byte[] message = messageStr.getBytes();


                DatagramSocket s = new DatagramSocket();
                //

                DatagramPacket p = new DatagramPacket(message, msg_length, local, server_port);
                s.send(p);//properly able to send data. i receive data to server

                Response_Server = "Request Sent.";
            } catch (IOException e) {
                e.printStackTrace();
                Response_Server = e.getMessage();
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


                Toast.makeText(getContext(), "Request Sent." , Toast.LENGTH_LONG).show();


            }else {
                Toast.makeText(getContext(), Response_Server, Toast.LENGTH_LONG).show();
            }
        }

    }
}