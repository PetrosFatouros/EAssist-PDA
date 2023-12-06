package com.unipi.pfatouros.eassist.fragment.admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.pfatouros.eassist.R;
import com.unipi.pfatouros.eassist.adapter.UserAdapter;
import com.unipi.pfatouros.eassist.model.Role;
import com.unipi.pfatouros.eassist.model.User;
import com.unipi.pfatouros.eassist.utility.ApiUtil;
import com.unipi.pfatouros.eassist.utility.PreferenceUtil;
import com.unipi.pfatouros.eassist.utility.Retrofit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewUsersFragment extends Fragment
        implements SearchView.OnQueryTextListener,
        UserAdapter.OnItemClickListener,
        DialogInterface.OnClickListener {

    // Instance variables
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private UserAdapter adapter;
    private List<User> unfilteredUsers;
    private int position;
    private Call<Void> call;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize recycler view and layout manager
        recyclerView = view.findViewById(R.id.viewUsersRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());

        // Set listener to search view
        SearchView searchView = view.findViewById(R.id.viewUsersSearchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(this);

        // Get all existing users and display them on screen

        // Get token
        String token = PreferenceUtil.getToken(getContext());

        // Create retrofit object
        Retrofit api = ApiUtil.getRetrofit(token).create(Retrofit.class);

        // Create call object
        Call<List<User>> call = api.getUsers();

        // Store current context in a variable in order to show toast
        final Context context = getContext();

        // Execute call
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call,
                                   @NonNull Response<List<User>> response) {

                if (response.isSuccessful()) {

                    // Get list of existing users if response was successful
                    unfilteredUsers = response.body();

                    // Setup the recycler view
                    setRecyclerView();

                } else {
                    // Show toast otherwise
                    String text = getResources()
                            .getString(R.string.unsuccessful_response_exception_message)
                            + response.code();
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call,
                                  @NonNull Throwable t) {

                // Show toast with error message
                String text = t.getLocalizedMessage();
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRecyclerView() {

        // Create adapter
        adapter = new UserAdapter(unfilteredUsers, getContext());

        // Set listener to adapter
        adapter.setOnItemClickListener(this);

        // Setup layout manager and adapter with recycler view
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public boolean onQueryTextChange(String s) {

        // Create a filtered list of users (based on username, email, roles)

        List<User> filteredUsers = new ArrayList<>();
        for (User user : unfilteredUsers) {

            // Get username and email
            String username = user.getUsername().toLowerCase();
            String email = user.getEmail().toLowerCase();

            // Build string with user's roles
            StringBuilder stringBuilder = new StringBuilder();
            for (Role role : user.getRoles()) {
                stringBuilder.append(role.getName().toLowerCase());
            }

            // Get roles
            String roles = stringBuilder.toString();

            // Do the filtering
            if (username.contains(s.toLowerCase()) ||
                    email.contains(s.toLowerCase()) ||
                    roles.contains(s.toLowerCase())) {

                filteredUsers.add(user);
            }
        }

        // Update adapter
        if (!Objects.equals(filteredUsers.size(), 0)) {
            adapter.setUsers(filteredUsers);
            adapter.notifyDataSetChanged();
        }

        return true;
    }

    @Override
    public void onButtonClick(int position) {

        // Delete user on given position

        // Get token
        String token = PreferenceUtil.getToken(getContext());

        // Create retrofit object
        Retrofit api = ApiUtil.getRetrofit(token).create(Retrofit.class);

        // Get username
        String username = adapter.getUsers().get(position).getUsername();

        // Update instance variable
        this.position = position;

        // Create call object
        this.call = api.deleteUser(username);

        // Ask admin user if he is sure he wants to register a new user
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder
                .setMessage(getResources().getString(R.string.are_you_sure))
                .setPositiveButton(getResources().getString(R.string.yes), this)
                .setNegativeButton(getResources().getString(R.string.no), this).show();
    }

    // Followed stackoverflow article:
    // https://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        switch (i) {
            case DialogInterface.BUTTON_POSITIVE:

                //Yes button clicked
                // Execute call
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call,
                                           @NonNull Response<Void> response) {

                        String text;
                        if (response.isSuccessful()) {
                            // User deleted
                            text = getResources()
                                    .getString(R.string.user_deleted);

                            // Update adapter
                            adapter.getUsers().remove(position);
                            adapter.notifyItemRemoved(position);
                        } else {
                            // Unsuccessful response
                            text = getResources()
                                    .getString(R.string.user_could_not_be_deleted);
                        }

                        // Show toast with corresponding message
                        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call,
                                          @NonNull Throwable t) {

                        // Show toast with error message
                        String text = t.getLocalizedMessage();
                        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
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