package com.dronaid.dronaid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.FloatProperty;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String token;
    String usernameText;

    private TextInputEditText username;
    private TextInputEditText password;
    private TextInputEditText accountType;

    private Animation fabOpenAnimation;
    private Animation fabCloseAnimation;
    private boolean isFabMenuOpen = false;

    private FloatingActionButton baseFAB;
    private LinearLayout editLayout;
    private LinearLayout deleteLayout;

    private FloatingActionButton editFAB;
    private FloatingActionButton deleteFAB;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        Log.d("TAG","Test");
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

        fabOpenAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.open_fab);

        fabCloseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.close_fab);
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        username = v.findViewById(R.id.username_text);
        password = v.findViewById(R.id.password_text);
        accountType = v.findViewById(R.id.account_layout_text);
        baseFAB = v.findViewById(R.id.baseFloatingActionButton);
        editLayout = v.findViewById(R.id.edit_layout);
        deleteLayout = v.findViewById(R.id.delete_layout);
        editFAB = v.findViewById(R.id.editFab);
        editFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapseFabMenu();
                LayoutInflater factory = LayoutInflater.from(getContext());
                final View editDialogView = factory.inflate(R.layout.dialog_update_user, null);
                final AlertDialog editDialog = new AlertDialog.Builder(getContext()).create();
                editDialog.setView(editDialogView);

                editDialogView.findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editDialogView.findViewById(R.id.progressBar25).setVisibility(View.VISIBLE);
                        TextInputEditText newUsername = editDialogView.findViewById(R.id.newUsername);
                        TextInputEditText newPassword = editDialogView.findViewById(R.id.newPassword);
                        TextInputEditText previousPassword = editDialogView.findViewById(R.id.oldPassword);

                        APIClient
                                .getAPIInterface()
                                .updateUser(usernameText,
                                        token,
                                        previousPassword.getText().toString(),
                                        newUsername.getText().toString().length()==0?usernameText:newUsername.getText().toString(),
                                        newPassword.getText().toString().length()==0?previousPassword.getText().toString():newPassword.getText().toString())
                                .enqueue(new Callback<UpdateUserResponseModel>() {
                                    @Override
                                    public void onResponse(Call<UpdateUserResponseModel> call, Response<UpdateUserResponseModel> response) {
                                        editDialogView.findViewById(R.id.progressBar25).setVisibility(View.INVISIBLE);
                                        if(response.body().isSuccess()){
                                            Toast.makeText(getContext(), "Account Updated", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getContext(),LoginActivity.class);
                                            startActivity(intent);
                                        }
                                        editDialog.dismiss();
                                    }

                                    @Override
                                    public void onFailure(Call<UpdateUserResponseModel> call, Throwable t) {
                                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                        editDialogView.findViewById(R.id.progressBar12).setVisibility(View.INVISIBLE);

                                    }
                                });

                    }
                });


                editDialogView.findViewById(R.id.cancelUpdate).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editDialog.dismiss();
                    }
                });

                editDialog.show();
            }
        });

        deleteFAB = v.findViewById(R.id.deleteFab);
        deleteFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapseFabMenu();
                LayoutInflater factory = LayoutInflater.from(getContext());
                final View deleteDialogView = factory.inflate(R.layout.dialog_delete, null);
                final AlertDialog deleteDialog = new AlertDialog.Builder(getContext()).create();
                deleteDialog.setView(deleteDialogView);
                deleteDialogView.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteDialogView.findViewById(R.id.progressBar12).setVisibility(View.VISIBLE);
                        TextInputEditText reenter = deleteDialogView.findViewById(R.id.reenter);
                        APIClient.getAPIInterface()
                                .deleteUser(usernameText,token,reenter.getText().toString())
                                .enqueue(new Callback<DeleteUserResponseModel>() {
                                    @Override
                                    public void onResponse(Call<DeleteUserResponseModel> call, Response<DeleteUserResponseModel> response) {
                                        deleteDialogView.findViewById(R.id.progressBar12).setVisibility(View.INVISIBLE);
                                        if(response.body().isSuccess()){
                                            Toast.makeText(getContext(), "Account Deleted", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getContext(),LoginActivity.class);
                                            startActivity(intent);
                                        }
                                        deleteDialog.dismiss();


                                    }

                                    @Override
                                    public void onFailure(Call<DeleteUserResponseModel> call, Throwable t) {
                                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                        deleteDialogView.findViewById(R.id.progressBar12).setVisibility(View.INVISIBLE);

                                    }
                                });

                    }
                });
                deleteDialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteDialog.dismiss();
                    }
                });

                deleteDialog.show();

            }
        });
        baseFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFabMenuOpen)
                    collapseFabMenu();
                else
                    expandFabMenu();
            }
        });
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        token = prefs.getString("token",null);
        usernameText = prefs.getString("username",null);
        sync();




        return v;
    }

    private void sync(){
        APIClient
                .getAPIInterface()
                .sync(usernameText,token)
                .enqueue(new Callback<SyncResponseModel>() {
                    @Override
                    public void onResponse(Call<SyncResponseModel> call, Response<SyncResponseModel> response) {
                        Log.d("TAG",call.toString());

                        if(response.body().isSuccess()){
                            Log.d("TAG",response.body().getUsername());
                            username.setText(response.body().getUsername());
                            password.setText(response.body().getPassword());
                            accountType.setText(response.body().getAccounttype());
                        }
                    }

                    @Override
                    public void onFailure(Call<SyncResponseModel> call, Throwable t) {
                        Log.d("TAG",call.request().toString());

                        Log.d("TAG",t.getMessage());

                    }
                });
    }


    private void expandFabMenu() {

baseFAB.setImageResource(R.drawable.ic_cross);
        editLayout.startAnimation(fabOpenAnimation);
        deleteLayout.startAnimation(fabOpenAnimation);
        editLayout.setClickable(true);
        deleteLayout.setClickable(true);
        isFabMenuOpen = true;

    }

    private void collapseFabMenu() {

        baseFAB.setImageResource(R.drawable.ic_menu);
        editLayout.startAnimation(fabCloseAnimation);
        deleteLayout.startAnimation(fabCloseAnimation);
        editLayout.setClickable(false);
        deleteLayout.setClickable(false);
        isFabMenuOpen = false;

    }



}


