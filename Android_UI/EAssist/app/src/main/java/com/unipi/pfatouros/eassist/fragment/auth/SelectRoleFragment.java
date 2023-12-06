package com.unipi.pfatouros.eassist.fragment.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.unipi.pfatouros.eassist.R;
import com.unipi.pfatouros.eassist.activity.AdminActivity;
import com.unipi.pfatouros.eassist.activity.EmployeeActivity;
import com.unipi.pfatouros.eassist.activity.ManagerActivity;
import com.unipi.pfatouros.eassist.utility.ApiUtil;
import com.unipi.pfatouros.eassist.utility.PreferenceUtil;
import com.unipi.pfatouros.eassist.utility.Retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectRoleFragment extends Fragment implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_role, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set listeners to role buttons
        Button adminButton = view.findViewById(R.id.adminButton);
        Button managerButton = view.findViewById(R.id.managerButton);
        Button employeeButton = view.findViewById(R.id.employeeButton);
        adminButton.setOnClickListener(this);
        managerButton.setOnClickListener(this);
        employeeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        // Get user's username
        String username = SelectRoleFragmentArgs.fromBundle(getArguments()).getUsername();

        // Get token
        String token = PreferenceUtil.getToken(getContext());

        // Create retrofit object
        Retrofit api = ApiUtil.getRetrofit(token).create(Retrofit.class);

        // If admin button was clicked
        if(view.getId() == R.id.adminButton){

            // Create call object
            Call<Boolean> call = api.hasRoleAdmin(username);

            // Execute call
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(@NonNull Call<Boolean> call,
                                       @NonNull Response<Boolean> response) {

                    if(response.isSuccessful()){

                        Boolean hasRole = response.body();

                        if(Boolean.TRUE.equals(hasRole)){

                            // Navigate to admin activity
                            Intent intent = new Intent(getActivity(), AdminActivity.class);
                            startActivity(intent);
                        }
                        else {

                            // Show toast with corresponding message
                            String text = getResources()
                                    .getString(R.string.you_are_not_an_admin);
                            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        // Show toast otherwise
                        String text = getResources()
                                .getString(R.string.unsuccessful_response_exception_message)
                                + response.code();
                        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Boolean> call,
                                      @NonNull Throwable t) {

                    // Show toast with error message
                    String text = t.getLocalizedMessage();
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                }
            });
        }
        // If manager button was clicked
        else if (view.getId() == R.id.managerButton){

            // Create call object
            Call<Boolean> call = api.hasRoleManager(username);

            // Execute call
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(@NonNull Call<Boolean> call,
                                       @NonNull Response<Boolean> response) {

                    if(response.isSuccessful()){

                        Boolean hasRole = response.body();

                        if(Boolean.TRUE.equals(hasRole)){

                            // Navigate to manager activity
                            Intent intent = new Intent(getActivity(), ManagerActivity.class);
                            startActivity(intent);
                        }
                        else {

                            // Show toast with corresponding message
                            String text = getResources()
                                    .getString(R.string.you_are_not_a_manager);
                            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        // Show toast otherwise
                        String text = getResources()
                                .getString(R.string.unsuccessful_response_exception_message)
                                + response.code();
                        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Boolean> call,
                                      @NonNull Throwable t) {

                    // Show toast with error message
                    String text = t.getLocalizedMessage();
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                }
            });
        }
        // If employee button was clicked
        else if(view.getId() == R.id.employeeButton){

            // Create call object
            Call<Boolean> call = api.hasRoleEmployee(username);

            // Execute call
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(@NonNull Call<Boolean> call,
                                       @NonNull Response<Boolean> response) {

                    if(response.isSuccessful()){
                        Boolean hasRole = response.body();

                        if(Boolean.TRUE.equals(hasRole)){

                            // Navigate to employee activity
                            Intent intent = new Intent(getActivity(), EmployeeActivity.class);
                            startActivity(intent);
                        }
                        else {

                            // Show toast with corresponding message
                            String text = getResources()
                                    .getString(R.string.you_are_not_an_employee);
                            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        // Show toast otherwise
                        String text = getResources()
                                .getString(R.string.unsuccessful_response_exception_message)
                                + response.code();
                        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Boolean> call,
                                      @NonNull Throwable t) {

                    // Show toast with error message
                    String text = t.getLocalizedMessage();
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}