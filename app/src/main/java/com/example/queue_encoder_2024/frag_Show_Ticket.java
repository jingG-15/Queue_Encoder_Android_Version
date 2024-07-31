package com.example.queue_encoder_2024;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.epson.epos2.Epos2CallbackCode;
import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frag_Show_Ticket#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag_Show_Ticket extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    TextView d_Ticket_Number;
    TextView d_Transaction;
    TextView d_Customer_Type;
    TextView d_SMS_Alert;

    Button butt_Done;
    Button butt_Re_Print;

    Bundle rec_bundle;

    String d_str_Last_Number;
    String d_str_Transaction;
    String d_str_Customer;
    String d_str_Cell_Number;
    String d_str_SMS_Agree;

    private ProgressDialog pDialog;

    Printer printer;

    Bitmap Logo;

    String json_message;

    String Settings_Printer_IP;

    public frag_Show_Ticket() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment frag_Show_Ticket.
     */
    // TODO: Rename and change types and number of parameters
    public static frag_Show_Ticket newInstance(String param1, String param2) {
        frag_Show_Ticket fragment = new frag_Show_Ticket();
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
        View root = inflater.inflate(R.layout.fragment_show_ticket, container, false);

        d_Customer_Type = root.findViewById(R.id.txt_Show_Customer);
        d_SMS_Alert = root.findViewById(R.id.txt_Show_SMS_Alert);
        d_Ticket_Number = root.findViewById(R.id.txt_Show_Ticket_Number);
        d_Transaction = root.findViewById(R.id.txt_Show_Transaction);

        butt_Done = root.findViewById(R.id.btn_Done);
        butt_Re_Print = root.findViewById(R.id.btn_Re_Print);

        rec_bundle = this.getArguments();
        if (rec_bundle != null) {
            d_str_Customer = rec_bundle.getString("f_d_Customer");
            d_str_Cell_Number = rec_bundle.getString("f_d_Cell_Number");
            d_str_Last_Number = rec_bundle.getString("f_d_Ticket_Num");
            d_str_Transaction =  rec_bundle.getString("f_d_Transaction");
            d_str_SMS_Agree = rec_bundle.getString("f_d_SMS_Agree");

            Settings_Printer_IP = rec_bundle.getString("Printer_IP");

            d_Customer_Type.setText(d_str_Customer);

            switch (d_str_SMS_Agree){
                case "Yes":
                    d_SMS_Alert.setText("SMS Alert: 0" + d_str_Cell_Number);
                    break;

                case "No":
                    d_SMS_Alert.setText("SMS Alert: No");
                    break;
            }

            d_Ticket_Number.setText(d_str_Last_Number);
            d_Transaction.setText(d_str_Transaction);


            butt_Done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    printer.clearCommandBuffer();

                    printer.setReceiveEventListener(null);
                    try {
                        printer.disconnect();
                        json_message = "Oke";
                    } catch (Epos2Exception e) {
                        e.printStackTrace();
                        int i = e.getErrorStatus();
                        json_message = Integer.toString(i);
                    }

//                    FragmentManager manager = getChildFragmentManager();
//                    FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
//                    manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);

//
//                    getParentFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    requireActivity().getOnBackPressedDispatcher().onBackPressed();
                }
            });

            new Prepare_Print_Data().execute();

        }

        return root;

    }
    class Prepare_Print_Data extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Printing...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {


            try {
                Logo = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(MainActivity.getContextOfApplication().getResources(), R.drawable.uuuu), 110, 110, true);


                printer = null;

                printer = new Printer(Printer.TM_T88, Printer.MODEL_SOUTHASIA, getContext());
                printer.setReceiveEventListener(frag_Show_Ticket.this::onPtrReceive);

                //printer.beginTransaction();
                printer.addPageBegin();
                printer.addPageArea(0,0, 512, 300);


                printer.addTextSize(1,1);
                printer.addText("\n");
                printer.addHPosition(150);
                printer.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.FALSE, Printer.COLOR_1);
                printer.addTextFont(Printer.FONT_B);
                printer.addText("Priority #:");

