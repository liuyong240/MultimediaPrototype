package org.multimediaprototype.ocs.base;

public class KeysTools {

	public static class UserOssListkey{
		
		/**
		 * 根据USER得到对应key值
		 * @param userkey
		 * @return
		 */
		public static String userOssListKey(Long userid, Integer offset,
	            Integer rowCount){
			
			return "USER"+userid.toString()+"-"+offset.toString()+"-"+rowCount.toString();
		}
	}

	
}
