package com.example.maxi.unifundme;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Maxi on 3/21/2018.
 */

public class DatabaseManager extends SQLiteOpenHelper {

    public SQLiteDatabase db = getReadableDatabase();
    private static String DATABASE_NAME = "unifundme";
    private static int DATABASE_VERSION = 1;

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    Boolean valueChecker(String table, String column, String[] userValues){
        String sql ="SELECT * FROM " + table + " WHERE " + column + " =?";

        Cursor c = db.rawQuery(sql,userValues);
        try {
            if (c.getCount() > 0)
                return false;
            else {
                return true;
            }
        } finally {
            c.close();
        }
    }

    Boolean checkMatch(String table, String column1, String column2, String[] userValues){
        String sql ="SELECT * FROM " + table + " WHERE " + column1 + "=? and " + column2 + "=?";

        Cursor c = db.rawQuery(sql,userValues);
        try {
            if (c.getCount() > 0)
                return false;
            else {
                return true;
            }
        } finally {
            c.close();
        }
    }

    void CreateSavedAward(AwardList newSavedAward, String currentUser){
        ContentValues savedAwardCV = new ContentValues();
        savedAwardCV.put("award_id", newSavedAward.getId());
        savedAwardCV.put("user_name", currentUser);
        savedAwardCV.put("award_source", newSavedAward.getSource());
        savedAwardCV.put("award_type", newSavedAward.getType());
        savedAwardCV.put("award_name", newSavedAward.getName());
        savedAwardCV.put("award_amount", newSavedAward.getAmount());

        try {
            db.insert("savedAwards", null, savedAwardCV);
        } catch (Exception ex){
            //  error handle
        }
    }

    ArrayList<AwardList> getSavedAwards(String[] userInput){
        ArrayList<AwardList> awards = new ArrayList<>();

        String sql ="SELECT award_id, award_source, award_type, award_name, award_amount FROM savedAwards WHERE user_name=?";

        Cursor c = db.rawQuery(sql,userInput);
        while (c.moveToNext()) {
            int idInt = c.getColumnIndex("award_id");
            Integer idColumn = c.getInt(idInt);

            int sourceInt = c.getColumnIndex("award_source");
            String sourceColumn = c.getString(sourceInt);

            int typeInt = c.getColumnIndex("award_type");
            String typeColumn = c.getString(typeInt);

            int schoolInt = c.getColumnIndex("award_name");
            String schoolColumn = c.getString(schoolInt);

            int amountInt = c.getColumnIndex("award_amount");
            Double amountColumn = c.getDouble(amountInt);

            // add result to award list
            awards.add(new AwardList(idColumn, sourceColumn, typeColumn, schoolColumn, amountColumn));

        }
        c.close();
        return awards;
    }

    void DeleteSavedAward(String[] awardInfo){
        try {
            db.delete("savedAwards", "award_id=? and user_name=?", awardInfo);
        } catch (Exception ex){
            //  error handle
        }
    }

    void CreateUser(User newAcc){
        ContentValues userCV = new ContentValues();
        userCV.put("username", newAcc.getUsername());
        userCV.put("email", newAcc.getEmailAddress());
        userCV.put("password", newAcc.getPassword());

        try {
            db.insert("users", null, userCV);
        } catch (Exception ex){
            //  error handle
        }
    }

    ArrayList<AwardList> getDefaultData(String[] userInput){
        ArrayList<AwardList> awards = new ArrayList<>();

        String sql ="SELECT award_id, award_source, award_type, school_name, award_amount FROM awards AS A INNER JOIN schools AS S ON S.school_id = A.award_school_id WHERE award_province_id=? and award_school_id=? and award_studies=? and award_student_type=? and award_aboriginality=? and award_req_gpa<=?";

        Cursor c = db.rawQuery(sql,userInput);
        while (c.moveToNext()) {
            int idInt = c.getColumnIndex("award_id");
            Integer idColumn = c.getInt(idInt);

            int sourceInt = c.getColumnIndex("award_source");
            String sourceColumn = c.getString(sourceInt);

            int typeInt = c.getColumnIndex("award_type");
            String typeColumn = c.getString(typeInt);

            int schoolInt = c.getColumnIndex("school_name");
            String schoolColumn = c.getString(schoolInt);

            int amountInt = c.getColumnIndex("award_amount");
            Double amountColumn = c.getDouble(amountInt);

            // add result to award list
            awards.add(new AwardList(idColumn, sourceColumn, typeColumn, schoolColumn, amountColumn));

        }
        c.close();
        return awards;
    }

