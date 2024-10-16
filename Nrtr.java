package com.hadith.narrator;

import android.database.*;
import java.util.*;
import android.util.*;
import android.text.*;

//7 name En, nickname En, famous_name En, family En, guardian En,  birth_year En, death_year En
//4 say INTEGER sy,lived_in INTEGER p, death_place INTEGER p, job INTEGER j
//1 gender INTEGER NOT NULL,
//4 cmn_name €strint cn, cmn_say €strint sy, tch_say €strint sy, std_say €strint sy
//4 tch_name €En, tch_famous €En,std_name €En, std_famous €En
public class Nrtr
{
	String[] wt;
	int id=0,nbc=0;
	final static String[] tit = {"الرتبة","الإسم","الشهرة","الكنية","النسبة","مواليه","المهنة","ولد عام","توفي عام","عاش في","مات في","جنس الراوي"};
	String wd;
	byte bcm=0,btc=0,bst=0;
	//"name0, gender1, nickname2, famous_name3, family4, guardian5, say6, birth_year7, death_year8, lived_in9, death_place10, job11, cmn_name12, cmn_say13,tch_name14, tch_famous15, tch_say16,std_name17, std_famous18, std_say19"
	//final static int[] ir = {6,0,3,2,4,5,11,7,8,9,10,1};
	private String[] st=null;
	private String[] tc=null;
	private String[] cm=null;
	
	Nrtr(int iv,int[] r,String[] s){
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
		cm=getCm(s[7],s[8]); // التعليقات
		tc=getTcSt(s[9],s[10],s[11]); // الشيوخ
		st=getTcSt(s[12],s[13],s[14]); // التلاميذ
	}
	void wtToWd(){
		wd="";
		for(int i=0; i<wt.length; i++){
			if(wt[i].isEmpty()) continue;
			if(!wd.isEmpty()) wd+="\n";
			wd+=wt[i];
		}
	}
	String famous_name(){
		return wt[2];
	}
	private List<String> toSel(List<String> sx, String[] m,String src)
	{
		if(m==null) return null;
		String[] sr=src.split("\n"); 
		char[] c=new char[sr.length];
		for(int i=0; i<sr.length; i++){
			c[i]='+';
			if(sr[i].isEmpty() || sr[i].equals("!") || sr[i].equals("!-") || sr[i].equals("-")) {c[i]=' '; sr[i]="";}
			else if(sr[i].startsWith("!-")) {sr[i]=sr[i].substring(2); c[i]='x';}
			else if(sr[i].startsWith("!")) {sr[i]=sr[i].substring(1); c[i]='!';}
			else if(sr[i].startsWith("-")) {sr[i]=sr[i].substring(1); c[i]='-';}
		}
		for(int i=0; i<m.length; i++){
			if(cndtn(sr,c,m[i])) sx.add(m[i]);
		}
		return sx;
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
	//الشيوخ
	public List<String> toTch(String sr)
	{
		List<String> sx=new ArrayList<>();
		return toSel(sx,tc,sr);
	}
	//التلاميذ
	public List<String> toStd(String sr)
	{
		List<String> sx=new ArrayList<>();
		return toSel(sx,st,sr);
	}
	// التعليقات
	public List<String> toCmn(String sr)
	{
		List<String> sx=new ArrayList<>();
		return toSel(sx,cm,sr);
	}
	// تعريف
	public Spanned toName()
	{
		StringBuffer sx=new StringBuffer();
		for(int i=0; i<wt.length; i++){
			if(wt[i]==null || wt[i].isEmpty()) continue;
			else sx.append("<font color=red>"+tit[i]+" : </font>"+wt[i]+"<br>");
		}
		sx.append("<font color=red>"+"المعرف"+" : </font>"+id+"<br>");
		return Html.fromHtml(sx.toString());
	}
	boolean isName(String src){
		if(wt==null) return false;
		String[] sr=src.split("\n"); 
		char[] c=new char[sr.length];
		for(int i=0; i<sr.length; i++){
			c[i]='-';
			if(sr[i].isEmpty() || sr[i].equals("!")) {c[i]=' '; sr[i]="";}
			else if(sr[i].startsWith("!")) {sr[i]=sr[i].substring(1); c[i]='!';}
		}
		return cndtn(sr,c,wd);
	}
	static String[] getCm(String nm,String sa){
		if(nm==null || sa==null || nm.isEmpty() || sa.isEmpty()) return null;
		String[] n=frmTbl(nm,rwtdb.cn);
		String[] s=frmTbl(sa,rwtdb.sy);
		if(n.length!=s.length) {
			int t=0;
		}
		String[] sb=new String[s.length];
		for(int r=0; r<s.length; r++) {
			sb[r]=n[r]+"¥"+s[r];
		}
		return sb;
	}
	static String[] getTcSt(String nm,String fm,String sa){
		if(nm==null || sa==null || fm==null || nm.isEmpty() || fm.isEmpty() || sa.isEmpty()) return null;
		String[] n=getArs(nm);
		String[] f=getArs(fm);
		String[] s=frmTbl(sa,rwtdb.sy);
		if(n.length!=f.length || n.length!=s.length) {
			int t=0;
		}
		String[] sb=new String[f.length];
		for(int r=0; r<f.length; r++) {
			sb[r]=n[r]+" ("+f[r]+")¥"+s[r];
		}
		return sb;
	}
	static String[] getArs(String v){
		boolean vc=false;
		if(v.endsWith("€")) {v+="¥"; vc=true;}
		String[] f=v.split("€"); if(vc) f[f.length-1]="";
		String[] ar=new String[f.length];
		for(int r=0; r<f.length; r++) {
			ar[r]=getAr(f[r]);
		}
		return ar;
	}
	static String[] frmTbl(String v,List<String> tbl){
		String[] ar=new String[v.length()/3];
		String er; int ig;
			for(int r=0; r<v.length(); r+=3) {
				er=v.substring(r,r+3);
				ig=strToShort(er);
				if(ig>0) ar[r/3]=getAr(tbl.get(ig-1));
			}
		return ar;
	}
	
	
	static String shortToStr(int a)
	{
		byte[] ret = new byte[2];
		ret[1] = (byte) (a & 0xFF);   
		ret[0] = (byte) ((a >> 8) & 0xFF);
		String g=android.util.Base64.encodeToString(ret,android.util.Base64.DEFAULT);
		return g.substring(0,3);
	}

	static int strToShort(String en)
	{
		byte[] b=android.util.Base64.decode(en+"=\n", android.util.Base64.DEFAULT);
		return (b[1] & 0xFF) + ((b[0] & 0xFF) << 8) +((0 & 0xFF) << 16) + ((0 & 0xFF) << 24);
	}

	static String getAr(String en){
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
