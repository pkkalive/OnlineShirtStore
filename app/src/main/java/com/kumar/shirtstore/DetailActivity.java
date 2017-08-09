package com.kumar.shirtstore;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Purushotham on 08/08/17.
 */

public class DetailActivity extends AppCompatActivity {

    public static final String TAG = "DetailActivity";
    private TextView tvName, tvDescription, tvPrice;
    private ImageView itemImage;
    private Button addCart, viewCart;
    CartItems item;
    ArrayList<String> cartItemList = new ArrayList<>();

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
        builder.setView(text);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String quantilty = text.getEditableText().toString();
                cartItemList.add(item.getId()+ "");
                cartItemList.add(item.getName());
                cartItemList.add(item.getColour());
                cartItemList.add(item.getPrice()+ "");
                cartItemList.add(quantilty);
                Toast.makeText(DetailActivity.this, quantilty + " " + item.getName()
                                + " shirts added in your cart" ,
                        Toast.LENGTH_SHORT).show();
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
        if (cartItemList.isEmpty()){
            Toast.makeText(DetailActivity.this, "There are no products to view your cart",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(DetailActivity.this, "You chose to view your cart",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ShoppingCart.class);
            intent.putStringArrayListExtra("test", cartItemList);
            startActivity(intent);


//            intent.putExtra("cartItemList", cartItemList);
        }
    }
}