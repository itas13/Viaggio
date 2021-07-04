package com.fuadi.Viaggio.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.fuadi.Viaggio.R;
import com.fuadi.Viaggio.database.DatabaseHelper;
import com.fuadi.Viaggio.session.SessionManager;

import java.util.Calendar;
import java.util.HashMap;

public class BookTiketActivity extends AppCompatActivity {

    protected Cursor cursor;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Spinner spinAsal, spinTujuan, spinDewasa, spinAnak;
    SessionManager session;
    String email;
    int id_book;
    public String sAsal, sTujuan, sTanggal, sDewasa, sAnak;
    int jmlDewasa, jmlAnak;
    int hargaDewasa, hargaAnak;
    int hargaTotalDewasa, hargaTotalAnak, hargaTotal;
    private EditText etTanggal;
    private DatePickerDialog dpTanggal;
    Calendar newCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_tiket);

        dbHelper = new DatabaseHelper(BookTiketActivity.this);
        db = dbHelper.getReadableDatabase();

        final String[] asal = {"Soekarno-Hatta International Airport, Jakarta", "SSK II International Airport, Pekanbaru", "Ngurah Rai International Airport, Bali", "Yogyakarta International Airport, Yogyakarta", "Juanda International Airport, Surabaya"};
        final String[] tujuan = {"Soekarno-Hatta International Airport, Jakarta", "SSK II International Airport, Pekanbaru", "Ngurah Rai International Airport, Bali", "Yogyakarta International Airport, Yogyakarta", "Juanda International Airport, Surabaya"};
        final String[] dewasa = {"0", "1", "2", "3", "4", "5"};
        final String[] anak = {"0", "1", "2", "3", "4", "5"};

        spinAsal = findViewById(R.id.asal);
        spinTujuan = findViewById(R.id.tujuan);
        spinDewasa = findViewById(R.id.dewasa);
        spinAnak = findViewById(R.id.anak);

        ArrayAdapter<CharSequence> adapterAsal = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, asal);
        adapterAsal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAsal.setAdapter(adapterAsal);

        ArrayAdapter<CharSequence> adapterTujuan = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, tujuan);
        adapterTujuan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTujuan.setAdapter(adapterTujuan);

        ArrayAdapter<CharSequence> adapterDewasa = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, dewasa);
        adapterDewasa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDewasa.setAdapter(adapterDewasa);

        ArrayAdapter<CharSequence> adapterAnak = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, anak);
        adapterAnak.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAnak.setAdapter(adapterAnak);

        spinAsal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sAsal = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinTujuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sTujuan = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinDewasa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sDewasa = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinAnak.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sAnak = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btnBook = findViewById(R.id.book);

        etTanggal = findViewById(R.id.tanggal_berangkat);
        etTanggal.setInputType(InputType.TYPE_NULL);
        etTanggal.requestFocus();
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);
        setDateTimeField();

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perhitunganHarga();
                if (sAsal != null && sTujuan != null && sTanggal != null && sDewasa != null) {
                    if ((sAsal.equalsIgnoreCase("Soekarno-Hatta International Airport, Jakarta") && sTujuan.equalsIgnoreCase("Soekarno-Hatta International Airport, Jakarta"))
                            || (sAsal.equalsIgnoreCase("SSK II International Airport, Pekanbaru") && sTujuan.equalsIgnoreCase("SSK II International Airport, Pekanbaru"))
                            || (sAsal.equalsIgnoreCase("Ngurah Rai International Airport, Bali") && sTujuan.equalsIgnoreCase("Ngurah Rai International Airport, Bali"))
                            || (sAsal.equalsIgnoreCase("Yogyakarta International Airport, Yogyakarta") && sTujuan.equalsIgnoreCase("Yogyakarta International Airport, Yogyakarta"))
                            || (sAsal.equalsIgnoreCase("Juanda International Airport, Surabaya") && sTujuan.equalsIgnoreCase("Juanda International Airport, Surabaya"))) {
                        Toast.makeText(BookTiketActivity.this, "Asal dan Tujuan tidak boleh sama !", Toast.LENGTH_LONG).show();
                    } else {
                        AlertDialog dialog = new AlertDialog.Builder(BookTiketActivity.this)
                                .setTitle("Booking pesawat sekarang?")
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            db.execSQL("INSERT INTO TB_BOOK (asal, tujuan, tanggal, dewasa, anak) VALUES ('" +
                                                    sAsal + "','" +
                                                    sTujuan + "','" +
                                                    sTanggal + "','" +
                                                    sDewasa + "','" +
                                                    sAnak + "');");
                                            cursor = db.rawQuery("SELECT id_book FROM TB_BOOK ORDER BY id_book DESC", null);
                                            cursor.moveToLast();
                                            if (cursor.getCount() > 0) {
                                                cursor.moveToPosition(0);
                                                id_book = cursor.getInt(0);
                                            }
                                            db.execSQL("INSERT INTO TB_HARGA (username, id_book, harga_dewasa, harga_anak, harga_total) VALUES ('" +
                                                    email + "','" +
                                                    id_book + "','" +
                                                    hargaTotalDewasa + "','" +
                                                    hargaTotalAnak + "','" +
                                                    hargaTotal + "');");
                                            Toast.makeText(BookTiketActivity.this, "Booking berhasil", Toast.LENGTH_LONG).show();
                                            finish();
                                        } catch (Exception e) {
                                            Toast.makeText(BookTiketActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })
                                .setNegativeButton("Tidak", null)
                                .create();
                        dialog.show();
                    }
                } else {
                    Toast.makeText(BookTiketActivity.this, "Mohon lengkapi data pemesanan!", Toast.LENGTH_LONG).show();
                }
            }
        });

        setupToolbar();

    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.tbKrl);
        toolbar.setTitle("Form Booking");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void perhitunganHarga() {
        if (sAsal.equalsIgnoreCase("Soekarno-Hatta International Airport, Jakarta") && sTujuan.equalsIgnoreCase("SSK II International Airport, Pekanbaru")) {
            hargaDewasa = 1500000;
            hargaAnak = 1000000;
        } else if (sAsal.equalsIgnoreCase("Soekarno-Hatta International Airport, Jakarta") && sTujuan.equalsIgnoreCase("Juanda International Airport, Surabaya")) {
            hargaDewasa = 900000;
            hargaAnak = 700000;
        } else if (sAsal.equalsIgnoreCase("Soekarno-Hatta International Airport, Jakarta") && sTujuan.equalsIgnoreCase("Ngurah Rai International Airport, Bali")) {
            hargaDewasa = 2000000;
            hargaAnak = 1600000;
        } else if (sAsal.equalsIgnoreCase("Soekarno-Hatta International Airport, Jakarta") && sTujuan.equalsIgnoreCase("Yogyakarta International Airport, Yogyakarta")) {
            hargaDewasa = 800000;
            hargaAnak = 600000;
        } else if (sAsal.equalsIgnoreCase("SSK II International Airport, Pekanbaru") && sTujuan.equalsIgnoreCase("Soekarno-Hatta International Airport, Jakarta")) {
            hargaDewasa = 1500000;
            hargaAnak = 1000000;
        } else if (sAsal.equalsIgnoreCase("SSK II International Airport, Pekanbaru") && sTujuan.equalsIgnoreCase("Juanda International Airport, Surabaya")) {
            hargaDewasa = 2100000;
            hargaAnak = 1800000;
        } else if (sAsal.equalsIgnoreCase("SSK II International Airport, Pekanbaru") && sTujuan.equalsIgnoreCase("Ngurah Rai International Airport, Bali")) {
            hargaDewasa = 2800000;
            hargaAnak = 2500000;
        } else if (sAsal.equalsIgnoreCase("SSK II International Airport, Pekanbaru") && sTujuan.equalsIgnoreCase("Yogyakarta International Airport, Yogyakarta")) {
            hargaDewasa = 1200000;
            hargaAnak = 1000000;
        } else if (sAsal.equalsIgnoreCase("Juanda International Airport, Surabaya") && sTujuan.equalsIgnoreCase("Soekarno-Hatta International Airport, Jakarta")) {
            hargaDewasa = 900000;
            hargaAnak = 700000;
        } else if (sAsal.equalsIgnoreCase("Juanda International Airport, Surabaya") && sTujuan.equalsIgnoreCase("SSK II International Airport, Pekanbaru")) {
            hargaDewasa = 2100000;
            hargaAnak = 1800000;
        } else if (sAsal.equalsIgnoreCase("Juanda International Airport, Surabaya") && sTujuan.equalsIgnoreCase("Ngurah Rai International Airport, Bali")) {
            hargaDewasa = 900000;
            hargaAnak = 700000;
        } else if (sAsal.equalsIgnoreCase("Juanda International Airport, Surabaya") && sTujuan.equalsIgnoreCase("Yogyakarta International Airport, Yogyakarta")) {
            hargaDewasa = 600000;
            hargaAnak = 400000;
        } else if (sAsal.equalsIgnoreCase("Ngurah Rai International Airport, Bali") && sTujuan.equalsIgnoreCase("Soekarno-Hatta International Airport, Jakarta")) {
            hargaDewasa = 2000000;
            hargaAnak = 1600000;
        } else if (sAsal.equalsIgnoreCase("Ngurah Rai International Airport, Bali") && sTujuan.equalsIgnoreCase("SSK II International Airport, Pekanbaru")) {
            hargaDewasa = 2800000;
            hargaAnak = 2500000;
        } else if (sAsal.equalsIgnoreCase("Ngurah Rai International Airport, Bali") && sTujuan.equalsIgnoreCase("Yogyakarta International Airport, Yogyakarta")) {
            hargaDewasa = 900000;
            hargaAnak = 700000;
        } else if (sAsal.equalsIgnoreCase("Ngurah Rai International Airport, Bali") && sTujuan.equalsIgnoreCase("Juanda International Airport, Surabaya")) {
            hargaDewasa = 900000;
            hargaAnak = 700000;
        } else if (sAsal.equalsIgnoreCase("Yogyakarta International Airport, Yogyakarta") && sTujuan.equalsIgnoreCase("Soekarno-Hatta International Airport, Jakarta")) {
            hargaDewasa = 800000;
            hargaAnak = 600000;
        } else if (sAsal.equalsIgnoreCase("Yogyakarta International Airport, Yogyakarta") && sTujuan.equalsIgnoreCase("SSK II International Airport, Pekanbaru")) {
            hargaDewasa = 1200000;
            hargaAnak = 1000000;
        } else if (sAsal.equalsIgnoreCase("Yogyakarta International Airport, Yogyakarta") && sTujuan.equalsIgnoreCase("Ngurah Rai International Airport, Bali")) {
            hargaDewasa = 900000;
            hargaAnak = 700000;
        } else if (sAsal.equalsIgnoreCase("Yogyakarta International Airport, Yogyakarta") && sTujuan.equalsIgnoreCase("Juanda International Airport, Surabaya")) {
            hargaDewasa = 600000;
            hargaAnak = 400000;
        }

        jmlDewasa = Integer.parseInt(sDewasa);
        jmlAnak = Integer.parseInt(sAnak);

        hargaTotalDewasa = jmlDewasa * hargaDewasa;
        hargaTotalAnak = jmlAnak * hargaAnak;
        hargaTotal = hargaTotalDewasa + hargaTotalAnak;
    }

    private void setDateTimeField() {
        etTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpTanggal.show();
            }
        });

        dpTanggal = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String[] bulan = {"Januari", "Februari", "Maret", "April", "Mei",
                        "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
                sTanggal = dayOfMonth + " " + bulan[monthOfYear] + " " + year;
                etTanggal.setText(sTanggal);

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
}