package cz.tvrzna.jackie;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class SerializationMapper.
 *
 * @author michalt
 */
public class SerializationMapper
{

	private SerializationMapper()
	{
	}

	/**
	 * Converts <code>object</code> to <code>Map</code> or <code>List</code>.
	 *
	 * @param <T>
	 *          the generic type
	 * @param object
	 *          the object
	 * @return the object
	 * @throws Exception
	 *           the exception
	 */
	protected static <T> Object convertFromObject(T object) throws Exception
	{
		if (object == null)
		{
			return null;
		}
		else if (((CommonUtils.SIMPLE_CLASSES.contains(object.getClass()) || Enum.class.isAssignableFrom(object.getClass())) && !object.getClass().isArray()) ||
				Map.class.isAssignableFrom(object.getClass()))
		{
			return object;
		}
		else if (Collection.class.isAssignableFrom(object.getClass()))
		{
			return processArray(((Collection<?>) object).toArray());
		}
		else if (object.getClass().isArray())
		{
			if (CommonUtils.PRIMITIVE_CLASSES.contains(object.getClass().getComponentType()))
			{
				return processArray(CommonUtils.convertPrimitiveArrayToObjects(object));
			}
			return processArray((Object[]) object);
		}
		else
		{
			return processObject(object);
		}
	}

	/**
	 * Process <code>object</code> and its each field, that is not final nor
	 * static.
	 *
	 * @param <T>
	 *          the generic type
	 * @param object
	 *          the object
	 * @return the map
	 * @throws Exception
	 *           the exception
	 */
	private static <T> Map<String, Object> processObject(T object) throws Exception
	{
		Map<String, Object> result = new LinkedHashMap<>();

		for (Field field : CommonUtils.getFields(object.getClass()))
		{
			processField(object, result, field);
		}

		return result;
	}

	/**
	 * Process field in <code>object</code> and puts the value into
	 * <code>result</code> map.
	 *
	 * @param <T>
	 *          the generic type
	 * @param object
	 *          the object
	 * @param result
	 *          the result
	 * @param field
	 *          the field
	 * @throws Exception
	 *           the exception
	 */
	private static <T> void processField(T object, Map<String, Object> result, Field field) throws Exception
	{
		field.setAccessible(true);
		Object value = field.get(object);
		if (null != value)
		{
			result.put(field.getName(), convertFromObject(value));
		}
	}

	/**
	 * Handle array.
	 *
	 * @param array
	 *          the array
	 * @return the list
	 * @throws Exception
	 *           the exception
	 */
	private static List<Object> processArray(Object[] array) throws Exception
	{
		List<Object> result = new ArrayList<>();
		for (Object obj : array)
		{
			result.add(convertFromObject(obj));
		}
		return result;
	}

}
