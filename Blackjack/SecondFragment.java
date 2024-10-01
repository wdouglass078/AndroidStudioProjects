package com.example.blackjack;

import android.annotation.SuppressLint;
/*import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;*/
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/*import androidx.annotation.DrawableRes;
import androidx.annotation.MainThread;*/
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
/*import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;*/
import androidx.navigation.fragment.NavHostFragment;

import com.example.blackjack.databinding.FragmentSecondBinding;

//import java.lang.reflect.Array;
import java.lang.reflect.Array;
import java.util.Random;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SecondFragment extends Fragment {
    public int playerScore = 0;
    public int dealerScore = 0;
    public boolean runTime = true;
    public boolean aceInPlayerHand = false;
    public boolean aceInDealerHand = false;
    public boolean playerTenAdded = false;
    public boolean dealerTenAdded = false;
    public int blankCardImage;
    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //player and dealer hands
        ArrayList<Integer> playerNums = new ArrayList<Integer>();
        ArrayList<Integer> dealerNums = new ArrayList<Integer>();
        //"creates" a deck of cards.
        ArrayList<Integer> cardValues = new ArrayList<Integer>();
        for (int i = 0; i < 52; i++) {
            int temp = (i + 1) % 13;
            if (temp > 10 || temp == 0) {
                temp = 10;
            }
            cardValues.add(temp);
        }
        //"creates" a deck of cards with pictures.
        ArrayList<Integer> cardPics = new ArrayList<Integer>();
        cardPics.add(R.drawable.sa);
        cardPics.add(R.drawable.s2);
        cardPics.add(R.drawable.s3);
        cardPics.add(R.drawable.s4);
        cardPics.add(R.drawable.s5);
        cardPics.add(R.drawable.s6);
        cardPics.add(R.drawable.s7);
        cardPics.add(R.drawable.s8);
        cardPics.add(R.drawable.s9);
        cardPics.add(R.drawable.s10);
        cardPics.add(R.drawable.sj);
        cardPics.add(R.drawable.sq);
        cardPics.add(R.drawable.sk);
        cardPics.add(R.drawable.ha);
        cardPics.add(R.drawable.h2);
        cardPics.add(R.drawable.h3);
        cardPics.add(R.drawable.h4);
        cardPics.add(R.drawable.h5);
        cardPics.add(R.drawable.h6);
        cardPics.add(R.drawable.h7);
        cardPics.add(R.drawable.h8);
        cardPics.add(R.drawable.h9);
        cardPics.add(R.drawable.h10);
        cardPics.add(R.drawable.hj);
        cardPics.add(R.drawable.hq);
        cardPics.add(R.drawable.hk);
        cardPics.add(R.drawable.da);
        cardPics.add(R.drawable.d2);
        cardPics.add(R.drawable.d3);
        cardPics.add(R.drawable.d4);
        cardPics.add(R.drawable.d5);
        cardPics.add(R.drawable.d6);
        cardPics.add(R.drawable.d7);
        cardPics.add(R.drawable.d8);
        cardPics.add(R.drawable.d9);
        cardPics.add(R.drawable.d10);
        cardPics.add(R.drawable.dj);
        cardPics.add(R.drawable.dq);
        cardPics.add(R.drawable.dk);
        cardPics.add(R.drawable.ca);
        cardPics.add(R.drawable.c2);
        cardPics.add(R.drawable.c3);
        cardPics.add(R.drawable.c4);
        cardPics.add(R.drawable.c5);
        cardPics.add(R.drawable.c6);
        cardPics.add(R.drawable.c7);
        cardPics.add(R.drawable.c8);
        cardPics.add(R.drawable.c9);
        cardPics.add(R.drawable.c10);
        cardPics.add(R.drawable.cj);
        cardPics.add(R.drawable.cq);
        cardPics.add(R.drawable.ck);

        //initial conditions
        clearCards(view, playerNums, dealerNums);
        gameStart(view, cardPics, cardValues, playerNums, dealerNums);

        //allows for gameplay to occur
        runTimeThings(view, playerNums, cardPics, cardValues, dealerNums);


        //home button
        binding.homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });


    }

    void restartGame(View view) {
        //create a button that will reset everything
        binding.resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //deletes view and recreates it, effectively resetting everything.
                getParentFragmentManager().beginTransaction().detach(SecondFragment.this).commit();
                getParentFragmentManager().beginTransaction().attach(SecondFragment.this).commit();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    void declareWinner(View view, ArrayList<Integer> playerValues, ArrayList<Integer> pics,
                       ArrayList<Integer> nums, ArrayList<Integer> dealerValues) {
        TextView text = view.findViewById(R.id.textView2);

        if (playerScore > 21) {
            //win condition
            text.setText("Lose!");
        } else if (dealerScore > 21) {
            //win condition
            text.setText("Win!");
        } else if (playerScore == dealerScore) {
            //win condition
            text.setText("Tie!");
        } else if (playerScore > dealerScore) {
            //win condition
            text.setText("Win!");
        } else {
            //win condition
            text.setText("Lose!");
        }
        restartGame(view);
    }

    void runTimeThings(View view, ArrayList<Integer> playerValues, ArrayList<Integer> pics,
                       ArrayList<Integer> nums, ArrayList<Integer> dealerValues) {
        //method adds functionality to hit and stand buttons.
        if (playerScore == 21) { //blackjack
            //reveals second dealer card and adds its value
            ImageView blankCard = view.findViewById(R.id.dealerCard2);
            blankCard.setImageResource(blankCardImage);
            dealerScore += dealerValues.get(1);
            updateText(view, playerValues, dealerValues);
            declareWinner(view, playerValues, pics, nums, dealerValues);
        }
        else {
            view.findViewById(R.id.hitButton).setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    //if false, hit doesn't go through
                    if (runTime) {
                        if (playerScore < 21) {
                            //hit occurs.
                            hit(view, pics, nums, playerValues, dealerValues);
                            updateText(view, playerValues, dealerValues);
                        }
                        if (playerScore >= 21) {
                            if (playerTenAdded) {
                                //subtracts ten if already added above 21.
                                playerScore -= 10;
                                playerTenAdded = false;
                                updateText(view, playerValues, dealerValues);
                            } else {
                                //player stops playing at 21 or a loss.
                                dealerAI(view, pics, nums, playerValues, dealerValues);
                                updateText(view, playerValues, dealerValues);
                                declareWinner(view, playerValues, pics, nums, dealerValues);
                            }
                        }
                    }
                }
            });

            view.findViewById(R.id.standButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if false, hit and stand don't go through
                    if (runTime) {
                        //setting runTime false "deactivates" the hit button
                        runTime = false;
                        //stand button means dealer's turn
                        dealerAI(view, pics, nums, playerValues, dealerValues);
                        updateText(view, playerValues, dealerValues);
                        declareWinner(view, playerValues, pics, nums, dealerValues);
                    }
                }
            });
        }
    }

    void dealerAI(View view, ArrayList<Integer> pics, ArrayList<Integer> nums, ArrayList<Integer> playerValues,
                  ArrayList<Integer> dealerValues) {

        //flips over the blank card adds back the second index
        ImageView blankCard = view.findViewById(R.id.dealerCard2);
        blankCard.setImageResource(blankCardImage);
        dealerScore += dealerValues.get(1);

        Random r = new Random();
        while (dealerScore < 17) {
            int index = r.nextInt(pics.size());
            if ((nums.get(index) == 1)) {
                aceInDealerHand = true;
            }
            dealerScore += nums.get(index);
            dealerValues.add(nums.get(index));
            //if there is a new ace, it checks if 10 can be added
            if ((aceInDealerHand && (dealerScore + 10) <= 21) && !dealerTenAdded) {
                dealerScore += 10;
                dealerTenAdded = true;
            }
            //removes ten if needed
            if (dealerScore > 21 && dealerTenAdded) {
                dealerScore -= 10;
                dealerTenAdded = false;
            }
            //changes which card will be changed
            //only 11 cards allowed because any after is a 21 or bust.
            switch (dealerValues.size() - 1) {
                case 2:
                    ImageView dealerCard = view.findViewById(R.id.dealerCard3);
                    dealerCard.setImageResource(pics.get(index));
                    break;
                case 3:
                    dealerCard = view.findViewById(R.id.dealerCard4);
                    dealerCard.setImageResource(pics.get(index));
                    break;
                case 4:
                    dealerCard = view.findViewById(R.id.dealerCard5);
                    dealerCard.setImageResource(pics.get(index));
                    break;
                case 5:
                    dealerCard = view.findViewById(R.id.dealerCard6);
                    dealerCard.setImageResource(pics.get(index));
                    break;
                case 6:
                    dealerCard = view.findViewById(R.id.dealerCard7);
                    dealerCard.setImageResource(pics.get(index));
                    break;
                case 7:
                    dealerCard = view.findViewById(R.id.dealerCard8);
                    dealerCard.setImageResource(pics.get(index));
                    break;
                case 8:
                    dealerCard = view.findViewById(R.id.dealerCard9);
                    dealerCard.setImageResource(pics.get(index));
                    break;
                case 9:
                    dealerCard = view.findViewById(R.id.dealerCard10);
                    dealerCard.setImageResource(pics.get(index));
                    break;
                case 10:
                    dealerCard = view.findViewById(R.id.dealerCard11);
                    dealerCard.setImageResource(pics.get(index));
                    break;
                default:
                    break;
            }
        }
    }

    void clearCards(View view, ArrayList<Integer> playerValues, ArrayList<Integer> dealerValues) {
        //reset everything to initial conditions
        dealerScore = 0;
        playerScore = 0;
        aceInDealerHand = false;
        aceInPlayerHand = false;
        playerTenAdded = false;
        dealerTenAdded = false;
        runTime = true;

        playerValues.clear();
        dealerValues.clear();
        for (int i = 0; i < 11; i++) {
            ImageView dealerHand = view.findViewById(R.id.dealerCard1 + i);
            ImageView playerHand = view.findViewById(R.id.playerCard1 + i);
            dealerHand.setImageDrawable(null);
            playerHand.setImageDrawable(null);
        }
    }

    void gameStart(View view, ArrayList<Integer> pics, ArrayList<Integer> nums,
                   ArrayList<Integer> playerValues, ArrayList<Integer> dealerValues
    ) {
        //deals out first two cards for dealer and player
        ImageView firstDealer = view.findViewById(R.id.dealerCard1);
        ImageView secondDealer = view.findViewById(R.id.dealerCard2);
        ImageView firstPlayer = view.findViewById(R.id.playerCard1);
        ImageView secondPlayer = view.findViewById(R.id.playerCard2);

        Random r = new Random();
        int index = r.nextInt(pics.size());

        //first dealer
        firstDealer.setImageResource(pics.get(index));
        dealerScore += nums.get(index);
        dealerValues.add(nums.get(index));
        if (nums.get(index) == 1) {
            aceInDealerHand = true;
        }
        nums.remove(index);
        pics.remove(index);

        //first player
        index = r.nextInt(pics.size());
        firstPlayer.setImageResource(pics.get(index));
        playerScore += nums.get(index);
        playerValues.add(nums.get(index));
        if (nums.get(index) == 1) {
            aceInPlayerHand = true;
        }
        nums.remove(index);
        pics.remove(index);

        //second dealer
        index = r.nextInt(pics.size());
        secondDealer.setImageResource(R.drawable.blank);
        blankCardImage = pics.get(index);
        dealerValues.add(nums.get(index));
        dealerScore += nums.get(index);
        if (nums.get(index) == 1) {
            aceInDealerHand = true;
        }
        nums.remove(index);
        pics.remove(index);

        //second player
        index = r.nextInt(pics.size());
        secondPlayer.setImageResource(pics.get(index));
        playerValues.add(nums.get(index));
        playerScore += nums.get(index);
        if (nums.get(index) == 1) {
            aceInPlayerHand = true;
        }
        nums.remove(index);
        pics.remove(index);

        //checks for aces and adds 10
        if (aceInPlayerHand) {
            playerScore += 10;
            playerTenAdded = true;
        }
        if (aceInDealerHand) {
            dealerScore += 10;
            dealerTenAdded = true;
        }
        dealerScore -= dealerValues.get(1);
        updateText(view, playerValues, dealerValues);

        //if blackjacks occur
        if (playerScore == 21) {
            runTime = false;
            updateText(view, playerValues, dealerValues);
        }
        if (dealerScore == 21) {
            runTime = false;
            //player should not see dealer's second card value
            secondDealer.setImageResource(blankCardImage);
            updateText(view, playerValues, dealerValues);
        }
    }

    void hit(View view, ArrayList<Integer> pics, ArrayList<Integer> nums,
             ArrayList<Integer> playerValues, ArrayList<Integer> dealerValues
    ) {
        Random r = new Random();
        int index = r.nextInt(pics.size());
        //nextCard.setImageResource(pics.get(index));
        playerValues.add(nums.get(index));

        if ((nums.get(index) == 1)) {
            aceInPlayerHand = true;
        }
        playerScore += nums.get(index);
        //checks for ace and if ten can be added
        if ((aceInPlayerHand && (playerScore + 10) < 21) && !playerTenAdded) {
            playerScore += 10;
            playerTenAdded = true;
        }
        updateText(view, playerValues, dealerValues);

        //only 11 cards allowed because any after is a 21 or bust.
        switch (playerValues.size() - 1) {
            case 2:
                ImageView playerCard = view.findViewById(R.id.playerCard3);
                playerCard.setImageResource(pics.get(index));
                break;
            case 3:
                playerCard = view.findViewById(R.id.playerCard4);
                playerCard.setImageResource(pics.get(index));
                break;
            case 4:
                playerCard = view.findViewById(R.id.playerCard5);
                playerCard.setImageResource(pics.get(index));
                break;
            case 5:
                playerCard = view.findViewById(R.id.playerCard6);
                playerCard.setImageResource(pics.get(index));
                break;
            case 6:
                playerCard = view.findViewById(R.id.playerCard7);
                playerCard.setImageResource(pics.get(index));
                break;
            case 7:
                playerCard = view.findViewById(R.id.playerCard8);
                playerCard.setImageResource(pics.get(index));
                break;
            case 8:
                playerCard = view.findViewById(R.id.playerCard9);
                playerCard.setImageResource(pics.get(index));
                break;
            case 9:
                playerCard = view.findViewById(R.id.playerCard10);
                playerCard.setImageResource(pics.get(index));
                break;
            case 10:
                playerCard = view.findViewById(R.id.playerCard11);
                playerCard.setImageResource(pics.get(index));
                break;
            default:
                break;
        }
    }

    //updates the texts whenever something happens.
    void updateText(View view, ArrayList<Integer> playerValues, ArrayList<Integer> dealerValues) {
        TextView playerDisplay = view.findViewById(R.id.playerText);
        TextView dealerDisplay = view.findViewById(R.id.dealerText);

        playerDisplay.setText("Player Score: " + playerScore);
        dealerDisplay.setText("Dealer Score: " + dealerScore);

        if (playerScore == 21 && playerValues.size() == 2) {
            playerDisplay.setText("Blackjack!");
        }

        if (dealerScore == 21 && dealerValues.size() == 2) {
            dealerDisplay.setText("Blackjack!");
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}


