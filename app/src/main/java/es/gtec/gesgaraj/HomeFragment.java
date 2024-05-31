package es.gtec.gesgaraj;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class HomeFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 100;
    private static final String PREFS_NAME = "prefs";
    private static final String PREFS_KEY_IMAGE_PATH = "image_path";
    private ImageView imgUsuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        imgUsuario = rootView.findViewById(R.id.img_usuario);
        imgUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });

        loadImageFromPreferences();

        // CardView click listeners
        CardView cardView = rootView.findViewById(R.id.grafico1);
        CardView cardView2 = rootView.findViewById(R.id.grafico2);
        CardView cardView3 = rootView.findViewById(R.id.grafico3);
        CardView cardView4 = rootView.findViewById(R.id.grafico4);
        CardView cardView5 = rootView.findViewById(R.id.grafico5);
        CardView cardView6 = rootView.findViewById(R.id.grafico6);
        CardView cardView7 = rootView.findViewById(R.id.grafico7);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PieChart.class);
                startActivity(intent);
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ColumnChart.class);
                startActivity(intent);
            }
        });

        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FunnelChart.class);
                startActivity(intent);
            }
        });

        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), VennDiagram.class);
                startActivity(intent);
            }
        });

        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ResourceChart.class);
                startActivity(intent);
            }
        });

        cardView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LineChart.class);
                startActivity(intent);
            }
        });

        cardView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RadarChart.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                bitmap = getResizedBitmap(bitmap, imgUsuario.getWidth(), imgUsuario.getHeight());
                imgUsuario.setImageBitmap(bitmap);
                saveImageToInternalStorage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveImageToInternalStorage(Bitmap bitmap) {
        File directory = requireActivity().getDir("imageDir", Activity.MODE_PRIVATE);
        File file = new File(directory, "profile.png");

        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            saveImagePathToPreferences(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveImagePathToPreferences(String path) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREFS_KEY_IMAGE_PATH, path);
        editor.apply();
    }

    private void loadImageFromPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        String imagePath = sharedPreferences.getString(PREFS_KEY_IMAGE_PATH, null);
        if (imagePath != null) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imgUsuario.setImageBitmap(bitmap);
            }
        }
    }

    private Bitmap getResizedBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }
}