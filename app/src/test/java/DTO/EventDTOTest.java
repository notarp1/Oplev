package DTO;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class EventDTOTest extends TestCase {
    EventDTO eventDTO = new EventDTO();
    int antal = 100000;

    public void testSetCoordinates() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            eventDTO.setCoordinates(random);
            assertEquals(random,eventDTO.getCoordinates());
        }
    }

    public void testSetEventId() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            eventDTO.setEventId(random);
            assertEquals(random,eventDTO.getEventId());
        }
    }

    public void testSetPrice() {
        for (int i = 0; i < antal; i++) {
            Random rand = new Random();
            int random = rand.nextInt(antal);
            eventDTO.setPrice(random);
            assertEquals(random,eventDTO.getPrice());
        }
    }

    public void testSetTitle() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            eventDTO.setTitle(random);
            assertEquals(random,eventDTO.getTitle());
        }
    }

    public void testSetCity() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            eventDTO.setCity(random);
            assertEquals(random,eventDTO.getCity());
        }
    }

    public void testSetDescription() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            eventDTO.setDescription(random);
            assertEquals(random,eventDTO.getDescription());
        }
    }

    public void testSetDate() {
        for (int i = 0; i < antal; i++) {
            Date random = new Date();
            eventDTO.setDate(random);
            assertEquals(random,eventDTO.getDate());
        }
    }

    public void testSetEventPic() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            eventDTO.setEventPic(random);
            assertEquals(random,eventDTO.getEventPic());
        }
    }

    public void testSetOwnerId() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            eventDTO.setOwnerId(random);
            assertEquals(random,eventDTO.getOwnerId());
        }
    }

    public void testSetParticipant() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            eventDTO.setParticipant(random);
            assertEquals(random,eventDTO.getParticipant());
        }
    }

    public void testSetApplicants() {
        ArrayList<String> applicants = new ArrayList<>();
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            applicants.add(random);
            eventDTO.setApplicants(applicants);
            assertEquals(applicants,eventDTO.getApplicants());
        }
    }

    public void testSetMaleOn() {
        for (int i = 0; i < antal; i++) {
            Random rand = new Random();
            boolean random = rand.nextBoolean();
            eventDTO.setMaleOn(random);
            assertEquals(random,eventDTO.isMaleOn());
        }
    }

    public void testSetFemaleOn() {
        for (int i = 0; i < antal; i++) {
            Random rand = new Random();
            boolean random = rand.nextBoolean();
            eventDTO.setFemaleOn(random);
            assertEquals(random,eventDTO.isFemaleOn());
        }
    }

    public void testSetMinAge() {
        for (int i = 0; i < antal; i++) {
            Random rand = new Random();
            int random = rand.nextInt(antal);
            eventDTO.setMinAge(random);
            assertEquals(random,eventDTO.getMinAge());
        }
    }

    public void testSetMaxAge() {
        for (int i = 0; i < antal; i++) {
            Random rand = new Random();
            int random = rand.nextInt(antal);
            eventDTO.setMaxAge(random);
            assertEquals(random,eventDTO.getMaxAge());
        }
    }

    public void testSetOwnerPic() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            eventDTO.setOwnerPic(random);
            assertEquals(random,eventDTO.getOwnerPic());
        }
    }

    public void testSetType() {
        for (int i = 0; i < antal; i++) {
            String random = UUID.randomUUID().toString();
            eventDTO.setType(random);
            assertEquals(random,eventDTO.getType());
        }
    }
}