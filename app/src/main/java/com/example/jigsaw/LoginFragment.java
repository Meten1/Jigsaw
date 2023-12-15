package com.example.jigsaw;

import android.content.Intent;
import android.database.Cursor;
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
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
    EditText name_input;
    EditText password_input;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login,
                container, false);
        sqlManager = new SqlManager(getActivity(), "user.db", null, 1);
        name_input = view.findViewById(R.id.name_input);
        password_input = view.findViewById(R.id.password_input);

        view.findViewById(R.id.login_button).setOnClickListener((v) -> {
            check();
        });
        view.findViewById(R.id.register_button).setOnClickListener((v) -> {
            register();
        });

        view.findViewById(R.id.recover_button).setOnClickListener((v) -> {
            recover();
        });
        return view;
    }

    public void check(){
        String username = name_input.getText().toString();
        String password = password_input.getText().toString();
        if (!username.equals("") && !password.equals("")) {
            SQLiteDatabase db = sqlManager.getReadableDatabase();
            String[] columns = {"username", "password"};
            Cursor cursor = db.query("user", columns,
                    String.format("username = '%s'", username),
                    null, null, null, null);
            if (cursor.moveToNext()) {
                if (password.equals(cursor.getString(1))) {
                    Toast.makeText(getActivity(),
                            "登陆成功，欢迎！", Toast.LENGTH_LONG).show();
                    cursor.close();
                    db.close();
                    login(username);
                } else {
                    Toast.makeText(getActivity(),
                            "用户名或密码错误，请检查你的用户名与密码！",
                            Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(),
                        "此用户不存在", Toast.LENGTH_LONG).show();
            }
            cursor.close();
            db.close();
            password_input.setText("");
        } else {
            Toast.makeText(getActivity(),
                    "用户名与密码不可为空", Toast.LENGTH_LONG).show();
        }
    }

    public void login(String username){
        LevelFragment levelFragment = LevelFragment.newInstance(username);

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

    public void register() {
        RegisterFragment registerFragment = new RegisterFragment();

        // 获取 Fragment 管理器
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // 开始 Fragment 事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment oldFragment = fragmentManager.findFragmentById(R.id.login_fragment);
        if (oldFragment != null) {
            transaction.remove(oldFragment);
        }
        // 用新的 Fragment 替换当前 Fragment（假设是 LoginFragment）
        transaction.replace(R.id.game_board, registerFragment);


        // 提交事务
        transaction.commit();
    }

    public void recover(){
        RecoverFragment recoverFragment = new RecoverFragment();

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // 开始 Fragment 事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment oldFragment = fragmentManager.findFragmentById(R.id.login_fragment);
        if (oldFragment != null) {
            transaction.remove(oldFragment);
        }
        // 用新的 Fragment 替换当前 Fragment（假设是 LoginFragment）
        transaction.replace(R.id.game_board, recoverFragment);

        // 提交事务
        transaction.commit();
    }




}