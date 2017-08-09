package com.kumar.shirtstore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.kumar.shirtstore.model.CartItemsList;
import com.kumar.shirtstore.service.MyService;

import java.util.List;

/**
 * Created by Purushotham on 09/08/17.
 */

public class ShoppingCart extends AppCompatActivity {

    private TextView itemName, itemPrice, itemQuantity, totalItemQuantity, totalItemPrice;
    CartItemsList cartItemsLists;
    double price, totalPrice;
    int quantity, totalQuantity;
    List<CartItemsList> mcartItemsList;
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
}
