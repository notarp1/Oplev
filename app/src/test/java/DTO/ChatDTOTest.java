package DTO;

import android.net.Uri;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class ChatDTOTest extends TestCase {
    ChatDTO chatDTO = new ChatDTO();
    int antal = 100000;

    public void testSetChatId() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            chatDTO.setChatId(random);
            assertEquals(random,chatDTO.getChatId());
        }
    }

    public void testSetDates() {
        ArrayList<Date> datoer = new ArrayList<>();
        for (int i = 0; i < antal; i++) {
            datoer.add(new Date());
        }
        chatDTO.setDates(datoer);
        assertEquals(datoer,chatDTO.getDates());
    }

    public void testSetMessages() {
        ArrayList<String> randomWords = new ArrayList<>();
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            randomWords.add(random);
            chatDTO.setMessages(randomWords);
            assertEquals(randomWords,chatDTO.getMessages());
        }
    }

    public void testSetSender() {
        ArrayList<String> randomSenders = new ArrayList<>();
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            randomSenders.add(random);
            chatDTO.setSender(randomSenders);
            assertEquals(randomSenders,chatDTO.getSender());
        }
    }

    public void testSetReceiver() {
        ArrayList<String> randomReceiver = new ArrayList<>();
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            randomReceiver.add(random);
            chatDTO.setReceiver(randomReceiver);
            assertEquals(randomReceiver,chatDTO.getReceiver());
        }
    }

    public void testSetEventID() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            chatDTO.setEventId(random);
            assertEquals(random,chatDTO.getEventId());
        }
    }

    public void testSetPictures() {
        Uri[] uris = {Uri.parse("https://graph.facebook.com/v5.0/4185160998179724/picture?height=200&width=200&migration_overrides=%7Boctober_2012%3Atrue%7D&access_token=EAAWaq21nRlkBACrIAMl5aQaF6LR20eelo2cPwELBeVPm87E6XaGOCw88ZBZAxlBmsCNbZBYnfZCi18SnoXvUtm0Ef33emzmmckH2iJYzHqBlvDhlD88z6rCVigSsFfpBrcnYbNd2NZA73y0eSgkOylgmOSM61YYuNBlEPzJJgl5wEX45cWHokYChztU4obqd63hzct706zGSCpabwIYPXmZApOXVd3bRiQkiGPsr3A2ZC70cggQGwoH"), Uri.parse("https://firebasestorage.googleapis.com/v0/b/opleva4.appspot.com/o/users%2FBsaBzk6GkChVFUMyxtYPojHJ1GW2%2F1?alt=media&token=b0c2bfde-7ba0-4523-afa6-0b82288262c0"), Uri.parse("https://firebasestorage.googleapis.com/v0/b/opleva4.appspot.com/o/users%2FDCa3ifGfRoUhoeenjMuNojn3HxS2%2F0?alt=media&token=f586ee9f-cf51-4f51-9bf2-2e3960575792"), Uri.parse("https://firebasestorage.googleapis.com/v0/b/opleva4.appspot.com/o/users%2FBsaBzk6GkChVFUMyxtYPojHJ1GW2%2F0?alt=media&token=a8c50f45-4841-417f-a770-194135594853"), Uri.parse("https://firebasestorage.googleapis.com/v0/b/opleva4.appspot.com/o/users%2FXhmsZYQTHFQcyHjxTjk5rHTNZCS2%2F1?alt=media&token=90ea2e62-2881-40c6-96d3-1d4cd261c0a5")};
        ArrayList<Uri> randomPictures = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < antal; i++) {
            randomPictures.add(uris[random.nextInt(uris.length-1)]);
            chatDTO.setPics(randomPictures);
            assertEquals(randomPictures,chatDTO.getPictures());
        }
    }

    public void testSetHeader() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            chatDTO.setHeader(random);
            assertEquals(random,chatDTO.getHeader());
        }
    }

    public void testSetUser1() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            chatDTO.setUser1(random);
            assertEquals(random,chatDTO.getUser1());
        }
    }

    public void testSetUser2() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            chatDTO.setUser2(random);
            assertEquals(random,chatDTO.getUser2());
        }
    }

    public void testSetUser1ID() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            chatDTO.setUser1ID(random);
            assertEquals(random,chatDTO.getUser1ID());
        }
    }

    public void testSetUser2ID() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            chatDTO.setUser2ID(random);
            assertEquals(random,chatDTO.getUser2ID());
        }
    }
}