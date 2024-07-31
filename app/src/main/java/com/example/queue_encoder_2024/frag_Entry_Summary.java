package com.example.queue_encoder_2024;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frag_Entry_Summary#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag_Entry_Summary extends Fragment {


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


    Bundle rec_bundle;

    ImageButton img_Sel_Transaction;
    ImageButton img_Sel_Customer;
    ImageButton img_Sel_Text_Alert;

    TextView txt_Sel_Transaction;
    TextView txt_Sel_Customer;
    TextView txt_Sel_Text_Alert;

    Button Confirm_Details;
    Button Cancel_Trans;

    String str_Sel_Transaction;
    String str_Sel_Customer;
    String str_Sel_SMS_Alert;
    String str_Mobile_Number;

    String db_Transaction;
    String db_Customer;
    String db_Last_Number;
    String db_Assigned_Window;
    String db_Status;
    String db_Cell_Number;



    String Debug_String;
    String json_message;

    String Response_Server;

    private ProgressDialog pDialog;

    String f_prnt_Transaction;
    String f_prnt_Customer;
    String f_prnt_Last_Number;
    String f_prnt_Cell_Number;


    String Settings_Server_IP;
    String Settings_Printer_IP;
    String Settings_Display_Port;



    public frag_Entry_Summary() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment frag_Entry_Summary.
     */
    // TODO: Rename and change types and number of parameters
    public static frag_Entry_Summary newInstance(String param1, String param2) {
        frag_Entry_Summary fragment = new frag_Entry_Summary();
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

        rec_bundle = this.getArguments();
        getActionBar().setTitle("Warning");
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_entry_summary, container, false);





        img_Sel_Customer = root.findViewById(R.id.btnimg_Sum_Customer);
        img_Sel_Text_Alert = root.findViewById(R.id.btnimg_Sum_SMS_Alert);
        img_Sel_Transaction = root.findViewById(R.id.btnimg_Sum_Transaction);

        txt_Sel_Customer = root.findViewById(R.id.txt_Sum_Customer);
        txt_Sel_Text_Alert = root.findViewById(R.id.txt_Sum_SMS_Alert);
        txt_Sel_Transaction = root.findViewById(R.id.txt_Sum_Transaction);

        Confirm_Details = root.findViewById(R.id.btn_Sum_Confirm);
        Cancel_Trans = root.findViewById(R.id.btn_Sum_Cancel);

        if (rec_bundle != null) {
            str_Sel_Customer = rec_bundle.getString("Customer");
            str_Sel_SMS_Alert = rec_bundle.getString("SMS_Agree");
            str_Sel_Transaction = rec_bundle.getString("Transaction");
            str_Mobile_Number =  rec_bundle.getString("Phone Number");

            Settings_Display_Port = rec_bundle.getString("Display_Port");
            Settings_Printer_IP = rec_bundle.getString("Printer_IP");
            Settings_Server_IP = rec_bundle.getString("Server_IP");

        }

        switch (str_Sel_Transaction) {
            case "Pay Bills":
                txt_Sel_Transaction.setText("Transaction: Pay Bills");
                img_Sel_Transaction.setImageResource(R.drawable.bills_not_pressed);
                db_Transaction = "Bills Payment";
                db_Assigned_Window = "Teller";
                break;
            case "Complaints":
                txt_Sel_Transaction.setText("Transaction: Complaints");
                img_Sel_Transaction.setImageResource(R.drawable.complaints_not_pressed1);
                db_Transaction = "Complaints";
                db_Assigned_Window = "CSR";
                break;
            case "Apply Connection":
                txt_Sel_Transaction.setText("Transaction: Apply Connection");
                img_Sel_Transaction.setImageResource(R.drawable.connect_not_pressed);
                db_Transaction = "Apply Connection / Reconnection";
                db_Assigned_Window = "CSR";
                break;
            case "Pay Application Fee":
                txt_Sel_Transaction.setText("Transaction: Pay Application Fee");
                img_Sel_Transaction.setImageResource(R.drawable.pay_application);
                db_Transaction = "Pay Application Fee";
                db_Assigned_Window = "Cashier";
                break;
        }


        switch (str_Sel_Customer) {
            case "Regular":
                txt_Sel_Customer.setText("Customer: Regular");
                img_Sel_Customer.setImageResource(R.drawable.regular_not_pressed);
                db_Customer = "Regular";
                break;
            case "PWD":
                txt_Sel_Customer.setText("Customer: PWD");
                img_Sel_Customer.setImageResource(R.drawable.pwd_not_pressed1);
                db_Customer = "PWD or Senior";
                break;
            case "Senior":
                txt_Sel_Customer.setText("Customer: Senior");
                img_Sel_Customer.setImageResource(R.drawable.senior_not_pressed);
                db_Customer = "PWD or Senior";
                break;
        }

        switch (str_Sel_SMS_Alert){
            case "Yes":
                txt_Sel_Text_Alert.setText("SMS Alert: 0" + str_Mobile_Number);
                img_Sel_Text_Alert.setImageResource(R.drawable.mobile_alert);
                db_Cell_Number = "0" + str_Mobile_Number;
                break;

            case "No":
                txt_Sel_Text_Alert.setText("SMS Alert: No");
                img_Sel_Text_Alert.setImageResource(R.drawable.mobile_alert);
                db_Cell_Number = "No";
                break;
        }

        Confirm_Details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db_Status = "Pending";

                new Get_Last_Ticket().execute();



            }
        });

        Cancel_Trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getChildFragmentManager();
                FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
                manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
        return root;
    }


    class Insert_New_Queue_Ticket extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Saving Queue Ticket. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {
            try {
                String link="http://" + Settings_Server_IP + ":8080/Queue_Encoder/InsertNewTicekt.php";
                HashMap<String,String> data_1 = new HashMap<>();
                int Last_Count = Integer.parseInt(db_Last_Number);
                Last_Count += 1;
                data_1.put("Last_Queue_Num", Integer.toString(Last_Count));
                data_1.put("Customer_Type", db_Customer);
                data_1.put("Transaction", db_Transaction);
                data_1.put("Assigned_Window", db_Assigned_Window);
                data_1.put("Status", db_Status);
                data_1.put("Cell_Number", db_Cell_Number);

                f_prnt_Last_Number = Integer.toString(Last_Count);
                f_prnt_Customer = str_Sel_Customer;
                f_prnt_Transaction = str_Sel_Transaction;
                f_prnt_Cell_Number = db_Cell_Number;


                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write( getPostDataString(data_1) );
                wr.flush();

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
                    JSONArray jsonArray = new JSONArray(line);
                    JSONObject obj = jsonArray.getJSONObject(0);
                    json_message = obj.getString("message");
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
            if(json_message.equals("Data Submit Successfully")){

                //TODO Print Receipt
                //new Print_Ticket().execute();
                new Send_Update_Command().execute();

                frag_Show_Ticket nextFrag= new frag_Show_Ticket();


                rec_bundle.putString("f_d_Ticket_Num", f_prnt_Last_Number);
                rec_bundle.putString("f_d_Transaction", f_prnt_Transaction);
                rec_bundle.putString("f_d_Customer", f_prnt_Customer);
                rec_bundle.putString("f_d_Cell_Number", f_prnt_Cell_Number);
                rec_bundle.putString("f_d_SMS_Agree", str_Sel_SMS_Alert);

                nextFrag.setArguments(rec_bundle);

                getChildFragmentManager().beginTransaction()
                        //.remove(H.this)
                        .replace(R.id.view_entry_summary, nextFrag, "findThisFragment")
                        .setCustomAnimations(androidx.navigation.ui.R.anim.nav_default_enter_anim, androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                        .addToBackStack(null)
                        .commit();
                getActionBar().setTitle("Ticket Summary");


                //Toast.makeText(getContext(), json_message_1 , Toast.LENGTH_LONG).show();

            }else if(json_message.equals("No Response from server")){
                Toast.makeText(getContext(), json_message + ". Please check your internet connection or contact BILECO IT division for technical details.", Toast.LENGTH_LONG).show();
            }else if(json_message.equals("Try Again Err: 10")){
                Toast.makeText(getContext(), json_message + ". \n\nPlease try again.", Toast.LENGTH_LONG).show();
                //Toast.makeText(getContext(), json_message_1 , Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getContext(), json_message.replace("222.127.226.225:8081", "Server").replace("/", "").replace("222.127.226.225:8080", "Server") , Toast.LENGTH_LONG).show();
            }
        }

    }








    class Get_Last_Ticket extends AsyncTask<String, String, String> {



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
                json_message = "Unknown Error has occurred.";
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
                    Log.d("Debug", line);
                    sb.append(line);
                    break;
                }

                reader.close();
                if (line != null){
                    Debug_String = line;
                    JSONArray jsonArray = new JSONArray(line);
                    JSONObject obj = jsonArray.getJSONObject(0);

                    json_message = obj.getString("message");
                    if(json_message.equals("Good")){
                        db_Last_Number = obj.getString("Last_Entry");
                    }else if(json_message.equals("Init")){
                        db_Last_Number = "0";
                    }else{
                        db_Last_Number = "";
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

                new Insert_New_Queue_Ticket().execute();

            }else if(json_message.equals("None")){

                Toast.makeText(getContext(), "Please try again." + "\n\n" + Debug_String , Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getContext(), json_message.replace(Settings_Server_IP + ":8080" , "Server") + "\n" + Debug_String , Toast.LENGTH_LONG).show();
            }
        }

    }


    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    class Send_Update_Command extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Sending Reset Command. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {



            try {
                String messageStr = "00" + "UPDT" + getLocalIpAddress();
                int server_port = Integer.parseInt(Settings_Display_Port);
                InetAddress local = InetAddress.getByName(Settings_Server_IP);
                int msg_length = messageStr.length();
                byte[] message = messageStr.getBytes();

                DatagramSocket s = new DatagramSocket();


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
            pDialog.dismiss();
            if(Response_Server.equals("Request Sent.")){


                Toast.makeText(getContext(), "Command Sent." , Toast.LENGTH_LONG).show();


            }else {
                Toast.makeText(getContext(), Response_Server, Toast.LENGTH_LONG).show();
            }
        }

    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = (NetworkInterface) ((Enumeration<?>) en).nextElement();
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
}