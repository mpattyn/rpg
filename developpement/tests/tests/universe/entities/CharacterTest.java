package tests.universe.entities;

import org.junit.Assert;
import org.junit.Test;

import universe.World;
import universe.beliefs.Fact;
import universe.entities.Character;
import universe.entities.Item;

public class CharacterTest {

    @Test
    public void create() {
	World w = new World(2, 4);
	Character roger = new Character(w, "Roger", 14);
	Character robert = new Character(w, "Robert", 8);
	Fact f = new Fact("Roger is a dragqueen");
	Fact g = new Fact("Robert is doing drugs");
	Fact h = new Fact("Life is amazing");
	Fact e = new Fact("Life sux");
	roger.addKnowledge(f);
	roger.addKnowledge(g);
	roger.addKnowledge(e);
	robert.addKnowledge(h);
	Item matchbox = new Item(w, "Match box", 50, true);
	Item match = new Item(w, "Match", 1, true);
	Item spoon = new Item(w, "Spoon", 200, true);
	matchbox.addItem(match);
	robert.addItem(spoon);
	roger.addItem(matchbox);
	System.out.println(w);
	Assert.assertTrue(roger.getKnowledges().contains(f));
	Assert.assertTrue(roger.getKnowledges().contains(g));
	Assert.assertTrue(roger.getKnowledges().contains(e));
	Assert.assertTrue(robert.getKnowledges().contains(h));
    }
}