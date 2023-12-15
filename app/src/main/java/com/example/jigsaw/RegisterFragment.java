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
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_register, container, false);
        sqlManager = new SqlManager(getActivity(), "user.db", null, 1);
        name_input = view.findViewById(R.id.name_input);
        password_input = view.findViewById(R.id.password_input);
        recover_input = view.findViewById(R.id.recover_input);

        view.findViewById(R.id.register_login_button).setOnClickListener((v) -> {
            check();
        });
        return view;
    }

    SqlManager sqlManager;
    EditText name_input;
    EditText password_input;
    EditText recover_input;

    public void check(){
        String username = name_input.getText().toString();
        String password = password_input.getText().toString();
        String recover = recover_input.getText().toString();
        if (!username.equals("")&&!password.equals("")){
            if (recover.length() == 6){
                SQLiteDatabase db = sqlManager.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("username", username);
                contentValues.put("password", password);
                contentValues.put("recover",recover);
                contentValues.put("Easy", 0);
                contentValues.put("Medium", 0);
                contentValues.put("Hard", 0);
                if (db.insert("user", null, contentValues) != -1) {
                    Toast.makeText(getActivity(),
                            "注册成功，欢迎！", Toast.LENGTH_LONG).show();
                    db.close();
                    start();
                } else {
                    Toast.makeText(getActivity(),
                            "注册失败，请检查用户名是否已存在！", Toast.LENGTH_LONG).show();
                }
                db.close();
            } else {
                Toast.makeText(getActivity(),
                        "恢复代码必须是6位！", Toast.LENGTH_LONG).show();
                recover_input.setText("");
            }
        } else {
            Toast.makeText(getActivity(),
                    "用户名和密码不可为空！", Toast.LENGTH_LONG).show();
        }
    }

    public void start(){
        LevelFragment levelFragment = new LevelFragment();

        // 获取 Fragment 管理器
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // 开始 Fragment 事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment oldFragment = fragmentManager.findFragmentById(R.id.register_fragment);
        if (oldFragment != null) {
            transaction.remove(oldFragment);
        }
        // 用新的 Fragment 替换当前 Fragment（假设是 LoginFragment）
        transaction.replace(R.id.game_board, levelFragment);

        // 提交事务
        transaction.commit();
    }
}