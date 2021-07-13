package cz.tvrzna.jackie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JackieTest
{

	public static class TestClass
	{
		protected long id;
		protected String name;
		protected List<TestClass> children;
		protected Map<String, TestClass> mapChildren;
	}

	@Test
	public void fromJsonToStringArray()
	{
		String json = "[\"value1\", 'value2', value3]";
		String[] arr = new Jackie().fromJson(json, String[].class);
		Assertions.assertNotNull(arr);
		Assertions.assertEquals(3, arr.length);
	}

	@Test
	public void fromJsonToIntegerArray()
	{
		String json = "[0, 1, \"2\", '3']";
		Integer[] arr = new Jackie().fromJson(json, Integer[].class);
		Assertions.assertNotNull(arr);
		Assertions.assertEquals(4, arr.length);
	}

	@Test
	public void fromJsonToBoolArray()
	{
		String json = "[true, false]";
		boolean[] arr = new Jackie().fromJson(json, boolean[].class);
		Assertions.assertNotNull(arr);
		Assertions.assertEquals(2, arr.length);
	}

	@Test
	public void fromJsonToHashMap()
	{
		String json = "{0: \"hello\", 1: \"bye\"}";
		Map<Integer, String> map = new Jackie().fromJson(json, Map.class, Integer.class, String.class);
		Assertions.assertEquals(2, map.size());
		Assertions.assertEquals("bye", map.get(1));
	}

	@Test
	public void fromJsonToObject()
	{
		String json = "{id: 20200327, 'name': \"SomeDopeName\", 'children': [{id: 1, name: 'help1', children: [{}]}, {id: 2, name: 'help2'}, {id: 3}], 'mapChildren': {'abc': {id: 4, name: 'help4', children: [{id: 5}]}, 'xyz': {id: 6}}}";
		TestClass obj = new Jackie().fromJson(json, TestClass.class);
		Assertions.assertNotNull(obj);
		Assertions.assertEquals(20200327l, obj.id);
		Assertions.assertEquals("SomeDopeName", obj.name);
		Assertions.assertEquals(3, obj.children.size());
		Assertions.assertNotNull(obj.children.get(2));
		Assertions.assertEquals(6, obj.mapChildren.get("xyz").id);
		Assertions.assertEquals(5, obj.mapChildren.get("abc").children.get(0).id);
	}

	@Test
	public void toJsonFromStringArray()
	{
		String expected = "[\"hello\",\"there\"]";
		String[] arr = new String[]
		{ "hello", "there" };
		Assertions.assertEquals(expected, new Jackie().toJson(arr));
	}

	@Test
	public void toJsonFromByteArray()
	{
		String expected = "[3,6,22,50]";
		byte[] arr = new byte[]
		{ 3, 6, 22, 50 };
		Assertions.assertEquals(expected, new Jackie().toJson(arr));
	}

	@Test
	public void toJsonFromChars()
	{
		String expected = "[\"a\",\"ࢥ\",\"y\"]";
		char[] arr = new char[]
		{ 'a', 2213, 'y' };
		Assertions.assertEquals(expected, new Jackie().toJson(arr));
	}

	@Test
	public void toJsonFromObject()
	{
		String expected = "{\"id\":20200327,\"name\":\"SomeDopeName\",\"children\":[{\"id\":1,\"name\":\"children\"}],\"mapChildren\":{\"first\":{\"id\":0},\"second\":{\"id\":2,\"name\":\"children-name\"}}}";
		TestClass obj = new TestClass();
		obj.id = 20200327l;
		obj.name = "SomeDopeName";
		obj.children = new ArrayList<>();
		obj.children.add(new TestClass());
		obj.children.get(0).id = 1l;
		obj.children.get(0).name = "children";
		obj.mapChildren = new HashMap<>();
		obj.mapChildren.put("first", new TestClass());
		obj.mapChildren.put("second", new TestClass());
		obj.mapChildren.get("second").id = 2l;
		obj.mapChildren.get("second").name = "children-name";
		Assertions.assertEquals(expected, new Jackie().toJson(obj));
	}

	@Test
	public void toJsonFromMapWithIntegerKeys() {
		String expected = "{\"child2\":{\"false\":\"nay\",\"true\":\"yay\"},\"child1\":{\"0\":\"hello\",\"1\":\"cya\"}}";

		Map<String, Object> parent = new HashMap<>();
		Map<Integer, String> child1 = new HashMap<>();
		child1.put(0, "hello");
		child1.put(1, "cya");
		parent.put("child1", child1);

		Map<Boolean, String> child2 = new HashMap<>();
		child2.put(Boolean.FALSE, "nay");
		child2.put(Boolean.TRUE, "yay");
		parent.put("child2", child2);

		Assertions.assertEquals(expected, new Jackie().toJson(parent));
	}

}
