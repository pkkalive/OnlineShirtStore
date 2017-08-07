package com.kumar.shirtstore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.kumar.shirtstore.interfaces.HttpUrl;
import com.kumar.shirtstore.model.CartItems;
import com.kumar.shirtstore.service.MyService;
import com.kumar.shirtstore.utils.NetworkHelper;


import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements HttpUrl {

//    ListView productList;
    private boolean networkOk;
    List<CartItems> mCartItems;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    String[] mCategories;
    RecyclerView mRecyclerView;
    CartItemAdapter mItemAdapter;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            CartItems[] cartItems = (CartItems[]) intent
                    .getParcelableArrayExtra(MyService.MY_SERVICE_PAYLOAD);
            if (cartItems != null) {
                Toast.makeText(MainActivity.this,
                        "Received " + cartItems.length + " items from the service",
                        Toast.LENGTH_LONG).show();
                mCartItems = Arrays.asList(cartItems);
            } else {
                Toast.makeText(MainActivity.this,
                        "Received 0 items from the service",
                        Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvItems);

        networkOk = NetworkHelper.hasNetworkAccess(this);

//        productList = (ListView) findViewById(R.id.products_listview);

        runIntent();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(MyService.MY_SERVICE_MESSAGE));


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
            case R.id.filter_by_size:
                Toast.makeText(MainActivity.this, "You clicked: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.filter_by_color:
                Toast.makeText(MainActivity.this, "You clicked: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.shopping_cart:
                Toast.makeText(MainActivity.this, "You clicked: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void runIntent(){
        if (networkOk) {
            Intent intent = new Intent(this, MyService.class);
            intent.setData(Uri.parse(JSON_URL));
            startService(intent);
        } else {
            Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
        }
    }

}
