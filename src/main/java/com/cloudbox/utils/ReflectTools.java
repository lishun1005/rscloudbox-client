package com.cloudbox.utils;


import java.lang.reflect.Field;



public class ReflectTools {

	public static Object[][] ReflectKV(Object obj) {
		if (obj == null)
			return null;
		Field[] fields = obj.getClass().getDeclaredFields();
		Object[][] reflectKV = new Object[fields.length][3];
		for (int j = 0; j < fields.length; j++) {
			fields[j].setAccessible(true);
			try {
				reflectKV[j][0] = fields[j].getName();
				reflectKV[j][1] = fields[j].get(obj);
				reflectKV[j][2] = fields[j].getType().getName();
			/*	System.out.println(reflectKV[j][0] + ":" + reflectKV[j][1]
						+ ":" + reflectKV[j][2]);*/
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return reflectKV;
	}

	public static String ReflectforUpdatesql(Object[][] reflectKV,
			String table, String[] whereK) {
		// TODO Auto-generated method stub
		String sql = null;
		String sqlwhere = null;
		sql = "UPDATE " + table + " SET ";
		sqlwhere = " where ";
		for (int i = 0; i < whereK.length; i++) {
			if (whereK[i]==null) {
				continue;
			} 
			for (int j = 0; j < reflectKV.length; j++) {
				if (reflectKV[j][1] != null && reflectKV[j][0] != "serialVersionUID") {
					if (reflectKV[j][0] == whereK[i]) {
						if(reflectKV[j][0]=="image_area"||reflectKV[i][2].toString()=="Number"){
							sqlwhere+=reflectKV[j][0] + "="+reflectKV[j][1]+",";
						}else{
							sqlwhere += reflectKV[j][0] + "='"+ reflectKV[j][1] + "',";
						}
					}
					if (reflectKV[j][0]=="image_area"||reflectKV[i][2].toString()=="Number") {
						sql += reflectKV[j][1] + ",";
					} else {
						sql += reflectKV[j][0] + "='" + reflectKV[j][1] + "', ";
					}

				}
			}
		}
		sql = sql.substring(0, sql.length() - 1);
		sqlwhere = sqlwhere.substring(0, sqlwhere.length() - 1);
		sql += sqlwhere;
//		System.out.println(sql);
		return sql;
	}
	public static String ReflectforUpdatesqlwithGeom(Object[][] reflectKV,
			String table, String[] whereK) {
		// TODO Auto-generated method stub
		String sql = null;
		String sqlwhere = null;
		sql = "UPDATE " + table + " SET ";
		sqlwhere = " where ";
		for (int i = 0; i < whereK.length; i++) {
			if (whereK[i]==null) {
				continue;
			} 
			for (int j = 0; j < reflectKV.length; j++) {
				if (reflectKV[j][1] != null && reflectKV[j][0] != "serialVersionUID") {
					if (reflectKV[j][0] == whereK[i]) {
						if(reflectKV[j][0]=="gemo"||reflectKV[j][0]=="range"){
							sqlwhere+=reflectKV[j][0] + "= ST_GeomFromText('"+reflectKV[j][1]+"'),";
						}else if(reflectKV[j][0]=="image_area"||reflectKV[i][2].toString()=="Number"){
							sqlwhere += reflectKV[j][0] + "="+ reflectKV[j][1] + ",";
						}else {
							sqlwhere += reflectKV[j][0] + "='"+ reflectKV[j][1] + "',";
						}
					}
					if(reflectKV[j][0]=="gemo"||reflectKV[j][0]=="range"){
						sql +=reflectKV[j][0] + "= ST_GeomFromText('"+reflectKV[j][1]+"'),";
					}else if(reflectKV[j][0]=="image_area"||reflectKV[i][2].toString()=="Number"){
						sql += reflectKV[j][0] + "="+ reflectKV[j][1] + ",";
					}else {
						sql += reflectKV[j][0] + "='"+ reflectKV[j][1] + "',";
					}
				}
			}
		}
		sql = sql.substring(0, sql.length() - 1);
		sqlwhere = sqlwhere.substring(0, sqlwhere.length() - 1);
		if (!sqlwhere.equals(" where")) {
			sql += sqlwhere;
		}
//		System.out.println(sql);
		return sql;
	}

	public static String ReflectforInsertsql(Object[][] reflectKV,String table) {
		String sql = null;
		String valueArray=null;
		String geom="";
		sql="INSERT INTO "+table +"(";
		valueArray="values(";
		for (int i = 0; i < reflectKV.length; i++) {
			if (reflectKV[i][1] != null && reflectKV[i][0] != "serialVersionUID") {
				if(reflectKV[i][0]=="gemo"||reflectKV[i][0]=="range"){
					sql+=reflectKV[i][0]+",";
					valueArray+="ST_GeomFromText('"+reflectKV[i][1]+"'),";
				}else if (reflectKV[i][0]=="image_area") {
					sql+=reflectKV[i][0]+",";
					valueArray+=""+reflectKV[i][1]+",";
				}else{
					sql+=reflectKV[i][0]+",";
					if (reflectKV[i][2].toString()=="Number") {
						valueArray+=""+reflectKV[i][1]+",";
					}
					valueArray+="'"+reflectKV[i][1]+"',";
				}
				
			}
		}
		sql = sql.substring(0, sql.length() - 1)+")";
		valueArray = valueArray.substring(0, valueArray.length() - 1)+")";
		sql+=" "+valueArray;
		return sql;
	}
	
	public static String ReflectforInsertsqlwithGeom(Object[][] reflectKV,String table) {
		String sql = null;
		String valueArray=null;
		sql="INSERT INTO "+table +"(";
		valueArray="value(";
		for (int i = 0; i < reflectKV.length; i++) {
			if (reflectKV[i][1] != null
					&& reflectKV[i][0] != "serialVersionUID") {
				if (reflectKV[i][0].equals("geom")||reflectKV[i][0].equals("range")) {
					sql+=reflectKV[i][0]+",";
					valueArray+="ST_GeomFromText('"+reflectKV[i][1]+"',4326),";
				}else if (reflectKV[i][0]=="image_area") {
					sql+=reflectKV[i][0]+",";
					valueArray+=""+reflectKV[i][1]+",";
				}else {
					sql+=reflectKV[i][0]+",";
					valueArray+=reflectKV[i][1]+",";
				}
			}
		}
		sql = sql.substring(0, sql.length() - 1)+")";
		valueArray = valueArray.substring(0, valueArray.length() - 1)+")";
		sql+=" "+valueArray;
//		System.out.println(sql);
		return sql;
	}
    public String ReflectforSelectsql(Object[][] reflectKV,String table,String[] whereK ,String[] selectwhatK) {
    	String sql="select";
    	String wheresql=" where ";
    	if (selectwhatK==null||selectwhatK.length==0) {
    		sql+=" *  ";
		}else {
			for (int i = 0; i < selectwhatK.length; i++) {
	    		sql+=selectwhatK[i]+",";
			}
		}
    	sql = sql.substring(0, sql.length() - 1)+" from "+table;
    	for (int i = 0; i < whereK.length; i++) {
    		if (whereK[i]==null) {
				continue;
			} 
			for (int j = 0; j < reflectKV.length; j++) {
				if (reflectKV[j][1] != null && reflectKV[j][0] != "serialVersionUID") {
					if (reflectKV[j][0] == whereK[i]) {
						if (reflectKV[j][2].equals(java.lang.String.class.getName())
								|| reflectKV[j][2].equals(java.sql.Timestamp.class.getName())) {
							wheresql += reflectKV[j][0] + "='"+ reflectKV[j][1] + "',";
						} else {
							wheresql += reflectKV[j][0] + "=" + reflectKV[j][1] + ",";
						}
					}
				}
			}
		}		
    	wheresql = wheresql.substring(0, wheresql.length() - 1);
    	sql+=wheresql;
    	return sql;
	}
    public String ReflectforSelectsqlwithGeom(Object[][] reflectKV,String table,String[] whereK,String[] selectwhatK) {
    	String sql="select";
    	String wheresql=" where ";
    	if (selectwhatK==null||selectwhatK.length==0) {
    		sql+=" *  ";
		}else {
			for (int i = 0; i < selectwhatK.length; i++) {
	    		sql+=selectwhatK[i]+",";
			}
		}
    	sql = sql.substring(0, sql.length() - 1)+" from "+table;
    	for (int i = 0; i < whereK.length; i++) {
    		if (whereK[i]==null) {
				continue;
			} 
			for (int j = 0; j < reflectKV.length; j++) {
				if (reflectKV[j][1] != null && reflectKV[j][0] != "serialVersionUID") {
					if (reflectKV[j][0] == whereK[i]) {
						if (reflectKV[j][2].equals(java.lang.String.class.getName())
								|| reflectKV[j][2].equals(java.sql.Timestamp.class.getName())) {
							if (whereK[i].equals("geom")||whereK[i].equals("range")) {
								wheresql +="ST_Intersects(geom,ST_GeomFromText('"+reflectKV[j][1]+"'))";
							}else {
								wheresql += reflectKV[j][0] + "='"+ reflectKV[j][1] + "',";
							}
						} else {
							wheresql += reflectKV[j][0] + "=" + reflectKV[j][1] + ",";
						}
					}
				}
			}
		}		
    	wheresql = wheresql.substring(0, wheresql.length() - 1);
    	sql+=wheresql;
    	return sql;
	}
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/* RsImageMetaData RsImageMetaData=new RsImageMetaData();
		 RsImageMetaData.setBegin_time("2013-12332-222");
		 RsImageMetaData.setImage_area(1);
	     Object[][] reflectKV22 =ReflectTools.ReflectKV(RsImageMetaData);
	     ReflectTools.ReflectforInsertsql(reflectKV22,"table");
	     String[] rkeStrings= new String[2];
	     rkeStrings[0]="begin_time";
	     ReflectTools.ReflectforUpdatesql(reflectKV22,"table",rkeStrings);*/
	}

}
