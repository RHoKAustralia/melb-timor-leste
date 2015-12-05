package org.rhok.linguist.util;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.rhok.linguist.R;
import org.rhok.linguist.application.LinguistApplication;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;



public class Reflect {

	
	public static Object getValue(Object object, String name) {
		return getValue(object, name, false);
	}

	public static Object getValue(Object object, String name,
			boolean lookSuperClass) {

		Field field = null;
		Method method = null;
		Object value = null;

		Class<?> cls;

		if (object instanceof Class)
			cls = (Class<?>) object;
		else
			cls = object.getClass();

		while (cls != null) {

			try {
				field = cls.getDeclaredField(name);
			} catch (SecurityException e1) {
			} catch (NoSuchFieldException e1) {
			}

			if (field != null) {
				try {
					value = field.get(object);
					break;
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				}
			}

			try {
				method = cls.getDeclaredMethod("get" + name, (Class[]) null);
			} catch (SecurityException e) {
			} catch (NoSuchMethodException e) {
			}
			if (method != null) {
				try {
					value = method.invoke(object, (Object[]) null);
					break;
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e) {
				}
			}

			if (lookSuperClass)
				cls = cls.getSuperclass();
			else
				cls = null;
		}

		return value;
	}
	public static boolean setValue(Object object, String name, Object value){
		return setValue(object, name, value, false);
	}
	public static boolean setValue(Object object, String name, Object value,
								  boolean lookSuperClass) {

		Field field = null;
		Method method = null;

		Class<?> cls;

		if (object instanceof Class)
			cls = (Class<?>) object;
		else
			cls = object.getClass();

		while (cls != null) {

			try {
				field = cls.getDeclaredField(name);
			} catch (SecurityException e1) {
			} catch (NoSuchFieldException e1) {
			}

			if (field != null) {
				try {
					field.set(object, value);
					return true;
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				}
			}

			if(value!=null) try {
				method = cls.getDeclaredMethod("set" + name, value.getClass());
			} catch (SecurityException e) {
			} catch (NoSuchMethodException e) {
			}
			if (method != null) {
				try {
					 method.invoke(object, value);
					return true;
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e) {
				}
			}

			if (lookSuperClass)
				cls = cls.getSuperclass();
			else
				cls = null;
		}

		return false;
	}
	/**
	 * Get resource id from specified name.
	 * 
	 * @param fieldName
	 *            The resource ID but using string format.
	 * @param cls
	 *            Class to get the declared field from.
	 * @return The resource ID from field's name, or 0 if not found
	 */
	public static int getResId(String fieldName, Class<?> cls) {
		if(StringUtils.isNullOrEmpty(fieldName)) return 0;
		if(fieldName.startsWith("res/")){
			//eg res/xml/user_settings_screen_general.xml
			String split[] = fieldName.split("/");
			fieldName = split[split.length-1];
			fieldName = fieldName.indexOf(".") == -1 ? fieldName : fieldName
					.substring(0, fieldName.indexOf("."));
		}
		Object value = getValue(cls, fieldName);
		
		if (value == null || !(value instanceof Integer))
			return 0;
		return (Integer) value;
		/**
		 * Note: could also use Resources.getIdentifier - but this requires a context.
		 */
	}
	
	/**
	 * Lookup a resource string with the same name as resId
	 * @param resId any resource ID with the same name as a resource string
	 * @return the string associated with that name
	 * @throws NotFoundException
	 */
	public static String stringForId(int resId) throws NotFoundException{
		int stringId= 0;
		String resourceName = null;
		try{
			Context c = LinguistApplication.getContextStatic();
			resourceName = c.getResources().getResourceEntryName(resId);
			stringId= Reflect.getResId(resourceName, R.string.class);
			return c.getString(stringId);			
		
		}
		catch(NotFoundException e){
			if(resourceName==null){
				throw new NotFoundException("Couldn't find name for resource "+resId);
			}
			else{
				throw new NotFoundException("Couldn't find string named "+resourceName);
			}
		}
	}

	/**
	 * Get drawable resource id by name.
	 * 
	 * @param imageName
	 * @return
	 */
	public static int getImageResId(String imageName) {

		if (imageName == null || imageName.length() == 0)
			return 0;

		imageName = imageName.indexOf(".") == -1 ? imageName : imageName
				.substring(0, imageName.indexOf("."));

        return getResId(imageName.toLowerCase(), R.drawable.class);
        /*
		try {
			return getResId(imageName.toLowerCase(), Class.forName(EQPayApplication.getContextStatic().getPackageName()+".R$drawable"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
*/
	}

