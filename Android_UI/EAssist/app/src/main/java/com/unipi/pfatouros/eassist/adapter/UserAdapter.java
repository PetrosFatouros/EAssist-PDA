package com.unipi.pfatouros.eassist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.unipi.pfatouros.eassist.R;
import com.unipi.pfatouros.eassist.model.Role;
import com.unipi.pfatouros.eassist.model.User;

import java.text.MessageFormat;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    // Instance  variables
    private List<User> users;
    private final Context context;
    private OnItemClickListener listener;

    // Getter
    public List<User> getUsers() {
        return users;
    }

    // Setter
    public void setUsers(List<User> users) {
        this.users = users;
    }

    // Constructor
    public UserAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    // Interface for implementing click on delete button of the cardView
    public interface OnItemClickListener{
        void onButtonClick(int position);
    }

    // Set listener
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    // ViewHolder class, corresponds to card_view_users
    public static class UserViewHolder extends RecyclerView.ViewHolder{

        // Instance  variables
        public TextView nameTextView;
        public TextView emailTextView;
        public TextView rolesTextView;
        public AppCompatButton deleteButton;

        // Constructor
        public UserViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            // Initialize instance variables
            nameTextView = itemView.findViewById(R.id.usernameCardViewTexView);
            emailTextView = itemView.findViewById(R.id.emailCardViewTexView);
            rolesTextView = itemView.findViewById(R.id.rolesCardViewTexView);
            deleteButton = itemView.findViewById(R.id.deleteUserCardViewAppCompatButton);

            // Set listener for delete button
            deleteButton.setOnClickListener(view -> {
                if (listener != null){
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onButtonClick(position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Create a new recycler view
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_view_users, parent, false);
        return new UserViewHolder(view, this.listener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        // Get user based on position
        User user = users.get(position);

        // Add user's info to card view

        // Set username
        holder.nameTextView.setText(MessageFormat.format("{0} {1}",
                context.getResources().getString(R.string.username_), user.getUsername()));

        // Set email
        holder.emailTextView.setText(MessageFormat.format("{0} {1}",
                context.getResources().getString(R.string.email_), user.getEmail()));

        // Build a string that contains all user's roles
        StringBuilder roles = new StringBuilder();
        for(Role role: user.getRoles()){
            roles
                    .append(role.getName().replace("ROLE_", "").toLowerCase())
                    .append(", ");
        }
        roles.delete(roles.length() - 2, roles.length());

        // Set roles
        holder.rolesTextView.setText(MessageFormat.format("{0} {1}",
                context.getResources().getString(R.string.roles_), roles));
    }

    @Override
    public int getItemCount() {

        // Return users' list size
        return users.size();
    }
}
