package com.kumar.shirtstore;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kumar.shirtstore.model.CartItems;
import com.kumar.shirtstore.model.CartItemsList;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import static com.kumar.shirtstore.service.MyService.ITEM_ID_KEY;
import static com.kumar.shirtstore.service.MyService.KEY;
import static com.kumar.shirtstore.service.MyService.SAVED_CART;

/**
 * Created by Purushotham on 08/08/17.
 */

public class DetailActivity extends AppCompatActivity {

    private TextView tvName, tvDescription, tvPrice;
    private ImageView itemImage;
    private Button addCart, viewCart;
    CartItems item;
    CartItemsList cartItemsLists = new CartItemsList();
    Intent intent ;
    Bundle mBundle = new Bundle();
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        item = getIntent().getExtras().getParcelable(CartItemAdapter.ITEM_KEY);
        if (item == null) {
            throw new AssertionError("Null data item received!");
        }

        tvName = (TextView) findViewById(R.id.tvItemName);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        itemImage = (ImageView) findViewById(R.id.itemImage);

        tvName.setText(item.getName());
        tvDescription.setText("Colour:" + item.getColour() + "\n"
                + "Size: " + item.getSize());

        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
        tvPrice.setText(nf.format(item.getPrice()));

        String imageURL = item.getPicture();
        Picasso.with(this)
                .load(imageURL)
                .resize(50, 50)
                .into(itemImage);

        addCart = (Button) findViewById(R.id.addCart);
        addCart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                addCart();
            }
        });

        viewCart = (Button) findViewById(R.id.viewCart);
        viewCart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                viewCart();
            }
        });
    }

    public static void savePreferencesBundle(SharedPreferences.Editor editor, String key, Bundle preferences) {
        Set<String> keySet = preferences.keySet();
        Iterator<String> it = keySet.iterator();
        String prefKeyPrefix = key + SAVED_CART;

        while (it.hasNext()){
            String bundleKey = it.next();
            Object o = preferences.get(bundleKey);
            if (o == null){
                editor.remove(prefKeyPrefix + bundleKey);
            } else if (o instanceof Integer){
                editor.putInt(prefKeyPrefix + bundleKey, (Integer) o);
            } else if (o instanceof Long){
                editor.putLong(prefKeyPrefix + bundleKey, (Long) o);
            } else if (o instanceof Boolean){
                editor.putBoolean(prefKeyPrefix + bundleKey, (Boolean) o);
            } else if (o instanceof CharSequence){
                editor.putString(prefKeyPrefix + bundleKey, ((CharSequence) o).toString());
            } else if (o instanceof Bundle){
                savePreferencesBundle(editor, prefKeyPrefix + bundleKey, ((Bundle) o));
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        for (int i = 0; i < menu.size(); i++){
            if (menu.getItem(i).getItemId() == R.id.filter_by_size){
                menu.getItem(i).setVisible(false);
            } else if (menu.getItem(i).getItemId() == R.id.filter_by_colour) {
                menu.getItem(i).setVisible(false);
            } else if (menu.getItem(i).getItemId() == R.id.action_settings) {
                menu.getItem(i).setVisible(false);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.shopping_cart:
                Toast.makeText(DetailActivity.this, "You clicked: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addCart(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add number of shirts");
        builder.setMessage("Please enter your desired shirts");
        final EditText text = new EditText(this);
        text.setInputType(InputType.TYPE_CLASS_NUMBER);
        text.setRawInputType(Configuration.KEYBOARD_12KEY);
        builder.setView(text);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String quantity = text.getEditableText().toString();
                Toast.makeText(DetailActivity.this, "Quantity is " + Integer.parseInt(quantity),
                        Toast.LENGTH_SHORT).show();
                if (quantity.isEmpty()){
                    Toast.makeText(DetailActivity.this, " You haven't selected any number of shirts",
                            Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                } else {
                    cartItemsLists.setId(item.getId());
                    cartItemsLists.setName(item.getName());
                    cartItemsLists.setColour(item.getColour());
                    cartItemsLists.setQuantity(Integer.parseInt(quantity));
                    cartItemsLists.setPrice(item.getPrice());
                    sharedPreferences = getSharedPreferences(SAVED_CART, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("id", item.getId());
                    editor.putString("name", item.getName());
                    editor.putString("colour", item.getColour());
                    editor.putInt("quantity", Integer.parseInt(quantity));
                    editor.putString("price", String.valueOf(item.getPrice()));
                    editor.commit();

                    mBundle.putParcelable(ITEM_ID_KEY, cartItemsLists);
                    Toast.makeText(DetailActivity.this, quantity + " " + item.getName()
                                    + " shirts added in your cart",
                            Toast.LENGTH_SHORT).show();
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
    }

    private void viewCart(){
        Toast.makeText(DetailActivity.this, "You chose to view your cart",
                Toast.LENGTH_SHORT).show();
        intent = new Intent(DetailActivity.this, ShoppingCart.class);
        intent.putExtras(mBundle);
        startActivity(intent);
    }
}