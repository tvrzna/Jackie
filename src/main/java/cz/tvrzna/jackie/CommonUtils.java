package cz.tvrzna.jackie;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CommonUtils
{
	protected static final String DATE_FORMAT_JSON = "yyyy-MM-dd'T'HH:mm:ss";
	protected static final String DEFAULT_SEPARATOR = "\"";
	protected static final List<Class<?>> SIMPLE_CLASSES = Arrays.asList(String.class, Boolean.class, Byte.class, Character.class, Short.class, Integer.class, Long.class,
			Number.class, Float.class, Double.class, Date.class, BigDecimal.class, BigInteger.class, boolean.class, byte.class, char.class, short.class, int.class, long.class, float.class, double.class);
	protected static final List<Class<?>> PRIMITIVE_CLASSES = Arrays.asList(boolean.class, byte.class, char.class, short.class, int.class, long.class, float.class, double.class);


	/**
	 * The Enum ESCAPE_CHARACTERS.
	 *
	 * @author michalt
	 * @since 0.3.1
	 */
	protected enum ESCAPE_CHARACTERS {
		TAB("\\t", "\t"),
		CARRIAGE_RETURN("\\r", "\r"),
		NEW_LINE("\\n", "\n"),
		FORM_FEED("\\f", "\f"),
		BACKSPACE("\\b", "\b");

		private final String value;
		private final String character;

		private ESCAPE_CHARACTERS(String value, String character)
		{
			this.value = value;
			this.character = character;
		}

		public String getValue()
		{
			return value;
		}

		public String getCharacter()
		{
			return character;
		}
	}

	private CommonUtils()
	{
	}

	/**
	 * Gets the fields.
	 *
	 * @param clazz
	 *          the clazz
	 * @return the fields
	 */
	protected static List<Field> getFields(Class<?> clazz)
	{
		List<Field> result = new ArrayList<>();
		List<String> lstFieldNames = new ArrayList<>();

		Class<?> current = clazz;
		while (current.getSuperclass() != null && !SIMPLE_CLASSES.contains(current))
		{
			for (Field field : current.getDeclaredFields())
			{
				if (!lstFieldNames.contains(field.getName()) && !Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()))
				{
					result.add(field);
					lstFieldNames.add(field.getName());
				}
			}
			current = current.getSuperclass();
		}
		return result;
	}

	/**
	 * Convert array to primitive.
	 *
	 * @param <T>
	 *          the generic type
	 * @param list
	 *          the list
	 * @param clazz
	 *          the clazz
	 * @return the t
	 * @throws Exception
	 *           the exception
	 */
	@SuppressWarnings("unchecked")
	protected static <T> T convertArrayToPrimitive(List<?> list, Class<?> clazz) throws Exception
	{
		if (clazz.equals(boolean.class))
		{
			boolean[] arr = new boolean[list.size()];
			for (int i = 0; i < list.size(); i++)
			{
				Object o = list.get(i);
				arr[i] = o != null ? ((Boolean) o).booleanValue() : null;
			}
			return (T) arr;
		}
		else if (clazz.equals(byte.class))
		{
			byte[] arr = new byte[list.size()];
			for (int i = 0; i < list.size(); i++)
			{
				Object o = list.get(i);
				arr[i] = o != null ? ((Byte) o).byteValue() : null;
			}
			return (T) arr;
		}
		else if (clazz.equals(char.class))
		{
			char[] arr = new char[list.size()];
			for (int i = 0; i < list.size(); i++)
			{
				Object o = list.get(i);
				arr[i] = o != null ? ((Character) o).charValue() : null;
			}
			return (T) arr;
		}
		else if (clazz.equals(short.class))
		{
			short[] arr = new short[list.size()];
			for (int i = 0; i < list.size(); i++)
			{
				Object o = list.get(i);
				arr[i] = o != null ? ((Short) o).shortValue() : null;
			}
			return (T) arr;
		}
		else if (clazz.equals(int.class))
		{
			int[] arr = new int[list.size()];
			for (int i = 0; i < list.size(); i++)
			{
				Object o = list.get(i);
				arr[i] = o != null ? ((Integer) o).intValue() : null;
			}
			return (T) arr;
		}
		else if (clazz.equals(long.class))
		{
			long[] arr = new long[list.size()];
			for (int i = 0; i < list.size(); i++)
			{
				Object o = list.get(i);
				arr[i] = o != null ? ((Long) o).longValue() : null;
			}
			return (T) arr;
		}
		else if (clazz.equals(float.class))
		{
			float[] arr = new float[list.size()];
			for (int i = 0; i < list.size(); i++)
			{
				Object o = list.get(i);
				arr[i] = o != null ? ((Float) o).floatValue() : null;
			}
			return (T) arr;
		}
		else if (clazz.equals(double.class))
		{
			double[] arr = new double[list.size()];
			for (int i = 0; i < list.size(); i++)
			{
				Object o = list.get(i);
				arr[i] = o != null ? ((Double) o).doubleValue() : null;
			}
			return (T) arr;
		}
		throw new Exception("Unkown primitive type.");
	}

	@SuppressWarnings("unchecked")
	protected static <T> Object[] convertPrimitiveArrayToObjects(T array) throws Exception
	{
		Class<?> subclazz = array.getClass().getComponentType();

		List<Object> list = new ArrayList<>();
		if (subclazz.equals(boolean.class))
		{
			for (boolean v : (boolean[]) array)
			{
				list.add(Boolean.valueOf(v));
			}
		}
		else if (subclazz.equals(byte.class))
		{
			for (byte v : (byte[]) array)
			{
				list.add(Byte.valueOf(v));
			}
		}
		else if (subclazz.equals(char.class))
		{
			for (char v : (char[]) array)
			{
				list.add(Character.valueOf(v));
			}
		}
		else if (subclazz.equals(short.class))
		{
			for (short v : (short[]) array)
			{
				list.add(Short.valueOf(v));
			}
		}
		else if (subclazz.equals(int.class))
		{
			for (int v : (int[]) array)
			{
				list.add(Integer.valueOf(v));
			}
		}
		else if (subclazz.equals(long.class))
		{
			for (long v : (long[]) array)
			{
				list.add(Long.valueOf(v));
			}
		}
		else if (subclazz.equals(float.class))
		{
			for (float v : (float[]) array)
			{
				list.add(Float.valueOf(v));
			}
		}
		else if (subclazz.equals(double.class))
		{
			for (double v : (double[]) array)
			{
				list.add(Double.valueOf(v));
			}
		}
		return list.toArray();
	}

	/**
	 * String Builder replace.
	 *
	 * @param sb
	 *          the sb
	 * @param target
	 *          the target
	 * @param replacement
	 *          the replacement
	 */
	protected static void stringBuilderReplace(StringBuilder sb, String target, String replacement)
	{
		for (int i = 0; i < sb.length(); i++)
		{
			if (i + 1 >= sb.length())
			{
				break;
			}
			i = sb.indexOf(target, i);
			if (i < 0)
			{
				break;
			}
			sb.replace(i, i + target.length(), replacement);
			i = i - target.length() + replacement.length();
		}
	}

}
