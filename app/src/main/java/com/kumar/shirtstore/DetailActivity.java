package com.kumar.shirtstore;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kumar.shirtstore.model.CartItems;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Purushotham on 08/08/17.
 */

public class DetailActivity extends AppCompatActivity {

    private TextView tvName, tvDescription, tvPrice;
    private ImageView itemImage;
    private Button addCart, viewCart;
    CartItems item;

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

        ImageLoader imageDownloadTask = new ImageLoader();
        imageDownloadTask.execute(item);

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

    private void addCart(){
        Toast.makeText(DetailActivity.this, "Product added in your cart" ,
                Toast.LENGTH_SHORT).show();
    }

    private void viewCart(){
        Toast.makeText(DetailActivity.this, "You chose to view your cart",
                Toast.LENGTH_SHORT).show();
    }

    private class ImageLoader extends AsyncTask<CartItems, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(CartItems... dataItems) {
            InputStream in = null;

            try {
                String imageUrl = item.getPicture();
                in = (InputStream) new URL(imageUrl).getContent();
                return BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            itemImage.setImageBitmap(bitmap);
        }
    }
}