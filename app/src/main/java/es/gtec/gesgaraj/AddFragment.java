package es.gtec.gesgaraj;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AddFragment extends Fragment {
    Button contactoButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        contactoButton = view.findViewById(R.id.button_contacto);
        contactoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:gesgarag@contacto.es"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Asunto del mensaje");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Cuerpo del mensaje");
                startActivity(Intent.createChooser(emailIntent, "Enviar correo..."));
            }
        });



        return view;
    }
}