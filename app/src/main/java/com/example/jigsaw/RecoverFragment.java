package com.example.jigsaw;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecoverFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecoverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecoverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecoverFragment newInstance(String param1, String param2) {
        RecoverFragment fragment = new RecoverFragment();
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
    SqlManager sqlManager;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recover, container, false);
        sqlManager = new SqlManager(getActivity(), "user.db", null, 1);

        view.findViewById(R.id.recover_button).setOnClickListener((v) -> {
            recover();
        });
        view.findViewById(R.id.back_button).setOnClickListener((v) -> {
            back();
        });

        return view;
    }


    public void recover(){
        EditText revoverInput = view.findViewById(R.id.recover_input);
        EditText passwordInput = view.findViewById(R.id.password_input);
        SQLiteDatabase db = sqlManager.getWritableDatabase();
        @SuppressLint("DefaultLocale") String sql = String.format(
                "UPDATE user set password = %s WHERE recover = '%s'",
                passwordInput.getText().toString(),revoverInput.getText().toString());
        try {
            db.execSQL(sql);
        } catch (Exception ignored){
            System.out.println(passwordInput.getText().toString()+"  "+revoverInput.getText().toString() + "------------------------");
        }
        db.close();
        back();
    }

    public void back(){
        LoginFragment loginFragment = new LoginFragment();
        // 获取 Fragment 管理器
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // 开始 Fragment 事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment oldFragment = fragmentManager.findFragmentById(R.id.recover_fragment);
        if (oldFragment != null) {
            transaction.remove(oldFragment);
        }
        // 用新的 Fragment 替换当前 Fragment（假设是 LoginFragment）
        transaction.replace(R.id.game_board, loginFragment);

        // 提交事务
        transaction.commit();
    }
}