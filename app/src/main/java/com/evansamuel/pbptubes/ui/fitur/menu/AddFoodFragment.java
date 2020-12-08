package com.evansamuel.pbptubes.ui.fitur.menu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.evansamuel.pbptubes.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFoodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFoodFragment extends Fragment {
    private ProgressDialog progressDialog;
    private EditText etName, etPrice, etImage;
    private Button addBtn,editBtn,cancelBtn;
    private String status;
    private MenuDao menu;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddFoodFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFoodFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFoodFragment newInstance(String param1, String param2) {
        AddFoodFragment fragment = new AddFoodFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_food, container, false);
        menu   = (MenuDao) getArguments().getSerializable("menu");

        progressDialog = new ProgressDialog(getContext());



        etName = v.findViewById(R.id.edtMenuName);
        etPrice = v.findViewById(R.id.edtMenuPrice);
        etImage = v.findViewById(R.id.edtPhoto);

        addBtn = v.findViewById(R.id.btnAdd);
        editBtn = v.findViewById(R.id.btn_edit);
        cancelBtn = v.findViewById(R.id.btn_cancel);

        status = getArguments().getString("status");
        if(status.equals("edit"))
        {
            etName.setText(menu.getNama());
            etPrice.setText(String.valueOf(Math.round(menu.getPrice())));
            etImage.setText(menu.getPhoto());
            addBtn.setVisibility(View.GONE);
        }
        else if(status.equals("tambah"))
        {
            editBtn.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.GONE);
        }
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etName.getText().toString().isEmpty())
                {
                    etName.setError("Field name must be filled");
                    etName.requestFocus();
                }
                else if(etPrice.getText().toString().isEmpty())
                {
                    etPrice.setError("Field price must be filled");
                    etPrice.requestFocus();
                }
                else if(etImage.getText().toString().isEmpty())
                {
                    etImage.setError("Image URL must be filled");
                    etImage.requestFocus();
                }
                else
                {
                    saveMenu();

                }
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etName.getText().toString().isEmpty())
                {
                    etName.setError("Field name must be filled");
                    etName.requestFocus();
                }
                else if(etPrice.getText().toString().isEmpty())
                {
                    etPrice.setError("Field price must be filled");
                    etPrice.requestFocus();
                }
                else if(etImage.getText().toString().isEmpty())
                {
                    etImage.setError("Image URL must be filled");
                    etImage.requestFocus();
                }
                else
                {
                    updateMenu(menu.getId());
                }
            }
        });

        return v;
    }

    private void saveMenu() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MenuResponse> add = apiService.createMenu(etName.getText().toString(),
                Double.parseDouble(etPrice.getText().toString()),etImage.getText().toString());


        add.enqueue(new Callback<MenuResponse>() {
            @Override
            public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {
                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MenuResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }

    private void updateMenu(String sId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MenuResponse> update = apiService.updateMenu(sId,etName.getText().toString(),Double.valueOf(etPrice.getText().toString()),etImage.getText().toString());


        update.enqueue(new Callback<MenuResponse>() {
            @Override
            public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {
                Toast.makeText(getContext(), "Update Berhasil",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();


            }

            @Override
            public void onFailure(Call<MenuResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }
}