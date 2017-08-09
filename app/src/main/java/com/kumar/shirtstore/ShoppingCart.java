package com.kumar.shirtstore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.kumar.shirtstore.model.CartItemsList;
import com.kumar.shirtstore.service.MyService;


/**
 * Created by Purushotham on 09/08/17.
 */

public class ShoppingCart extends AppCompatActivity {

    private TextView itemName, itemPrice, itemQuantity, totalItemQuantity, totalItemPrice;
    CartItemsList cartItemsLists;
    double price, totalPrice;
    int quantity, totalQuantity;
    private ImageButton removeItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_detail);

        cartItemsLists = (CartItemsList)getIntent().getParcelableExtra(MyService.ITEM_ID_KEY);
        itemName = (TextView) findViewById(R.id.itemName);
        itemPrice = (TextView) findViewById(R.id.itemPrice);
        itemQuantity = (TextView) findViewById(R.id.cartQuantity);
        totalItemQuantity = (TextView) findViewById(R.id.totalItemQuantity);
        totalItemPrice = (TextView) findViewById(R.id.totalItemPrice);
        removeItem = (ImageButton) findViewById(R.id.cart_minus_img);
        removeItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(ShoppingCart.this, "you have removed the item", Toast.LENGTH_SHORT).show();
                removeCartItem();
            }

        });
        displayCart();
    }
    private void displayCart(){
        if (cartItemsLists !=null){
            itemName.setText(cartItemsLists.getName());

            price = cartItemsLists.getPrice();
            itemPrice.setText(price + "");

            quantity = cartItemsLists.getQuantity();
            Log.i("quantity is ", "displayCart: " + quantity);
            itemQuantity.setText(quantity + "");

            totalQuantity = cartItemsLists.getQuantity();
            totalItemQuantity.setText(totalQuantity +"");

            totalPrice = cartItemsLists.getPrice() * quantity;
            totalItemPrice.setText(totalPrice + "");
        }

    }

    private void removeCartItem(){
        itemName.setText("");
        itemPrice.setText(0+"");
        quantity = 0;
        itemQuantity.setText(quantity + "");
        totalQuantity = 0;
        totalItemQuantity.setText(totalQuantity +"");
        totalPrice = cartItemsLists.getPrice() * quantity;
        totalItemPrice.setText(totalPrice + "");
    }
}
