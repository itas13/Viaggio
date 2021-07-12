package com.fuadi.Viaggio.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fuadi.Viaggio.R;
import com.fuadi.Viaggio.adapter.AlertDialogManager;
import com.fuadi.Viaggio.session.SessionManager;

public class MainActivity extends AppCompatActivity {

    //Deklarasi dari variabel yang ada
    AlertDialogManager alert = new AlertDialogManager();
    SessionManager session;
    Button btnLogout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        btnLogout = findViewById(R.id.out);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Anda yakin ingin keluar ?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                session.logoutUser();
                            }
                        })
                        .setNegativeButton("Tidak", null)
                        .create();
                dialog.show();
            }
        });
    }

    //menampilkan fitur profile
    public void profileMenu(View v) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    //menampilkan fitur history
    public void historyMenu(View v) {
        Intent i = new Intent(this, HistoryActivity.class);
        startActivity(i);
    }

    //menampilkan fitur booktiket
    public void bookTiket(View v) {
        Intent i = new Intent(this, BookTiketActivity.class);
        startActivity(i);
    }
}
