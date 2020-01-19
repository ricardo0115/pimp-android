package fr.ubordeaux.pimp.activity;

import androidx.appcompat.app.AppCompatActivity;
import fr.ubordeaux.pimp.R;
import fr.ubordeaux.pimp.io.BitmapIO;
import fr.ubordeaux.pimp.util.MainSingleton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

public class MainActivity extends AppCompatActivity {
    private Bitmap currentImage; //Temporary champ waiting for others decision about Picture class
    private PhotoView iv;

    public PhotoView getIv() {
        return iv;
    }

    public void setIv(PhotoView iv) {
        this.iv = iv;
    }

    public static final int REQUEST_GET_SINGLE_FILE = 64;
    public static final int REQUEST_TAKE_PHOTO = 1;


    /**
     * Inflate upper menu
     * @param menu to inflate
     * @return true if menu inflated with success
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }



    /**
     * Load image from internal storage
     * @param reqCode request code to identify user's choice
     * @param resultCode result to load image
     * @param data Event given by user to display something
     */
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == REQUEST_GET_SINGLE_FILE) {
            if (resultCode == RESULT_OK) {
                try {
                    BitmapIO.LoadImageTask(data.getData(), this);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        }

        if (reqCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri imageUri = BitmapIO.getUriFromCameraFile();
                    BitmapIO.LoadImageTask(imageUri, this);
                }catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(this, "You haven't took picture", Toast.LENGTH_LONG).show();

            }
        }
    }

    /**
     *
     * @param item Item chosen by user.
     * @return true user click on an item.
     */


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            //Load photo from gallery
            case R.id.loadFromGallery:

                BitmapIO.startGalleryActivity();

                return true;
            case R.id.imageInfo:
                BitmapIO.dispatchTakePictureIntent();
                //Display width and height from bitmap
                return true;


            default:
                super.onOptionsItemSelected(item);
        }
        return false;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initialize Singleton
        MainSingleton.setContext(this);

        //Loading default image from resources
        Bitmap bmp = BitmapIO. decodeAndScaleBitmapFromResource(R.drawable.starwars);

        iv = findViewById(R.id.imageView);


        //Allow more zooming
        iv.setMaximumScale(10);
        iv.setImageBitmap(bmp);
    }




}