	/**
	 * Invokes a method from the object.
	 * 
	 * @param object
	 * @param selector
	 * @param parameterClasses
	 * @param parameterValues
	 * @return
	 */
	public static Object invoke(Object object, String selector,
			Class<?>[] parameterClasses, Object[] parameterValues) {

		if (selector == null || selector.length() == 0)
			return null;

		try {
			Method method = object.getClass().getMethod(selector,
					parameterClasses);
			return method.invoke(object, parameterValues);
		} catch (NoSuchMethodException e) {
		} catch (InvocationTargetException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}

		return null;

	}
	
	public static Object invoke(Object object, String selector) {
		return invoke(object, selector, (Class[]) null,
		(Object[]) null);
	}
	


	public static <T> void writeToParcel(Class<T> modelClass, T instance, Parcel dest, int flags) {
		if(instance instanceof List){
			dest.writeList((List<?>) instance);
		}
		try{
			for(Field f : modelClass.getDeclaredFields()){
				//Class<?> fieldClass = f.getType();
				int modifiers = f.getModifiers();
				if(Modifier.isStatic(modifiers) 
						|| Modifier.isFinal(modifiers) 													
						|| Modifier.isSynchronized(modifiers)
						//|| Modifier.isPublic(modifiers)
						|| Modifier.isVolatile(modifiers)
						|| Modifier.isTransient(modifiers)){					
					continue;
				}
				boolean changed=false;
				if(!f.isAccessible()){
					f.setAccessible(true);
					changed=true;				
				}
				if(List.class.isAssignableFrom(f.getType())){
					dest.writeList((List<?>) f.get(instance));
				}
				else{
					dest.writeValue(f.get(instance));
				}
				
				if(changed){
					f.setAccessible(false);
				}
			}
			}
			catch(Exception e){
				Log.e("Parcel", "Write to parcel error: ",e);
			}	
	}
	


	public static <T> void readFromParcel(Class<T> modelClass, T instance, Parcel in) {
		Field f=null;
		if(instance instanceof List){
			in.readList((List<?>) instance, modelClass.getClassLoader());
		}
		try{
			for( int i=0;i< modelClass.getDeclaredFields().length;i++){
				f = modelClass.getDeclaredFields()[i];
				//Class<?> fieldClass = f.getType();
				int modifiers = f.getModifiers();
				if(Modifier.isStatic(modifiers) 
						|| Modifier.isFinal(modifiers) 													
						|| Modifier.isSynchronized(modifiers)
						//|| Modifier.isPublic(modifiers)
						|| Modifier.isVolatile(modifiers)
						|| Modifier.isTransient(modifiers)){					
					continue;
				}
				boolean changed=false;
				if(!f.isAccessible()){
					f.setAccessible(true);
					changed=true;				
				}
				if(List.class.isAssignableFrom(f.getType())){
					List<?> newList = (List<?>) f.getType().getConstructor((Class[])null).newInstance();
					f.set(instance, newList);
					in.readList((List<?>) newList, modelClass.getClassLoader());
				}
				else{
					f.set(instance, in.readValue(modelClass.getClassLoader()));
				}
				
				if(changed){
					f.setAccessible(false);
				}
				
			}
		}
		catch(Exception e){
			Log.e("Parcel", "Read from parcel error: "+f,e);
		}
	}
    public static byte[] parcelableToBytes(Parcelable parceable) {
        Parcel parcel = Parcel.obtain();
        parceable.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle(); // not sure if needed or a good idea
        return bytes;
    }

    public static Parcel bytesToParcel(byte[] bytes) {
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0); // this is extremely important!
        return parcel;
    }

    public static <T> T bytesToParcelable(byte[] bytes, Parcelable.Creator<T> creator) {
        Parcel parcel = bytesToParcel(bytes);
        return creator.createFromParcel(parcel);
    }
    @SuppressWarnings("unchecked")
    public static <T> T cloneUsingParcel(T in){
        Parcel parcel =Parcel.obtain();
        parcel.writeValue(in);
        byte[] bytes = parcel.marshall();
        Parcel unParcel = bytesToParcel(bytes);
        T copy = (T) unParcel.readValue(in.getClass().getClassLoader());
        parcel.recycle();
        unParcel.recycle();
        return copy;

    }
}
