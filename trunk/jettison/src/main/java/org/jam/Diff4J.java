package org.jam;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jam.utils.SetUtils;

@SuppressWarnings("unchecked")
public class Diff4J {
	static {
		Properties props = new Properties();
		try {
			props.load(ClassLoader
					.getSystemResourceAsStream("log4j.properties"));
			PropertyConfigurator.configure(props);
		} catch (IOException e) {
			System.out.println("Could not configure log4j");
			e.printStackTrace();
		}
	}

	private static final Logger logger = Logger.getLogger(Diff4J.class);
	private static int depth = -1;

	private Set visited = new HashSet();

	private Collection<ChangeInfo> changes = new LinkedList<ChangeInfo>();

	public Collection<ChangeInfo> diff(Object left, Object right) {
		handleObject(
				new TypeInfo(left, left.getClass(), null, null),
				new TypeInfo(right, right.getClass(), null, null));
		return changes;
	}

	static class TypeInfo {
		Object obj;
		Class<?> clz;
		Object parent;
		Field field;

		TypeInfo(Object obj, Class<?> clz, Object parent, Field field) {
			this.obj = obj;
			this.clz = clz;
			this.parent = parent;
			this.field = field;
		}
	}

	private void handleObject(TypeInfo left, TypeInfo right) {
		++depth;
		try {
			if (!visited.contains(left.obj)) {
				visited.add(left.obj);
				try {
					if (isPrimitiveOrWrapper(left.clz)) {
						handlePrimitive(left, right);
					} else if (isCollection(left.clz)) {
						handleCollection(left, right);
					} else if (isMap(left.clz)) {
						handleMap(left, right);
					} else {
						handleClass(left, right);
					}
				} finally {
					visited.remove(left.obj);
				}
			}
		} finally {
			--depth;
		}
	}

	private void handleClass(TypeInfo left, TypeInfo right) {
		if (logger.isDebugEnabled())
			logger.debug(getTabs() + "Class: " + left.clz.getName());

		Field[] fields;

		if (left.obj != null) {
			Class<?> clz = left.clz;

			while (clz != Object.class) {
				fields = clz.getDeclaredFields();

				for (Field field : fields) {
					int modifiers = field.getModifiers();
					if (!(Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers))) {
						Object fieldObj1 = getObject(field, left.obj);
						Object fieldObj2 = getObject(field, right.obj);
						handleObject(
								new TypeInfo(fieldObj1, field.getType(), left.obj, field), 
								new TypeInfo(fieldObj2, field.getType(), right.obj, field));
					}
				}

				clz = clz.getSuperclass();
			}
		}
	}

	private static Object getObject(Field field, Object parent) {
		try {
			try {
				return field.get(parent);
			} catch (IllegalAccessException e) {
				field.setAccessible(true);
				return field.get(parent);
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private void handleMap(TypeInfo info, TypeInfo right) {
		// what to do w/ a map?
		// if(info.obj != null) {
		// Map<?, ?> map = (Map<?, ?>) info.obj;
		//            
		// for(Map.Entry<?, ?> entry : map.entrySet())
		// handleObject(entry.getValue());
		// }
	}

	private void handleCollection(TypeInfo left, TypeInfo right) {
		if (logger.isDebugEnabled())
			logger.debug(getTabs() + "Collection: " + left.field.getName());

		Class<?> colType = getParameterizedType(left.field);
		Class<?> parentType = left.parent.getClass();
		
		Collection col1 = (Collection) left.obj;
		Collection col2 = (Collection) right.obj;

		Collection<?> disjoint = SetUtils.disjoint(col1, col2);

		//process the differences
		for (Object obj : disjoint) {
			ChangeInfo change = new ChangeInfo();
			change.setFieldName(left.field.getName());
			change.setEnclosingType(parentType);

			if (col1.contains(obj)) {
				change.setFrom(obj);
				change.setChangeType(ChangeType.REMOVE);
			} else {
				change.setTo(obj);
				change.setChangeType(ChangeType.ADD);
			}

			changes.add(change);
		}

		Map<Integer, ?> col2Map = SetUtils.getIdentityMap(col2);
		
		Collection<?> intersection = SetUtils.intersection(col1, col2);

		//examine the commnon objects...
		for (Object leftObj : intersection) {
			Object rightObj = col2Map.get(leftObj.hashCode());
			handleObject(
					new TypeInfo(leftObj, colType, null, null),
					new TypeInfo(rightObj, colType, null, null));
		}
	}

	public static Class<?> getParameterizedType(Field field) {
		Type type = field.getGenericType();

		if (type instanceof ParameterizedType) {
			ParameterizedType pType = (ParameterizedType) type;
			Type[] typeArgs = pType.getActualTypeArguments();

			if (typeArgs != null 
					&& typeArgs.length > 0
					&& typeArgs[0] instanceof Class<?>)
				return (Class<?>) typeArgs[0];
		}
		return null;
	}

	private void handlePrimitive(TypeInfo left, TypeInfo right) {
		if (logger.isDebugEnabled()) {
			StringBuilder output = new StringBuilder();

			output.append(getTabs()).
				append("Field: ").
				append(left.field.getName()).
				append(" Value: ").
				append(left.obj.toString());

			logger.debug(output.toString());
		}

		ChangeInfo change = null;
		
		Class<?> parentType = left.parent.getClass();

		if (left.obj == null) {
			if (right.obj != null) {
				change = new ChangeInfo();
				change.setChangeType(ChangeType.ADD);
				change.setTo(right.obj);
				change.setFieldName(left.field.getName());
				change.setEnclosingType(parentType);
			}
		} else if (right.obj != null) {
			if (!left.obj.equals(right.obj)) {
				change = new ChangeInfo();
				change.setChangeType(ChangeType.CHANGE);
				change.setFrom(left.obj);
				change.setTo(right.obj);
				change.setFieldName(left.field.getName());
				change.setEnclosingType(parentType);
			}
		} else {
			change = new ChangeInfo();
			change.setChangeType(ChangeType.REMOVE);
			change.setFrom(left.obj);
			change.setFieldName(left.field.getName());
			change.setEnclosingType(parentType);
		}

		if (change != null)
			changes.add(change);
	}

	private static String getTabs() {
		StringBuilder tabs = new StringBuilder();
		for (int i = 0; i < depth; ++i)
			tabs.append('\t');
		return tabs.toString();
	}

	private static boolean isPrimitiveOrWrapper(Class<?> clz) {
		return clz.isPrimitive() 
				|| Number.class.isAssignableFrom(clz)
				|| Boolean.class.isAssignableFrom(clz)
				|| Character.class.isAssignableFrom(clz)
				|| String.class.isAssignableFrom(clz);
	}

	private static boolean isCollection(Class<?> clz) {
		return Collection.class.isAssignableFrom(clz);
	}

	private static boolean isMap(Class<?> clz) {
		return Map.class.isAssignableFrom(clz);
	}
}
