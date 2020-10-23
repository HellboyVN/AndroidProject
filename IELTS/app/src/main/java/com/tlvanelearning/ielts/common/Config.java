package com.tlvanelearning.ielts.common;

import java.util.ArrayList;

public class Config {
    public static String ADMOB_BANNER_ADS_ID = "ca-app-pub-8468661407843417/9208430636";
    public static String ADMOB_INTER_ADS_ID = "ca-app-pub-8468661407843417/6195733418";
    public static String FACEBOOK_BANNER_ADS_ID = "2001659010117857_2002475570036201";
    public static String FACEBOOK_INTER_ADS_ID = "2001659010117857_2002477126702712";
    public static final String ITEM_SKU = "com.tlvanelearning.ielts";
//    public static final String ITEM_SKU = "android.test.purchased";
    public static String URL = "https://raw.githubusercontent.com/HellboyVN/IeltsListening/master/";
    public static ArrayList<Item> items = new ArrayList<>();

    static {
        items.add(new SectionItem("IELTS for You"));
        items.add(new EntryItem("IELTS Writing", "Over 150 IELTS Writing sample"));
        items.add(new EntryItem("IELTS Speaking", "Over 150 IELTS Speaking sample"));
        items.add(new EntryItem("IELTS Quiz", "Over 2000 questions & 200 levels"));
        items.add(new EntryItem("IELTS Vocabulary", "Over 5000 IELTS Words to learn vocabulary & simple"));
        items.add(new SectionItem("English Listening"));
        items.add(new EntryItem("All lessons", "Over 700 listening lessons."));
        items.add(new SectionItem("Common Words & Phrases"));
        items.add(new EntryItem("Most Common Phrases", "1000 most commonly used phrases."));
        items.add(new EntryItem("Common Phrases by Categories", "List of common phrases sorting alphabetically."));
        items.add(new EntryItem("Most Common Words", "1200 most commonly used words."));
        items.add(new EntryItem("Common Words by Categories", "List of common words sorting alphabetically."));
        items.add(new SectionItem("More"));
        items.add(new EntryItem("English Idioms & Phrases", "Over 500 idioms."));
        items.add(new EntryItem("English Useful Expressions", "119 topics & 1100 useful expressions."));
        items.add(new EntryItem("Irregular verbs.", "600+ irregular verbs."));
        items.add(new EntryItem("English Proverbs.", "List of 750+ english proverbs."));
        items.add(new EntryItem("American Slang.", "List of 200+ most common American Slangs."));
        items.add(new EntryItem("English Idioms by Categories", "List of idioms grouped by categories."));
        items.add(new EntryItem("Common Phrasal Verbs", "Over 1700+ common phrasal verbs."));
        items.add(new EntryItem("SAT Words", "Over 3000+ common SAT words."));
        items.add(new EntryItem("English Tenses", "Verb tenses to learn english grammar."));
        items.add(new EntryItem("Grammar Rules", "Quick basic grammar for many rules."));
        items.add(new EntryItem("3000 Words", "3000 words most of common."));
        items.add(new SectionItem("About"));
        items.add(new EntryItem("Rate app", "Please rate my app if you like."));
        items.add(new EntryItem("More app", "More app from Smart Security Group. Enjoy!"));
        items.add(new EntryItem("Feedback", "Send feedback to Smart Security Group."));
    }
}
