package com.hadith.narrator;
import java.io.*;
import android.database.sqlite.*;
import android.content.*;
import android.database.*;
import java.util.*;
import android.widget.*;

public class RSve extends SQLiteOpenHelper
{
	private static final String PATH = "/storage/emulated/0/program/rwt.db";
	private static final String tblsv="tbl_save",tblfm="tbl_fams",tblnt="tbl_ntg";
	private static final String cl="id,name,gender,nickname,famous_name,family,guardian,say,birth_year,death_year,lived_in,death_place,job,cmn_name,cmn_say,tch_name,tch_famous,tch_say,std_name,std_famous,std_say";
	List<Karts> lsv,lfm,lnt;
	Context context;
	
	public RSve(Context cntxt) {
		super(cntxt, PATH, null, 1);
		context=cntxt;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db){
		String crta="CREATE TABLE IF NOT EXISTS ";
		String crtb=" (id INTEGER PRIMARY KEY, name TEXT NOT NULL,gender INTEGER NOT NULL, nickname TEXT, famous_name TEXT, family TEXT, guardian TEXT, say INTEGER, birth_year TEXT, death_year TEXT, lived_in INTEGER, death_place INTEGER, job INTEGER, cmn_name TEXT, cmn_say TEXT,tch_name TEXT, tch_famous TEXT, tch_say TEXT,std_name TEXT, std_famous TEXT, std_say TEXT)";
		db.execSQL(crta + tblsv + crtb);
		db.execSQL(crta + tblfm + crtb);
		db.execSQL(crta + tblnt + crtb);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int p2, int p3){}
	
