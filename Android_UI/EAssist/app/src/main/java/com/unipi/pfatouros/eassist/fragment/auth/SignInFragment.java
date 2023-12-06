package com.unipi.pfatouros.eassist.fragment.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.unipi.pfatouros.eassist.R;
import com.unipi.pfatouros.eassist.model.request.SignInRequest;
import com.unipi.pfatouros.eassist.utility.ApiUtil;
import com.unipi.pfatouros.eassist.utility.PreferenceUtil;
import com.unipi.pfatouros.eassist.utility.Retrofit;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInFragment extends Fragment implements View.OnClickListener {

    // Instance variables
    private AppCompatEditText usernameEditText;
    private AppCompatEditText passwordEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set listener to sign in button
        Button signInButton = view.findViewById(R.id.signInButton);
        signInButton.setOnClickListener(this);

        // Initialize editTexts based on their id
        usernameEditText = view.findViewById(R.id.signInUsernameAppCompatEditText);
        passwordEditText = view.findViewById(R.id.signInPasswordAppCompatEditText);
    }

    @Override
    public void onClick(View view) {

        // If sign in button is clicked
        if (view.getId() == R.id.signInButton) {

            // Authenticate user, get jwt, save jwt to shared preferences
            // and navigate to next fragment (SelectRoleFragment)

            // Get username and password from fragment
            String username = Objects.requireNonNull(usernameEditText.getText()).toString();
            String password = Objects.requireNonNull(passwordEditText.getText()).toString();

            // Create request object
            SignInRequest request = new SignInRequest(username, password);

            // Create retrofit object
            Retrofit api = ApiUtil.getRetrofit().create(Retrofit.class);

            // Create call object
            Call<String> call = api.signIn(request);

            // Execute call
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call,
                                       @NonNull Response<String> response) {

                    if (response.isSuccessful()) {

                        // Get json web token if response was successful
                        String jwt = response.body();

                        // Save jwt to shared preferences
                        boolean saved = PreferenceUtil.saveToken(jwt, getContext());
                        if (!saved) {
                            // Show toast with message if jwt could not be saved
                            String text = getResources().getString(R.string.jwt_could_not_be_saved);
                            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                        }

                        // Navigate to SelectRoleFragment
                        Navigation.findNavController(view)
                                .navigate(SignInFragmentDirections
                                        .actionSignInFragmentToSelectRoleFragment(username));

                    } else {
                        // Show toast otherwise
                        String text = getResources().getString(R.string.bad_credentials);
                        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call,
                                      @NonNull Throwable t) {

                    // Show toast with error message
                    String text = t.getLocalizedMessage();
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}