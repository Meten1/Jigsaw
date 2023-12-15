package com.example.jigsaw;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<String> mParam1;
    private boolean mParam2;

    public ScoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScoreFragment newInstance(ArrayList<String> param1, boolean param2) {
        ScoreFragment fragment = new ScoreFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, param1);
        args.putBoolean(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getStringArrayList(ARG_PARAM1);
            mParam2 = getArguments().getBoolean(ARG_PARAM2);
        }
    }

    View view;

    AssetManager assetManager;
    TextView message;
    TextView show_name;
    ImageView imageView;
    TextView step_tip;
    int screen_width;
    String username;
    int step;
    String difficulty;

    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_score, container, false);

        assetManager = getActivity().getAssets();
        ImageManager imageManager = new ImageManager(assetManager, "jigsawPicture/");

        message = view.findViewById(R.id.message);
        imageView = view.findViewById(R.id.image);


        boolean is_win = mParam2;
        if (is_win) {
            message.setText("你做的很棒，继续下去！");
        } else {
            message.setText("不要灰心，再试一次，你可以的！");
        }

        show_name = view.findViewById(R.id.name);
        show_name.setText(mParam1.get(0));

        int image_index = Integer.parseInt(mParam1.get(1));
        imageView.setImageBitmap(imageManager.get(image_index));

        screen_width = Integer.parseInt(mParam1.get(2));;

        username = mParam1.get(3);
        step = Integer.parseInt(mParam1.get(4));
        difficulty = mParam1.get(5);


        imageView.post(() -> {
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            params.width = (screen_width - 100);
            params.height = (screen_width - 100);
            imageView.setLayoutParams(params);
        });

        step_tip = view.findViewById(R.id.step_tip);
        step_tip.setText(String.format("你一共走了%d步", step));

        view.findViewById(R.id.again_button).setOnClickListener((v) -> {
            again();
        });

        view.findViewById(R.id.stop_button).setOnClickListener((v) -> {
            stop();
        });

        return view;
    }

    public void again() {
        LevelFragment levelFragment = new LevelFragment();

        // 获取 Fragment 管理器
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // 开始 Fragment 事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment oldFragment = fragmentManager.findFragmentById(R.id.score_fragment);
        if (oldFragment != null) {
            transaction.remove(oldFragment);
        }
        // 用新的 Fragment 替换当前 Fragment（假设是 LoginFragment）
        transaction.replace(R.id.game_board, levelFragment);

        // 提交事务
        transaction.commit();
    }

    public void stop() {
        LoginFragment loginFragment = new LoginFragment();

        // 获取 Fragment 管理器
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // 开始 Fragment 事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment oldFragment = fragmentManager.findFragmentById(R.id.score_fragment);
        if (oldFragment != null) {
            transaction.remove(oldFragment);
        }
        // 用新的 Fragment 替换当前 Fragment（假设是 LoginFragment）
        transaction.replace(R.id.game_board, loginFragment);

        // 提交事务
        transaction.commit();
    }
}