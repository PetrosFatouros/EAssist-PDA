package com.unipi.pfatouros.eassist.fragment.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.unipi.pfatouros.eassist.R;
import com.unipi.pfatouros.eassist.model.request.SignUpRequest;
import com.unipi.pfatouros.eassist.utility.ApiUtil;
import com.unipi.pfatouros.eassist.utility.PreferenceUtil;
import com.unipi.pfatouros.eassist.utility.Retrofit;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUserFragment extends Fragment
        implements View.OnClickListener, DialogInterface.OnClickListener {

    // Instance variables
    private AppCompatEditText usernameEditText;
    private AppCompatEditText emailEditText;
    private AppCompatEditText passwordEditText;
    private SwitchMaterial employeeSwitch;
    private SwitchMaterial managerSwitch;
    private SwitchMaterial adminSwitch;
    private Call<Void> call;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize instance variables based on their id
        usernameEditText = view.findViewById(R.id.signUpUsernameAppCompatEditText);
        emailEditText = view.findViewById(R.id.signUpEmailAppCompatEditText);
        passwordEditText = view.findViewById(R.id.signUpPasswordAppCompatEditText);
        employeeSwitch = view.findViewById(R.id.employeeRoleSwitchMaterial);
        managerSwitch = view.findViewById(R.id.managerRoleSwitchMaterial);
        adminSwitch = view.findViewById(R.id.adminRoleSwitchMaterial);

        // Set listener to sign up button
        Button addUserButton = view.findViewById(R.id.signUpButton);
        addUserButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        // If sign up button is clicked
        if (view.getId() == R.id.signUpButton) {

            // Gather user's info
            String username = (Objects.requireNonNull(usernameEditText.getText()).toString());
            String email = (Objects.requireNonNull(emailEditText.getText()).toString());
            String password = (Objects.requireNonNull(passwordEditText.getText()).toString());
            Set<String> roles = new HashSet<>();
            if (employeeSwitch.isChecked()) {
                roles.add(getResources().getString(R.string.employee_lower));
            }
            if (managerSwitch.isChecked()) {
                roles.add(getResources().getString(R.string.manager_lower));
            }
            if (adminSwitch.isChecked()) {
                roles.add(getResources().getString(R.string.admin_lower));
            }

            // Create request
            SignUpRequest request = new SignUpRequest(username, email, password, roles);

            // Get token
            String token = PreferenceUtil.getToken(getContext());

            // Create retrofit object
            Retrofit api = ApiUtil.getRetrofit(token).create(Retrofit.class);

            // Initialize call object
            call = api.signUpUser(request);

            // Ask admin user if he is sure he wants to register a new user
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder
                    .setMessage(getResources().getString(R.string.are_you_sure))
                    .setPositiveButton(getResources().getString(R.string.yes), this)
                    .setNegativeButton(getResources().getString(R.string.no), this).show();
        }
    }

    // Followed stackoverflow article:
    // https://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        switch (i) {
            case DialogInterface.BUTTON_POSITIVE:

                //Yes button clicked

                // Store current context in a variable in order to show toast
                final Context context = getContext();

                // Execute call
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call,
                                           @NonNull Response<Void> response) {

                        String text;
                        if (response.isSuccessful()) {
                            // Registration completed
                            text = getResources()
                                    .getString(R.string.registration_completed);
                        } else {
                            // Unsuccessful response
                            text = getResources()
                                    .getString(R.string.registration_could_not_be_completed);
                        }

                        // Show toast with corresponding message
                        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call,
                                          @NonNull Throwable t) {

                        // Show toast with error message
                        String text = t.getLocalizedMessage();
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                    }
                });

                break;

            case DialogInterface.BUTTON_NEGATIVE:

                //No button clicked
                // Do nothing

                break;
        }
    }
}