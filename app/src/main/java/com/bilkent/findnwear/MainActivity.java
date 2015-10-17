package com.bilkent.findnwear;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.cengalabs.flatui.FlatUI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
// Converts the default values (radius, size, border) to dp to be compatible with different
// screen sizes. If you skip this there may be problem with different screen densities
        FlatUI.initDefaultValues(this);

// Setting default theme to avoid to add the attribute "theme" to XML
// and to be able to change the whole theme at once
        FlatUI.setDefaultTheme(FlatUI.DEEP);
        FlatUI.setDefaultTheme(R.array.deep);    // for using custom theme as default

// Getting action bar drawable and setting it.
// Sometimes weird problems may occur while changing action bar drawable at runtime.
// You can try to set title of the action bar to invalidate it after setting background.
        //getActionBar().setBackgroundDrawable(FlatUI.getActionBarDrawable(MainActivity.this, FlatUI.DEEP, false));
        //getSupportActionBar().setBackgroundDrawable(FlatUI.getActionBarDrawable(MainActivity.this,FlatUI.DEEP, false));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
