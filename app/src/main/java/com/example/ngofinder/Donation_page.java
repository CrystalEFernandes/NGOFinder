package com.example.ngofinder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.ngofinder.Model.DonationModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class Donation_page extends AppCompatActivity implements PaymentResultListener {
    EditText amount, remark;
    private Button stats,login;
    Button donateBtn;
    int famount = 0;
    String charityId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_page);

        Checkout.preload(getApplicationContext());
        donateBtn = (Button) findViewById(R.id.donateBtn);
        amount = (EditText) findViewById(R.id.amount); // initialize amount EditText
        remark = (EditText) findViewById(R.id.remark); // initialize remark EditText
        Intent intent = getIntent();
        charityId = intent.getStringExtra("charity_id");

        donateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount_don = amount.getText().toString().trim(); // get amount from EditText
                PaymentNow(amount_don+"00"); // pass amount to PaymentNow method
            }
        });

    }

    private void PaymentNow(String amount) {
        final Activity activity = this;
        String samount = String.valueOf(amount);
        int famount = Math.round(Float.parseFloat(samount));

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_live_9sVXb9hJHWB7yj");
        checkout.setImage(R.drawable.ic_love);

        try {
            JSONObject options = new JSONObject();

            options.put("name", "Merchant Name");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", famount);//pass amount in currency subunits
            options.put("prefill.email", "gaurav.kumar@example.com");
            options.put("prefill.contact","9988776655");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    public void process() {
        String amount_don = amount.getText().toString().trim();
        String remark_don = remark.getText().toString().trim();

        DonationModel obj = new DonationModel(amount_don, remark_don);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference node=db.getReference("Donations").child(charityId); // modify the node reference to "Donations" and add charityId
        String donateId = node.push().getKey(); // generate a unique ID for "Donated" node
        node.child(donateId).child("Amount").setValue(amount_don); // set "amount" field inside "Donated" node
        node.child(donateId).child("Remark").setValue(remark_don); // set "remark" field inside "Donated" node

        amount.setText("");
        remark.setText("");
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();

    }
}