package com.kkp.demo.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomMoneyUtils {

    public static List<Integer> randomMoneyList(final int amount, final int personnel) {
        // 뿌릴 금액 분배
        Random random = new Random();
        List<Integer> randomNumList = new ArrayList<>();

        int randomNumTot = 0;
        int people = personnel;
        while(people > 0) {
            int randomNum = random.nextInt(10);
            randomNumList.add(randomNum);
            randomNumTot += randomNum;
            people--;
        }

        List<Integer> fallingMoneyList = new ArrayList<>();
        int seperated = amount / randomNumTot;
        int seperatedMoneyAmount = 0;

        for(int j=0; j<randomNumList.size(); j++) {
            int fallingMoney = seperated*randomNumList.get(j);
            fallingMoneyList.add(fallingMoney);
            seperatedMoneyAmount += fallingMoney;
        }

        int addedChanges = fallingMoneyList.get(0) + ( amount-seperatedMoneyAmount );
        fallingMoneyList.remove(0);
        fallingMoneyList.add(addedChanges);

        return fallingMoneyList;
    }
}
