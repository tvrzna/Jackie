package cz.tvrzna.jackie;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import cz.tvrzna.jackie.JackieTest.TestClass;


public class BenchmarkTest
{

	@Test
	public void serializationSpeed()
	{
		final String json = "{\"id\":345345,\"bigDecimal\":10,\"children\":[{\"id\":3434534,\"name\":\"\\\"Some k\\\"ind\\\" of \\n unkown \\n name \\t \\r.\"}]}";

		long start = new Date().getTime();
		Jackie j = new Jackie();

		TestClass t = new TestClass();
		t.id = 345345l;
		t.bigDecimal = BigDecimal.TEN;
		t.children = new ArrayList<>();
		t.children.add(new TestClass());
		t.children.get(0).id = 3434534l;
		t.children.get(0).name = "\"Some k\"ind\" of \n unkown \n name \t \r.";


		for (int i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++) {
			j.toJson(t);
		}

		System.out.format("Serialization took %d ms\n", new Date().getTime() - start);

		Assertions.assertEquals(json, j.toJson(t));
	}


	@Test
	public void deserializationSpeed()
	{
		long start = new Date().getTime();
		Jackie j = new Jackie();

		final String json = "{\"id\":345345,\"bigDecimal\":10.0,\"children\":[{\"id\":3434534,\"name\":\"\\\"Some k\\\"ind\\\" of \\n unkown \\n name \\t \\r.\"}]}";

		TestClass t = null;

		for (int i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++) {
			t = j.fromJson(json, TestClass.class);
		}

		System.out.format("Deserialization took %d ms\n", new Date().getTime() - start);

		Assertions.assertEquals(json, j.toJson(t));
	}
}
