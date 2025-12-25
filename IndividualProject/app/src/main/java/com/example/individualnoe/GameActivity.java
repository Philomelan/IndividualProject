package com.example.individualnoe;

import static java.lang.Math.min;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private TextView tvYear, tvFood, tvEnergy, tvTech, tvArt, tvCharName, tvQuestion;
    private ImageButton btnLeftArrow, btnRightArrow, btnMainMenu, btnSettings;
    private ImageView ivCharacter;
    private CardView cardContent;

    private int year = 1;
    private int food = 15;
    private int energy = 15;
    private int tech = 15;
    private int art = 15;

    private List<Character> characters;
    private List<Question> questions = new ArrayList<>();
    private int currentQuestionId = 0;
    private boolean buttonsEnabled = true;

    private HistoryPrefs historyPrefs;
    private AudioManager audioManager;

    private String questionText;
    private int current_food, current_energy, current_tech, current_art;
    private boolean isHandlingArrowClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.game), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setListeners();

        characters = CharacterLoader.loadCharacters(this);
        questions = QuestionLoader.loadQuestionsWithCharacters(this, characters);
        prepareQuestions();

        historyPrefs = HistoryPrefs.getInstance(this);
        audioManager = AudioManager.getInstance(this);

        updateUI();
    }

    private void initViews() {
        tvYear = findViewById(R.id.tvYear);
        tvFood = findViewById(R.id.tvFood);
        tvEnergy = findViewById(R.id.tvEnergy);
        tvTech = findViewById(R.id.tvTech);
        tvArt = findViewById(R.id.tvArt);
        tvCharName = findViewById(R.id.tvCharName);
        tvQuestion = findViewById(R.id.tvQuestion);
        btnLeftArrow = findViewById(R.id.btnLeftArrow);
        btnRightArrow = findViewById(R.id.btnRightArrow);
        btnMainMenu = findViewById(R.id.btnMainMenu);
        btnSettings = findViewById(R.id.btnSettings);
        ivCharacter = findViewById(R.id.ivCharacter);
        cardContent = findViewById(R.id.cardContent);
    }

    private void setListeners() {
        btnLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleArrowClick(false);
            }
        });

        btnRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleArrowClick(true);
            }
        });

        btnLeftArrow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showAnswerAndCons(false);
                return true;
            }
        });

        btnRightArrow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showAnswerAndCons(true);
                return true;
            }
        });

        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        restoreOriginalText();
                        return false;
                }
                return false;
            }
        };
        btnLeftArrow.setOnTouchListener(touchListener);
        btnRightArrow.setOnTouchListener(touchListener);

        btnMainMenu.setOnClickListener(v -> {
                audioManager.playClickSound();
                finish();
        });

        btnSettings.setOnClickListener(v -> {
            audioManager.playClickSound();
            Intent intent = new Intent(GameActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }


    private void handleArrowClick(boolean isRight) {
        if (!buttonsEnabled) return;
        isHandlingArrowClick = true;

        Question currentQuestion = questions.get(currentQuestionId);
        currentQuestion.shown = true;

        int[] consequences = isRight ? currentQuestion.consB : currentQuestion.consA;
        food = min(30, food + consequences[0]);
        energy = min(30, energy + consequences[1]);
        tech = min(30, tech + consequences[2]);
        art = min(30, art + consequences[3]);

        if (allQuestionsShown()) {
            prepareQuestions();
            currentQuestionId = 0;
        } else {
            currentQuestionId++;
        }

        updateResources();
        if (checkGameOver()) {
            showDefeatDialog();
        } else {
            setButtonsEnabled(false);
            setTextVisibility(false);
            cardContent.postDelayed(() -> {
                animateCardExit(isRight);
            }, 150);
            isHandlingArrowClick = false;
        }
    }

    private void setButtonsEnabled(boolean enabled) {
        buttonsEnabled = enabled;
        btnLeftArrow.setEnabled(enabled);
        btnRightArrow.setEnabled(enabled);

        if (enabled) {
            animateButtonsIn();
        } else {
            animateButtonsOut();
        }
    }

    private void animateButtonsOut() {
        btnLeftArrow.animate()
                .alpha(0f)
                .scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(150)
                .start();

        btnRightArrow.animate()
                .alpha(0f)
                .scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(150)
                .start();
    }

    private void animateButtonsIn() {
        btnLeftArrow.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(150)
                .start();

        btnRightArrow.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(150)
                .start();
    }

    private void setTextVisibility(boolean visibility) {
        if (visibility) {
            animateTextIn();
        } else {
            animateTextOut();
        }
    }

    private void animateTextOut() {
        tvCharName.animate()
                .alpha(0f)
                .scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(150)
                .start();

        tvQuestion.animate()
                .alpha(0f)
                .scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(150)
                .start();
    }

    private void animateTextIn() {
        tvCharName.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(150)
                .start();

        tvQuestion.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(150)
                .start();
    }

    private void animateCardExit(boolean isRight) {
        audioManager.playSwipeSound();
        float targetX = isRight ? 1000f : -1000f;

        cardContent.animate()
                .translationX(targetX)
                .alpha(0f)
                .setDuration(300)
                .setInterpolator(new AccelerateInterpolator())
                .withEndAction(() -> {
                    year++;
                    updateUI();

                    cardContent.postDelayed(() -> {
                        cardContent.setTranslationX(0f);
                        cardContent.animate()
                                .alpha(1f)
                                .setDuration(300)
                                .start();
                    }, 200);

                    cardContent.postDelayed(() -> {
                        setButtonsEnabled(true);
                        setTextVisibility(true);
                    }, 150);
                })
                .start();
    }

    @SuppressLint("SetTextI18n")
    private void showAnswerAndCons(boolean isRightAnswer) {
        if (!buttonsEnabled) return;

        Question currentQuestion = questions.get(currentQuestionId);
        String answerText = isRightAnswer ? currentQuestion.optionB : currentQuestion.optionA;
        questionText = currentQuestion.questionText;
        tvQuestion.setText(answerText);

        current_food = food;
        current_energy = energy;
        current_tech = tech;
        current_art = art;
        int[] cons = isRightAnswer ? currentQuestion.consB : currentQuestion.consA;
        tvFood.setText(current_food + " (" + cons[0] + ")");
        tvEnergy.setText(current_energy + " (" + cons[1] + ")");
        tvTech.setText(current_tech + " (" + cons[2] + ")");
        tvArt.setText(current_art + " (" + cons[3] + ")");
    }

    private void restoreOriginalText() {
        if (isHandlingArrowClick) return;

        tvQuestion.setText(questionText);
        tvFood.setText(String.valueOf(current_food));
        tvEnergy.setText(String.valueOf(current_energy));
        tvTech.setText(String.valueOf(current_tech));
        tvArt.setText(String.valueOf(current_art));
    }


    private void prepareQuestions() {
        for (Question q : questions) {
            q.shown = false;
        }
        Collections.shuffle(questions);
    }

    private boolean allQuestionsShown() {
        for (Question question : questions) {
            if (!question.shown) {
                return false;
            }
        }
        return true;
    }

    private void updateResources() {
        tvFood.setText(String.valueOf(food));
        tvEnergy.setText(String.valueOf(energy));
        tvTech.setText(String.valueOf(tech));
        tvArt.setText(String.valueOf(art));
    }

    private void updateUI() {
        tvYear.setText("Год: " + year);
        updateResources();

        Question currentQuestion = questions.get(currentQuestionId);
        tvQuestion.setText(currentQuestion.questionText);

        Character character = currentQuestion.character;
        tvCharName.setText(character.name);
        int avatarResId = character.getAvatarResId(this);
        ivCharacter.setImageResource(avatarResId);
    }

    private boolean checkGameOver() {
        return food <= 0 || energy <= 0 || tech <= 0 || art <= 0;
    }

    private void showDefeatDialog() {
        setButtonsEnabled(false);

        String deathMessage = "";
        String deathReason = "";
        if (food <= 0) {
            deathMessage = "Последние консервы съедены. Голодная смерть настигла всех через " + year + " лет.";
            deathReason = "Голодная смерть";
        } else if (energy <= 0) {
            deathMessage = "Генераторы замолчали. В темноте и холоде поселение не выжило. На " + year + " году последний свет погас.";
            deathReason = "Энергоколлапс";
        } else if (tech <= 0) {
            deathMessage = "Без технологий вы не смогли противостоять угрозам пустоши. Цивилизация продержалась " + year + " лет.";
            deathReason = "Упадок технологий";
        } else if (art <= 0) {
            deathMessage = "Духовный упадок и потеря морали разрушили ваше общество изнутри. Выживание потеряло смысл через " + year + " лет.";
            deathReason = "Духовный крах";
        }

        GameAttempt attempt = new GameAttempt(year, deathReason);
        historyPrefs.saveAttempt(attempt);

        new AlertDialog.Builder(this)
                .setTitle("Конец игры")
                .setMessage(deathMessage)
                .setPositiveButton("Новая игра", (dialog, which) -> {
                    restartGame();
                    dialog.dismiss();
                })
                .setNegativeButton("В меню", (dialog, which) -> {
                    finish();
                })
                .setIcon(R.drawable.ic_defeat)
                .setCancelable(false)
                .show();
    }

    private void restartGame() {
        year = 1;
        food = 15;
        energy = 15;
        tech = 15;
        art = 15;

        prepareQuestions();
        currentQuestionId = 0;

        updateUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        audioManager.pauseMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        audioManager.resumeMusic();
    }
}