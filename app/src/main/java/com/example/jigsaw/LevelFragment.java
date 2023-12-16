package com.example.jigsaw;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LevelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LevelFragment extends Fragment {





    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    public LevelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param username Parameter 1.
     * @return A new instance of fragment GameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LevelFragment newInstance(String username) {
        LevelFragment fragment = new LevelFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_level,
                container, false);

        view.findViewById(R.id.play).setOnClickListener((v) -> {
            play();
        });
        view.findViewById(R.id.history_button).setOnClickListener((v) -> {
            showHistory();
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    public void play(){
        Spinner spinner = view.findViewById(R.id.difficulty);
        GameFragment gameFragment = GameFragment.newInstance(spinner.getSelectedItem().toString(),mParam1);

        // 获取 Fragment 管理器
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // 开始 Fragment 事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment oldFragment = fragmentManager.findFragmentById(R.id.level_fragment);
        if (oldFragment != null) {
            transaction.remove(oldFragment);
        }
        // 用新的 Fragment 替换当前 Fragment（假设是 LoginFragment）
        transaction.replace(R.id.game_board, gameFragment);

        // 提交事务
        transaction.commit();
    }

    public void showHistory(){
        HistoryFragment historyFragment = HistoryFragment.newInstance(mParam1);

        // 获取 Fragment 管理器
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // 开始 Fragment 事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment oldFragment = fragmentManager.findFragmentById(R.id.level_fragment);
        if (oldFragment != null) {
            transaction.remove(oldFragment);
        }
        // 用新的 Fragment 替换当前 Fragment（假设是 LoginFragment）
        transaction.replace(R.id.game_board, historyFragment);

        // 提交事务
        transaction.commit();
    }


}