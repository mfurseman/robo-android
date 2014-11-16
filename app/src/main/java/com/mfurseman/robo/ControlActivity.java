package com.mfurseman.robo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VerticalSeekBar;


public class ControlActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        bindSeekbarToTextView(
                (VerticalSeekBar) findViewById(R.id.left_motor_seekbar),
                (TextView)  findViewById(R.id.left_motor_textview));

        bindSeekbarToTextView(
                (VerticalSeekBar) findViewById(R.id.right_motor_seekbar),
                (TextView)  findViewById(R.id.right_motor_textview));
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
                textView.setText(Integer.toString(i));
            }
        });
    }
}
