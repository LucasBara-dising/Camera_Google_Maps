package com.example.camera;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // Define the pic id
    private static final int pic_id = 123;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    // Define the button and imageview type variable
    Button camera_open_id;
    Button maps_open;
    ImageView click_image_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        camera_open_id = findViewById(R.id.camera_button);
        maps_open = findViewById(R.id.maps_open);
        click_image_id = findViewById(R.id.click_image);

        camera_open_id.setOnClickListener(v -> {
            // chama a internsão da camera
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // inicia a camera
            startActivityForResult(camera_intent, pic_id);
        });

       maps_open.setOnClickListener(v -> {
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
               if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                   ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
               } else {
                   // Permissão já concedida, pode obter a localização
                   getLocation();
               }
           }

       });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Acha a foto
        if (requestCode == pic_id) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // coloca no imageView
            click_image_id.setImageBitmap(photo);
        }
    }

    private void openMaps(double latitude, double longitude ) {

        String uri = String.format("geo:%f,%f", latitude, longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    private void getLocation() {
            // Obter o LocationManager
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            // Obter a última localização conhecida
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownLocation != null) {
                    // Processar a localização
                    double latitude = lastKnownLocation.getLatitude();
                    double longitude = lastKnownLocation.getLongitude();

                    // Exibir a localização no console
                    openMaps(latitude, longitude);
                    Toast.makeText(getApplicationContext(), "Latitude: "+latitude+" Longitude: "+ longitude, Toast.LENGTH_LONG).show();
                }
            }

    }
}