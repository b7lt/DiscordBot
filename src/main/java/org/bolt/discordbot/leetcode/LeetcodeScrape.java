package org.bolt.discordbot.leetcode;

import java.net.URLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import org.json.*;

public class LeetcodeScrape {
    static final String PROBLEMS_ENDPOINT = "https://leetcode.com/api/problems/algorithms/";
    static final String PROBLEMS_BASEURL = "https://leetcode.com/problems/";

    public static JSONObject scrapeJSON() {
        String content = null;
        URLConnection connection = null;

        try {
            connection = new URL(PROBLEMS_ENDPOINT).openConnection();
            Scanner scan = new Scanner(connection.getInputStream());
            scan.useDelimiter("\\Z");
            content = scan.next();
            scan.close();

            JSONObject json = new JSONObject(content);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to fetch Leetcode data");
            return null;
        }
    }

    public static JSONObject leetcodeQuestion(int difficulty, boolean paid)
    {
        JSONObject rawLeetcode = scrapeJSON();
        if(rawLeetcode==null) return null;
        JSONArray problemsJSON = rawLeetcode.getJSONArray("stat_status_pairs");
        ArrayList<JSONObject> problems = new ArrayList<>();
        for (int i = 0; i < problemsJSON.length(); i++) {
            problems.add(problemsJSON.getJSONObject(i));
        }

        ArrayList<JSONObject> filtered = new ArrayList<>();
        for (int i = 0; i < problems.size(); i++)
        {
            JSONObject question = problems.get(i);
            if (question.getJSONObject("difficulty").getInt("level") == difficulty) {
                filtered.add(question);
            }
        }
        problems = filtered;

        if(!paid)
        {
            filtered = new ArrayList<>();
            for (int i = 0; i < problems.size(); i++) {
                JSONObject question = problems.get(i);
                if(!question.getBoolean("paid_only"))
                {
                    filtered.add(question);
                }
            }
            problems = filtered;
        }
        Random rand = new Random();
        int randomInt = rand.nextInt(problems.size());

        return problems.get(randomInt);
    }

}
