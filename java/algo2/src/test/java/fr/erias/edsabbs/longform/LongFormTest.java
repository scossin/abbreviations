package fr.erias.edsabbs.longform;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.HashSet;

import org.junit.Test;

import fr.erias.edsabbs.graph.Candidate;

public class LongFormTest {

//	@Test
//	public void IsStopwordTest() throws IOException {
//		HashSet<Candidate> set = new HashSet<Candidate>();
//		Candidate tf1 = new Candidate("idm",1);
//		Candidate tf2 = new Candidate("idm",2);
//		set.add(tf1);
//		assertTrue(set.contains(tf2)); // be true since frequency is disregarded 
//		set.add(tf2);
//		assertEquals(1, set.size()); // expects 1 element because TF overrides the equals and hashCode methods
//		assertEquals(1, set.iterator().next().getFreq()); // 1 since tf2 is not added since it already contains tf1
//		assertTrue(tf1.equals(tf2));
//	}
}
