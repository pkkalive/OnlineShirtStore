package com.kumar.shirtstore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kumar.shirtstore.interfaces.HttpUrl;
import com.kumar.shirtstore.model.CartItems;
import com.kumar.shirtstore.service.MyService;
import com.kumar.shirtstore.utils.NetworkHelper;
import com.kumar.shirtstore.utils.RequestPackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements HttpUrl  {

    boolean networkOk;
    List<CartItems> cartItemsList;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    String[] mCategories;
    RecyclerView mRecyclerView;
    CartItemAdapter mCartItemAdapter;
    Map<String, Bitmap> mBitmaps = new HashMap<>();

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            CartItems[] cartItems = (CartItems[]) intent
                    .getParcelableArrayExtra(MyService.MY_SERVICE_PAYLOAD);
            if (cartItems !=null) {
                Toast.makeText(MainActivity.this,
                        "Received " + cartItems.length + " items from the service",
                        Toast.LENGTH_LONG).show();
                cartItemsList = Arrays.asList(cartItems);

            } else {
                Toast.makeText(MainActivity.this,
                        "Please wait retriving the information",
                        Toast.LENGTH_LONG).show();
                displayDataItems(null);
            }
            displayDataItems(null);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvItems);
        networkOk = NetworkHelper.hasNetworkAccess(this);
        //      Code to manage sliding navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mCategories = getResources().getStringArray(R.array.colour);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, mCategories));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = mCategories[position];
                Toast.makeText(MainActivity.this, "You chose " + category,
                        Toast.LENGTH_SHORT).show();
                mDrawerLayout.closeDrawer(mDrawerList);
                displayDataItems(category);
            }
        });
//      end of navigation drawer
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean grid = settings.getBoolean(getString(R.string.pref_display_grid), false);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvItems);
        if (grid) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
        runIntent();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(MyService.MY_SERVICE_MESSAGE));
    }

    private void runIntent(){
        if (networkOk) {
            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setEndPoint(JSON_URL);
            requestPackage.setParam("colour", "blue");
            requestPackage.setMethod("GET");
            Intent intent = new Intent(this, MyService.class);
            intent.putExtra(MyService.REQUEST_PACKAGE, requestPackage);
            startService(intent);
        } else {
            Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayDataItems(String category) {
        if (cartItemsList != null) {
            mCartItemAdapter = new CartItemAdapter(this, cartItemsList, mBitmaps);
            mRecyclerView.setAdapter(mCartItemAdapter);
        }
    }

    private void filterByColour(final String colour) {

        List<CartItems> cartItemsListByColour = new ArrayList<>();

        try {
            for (int position = 0; position < cartItemsList.size(); position++) {
                if (cartItemsList.get(position).getColour().equals(colour)) {
                    cartItemsListByColour.add(cartItemsList.get(position));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof NullPointerException){
                Toast.makeText(MainActivity.this,
                        "There are no items to be loaded",
                        Toast.LENGTH_LONG).show();
            }
        }

        Toast.makeText(MainActivity.this,
                "Filtered to " + cartItemsListByColour.size() + " items from the service",
                Toast.LENGTH_LONG).show();
        if (cartItemsListByColour != null) {
            mCartItemAdapter = new CartItemAdapter(this, cartItemsListByColour, mBitmaps);
            mRecyclerView.setAdapter(mCartItemAdapter);
        }
    }

    private void filterBySize(final String size) {

        List<CartItems> cartItemsListBySize = new ArrayList<>();
        for (int position = 0; position < cartItemsList.size(); position++) {
            if (cartItemsList.get(position).getSize().equals(size)){
                cartItemsListBySize.add(cartItemsList.get(position));
            }
        }
        Toast.makeText(MainActivity.this,
                "Filtered to " + cartItemsListBySize.size() + " items from the service",
                Toast.LENGTH_LONG).show();
        if (cartItemsListBySize != null) {
            mCartItemAdapter = new CartItemAdapter(this, cartItemsListBySize, mBitmaps);
            mRecyclerView.setAdapter(mCartItemAdapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.shopping_cart:
                Toast.makeText(MainActivity.this, "You clicked: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                // Show the settings screen
                Intent settingsIntent = new Intent(this, PrefsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.filter_by_colour:
                Toast.makeText(MainActivity.this, "You clicked: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Filter by color");
                alert.setMessage("please enter your desired shirt colour");
                final EditText input = new EditText(this);
                alert.setView(input);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String filterByColour = input.getEditableText().toString();
                        if (filterByColour.isEmpty()){
                            Toast.makeText(MainActivity.this, " You haven't entered your desired colour",
                                    Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        } else {
                            filterByColour(filterByColour);
                        }
                    }
                });
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
                return true;

            case R.id.filter_by_size:
                Toast.makeText(MainActivity.this, "You clicked: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Filter by size");
                builder.setMessage("Please enter your desired shirt size");
                final EditText text = new EditText(this);
                builder.setView(text);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String filterBySize = text.getEditableText().toString();
                        if (filterBySize.isEmpty()){
                            Toast.makeText(MainActivity.this, " You haven't entered your desired size",
                                    Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        } else{
                            filterBySize(filterBySize);
                        }
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}