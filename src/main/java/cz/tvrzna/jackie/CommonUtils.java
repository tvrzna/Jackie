package cz.tvrzna.jackie;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CommonUtils
{
	protected static final String DATE_FORMAT_JSON = "yyyy-MM-dd'T'HH:mm:ss";
	protected static final String DEFAULT_SEPARATOR = "\"";
	protected static final List<Class<?>> SIMPLE_CLASSES = Arrays.asList(String.class, Boolean.class, Byte.class, Short.class, Integer.class, Long.class, Number.class, Float.class,
			Double.class, Date.class, boolean.class, short.class, int.class, long.class, float.class, double.class);
	protected static final List<Class<?>> PRIMITIVE_CLASSES = Arrays.asList(boolean.class, byte.class, short.class, int.class, long.class, float.class, double.class);

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
	 * @param <T> the generic type
	 * @param list the list
	 * @param clazz the clazz
	 * @return the t
	 * @throws Exception the exception
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

}
