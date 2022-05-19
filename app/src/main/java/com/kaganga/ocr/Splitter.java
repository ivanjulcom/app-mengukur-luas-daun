package com.kaganga.ocr;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.log;
import static java.lang.Math.max;

public class Splitter {
    private Context context;
    private BufferedReader reader;
    private String input;
    private List<String> words;
    private Map<String, Double> wordcost;
    private List<Double> cost;
    private int maxword;

    public Splitter(String input, Context context) {
        this.input = input;
        this.context = context;
    }

    public String doLettersToWords(){

        words = new ArrayList<String>();
        try{
            final InputStream file = context.getAssets().open("words_list.txt");
            reader = new BufferedReader(new InputStreamReader(file));
            String line = reader.readLine();
            while(line != null){
                words.add(line);
                line = reader.readLine();
            }
        } catch(IOException ioe){
            ioe.printStackTrace();
        }

        wordcost = new HashMap<String, Double>();
        maxword = 0;
        for (int i = 0; i < words.size(); i++){
            wordcost.put(words.get(i), log((i+1)*log(words.size())));
            if(words.get(i).length() > maxword){
                maxword = words.get(i).length();
            }
        }

        cost = new ArrayList<Double>();
        cost.add(0.0);
        for (int i = 1; i <= input.length(); i++){
            Map.Entry<Double, Integer> ck = wordMatch(i);
            cost.add(ck.getKey());
            //Log.d("wordmatch","i"+i+" "+ck.getKey()+" length "+ck.getValue());
        }

        List<String> output = new ArrayList<String>();
        int i = input.length();
        while (i>0){
            Map.Entry<Double, Integer> ck = wordMatch(i);
            //Log.d("wordmatch", "cost "+ck.getKey()+" length "+ck.getValue());
            assert ck.getKey() == cost.get(i);
            output.add(input.substring(i-ck.getValue(), i));
            i -= ck.getValue();
        }

        Collections.reverse(output);
        String result = TextUtils.join(" ", output);
        Log.d("Result", result);
        return result;
    }

    private Map.Entry<Double, Integer> wordMatch(int i){

        List<Double> candidates = new ArrayList<Double>(cost.subList(max(0, i-maxword),i));
        Collections.reverse(candidates);
        int k = 1;
        Double c = Double.POSITIVE_INFINITY;
        for (int j = 0; j < candidates.size(); j++){
            Double cc = wordcost.get(input.substring(i-j-1, i));
            if (cc == null){
                cc = Double.POSITIVE_INFINITY;
            }
            Double ccc = candidates.get(j) + cc;
            if (c > ccc){
                c = ccc;
                k = j + 1;
            }
        }
        return new AbstractMap.SimpleEntry<Double, Integer>(c,k);
    }
}
