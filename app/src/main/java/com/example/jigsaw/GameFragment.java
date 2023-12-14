package com.example.jigsaw;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param level Parameter 1.
     * @param userName Parameter 2.
     * @return A new instance of fragment GameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameFragment newInstance(String level, String userName) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, level);
        args.putString(ARG_PARAM2, userName);
        fragment.setArguments(args);
        return fragment;
    }

    ViewGroup game;
    TableLayout background_board;
    String difficulty;
    int side_quantity;
    int total_quantity;
    AssetManager assetManager;
    String image_name;
    int image_index;
    TextView name;
    List<ImagePiece> imagePieceList;
    int screen_width;
    int[][] indexs;
    int blank;
    int[] blankXY;
    int step;
    TextView step_show;
    String username;

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
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        difficulty = mParam1;
        username = mParam2;

        if (difficulty.equals("简单")) {
            side_quantity = 2;
        } else if (difficulty.equals("中等")) {
            side_quantity = 3;
        } else {
            side_quantity = 4;
        }

        Random random = new Random();

        game = view.findViewById(R.id.game_board);
        background_board = (TableLayout) game.getChildAt(game.getChildCount() - 2);

        total_quantity = side_quantity * side_quantity;

        blank = random.nextInt(side_quantity * side_quantity);
        blankXY = new int[2];

        assetManager = getActivity().getAssets();
        ImageManager imageManager = new ImageManager(assetManager, "jigsawPicture/");
        image_index = random.nextInt(imageManager.count());
        image_name = imageManager.getName(image_index);
        imagePieceList = ImageSplitter.split(
                imageManager.get(image_index), side_quantity, side_quantity);

        name = view.findViewById(R.id.name);
        name.setText(image_name);


        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        screen_width = dm.widthPixels;

        indexs = get_random_nums();

        step = 0;
        step_show = view.findViewById(R.id.step);

        add_Jigsaws();
        set_images();
        view.findViewById(R.id.give_up_botton).setOnClickListener((v) -> {
            game_end(false);
        });
        return view;
    }


    private void add_Jigsaws() {
        for (int i = 0; i < side_quantity; i++) {
            getLayoutInflater().inflate(R.layout.jigsaw_row, background_board);
            LinearLayout linearLayout = (LinearLayout)
                    background_board.getChildAt(
                            background_board.getChildCount() - 1);
            for (int j = 0; j < side_quantity; j++) {
                ImageButton imageButton = new ImageButton(getActivity());
                imageButton.setOnClickListener(view -> {
                    ImageButton click_button = (ImageButton) view;

                    for (int i1 = 0; i1 < side_quantity; i1++) {
                        LinearLayout linearLayout1 =
                                (LinearLayout) background_board.getChildAt(i1);
                        for (int j1 = 0; j1 < side_quantity; j1++) {
                            ImageButton imageButton1 =
                                    (ImageButton) linearLayout1.getChildAt(j1);
                            if (imageButton1 == click_button) {
                                int[] coordinate = {j1, i1};
                                move(coordinate);
                                check_win();
                                update_status();
                                break;
                            }
                        }
                    }
                });
                linearLayout.addView(imageButton);
            }
        }
    }

    private void check_win() {
        boolean is_win = true;
        int correct_index = 0;
        for (int i = 0; i < side_quantity; i++) {
            for (int j = 0; j < side_quantity; j++) {
                if (indexs[i][j] != correct_index) {
                    is_win = false;
                }
                correct_index++;
            }
        }
        if (is_win) {
            game_end(true);
        }
    }

    public void abandon(View view) {
        game_end(false);
    }

    private void set_images() {
        for (int i = 0; i < side_quantity; i++) {
            ViewGroup viewGroup = (ViewGroup) background_board.getChildAt(i);
            for (int j = 0; j < side_quantity; j++) {
                ImageButton imageButton = (ImageButton) viewGroup.getChildAt(j);
                imageButton.setScaleType(ImageView.ScaleType.FIT_XY);
                imageButton.post(() -> {
                    ViewGroup.LayoutParams params = imageButton.getLayoutParams();
                    params.width = (screen_width - 100) / side_quantity;
                    params.height = (screen_width - 100) / side_quantity;
                    imageButton.setLayoutParams(params);
                });
                int index = indexs[i][j];

                if (index != blank) {
                    imageButton.setImageBitmap(imagePieceList.get(index).bitmap);
                } else {
                    blankXY[0] = j;
                    blankXY[1] = i;
                    imageButton.setImageBitmap(new ImageManager(
                            assetManager, "blank/").get(0));
                }

            }
        }
    }

    private int[][] get_random_nums() {
        int[] nums = new int[total_quantity];
        for (int i = 0; i < total_quantity; i++) {
            nums[i] = i;
        }
        Random random = new Random();
        for (int i = 0; i < total_quantity; i++) {
            int index = random.nextInt(total_quantity);
            int temp = nums[i];
            nums[i] = nums[index];
            nums[index] = temp;
        }
        int[][] nums_two = new int[side_quantity][side_quantity];
        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i != 0 && i % side_quantity == 0) {
                count++;
            }
            nums_two[count][i % side_quantity] = nums[i];
        }
        return nums_two;
    }

    private void move(int[] coordinate) {
        int temp = indexs[coordinate[1]][coordinate[0]];
        indexs[coordinate[1]][coordinate[0]] = indexs[blankXY[1]][blankXY[0]];
        indexs[blankXY[1]][blankXY[0]] = temp;
        set_images();
    }

    private void game_end(boolean is_win) {
        ArrayList<String> data = new ArrayList<>();
        data.add(image_name);
        data.add(String.valueOf(image_index));
        data.add(String.valueOf(screen_width));
        data.add(username);
        data.add(String.valueOf(step));
        data.add(difficulty);

        ScoreFragment scoreFragment = ScoreFragment.newInstance(data,is_win);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // 开始 Fragment 事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment oldFragment = fragmentManager.findFragmentById(R.id.game_fragment);
        if (oldFragment != null) {
            transaction.remove(oldFragment);
        }
        // 用新的 Fragment 替换当前 Fragment（假设是 LoginFragment）
        transaction.replace(R.id.game_board, scoreFragment);

        // 提交事务
        transaction.commit();
    }

    @SuppressLint("DefaultLocale")
    private void update_status() {
        step++;
        step_show.setText(String.format("你走了%d步", step));
    }
}