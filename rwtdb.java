package com.hadith.narrator;

import android.database.sqlite.*;
import android.database.*;
import java.io.*;
import android.widget.*;
import android.content.*;
import java.util.*;
import android.util.*;
import android.net.*;

public class rwtdb
{
	//private static String DB_PATH = "//assets/";  
	//private static String DB_NAME ="alrowat.db";// Database name 
	private static final String DATABASE_PATH = "/storage/emulated/0/program/rowats.db";
	//private static final String DATABASE_PATH = "/storage/extSdCard/rowats.db";
	static SQLiteDatabase dtb;
	static boolean bg=false;
	
	static List<String> sy;
	static List<String> j;
	static List<String> p;
	static List<String> cn;
	
	static void load(Context cx){
		 begin(cx);
		 sy=gets_name("Sayes","saying"); // الأقوال
		 j=gets_name("Jobs","job"); // المهن
		 p=gets_name("Places","place"); // الأماكن
		 cn=gets_name("Cmn_name","name"); // أسماء أئمة الجرح والتعديل
	}
    static void begin(Context cx)  {
		//String fl = cx.getApplicationInfo().dataDir + "/databases/" + DB_NAME; 
		//if(!checkDataBase(fl)) try{ copyDataBase(cx,fl); } catch(Exception ex){}
		try{
			dtb=SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READONLY);
			bg=true;
		}catch(SQLException ex){
			show(cx,ex.toString());
		}
    }/*
	static private boolean checkDataBase(String f) 
	{ 
		File dbFile = new File(f); 
		boolean g = dbFile.exists();
		return g;
	} 
	static private void copyDataBase(Context cx,String f) throws IOException 
	{ 
		InputStream mInput = cx.getAssets().open(DB_NAME); 
		String outFileName = f; 
		OutputStream mOutput = new FileOutputStream(outFileName); 
		byte[] mBuffer = new byte[1024]; 
		int mLength; 
		while ((mLength = mInput.read(mBuffer))>0) 
		{ 
			mOutput.write(mBuffer, 0, mLength); 
		} 
		mOutput.flush(); 
		mOutput.close(); 
		mInput.close(); 
	}*/
	static void show(Context th, String msg){
		Toast.makeText(th,msg,Toast.LENGTH_LONG).show();
	}
	private static String condtion(String[] sr){// name tch std nick fmly grdn
		String[] u; String[] t={"name","famous_name","tch_name","tch_famous","std_name","std_famous","nickname","family","guardian","birth_year","death_year"};
		String d,v=" OR ",w=" AND "; String con=""; int r=0;
		for(int n=0;n<6;n++){
			if(!sr[n].isEmpty()){
				u=sr[n].split("\n"); if(!con.isEmpty()) con+=w;
				for(int i=0;i<u.length;i++){
					if(u[i].startsWith("-")) u[i]=u[i].substring(1); else u[i]="*"+u[i];
					if(u[i].endsWith("-")) u[i]=u[i].substring(0,u[i].length()-1); else u[i]+="*";
					d=" GLOB '"+getEn(u[i])+"'"; if(i>0) con+=w;
					if(n<3) { r=n*2; con+="( "+t[r]+d+v+t[r+1]+d+" )";}
					else { r=n+3; con+="( "+t[r]+d+" )";}
					
				}
			}
		}
		for(int n=6;n<8;n++){
			if(!sr[n].isEmpty()){
				u=sr[n].split("\n"); if(!con.isEmpty()) con+=w;
				for(int i=0;i<u.length;i++){
					d=" GLOB '*"+u[i]+"*'"; if(i>0) con+=w;
					con+="( "+t[n+3]+d+" )";
				}
			}
		}
		return con;
	}
	static Cursor srto(String src){
		String[] sr=src.split("¥");
		String sql="SELECT * FROM Narrators WHERE "+condtion(sr)+" LIMIT "+100;
		Cursor cr=null;
		try{
		 	cr=dtb.rawQuery(sql,null);
		}catch(Exception ex){}
		return cr;
	}
	static Nrtr[] getGrps(String src){
		String[] sr=src.split("¥");
		//String cl="id0, name1, gender2, nickname3, famous_name4, family5, guardian6, say7, birth_year8, death_year9, lived_in10, death_place11, job12, 
		// cmn_name13, cmn_say14,tch_name15, tch_famous16, tch_say17,std_name18, std_famous19, std_say20";
		String sql="SELECT * FROM Narrators WHERE "+condtion(sr)+" LIMIT "+100;
		//String sql="SELECT * FROM Narrators WHERE 'jo' = 'j%' AND id = 1";
		Cursor cr=null;
		try{
		 	cr=dtb.rawQuery(sql,null);
		}catch(Exception ex){}
		if (cr == null) return null;
		return get(cr);
		}
		
		static Nrtr[] get(Cursor cr)
		{
			int[] r=new int[5]; int id;
			String[] s=new String[15];
		Nrtr[] it=null; int mx;
		try{
			if (cr.moveToFirst()) {
				mx=cr.getCount(); it=new Nrtr[mx];
				for(int i=0; i<mx; i++){
					id=cr.getInt(0);
					r[0]=cr.getInt(2); r[1]=cr.getInt(7); r[2]=cr.getInt(10);
					r[3]=cr.getInt(11); r[4]=cr.getInt(12);
					s[0]=cr.getString(1); s[1]=cr.getString(3); s[2]=cr.getString(4);
					s[3]=cr.getString(5); s[4]=cr.getString(6); s[5]=cr.getString(8);
					s[6]=cr.getString(9); s[7]=cr.getString(13); s[8]=cr.getString(14);
					s[9]=cr.getString(15); s[10]=cr.getString(16); s[11]=cr.getString(17);
					s[12]=cr.getString(18); s[13]=cr.getString(19); s[14]=cr.getString(20);
					it[i]=new Nrtr(id,r,s);
					cr.moveToNext();
				}
			}
		}catch(Exception e){}
		return it;
	}
	static Nrtr getNarrator(int id){
		String cl="name, gender, nickname, famous_name, family, guardian, say, birth_year, death_year, lived_in, death_place, job, cmn_name, cmn_say,tch_name, tch_famous, tch_say,std_name, std_famous, std_say";
		String con=" WHERE id = "+id;
		String sql="SELECT "+cl+" FROM Narrators"+con;
		Cursor cr=dtb.rawQuery(sql,null);
		return getn(cr,id);
	}
	private static Nrtr getn(Cursor cr,int id){
		int[] r=new int[5];
		String[] s=new String[15];
		Nrtr vb=null;
		if (cr == null) return null;
		try{
			if (cr.moveToFirst()) {
				r[0]=cr.getInt(1); r[1]=cr.getInt(6); r[2]=cr.getInt(9);
				r[3]=cr.getInt(10); r[4]=cr.getInt(11);
				s[0]=cr.getString(0); s[1]=cr.getString(2); s[2]=cr.getString(3);
				s[3]=cr.getString(4); s[4]=cr.getString(5); s[5]=cr.getString(7);
				s[6]=cr.getString(8); s[7]=cr.getString(12); s[8]=cr.getString(13);
				s[9]=cr.getString(14); s[10]=cr.getString(15); s[11]=cr.getString(16);
				s[12]=cr.getString(17); s[13]=cr.getString(18); s[14]=cr.getString(19);
				vb = new Nrtr(id,r,s);
			}
		}catch(Exception e){}
		return vb;
	}
	static List<String> gets_name(String tbl,String cl){
		String sql="SELECT "+cl+" FROM "+tbl;
		Cursor cursor=dtb.rawQuery(sql,null);
		List<String> g=new ArrayList<String>();
		try{
			if (cursor.moveToFirst()) {
				for(int i=0; i<cursor.getCount(); i++){
					g.add(cursor.getString(0));
					cursor.moveToNext();
				}
			}
		}catch(Exception e){}
		return g;
	}
	static String shortToStr(int a)
	{
		byte[] ret = new byte[2];
		ret[1] = (byte) (a & 0xFF);   
		ret[0] = (byte) ((a >> 8) & 0xFF);
		String g= android.util.Base64.encodeToString(ret, android.util.Base64.DEFAULT);
		return g.substring(0,3);
	}

	static int strToShort(String en)
	{
		byte[] b=android.util.Base64.decode(en+"=\n", android.util.Base64.DEFAULT);
		return (b[1] & 0xFF) + ((b[0] & 0xFF) << 8) +((0 & 0xFF) << 16) + ((0 & 0xFF) << 24);
	}

	static String getAr(String en){
		if(en==null) return "";
		String a="ءآأؤإئابةتثجحخدذرزسشصضطظعغ",b="فقكلمنهوىي";
		char[] c=en.toCharArray();
		String ar="";
		for(int i=0; i<c.length; i++){
			if(c[i]>96 && c[i]<123){ ar+=a.charAt(c[i]-97); continue; }
			if(c[i]>64 && c[i]<75){ ar+=b.charAt(c[i]-65); continue; }
			ar+=c[i];
		}
		return ar;
	}

	static String getEn(String ar){
		String a="ءآأؤإئابةتثجحخدذرزسشصضطظعغ",b="فقكلمنهوىي";
		char[] c=ar.toCharArray(); 
		String en=""; int u;
		for(int i=0; i<c.length; i++){
			u=a.indexOf(c[i]);
			if(u>=0){ en+=(char)(97+u); continue; }
			u=b.indexOf(c[i]);
			if(u>=0){ en+=(char)(65+u); continue; }
			en+=c[i];
		}
		return en;
	}
}
/*
CREATE TABLE Sayes (id INTEGER PRIMARY KEY AUTOINCREMENT, saying TEXT)
CREATE TABLE Jobs (id INTEGER PRIMARY KEY AUTOINCREMENT, Job TEXT)
CREATE TABLE Places (id INTEGER PRIMARY KEY AUTOINCREMENT, place TEXT)
CREATE TABLE Cmn_name (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)
CREATE TABLE Narrators (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL,gender INTEGER NOT NULL, nickname TEXT, famous_name TEXT, family TEXT, guardian TEXT, say INTEGER, birth_year TEXT, death_year TEXT, lived_in INTEGER, death_place INTEGER, job INTEGER, cmn_name TEXT, cmn_say TEXT,tch_name TEXT, tch_famous TEXT, tch_say TEXT,std_name TEXT, std_famous TEXT, std_say TEXT)
"                       id,                                   name,              gender,                  nickname,      famous_name,      family,      guardian,      say,         birth_year,      death_year,      lived_in,         death_place,         job,         cmn_name,      cmn_say,     tch_name,      tch_famous,      tch_say,     std_name,      std_famous,      std_say"
*/
