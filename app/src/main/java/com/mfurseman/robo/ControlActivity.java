package com.mfurseman.robo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VerticalSeekBar;


public class ControlActivity extends Activity {

    /* Bind Android views to local variables */
    private VerticalSeekBar leftMotorSeekbar;
    private VerticalSeekBar rightMotorSeekbar;
    private TextView leftMotorTextView;
    private TextView rightMotorTextView;
    private Button coastButton;
    private Button stopButton;
    private Button connectButton;
    private EditText addressEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        leftMotorSeekbar = (VerticalSeekBar) findViewById(R.id.left_motor_seekbar);
        rightMotorSeekbar = (VerticalSeekBar) findViewById(R.id.right_motor_seekbar);
        leftMotorTextView = (TextView)  findViewById(R.id.left_motor_textview);
        rightMotorTextView = (TextView) findViewById(R.id.right_motor_textview);
        coastButton = (Button) findViewById(R.id.coast_button);
        stopButton = (Button) findViewById(R.id.stop_button);
        connectButton = (Button) findViewById(R.id.connect_button);
        addressEditText = (EditText) findViewById(R.id.address_edit_text);

        bindSeekbarToTextView(leftMotorSeekbar, leftMotorTextView);
        bindSeekbarToTextView(rightMotorSeekbar, rightMotorTextView);

        coastButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                coast();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                stop();
            }
        });
        connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                connect();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void bindSeekbarToTextView(final SeekBar seekBar, final TextView textView) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // TODO: Send motor value to server
                textView.setText(Integer.toString(translateSeekbarToMotor(i)));
            }
        });
    }

    private void coast() {
        // TODO: Actually send coast command to server
        leftMotorSeekbar.setPosition(translateMotorToSeekbar(0));
        rightMotorSeekbar.setPosition(translateMotorToSeekbar(0));
    }

    private void stop() {
        // TODO: Send stop command to server
        leftMotorSeekbar.setPosition(translateMotorToSeekbar(0));
        rightMotorSeekbar.setPosition(translateMotorToSeekbar(0));
    }

    private void connect() {
        //TODO: Attempt to connect to a socket
        String address = addressEditText.getText().toString();
        Log.d("mfurseman", "Connect" + address);
        Toast.makeText(this, "Connection to " + address + " failed.", Toast.LENGTH_SHORT).show();
    }

    private int translateSeekbarToMotor(int seekbar) {
        return seekbar - 128;
    }

    private int translateMotorToSeekbar(int motor) {
        return motor + 128;
    }
}
