package com.mfurseman.robo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VerticalSeekBar;

import java.util.ArrayList;
import java.util.List;


public class ControlActivity extends Activity implements SocketConnectionAdapter {

    public static final String TAG = "Robo";

    private ArrayList<String> commands = new ArrayList<String>();
    private OnConnection onConnection;
    private OnCommandReceived onCommandReceived;
    private Handler handler;

    /* Bind Android views to local variables */
    private VerticalSeekBar leftMotorSeekbar;
    private VerticalSeekBar rightMotorSeekbar;
    private TextView leftMotorTextView;
    private TextView rightMotorTextView;
    private Button coastButton;
    private Button stopButton;
    private Button connectButton;
    private Button onButton;
    private Button offButton;
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
        onButton = (Button) findViewById(R.id.on_button);
        offButton = (Button) findViewById(R.id.off_button);
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
        onButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setOnBoardLedOn();
            }
        });
        offButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setOnBoardLedOff();
            }
        });

        handler = new Handler();
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
            private int position = 0;
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText(Integer.toString(translateSeekbarToMotor(position)));
                // TODO: Send motor value to server
            }
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                position = i;
            }
        });
    }

    private void coast() {
        // TODO: Actually send coast command to server
        leftMotorSeekbar.setProgressAndThumb(translateMotorToSeekbar(0));
        rightMotorSeekbar.setProgressAndThumb(translateMotorToSeekbar(0));
    }

    private void stop() {
        // TODO: Send stop command to server
        leftMotorSeekbar.setProgressAndThumb(translateMotorToSeekbar(0));
        rightMotorSeekbar.setProgressAndThumb(translateMotorToSeekbar(0));
    }

    private void connect() {
        String address = addressEditText.getText().toString();
        RoboClient roboClient = new RoboClient(address, this);
        new Thread(roboClient).start();
    }

    private void setOnBoardLedOn() {
        commands.add("b"); // TODO: Move command creation out
    }

    private void setOnBoardLedOff() {
        commands.add("c");
    }

    private int translateSeekbarToMotor(int seekbar) {
        return seekbar - 128;
    }

    private int translateMotorToSeekbar(int motor) {
        return motor + 128;
    }

    private class OnConnection implements Runnable {
        @Override
        public void run() {
            addressEditText.setEnabled(false);
            connectButton.setEnabled(false);
            onButton.setEnabled(true);
            offButton.setEnabled(true);
            coastButton.setEnabled(true);
            stopButton.setEnabled(true);
            leftMotorTextView.setEnabled(true);
            leftMotorSeekbar.setEnabled(true);
            rightMotorTextView.setEnabled(true);
            rightMotorSeekbar.setEnabled(true);
        }
    }

    private class OnCommandReceived implements Runnable {
        @Override
        public void run() {
            // TODO: Something with the received commands
        }
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    @Override
    public Runnable getOnConnection() {
        if(onConnection == null) {
            onConnection = new OnConnection();
        }
        return onConnection;
    }

    @Override
    public Runnable getOnCommandReceived() {
        if(onCommandReceived == null) {
            onCommandReceived = new OnCommandReceived();
        }
        return onCommandReceived;
    }

    @Override
    public List<String> getCommandList() {
        return commands;
    }
}
