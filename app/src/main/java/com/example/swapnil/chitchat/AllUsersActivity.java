package com.example.swapnil.chitchat;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUsersActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView allUsersList;
    private DatabaseReference allDatabaseUserreference;
    private FirebaseRecyclerAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        mToolbar = (Toolbar)findViewById(R.id.all_users_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        allUsersList = (RecyclerView)findViewById(R.id.all_users_list);
        allUsersList.setHasFixedSize(true);
        allUsersList.setLayoutManager(new LinearLayoutManager(this));
        allDatabaseUserreference = FirebaseDatabase.getInstance().getReference().child("Users");
        allDatabaseUserreference.keepSynced(true);

    }


    @Override
    protected void onStart() {
        super.onStart();




        FirebaseRecyclerOptions<AllUsers> options =
                new FirebaseRecyclerOptions.Builder<AllUsers>()
                        .setQuery(allDatabaseUserreference, AllUsers.class)
                        .build();

            adapter = new FirebaseRecyclerAdapter<AllUsers, AllUsersViewHolder>(options) {


            @Override
            public AllUsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_users_display_layout, parent, false);

                //Toast.makeText(AllUsersActivity.this,"AllUsers Activity is runninggggggggg!",Toast.LENGTH_LONG).show();

                return new AllUsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(AllUsersViewHolder viewHolder, final int position, AllUsers model) {
                // Bind the image_details object to the BlogViewHolder
                // ...
                viewHolder.setUser_name(model.getUser_name());
                viewHolder.setUser_status(model.getUser_status());
                viewHolder.setUser_thumb_image(getApplicationContext(),model.getUser_thumb_image());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String visit_user_id = getRef(position).getKey();

                        Intent profileIntnet = new Intent(AllUsersActivity.this,ProfileActivity.class);
                        profileIntnet.putExtra("visit_user_id",visit_user_id);
                        startActivity(profileIntnet);
                    }
                });


            }
        };
        adapter.startListening();

        allUsersList.setAdapter(adapter);


    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }



    public static class AllUsersViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public AllUsersViewHolder(View itemView) {

            super(itemView);
            mView = itemView;
        }

        public void setUser_name(String user_name){

            TextView name = (TextView)mView.findViewById(R.id.all_users_username);
            name.setText(user_name);

        }

        public void setUser_status(String user_status){

            TextView status = (TextView)mView.findViewById(R.id.all_users_status);
            status.setText(user_status);
        }

        public void setUser_thumb_image(final  Context ctx,final String user_thumb_image){

          final CircleImageView image = (CircleImageView) mView.findViewById(R.id.all_users_profile_image);


            Picasso.get().load(user_thumb_image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.defualt_image_profile).into(image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {

                    Picasso.get().load(user_thumb_image).placeholder(R.drawable.defualt_image_profile).into(image);

                }
            });


        }
    }
}
