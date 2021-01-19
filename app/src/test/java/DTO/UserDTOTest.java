package DTO;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class UserDTOTest extends TestCase {
    UserDTO userDTO = new UserDTO();
    int antal = 100000;

    public void testSetUserId() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            userDTO.setUserId(random);
            assertEquals(random,userDTO.getUserId());
        }
    }

    public void testSetAge() {
        for (int i = 0; i < antal; i++) {
            Random rand = new Random();
            int random = rand.nextInt(antal);
            userDTO.setAge(random);
            assertEquals(random,userDTO.getAge());
        }
    }

    public void testSetDescription() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            userDTO.setDescription(random);
            assertEquals(random,userDTO.getDescription());
        }
    }

    public void testSetJob() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            userDTO.setJob(random);
            assertEquals(random,userDTO.getJob());
        }
    }

    public void testSetEducation() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            userDTO.setEducation(random);
            assertEquals(random,userDTO.getEducation());
        }
    }

    public void testSetUserPicture() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            userDTO.setUserPicture(random);
            assertEquals(random,userDTO.getUserPicture());
        }
    }

    public void testSetfName() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            userDTO.setfName(random);
            assertEquals(random,userDTO.getfName());
        }
    }

    public void testSetlName() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            userDTO.setlName(random);
            assertEquals(random,userDTO.getlName());
        }
    }

    public void testSetCity() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            userDTO.setCity(random);
            assertEquals(random,userDTO.getCity());
        }
    }

    public void testSetEmail() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            userDTO.setEmail(random);
            assertEquals(random,userDTO.getEmail());
        }
    }

    public void testSetChatId() {
        ArrayList<String> chatids = new ArrayList<>();
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            chatids.add(random);
            userDTO.setChatId(chatids);
            assertEquals(chatids,userDTO.getChatId());
        }
    }

    public void testSetPictures() {
        ArrayList<String> pictures = new ArrayList<>();
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            pictures.add(random);
            userDTO.setPictures(pictures);
            assertEquals(pictures,userDTO.getPictures());
        }
    }

    public void testSetEvents() {
        ArrayList<String> events = new ArrayList<>();
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            events.add(random);
            userDTO.setEvents(events);
            assertEquals(events,userDTO.getEvents());
        }
    }

    public void testSetRequestedEvents() {
        ArrayList<String> events = new ArrayList<>();
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            events.add(random);
            userDTO.setRequestedEvents(events);
            assertEquals(events,userDTO.getRequestedEvents());
        }
    }

    public void testSetLikedeEvents() {
        ArrayList<String> events = new ArrayList<>();
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            events.add(random);
            userDTO.setLikedeEvents(events);
            assertEquals(events,userDTO.getLikedeEvents());
        }
    }
}