//                    printer.addHPosition(1);
//                    printer.addLogo(32, 33);


                printer.addTextSize(3,3);
                printer.addHPosition(150 + 120);
                printer.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.TRUE, Printer.COLOR_1);
                printer.addTextFont(Printer.FONT_B);
                printer.addText(d_str_Last_Number);

                printer.addTextSize(1,1);
                printer.addText("\n");
                printer.addHPosition(150);
                printer.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.FALSE, Printer.COLOR_1);
                printer.addTextFont(Printer.FONT_B);
                printer.addText("Transaction: ");




                printer.addTextSize(1,1);
                printer.addHPosition(150 + 120);
                printer.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.TRUE, Printer.COLOR_1);
                printer.addTextFont(Printer.FONT_B);
                printer.addText(d_str_Transaction);


                printer.addTextSize(1,1);
                printer.addText("\n");
                printer.addHPosition(150);
                printer.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.FALSE, Printer.COLOR_1);
                printer.addTextFont(Printer.FONT_B);
                printer.addText("Customer: ");




                printer.addTextSize(1,1);
                printer.addHPosition(150 + 120);
                printer.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.TRUE, Printer.COLOR_1);
                printer.addTextFont(Printer.FONT_B);
                printer.addText(d_str_Customer);

                printer.addTextSize(1,1);
                printer.addText("\n");
                printer.addHPosition(150);
                printer.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.FALSE, Printer.COLOR_1);
                printer.addTextFont(Printer.FONT_B);
                printer.addText("SMS Alert: ");

                printer.addHPosition(0);
                printer.addImage(Logo, 0,0,110,110, Printer.COLOR_1, Printer.MODE_MONO_HIGH_DENSITY, Printer.PARAM_DEFAULT,
                        1.0, Printer.COMPRESS_AUTO);
                //printer.addLogo(32,32);

                printer.addTextSize(1,1);
                printer.addHPosition(150 + 120);
                printer.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.TRUE, Printer.COLOR_1);
                printer.addTextFont(Printer.FONT_B);
                printer.addText(d_str_Cell_Number);

                Date nuw = new Date();
                SimpleDateFormat temp3 = new SimpleDateFormat("MMM dd, yyyy | hh:mm a", Locale.getDefault());
                String Date_Reg = temp3.format(nuw);

                printer.addTextSize(1,1);
                printer.addText("\n");
                printer.addHPosition(150);
                printer.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.FALSE, Printer.COLOR_1);
                printer.addTextFont(Printer.FONT_B);
                printer.addText( "Date: ");

                printer.addTextSize(1,1);
                printer.addHPosition(150 + 120);
                printer.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.TRUE, Printer.COLOR_1);
                printer.addTextFont(Printer.FONT_B);
                printer.addText(  Date_Reg);




                //printer.addPageArea(0,0, 512, 1);


                printer.addPageEnd();

                printer.addCut(Printer.CUT_NO_FEED);
                // printer.addPageBegin();

                //printer.addPageEnd();


                if(printer.getStatus().getConnection() == Printer.FALSE){
                    printer.connect("TCP:" + Settings_Printer_IP, Printer.PARAM_DEFAULT);

                }
                printer.sendData(Printer.PARAM_DEFAULT);

                //printer.clearCommandBuffer();
                json_message = "Request Sent.";

            }
            catch (Epos2Exception e) {
//Displays error messages
                int i = e.getErrorStatus();
                json_message = Integer.toString(i);
            }



            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();

            if(json_message.equals("Request Sent.")){

                Toast.makeText(getContext(), json_message, Toast.LENGTH_LONG).show();

                butt_Re_Print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            pDialog = new ProgressDialog(getContext());
                            pDialog.setMessage("Re-Printing...");
                            pDialog.setIndeterminate(false);
                            pDialog.setCancelable(false);
                            pDialog.show();
                            //printer.connect("TCP:192.168.1.246", Printer.PARAM_DEFAULT);
                            if(printer.getStatus().getConnection() == Printer.FALSE){
                                printer.connect("TCP:" + Settings_Printer_IP, Printer.PARAM_DEFAULT);

                            }
                            printer.sendData(Printer.PARAM_DEFAULT);
                            pDialog.dismiss();
                        } catch (Epos2Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), e.getErrorStatus(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }else if(json_message.equals("1")){
                Toast.makeText(getContext(), "An invalid parameter was passed.", Toast.LENGTH_LONG).show();
                //new Disconnect_Printer().execute();
            }else if(json_message.equals("2")){
                Toast.makeText(getContext(), "Not enough memory on the printer allocated.", Toast.LENGTH_LONG).show();
                //new Disconnect_Printer().execute();
            }else if(json_message.equals("3")){
                Toast.makeText(getContext(), "An unknown error has occurred.", Toast.LENGTH_LONG).show();
                //new Disconnect_Printer().execute();
            }else{

                Toast.makeText(getContext(), json_message, Toast.LENGTH_LONG).show();
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




}