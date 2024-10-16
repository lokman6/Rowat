package com.hadith.narrator;
import android.text.*;

public class Karts
{
	String[] wt;
	int id=0,nbc=0;
	final static String[] tit = {"الرتبة","الإسم","الشهرة","الكنية","النسبة","مواليه","المهنة","ولد عام","توفي عام","عاش في","مات في","جنس الراوي"};
	String wd;
	
	Karts(int iv,int[] r,String[] s){
		id=iv;
		wt=new String[12];
		for(int i=0; i<wt.length; i++){ wt[i]=""; }
		if(r[1]>0) wt[0]=getAr(rwtdb.sy.get(r[1]-1)); // الرتبة

		wt[1]=getAr(s[0]); // الإسم
		wt[2]=getAr(s[2]); // الشهرة
		wt[3]=getAr(s[1]); // الكنية
		wt[4]=getAr(s[3]); // النسبة
		wt[5]=getAr(s[4]); // مواليه

		if(r[4]>0) wt[6]=getAr(rwtdb.j.get(r[4]-1));// المهنة 
		wt[7]=getAr(s[5]);// الميلاد 
		wt[8]=getAr(s[6]);// الوفاة 
		if(r[2]>0) wt[9]=getAr(rwtdb.p.get(r[2]-1));// مكان الإقامة 
		if(r[3]>0) wt[10]=getAr(rwtdb.p.get(r[3]-1));// مكان الوفاة 
		wt[11]="رجل"; if(r[0]==0) wt[11]="امرأة";
		wtToWd();
	}
	// تعريف
	public Spanned toName(int n)
	{
		StringBuffer sx=new StringBuffer("     "+(n+1)+"<br>");
		for(int i=0; i<wt.length; i++){
			if(wt[i]==null || wt[i].isEmpty()) continue;
			else sx.append("<font color=red>"+tit[i]+" : </font>"+wt[i]+"<br>");
		}
		sx.append("<font color=red>"+"المعرف"+" : </font>"+id+"<br>");
		return Html.fromHtml(sx.toString());
	}
	public String toName()
	{
		StringBuffer sx=new StringBuffer();
		for(int i=0; i<wt.length; i++){
			if(wt[i]==null || wt[i].isEmpty()) continue;
			else sx.append(tit[i]+" : "+wt[i]+"\n");
		}
		return sx.toString();
	}
	boolean isName(String src){
		if(wt==null) return false;
		String[] sr=src.split("\n"); 
		char[] c=new char[sr.length];
		for(int i=0; i<sr.length; i++){
			c[i]='+';
			if(sr[i].isEmpty() || sr[i].equals("!") || sr[i].equals("!-") || sr[i].equals("-")) {c[i]=' '; sr[i]="";}
			else if(sr[i].startsWith("!-")) {sr[i]=sr[i].substring(2); c[i]='x';}
			else if(sr[i].startsWith("!")) {sr[i]=sr[i].substring(1); c[i]='!';}
			else if(sr[i].startsWith("-")) {sr[i]=sr[i].substring(1); c[i]='-';}
		}
		return cndtn(sr,c,wd);
	}
	private boolean cndtn(String[] sr,char[] c,String m){
		if(m==null || m.isEmpty()) return false;
		boolean rd;
		for(int i=0; i<sr.length; i++){
			rd=true;
			if(c[i]!=' ') {
				if(c[i]=='-' || c[i]=='x') rd=m.startsWith(sr[i]);
				else if(c[i]=='+' || c[i]=='!') rd=m.contains(sr[i]); 
				if(c[i]=='!' || c[i]=='x') rd=!rd;
			}
			if(!rd) return false;
		}
		return true;
	}
	private void wtToWd(){
		wd="";
		for(int i=0; i<wt.length; i++){
			if(wt[i].isEmpty()) continue;
			if(!wd.isEmpty()) wd+="\n";
			wd+=wt[i];
		}
	}
	static private String getAr(String en){
		if(en==null || en.isEmpty()) return "";
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
}
