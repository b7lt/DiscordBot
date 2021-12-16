package org.bolt.discordbot.birthday;

import org.bolt.discordbot.Main;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BirthdayManager
{
    private JSONObject birthdays;
    public BirthdayManager() throws IOException {
        //add check to see if file exists, if it doesnt then create a new one
        //also create check to see if file contains {} like a json object at beginning and end, otherwise json will fail to parse
        birthdays = parseJSONFile("birthdays.json");
    }

    public JSONObject parseJSONFile(String filename) throws JSONException, IOException
    {
        String content = new String(Files.readAllBytes(Paths.get(filename)));
        return new JSONObject(content);
    }

    public void addBirthday(String userId, int month, int day) throws IOException {
        FileWriter file = new FileWriter("birthdays.json");

//        birthdays.put(userId, month + " " + day);

        JSONArray list = new JSONArray();
        list.put(month);
        list.put(day);
        birthdays.put(userId, list);

//        JSONArray list = new JSONArray();
//        list.put("msg 1");
//        list.put("msg 2");
//        list.put("msg 3");

//        obj.put("messages", list);

        file.write(birthdays.toString(4));
        file.close();
        System.out.println("Created birthday for " + Main.getBotInstance().getUserById(userId) + " on " + month + "/" + day);


//        System.out.println(obj.toString(4));
//        System.out.println(list.get(2));
//        System.out.println(obj.getInt("age"));
    }

    public void removeBirthday(String userId) throws IOException
    {
        FileWriter file = new FileWriter("birthdays.json");
        birthdays.remove(userId);
        file.write(birthdays.toString(4));
        file.close();
        System.out.println("Deleted birthday for " + userId);
    }

    public void updateBirthday(String userId, int month, int day) throws IOException
    {
        FileWriter file = new FileWriter("birthdays.json");
        birthdays.remove(userId);
        birthdays.put(userId, month + " " + day);
        file.write(birthdays.toString(4));
        file.close();
    }

    public int[] returnBirthday(String id)
    {
        int[] birthday = new int[2];
        birthday[0] = (int) birthdays.getJSONArray(id).get(0);
        birthday[1] = (int) birthdays.getJSONArray(id).get(1);
        return birthday;
    }

    public int numberOfBirthdays()
    {
        return birthdays.length();
    }

    public void createBirthdayJobs()
    {
        for(int i = 0; i < numberOfBirthdays(); i++)
        {

        }
    }

//    public static void addBirthday(String id, int month, int day)
//    {
//        try(FileWriter fw = new FileWriter("birthdays.json", true);
//            BufferedWriter bw = new BufferedWriter(fw);
//            PrintWriter out = new PrintWriter(bw))
//        {
//            out.println("the text");
//            //more code
//            bw.write("Test");
//            bw.newLine();
//            bw.close();
//            out.println("more text");
//            //more code
//        } catch (IOException e) {
//            //exception handling left as an exercise for the reader
//        }
//    }
}
