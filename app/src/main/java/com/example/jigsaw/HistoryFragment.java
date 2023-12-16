package com.example.jigsaw;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    public HistoryFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment HistoryFragment.
     */
    public static HistoryFragment newInstance(String param1) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    String username;
    View view;
    SqlManager sqlManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        sqlManager = new SqlManager(getActivity(), "user.db", null, 1);
        username = mParam1;
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_history, container, false);
        TextView welcome = view.findViewById(R.id.welcome_text);
        welcome.setText("欢迎您, " + username + ", 游玩拼图快快快！");
        SqlManager sqlManager = new SqlManager(
                getActivity(), "user.db", null, 1);
        SQLiteDatabase db = sqlManager.getWritableDatabase();
        String[] columns = {"username", "Easy", "Medium", "Hard"};
        @SuppressLint("DefaultLocale") Cursor cursor = db.query(
                "user", columns, String.format("username = '%s'", username),
                null, null, null, null);
        int easy = 0;
        int medium = 0;
        int hard = 0;
        if (cursor.moveToFirst()) {
            easy = cursor.getInt(1);
            medium = cursor.getInt(2);
            hard = cursor.getInt(3);
        }
        cursor.close();
        db.close();
        TextView intro = view.findViewById(R.id.introduction_text);
        intro.setText(String.format("您的历史成功次数为：\n简单：%d\n中等：%d\n困难：%d\n", easy, medium, hard));

        view.findViewById(R.id.back_button).setOnClickListener((v) -> back());
        return view;
    }

    public void back() {
        LevelFragment levelFragment = LevelFragment.newInstance(username);
        // 获取 Fragment 管理器
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // 开始 Fragment 事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment oldFragment = fragmentManager.findFragmentById(R.id.history_fragment);
        if (oldFragment != null) {
            transaction.remove(oldFragment);
        }
        // 用新的 Fragment 替换当前 Fragment（假设是 LoginFragment）
        transaction.replace(R.id.game_board, levelFragment);

        // 提交事务
        transaction.commit();
    }


}