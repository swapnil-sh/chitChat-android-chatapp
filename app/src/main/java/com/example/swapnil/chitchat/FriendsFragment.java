package com.example.swapnil.chitchat;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {


    private RecyclerView myFriendsList;
    private DatabaseReference FriendsReference;
    private DatabaseReference UsersReference;
    private FirebaseAuth mAuth;
    private FirebaseRecyclerAdapter adapter;
    String online_user_id;
    private View myMainView;


    public FriendsFragment() {
        // Required empty public constructor


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       myMainView =  inflater.inflate(R.layout.fragment_friends, container, false);

       myFriendsList = (RecyclerView) myMainView.findViewById(R.id.friends_list);

       mAuth = FirebaseAuth.getInstance();

       online_user_id = mAuth.getCurrentUser().getUid();

       FriendsReference = FirebaseDatabase.getInstance().getReference().child("Friends").child(online_user_id);
       FriendsReference.keepSynced(true);
       UsersReference = FirebaseDatabase.getInstance().getReference().child("Users");
       UsersReference.keepSynced(true);

       myFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));



        // Inflate the layout for this fragment
        return myMainView;
    }

    @Override
    public void onStart() {
        super.onStart();



        FirebaseRecyclerOptions<Friends> options =
                new FirebaseRecyclerOptions.Builder<Friends>()
                        .setQuery(FriendsReference,Friends.class)
                        .build();


        adapter = new FirebaseRecyclerAdapter<Friends,FriendsViewHolder>(options) {


            @Override
            public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_users_display_layout, parent, false);

                //Toast.makeText(AllUsersActivity.this,"AllUsers Activity is runninggggggggg!",Toast.LENGTH_LONG).show();

                return new FriendsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(final FriendsViewHolder viewHolder, final int position, Friends model) {
                // Bind the image_details object to the BlogViewHolder
                // ...


                viewHolder.setDate(model.getDate());

                String list_user_id = getRef(position).getKey();

                UsersReference.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String userName = dataSnapshot.child("user_name").getValue().toString();
                        String thumbImage = dataSnapshot.child("user_thumb_image").getValue().toString();

                        if(dataSnapshot.hasChild("online")){

                            Boolean online_status = (boolean) dataSnapshot.child("online").getValue();
                            viewHolder.setUserOnline(online_status);
                        }

                        viewHolder.setUserName(userName);
                        viewHolder.setThumbImage(thumbImage, getContext());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        };
        adapter.startListening();

        myFriendsList.setAdapter(adapter);


    }



    public static class FriendsViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public FriendsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setDate(String date){

            TextView sinceFriendsDate = (TextView) mView.findViewById(R.id.all_users_status);

            sinceFriendsDate.setText(date);
        }

        public void setUserName(String userName){

            TextView userNameDisplay = (TextView) mView.findViewById(R.id.all_users_username);
            userNameDisplay.setText(userName);
        }

        public void setThumbImage(final String thumbImage, final Context ctx){


            final CircleImageView image = (CircleImageView) mView.findViewById(R.id.all_users_profile_image);


            Picasso.get().load(thumbImage).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.defualt_image_profile).into(image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {

                    Picasso.get().load(thumbImage).placeholder(R.drawable.defualt_image_profile).into(image);

                }
            });



        }

        public void setUserOnline(Boolean online_status) {


            ImageView onlineStatusView = (ImageView) mView.findViewById(R.id.online_status);

            if(online_status == true){

                onlineStatusView.setVisibility(View.VISIBLE);
            }
            else{

                onlineStatusView.setVisibility(View.INVISIBLE);
            }
        }
    }
}
