package com.example.jigsaw;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {


    public RegisterFragment() {
        // Required empty public constructor
    }


    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        sqlManager = new SqlManager(getActivity(), "user.db", null, 1);
        name_input = view.findViewById(R.id.name_input);
        password_input = view.findViewById(R.id.password_input);
        recover_input = view.findViewById(R.id.recover_input);

        view.findViewById(R.id.register_login_button).setOnClickListener((v) -> check());

        return view;
    }

    SqlManager sqlManager;
    EditText name_input;
    EditText password_input;
    EditText recover_input;

    public void check() {
        String username = name_input.getText().toString();
        String password = password_input.getText().toString();
        String recover = recover_input.getText().toString();
        if (!username.equals("") && !password.equals("")) {
            if (recover.length() == 6) {
                SQLiteDatabase db = sqlManager.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("username", username);
                contentValues.put("password", password);
                contentValues.put("recover", recover);
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
                        "恢复代码必须是6位且不可与已有代码重复！", Toast.LENGTH_LONG).show();
                recover_input.setText("");
            }
        } else {
            Toast.makeText(getActivity(),
                    "用户名和密码不可为空！", Toast.LENGTH_LONG).show();
        }
    }

    public void start() {
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