	// get All
	void getsfm() {
        lfm= gets("SELECT * FROM " + tblfm);
    }
	void getsnt() {
        lnt=gets("SELECT * FROM " + tblnt);
    }
	void getssv() {
        lsv=gets("SELECT * FROM " + tblsv);
    }
	// تبحث في الرواة وتخزن في النتائج
	void getGrps(String src){
		Cursor cr= rwtdb.srto(src);// بحث 
		if(cr==null) return;
		delnt(); int mx,id;
		SQLiteDatabase db = getWritableDatabase();
        lnt=new ArrayList<>(); int[] r=new int[5]; String[] s=new String[7];
		try{
			if (cr.moveToFirst()) {
				mx=cr.getCount();
				for(int i=0; i<mx; i++){
					db.insert(tblnt, null, getvlu(cr));// تخزين 
					id=cr.getInt(0);
					r[0]=cr.getInt(2); r[1]=cr.getInt(7); r[2]=cr.getInt(10);
					r[3]=cr.getInt(11); r[4]=cr.getInt(12);
					s[0]=cr.getString(1); s[1]=cr.getString(3); s[2]=cr.getString(4);
					s[3]=cr.getString(5); s[4]=cr.getString(6); s[5]=cr.getString(8);
					s[6]=cr.getString(9);
					lnt.add(new Karts(id,r,s));
					cr.moveToNext();
				}
			}
		}catch(Exception e){}
	}
	private ContentValues getvlu(Cursor cr){
        String[] ft=cl.split(","); byte[] b={0,1,0,1,1,1,1,0,1,1,0,0,0,1,1,1,1,1,1,1,1};
		ContentValues values = new ContentValues();
		for(int i=0; i<ft.length; i++){
			if(b[i]==0) values.put(ft[i], cr.getInt(i));
        	else values.put(ft[i], cr.getString(i));
		}
        return values;
    }
	// mov one
	void ntTosv(int id){
		if(indexOf(lsv,id)>=0) { show("موجود بالفعل"); return;}
		SQLiteDatabase db = getWritableDatabase();
        Cursor cr = db.rawQuery("SELECT * FROM " + tblnt +" WHERE id = "+id, null);
		if(cr==null) return; else cr.moveToFirst();
		db.insert(tblsv, null, getvlu(cr));// تخزين 
		cr.moveToFirst();
		lsv.add(gti(cr));
	}
	void ntTofm(int id){
		if(indexOf(lfm,id)>=0) { show("موجود بالفعل"); return;}
		SQLiteDatabase db = getWritableDatabase();
        Cursor cr = db.rawQuery("SELECT * FROM " + tblnt +" WHERE id = "+id, null);
		if(cr==null) return; else cr.moveToFirst();
		db.insert(tblfm, null, getvlu(cr));// تخزين 
		cr.moveToFirst();
		lfm.add(gti(cr));
	}
	void fmTosv(int id){
		if(indexOf(lsv,id)>=0) { show("موجود بالفعل"); return;}
		SQLiteDatabase db = getWritableDatabase();
        Cursor cr = db.rawQuery("SELECT * FROM " + tblfm +" WHERE id = "+id, null);
		if(cr==null) return; else cr.moveToFirst();
		db.insert(tblsv, null, getvlu(cr));// تخزين 
		cr.moveToFirst();
		lsv.add(gti(cr));
	}
	void fmTont(int id){
		if(indexOf(lnt,id)>=0) { show("موجود بالفعل"); return;}
		SQLiteDatabase db = getWritableDatabase();
        Cursor cr = db.rawQuery("SELECT * FROM " + tblfm +" WHERE id = "+id, null);
		if(cr==null) return; else cr.moveToFirst();
		db.insert(tblnt, null, getvlu(cr));// تخزين 
		cr.moveToFirst();
		lnt.add(gti(cr));
	}
	void svTont(int id){
		if(indexOf(lnt,id)>=0) { show("موجود بالفعل"); return;}
		SQLiteDatabase db = getWritableDatabase();
        Cursor cr = db.rawQuery("SELECT * FROM " + tblsv +" WHERE id = "+id, null);
		if(cr==null) return; else cr.moveToFirst();
		db.insert(tblnt, null, getvlu(cr));// تخزين 
		cr.moveToFirst();
		lnt.add(gti(cr));
	}
	void svTofm(int id){
		if(indexOf(lfm,id)>=0) { show("موجود بالفعل"); return;}
		SQLiteDatabase db = getWritableDatabase();
        Cursor cr = db.rawQuery("SELECT * FROM " + tblsv +" WHERE id = "+id, null);
		if(cr==null) return; else cr.moveToFirst();
		db.insert(tblfm, null, getvlu(cr));// تخزين 
		cr.moveToFirst();
		lfm.add(gti(cr));
	}
	// get one
	Nrtr getfm(int id) {
        return get("SELECT * FROM " + tblfm +" WHERE id = "+id);
    }
	Nrtr getnt(int id) {
        return get("SELECT * FROM " + tblnt +" WHERE id = "+id);
    }
	Nrtr getsv(int id) {
        return get("SELECT * FROM " + tblsv +" WHERE id = "+id);
    }
	// -----
	private Nrtr get(String sql)
	{
		SQLiteDatabase db = getReadableDatabase();
        Cursor cr = db.rawQuery(sql, null);
        if (cr == null) return null;
		int[] r=new int[5]; int id;
		String[] s=new String[15];
		Nrtr it=null;
		try{
			if (cr.moveToFirst()) {
				id=cr.getInt(0);
				r[0]=cr.getInt(2); r[1]=cr.getInt(7); r[2]=cr.getInt(10);
				r[3]=cr.getInt(11); r[4]=cr.getInt(12);
				s[0]=cr.getString(1); s[1]=cr.getString(3); s[2]=cr.getString(4);
				s[3]=cr.getString(5); s[4]=cr.getString(6); s[5]=cr.getString(8);
				s[6]=cr.getString(9); s[7]=cr.getString(13); s[8]=cr.getString(14);
				s[9]=cr.getString(15); s[10]=cr.getString(16); s[11]=cr.getString(17);
				s[12]=cr.getString(18); s[13]=cr.getString(19); s[14]=cr.getString(20);
				it=new Nrtr(id,r,s);
				cr.moveToNext();
			}
		}catch(Exception e){}
		return it;
	}
	private Karts gti(Cursor cr){
		int[] r=new int[5]; int id;
		String[] s=new String[7];
		id=cr.getInt(0);
		r[0]=cr.getInt(2); r[1]=cr.getInt(7); r[2]=cr.getInt(10);
		r[3]=cr.getInt(11); r[4]=cr.getInt(12);
		s[0]=cr.getString(1); s[1]=cr.getString(3); s[2]=cr.getString(4);
		s[3]=cr.getString(5); s[4]=cr.getString(6); s[5]=cr.getString(8);
		s[6]=cr.getString(9); 
		return new Karts(id,r,s);
	}
	private List<Karts> gets(String sql)
	{
		SQLiteDatabase db = getReadableDatabase();
        Cursor cr = db.rawQuery(sql, null);
       // if (cr != null) cr.moveToNext(); else return null;
		List<Karts> it=new ArrayList<>(); int mx;
		try{
			if (cr.moveToFirst()) {
				mx=cr.getCount();
				for(int i=0; i<mx; i++){
					it.add(gti(cr));
					cr.moveToNext();
				}
			}
		}catch(Exception e){}
		return it;
	}
	// del All
	void delsv(){
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("DELETE FROM "+tblsv);
	}
	void delfm(){
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("DELETE FROM "+tblfm);
	}
	void delnt(){
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("DELETE FROM "+tblnt);
	}
	// del one
	void delsv(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(tblsv, "id = ?", new String[] { String.valueOf(id) });
        db.close();
		del(lsv,id);
    }
	void delfm(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(tblfm, "id = ?", new String[] { String.valueOf(id) });
        db.close();
		del(lfm,id);
    }
	void delnt(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(tblnt, "id = ?", new String[] { String.valueOf(id) });
        db.close();
		del(lnt,id);
    }
	// adding
	long addfm(ContentValues cv){
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(tblfm, null, cv);
    }
	long addnt(ContentValues cv){
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(tblnt, null, cv);
    }
	long addsv(ContentValues cv){
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(tblsv, null, cv);
    }
	static int indexOf(List<Karts> g,int id){
		for(int i=0; i<g.size(); i++){
			if(g.get(i).id==id) return i;
		}
		return -1;
	}
	private static List<Karts> del(List<Karts> g,int id){
		for(int i=0; i<g.size(); i++){
			if(g.get(i).id==id) { g.remove(i); break; }
		}
		return g;
	}
	void show(String msg){
		Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
	}
}
