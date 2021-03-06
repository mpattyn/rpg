package tests.universe.entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import universe.Position;
import universe.World;
import universe.desires.Verb;
import universe.entities.Item;
import universe.entities.NPC;
import universe.utils.DatabaseManager;

public class NPCTest {

    @Before
    public void setUp() {
        DatabaseManager.emptyEntities();
    }

    @Test
    public void testGetWorldKnowledge() {
        World w = new World(1, 2);
        NPC npc1 = (NPC) DatabaseManager.create(NPC.class, "Azu", 50);
        npc1.setWorld(w);
        npc1.generateDesires(Verb.OWN);
        npc1.setPosition(new Position(1, 1));
        Item item1 = (Item) DatabaseManager.create(Item.class, "Sword", 20,
                true);
        item1.setWorld(w);
        item1.setPosition(new Position(1, 2));

        // Azu doesn't know about the Sword
        Assert.assertFalse(npc1.knowsAbout(item1));
        // Sword knows about itself
        Assert.assertTrue(item1.knowsAbout(item1));

        // run 1 turn to make Azu learn things around him
        Thread t = new Thread(npc1);
        t.start();
        w.active = false;
        try {
            t.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        w.active = true;

        // Azu knows about the Sword
        Assert.assertTrue(npc1.knowsAbout(item1));
    }

    @Test
    public void testPickup() {
        World w = new World(1, 2);
        NPC npc1 = (NPC) DatabaseManager.create(NPC.class, "Azu", 50);
        npc1.setWorld(w);
        npc1.generateDesires(Verb.OWN);
        npc1.setPosition(new Position(1, 1));
        Item item1 = (Item) DatabaseManager.create(Item.class, "Sword", 20,
                true);
        item1.setWorld(w);
        item1.setPosition(new Position(1, 2));

        // Azu doesn't have the Sword
        Assert.assertFalse(npc1.got(item1));

        // run 1 turn to make Azu pick up the Sword
        Thread t = new Thread(npc1);
        t.start();
        w.active = false;
        try {
            t.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        w.active = true;

        // Azu now have the Sword
        Assert.assertTrue(npc1.got(item1));
    }

    @Test
    public void testMovePickup() {
        World w = new World(3, 5);
        NPC npc1 = (NPC) DatabaseManager.create(NPC.class, "Azu", 50);
        npc1.setWorld(w);
        npc1.generateDesires(Verb.OWN);
        npc1.setPosition(new Position(1, 1));
        Item item1 = (Item) DatabaseManager.create(Item.class, "Sword", 20,
                true);
        item1.setWorld(w);
        item1.setPosition(new Position(3, 5));

        // Azu doesn't have the Sword and is away from it
        Assert.assertFalse(npc1.got(item1));
        Assert.assertFalse(npc1.getActionZone().contain(item1.getPosition()));

        // run 1 turn to make Azu move
        Thread t = new Thread(npc1);
        t.start();
        w.active = false;
        try {
            t.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        w.active = true;

        // Azu still doesn't have the Sword but is near it
        Assert.assertFalse(npc1.got(item1));
        Assert.assertTrue(npc1.getActionZone().contain(item1.getPosition()));

        // run another turn to make Azu pick up the Sword
        t = new Thread(npc1);
        t.start();
        w.active = false;
        try {
            t.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        w.active = true;

        // Azu now have the Sword
        Assert.assertTrue(npc1.got(item1));
    }

    @Test
    public void testLearn() {
        World w = new World(3, 5);
        NPC npc1 = (NPC) DatabaseManager.create(NPC.class, "Azu", 50);
        npc1.setWorld(w);
        npc1.generateDesires(Verb.LEARN);
        npc1.setPosition(new Position(1, 1));

        NPC npc2 = (NPC) DatabaseManager.create(NPC.class, "Pierre", 50);
        npc2.setWorld(w);
        npc2.setPosition(new Position(3, 5));

        Assert.assertFalse(npc1.knowsAbout(npc2));

        Thread t = new Thread(npc1);
        t.start();
        w.active = false;
        try {
            t.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        w.active = true;

        Assert.assertTrue(npc1.knowsAbout(npc2));
        Assert.assertFalse(npc1.knowsAll(npc2.getKnowledges()));

        // multiple turns because discuss return a random knowledge, and he
        // wants to know everything
        t = new Thread(npc1);
        t.start();

        try {
            Thread.sleep(6000);
        }
        catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        w.active = false;
        try {
            t.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        w.active = true;

        Assert.assertTrue(npc1.knowsAll(npc2.getKnowledges()));
    }
}
