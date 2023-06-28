package com.udinus.aplikasimobile.activity.product;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udinus.aplikasimobile.R;
import com.udinus.aplikasimobile.databinding.ActivityProductEditBinding;
import com.udinus.aplikasimobile.repository.model.Barang;

import java.util.Objects;

public class ProductEdit extends AppCompatActivity {
    ActivityProductEditBinding binding;
    private DatabaseReference databaseRef;
    private Barang barang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Mengubah judul yang ada pada App Bar
        Objects.requireNonNull(getSupportActionBar()).setTitle("Mengubah Data Barang");

        // Mengambil data dari intent dengan ParcelableExtra
        barang = getIntent().getParcelableExtra("key_barang");

        databaseRef = FirebaseDatabase.getInstance().getReference("barang");

        binding.tvKode.setText(barang.getCode());
        binding.edtNama.setText(barang.getName());
        binding.edtSatuan.setText(barang.getSatuan());
        binding.edtHarga.setText(String.valueOf(barang.getPrice()));

        binding.btnSave.setOnClickListener(view -> editBarang());
        binding.btnCancel.setOnClickListener(view -> finish());
        binding.btnHapus.setOnClickListener(view -> showDeleteConfirmationDialog());
    }

    private void editBarang() {
        String nama = binding.edtNama.getText().toString().trim();
        String satuan = binding.edtSatuan.getText().toString().trim();
        double harga = Double.parseDouble(binding.edtHarga.getText().toString());

        barang.setName(nama);
        barang.setSatuan(satuan);
        barang.setPrice(harga);

        databaseRef.child(barang.getKey()).setValue(barang);
        finish();
    }

    private void deleteBarang() {
        databaseRef.child(barang.getKey()).removeValue();
        finish();
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Hapus")
                .setMessage("Apakah Anda ingin menghapus " + barang.getName() + "?")
                .setIcon(R.drawable.round_delete_24)
                .setPositiveButton("Hapus", (dialog, whichButton) -> {
                    deleteBarang();
                    dialog.dismiss();
                })
                .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
}