    User getUserInfo(String[] userInput){
        User accountInfo = null;

        String sql ="SELECT * FROM users WHERE username=?";

        Cursor c = db.rawQuery(sql,userInput);
        try {
            while (c.moveToNext()) {
                int idInt = c.getColumnIndex("user_id");
                Integer idColumn = c.getInt(idInt);

                int usernameInt = c.getColumnIndex("username");
                String usernameColumn = c.getString(usernameInt);

                int emailInt = c.getColumnIndex("email");
                String emailColumn = c.getString(emailInt);

                int passwordInt = c.getColumnIndex("password");
                String passwordColumn = c.getString(passwordInt);

                int profileSetInt = c.getColumnIndex("profileSet");
                Integer profileSetColumn = c.getInt(profileSetInt);

                int studyInt = c.getColumnIndex("study");
                String studyColumn = c.getString(studyInt);

                int localityInt = c.getColumnIndex("locality");
                String aboriginalityColumn = c.getString(localityInt);

                int aboriginalityInt = c.getColumnIndex("aboriginality");
                String provinceColumn = c.getString(aboriginalityInt);

                int provinceInt = c.getColumnIndex("province");
                String schoolColumn = c.getString(provinceInt);

                int schoolInt = c.getColumnIndex("school");
                String gpaSetColumn = c.getString(schoolInt);

                int gpaInt = c.getColumnIndex("gpa");
                Double gpaColumn = c.getDouble(gpaInt);

                // add result to User project
                accountInfo = new User(idColumn, usernameColumn, emailColumn, passwordColumn, profileSetColumn, studyColumn, aboriginalityColumn, provinceColumn, schoolColumn, gpaSetColumn, gpaColumn);
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
        c.close();
        return accountInfo;
    }

    void UpdateAccount(User info){
        ContentValues accountCV = new ContentValues();
        accountCV.put("password",info.getPassword());
        accountCV.put("profileSet", info.getProfileSet());
        accountCV.put("study", info.getStudy());
        accountCV.put("locality", info.getLocality());
        accountCV.put("aboriginality", info.getAboriginality());
        accountCV.put("province", info.getProvince());
        accountCV.put("school", info.getSchool());
        accountCV.put("gpa", info.getGpa());
        db.update("users", accountCV, "user_id=" + info.getId().toString(), null);
    }

    ArrayList<AwardList> getAnySchoolData(String[] userInput){
        ArrayList<AwardList> awards = new ArrayList<>();

        Cursor anySchool = db.rawQuery("SELECT award_id, award_source, award_type, school_name, award_amount FROM awards AS A INNER JOIN schools AS S ON S.school_id = A.award_school_id WHERE award_province_id=? and award_studies=? and award_student_type=? and award_aboriginality=? and award_req_gpa<=?", userInput);
        try {
            while (anySchool.moveToNext()) {
                int idInt = anySchool.getColumnIndex("award_id");
                Integer idColumn = anySchool.getInt(idInt);

                int sourceInt = anySchool.getColumnIndex("award_source");
                String sourceColumn = anySchool.getString(sourceInt);

                int typeInt = anySchool.getColumnIndex("award_type");
                String typeColumn = anySchool.getString(typeInt);

                int schoolInt = anySchool.getColumnIndex("school_name");
                String schoolColumn = anySchool.getString(schoolInt);

                int amountInt = anySchool.getColumnIndex("award_amount");
                Double amountColumn = anySchool.getDouble(amountInt);

                // add result to award list
                awards.add(new AwardList(idColumn, sourceColumn, typeColumn, schoolColumn, amountColumn));
            }
        } catch (Exception ex){
            ex.getMessage();
        }
        anySchool.close();
        return awards;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (\n" +
                "\t`user_id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`username`\tTEXT NOT NULL,\n" +
                "\t`email`\tTEXT NOT NULL,\n" +
                "\t`password`\tTEXT NOT NULL,\n" +
                "\t`profileSet`\tINTEGER NOT NULL DEFAULT 0,\n" +
                "\t`study`\tINTEGER,\n" +
                "\t`locality`\tINTEGER,\n" +
                "\t`aboriginality`\tINTEGER,\n" +
                "\t`province`\tINTEGER,\n" +
                "\t`school`\tINTEGER,\n" +
                "\t`gpa`\tNUMERIC\n" +
                ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS savedAwards (\n" +
                "\tsaved_id\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\taward_id\tINTEGER NOT NULL,\n" +
                "\tuser_name\tTEXT NOT NULL,\n" +
                "\taward_source\tTEXT NOT NULL,\n" +
                "\taward_type\tTEXT NOT NULL,\n" +
                "\taward_name\tTEXT NOT NULL,\n" +
                "\taward_amount\tNUMERIC NOT NULL);");

        db.execSQL("CREATE TABLE IF NOT EXISTS awards (\n" +
                "\taward_id\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\taward_school_id\tINTEGER NOT NULL,\n" +
                "\taward_province_id\tINTEGER NOT NULL,\n" +
                "\taward_source\tTEXT NOT NULL,\n" +
                "\taward_type\tTEXT NOT NULL,\n" +
                "\taward_name\tTEXT NOT NULL,\n" +
                "\taward_amount\tNUMERIC NOT NULL,\n" +
                "\taward_studies\tTEXT NOT NULL,\n" +
                "\taward_student_type\tBLOB NOT NULL,\n" +
                "\taward_aboriginality\tINTEGER NOT NULL,\n" +
                "\taward_req_gpa\tNUMERIC NOT NULL);");

        String count = "SELECT count(*) FROM awards";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);

        if(icount>0){
        }
        else {
            db.execSQL("INSERT INTO `awards` VALUES (1,20,2,'Private','Scholarship','Thoughtbridge',10066.94,'1','1',0,2.8);");
            db.execSQL("INSERT INTO `awards` VALUES (2,1,2,'School','Bursary','Fivespan',2157.59,'1','1',0,1.44);");
            db.execSQL("INSERT INTO `awards` VALUES (3,14,2,'Private','Scholarship','Myworks',13592.4,'0','1',0,2.88);");
            db.execSQL("INSERT INTO `awards` VALUES (4,13,2,'School','Scholarship','Jayo',11746.17,'0','0',0,1.12);");
            db.execSQL("INSERT INTO `awards` VALUES (5,17,2,'School','Scholarship','Twinte',11279.7,'0','1',0,2.7);");
            db.execSQL("INSERT INTO `awards` VALUES (6,3,2,'Private','Scholarship','Abatz',14443.76,'0','0',0,1.55);");
            db.execSQL("INSERT INTO `awards` VALUES (7,16,2,'School','Grant','Eadel',19788.15,'1','1',0,2.52);");
            db.execSQL("INSERT INTO `awards` VALUES (8,2,2,'School','Grant','Gevee',11698.25,'1','0',0,2.15);");
            db.execSQL("INSERT INTO `awards` VALUES (9,20,2,'School','Bursary','Viva',17795.59,'1','0',1,2.06);");
            db.execSQL("INSERT INTO `awards` VALUES (10,11,2,'School','Scholarship','Linktype',7368.62,'1','1',0,2.47);");
            db.execSQL("INSERT INTO `awards` VALUES (11,13,2,'School','Bursary','Zoombeat',20014.9,'0','1',0,2.02);");
            db.execSQL("INSERT INTO `awards` VALUES (12,14,2,'Private','Bursary','Brainlounge',16797.54,'0','0',0,2.9);");
            db.execSQL("INSERT INTO `awards` VALUES (13,7,2,'Private','Grant','Gabspot',18480.02,'0','1',1,2.07);");
            db.execSQL("INSERT INTO `awards` VALUES (14,5,2,'Government','Scholarship','Voonte',12333.63,'0','1',0,2.64);");
            db.execSQL("INSERT INTO `awards` VALUES (15,12,2,'Government','Scholarship','Flashset',20556.68,'0','1',1,1.54);");
            db.execSQL("INSERT INTO `awards` VALUES (16,25,2,'Private','Scholarship','Jatri',20512.23,'0','1',0,2.98);");
            db.execSQL("INSERT INTO `awards` VALUES (17,9,2,'School','Grant','Twinte',9302.83,'1','1',1,1.32);");
            db.execSQL("INSERT INTO `awards` VALUES (18,16,2,'School','Grant','Wordpedia',6032.04,'0','0',1,2.04);");
            db.execSQL("INSERT INTO `awards` VALUES (19,4,2,'Private','Scholarship','Thoughtsphere',14793.84,'1','1',0,2.91);");
            db.execSQL("INSERT INTO `awards` VALUES (20,6,2,'Private','Grant','Nlounge',6900.31,'1','0',1,2.85);");
            db.execSQL("INSERT INTO `awards` VALUES (21,12,2,'School','Grant','Babbleset',1187.84,'1','1',0,2.96);");
            db.execSQL("INSERT INTO `awards` VALUES (22,13,2,'Private','Bursary','Flipstorm',21362.39,'1','1',0,2.18);");
            db.execSQL("INSERT INTO `awards` VALUES (23,3,2,'Private','Scholarship','Zoombeat',10606.07,'0','1',0,1.76);");
            db.execSQL("INSERT INTO `awards` VALUES (24,19,2,'School','Bursary','Buzzdog',16819.82,'0','1',0,1.13);");
            db.execSQL("INSERT INTO `awards` VALUES (25,10,2,'School','Bursary','Jabbersphere',9236.86,'0','0',1,2.45);");
            db.execSQL("INSERT INTO `awards` VALUES (26,22,2,'Government','Bursary','Dynabox',19437.43,'1','1',1,1.46);");
            db.execSQL("INSERT INTO `awards` VALUES (27,14,2,'School','Bursary','Realpoint',9783.53,'1','0',1,1.71);");
            db.execSQL("INSERT INTO `awards` VALUES (28,2,2,'School','Bursary','Dynava',11654.25,'0','0',0,1.87);");
            db.execSQL("INSERT INTO `awards` VALUES (29,16,2,'Government','Grant','Yotz',18455.64,'1','1',0,1.49);");
            db.execSQL("INSERT INTO `awards` VALUES (30,12,2,'Private','Bursary','Oozz',14722.78,'0','1',0,2.62);");
            db.execSQL("INSERT INTO `awards` VALUES (31,13,2,'Private','Scholarship','Wordify',17317.28,'1','0',1,2.68);");
            db.execSQL("INSERT INTO `awards` VALUES (32,3,2,'Government','Bursary','Oodoo',22757.98,'0','0',0,2.78);");
            db.execSQL("INSERT INTO `awards` VALUES (33,2,2,'Government','Scholarship','Fliptune',14319.88,'1','0',1,2.21);");
            db.execSQL("INSERT INTO `awards` VALUES (34,19,2,'Private','Scholarship','Topiclounge',24288.36,'1','1',0,3.06);");
            db.execSQL("INSERT INTO `awards` VALUES (35,18,2,'School','Grant','Fivechat',2478.09,'0','1',1,1.16);");
            db.execSQL("INSERT INTO `awards` VALUES (36,15,2,'Government','Bursary','Fadeo',6336.76,'1','0',0,1.41);");
            db.execSQL("INSERT INTO `awards` VALUES (37,22,2,'School','Bursary','Kwimbee',1803,'0','1',0,1.71);");
            db.execSQL("INSERT INTO `awards` VALUES (38,5,2,'Private','Scholarship','Fanoodle',11371.52,'1','1',0,1.62);");
            db.execSQL("INSERT INTO `awards` VALUES (39,16,2,'Private','Grant','Cogibox',22195.09,'1','1',1,2.42);");
            db.execSQL("INSERT INTO `awards` VALUES (40,14,2,'Government','Grant','Thoughtblab',17556.67,'1','1',1,1.69);");
            db.execSQL("INSERT INTO `awards` VALUES (41,8,2,'Government','Scholarship','Topicshots',23271.7,'0','0',0,2.2);");
            db.execSQL("INSERT INTO `awards` VALUES (42,17,2,'Government','Grant','Talane',11424.94,'0','1',1,2.78);");
            db.execSQL("INSERT INTO `awards` VALUES (43,12,2,'Private','Bursary','Myworks',19548.92,'1','1',0,1.6);");
            db.execSQL("INSERT INTO `awards` VALUES (44,25,2,'Government','Bursary','Reallinks',18564.2,'0','1',0,1.4);");
            db.execSQL("INSERT INTO `awards` VALUES (45,1,2,'Private','Bursary','Avamm',14420,'0','0',1,1.8);");
            db.execSQL("INSERT INTO `awards` VALUES (46,23,2,'Private','Scholarship','Skiba',22882.9,'0','1',0,2.94);");
            db.execSQL("INSERT INTO `awards` VALUES (47,3,2,'School','Bursary','Dabshots',23660.14,'1','0',1,2.5);");
            db.execSQL("INSERT INTO `awards` VALUES (48,21,2,'Government','Grant','Thoughtstorm',13965.72,'0','1',0,2.53);");
            db.execSQL("INSERT INTO `awards` VALUES (49,13,2,'Government','Grant','Edgeify',23343.69,'1','0',1,1.19);");
            db.execSQL("INSERT INTO `awards` VALUES (50,10,2,'School','Scholarship','Meetz',23015.23,'0','1',1,3.04);");
            db.execSQL("INSERT INTO `awards` VALUES (51,4,2,'Private','Bursary','Tazz',23298.03,'0','1',1,1);");
            db.execSQL("INSERT INTO `awards` VALUES (52,2,2,'Private','Bursary','Podcat',5529.83,'0','1',0,1.46);");
            db.execSQL("INSERT INTO `awards` VALUES (53,5,2,'Private','Scholarship','Quamba',19055.17,'0','1',0,2.89);");
            db.execSQL("INSERT INTO `awards` VALUES (54,9,2,'Government','Scholarship','Fatz',1208.91,'1','0',0,2.3);");
            db.execSQL("INSERT INTO `awards` VALUES (55,22,2,'Government','Grant','Skidoo',13706.09,'1','1',1,2.92);");
            db.execSQL("INSERT INTO `awards` VALUES (56,17,2,'School','Scholarship','Jaxworks',15176.43,'0','1',1,2.8);");
            db.execSQL("INSERT INTO `awards` VALUES (57,9,2,'Government','Bursary','Topiclounge',10165.22,'1','0',1,2.8);");
            db.execSQL("INSERT INTO `awards` VALUES (58,24,2,'Government','Bursary','Gabspot',7711.8,'0','1',0,2.85);");
            db.execSQL("INSERT INTO `awards` VALUES (59,9,2,'Government','Scholarship','Jabbertype',2393.83,'0','0',1,2.43);");
            db.execSQL("INSERT INTO `awards` VALUES (60,8,2,'School','Bursary','Meemm',19419.44,'1','1',1,2.13);");
            db.execSQL("INSERT INTO `awards` VALUES (61,21,2,'Government','Grant','Eayo',14137.46,'1','1',0,1.2);");
            db.execSQL("INSERT INTO `awards` VALUES (62,5,2,'Private','Scholarship','Livepath',11593.67,'0','1',1,1.94);");
            db.execSQL("INSERT INTO `awards` VALUES (63,20,2,'School','Bursary','Omba',2210.98,'0','0',1,2.55);");
            db.execSQL("INSERT INTO `awards` VALUES (64,8,2,'Government','Bursary','Muxo',5721.59,'1','1',1,1.23);");
            db.execSQL("INSERT INTO `awards` VALUES (65,1,2,'School','Scholarship','Mydo',8001.18,'1','0',1,1.1);");
            db.execSQL("INSERT INTO `awards` VALUES (66,7,2,'Private','Scholarship','Edgeclub',18083.98,'1','0',0,1.51);");
            db.execSQL("INSERT INTO `awards` VALUES (67,16,2,'Government','Scholarship','Miboo',20205.27,'1','0',1,1.49);");
            db.execSQL("INSERT INTO `awards` VALUES (68,18,2,'Government','Scholarship','Babblestorm',22257.14,'1','1',1,1.54);");
            db.execSQL("INSERT INTO `awards` VALUES (69,2,2,'Private','Grant','Demivee',4005.35,'1','0',0,2.19);");
            db.execSQL("INSERT INTO `awards` VALUES (70,9,2,'Government','Grant','JumpXS',16613.4,'0','0',0,3.03);");
            db.execSQL("INSERT INTO `awards` VALUES (71,16,2,'Private','Scholarship','Edgepulse',8865.95,'1','0',0,1.66);");
            db.execSQL("INSERT INTO `awards` VALUES (72,3,2,'Government','Scholarship','Voolith',16067.03,'1','0',0,3);");
            db.execSQL("INSERT INTO `awards` VALUES (73,10,2,'School','Scholarship','Tekfly',23644.84,'0','1',0,1.18);");
            db.execSQL("INSERT INTO `awards` VALUES (74,24,2,'Government','Grant','Linklinks',20054.44,'1','0',0,1.97);");
            db.execSQL("INSERT INTO `awards` VALUES (75,11,2,'School','Bursary','Zoomcast',13859.52,'1','1',0,1.25);");
            db.execSQL("INSERT INTO `awards` VALUES (76,17,2,'Government','Scholarship','Quatz',19715.44,'1','0',0,2.1);");
            db.execSQL("INSERT INTO `awards` VALUES (77,1,2,'Government','Grant','Jetwire',5675.66,'0','0',1,2.58);");
            db.execSQL("INSERT INTO `awards` VALUES (78,13,2,'Private','Grant','Kwimbee',11874.24,'0','0',1,1.65);");
            db.execSQL("INSERT INTO `awards` VALUES (79,3,2,'Private','Grant','Kayveo',2280.84,'1','1',0,1.36);");
            db.execSQL("INSERT INTO `awards` VALUES (80,11,2,'Private','Scholarship','Fivechat',13220.21,'0','0',0,2.6);");
            db.execSQL("INSERT INTO `awards` VALUES (81,7,2,'School','Scholarship','Mynte',7587.64,'0','1',1,1.01);");
            db.execSQL("INSERT INTO `awards` VALUES (82,17,2,'Government','Bursary','Dazzlesphere',21914.97,'0','1',0,1.1);");
            db.execSQL("INSERT INTO `awards` VALUES (83,13,2,'Government','Grant','Teklist',1333.94,'0','0',1,2.74);");
            db.execSQL("INSERT INTO `awards` VALUES (84,12,2,'Private','Grant','Riffpath',21476.65,'1','1',1,1.23);");
            db.execSQL("INSERT INTO `awards` VALUES (85,3,2,'Private','Grant','Abatz',1127.48,'0','1',1,2.11);");
            db.execSQL("INSERT INTO `awards` VALUES (86,7,2,'Government','Bursary','Jaxnation',23210.67,'0','0',0,2.78);");
            db.execSQL("INSERT INTO `awards` VALUES (87,20,2,'School','Scholarship','Oodoo',24260.01,'1','0',1,1.98);");
            db.execSQL("INSERT INTO `awards` VALUES (88,10,2,'Private','Scholarship','Wordtune',8309.19,'1','1',0,1.59);");
            db.execSQL("INSERT INTO `awards` VALUES (89,1,2,'Government','Grant','Quatz',3740.9,'0','0',0,1.82);");
            db.execSQL("INSERT INTO `awards` VALUES (90,1,2,'Private','Bursary','Fanoodle',21032.85,'0','0',1,1.72);");
            db.execSQL("INSERT INTO `awards` VALUES (91,9,2,'Government','Bursary','Devshare',13031.56,'0','1',0,2.11);");
            db.execSQL("INSERT INTO `awards` VALUES (92,11,2,'School','Scholarship','Bubbletube',5572.93,'1','0',0,2.03);");
            db.execSQL("INSERT INTO `awards` VALUES (93,7,2,'School','Scholarship','Rooxo',1473.77,'0','1',0,2.75);");
            db.execSQL("INSERT INTO `awards` VALUES (94,17,2,'Private','Grant','Yoveo',24469.93,'0','0',0,2.87);");
            db.execSQL("INSERT INTO `awards` VALUES (95,22,2,'Private','Grant','Leexo',14917.2,'0','0',1,2.86);");
            db.execSQL("INSERT INTO `awards` VALUES (96,24,2,'School','Bursary','Avamba',9229.55,'0','0',0,3.02);");
            db.execSQL("INSERT INTO `awards` VALUES (97,20,2,'Private','Bursary','Janyx',10455.73,'1','0',1,1.16);");
            db.execSQL("INSERT INTO `awards` VALUES (98,17,2,'Government','Scholarship','Jaxworks',15755.15,'1','1',1,1.67);");
            db.execSQL("INSERT INTO `awards` VALUES (99,23,2,'School','Bursary','Vinder',22342.38,'1','0',0,2.5);");
            db.execSQL("INSERT INTO `awards` VALUES (100,18,2,'School','Bursary','Riffpath',23204.32,'0','0',0,1.54);");
            db.execSQL("INSERT INTO `awards` VALUES (101,8,2,'Government','Grant','Fatz',4472.6,'0','0',0,1.05);");
            db.execSQL("INSERT INTO `awards` VALUES (102,25,2,'Government','Grant','Divavu',1685.72,'1','1',0,2.65);");
            db.execSQL("INSERT INTO `awards` VALUES (103,9,2,'School','Grant','Thoughtsphere',12029.22,'0','1',1,2.64);");
            db.execSQL("INSERT INTO `awards` VALUES (104,22,2,'Private','Bursary','Thoughtstorm',22245.53,'1','0',0,2.51);");
            db.execSQL("INSERT INTO `awards` VALUES (105,4,2,'Private','Bursary','Jaxworks',18403.07,'0','1',0,1.48);");
            db.execSQL("INSERT INTO `awards` VALUES (106,4,2,'Private','Scholarship','Thoughtsphere',6620.59,'1','0',1,1.83);");
            db.execSQL("INSERT INTO `awards` VALUES (107,2,2,'School','Bursary','Roombo',18655.57,'1','0',1,2.3);");
            db.execSQL("INSERT INTO `awards` VALUES (108,13,2,'Government','Bursary','Dablist',5245.29,'1','0',1,1.68);");
            db.execSQL("INSERT INTO `awards` VALUES (109,18,2,'Private','Scholarship','Edgeify',4613.54,'0','0',1,1.18);");
            db.execSQL("INSERT INTO `awards` VALUES (110,14,2,'School','Scholarship','Voonyx',14414.55,'1','1',1,3.02);");
            db.execSQL("INSERT INTO `awards` VALUES (111,11,2,'Private','Bursary','Dynabox',1668.98,'1','1',1,2.58);");
            db.execSQL("INSERT INTO `awards` VALUES (112,11,2,'Government','Scholarship','Mydo',7044.77,'1','1',0,2.13);");
            db.execSQL("INSERT INTO `awards` VALUES (113,3,2,'School','Bursary','Topicware',14781.79,'0','0',1,1.98);");
            db.execSQL("INSERT INTO `awards` VALUES (114,16,2,'School','Scholarship','Feedspan',18867.19,'0','0',0,2.39);");
            db.execSQL("INSERT INTO `awards` VALUES (115,24,2,'Government','Scholarship','Fiveclub',12011.14,'0','0',1,1.97);");
            db.execSQL("INSERT INTO `awards` VALUES (116,20,2,'School','Bursary','Edgeclub',11937.49,'1','1',0,1.19);");
            db.execSQL("INSERT INTO `awards` VALUES (117,23,2,'Government','Grant','Flashspan',4693.56,'1','0',0,1.77);");
            db.execSQL("INSERT INTO `awards` VALUES (118,19,2,'Government','Scholarship','Leenti',21007.81,'1','1',0,2.87);");
            db.execSQL("INSERT INTO `awards` VALUES (119,22,2,'Government','Grant','Skinte',20992.52,'1','1',0,1.7);");
            db.execSQL("INSERT INTO `awards` VALUES (120,19,2,'School','Scholarship','Tazz',24272.92,'0','0',1,2.45);");
            db.execSQL("INSERT INTO `awards` VALUES (121,12,2,'School','Grant','Meemm',4461.84,'0','1',0,1.37);");
            db.execSQL("INSERT INTO `awards` VALUES (122,8,2,'Private','Grant','Oyonder',2096.41,'0','0',0,1.39);");
            db.execSQL("INSERT INTO `awards` VALUES (123,1,2,'School','Grant','Wikizz',11412.55,'1','0',0,1.37);");
            db.execSQL("INSERT INTO `awards` VALUES (124,24,2,'Government','Bursary','Feedmix',7648.35,'0','1',1,1.59);");
            db.execSQL("INSERT INTO `awards` VALUES (125,5,2,'School','Bursary','Thoughtblab',10373.06,'1','1',0,1.53);");
            db.execSQL("INSERT INTO `awards` VALUES (126,23,2,'Government','Bursary','Yabox',8328.8,'1','0',1,2.75);");
            db.execSQL("INSERT INTO `awards` VALUES (127,19,2,'Private','Bursary','Realcube',14544.67,'1','1',0,1.22);");
            db.execSQL("INSERT INTO `awards` VALUES (128,22,2,'Government','Grant','Yabox',2063.64,'1','1',0,1.14);");
            db.execSQL("INSERT INTO `awards` VALUES (129,1,2,'Private','Scholarship','Mymm',11534.97,'0','1',0,1.29);");
            db.execSQL("INSERT INTO `awards` VALUES (130,5,2,'Government','Scholarship','Trudeo',12868.78,'0','1',0,2.09);");
            db.execSQL("INSERT INTO `awards` VALUES (131,25,2,'Government','Bursary','Skinte',16213.69,'1','1',0,2.83);");
            db.execSQL("INSERT INTO `awards` VALUES (132,10,2,'Private','Bursary','Oozz',3373.06,'0','0',1,1.59);");
            db.execSQL("INSERT INTO `awards` VALUES (133,20,2,'Private','Grant','Wikivu',9373.82,'0','1',0,2.8);");
            db.execSQL("INSERT INTO `awards` VALUES (134,1,2,'Private','Grant','Thoughtbridge',7260.77,'1','0',1,2.4);");
            db.execSQL("INSERT INTO `awards` VALUES (135,1,2,'School','Scholarship','Feedspan',11384.16,'0','1',0,1.76);");
            db.execSQL("INSERT INTO `awards` VALUES (136,7,2,'Private','Grant','Vipe',16103.54,'1','1',0,1.45);");
            db.execSQL("INSERT INTO `awards` VALUES (137,21,2,'Government','Bursary','Lazzy',11009.57,'0','1',1,2.42);");
            db.execSQL("INSERT INTO `awards` VALUES (138,2,2,'School','Bursary','Demizz',3231.38,'0','1',1,1.7);");
            db.execSQL("INSERT INTO `awards` VALUES (139,8,2,'School','Bursary','Quaxo',5147.1,'1','0',0,1.02);");
            db.execSQL("INSERT INTO `awards` VALUES (140,3,2,'Private','Bursary','Brightbean',9694.61,'1','1',0,2.23);");
            db.execSQL("INSERT INTO `awards` VALUES (141,16,2,'Private','Grant','Thoughtblab',2244.12,'0','0',0,2.11);");
            db.execSQL("INSERT INTO `awards` VALUES (142,25,2,'Private','Grant','Photofeed',12938,'0','0',1,1.6);");
            db.execSQL("INSERT INTO `awards` VALUES (143,15,2,'Private','Scholarship','Youopia',24271.55,'1','0',1,1.93);");
            db.execSQL("INSERT INTO `awards` VALUES (144,8,2,'Government','Scholarship','Rhyloo',23149.61,'0','0',0,1.84);");
            db.execSQL("INSERT INTO `awards` VALUES (145,21,2,'Government','Bursary','Chatterbridge',24658.44,'0','1',0,1.66);");
            db.execSQL("INSERT INTO `awards` VALUES (146,13,2,'Private','Bursary','Dabtype',21179.13,'0','0',1,2.79);");
            db.execSQL("INSERT INTO `awards` VALUES (147,9,2,'School','Scholarship','Feednation',13429.46,'0','1',1,2.56);");
            db.execSQL("INSERT INTO `awards` VALUES (148,11,2,'Government','Bursary','Wikizz',8372.23,'0','0',1,2.23);");
            db.execSQL("INSERT INTO `awards` VALUES (149,21,2,'Government','Scholarship','Chatterbridge',3755.14,'1','1',1,1.45);");
            db.execSQL("INSERT INTO `awards` VALUES (150,9,2,'School','Scholarship','Meevee',23561.33,'0','0',0,1.74);");
            db.execSQL("INSERT INTO `awards` VALUES (151,19,2,'Government','Bursary','Lazzy',1004.35,'1','0',0,1.46);");
            db.execSQL("INSERT INTO `awards` VALUES (152,4,2,'School','Grant','Photolist',11582.54,'0','1',0,2.88);");
            db.execSQL("INSERT INTO `awards` VALUES (153,13,2,'Government','Grant','Eadel',24375.47,'0','0',0,1.84);");
            db.execSQL("INSERT INTO `awards` VALUES (154,21,2,'School','Grant','Oyope',24404.38,'0','0',1,2.85);");
            db.execSQL("INSERT INTO `awards` VALUES (155,9,2,'School','Grant','Jazzy',3650.69,'1','1',0,2.84);");
            db.execSQL("INSERT INTO `awards` VALUES (156,24,2,'School','Scholarship','Blognation',7139.51,'0','1',0,2.64);");
            db.execSQL("INSERT INTO `awards` VALUES (157,7,2,'School','Grant','Twimbo',3987.41,'1','1',1,1.81);");
            db.execSQL("INSERT INTO `awards` VALUES (158,3,2,'Government','Grant','Yamia',10722.89,'0','1',1,1.69);");
            db.execSQL("INSERT INTO `awards` VALUES (159,12,2,'School','Scholarship','Oloo',24412.79,'1','1',0,1.26);");
            db.execSQL("INSERT INTO `awards` VALUES (160,19,2,'Private','Bursary','Pixoboo',11876.05,'1','0',0,1.26);");
            db.execSQL("INSERT INTO `awards` VALUES (161,19,2,'School','Grant','Fivechat',1876.9,'0','1',1,2.24);");
            db.execSQL("INSERT INTO `awards` VALUES (162,5,2,'Private','Grant','Babbleblab',14231.08,'1','0',0,1.06);");
            db.execSQL("INSERT INTO `awards` VALUES (163,22,2,'Private','Grant','Meevee',9306.1,'0','0',0,2.35);");
            db.execSQL("INSERT INTO `awards` VALUES (164,23,2,'Government','Scholarship','Kazio',582.23,'1','1',0,2.79);");
            db.execSQL("INSERT INTO `awards` VALUES (165,1,2,'Private','Bursary','Meemm',6288.47,'0','1',1,1.76);");
            db.execSQL("INSERT INTO `awards` VALUES (166,2,2,'Government','Grant','Jazzy',14855.32,'1','1',0,2.68);");
            db.execSQL("INSERT INTO `awards` VALUES (167,12,2,'Private','Scholarship','Devcast',5018.07,'1','0',1,1.32);");
            db.execSQL("INSERT INTO `awards` VALUES (168,18,2,'Private','Bursary','Flashset',18905.64,'1','1',0,1.59);");
            db.execSQL("INSERT INTO `awards` VALUES (169,25,2,'School','Scholarship','Buzzster',7354.65,'1','0',0,2.29);");
            db.execSQL("INSERT INTO `awards` VALUES (170,19,2,'Private','Bursary','Thoughtblab',13215.44,'0','1',1,1.69);");
            db.execSQL("INSERT INTO `awards` VALUES (171,8,2,'Government','Scholarship','Quaxo',6417.62,'1','1',0,2.55);");
            db.execSQL("INSERT INTO `awards` VALUES (172,10,2,'School','Grant','Linkbuzz',1125.51,'1','1',1,2.36);");
            db.execSQL("INSERT INTO `awards` VALUES (173,9,2,'Private','Bursary','Wordpedia',12047.39,'0','0',0,2.62);");
            db.execSQL("INSERT INTO `awards` VALUES (174,16,2,'School','Scholarship','Abatz',24182.11,'0','1',1,2.32);");
            db.execSQL("INSERT INTO `awards` VALUES (175,24,2,'School','Bursary','Skynoodle',5320.3,'0','1',0,1);");
            db.execSQL("INSERT INTO `awards` VALUES (176,4,2,'Government','Bursary','JumpXS',2077.21,'1','1',1,2.56);");
            db.execSQL("INSERT INTO `awards` VALUES (177,9,2,'School','Scholarship','Twimm',24611.82,'1','1',1,2.54);");
            db.execSQL("INSERT INTO `awards` VALUES (178,15,2,'Government','Grant','Wordify',24882.82,'0','0',0,1.37);");
            db.execSQL("INSERT INTO `awards` VALUES (179,9,2,'Government','Grant','Topiczoom',10843.17,'1','1',1,1.73);");
            db.execSQL("INSERT INTO `awards` VALUES (180,15,2,'School','Grant','Rhybox',4027.91,'1','1',1,2.14);");
            db.execSQL("INSERT INTO `awards` VALUES (181,7,2,'Government','Grant','Zoomzone',17959.27,'1','0',0,1.22);");
            db.execSQL("INSERT INTO `awards` VALUES (182,21,2,'Government','Bursary','Jetpulse',1213.53,'1','0',1,2.27);");
            db.execSQL("INSERT INTO `awards` VALUES (183,24,2,'Government','Grant','Brainbox',21430.15,'0','1',1,1.65);");
            db.execSQL("INSERT INTO `awards` VALUES (184,18,2,'School','Scholarship','Bluezoom',21271.34,'1','1',0,2.8);");
            db.execSQL("INSERT INTO `awards` VALUES (185,17,2,'Government','Bursary','Vitz',6825.84,'0','1',0,2.16);");
            db.execSQL("INSERT INTO `awards` VALUES (186,6,2,'Private','Scholarship','Meemm',2037.92,'0','0',0,1.34);");
            db.execSQL("INSERT INTO `awards` VALUES (187,7,2,'Private','Bursary','Zooveo',21758.8,'1','1',1,1.51);");
            db.execSQL("INSERT INTO `awards` VALUES (188,16,2,'Government','Bursary','Topiclounge',12594.75,'1','1',1,2.57);");
            db.execSQL("INSERT INTO `awards` VALUES (189,21,2,'Private','Grant','Ainyx',5273.47,'0','0',0,1.39);");
            db.execSQL("INSERT INTO `awards` VALUES (190,9,2,'Government','Scholarship','Agivu',16981.68,'0','0',0,1.82);");
            db.execSQL("INSERT INTO `awards` VALUES (191,7,2,'School','Bursary','Youtags',5654.28,'1','0',0,1.36);");
            db.execSQL("INSERT INTO `awards` VALUES (192,21,2,'Private','Grant','Einti',2686,'0','0',1,2.68);");
            db.execSQL("INSERT INTO `awards` VALUES (193,16,2,'Private','Grant','Flipstorm',8409.44,'0','0',0,1.71);");
            db.execSQL("INSERT INTO `awards` VALUES (194,17,2,'School','Grant','Jabbertype',21528.59,'0','1',1,1.51);");
            db.execSQL("INSERT INTO `awards` VALUES (195,5,2,'School','Scholarship','Blogtag',20296.96,'0','1',1,2.45);");
            db.execSQL("INSERT INTO `awards` VALUES (196,16,2,'Private','Grant','Quinu',15339.69,'0','0',0,2.6);");
            db.execSQL("INSERT INTO `awards` VALUES (197,8,2,'Government','Scholarship','Bubbletube',9656.73,'0','1',1,2.83);");
            db.execSQL("INSERT INTO `awards` VALUES (198,2,2,'School','Grant','Nlounge',20960.25,'1','0',1,1.07);");
            db.execSQL("INSERT INTO `awards` VALUES (199,21,2,'Private','Grant','Npath',1204.34,'0','0',1,2.2);");
            db.execSQL("INSERT INTO `awards` VALUES (200,25,2,'Government','Scholarship','Jabbercube',5515.92,'1','1',0,2.76);");
            db.execSQL("INSERT INTO `awards` VALUES (201,20,2,'Private','Bursary','Zoomlounge',16005.03,'0','1',1,1.92);");
            db.execSQL("INSERT INTO `awards` VALUES (202,19,2,'Private','Grant','Quinu',1183.43,'1','1',0,1.93);");
            db.execSQL("INSERT INTO `awards` VALUES (203,4,2,'Government','Scholarship','Skinix',9869.96,'0','0',1,2.95);");
            db.execSQL("INSERT INTO `awards` VALUES (204,3,2,'Private','Scholarship','Oyoyo',13711.19,'1','0',0,1.89);");
            db.execSQL("INSERT INTO `awards` VALUES (205,7,2,'School','Scholarship','Brainsphere',16947.67,'1','0',1,1.28);");
            db.execSQL("INSERT INTO `awards` VALUES (206,7,2,'School','Bursary','Fivespan',8891.63,'0','1',0,1.62);");
            db.execSQL("INSERT INTO `awards` VALUES (207,16,2,'Government','Grant','Snaptags',13338.28,'0','1',0,2.27);");
            db.execSQL("INSERT INTO `awards` VALUES (208,24,2,'Government','Bursary','Cogidoo',4343.88,'0','1',1,1.83);");
            db.execSQL("INSERT INTO `awards` VALUES (209,11,2,'Private','Scholarship','Wikibox',19024.74,'1','1',0,1.29);");
            db.execSQL("INSERT INTO `awards` VALUES (210,23,2,'Government','Bursary','Zoozzy',17493.51,'0','0',1,1.12);");
            db.execSQL("INSERT INTO `awards` VALUES (211,2,2,'Government','Scholarship','Skinder',19561.63,'1','1',0,2.58);");
            db.execSQL("INSERT INTO `awards` VALUES (212,12,2,'School','Bursary','Fadeo',22530.09,'0','0',1,2.62);");
            db.execSQL("INSERT INTO `awards` VALUES (213,11,2,'School','Grant','Muxo',15006.91,'0','0',1,2.08);");
            db.execSQL("INSERT INTO `awards` VALUES (214,1,2,'Private','Grant','Aimbo',2263.01,'0','0',0,2.44);");
            db.execSQL("INSERT INTO `awards` VALUES (215,11,2,'Government','Bursary','Latz',13623.94,'1','1',1,2.75);");
            db.execSQL("INSERT INTO `awards` VALUES (216,13,2,'School','Bursary','Shufflester',5514.87,'0','1',1,2.28);");
            db.execSQL("INSERT INTO `awards` VALUES (217,14,2,'School','Bursary','Quire',736.74,'1','1',0,1.39);");
            db.execSQL("INSERT INTO `awards` VALUES (218,6,2,'Private','Scholarship','Yakijo',7363.16,'0','1',0,2.33);");
            db.execSQL("INSERT INTO `awards` VALUES (219,16,2,'School','Bursary','Brightdog',10121.12,'0','0',1,2.27);");
            db.execSQL("INSERT INTO `awards` VALUES (220,24,2,'Private','Grant','Browsedrive',2054.61,'0','0',0,1.59);");
            db.execSQL("INSERT INTO `awards` VALUES (221,25,2,'Government','Bursary','Oyondu',23418.6,'0','1',0,2);");
            db.execSQL("INSERT INTO `awards` VALUES (222,15,2,'School','Grant','Flashpoint',19051.43,'0','1',1,1.43);");
            db.execSQL("INSERT INTO `awards` VALUES (223,16,2,'School','Bursary','Trunyx',19287.23,'0','1',0,2.59);");
            db.execSQL("INSERT INTO `awards` VALUES (224,10,2,'School','Scholarship','Skippad',16349.84,'1','0',1,1.6);");
            db.execSQL("INSERT INTO `awards` VALUES (225,23,2,'Private','Grant','Bubblemix',23303,'1','0',0,1.42);");
            db.execSQL("INSERT INTO `awards` VALUES (226,25,2,'School','Scholarship','Miboo',20620.64,'1','1',1,1.02);");
            db.execSQL("INSERT INTO `awards` VALUES (227,19,2,'School','Bursary','Npath',10831.88,'1','0',0,2.6);");
            db.execSQL("INSERT INTO `awards` VALUES (228,21,2,'Private','Bursary','Camimbo',11620.76,'1','1',0,1.44);");
            db.execSQL("INSERT INTO `awards` VALUES (229,22,2,'Government','Grant','Gigaclub',18482.86,'1','0',1,2.77);");
            db.execSQL("INSERT INTO `awards` VALUES (230,21,2,'Government','Grant','Devbug',19754.36,'1','1',1,1.39);");
            db.execSQL("INSERT INTO `awards` VALUES (231,17,2,'Government','Grant','Zazio',11480.39,'0','1',1,1.39);");
            db.execSQL("INSERT INTO `awards` VALUES (232,8,2,'Private','Bursary','Meeveo',5137.73,'0','1',1,2.93);");
            db.execSQL("INSERT INTO `awards` VALUES (233,4,2,'Private','Grant','Miboo',16439.24,'1','0',0,1.14);");
            db.execSQL("INSERT INTO `awards` VALUES (234,25,2,'Private','Grant','Vinder',19090.86,'1','1',0,2.75);");
            db.execSQL("INSERT INTO `awards` VALUES (235,4,2,'Private','Grant','Devpoint',19553.94,'1','0',0,1.3);");
            db.execSQL("INSERT INTO `awards` VALUES (236,8,2,'Private','Scholarship','Wordpedia',10541.81,'0','1',1,3.05);");
            db.execSQL("INSERT INTO `awards` VALUES (237,4,2,'Government','Bursary','Photobug',9164.38,'0','1',0,1.18);");
            db.execSQL("INSERT INTO `awards` VALUES (238,9,2,'School','Grant','Zoomlounge',4722.54,'1','1',0,1.09);");
            db.execSQL("INSERT INTO `awards` VALUES (239,25,2,'Private','Grant','Tagcat',9835.95,'0','0',1,2.02);");
            db.execSQL("INSERT INTO `awards` VALUES (240,11,2,'School','Grant','Eazzy',20061.48,'1','1',1,1.13);");
            db.execSQL("INSERT INTO `awards` VALUES (241,13,2,'Government','Grant','Vitz',7760.51,'1','1',0,2.59);");
            db.execSQL("INSERT INTO `awards` VALUES (242,6,2,'School','Bursary','Dynazzy',17690.01,'1','0',0,1.64);");
            db.execSQL("INSERT INTO `awards` VALUES (243,2,2,'Government','Scholarship','Dabshots',22304.25,'1','1',0,1.85);");
            db.execSQL("INSERT INTO `awards` VALUES (244,20,2,'School','Bursary','Jazzy',17971.7,'0','0',1,2.2);");
            db.execSQL("INSERT INTO `awards` VALUES (245,12,2,'Government','Grant','Voomm',1010.48,'0','0',0,1.53);");
            db.execSQL("INSERT INTO `awards` VALUES (246,5,2,'School','Scholarship','Flashpoint',1937.5,'0','1',1,2.04);");
            db.execSQL("INSERT INTO `awards` VALUES (247,11,2,'School','Scholarship','Wikivu',7702.96,'1','0',0,1.29);");
            db.execSQL("INSERT INTO `awards` VALUES (248,14,2,'Government','Grant','Thoughtsphere',19857.31,'1','0',0,1.4);");
            db.execSQL("INSERT INTO `awards` VALUES (249,12,2,'Private','Scholarship','Jatri',8291.12,'0','1',0,2.96);");
            db.execSQL("INSERT INTO `awards` VALUES (250,17,2,'Private','Scholarship','Twitterbeat',2463.36,'1','0',0,2.67);");
            db.execSQL("INSERT INTO `awards` VALUES (251,20,2,'Private','Grant','Edgepulse',4715.63,'1','0',0,2.42);");
            db.execSQL("INSERT INTO `awards` VALUES (252,18,2,'Government','Scholarship','Cogibox',13233.81,'1','0',1,2.35);");
            db.execSQL("INSERT INTO `awards` VALUES (253,18,2,'Government','Bursary','Jabberbean',14895.43,'1','0',0,3.07);");
            db.execSQL("INSERT INTO `awards` VALUES (254,16,2,'School','Scholarship','Zooveo',2041.44,'1','0',0,2.59);");
            db.execSQL("INSERT INTO `awards` VALUES (255,17,2,'School','Grant','Kwilith',6922.69,'0','1',0,2.27);");
            db.execSQL("INSERT INTO `awards` VALUES (256,4,2,'Government','Scholarship','Trilith',11702.69,'0','0',1,2.08);");
            db.execSQL("INSERT INTO `awards` VALUES (257,8,2,'School','Scholarship','Realbridge',16195.98,'1','1',0,2.76);");
            db.execSQL("INSERT INTO `awards` VALUES (258,17,2,'Government','Bursary','Flashspan',19735.52,'1','1',1,1.07);");
            db.execSQL("INSERT INTO `awards` VALUES (259,18,2,'Government','Grant','Centimia',18549.51,'0','1',0,2.65);");
            db.execSQL("INSERT INTO `awards` VALUES (260,12,2,'Private','Bursary','Zava',11347.1,'0','1',0,2.25);");
            db.execSQL("INSERT INTO `awards` VALUES (261,10,2,'Government','Scholarship','Brightdog',14703.23,'1','0',1,1.13);");
            db.execSQL("INSERT INTO `awards` VALUES (262,23,2,'School','Scholarship','Realblab',22105.52,'0','0',0,2.71);");
            db.execSQL("INSERT INTO `awards` VALUES (263,14,2,'School','Scholarship','Dynazzy',24915.03,'0','1',0,1.32);");
            db.execSQL("INSERT INTO `awards` VALUES (264,10,2,'Private','Grant','Skyble',10827.52,'0','1',1,2.76);");
            db.execSQL("INSERT INTO `awards` VALUES (265,8,2,'Private','Bursary','Cogibox',8222.35,'1','1',0,2.53);");
            db.execSQL("INSERT INTO `awards` VALUES (266,16,2,'Government','Scholarship','Kwideo',14470.09,'1','1',1,2.05);");
            db.execSQL("INSERT INTO `awards` VALUES (267,25,2,'Private','Scholarship','Aivee',6361.73,'0','1',1,1.06);");
            db.execSQL("INSERT INTO `awards` VALUES (268,4,2,'School','Bursary','Brainsphere',1196.05,'1','1',1,1.21);");
            db.execSQL("INSERT INTO `awards` VALUES (269,17,2,'School','Scholarship','Quamba',3832.5,'0','1',0,3.05);");
            db.execSQL("INSERT INTO `awards` VALUES (270,2,2,'School','Grant','Plambee',13550.31,'0','0',1,2.12);");
            db.execSQL("INSERT INTO `awards` VALUES (271,12,2,'School','Scholarship','Ntags',4583.19,'1','1',1,2.76);");
            db.execSQL("INSERT INTO `awards` VALUES (272,10,2,'School','Grant','Wikizz',21899.71,'0','1',0,2.49);");
            db.execSQL("INSERT INTO `awards` VALUES (273,19,2,'School','Grant','Youfeed',9096.09,'0','0',1,2.73);");
            db.execSQL("INSERT INTO `awards` VALUES (274,8,2,'Government','Scholarship','Avavee',5122.86,'0','0',1,1.86);");
            db.execSQL("INSERT INTO `awards` VALUES (275,6,2,'School','Bursary','Zoombeat',9786.09,'1','0',1,1.6);");
            db.execSQL("INSERT INTO `awards` VALUES (276,11,2,'Government','Scholarship','Mybuzz',9069.21,'1','0',1,2.8);");
            db.execSQL("INSERT INTO `awards` VALUES (277,5,2,'School','Bursary','Devpulse',11418.99,'1','0',1,1.78);");
            db.execSQL("INSERT INTO `awards` VALUES (278,13,2,'Government','Grant','Feednation',15924.82,'0','0',0,2.45);");
            db.execSQL("INSERT INTO `awards` VALUES (279,23,2,'Private','Scholarship','Twinte',14008.8,'1','0',0,1.4);");
            db.execSQL("INSERT INTO `awards` VALUES (280,4,2,'Government','Grant','Quimm',4312.79,'0','1',0,1.14);");
            db.execSQL("INSERT INTO `awards` VALUES (281,7,2,'Government','Scholarship','Edgeblab',17253.71,'1','1',1,2.47);");
            db.execSQL("INSERT INTO `awards` VALUES (282,2,2,'Private','Scholarship','Twinte',21988.06,'1','1',0,1.35);");
            db.execSQL("INSERT INTO `awards` VALUES (283,9,2,'Government','Scholarship','Bubbletube',22004.97,'1','1',0,2.45);");
            db.execSQL("INSERT INTO `awards` VALUES (284,18,2,'Government','Grant','Divavu',18939.17,'0','1',0,2.34);");
            db.execSQL("INSERT INTO `awards` VALUES (285,24,2,'School','Bursary','Eadel',16210.59,'1','0',0,1.87);");
            db.execSQL("INSERT INTO `awards` VALUES (286,20,2,'Government','Scholarship','Tagcat',1925.99,'0','0',0,1.37);");
            db.execSQL("INSERT INTO `awards` VALUES (287,11,2,'Private','Grant','Viva',21996.36,'0','0',0,2.18);");
            db.execSQL("INSERT INTO `awards` VALUES (288,9,2,'School','Grant','Oyondu',9902.83,'1','0',1,3.04);");
            db.execSQL("INSERT INTO `awards` VALUES (289,9,2,'Government','Bursary','Flipopia',5273.98,'1','0',1,2.93);");
            db.execSQL("INSERT INTO `awards` VALUES (290,17,2,'Government','Scholarship','Cogilith',10723.57,'1','0',1,2.59);");
            db.execSQL("INSERT INTO `awards` VALUES (291,20,2,'School','Bursary','Realmix',1643.02,'0','1',0,2.02);");
            db.execSQL("INSERT INTO `awards` VALUES (292,2,2,'Government','Scholarship','Quatz',20029.14,'0','0',1,1.3);");
            db.execSQL("INSERT INTO `awards` VALUES (293,11,2,'Private','Scholarship','Buzzshare',8471.83,'1','0',1,1.06);");
            db.execSQL("INSERT INTO `awards` VALUES (294,1,2,'Government','Scholarship','Photobug',22085.54,'1','1',0,2.84);");
            db.execSQL("INSERT INTO `awards` VALUES (295,10,2,'Government','Bursary','Topiczoom',18062.26,'0','1',1,1.07);");
            db.execSQL("INSERT INTO `awards` VALUES (296,12,2,'Private','Bursary','Divanoodle',15133.19,'1','1',0,2.62);");
            db.execSQL("INSERT INTO `awards` VALUES (297,17,2,'Government','Bursary','Feedfish',3230.05,'1','0',1,2.9);");
            db.execSQL("INSERT INTO `awards` VALUES (298,21,2,'Private','Scholarship','Eare',18463.41,'0','0',0,2.84);");
            db.execSQL("INSERT INTO `awards` VALUES (299,5,2,'Private','Bursary','Riffpath',21331.79,'1','1',1,2.09);");
            db.execSQL("INSERT INTO `awards` VALUES (300,17,2,'Private','Bursary','Bubblebox',9907.39,'1','0',0,2.32);");
            db.execSQL("INSERT INTO `awards` VALUES (301,6,2,'School','Bursary','Skimia',2608.03,'1','0',0,2.02);");
            db.execSQL("INSERT INTO `awards` VALUES (302,1,2,'Government','Grant','Skidoo',8473,'0','1',0,2.87);");
            db.execSQL("INSERT INTO `awards` VALUES (303,15,2,'Private','Grant','Skibox',16138.78,'1','1',1,2.95);");
            db.execSQL("INSERT INTO `awards` VALUES (304,9,2,'Private','Scholarship','Feedfish',16201.07,'1','1',1,2.79);");
            db.execSQL("INSERT INTO `awards` VALUES (305,25,2,'Government','Scholarship','Yacero',4887,'1','1',1,2.59);");
            db.execSQL("INSERT INTO `awards` VALUES (306,13,2,'Government','Grant','Dabfeed',13854.12,'0','0',1,2.43);");
            db.execSQL("INSERT INTO `awards` VALUES (307,14,2,'Private','Bursary','Feedspan',21507.37,'1','0',0,1.4);");
            db.execSQL("INSERT INTO `awards` VALUES (308,2,2,'Private','Scholarship','Zoozzy',15364.96,'0','0',0,2.78);");
            db.execSQL("INSERT INTO `awards` VALUES (309,10,2,'School','Bursary','Skilith',4499.03,'1','1',1,1.89);");
            db.execSQL("INSERT INTO `awards` VALUES (310,5,2,'Government','Bursary','Miboo',18770.61,'0','0',0,1.14);");
            db.execSQL("INSERT INTO `awards` VALUES (311,22,2,'School','Grant','Camido',3970.73,'1','0',1,2.24);");
            db.execSQL("INSERT INTO `awards` VALUES (312,25,2,'Private','Grant','Yozio',6839.31,'1','0',1,2.88);");
            db.execSQL("INSERT INTO `awards` VALUES (313,15,2,'Government','Grant','Podcat',15637.5,'1','0',0,1.09);");
            db.execSQL("INSERT INTO `awards` VALUES (314,5,2,'School','Bursary','Dynava',5735.31,'0','0',0,1.36);");
            db.execSQL("INSERT INTO `awards` VALUES (315,10,2,'School','Bursary','Aivee',17635.7,'0','1',1,2.17);");
            db.execSQL("INSERT INTO `awards` VALUES (316,3,2,'School','Grant','Gabtype',13378.57,'1','0',0,1.84);");
            db.execSQL("INSERT INTO `awards` VALUES (317,6,2,'Private','Bursary','JumpXS',24832.32,'1','1',1,2.82);");
            db.execSQL("INSERT INTO `awards` VALUES (318,10,2,'Government','Bursary','Jaloo',2004.7,'0','1',1,2.24);");
            db.execSQL("INSERT INTO `awards` VALUES (319,13,2,'Private','Scholarship','Twimbo',21502.12,'1','0',0,1.7);");
            db.execSQL("INSERT INTO `awards` VALUES (320,17,2,'Government','Scholarship','Topdrive',20050.38,'0','1',1,1.4);");
            db.execSQL("INSERT INTO `awards` VALUES (321,23,2,'Government','Bursary','Meembee',19247.35,'1','0',1,1.68);");
            db.execSQL("INSERT INTO `awards` VALUES (322,12,2,'Private','Grant','Roodel',5184.15,'1','0',0,2.72);");
            db.execSQL("INSERT INTO `awards` VALUES (323,16,2,'Private','Bursary','Feedfire',5851.65,'1','0',0,2.22);");
            db.execSQL("INSERT INTO `awards` VALUES (324,2,2,'Government','Bursary','Devpoint',6534.57,'0','1',1,1.51);");
            db.execSQL("INSERT INTO `awards` VALUES (325,22,2,'Private','Bursary','Gigabox',16132.47,'0','1',0,1.74);");
            db.execSQL("INSERT INTO `awards` VALUES (326,23,2,'Private','Scholarship','Digitube',5359.29,'0','1',1,1.52);");
            db.execSQL("INSERT INTO `awards` VALUES (327,20,2,'Private','Bursary','Buzzdog',16536.95,'1','1',0,1.45);");
            db.execSQL("INSERT INTO `awards` VALUES (328,18,2,'Private','Bursary','Yambee',21689.15,'0','1',0,1.85);");
            db.execSQL("INSERT INTO `awards` VALUES (329,21,2,'Government','Scholarship','Abata',1986.58,'1','1',0,2.43);");
            db.execSQL("INSERT INTO `awards` VALUES (330,2,2,'Government','Bursary','Twitterbridge',20368.6,'0','1',0,2.54);");
            db.execSQL("INSERT INTO `awards` VALUES (331,6,2,'School','Grant','Voonyx',7932.06,'0','1',0,2.89);");
            db.execSQL("INSERT INTO `awards` VALUES (332,19,2,'Government','Bursary','Zazio',3511.85,'1','1',1,1.59);");
            db.execSQL("INSERT INTO `awards` VALUES (333,8,2,'School','Bursary','Demimbu',3760.09,'1','1',1,2.58);");
            db.execSQL("INSERT INTO `awards` VALUES (334,21,2,'School','Scholarship','Divanoodle',19468.87,'1','0',1,1.8);");
            db.execSQL("INSERT INTO `awards` VALUES (335,8,2,'School','Bursary','Layo',20015.28,'1','0',0,1.1);");
            db.execSQL("INSERT INTO `awards` VALUES (336,16,2,'School','Grant','Yacero',6737.15,'0','1',1,1.48);");
            db.execSQL("INSERT INTO `awards` VALUES (337,19,2,'Private','Bursary','Tekfly',6015.64,'0','0',1,2.01);");
            db.execSQL("INSERT INTO `awards` VALUES (338,24,2,'School','Bursary','Voonder',11468.64,'0','1',1,1.41);");
            db.execSQL("INSERT INTO `awards` VALUES (339,14,2,'Private','Grant','Trupe',23182.73,'0','1',1,1.53);");
            db.execSQL("INSERT INTO `awards` VALUES (340,13,2,'Private','Grant','LiveZ',23494.71,'0','1',0,2.6);");
            db.execSQL("INSERT INTO `awards` VALUES (341,12,2,'Private','Grant','Ntags',10853.3,'1','1',1,1.08);");
            db.execSQL("INSERT INTO `awards` VALUES (342,25,2,'School','Bursary','Skimia',6956.35,'0','1',0,1.55);");
            db.execSQL("INSERT INTO `awards` VALUES (343,18,2,'Private','Grant','Youopia',13153.83,'1','0',0,1.08);");
            db.execSQL("INSERT INTO `awards` VALUES (344,15,2,'School','Scholarship','Skyba',5266.24,'0','0',1,2.33);");
            db.execSQL("INSERT INTO `awards` VALUES (345,17,2,'Private','Grant','Babbleset',15599.88,'1','0',0,2.39);");
            db.execSQL("INSERT INTO `awards` VALUES (346,12,2,'School','Scholarship','Livefish',8508.8,'0','1',0,1.34);");
            db.execSQL("INSERT INTO `awards` VALUES (347,17,2,'Government','Scholarship','Skyndu',19325.82,'1','1',1,2.12);");
            db.execSQL("INSERT INTO `awards` VALUES (348,14,2,'School','Scholarship','Devify',8389.5,'0','1',1,1.02);");
            db.execSQL("INSERT INTO `awards` VALUES (349,16,2,'School','Grant','Katz',3713.13,'1','1',1,1.05);");
            db.execSQL("INSERT INTO `awards` VALUES (350,20,2,'School','Scholarship','JumpXS',3595.61,'1','1',0,3.03);");
            db.execSQL("INSERT INTO `awards` VALUES (351,21,2,'Private','Bursary','Mycat',14377.06,'0','1',1,1.63);");
            db.execSQL("INSERT INTO `awards` VALUES (352,17,2,'School','Grant','Meedoo',6796.19,'1','1',1,2.46);");
            db.execSQL("INSERT INTO `awards` VALUES (353,20,2,'Private','Scholarship','Realbridge',14792.53,'0','1',0,1.4);");
            db.execSQL("INSERT INTO `awards` VALUES (354,8,2,'School','Scholarship','Voomm',636.55,'1','0',0,2.71);");
            db.execSQL("INSERT INTO `awards` VALUES (355,23,2,'Private','Grant','Jaxspan',20549.08,'0','0',0,2.39);");
            db.execSQL("INSERT INTO `awards` VALUES (356,22,2,'Private','Scholarship','Minyx',23177.16,'1','0',0,1.82);");
            db.execSQL("INSERT INTO `awards` VALUES (357,24,2,'Private','Bursary','Linkbuzz',14832.79,'1','0',1,1.79);");
            db.execSQL("INSERT INTO `awards` VALUES (358,10,2,'Government','Bursary','BlogXS',16411.01,'1','0',1,2.02);");
            db.execSQL("INSERT INTO `awards` VALUES (359,16,2,'School','Grant','Fliptune',17445.18,'1','1',0,1.24);");
            db.execSQL("INSERT INTO `awards` VALUES (360,21,2,'School','Bursary','Kaymbo',23001.4,'1','0',1,1.9);");
            db.execSQL("INSERT INTO `awards` VALUES (361,11,2,'School','Grant','Ntag',9971.04,'1','0',0,1.26);");
            db.execSQL("INSERT INTO `awards` VALUES (362,4,2,'Private','Scholarship','Mybuzz',8714.93,'0','0',1,2.29);");
            db.execSQL("INSERT INTO `awards` VALUES (363,1,2,'Private','Scholarship','Topiczoom',6438.57,'1','0',1,1.29);");
            db.execSQL("INSERT INTO `awards` VALUES (364,21,2,'Government','Scholarship','Zoomdog',5685.07,'1','1',1,2.7);");
            db.execSQL("INSERT INTO `awards` VALUES (365,7,2,'Government','Bursary','Kwimbee',9617.69,'1','1',1,3.09);");
            db.execSQL("INSERT INTO `awards` VALUES (366,5,2,'Private','Scholarship','Browseblab',13406.25,'0','0',1,2.17);");
            db.execSQL("INSERT INTO `awards` VALUES (367,1,2,'School','Grant','Jaxspan',20062.27,'0','1',1,2.73);");
            db.execSQL("INSERT INTO `awards` VALUES (368,5,2,'Private','Bursary','Oyoloo',19488.05,'0','0',1,2.75);");
            db.execSQL("INSERT INTO `awards` VALUES (369,24,2,'Government','Grant','Podcat',19694.35,'0','0',0,3.03);");
            db.execSQL("INSERT INTO `awards` VALUES (370,7,2,'School','Bursary','Babblestorm',21887.75,'0','1',0,1.13);");
            db.execSQL("INSERT INTO `awards` VALUES (371,14,2,'Government','Bursary','Vinte',12297.32,'0','0',1,2.8);");
            db.execSQL("INSERT INTO `awards` VALUES (372,12,2,'Government','Grant','Kazu',13231.24,'1','1',0,2.07);");
            db.execSQL("INSERT INTO `awards` VALUES (373,4,2,'School','Grant','Livefish',24831.16,'1','1',0,2.52);");
            db.execSQL("INSERT INTO `awards` VALUES (374,4,2,'School','Grant','Edgeblab',4752.34,'1','0',0,3.03);");
            db.execSQL("INSERT INTO `awards` VALUES (375,1,2,'School','Scholarship','Devcast',23726.97,'1','0',1,2.43);");
            db.execSQL("INSERT INTO `awards` VALUES (376,5,2,'Government','Scholarship','Oyondu',16086.1,'1','0',0,1.06);");
            db.execSQL("INSERT INTO `awards` VALUES (377,22,2,'Private','Grant','Meevee',15495.57,'0','0',1,2.07);");
            db.execSQL("INSERT INTO `awards` VALUES (378,17,2,'Private','Scholarship','Dabvine',3562.72,'1','1',1,1.49);");
            db.execSQL("INSERT INTO `awards` VALUES (379,6,2,'School','Scholarship','Jamia',16932.63,'0','0',1,2.44);");
            db.execSQL("INSERT INTO `awards` VALUES (380,17,2,'School','Scholarship','Zoovu',19497.42,'0','1',1,2.38);");
            db.execSQL("INSERT INTO `awards` VALUES (381,17,2,'Government','Bursary','DabZ',13291.5,'1','0',1,2.36);");
            db.execSQL("INSERT INTO `awards` VALUES (382,7,2,'School','Bursary','Skynoodle',3583.09,'0','0',1,1.55);");
            db.execSQL("INSERT INTO `awards` VALUES (383,22,2,'School','Scholarship','Gabtype',6348.57,'0','1',1,1.53);");
            db.execSQL("INSERT INTO `awards` VALUES (384,15,2,'Private','Bursary','Kwilith',20623.32,'0','1',1,1.15);");
            db.execSQL("INSERT INTO `awards` VALUES (385,7,2,'Government','Grant','Dablist',14526.32,'0','0',1,2.18);");
            db.execSQL("INSERT INTO `awards` VALUES (386,1,2,'Government','Grant','Yakidoo',20908.31,'0','0',1,2.47);");
            db.execSQL("INSERT INTO `awards` VALUES (387,5,2,'School','Bursary','Youspan',14949.67,'0','1',0,1.3);");
            db.execSQL("INSERT INTO `awards` VALUES (388,19,2,'School','Scholarship','Blogtag',15665.14,'1','1',0,2.58);");
            db.execSQL("INSERT INTO `awards` VALUES (389,3,2,'School','Bursary','Quaxo',8311.52,'0','1',0,1.93);");
            db.execSQL("INSERT INTO `awards` VALUES (390,10,2,'Private','Bursary','Wordify',13893,'0','1',1,2.91);");
            db.execSQL("INSERT INTO `awards` VALUES (391,19,2,'Private','Scholarship','Skyble',13740.62,'1','1',1,1.63);");
            db.execSQL("INSERT INTO `awards` VALUES (392,4,2,'Government','Grant','Gigabox',19977.38,'0','0',0,1.44);");
            db.execSQL("INSERT INTO `awards` VALUES (393,23,2,'Private','Bursary','Demizz',21720.81,'0','1',0,2.03);");
            db.execSQL("INSERT INTO `awards` VALUES (394,5,2,'Government','Grant','Livetube',10088.52,'0','0',0,1.56);");
            db.execSQL("INSERT INTO `awards` VALUES (395,6,2,'Private','Bursary','Feedmix',3791.53,'0','0',0,2.28);");
            db.execSQL("INSERT INTO `awards` VALUES (396,15,2,'School','Grant','Twinder',23357,'1','0',0,1.56);");
            db.execSQL("INSERT INTO `awards` VALUES (397,18,2,'School','Grant','Fanoodle',1263.73,'1','0',0,1.14);");
            db.execSQL("INSERT INTO `awards` VALUES (398,3,2,'Private','Scholarship','Twiyo',17285.97,'1','1',1,2.95);");
            db.execSQL("INSERT INTO `awards` VALUES (399,16,2,'School','Grant','Reallinks',3666.4,'1','1',0,2.48);");
            db.execSQL("INSERT INTO `awards` VALUES (400,3,2,'Private','Bursary','Photofeed',22722.04,'0','1',1,2.64);");
            db.execSQL("INSERT INTO `awards` VALUES (401,4,2,'Private','Grant','Zoozzy',1613.34,'1','0',1,2.5);");
            db.execSQL("INSERT INTO `awards` VALUES (402,23,2,'School','Scholarship','Pixope',7963.82,'1','0',1,1.36);");
            db.execSQL("INSERT INTO `awards` VALUES (403,5,2,'Private','Scholarship','Layo',15713.6,'0','1',1,1.44);");
            db.execSQL("INSERT INTO `awards` VALUES (404,20,2,'Government','Bursary','Twitterwire',24760.49,'0','1',0,1.15);");
            db.execSQL("INSERT INTO `awards` VALUES (405,14,2,'Private','Scholarship','Livetube',11404.36,'0','1',1,2.39);");
            db.execSQL("INSERT INTO `awards` VALUES (406,4,2,'Private','Bursary','Oyoloo',18048.39,'1','0',0,1.34);");
            db.execSQL("INSERT INTO `awards` VALUES (407,9,2,'School','Grant','Voonder',22743.04,'1','0',1,1.18);");
            db.execSQL("INSERT INTO `awards` VALUES (408,21,2,'Government','Grant','Dynabox',1820.54,'1','1',0,1.24);");
            db.execSQL("INSERT INTO `awards` VALUES (409,16,2,'School','Grant','Gabcube',10916.79,'1','0',1,2.13);");
            db.execSQL("INSERT INTO `awards` VALUES (410,10,2,'School','Grant','Edgepulse',12237.43,'1','1',0,1.68);");
            db.execSQL("INSERT INTO `awards` VALUES (411,5,2,'Government','Bursary','Brainverse',4092.97,'1','0',1,2.62);");
            db.execSQL("INSERT INTO `awards` VALUES (412,9,2,'Government','Scholarship','Oyondu',17135.25,'1','0',0,1.63);");
            db.execSQL("INSERT INTO `awards` VALUES (413,24,2,'School','Bursary','Avaveo',10338.32,'0','0',1,1.4);");
            db.execSQL("INSERT INTO `awards` VALUES (414,6,2,'Government','Bursary','Agivu',22474.17,'0','1',0,1.12);");
            db.execSQL("INSERT INTO `awards` VALUES (415,17,2,'Government','Bursary','Innojam',17170.37,'0','1',1,1.55);");
            db.execSQL("INSERT INTO `awards` VALUES (416,25,2,'Private','Grant','Dynabox',18666.46,'0','0',1,1.05);");
            db.execSQL("INSERT INTO `awards` VALUES (417,17,2,'Private','Bursary','Katz',19385.36,'1','1',0,3.09);");
            db.execSQL("INSERT INTO `awards` VALUES (418,6,2,'Government','Grant','Voonte',7748.35,'0','1',0,1.12);");
            db.execSQL("INSERT INTO `awards` VALUES (419,1,2,'School','Scholarship','Flashpoint',20853.57,'1','0',0,1.64);");
            db.execSQL("INSERT INTO `awards` VALUES (420,8,2,'Private','Bursary','Kwimbee',8847.38,'0','1',1,2.54);");
            db.execSQL("INSERT INTO `awards` VALUES (421,4,2,'Private','Bursary','Tagchat',6317.22,'0','0',1,2.77);");
            db.execSQL("INSERT INTO `awards` VALUES (422,15,2,'Private','Bursary','Devpoint',17355.61,'1','1',1,2.2);");
            db.execSQL("INSERT INTO `awards` VALUES (423,18,2,'School','Scholarship','Photofeed',11719.03,'1','1',1,1.56);");
            db.execSQL("INSERT INTO `awards` VALUES (424,9,2,'School','Bursary','Realcube',15797.02,'0','0',1,2.15);");
            db.execSQL("INSERT INTO `awards` VALUES (425,19,2,'Government','Scholarship','Roombo',16619.98,'1','0',0,1.76);");
            db.execSQL("INSERT INTO `awards` VALUES (426,4,2,'School','Scholarship','Yambee',7986.75,'0','0',0,3.1);");
            db.execSQL("INSERT INTO `awards` VALUES (427,13,2,'School','Bursary','Jayo',20487.97,'0','1',0,2.02);");
            db.execSQL("INSERT INTO `awards` VALUES (428,2,2,'Government','Grant','Edgeclub',7372.34,'1','0',0,2.12);");
            db.execSQL("INSERT INTO `awards` VALUES (429,24,2,'Government','Scholarship','Bluejam',796.78,'0','0',1,2.92);");
            db.execSQL("INSERT INTO `awards` VALUES (430,24,2,'School','Bursary','Jetwire',2971.87,'0','1',0,3.07);");
            db.execSQL("INSERT INTO `awards` VALUES (431,7,2,'Government','Scholarship','Zava',1945.62,'1','0',0,3.07);");
            db.execSQL("INSERT INTO `awards` VALUES (432,9,2,'Government','Scholarship','Realbridge',3991.17,'1','1',0,1.32);");
            db.execSQL("INSERT INTO `awards` VALUES (433,4,2,'School','Bursary','Podcat',17652.89,'1','1',1,2.3);");
            db.execSQL("INSERT INTO `awards` VALUES (434,14,2,'School','Grant','Cogidoo',3689.69,'1','1',1,1.45);");
            db.execSQL("INSERT INTO `awards` VALUES (435,22,2,'Private','Grant','Realcube',3172.17,'1','0',0,2.03);");
            db.execSQL("INSERT INTO `awards` VALUES (436,4,2,'Private','Grant','Twitterlist',23658.49,'1','0',0,1.93);");
            db.execSQL("INSERT INTO `awards` VALUES (437,9,2,'School','Scholarship','Topicshots',11666.78,'0','0',1,2.92);");
            db.execSQL("INSERT INTO `awards` VALUES (438,16,2,'School','Grant','Viva',23631.1,'0','0',1,1.9);");
            db.execSQL("INSERT INTO `awards` VALUES (439,23,2,'School','Grant','Topiczoom',11212.2,'1','0',1,1.25);");
            db.execSQL("INSERT INTO `awards` VALUES (440,11,2,'Government','Bursary','Devshare',5097.52,'1','1',0,2.96);");
            db.execSQL("INSERT INTO `awards` VALUES (441,8,2,'Government','Bursary','Quinu',18047.79,'0','1',1,2.63);");
            db.execSQL("INSERT INTO `awards` VALUES (442,1,2,'Government','Bursary','Dabshots',5768.49,'0','0',1,2.5);");
            db.execSQL("INSERT INTO `awards` VALUES (443,19,2,'School','Scholarship','Thoughtbeat',22809.32,'0','1',1,1.11);");
            db.execSQL("INSERT INTO `awards` VALUES (444,17,2,'School','Scholarship','Oyope',13454.56,'0','1',1,2.25);");
            db.execSQL("INSERT INTO `awards` VALUES (445,13,2,'Private','Bursary','Trunyx',2473.79,'1','0',0,2.91);");
            db.execSQL("INSERT INTO `awards` VALUES (446,7,2,'Government','Scholarship','Pixonyx',22199.64,'0','1',0,2.98);");
            db.execSQL("INSERT INTO `awards` VALUES (447,21,2,'Government','Bursary','Babbleblab',23030.05,'0','0',1,1.79);");
            db.execSQL("INSERT INTO `awards` VALUES (448,6,2,'School','Grant','Oozz',6793.59,'1','0',1,1.45);");
            db.execSQL("INSERT INTO `awards` VALUES (449,2,2,'Private','Bursary','Pixonyx',5635.61,'1','1',1,2.77);");
            db.execSQL("INSERT INTO `awards` VALUES (450,24,2,'School','Scholarship','Lazzy',17435.06,'0','0',0,1.24);");
            db.execSQL("INSERT INTO `awards` VALUES (451,20,2,'School','Grant','Meevee',3945.86,'1','0',0,1.16);");
            db.execSQL("INSERT INTO `awards` VALUES (452,8,2,'School','Grant','Roodel',1715.11,'0','0',1,2.44);");
            db.execSQL("INSERT INTO `awards` VALUES (453,1,2,'Private','Scholarship','Snaptags',21849.97,'0','1',1,1.72);");
            db.execSQL("INSERT INTO `awards` VALUES (454,22,2,'School','Scholarship','Yacero',1096.7,'0','1',0,1.56);");
            db.execSQL("INSERT INTO `awards` VALUES (455,6,2,'Private','Grant','Linktype',20483.97,'0','1',1,1.75);");
            db.execSQL("INSERT INTO `awards` VALUES (456,9,2,'Government','Bursary','Flashpoint',19772.93,'1','1',1,1.68);");
            db.execSQL("INSERT INTO `awards` VALUES (457,2,2,'School','Grant','Innotype',953.32,'1','1',0,2.19);");
            db.execSQL("INSERT INTO `awards` VALUES (458,1,2,'Private','Scholarship','Devpoint',6954.02,'0','0',0,2.19);");
            db.execSQL("INSERT INTO `awards` VALUES (459,4,2,'Private','Grant','Layo',6938.61,'1','0',1,1.96);");
            db.execSQL("INSERT INTO `awards` VALUES (460,8,2,'Government','Grant','Kaymbo',7191.42,'1','0',0,2.88);");
            db.execSQL("INSERT INTO `awards` VALUES (461,20,2,'Government','Grant','Zazio',12661.95,'0','0',0,2.69);");
            db.execSQL("INSERT INTO `awards` VALUES (462,12,2,'School','Scholarship','Divavu',4082.08,'0','1',0,2.52);");
            db.execSQL("INSERT INTO `awards` VALUES (463,15,2,'Private','Scholarship','Chatterpoint',15083.99,'1','0',0,3);");
            db.execSQL("INSERT INTO `awards` VALUES (464,7,2,'Government','Grant','Jabbersphere',11831.05,'0','0',1,1.95);");
            db.execSQL("INSERT INTO `awards` VALUES (465,23,2,'School','Scholarship','Jayo',12728.67,'1','1',1,1.87);");
            db.execSQL("INSERT INTO `awards` VALUES (466,13,2,'School','Grant','Lazz',6964.61,'1','0',1,2.59);");
            db.execSQL("INSERT INTO `awards` VALUES (467,7,2,'School','Bursary','Fivebridge',2078.75,'1','1',1,2.12);");
            db.execSQL("INSERT INTO `awards` VALUES (468,2,2,'School','Grant','Wordware',11430.24,'1','0',1,1.68);");
            db.execSQL("INSERT INTO `awards` VALUES (469,16,2,'Private','Bursary','Voomm',22504.11,'0','0',1,1.64);");
            db.execSQL("INSERT INTO `awards` VALUES (470,5,2,'Government','Scholarship','Skyvu',21718.65,'1','1',0,1.31);");
            db.execSQL("INSERT INTO `awards` VALUES (471,16,2,'Private','Bursary','Realblab',15473.53,'1','1',1,2.2);");
            db.execSQL("INSERT INTO `awards` VALUES (472,8,2,'Private','Bursary','Jabberbean',9781.62,'1','0',1,2.72);");
            db.execSQL("INSERT INTO `awards` VALUES (473,20,2,'Private','Bursary','Riffpedia',11828.91,'0','0',0,2.46);");
            db.execSQL("INSERT INTO `awards` VALUES (474,25,2,'Government','Bursary','Wikibox',1616.13,'0','0',1,2.29);");
            db.execSQL("INSERT INTO `awards` VALUES (475,6,2,'Government','Bursary','Thoughtbeat',5583.16,'1','0',0,2.9);");
            db.execSQL("INSERT INTO `awards` VALUES (476,10,2,'School','Grant','Fivespan',4203.86,'0','1',1,1.76);");
            db.execSQL("INSERT INTO `awards` VALUES (477,1,2,'Government','Scholarship','Trudoo',18783.98,'0','1',0,1.22);");
            db.execSQL("INSERT INTO `awards` VALUES (478,20,2,'Private','Grant','Jabberstorm',6998.21,'0','1',0,2.27);");
            db.execSQL("INSERT INTO `awards` VALUES (479,24,2,'Private','Grant','Kazu',24591.75,'1','0',1,1.6);");
            db.execSQL("INSERT INTO `awards` VALUES (480,9,2,'Government','Bursary','Jayo',22393.63,'0','0',1,2.22);");
            db.execSQL("INSERT INTO `awards` VALUES (481,6,2,'School','Scholarship','Skyble',21442.83,'1','0',0,2.4);");
            db.execSQL("INSERT INTO `awards` VALUES (482,8,2,'Private','Bursary','Meeveo',19049.07,'0','0',0,2.22);");
            db.execSQL("INSERT INTO `awards` VALUES (483,21,2,'Private','Grant','Wikivu',22381.08,'0','0',1,1.6);");
            db.execSQL("INSERT INTO `awards` VALUES (484,1,2,'Government','Bursary','Podcat',20211.3,'1','1',0,1.71);");
            db.execSQL("INSERT INTO `awards` VALUES (485,1,2,'School','Scholarship','Topicstorm',13504.87,'0','0',1,2.45);");
            db.execSQL("INSERT INTO `awards` VALUES (486,3,2,'Private','Grant','Zava',2495.46,'0','1',1,1.5);");
            db.execSQL("INSERT INTO `awards` VALUES (487,21,2,'Private','Scholarship','Ooba',21706.24,'1','1',0,1.23);");
            db.execSQL("INSERT INTO `awards` VALUES (488,17,2,'Private','Scholarship','Nlounge',2007.45,'1','1',1,1.08);");
            db.execSQL("INSERT INTO `awards` VALUES (489,9,2,'School','Bursary','Skimia',21906.67,'1','0',0,1.42);");
            db.execSQL("INSERT INTO `awards` VALUES (490,14,2,'Government','Grant','Katz',16862,'1','1',0,1.97);");
            db.execSQL("INSERT INTO `awards` VALUES (491,10,2,'School','Scholarship','Realblab',21030.36,'1','0',0,2.86);");
            db.execSQL("INSERT INTO `awards` VALUES (492,10,2,'Private','Scholarship','Fatz',6103.62,'0','0',1,2.3);");
            db.execSQL("INSERT INTO `awards` VALUES (493,15,2,'Private','Bursary','Edgeblab',11005.26,'0','1',1,2.31);");
            db.execSQL("INSERT INTO `awards` VALUES (494,8,2,'School','Grant','Flashdog',21177.94,'1','1',0,2.76);");
            db.execSQL("INSERT INTO `awards` VALUES (495,8,2,'Private','Scholarship','Ooba',7231.22,'1','1',0,2.13);");
            db.execSQL("INSERT INTO `awards` VALUES (496,24,2,'School','Scholarship','Trupe',18626.92,'0','1',0,1.76);");
            db.execSQL("INSERT INTO `awards` VALUES (497,24,2,'Private','Bursary','Yabox',24269.73,'1','0',0,1.26);");
            db.execSQL("INSERT INTO `awards` VALUES (498,1,2,'Government','Grant','Edgetag',12775.01,'1','1',1,2.4);");
            db.execSQL("INSERT INTO `awards` VALUES (499,14,2,'Private','Scholarship','Babblestorm',841.12,'1','1',0,1.48);");
            db.execSQL("INSERT INTO `awards` VALUES (500,7,2,'School','Grant','Aimbu',16268.42,'0','1',0,2.06);");
            db.execSQL("INSERT INTO `awards` VALUES (501,19,2,'Government','Grant','Browsedrive',12669.81,'1','0',1,1.46);");
            db.execSQL("INSERT INTO `awards` VALUES (502,16,2,'Private','Bursary','Voonyx',20936.1,'1','0',1,2.73);");
            db.execSQL("INSERT INTO `awards` VALUES (503,17,2,'School','Bursary','Divanoodle',20832.93,'1','1',0,1.57);");
            db.execSQL("INSERT INTO `awards` VALUES (504,11,2,'School','Bursary','Centimia',16228.57,'0','0',0,2.58);");
            db.execSQL("INSERT INTO `awards` VALUES (505,15,2,'School','Bursary','Bubbletube',24431.34,'0','0',0,1.77);");
            db.execSQL("INSERT INTO `awards` VALUES (506,25,2,'Private','Grant','Kazu',14806.82,'0','1',0,1.5);");
            db.execSQL("INSERT INTO `awards` VALUES (507,10,2,'Government','Scholarship','Layo',18596.21,'0','1',1,1.91);");
            db.execSQL("INSERT INTO `awards` VALUES (508,19,2,'Government','Grant','Tagpad',9546.24,'0','0',0,1.39);");
            db.execSQL("INSERT INTO `awards` VALUES (509,16,2,'Private','Bursary','Dynabox',10921.68,'0','1',0,1.15);");
            db.execSQL("INSERT INTO `awards` VALUES (510,14,2,'Private','Grant','Jayo',21239.41,'0','1',0,1.27);");
            db.execSQL("INSERT INTO `awards` VALUES (511,5,2,'Private','Scholarship','Oyoba',24677.13,'1','0',0,1.44);");
            db.execSQL("INSERT INTO `awards` VALUES (512,17,2,'Government','Bursary','Voonte',17527.54,'0','1',1,3.09);");
            db.execSQL("INSERT INTO `awards` VALUES (513,1,2,'School','Scholarship','Trunyx',8446.3,'0','1',0,2.98);");
            db.execSQL("INSERT INTO `awards` VALUES (514,13,2,'Government','Bursary','Quimba',13256.08,'1','0',0,2.57);");
            db.execSQL("INSERT INTO `awards` VALUES (515,21,2,'School','Bursary','Devshare',16508.77,'1','0',0,1.52);");
            db.execSQL("INSERT INTO `awards` VALUES (516,21,2,'Government','Scholarship','Feedspan',24227.66,'0','1',0,3);");
            db.execSQL("INSERT INTO `awards` VALUES (517,6,2,'School','Bursary','Jaxbean',13178.59,'0','0',0,1.45);");
            db.execSQL("INSERT INTO `awards` VALUES (518,9,2,'School','Grant','Flashset',6328.59,'1','1',0,1.59);");
            db.execSQL("INSERT INTO `awards` VALUES (519,16,2,'Private','Scholarship','Youopia',18572.74,'1','0',1,1.77);");
            db.execSQL("INSERT INTO `awards` VALUES (520,23,2,'School','Grant','Photobean',23421.87,'1','0',1,1.93);");
            db.execSQL("INSERT INTO `awards` VALUES (521,5,2,'Private','Bursary','Youspan',18086.56,'0','1',0,1.64);");
            db.execSQL("INSERT INTO `awards` VALUES (522,11,2,'School','Scholarship','Kazio',11886.08,'1','0',1,1.11);");
            db.execSQL("INSERT INTO `awards` VALUES (523,11,2,'Government','Bursary','Feedfire',14027.27,'1','0',1,2.74);");
            db.execSQL("INSERT INTO `awards` VALUES (524,23,2,'School','Bursary','Zoombox',19252.23,'1','0',1,1.83);");
            db.execSQL("INSERT INTO `awards` VALUES (525,10,2,'Private','Scholarship','Browsedrive',20151.39,'1','0',0,2.2);");
            db.execSQL("INSERT INTO `awards` VALUES (526,14,2,'Government','Scholarship','Twitterwire',2944.68,'0','1',1,2.74);");
            db.execSQL("INSERT INTO `awards` VALUES (527,15,2,'Government','Grant','Vinder',23738.5,'0','0',0,2.73);");
            db.execSQL("INSERT INTO `awards` VALUES (528,1,2,'School','Grant','Yodel',6241.74,'1','0',0,2.7);");
            db.execSQL("INSERT INTO `awards` VALUES (529,21,2,'Private','Scholarship','Feedfire',23696.33,'0','0',1,1.52);");
            db.execSQL("INSERT INTO `awards` VALUES (530,15,2,'Private','Bursary','Twitterlist',15792.48,'1','0',1,1.51);");
            db.execSQL("INSERT INTO `awards` VALUES (531,11,2,'School','Bursary','Mybuzz',24510.95,'1','0',1,1.07);");
            db.execSQL("INSERT INTO `awards` VALUES (532,3,2,'Private','Bursary','Skajo',7736.09,'1','1',0,1.86);");
            db.execSQL("INSERT INTO `awards` VALUES (533,7,2,'Private','Grant','Gigabox',5984.58,'1','0',0,3.05);");
            db.execSQL("INSERT INTO `awards` VALUES (534,3,2,'Government','Bursary','Thoughtworks',17847.7,'0','0',1,1.03);");
            db.execSQL("INSERT INTO `awards` VALUES (535,2,2,'Private','Grant','Linklinks',6310.32,'0','0',0,2.13);");
            db.execSQL("INSERT INTO `awards` VALUES (536,20,2,'Private','Scholarship','Twiyo',1071.65,'0','0',1,1.36);");
            db.execSQL("INSERT INTO `awards` VALUES (537,3,2,'Private','Bursary','Topiczoom',1230.28,'1','1',0,2.95);");
            db.execSQL("INSERT INTO `awards` VALUES (538,24,2,'School','Scholarship','Blogtag',1723,'1','0',1,1.33);");
            db.execSQL("INSERT INTO `awards` VALUES (539,18,2,'Government','Scholarship','Jaloo',17584.18,'1','0',1,2.72);");
            db.execSQL("INSERT INTO `awards` VALUES (540,3,2,'Government','Bursary','Demivee',9993.09,'0','1',0,1.49);");
            db.execSQL("INSERT INTO `awards` VALUES (541,2,2,'School','Bursary','Oyondu',22510.42,'1','0',1,2.27);");
            db.execSQL("INSERT INTO `awards` VALUES (542,18,2,'Private','Scholarship','Jamia',23020.23,'0','1',1,2.45);");
            db.execSQL("INSERT INTO `awards` VALUES (543,8,2,'School','Scholarship','Zoovu',9691.83,'0','1',0,1.09);");
            db.execSQL("INSERT INTO `awards` VALUES (544,20,2,'Private','Bursary','Gigashots',2894.31,'0','1',0,2.7);");
            db.execSQL("INSERT INTO `awards` VALUES (545,1,2,'Government','Bursary','Yoveo',1680.14,'1','1',1,2.4);");
            db.execSQL("INSERT INTO `awards` VALUES (546,20,2,'School','Grant','Youfeed',24991.55,'1','1',1,1.48);");
            db.execSQL("INSERT INTO `awards` VALUES (547,15,2,'Private','Bursary','Wikizz',4125.8,'0','0',1,1.14);");
            db.execSQL("INSERT INTO `awards` VALUES (548,9,2,'Private','Grant','Buzzshare',7594.76,'1','1',1,1.85);");
            db.execSQL("INSERT INTO `awards` VALUES (549,14,2,'School','Grant','Demivee',3544.88,'1','1',0,2.24);");
            db.execSQL("INSERT INTO `awards` VALUES (550,15,2,'Private','Bursary','BlogXS',13819.57,'1','0',1,2.97);");
            db.execSQL("INSERT INTO `awards` VALUES (551,19,2,'Government','Bursary','Brightdog',20728.81,'0','1',1,2.09);");
            db.execSQL("INSERT INTO `awards` VALUES (552,13,2,'School','Scholarship','Blogtags',4106.7,'0','1',1,2.15);");
            db.execSQL("INSERT INTO `awards` VALUES (553,18,2,'Government','Bursary','Voonte',22694.69,'1','0',0,1.39);");
            db.execSQL("INSERT INTO `awards` VALUES (554,11,2,'Private','Grant','Yodo',20981.03,'1','0',1,2.11);");
            db.execSQL("INSERT INTO `awards` VALUES (555,10,2,'School','Grant','Riffpath',23623.55,'0','0',0,2.37);");
            db.execSQL("INSERT INTO `awards` VALUES (556,4,2,'Government','Scholarship','Jabberstorm',5544.21,'0','0',0,1.19);");
            db.execSQL("INSERT INTO `awards` VALUES (557,9,2,'Government','Bursary','Npath',10500.35,'0','1',1,2.24);");
            db.execSQL("INSERT INTO `awards` VALUES (558,22,2,'Government','Grant','Teklist',8597.32,'1','0',0,2.31);");
            db.execSQL("INSERT INTO `awards` VALUES (559,24,2,'School','Bursary','Trudoo',19453.5,'1','1',0,2.59);");
            db.execSQL("INSERT INTO `awards` VALUES (560,13,2,'Private','Bursary','Dabjam',7342.09,'1','1',1,2.16);");
            db.execSQL("INSERT INTO `awards` VALUES (561,9,2,'School','Bursary','Lazzy',18336.59,'0','1',1,2.87);");
            db.execSQL("INSERT INTO `awards` VALUES (562,21,2,'Private','Grant','Rhynoodle',6917.89,'0','0',1,1.37);");
            db.execSQL("INSERT INTO `awards` VALUES (563,5,2,'School','Bursary','Voonyx',8170.66,'1','0',0,1.68);");
            db.execSQL("INSERT INTO `awards` VALUES (564,18,2,'Private','Scholarship','Demivee',24930.18,'0','1',0,1.61);");
            db.execSQL("INSERT INTO `awards` VALUES (565,10,2,'Government','Bursary','Fadeo',12407.66,'0','0',1,2.9);");
            db.execSQL("INSERT INTO `awards` VALUES (566,20,2,'Government','Bursary','Aimbo',13559.25,'1','1',0,1.3);");
            db.execSQL("INSERT INTO `awards` VALUES (567,7,2,'Government','Bursary','Voomm',20927.28,'1','0',1,2.37);");
            db.execSQL("INSERT INTO `awards` VALUES (568,11,2,'School','Grant','Vidoo',22749.24,'1','0',1,1.62);");
            db.execSQL("INSERT INTO `awards` VALUES (569,21,2,'Government','Scholarship','Ailane',11072.21,'0','1',0,2.89);");
            db.execSQL("INSERT INTO `awards` VALUES (570,16,2,'Government','Scholarship','Tambee',19380.35,'0','1',1,1.86);");
            db.execSQL("INSERT INTO `awards` VALUES (571,8,2,'School','Scholarship','Eamia',9017.27,'1','0',0,1.15);");
            db.execSQL("INSERT INTO `awards` VALUES (572,13,2,'School','Scholarship','Quimm',3226.95,'1','1',0,1.39);");
            db.execSQL("INSERT INTO `awards` VALUES (573,22,2,'Government','Bursary','Latz',7787.58,'1','1',0,1.12);");
            db.execSQL("INSERT INTO `awards` VALUES (574,3,2,'Government','Bursary','Buzzster',7272.92,'1','1',0,2.65);");
            db.execSQL("INSERT INTO `awards` VALUES (575,4,2,'Government','Bursary','Youbridge',13321.1,'1','0',1,3);");
            db.execSQL("INSERT INTO `awards` VALUES (576,2,2,'Private','Grant','Skipfire',20925.15,'1','1',1,2.6);");
            db.execSQL("INSERT INTO `awards` VALUES (577,2,2,'Government','Bursary','Fanoodle',2441.6,'0','0',1,1.67);");
            db.execSQL("INSERT INTO `awards` VALUES (578,24,2,'Government','Grant','Dabshots',718.42,'1','0',0,2.34);");
            db.execSQL("INSERT INTO `awards` VALUES (579,25,2,'School','Bursary','Topicware',14992.68,'1','0',1,3);");
            db.execSQL("INSERT INTO `awards` VALUES (580,4,2,'Government','Grant','Babblestorm',17638.09,'1','0',0,1.19);");
            db.execSQL("INSERT INTO `awards` VALUES (581,25,2,'School','Scholarship','Blognation',15831.64,'1','0',1,2.61);");
            db.execSQL("INSERT INTO `awards` VALUES (582,14,2,'Private','Scholarship','Feednation',9809.66,'0','0',1,2.38);");
            db.execSQL("INSERT INTO `awards` VALUES (583,11,2,'School','Bursary','Yadel',16287.14,'1','1',0,1.08);");
            db.execSQL("INSERT INTO `awards` VALUES (584,15,2,'School','Grant','Quinu',14007,'0','1',0,2.34);");
            db.execSQL("INSERT INTO `awards` VALUES (585,22,2,'School','Grant','Gevee',6760.73,'0','0',1,1.47);");
            db.execSQL("INSERT INTO `awards` VALUES (586,2,2,'Government','Grant','Dabshots',19156.04,'0','0',0,1.97);");
            db.execSQL("INSERT INTO `awards` VALUES (587,10,2,'Private','Bursary','Dynabox',8515.85,'1','0',1,1.95);");
            db.execSQL("INSERT INTO `awards` VALUES (588,21,2,'School','Bursary','Yacero',5390.23,'1','0',1,1.42);");
            db.execSQL("INSERT INTO `awards` VALUES (589,21,2,'School','Grant','Quinu',12313.71,'0','1',0,2.7);");
            db.execSQL("INSERT INTO `awards` VALUES (590,6,2,'School','Grant','Thoughtblab',5449.06,'1','0',0,1.31);");
            db.execSQL("INSERT INTO `awards` VALUES (591,24,2,'Government','Bursary','Tagfeed',4684,'1','1',0,1.59);");
            db.execSQL("INSERT INTO `awards` VALUES (592,22,2,'School','Scholarship','Photolist',1808.18,'1','1',0,1.11);");
            db.execSQL("INSERT INTO `awards` VALUES (593,15,2,'Government','Bursary','Jaxbean',10300.15,'0','1',0,1.43);");
            db.execSQL("INSERT INTO `awards` VALUES (594,18,2,'School','Grant','Jabbercube',14244.89,'1','1',0,2.05);");
            db.execSQL("INSERT INTO `awards` VALUES (595,3,2,'Private','Scholarship','Wikibox',19813.8,'1','1',0,2.37);");
            db.execSQL("INSERT INTO `awards` VALUES (596,18,2,'Private','Scholarship','Buzzbean',17317.7,'1','0',0,3.02);");
            db.execSQL("INSERT INTO `awards` VALUES (597,12,2,'Government','Grant','Photojam',18476.56,'1','0',0,1.85);");
            db.execSQL("INSERT INTO `awards` VALUES (598,22,2,'Private','Scholarship','Plajo',23956.04,'1','0',0,1.28);");
            db.execSQL("INSERT INTO `awards` VALUES (599,19,2,'School','Scholarship','Innotype',1330.63,'0','0',0,2.13);");
            db.execSQL("INSERT INTO `awards` VALUES (600,15,2,'School','Scholarship','Twimm',19221.98,'0','0',1,2.69);");
            db.execSQL("INSERT INTO `awards` VALUES (601,8,2,'Government','Bursary','Flashset',18790.6,'0','0',0,2);");
            db.execSQL("INSERT INTO `awards` VALUES (602,22,2,'School','Scholarship','Twitterwire',7726.9,'0','1',0,2.8);");
            db.execSQL("INSERT INTO `awards` VALUES (603,2,2,'Government','Bursary','Talane',21703.68,'1','1',1,1.55);");
            db.execSQL("INSERT INTO `awards` VALUES (604,20,2,'Private','Bursary','Yodel',3975.54,'1','0',1,2.29);");
            db.execSQL("INSERT INTO `awards` VALUES (605,8,2,'School','Grant','Yakidoo',20745.25,'0','0',0,1.15);");
            db.execSQL("INSERT INTO `awards` VALUES (606,13,2,'Private','Bursary','Dynazzy',21998.52,'1','1',1,2.24);");
            db.execSQL("INSERT INTO `awards` VALUES (607,23,2,'Private','Bursary','Quamba',4417.11,'0','1',1,1.21);");
            db.execSQL("INSERT INTO `awards` VALUES (608,19,2,'School','Bursary','Yombu',21920.59,'1','1',0,2.18);");
            db.execSQL("INSERT INTO `awards` VALUES (609,1,2,'Private','Grant','Thoughtbridge',8390.35,'1','0',0,2.51);");
            db.execSQL("INSERT INTO `awards` VALUES (610,21,2,'Government','Scholarship','Viva',6901.87,'0','0',0,1.03);");
            db.execSQL("INSERT INTO `awards` VALUES (611,18,2,'Private','Grant','Geba',21269.04,'0','1',0,2.89);");
            db.execSQL("INSERT INTO `awards` VALUES (612,25,2,'School','Grant','Gevee',22231.7,'1','0',1,2.58);");
            db.execSQL("INSERT INTO `awards` VALUES (613,12,2,'School','Scholarship','Wordtune',13467.97,'1','0',1,1.85);");
            db.execSQL("INSERT INTO `awards` VALUES (614,5,2,'School','Grant','Blogtags',19508.56,'0','1',0,2.07);");
            db.execSQL("INSERT INTO `awards` VALUES (615,13,2,'Private','Grant','Voonte',13566.94,'1','1',0,3.06);");
            db.execSQL("INSERT INTO `awards` VALUES (616,3,2,'School','Bursary','Skaboo',20480.15,'1','1',1,1.06);");
            db.execSQL("INSERT INTO `awards` VALUES (617,19,2,'Government','Bursary','Skilith',20619.7,'1','1',1,1.19);");
            db.execSQL("INSERT INTO `awards` VALUES (618,3,2,'Private','Grant','Topiczoom',10789.27,'0','1',1,2.63);");
            db.execSQL("INSERT INTO `awards` VALUES (619,22,2,'Government','Bursary','Yodel',21814.72,'1','1',0,2.13);");
            db.execSQL("INSERT INTO `awards` VALUES (620,9,2,'Government','Bursary','Skinix',17807.93,'0','0',0,2.91);");
            db.execSQL("INSERT INTO `awards` VALUES (621,9,2,'School','Grant','Jabbertype',10108.66,'0','0',1,1.47);");
            db.execSQL("INSERT INTO `awards` VALUES (622,11,2,'Government','Scholarship','Tagchat',19544.22,'1','1',0,2.12);");
            db.execSQL("INSERT INTO `awards` VALUES (623,17,2,'Private','Scholarship','Blogspan',19104.05,'1','0',0,2.29);");
            db.execSQL("INSERT INTO `awards` VALUES (624,4,2,'School','Bursary','Tagfeed',21924.84,'1','0',0,1.56);");
            db.execSQL("INSERT INTO `awards` VALUES (625,21,2,'Government','Grant','Meemm',19087.82,'1','0',1,1.42);");
            db.execSQL("INSERT INTO `awards` VALUES (626,16,2,'School','Scholarship','InnoZ',2797.83,'0','1',0,1.42);");
            db.execSQL("INSERT INTO `awards` VALUES (627,4,2,'School','Grant','Camimbo',18299.7,'1','1',1,3.02);");
            db.execSQL("INSERT INTO `awards` VALUES (628,19,2,'Private','Grant','Rooxo',7985.8,'1','0',0,1.45);");
            db.execSQL("INSERT INTO `awards` VALUES (629,25,2,'Government','Grant','Wordify',22113.65,'0','0',0,2.75);");
            db.execSQL("INSERT INTO `awards` VALUES (630,4,2,'Government','Bursary','Eadel',15576.45,'1','0',1,2.15);");
            db.execSQL("INSERT INTO `awards` VALUES (631,24,2,'Government','Grant','Wikizz',17685.73,'1','1',1,2.31);");
            db.execSQL("INSERT INTO `awards` VALUES (632,3,2,'School','Bursary','Edgeclub',19377.89,'0','1',0,1.15);");
            db.execSQL("INSERT INTO `awards` VALUES (633,15,2,'School','Bursary','Dabvine',22749.58,'1','0',1,2.75);");
            db.execSQL("INSERT INTO `awards` VALUES (634,25,2,'School','Scholarship','Avamm',12754.17,'0','1',1,3.04);");
            db.execSQL("INSERT INTO `awards` VALUES (635,4,2,'Government','Grant','Dabvine',21078.16,'1','1',1,2.29);");
            db.execSQL("INSERT INTO `awards` VALUES (636,4,2,'School','Scholarship','Aimbo',11753.24,'1','1',1,1.65);");
            db.execSQL("INSERT INTO `awards` VALUES (637,5,2,'School','Grant','Buzzshare',19370,'1','1',0,2.97);");
            db.execSQL("INSERT INTO `awards` VALUES (638,24,2,'Private','Bursary','Edgewire',1025.8,'0','1',0,1.1);");
            db.execSQL("INSERT INTO `awards` VALUES (639,7,2,'Private','Grant','Centizu',14170.69,'0','0',0,1.26);");
            db.execSQL("INSERT INTO `awards` VALUES (640,5,2,'Private','Bursary','Skyble',8699.07,'1','0',1,1.99);");
            db.execSQL("INSERT INTO `awards` VALUES (641,25,2,'Private','Scholarship','Divavu',24297.41,'1','0',0,2.23);");
            db.execSQL("INSERT INTO `awards` VALUES (642,3,2,'Government','Scholarship','Jabbertype',2816.77,'0','1',1,2.46);");
            db.execSQL("INSERT INTO `awards` VALUES (643,21,2,'Private','Scholarship','Yata',12236.93,'0','1',1,2.93);");
            db.execSQL("INSERT INTO `awards` VALUES (644,6,2,'Government','Bursary','Mynte',4667.42,'0','0',0,1.77);");
            db.execSQL("INSERT INTO `awards` VALUES (645,22,2,'Government','Bursary','Voolith',12605.53,'1','0',0,2.46);");
            db.execSQL("INSERT INTO `awards` VALUES (646,21,2,'School','Bursary','Zoomcast',1227.35,'1','1',0,1.07);");
            db.execSQL("INSERT INTO `awards` VALUES (647,5,2,'Private','Bursary','Twimbo',13256.52,'0','1',1,2.05);");
            db.execSQL("INSERT INTO `awards` VALUES (648,7,2,'Private','Grant','Voomm',16384.51,'1','1',1,2.05);");
            db.execSQL("INSERT INTO `awards` VALUES (649,12,2,'Government','Grant','Skyble',22672.38,'1','0',0,2.9);");
            db.execSQL("INSERT INTO `awards` VALUES (650,2,2,'School','Bursary','Bubblemix',21523.84,'0','0',0,2.86);");
            db.execSQL("INSERT INTO `awards` VALUES (651,12,2,'Government','Scholarship','Skynoodle',21361.7,'1','0',0,2.01);");
            db.execSQL("INSERT INTO `awards` VALUES (652,8,2,'School','Scholarship','Ailane',3007.55,'1','1',0,2.27);");
            db.execSQL("INSERT INTO `awards` VALUES (653,21,2,'School','Grant','Realblab',11084.73,'0','1',1,2.59);");
            db.execSQL("INSERT INTO `awards` VALUES (654,25,2,'Government','Grant','Brainlounge',12094.73,'0','1',0,1.31);");
            db.execSQL("INSERT INTO `awards` VALUES (655,14,2,'Government','Scholarship','InnoZ',17653.22,'1','0',0,2.39);");
            db.execSQL("INSERT INTO `awards` VALUES (656,24,2,'School','Scholarship','Viva',19922.86,'0','1',1,2.32);");
            db.execSQL("INSERT INTO `awards` VALUES (657,7,2,'School','Scholarship','Tagtune',10765.47,'0','0',1,1.92);");
            db.execSQL("INSERT INTO `awards` VALUES (658,1,2,'Government','Scholarship','Skyndu',21194.04,'0','1',0,1.66);");
            db.execSQL("INSERT INTO `awards` VALUES (659,11,2,'Government','Grant','DabZ',19440.16,'1','1',1,3.03);");
            db.execSQL("INSERT INTO `awards` VALUES (660,10,2,'School','Scholarship','Tagfeed',6832.37,'1','1',1,2.74);");
            db.execSQL("INSERT INTO `awards` VALUES (661,14,2,'Private','Grant','Photobug',1963.42,'1','0',0,1.51);");
            db.execSQL("INSERT INTO `awards` VALUES (662,22,2,'School','Scholarship','Jazzy',4526.04,'0','1',1,2.57);");
            db.execSQL("INSERT INTO `awards` VALUES (663,1,2,'Government','Bursary','Thoughtstorm',11308.2,'0','0',1,2.43);");
            db.execSQL("INSERT INTO `awards` VALUES (664,7,2,'School','Grant','Skiba',18064.1,'1','0',0,1.17);");
            db.execSQL("INSERT INTO `awards` VALUES (665,25,2,'School','Grant','Quaxo',8989.19,'0','0',0,2.77);");
            db.execSQL("INSERT INTO `awards` VALUES (666,22,2,'School','Scholarship','Skiptube',1137.19,'1','1',1,2);");
            db.execSQL("INSERT INTO `awards` VALUES (667,6,2,'Government','Grant','Vinte',6132.03,'0','1',0,1.71);");
            db.execSQL("INSERT INTO `awards` VALUES (668,19,2,'School','Grant','Photobug',1566.62,'1','0',0,1.81);");
            db.execSQL("INSERT INTO `awards` VALUES (669,9,2,'Private','Scholarship','Yamia',14109.47,'0','1',1,2.87);");
            db.execSQL("INSERT INTO `awards` VALUES (670,7,2,'School','Bursary','Rhybox',19673.39,'1','0',1,1.3);");
            db.execSQL("INSERT INTO `awards` VALUES (671,13,2,'School','Grant','Flashspan',1444.53,'0','1',0,1.4);");
            db.execSQL("INSERT INTO `awards` VALUES (672,6,2,'Government','Bursary','Wordify',14031.25,'0','0',1,1.4);");
            db.execSQL("INSERT INTO `awards` VALUES (673,20,2,'School','Bursary','Eazzy',14921.07,'1','1',1,2.69);");
            db.execSQL("INSERT INTO `awards` VALUES (674,13,2,'School','Grant','Vipe',5986.69,'1','0',0,2.35);");
            db.execSQL("INSERT INTO `awards` VALUES (675,10,2,'Government','Bursary','Wikibox',9452.15,'1','0',1,2.4);");
            db.execSQL("INSERT INTO `awards` VALUES (676,8,2,'Private','Bursary','Vidoo',2204.15,'1','1',1,1.9);");
            db.execSQL("INSERT INTO `awards` VALUES (677,20,2,'Government','Bursary','Trunyx',17395.44,'0','1',1,2.24);");
            db.execSQL("INSERT INTO `awards` VALUES (678,5,2,'School','Scholarship','Skinte',7750.29,'0','0',1,2.52);");
            db.execSQL("INSERT INTO `awards` VALUES (679,14,2,'School','Grant','Flipstorm',7214.31,'0','0',0,2.74);");
            db.execSQL("INSERT INTO `awards` VALUES (680,15,2,'Private','Bursary','Quimba',23692.46,'0','1',0,2.96);");
            db.execSQL("INSERT INTO `awards` VALUES (681,16,2,'School','Bursary','Eire',20071.71,'0','1',1,2.95);");
            db.execSQL("INSERT INTO `awards` VALUES (682,12,2,'School','Bursary','Riffpath',5861.18,'1','1',0,1.64);");
            db.execSQL("INSERT INTO `awards` VALUES (683,18,2,'Private','Bursary','Babbleopia',20168.76,'0','0',0,1.54);");
            db.execSQL("INSERT INTO `awards` VALUES (684,23,2,'School','Scholarship','Skiba',2359.86,'0','0',1,1.39);");
            db.execSQL("INSERT INTO `awards` VALUES (685,22,2,'Government','Bursary','Youopia',5804.62,'0','1',1,1.18);");
            db.execSQL("INSERT INTO `awards` VALUES (686,17,2,'School','Bursary','Abatz',13342.67,'0','1',0,2.44);");
            db.execSQL("INSERT INTO `awards` VALUES (687,14,2,'Government','Bursary','Brightbean',20406.17,'0','1',0,1.25);");
            db.execSQL("INSERT INTO `awards` VALUES (688,14,2,'Private','Bursary','Jabberbean',23824.72,'1','1',0,2.68);");
            db.execSQL("INSERT INTO `awards` VALUES (689,19,2,'School','Grant','Photolist',1148.49,'0','0',1,1.27);");
            db.execSQL("INSERT INTO `awards` VALUES (690,17,2,'Government','Bursary','Tekfly',19251.12,'1','1',0,2.82);");
            db.execSQL("INSERT INTO `awards` VALUES (691,13,2,'Government','Scholarship','Flashset',5415.66,'0','1',1,1.08);");
            db.execSQL("INSERT INTO `awards` VALUES (692,12,2,'Private','Bursary','Fivechat',6586.05,'0','1',1,1.58);");
            db.execSQL("INSERT INTO `awards` VALUES (693,1,2,'School','Scholarship','Demivee',12559.94,'0','0',0,3.02);");
            db.execSQL("INSERT INTO `awards` VALUES (694,19,2,'School','Grant','Youspan',11288.55,'0','1',1,2.72);");
            db.execSQL("INSERT INTO `awards` VALUES (695,13,2,'School','Bursary','Skinder',4365.99,'0','1',1,2.83);");
            db.execSQL("INSERT INTO `awards` VALUES (696,25,2,'School','Scholarship','Meeveo',8453.29,'1','1',1,1.95);");
            db.execSQL("INSERT INTO `awards` VALUES (697,4,2,'School','Scholarship','Voolith',6031.89,'0','1',1,2.58);");
            db.execSQL("INSERT INTO `awards` VALUES (698,9,2,'School','Grant','Jetwire',17259.15,'0','1',0,2.35);");
            db.execSQL("INSERT INTO `awards` VALUES (699,12,2,'Government','Bursary','Edgepulse',11343.93,'0','1',0,2.75);");
            db.execSQL("INSERT INTO `awards` VALUES (700,15,2,'Government','Grant','Yambee',14425.89,'1','1',0,2.61);");
            db.execSQL("INSERT INTO `awards` VALUES (701,23,2,'Private','Grant','Tagpad',6984.92,'1','1',0,1.77);");
            db.execSQL("INSERT INTO `awards` VALUES (702,8,2,'Private','Bursary','Flipbug',17368.93,'0','0',1,1.83);");
            db.execSQL("INSERT INTO `awards` VALUES (703,5,2,'Private','Bursary','Yacero',6466.48,'1','0',0,1.79);");
            db.execSQL("INSERT INTO `awards` VALUES (704,18,2,'Private','Scholarship','Tagopia',23939.44,'1','1',1,2.42);");
            db.execSQL("INSERT INTO `awards` VALUES (705,8,2,'Private','Scholarship','Skiptube',17046.83,'1','0',0,2.3);");
            db.execSQL("INSERT INTO `awards` VALUES (706,1,2,'Private','Grant','Twitterbridge',11022.33,'1','0',0,1.91);");
            db.execSQL("INSERT INTO `awards` VALUES (707,13,2,'School','Bursary','Yodo',2776.83,'1','0',1,2.17);");
            db.execSQL("INSERT INTO `awards` VALUES (708,9,2,'Private','Grant','Thoughtworks',14909.34,'0','1',1,1.17);");
            db.execSQL("INSERT INTO `awards` VALUES (709,8,2,'School','Grant','Twitterlist',20855.36,'1','1',0,1.13);");
            db.execSQL("INSERT INTO `awards` VALUES (710,14,2,'School','Grant','Riffpedia',4026.09,'1','1',0,1.02);");
            db.execSQL("INSERT INTO `awards` VALUES (711,3,2,'School','Scholarship','Skyndu',7704.68,'1','0',0,3.07);");
            db.execSQL("INSERT INTO `awards` VALUES (712,19,2,'Government','Scholarship','Yakidoo',15272.49,'0','0',1,2.89);");
            db.execSQL("INSERT INTO `awards` VALUES (713,16,2,'Private','Grant','Flipstorm',9297.83,'0','0',0,1.23);");
            db.execSQL("INSERT INTO `awards` VALUES (714,15,2,'School','Scholarship','Quatz',2306.79,'0','0',1,2.16);");
            db.execSQL("INSERT INTO `awards` VALUES (715,1,2,'Private','Bursary','Dynabox',7819,'0','0',1,2.59);");
            db.execSQL("INSERT INTO `awards` VALUES (716,8,2,'School','Grant','Realfire',9916.92,'0','0',1,3.08);");
            db.execSQL("INSERT INTO `awards` VALUES (717,23,2,'Private','Scholarship','Roodel',15175.92,'0','0',0,2.49);");
            db.execSQL("INSERT INTO `awards` VALUES (718,16,2,'School','Grant','Zoovu',18510.71,'0','1',1,1.22);");
            db.execSQL("INSERT INTO `awards` VALUES (719,10,2,'School','Grant','Wordify',8899.64,'0','0',0,2.49);");
            db.execSQL("INSERT INTO `awards` VALUES (720,17,2,'Private','Bursary','Twitterbridge',1972.13,'0','1',0,2.55);");
            db.execSQL("INSERT INTO `awards` VALUES (721,21,2,'School','Bursary','Tambee',17810.41,'1','0',0,1.02);");
            db.execSQL("INSERT INTO `awards` VALUES (722,24,2,'Private','Scholarship','Agivu',20289.11,'1','0',1,2);");
            db.execSQL("INSERT INTO `awards` VALUES (723,19,2,'Government','Scholarship','Bubblebox',10681.51,'1','0',0,1.5);");
            db.execSQL("INSERT INTO `awards` VALUES (724,6,2,'School','Scholarship','Pixonyx',7795.99,'1','0',0,1.39);");
            db.execSQL("INSERT INTO `awards` VALUES (725,5,2,'Private','Bursary','Yakidoo',15033.63,'0','0',1,1.07);");
            db.execSQL("INSERT INTO `awards` VALUES (726,6,2,'School','Bursary','Jayo',10069.06,'1','1',0,2.19);");
            db.execSQL("INSERT INTO `awards` VALUES (727,14,2,'Private','Bursary','Edgeify',5120.79,'0','1',1,1.28);");
            db.execSQL("INSERT INTO `awards` VALUES (728,12,2,'Government','Scholarship','Voolith',943.73,'0','1',1,1.93);");
            db.execSQL("INSERT INTO `awards` VALUES (729,24,2,'School','Grant','Blogpad',16787.28,'0','1',1,2.94);");
            db.execSQL("INSERT INTO `awards` VALUES (730,1,2,'School','Bursary','Quire',1924.3,'1','0',0,1.3);");
            db.execSQL("INSERT INTO `awards` VALUES (731,1,2,'Private','Scholarship','Blognation',24358.24,'0','1',1,2.44);");
            db.execSQL("INSERT INTO `awards` VALUES (732,18,2,'School','Scholarship','Kamba',1192.55,'1','1',1,1.19);");
            db.execSQL("INSERT INTO `awards` VALUES (733,11,2,'Government','Scholarship','Feedspan',21164.63,'0','1',0,1.36);");
            db.execSQL("INSERT INTO `awards` VALUES (734,16,2,'Private','Scholarship','Avavee',7677.02,'1','0',1,2.42);");
            db.execSQL("INSERT INTO `awards` VALUES (735,13,2,'School','Scholarship','Zooveo',12756.82,'1','1',1,1.96);");
            db.execSQL("INSERT INTO `awards` VALUES (736,14,2,'School','Bursary','Lazzy',18530.31,'1','0',1,2.43);");
            db.execSQL("INSERT INTO `awards` VALUES (737,14,2,'Government','Bursary','Realbuzz',6538.88,'0','0',0,2.41);");
            db.execSQL("INSERT INTO `awards` VALUES (738,12,2,'School','Scholarship','Fanoodle',5321.47,'0','1',0,2.82);");
            db.execSQL("INSERT INTO `awards` VALUES (739,6,2,'Private','Scholarship','Riffpedia',20961.5,'1','1',1,2.99);");
            db.execSQL("INSERT INTO `awards` VALUES (740,13,2,'School','Bursary','Livefish',11077.38,'1','1',1,2.44);");
            db.execSQL("INSERT INTO `awards` VALUES (741,9,2,'School','Bursary','Shuffletag',504.36,'1','1',1,2.7);");
            db.execSQL("INSERT INTO `awards` VALUES (742,19,2,'School','Bursary','Zoonoodle',13633.1,'1','0',1,2);");
            db.execSQL("INSERT INTO `awards` VALUES (743,9,2,'Private','Scholarship','Topdrive',3446.94,'0','0',0,1.18);");
            db.execSQL("INSERT INTO `awards` VALUES (744,25,2,'Private','Scholarship','Dazzlesphere',22013.4,'0','0',0,2.75);");
            db.execSQL("INSERT INTO `awards` VALUES (745,24,2,'Private','Grant','Livetube',3741.82,'1','1',1,2.03);");
            db.execSQL("INSERT INTO `awards` VALUES (746,25,2,'Private','Grant','Oyonder',4756.05,'1','1',1,1.72);");
            db.execSQL("INSERT INTO `awards` VALUES (747,15,2,'School','Bursary','Innotype',12141.49,'1','0',0,2.63);");
            db.execSQL("INSERT INTO `awards` VALUES (748,18,2,'Private','Grant','DabZ',1304.69,'0','1',0,2.77);");
            db.execSQL("INSERT INTO `awards` VALUES (749,3,2,'Government','Scholarship','Browsebug',12031.77,'1','1',0,2.48);");
            db.execSQL("INSERT INTO `awards` VALUES (750,17,2,'Private','Grant','Tagopia',17517.86,'0','1',0,2.42);");
            db.execSQL("INSERT INTO `awards` VALUES (751,14,2,'Private','Bursary','Zoomzone',15296.64,'0','1',0,2.31);");
            db.execSQL("INSERT INTO `awards` VALUES (752,2,2,'Private','Scholarship','Eidel',1711.69,'0','1',0,1.54);");
            db.execSQL("INSERT INTO `awards` VALUES (753,21,2,'Private','Grant','Topdrive',16314.48,'0','1',0,2.28);");
            db.execSQL("INSERT INTO `awards` VALUES (754,23,2,'Government','Scholarship','Quatz',15693.61,'1','0',1,1.69);");
            db.execSQL("INSERT INTO `awards` VALUES (755,10,2,'Government','Bursary','Zoovu',18237.78,'1','0',1,1.27);");
            db.execSQL("INSERT INTO `awards` VALUES (756,14,2,'Government','Scholarship','Divanoodle',4675.1,'1','1',1,1.43);");
            db.execSQL("INSERT INTO `awards` VALUES (757,19,2,'Private','Bursary','Kwimbee',23014.21,'0','1',1,1.93);");
            db.execSQL("INSERT INTO `awards` VALUES (758,24,2,'Private','Grant','Riffwire',21303.33,'0','0',0,2.06);");
            db.execSQL("INSERT INTO `awards` VALUES (759,9,2,'Government','Bursary','Skyndu',2395.65,'1','0',0,2.34);");
            db.execSQL("INSERT INTO `awards` VALUES (760,25,2,'Private','Bursary','Jaxbean',18835.34,'1','0',0,1.87);");
            db.execSQL("INSERT INTO `awards` VALUES (761,9,2,'School','Bursary','Zoomcast',2872.5,'1','1',0,2.76);");
            db.execSQL("INSERT INTO `awards` VALUES (762,15,2,'Government','Bursary','Vimbo',3419.6,'1','1',0,1.12);");
            db.execSQL("INSERT INTO `awards` VALUES (763,20,2,'Private','Grant','Dazzlesphere',23067.38,'1','1',1,1.59);");
            db.execSQL("INSERT INTO `awards` VALUES (764,17,2,'Government','Scholarship','Oloo',720.92,'0','1',0,2.91);");
            db.execSQL("INSERT INTO `awards` VALUES (765,6,2,'School','Bursary','Wikizz',15998.47,'1','1',1,2.78);");
            db.execSQL("INSERT INTO `awards` VALUES (766,13,2,'Government','Grant','Wikivu',5353.18,'1','0',1,1.99);");
            db.execSQL("INSERT INTO `awards` VALUES (767,5,2,'School','Scholarship','Janyx',14876.07,'1','0',0,2.42);");
            db.execSQL("INSERT INTO `awards` VALUES (768,11,2,'School','Grant','Twitterlist',2619.51,'1','0',0,1.25);");
            db.execSQL("INSERT INTO `awards` VALUES (769,4,2,'Government','Bursary','Pixoboo',6791.81,'0','0',1,2.41);");
            db.execSQL("INSERT INTO `awards` VALUES (770,12,2,'Private','Scholarship','Zooveo',1600.39,'0','0',0,2.17);");
            db.execSQL("INSERT INTO `awards` VALUES (771,2,2,'Private','Bursary','Skiba',15371.11,'1','1',1,1.93);");
            db.execSQL("INSERT INTO `awards` VALUES (772,24,2,'School','Bursary','Trilith',20657.52,'0','1',0,1.8);");
            db.execSQL("INSERT INTO `awards` VALUES (773,2,2,'Private','Grant','Photojam',23249.93,'0','0',0,2.67);");
            db.execSQL("INSERT INTO `awards` VALUES (774,12,2,'Government','Scholarship','Omba',1703.7,'1','1',0,2.88);");
            db.execSQL("INSERT INTO `awards` VALUES (775,14,2,'Government','Grant','Zoombeat',13245.48,'0','0',0,1.34);");
            db.execSQL("INSERT INTO `awards` VALUES (776,12,2,'School','Bursary','Cogidoo',17112.92,'0','0',0,1.5);");
            db.execSQL("INSERT INTO `awards` VALUES (777,16,2,'Government','Grant','Demivee',16610.72,'0','1',0,1.99);");
            db.execSQL("INSERT INTO `awards` VALUES (778,23,2,'Government','Grant','Buzzbean',5870.67,'1','0',1,1.3);");
            db.execSQL("INSERT INTO `awards` VALUES (779,13,2,'School','Grant','Twiyo',19858.36,'1','0',1,2.95);");
            db.execSQL("INSERT INTO `awards` VALUES (780,9,2,'School','Grant','Eabox',5275.93,'0','1',1,2.56);");
            db.execSQL("INSERT INTO `awards` VALUES (781,14,2,'Government','Scholarship','Quinu',19159.22,'0','1',1,1.97);");
            db.execSQL("INSERT INTO `awards` VALUES (782,5,2,'Government','Scholarship','Browsebug',16679.61,'1','0',1,2.42);");
            db.execSQL("INSERT INTO `awards` VALUES (783,13,2,'Private','Bursary','Jaxnation',2795.69,'1','0',1,2.01);");
            db.execSQL("INSERT INTO `awards` VALUES (784,24,2,'School','Scholarship','Devbug',13399.47,'1','1',0,1.48);");
            db.execSQL("INSERT INTO `awards` VALUES (785,6,2,'Private','Bursary','Eare',24600.73,'0','1',1,2.94);");
            db.execSQL("INSERT INTO `awards` VALUES (786,3,2,'Government','Grant','Rhyloo',8753.03,'1','0',0,1.31);");
            db.execSQL("INSERT INTO `awards` VALUES (787,9,2,'School','Scholarship','Livefish',9837.59,'1','1',0,3.09);");
            db.execSQL("INSERT INTO `awards` VALUES (788,15,2,'Private','Grant','Bluejam',16393.21,'1','1',0,2.41);");
            db.execSQL("INSERT INTO `awards` VALUES (789,11,2,'Private','Scholarship','Mudo',21080.27,'0','0',1,1.26);");
            db.execSQL("INSERT INTO `awards` VALUES (790,17,2,'School','Bursary','Jabberstorm',9837.45,'0','1',1,2);");
            db.execSQL("INSERT INTO `awards` VALUES (791,6,2,'Private','Scholarship','Eimbee',20130.55,'1','1',1,1.28);");
            db.execSQL("INSERT INTO `awards` VALUES (792,18,2,'School','Bursary','Eamia',20995.76,'0','0',1,2.19);");
            db.execSQL("INSERT INTO `awards` VALUES (793,23,2,'School','Bursary','Photobug',17180.96,'0','1',0,2.96);");
            db.execSQL("INSERT INTO `awards` VALUES (794,20,2,'Government','Grant','Flipopia',14497.55,'0','0',1,1.12);");
            db.execSQL("INSERT INTO `awards` VALUES (795,10,2,'Government','Scholarship','Mydeo',14668.96,'1','0',1,1.64);");
            db.execSQL("INSERT INTO `awards` VALUES (796,8,2,'Private','Grant','Yata',5233.39,'0','1',1,2.26);");
            db.execSQL("INSERT INTO `awards` VALUES (797,5,2,'School','Bursary','Buzzdog',13523.65,'1','1',1,2.27);");
            db.execSQL("INSERT INTO `awards` VALUES (798,13,2,'Government','Grant','Thoughtsphere',8318.81,'1','0',1,2.12);");
            db.execSQL("INSERT INTO `awards` VALUES (799,24,2,'Private','Grant','Devcast',10161.07,'0','0',0,2.09);");
            db.execSQL("INSERT INTO `awards` VALUES (800,10,2,'Private','Scholarship','Jayo',2302.63,'1','1',1,1.89);");
            db.execSQL("INSERT INTO `awards` VALUES (801,25,2,'Private','Bursary','Linkbuzz',15080.61,'1','0',0,2.62);");
            db.execSQL("INSERT INTO `awards` VALUES (802,7,2,'Private','Bursary','Divavu',8230.93,'0','1',0,2.65);");
            db.execSQL("INSERT INTO `awards` VALUES (803,12,2,'Government','Scholarship','Skyvu',17572.11,'0','0',0,1.25);");
            db.execSQL("INSERT INTO `awards` VALUES (804,19,2,'Private','Scholarship','Wordpedia',18539.45,'0','0',1,2.3);");
            db.execSQL("INSERT INTO `awards` VALUES (805,19,2,'Private','Scholarship','BlogXS',20013.16,'0','0',1,3.07);");
            db.execSQL("INSERT INTO `awards` VALUES (806,15,2,'School','Grant','Rhynoodle',7773.83,'1','0',1,1.79);");
            db.execSQL("INSERT INTO `awards` VALUES (807,22,2,'Government','Grant','Vimbo',23680.77,'0','1',0,1.12);");
            db.execSQL("INSERT INTO `awards` VALUES (808,9,2,'Private','Grant','Eimbee',9165.52,'1','1',1,1.04);");
            db.execSQL("INSERT INTO `awards` VALUES (809,11,2,'Government','Bursary','Rhycero',22053.25,'0','0',1,1.64);");
            db.execSQL("INSERT INTO `awards` VALUES (810,3,2,'Government','Scholarship','Realcube',22904.45,'1','1',0,1.97);");
            db.execSQL("INSERT INTO `awards` VALUES (811,12,2,'School','Grant','Devbug',4678.45,'0','1',1,1.28);");
            db.execSQL("INSERT INTO `awards` VALUES (812,12,2,'Private','Scholarship','Yadel',22483.51,'0','1',0,2.3);");
            db.execSQL("INSERT INTO `awards` VALUES (813,4,2,'Private','Grant','Teklist',7560.64,'0','0',0,1.69);");
            db.execSQL("INSERT INTO `awards` VALUES (814,22,2,'Government','Grant','Kamba',8524.38,'1','1',1,2.25);");
            db.execSQL("INSERT INTO `awards` VALUES (815,14,2,'Private','Grant','Voolia',1370.58,'0','0',0,1.32);");
            db.execSQL("INSERT INTO `awards` VALUES (816,11,2,'Government','Scholarship','Dynabox',10128.31,'1','1',0,1.54);");
            db.execSQL("INSERT INTO `awards` VALUES (817,8,2,'Private','Grant','Zoonoodle',22902.12,'1','0',1,1.74);");
            db.execSQL("INSERT INTO `awards` VALUES (818,22,2,'School','Bursary','Skajo',9410.06,'1','0',0,1.57);");
            db.execSQL("INSERT INTO `awards` VALUES (819,12,2,'Government','Grant','Geba',13396.14,'0','1',1,2.95);");
            db.execSQL("INSERT INTO `awards` VALUES (820,10,2,'Private','Bursary','Avaveo',17035.49,'1','0',1,2.42);");
            db.execSQL("INSERT INTO `awards` VALUES (821,11,2,'School','Bursary','Cogilith',23054.42,'0','0',0,1.33);");
            db.execSQL("INSERT INTO `awards` VALUES (822,14,2,'Private','Scholarship','Photobean',11808.66,'1','1',1,1.33);");
            db.execSQL("INSERT INTO `awards` VALUES (823,24,2,'Government','Grant','Skyvu',3586.21,'0','1',1,1.34);");
            db.execSQL("INSERT INTO `awards` VALUES (824,25,2,'Government','Bursary','Omba',3218.61,'1','1',0,1.75);");
            db.execSQL("INSERT INTO `awards` VALUES (825,24,2,'School','Scholarship','Jabberstorm',22189.87,'0','1',1,2.95);");
            db.execSQL("INSERT INTO `awards` VALUES (826,25,2,'Government','Scholarship','Skiba',14386.05,'1','1',1,2.36);");
            db.execSQL("INSERT INTO `awards` VALUES (827,10,2,'School','Bursary','Zoovu',22423.22,'0','0',1,2.34);");
            db.execSQL("INSERT INTO `awards` VALUES (828,18,2,'Government','Scholarship','Einti',14848.66,'0','0',1,1.04);");
            db.execSQL("INSERT INTO `awards` VALUES (829,16,2,'Private','Scholarship','Yoveo',18561.62,'1','1',0,1.45);");
            db.execSQL("INSERT INTO `awards` VALUES (830,5,2,'Government','Scholarship','Thoughtstorm',7818.14,'0','0',1,1.81);");
            db.execSQL("INSERT INTO `awards` VALUES (831,11,2,'School','Scholarship','Lazzy',21574.86,'1','1',0,2.74);");
            db.execSQL("INSERT INTO `awards` VALUES (832,23,2,'School','Scholarship','Talane',10506,'0','1',1,2.57);");
            db.execSQL("INSERT INTO `awards` VALUES (833,2,2,'Government','Scholarship','Trilith',1689.96,'0','1',1,1.12);");
            db.execSQL("INSERT INTO `awards` VALUES (834,12,2,'Private','Bursary','Voonte',17409.77,'1','0',1,2.26);");
            db.execSQL("INSERT INTO `awards` VALUES (835,8,2,'Private','Scholarship','Janyx',544.39,'0','1',0,1.44);");
            db.execSQL("INSERT INTO `awards` VALUES (836,2,2,'Private','Bursary','Trunyx',11251.19,'0','1',0,2.68);");
            db.execSQL("INSERT INTO `awards` VALUES (837,18,2,'Government','Scholarship','Divavu',6326.55,'1','1',0,2.82);");
            db.execSQL("INSERT INTO `awards` VALUES (838,18,2,'Private','Grant','Roodel',11630.06,'0','0',1,1.28);");
            db.execSQL("INSERT INTO `awards` VALUES (839,15,2,'Government','Grant','Gabvine',1764.16,'1','1',0,2.29);");
            db.execSQL("INSERT INTO `awards` VALUES (840,7,2,'Government','Scholarship','Linklinks',2026.73,'0','1',0,2.18);");
            db.execSQL("INSERT INTO `awards` VALUES (841,10,2,'Government','Scholarship','Katz',551.11,'0','1',1,1.5);");
            db.execSQL("INSERT INTO `awards` VALUES (842,12,2,'Private','Scholarship','Blogtag',21354.23,'1','1',0,2.14);");
            db.execSQL("INSERT INTO `awards` VALUES (843,25,2,'Government','Scholarship','Eabox',24549.74,'0','0',0,1.13);");
            db.execSQL("INSERT INTO `awards` VALUES (844,10,2,'Government','Scholarship','Fivespan',828.34,'1','0',0,1.41);");
            db.execSQL("INSERT INTO `awards` VALUES (845,5,2,'Private','Grant','Gabtype',12083.78,'0','1',1,2.41);");
            db.execSQL("INSERT INTO `awards` VALUES (846,4,2,'Private','Bursary','Quaxo',18653.71,'1','1',1,1.84);");
            db.execSQL("INSERT INTO `awards` VALUES (847,2,2,'Government','Grant','Jetwire',21292.25,'0','0',0,1.85);");
            db.execSQL("INSERT INTO `awards` VALUES (848,5,2,'Government','Bursary','Teklist',500.71,'0','0',1,1.25);");
            db.execSQL("INSERT INTO `awards` VALUES (849,21,2,'Government','Scholarship','Twitternation',5446.56,'1','1',1,2.83);");
            db.execSQL("INSERT INTO `awards` VALUES (850,3,2,'School','Scholarship','Browseblab',22671.24,'1','0',0,1.35);");
            db.execSQL("INSERT INTO `awards` VALUES (851,17,2,'School','Bursary','Ainyx',16909.41,'1','1',1,1.94);");
            db.execSQL("INSERT INTO `awards` VALUES (852,19,2,'School','Bursary','Jayo',20721.7,'0','0',0,2.02);");
            db.execSQL("INSERT INTO `awards` VALUES (853,9,2,'Private','Grant','Avaveo',21122.29,'1','0',0,1.34);");
            db.execSQL("INSERT INTO `awards` VALUES (854,3,2,'School','Grant','Gigabox',7910.99,'1','1',0,1.43);");
            db.execSQL("INSERT INTO `awards` VALUES (855,13,2,'Private','Scholarship','Thoughtmix',5422.93,'1','1',0,1.03);");
            db.execSQL("INSERT INTO `awards` VALUES (856,12,2,'Private','Grant','Flashpoint',20592.42,'0','1',0,2);");
            db.execSQL("INSERT INTO `awards` VALUES (857,23,2,'Private','Bursary','Quaxo',11487.36,'0','1',1,1.33);");
            db.execSQL("INSERT INTO `awards` VALUES (858,5,2,'School','Bursary','Gigazoom',1562.41,'1','0',1,2.89);");
            db.execSQL("INSERT INTO `awards` VALUES (859,9,2,'Government','Scholarship','Gabvine',9211.83,'0','1',0,1.32);");
            db.execSQL("INSERT INTO `awards` VALUES (860,13,2,'Private','Grant','Yamia',7611.09,'0','1',1,1.72);");
            db.execSQL("INSERT INTO `awards` VALUES (861,24,2,'Government','Grant','Realbridge',22295.25,'1','1',1,3.06);");
            db.execSQL("INSERT INTO `awards` VALUES (862,2,2,'Private','Scholarship','Jabberbean',16663.55,'0','1',1,2.34);");
            db.execSQL("INSERT INTO `awards` VALUES (863,15,2,'Private','Grant','Riffwire',15541,'0','1',0,2.53);");
            db.execSQL("INSERT INTO `awards` VALUES (864,21,2,'School','Scholarship','Katz',22679.5,'0','1',0,1.28);");
            db.execSQL("INSERT INTO `awards` VALUES (865,25,2,'Government','Scholarship','Topicshots',18817.17,'1','0',1,1.16);");
            db.execSQL("INSERT INTO `awards` VALUES (866,22,2,'Government','Grant','Jabberbean',4327.5,'0','0',1,1.46);");
            db.execSQL("INSERT INTO `awards` VALUES (867,10,2,'School','Scholarship','Demimbu',20536.3,'0','0',1,2.5);");
            db.execSQL("INSERT INTO `awards` VALUES (868,19,2,'Government','Grant','Edgeclub',22483.1,'1','1',0,3.08);");
            db.execSQL("INSERT INTO `awards` VALUES (869,12,2,'Government','Scholarship','Mita',8485.16,'0','1',0,1.89);");
            db.execSQL("INSERT INTO `awards` VALUES (870,23,2,'Government','Grant','Realblab',10035.54,'0','1',1,3.07);");
            db.execSQL("INSERT INTO `awards` VALUES (871,8,2,'School','Bursary','Topicstorm',3771.74,'0','0',0,1.09);");
            db.execSQL("INSERT INTO `awards` VALUES (872,20,2,'School','Scholarship','Skajo',16777.88,'0','0',0,1.1);");
            db.execSQL("INSERT INTO `awards` VALUES (873,5,2,'Government','Scholarship','Blogpad',16549.61,'0','0',1,2.47);");
            db.execSQL("INSERT INTO `awards` VALUES (874,10,2,'Private','Scholarship','Jabberbean',9490.51,'0','0',1,1.44);");
            db.execSQL("INSERT INTO `awards` VALUES (875,4,2,'School','Scholarship','Jamia',13014.01,'1','1',0,2.85);");
            db.execSQL("INSERT INTO `awards` VALUES (876,6,2,'Government','Bursary','Layo',15381.52,'0','0',1,1.64);");
            db.execSQL("INSERT INTO `awards` VALUES (877,14,2,'Private','Grant','Thoughtmix',19354.41,'0','0',1,1.81);");
            db.execSQL("INSERT INTO `awards` VALUES (878,6,2,'School','Scholarship','Thoughtblab',11892.54,'0','0',0,2.02);");
            db.execSQL("INSERT INTO `awards` VALUES (879,19,2,'School','Bursary','Leexo',752.82,'1','1',1,2.7);");
            db.execSQL("INSERT INTO `awards` VALUES (880,9,2,'Government','Bursary','Skyba',15361.51,'0','0',0,2.19);");
            db.execSQL("INSERT INTO `awards` VALUES (881,8,2,'Private','Grant','Brightdog',2779.4,'0','1',1,1.34);");
            db.execSQL("INSERT INTO `awards` VALUES (882,11,2,'Private','Scholarship','Dabtype',22588.33,'0','1',0,1.02);");
            db.execSQL("INSERT INTO `awards` VALUES (883,25,2,'School','Bursary','Demimbu',18451.66,'1','1',0,1.79);");
            db.execSQL("INSERT INTO `awards` VALUES (884,22,2,'Private','Bursary','Browsecat',2332.64,'1','1',0,2.59);");
            db.execSQL("INSERT INTO `awards` VALUES (885,17,2,'Private','Grant','Vinder',621.44,'1','1',1,1.95);");
            db.execSQL("INSERT INTO `awards` VALUES (886,13,2,'Private','Scholarship','Gabspot',1898.43,'0','1',1,1.62);");
            db.execSQL("INSERT INTO `awards` VALUES (887,6,2,'Government','Grant','Zoozzy',20624.52,'0','0',0,1.37);");
            db.execSQL("INSERT INTO `awards` VALUES (888,11,2,'Private','Scholarship','Snaptags',21510.11,'0','1',1,1.73);");
            db.execSQL("INSERT INTO `awards` VALUES (889,4,2,'School','Bursary','Yodoo',13198.77,'0','0',1,2.27);");
            db.execSQL("INSERT INTO `awards` VALUES (890,2,2,'School','Grant','Zoombeat',1923.56,'1','1',1,1.49);");
            db.execSQL("INSERT INTO `awards` VALUES (891,4,2,'School','Bursary','Photojam',3133.7,'1','0',1,1.96);");
            db.execSQL("INSERT INTO `awards` VALUES (892,18,2,'Government','Scholarship','Edgetag',5574.32,'0','1',1,2.42);");
            db.execSQL("INSERT INTO `awards` VALUES (893,8,2,'School','Scholarship','Avamm',12042.59,'0','0',0,1.49);");
            db.execSQL("INSERT INTO `awards` VALUES (894,4,2,'Private','Bursary','Meemm',16667.38,'1','0',1,1.71);");
            db.execSQL("INSERT INTO `awards` VALUES (895,12,2,'Government','Bursary','Katz',11099.43,'1','0',1,1.18);");
            db.execSQL("INSERT INTO `awards` VALUES (896,24,2,'School','Grant','Yamia',6735.72,'0','1',1,2.27);");
            db.execSQL("INSERT INTO `awards` VALUES (897,17,2,'Government','Scholarship','Meemm',15151.89,'0','1',1,3.04);");
            db.execSQL("INSERT INTO `awards` VALUES (898,18,2,'School','Scholarship','Yambee',13302.55,'1','1',1,2.42);");
            db.execSQL("INSERT INTO `awards` VALUES (899,8,2,'School','Scholarship','Aivee',5929.46,'0','1',0,1.28);");
            db.execSQL("INSERT INTO `awards` VALUES (900,6,2,'School','Scholarship','Oyoyo',24438.84,'1','1',1,1.71);");
            db.execSQL("INSERT INTO `awards` VALUES (901,6,2,'School','Grant','Quimba',12785.85,'1','1',1,2.4);");
            db.execSQL("INSERT INTO `awards` VALUES (902,15,2,'Private','Bursary','Tambee',1095.47,'1','1',0,2.41);");
            db.execSQL("INSERT INTO `awards` VALUES (903,9,2,'Private','Bursary','Jabbersphere',2040.13,'1','1',1,2.41);");
            db.execSQL("INSERT INTO `awards` VALUES (904,7,2,'School','Grant','Wikizz',21980.74,'1','1',1,2.48);");
            db.execSQL("INSERT INTO `awards` VALUES (905,19,2,'School','Scholarship','Tekfly',1550.01,'0','1',1,2.19);");
            db.execSQL("INSERT INTO `awards` VALUES (906,13,2,'Private','Bursary','Dabshots',3463.44,'1','0',1,1.79);");
            db.execSQL("INSERT INTO `awards` VALUES (907,18,2,'School','Scholarship','Wikizz',3849.63,'0','1',0,1.21);");
            db.execSQL("INSERT INTO `awards` VALUES (908,1,2,'School','Bursary','Skipfire',17316.85,'1','1',0,1.34);");
            db.execSQL("INSERT INTO `awards` VALUES (909,24,2,'Government','Scholarship','Divanoodle',12846.13,'0','0',0,1.14);");
            db.execSQL("INSERT INTO `awards` VALUES (910,5,2,'Government','Bursary','Youfeed',3511.13,'1','1',0,1.79);");
            db.execSQL("INSERT INTO `awards` VALUES (911,12,2,'School','Bursary','Rhyzio',12629.71,'1','0',1,2.75);");
            db.execSQL("INSERT INTO `awards` VALUES (912,6,2,'Government','Grant','Dabvine',18088.35,'1','1',1,2.13);");
            db.execSQL("INSERT INTO `awards` VALUES (913,23,2,'School','Grant','Skipstorm',23721.96,'0','1',0,2.34);");
            db.execSQL("INSERT INTO `awards` VALUES (914,17,2,'Private','Bursary','Oyoba',24215.12,'0','0',0,2.12);");
            db.execSQL("INSERT INTO `awards` VALUES (915,1,2,'School','Scholarship','Bubbletube',9749.99,'1','1',1,1.58);");
            db.execSQL("INSERT INTO `awards` VALUES (916,14,2,'Government','Scholarship','Bubblemix',13863.3,'1','0',0,2.24);");
            db.execSQL("INSERT INTO `awards` VALUES (917,21,2,'Private','Grant','Geba',16709.97,'0','1',0,3.08);");
            db.execSQL("INSERT INTO `awards` VALUES (918,4,2,'School','Bursary','Topiczoom',21336.66,'1','0',0,2.75);");
            db.execSQL("INSERT INTO `awards` VALUES (919,3,2,'School','Scholarship','Yamia',8357.09,'0','0',1,2.95);");
            db.execSQL("INSERT INTO `awards` VALUES (920,11,2,'Private','Bursary','Demivee',12753.1,'1','1',1,2.23);");
            db.execSQL("INSERT INTO `awards` VALUES (921,16,2,'School','Scholarship','Janyx',3302.64,'1','1',1,2.88);");
            db.execSQL("INSERT INTO `awards` VALUES (922,16,2,'School','Grant','Mydo',18389.63,'1','0',0,2.33);");
            db.execSQL("INSERT INTO `awards` VALUES (923,1,2,'School','Bursary','Babbleblab',12975.94,'1','0',1,2.8);");
            db.execSQL("INSERT INTO `awards` VALUES (924,25,2,'Government','Grant','Quatz',19947.48,'1','1',1,1.85);");
            db.execSQL("INSERT INTO `awards` VALUES (925,6,2,'School','Grant','Brainlounge',921.41,'1','1',0,2.25);");
            db.execSQL("INSERT INTO `awards` VALUES (926,23,2,'School','Scholarship','Browsezoom',12721.82,'0','0',1,1.94);");
            db.execSQL("INSERT INTO `awards` VALUES (927,13,2,'School','Scholarship','Yacero',3952.48,'0','1',0,1.69);");
            db.execSQL("INSERT INTO `awards` VALUES (928,20,2,'Private','Grant','Yodoo',5909.19,'0','1',1,1.94);");
            db.execSQL("INSERT INTO `awards` VALUES (929,19,2,'School','Scholarship','Voomm',20792.39,'0','0',1,1.04);");
            db.execSQL("INSERT INTO `awards` VALUES (930,6,2,'Private','Bursary','Oodoo',23555.82,'1','0',1,1.83);");
            db.execSQL("INSERT INTO `awards` VALUES (931,21,2,'School','Scholarship','Fliptune',21241.85,'0','1',1,2.61);");
            db.execSQL("INSERT INTO `awards` VALUES (932,3,2,'School','Grant','Jayo',24691.62,'0','1',0,2.29);");
            db.execSQL("INSERT INTO `awards` VALUES (933,23,2,'Government','Grant','Twitterworks',16505.89,'0','1',1,2.44);");
            db.execSQL("INSERT INTO `awards` VALUES (934,9,2,'School','Scholarship','Podcat',20403.27,'0','1',0,1.54);");
            db.execSQL("INSERT INTO `awards` VALUES (935,4,2,'School','Bursary','Photobug',4611.33,'0','0',1,2.15);");
            db.execSQL("INSERT INTO `awards` VALUES (936,16,2,'School','Grant','Camimbo',3252.34,'1','1',0,2.61);");
            db.execSQL("INSERT INTO `awards` VALUES (937,17,2,'Private','Scholarship','Bubblemix',1893.74,'1','0',1,2.26);");
            db.execSQL("INSERT INTO `awards` VALUES (938,2,2,'Government','Bursary','Skyndu',8042.06,'1','1',1,2.71);");
            db.execSQL("INSERT INTO `awards` VALUES (939,24,2,'Private','Bursary','Yoveo',22042.63,'0','0',0,2.2);");
            db.execSQL("INSERT INTO `awards` VALUES (940,14,2,'Private','Grant','Twimbo',9806.61,'1','1',1,1.02);");
            db.execSQL("INSERT INTO `awards` VALUES (941,25,2,'Private','Bursary','Edgepulse',9893.11,'1','1',1,1.1);");
            db.execSQL("INSERT INTO `awards` VALUES (942,13,2,'Private','Scholarship','Meeveo',9393.68,'1','0',1,1.41);");
            db.execSQL("INSERT INTO `awards` VALUES (943,22,2,'Private','Scholarship','Skimia',24671.86,'0','0',0,2.97);");
            db.execSQL("INSERT INTO `awards` VALUES (944,8,2,'Private','Bursary','Vinte',12432.52,'0','1',1,1.24);");
            db.execSQL("INSERT INTO `awards` VALUES (945,13,2,'Private','Grant','Gabcube',23960.81,'0','1',0,2.94);");
            db.execSQL("INSERT INTO `awards` VALUES (946,21,2,'School','Bursary','Yambee',10311.18,'1','0',1,1.48);");
            db.execSQL("INSERT INTO `awards` VALUES (947,1,2,'School','Grant','Flashset',9953.84,'0','1',1,2.93);");
            db.execSQL("INSERT INTO `awards` VALUES (948,20,2,'School','Scholarship','Wikivu',1454.44,'1','0',0,2.96);");
            db.execSQL("INSERT INTO `awards` VALUES (949,22,2,'School','Bursary','Linkbuzz',21870.15,'1','1',0,1.4);");
            db.execSQL("INSERT INTO `awards` VALUES (950,4,2,'School','Bursary','Pixoboo',16694.03,'0','0',0,1.36);");
            db.execSQL("INSERT INTO `awards` VALUES (951,7,2,'Government','Scholarship','Zoomzone',6259.18,'0','0',1,2.27);");
            db.execSQL("INSERT INTO `awards` VALUES (952,16,2,'Government','Bursary','Realblab',17311.9,'0','0',1,2.04);");
            db.execSQL("INSERT INTO `awards` VALUES (953,24,2,'School','Bursary','Bubblemix',1941.71,'0','1',0,1.79);");
            db.execSQL("INSERT INTO `awards` VALUES (954,13,2,'School','Grant','Jabbercube',11692.23,'0','1',1,1.36);");
            db.execSQL("INSERT INTO `awards` VALUES (955,22,2,'Government','Bursary','Bluezoom',11738.89,'0','0',1,2.75);");
            db.execSQL("INSERT INTO `awards` VALUES (956,16,2,'Private','Scholarship','Yoveo',20343.14,'1','1',0,1.48);");
            db.execSQL("INSERT INTO `awards` VALUES (957,22,2,'School','Scholarship','Browsetype',24323.89,'1','1',0,2.41);");
            db.execSQL("INSERT INTO `awards` VALUES (958,2,2,'Private','Bursary','Browseblab',16494.5,'0','1',0,2.95);");
            db.execSQL("INSERT INTO `awards` VALUES (959,23,2,'Government','Scholarship','Thoughtmix',13142.62,'0','1',1,1.44);");
            db.execSQL("INSERT INTO `awards` VALUES (960,14,2,'Government','Scholarship','Flashpoint',14728.46,'0','1',1,1.13);");
            db.execSQL("INSERT INTO `awards` VALUES (961,16,2,'Private','Grant','Skimia',2872.18,'0','0',1,1.46);");
            db.execSQL("INSERT INTO `awards` VALUES (962,20,2,'School','Scholarship','Skiptube',9295.2,'0','1',0,2);");
            db.execSQL("INSERT INTO `awards` VALUES (963,21,2,'Private','Grant','Photofeed',24441.58,'1','1',0,1.14);");
            db.execSQL("INSERT INTO `awards` VALUES (964,19,2,'Private','Grant','Agivu',7159.99,'0','1',0,2.8);");
            db.execSQL("INSERT INTO `awards` VALUES (965,9,2,'Private','Scholarship','Zoombeat',584.9,'0','1',0,2.76);");
            db.execSQL("INSERT INTO `awards` VALUES (966,14,2,'School','Grant','Skyble',655.62,'0','0',1,2.69);");
            db.execSQL("INSERT INTO `awards` VALUES (967,11,2,'Government','Grant','Meemm',10720.36,'1','1',0,1.36);");
            db.execSQL("INSERT INTO `awards` VALUES (968,10,2,'Private','Grant','Wikizz',5213.44,'0','0',0,2.88);");
            db.execSQL("INSERT INTO `awards` VALUES (969,10,2,'School','Grant','Flipstorm',10308.69,'1','0',1,2.44);");
            db.execSQL("INSERT INTO `awards` VALUES (970,23,2,'Private','Grant','Pixoboo',3675.44,'1','1',0,2.69);");
            db.execSQL("INSERT INTO `awards` VALUES (971,11,2,'Government','Bursary','Skibox',9295.07,'1','1',1,1.61);");
            db.execSQL("INSERT INTO `awards` VALUES (972,23,2,'Private','Bursary','Kaymbo',9865.69,'0','1',0,1.34);");
            db.execSQL("INSERT INTO `awards` VALUES (973,5,2,'School','Grant','Centimia',2786.95,'0','1',1,2.33);");
            db.execSQL("INSERT INTO `awards` VALUES (974,24,2,'School','Scholarship','Ailane',16691.13,'1','0',1,2.01);");
            db.execSQL("INSERT INTO `awards` VALUES (975,15,2,'School','Scholarship','Buzzster',18289.35,'0','1',0,1.08);");
            db.execSQL("INSERT INTO `awards` VALUES (976,22,2,'School','Scholarship','Zoomdog',11365.34,'0','1',1,2.76);");
            db.execSQL("INSERT INTO `awards` VALUES (977,1,2,'School','Bursary','Jaxspan',4401.72,'1','1',0,2.56);");
            db.execSQL("INSERT INTO `awards` VALUES (978,23,2,'Government','Grant','Zava',13074.66,'0','0',0,1.02);");
            db.execSQL("INSERT INTO `awards` VALUES (979,5,2,'Private','Grant','Dabfeed',16363,'1','1',1,2.48);");
            db.execSQL("INSERT INTO `awards` VALUES (980,18,2,'Private','Scholarship','Meevee',20746.94,'1','1',0,2.27);");
            db.execSQL("INSERT INTO `awards` VALUES (981,17,2,'Government','Grant','Aimbo',17870.77,'1','0',1,2.41);");
            db.execSQL("INSERT INTO `awards` VALUES (982,19,2,'Government','Bursary','Thoughtworks',16865.42,'1','1',0,1.33);");
            db.execSQL("INSERT INTO `awards` VALUES (983,5,2,'School','Grant','Eidel',18798.22,'0','0',1,2.57);");
            db.execSQL("INSERT INTO `awards` VALUES (984,17,2,'Private','Bursary','Rhybox',21860.24,'0','0',1,1.27);");
            db.execSQL("INSERT INTO `awards` VALUES (985,23,2,'School','Bursary','Mydo',9585.27,'1','0',0,3);");
            db.execSQL("INSERT INTO `awards` VALUES (986,3,2,'Government','Grant','Jabberstorm',5696.93,'0','0',0,2.7);");
            db.execSQL("INSERT INTO `awards` VALUES (987,19,2,'School','Bursary','Meedoo',24678.4,'1','0',1,2.1);");
            db.execSQL("INSERT INTO `awards` VALUES (988,19,2,'Private','Bursary','Tazzy',15827.98,'0','0',0,2.22);");
            db.execSQL("INSERT INTO `awards` VALUES (989,13,2,'Private','Bursary','Shufflebeat',16267.64,'1','0',1,2.84);");
            db.execSQL("INSERT INTO `awards` VALUES (990,5,2,'Private','Grant','Voonte',18931.63,'0','1',1,1.88);");
            db.execSQL("INSERT INTO `awards` VALUES (991,4,2,'Private','Bursary','Jabberbean',6402.32,'0','1',0,1.66);");
            db.execSQL("INSERT INTO `awards` VALUES (992,7,2,'Government','Scholarship','Brainsphere',10703.81,'1','0',0,1.08);");
            db.execSQL("INSERT INTO `awards` VALUES (993,13,2,'School','Bursary','Lazzy',15700.09,'0','0',0,1.23);");
            db.execSQL("INSERT INTO `awards` VALUES (994,24,2,'Private','Scholarship','Miboo',9717.78,'0','0',1,1.29);");
            db.execSQL("INSERT INTO `awards` VALUES (995,21,2,'Government','Bursary','Meembee',7475.69,'0','0',0,2.42);");
            db.execSQL("INSERT INTO `awards` VALUES (996,5,2,'School','Scholarship','Divape',21081.96,'1','0',1,2.65);");
            db.execSQL("INSERT INTO `awards` VALUES (997,13,2,'School','Bursary','DabZ',16285.61,'0','1',1,1.33);");
            db.execSQL("INSERT INTO `awards` VALUES (998,16,2,'School','Scholarship','Voomm',2621.04,'1','0',1,1.23);");
            db.execSQL("INSERT INTO `awards` VALUES (999,18,2,'Government','Bursary','Bluejam',21528.23,'1','0',0,2.6);");
            db.execSQL("INSERT INTO `awards` VALUES (1000,1,2,'Government','Bursary','Voonte',19388.07,'1','1',0,2.56);");
        }

        db.execSQL("CREATE TABLE IF NOT EXISTS provinces (\n" +
                "\t`province_id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`province_name`\tTEXT NOT NULL,\n" +
                "\t`province_code`\tTEXT NOT NULL\n" +
                ");");

        String countProvince = "SELECT count(*) FROM provinces";
        Cursor cProv = db.rawQuery(countProvince, null);
        cProv.moveToFirst();
        int icountProv = cProv.getInt(0);


        if(icountProv>0){
        }
        else {
            db.execSQL("INSERT INTO provinces VALUES (1,'Alberta','AB');");
            db.execSQL("INSERT INTO provinces VALUES (2,'British Columbia','BC');");
            db.execSQL("INSERT INTO provinces VALUES (3,'Manitoba','MB');");
            db.execSQL("INSERT INTO provinces VALUES (4,'New Brunswick','NB');");
            db.execSQL("INSERT INTO provinces VALUES (5,'Newfoundland and Labrador','NL');");
            db.execSQL("INSERT INTO provinces VALUES (6,'Nova Scotia','NS');");
            db.execSQL("INSERT INTO provinces VALUES (7,'Ontario','ON');");
            db.execSQL("INSERT INTO provinces VALUES (8,'Prince Edward Island','PE');");
            db.execSQL("INSERT INTO provinces VALUES (9,'Quebec','QC');");
            db.execSQL("INSERT INTO provinces VALUES (10,'Saskatchewan','SK');");
        }

        db.execSQL("CREATE TABLE IF NOT EXISTS schools (\n" +
                "\t`school_id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`school_name`\tTEXT NOT NULL,\n" +
                "\t`school_province_id`\tINTEGER NOT NULL,\n" +
                "\tFOREIGN KEY(`school_province_id`) REFERENCES `provinces`(`province_id`)\n" +
                ");");

        String countSchool = "SELECT count(*) FROM schools";
        Cursor cSchool = db.rawQuery(countSchool, null);
        cSchool.moveToFirst();
        int icountSchool = cSchool.getInt(0);
        if(icountSchool>0){
        }
        else {
            db.execSQL("INSERT INTO schools VALUES (1,'Camosun College',2);");
            db.execSQL("INSERT INTO schools VALUES (2,'College of New Caledonia',2);");
            db.execSQL("INSERT INTO schools VALUES (3,'College of the Rockies',2);");
            db.execSQL("INSERT INTO schools VALUES (4,'Douglas College',2);");
            db.execSQL("INSERT INTO schools VALUES (5,'Langara College',2);");
            db.execSQL("INSERT INTO schools VALUES (6,'North Island College',2);");
            db.execSQL("INSERT INTO schools VALUES (7,'Northern Lights College',2);");
            db.execSQL("INSERT INTO schools VALUES (8,'Northwest Community College',2);");
            db.execSQL("INSERT INTO schools VALUES (9,'Okanagan College',2);");
            db.execSQL("INSERT INTO schools VALUES (10,'Selkirk College',2);");
            db.execSQL("INSERT INTO schools VALUES (11,'Vancouver Community College',2);");
            db.execSQL("INSERT INTO schools VALUES (12,'Capilano University',2);");
            db.execSQL("INSERT INTO schools VALUES (13,'Emily Carr University of Art and Design',2);");
            db.execSQL("INSERT INTO schools VALUES (14,'Kwantlen Polytechnic University',2);");
            db.execSQL("INSERT INTO schools VALUES (15,'Royal Roads University',2);");
            db.execSQL("INSERT INTO schools VALUES (16,'Simon Fraser University',2);");
            db.execSQL("INSERT INTO schools VALUES (17,'Thompson Rivers University',2);");
            db.execSQL("INSERT INTO schools VALUES (18,'University of British Columbia',2);");
            db.execSQL("INSERT INTO schools VALUES (19,'University of the Fraser Valley',2);");
            db.execSQL("INSERT INTO schools VALUES (20,'University of Northern British Columbia',2);");
            db.execSQL("INSERT INTO schools VALUES (21,'University of Victoria',2);");
            db.execSQL("INSERT INTO schools VALUES (22,'Vancouver Island University',2);");
            db.execSQL("INSERT INTO schools VALUES (23,'British Columbia Institute of Technology',2);");
            db.execSQL("INSERT INTO schools VALUES (24,'Justice Institute of British Columbia',2);");
            db.execSQL("INSERT INTO schools VALUES (25,'Nicola Valley Institute of Technology',2);");
        }

        db.execSQL("CREATE TABLE IF NOT EXISTS awardsSaved (\n" +
                "\t`award_id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`award_`\tTEXT NOT NULL,\n" +
                "\t`province_code`\tTEXT NOT NULL\n" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
