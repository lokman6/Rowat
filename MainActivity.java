package com.hadith.narrator;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import android.graphics.*;
import android.text.*;
import java.io.*;
import java.util.*;
import android.content.*;
import android.util.*;
import android.view.inputmethod.*;
import android.text.ClipboardManager;

public class MainActivity extends Activity 
{
	LinearLayout grp,stdy,tech,sve,fms,comn,plst;
	TextView run,nb_cmn,nb_tch,nb_std,nb_ntg,nb_sve,nb_fms,kart;
	TextView[] gr,std,tec,sav,fam,cmn;
	int tab=0;
	EditText edn,edt,eds,edf,edc,edg,brth,deth,sr_cmn,sr_tch,sr_std,sr_ntg,sr_sve,sr_fms;
	TabHost.TabSpec[] spec;
	TabHost tabhost;
	Nrtr rac; // الراوي النشط
	RSve rsv; // النتائج والمشاهير والمحفوظ
	//String path;
	ProgressBar pwait;
	PopupWindow wndup;
	View pvw;
	private ClipboardManager clp;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.main);
		rwtdb.load(this);
		rsv=new RSve(this);
		clp = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
		initTab();
		setTabColor();
		initwnd();
		actvnt();
		actvsv();
		actvfm();
    }
	// create popuoWindow
	void initwnd(){
		LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		pvw = inflater.inflate(R.layout.pop, null);  
		wndup = new PopupWindow(pvw,LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
		plst=(LinearLayout)pvw.findViewById(R.id.plst);
	}
	// add elements
	void pop_nt(final int id){
		String[] nm={"اختيار","إلى المحفوظ","إلى المشاهير","نسخ","حذف"};
		plst.removeAllViews();
		TextView[] tg=new TextView[nm.length];
		for(int i=0; i<tg.length; i++){
			tg[i]=new TextView(this);
			tg[i].setText(nm[i]);
			tg[i].setGravity(Gravity.CENTER);
			tg[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_highlight_square));
			tg[i].setTextSize(28);
			tg[i].setTextColor(Color.BLACK);
			plst.addView(tg[i]);
		}
		tg[0].setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					rac=rsv.getnt(id);
					toAll();
					wndup.dismiss();
				}
			});
		tg[1].setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					rsv.ntTosv(id);
					actvsv();
					wndup.dismiss();
				}
			});
		tg[2].setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					rsv.ntTofm(id);
					actvfm();
					wndup.dismiss();
				}
			});
		tg[3].setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					int ix=rsv.indexOf(rsv.lnt,id);
					clp.setText(rsv.lnt.get(ix).toName());
					wndup.dismiss();
				}
			});
		tg[4].setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					rsv.delnt(id);
					actvnt();
					wndup.dismiss();
				}
			});
	}
	void pop_sv(final int id){
		String[] nm={"اختيار","إلى المشاهير","نسخ","حذف"};
		plst.removeAllViews();
		TextView[] tg=new TextView[nm.length];
		for(int i=0; i<tg.length; i++){
			tg[i]=new TextView(this);
			tg[i].setText(nm[i]);
			tg[i].setGravity(Gravity.CENTER);
			tg[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_highlight_square));
			tg[i].setTextSize(28);
			tg[i].setTextColor(Color.BLACK);
			plst.addView(tg[i]);
		}
		tg[0].setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					rac=rsv.getsv(id);
					toAll();
					wndup.dismiss();
				}
			});
		tg[1].setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					rsv.svTofm(id);
					actvfm();
					wndup.dismiss();
				}
			});
		tg[2].setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					int ix=rsv.indexOf(rsv.lsv,id);
					clp.setText(rsv.lsv.get(ix).toName());
					wndup.dismiss();
				}
			});
		tg[3].setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					rsv.delsv(id);
					actvsv();
					wndup.dismiss();
				}
			});
	}
	void pop_fm(final int id){
		String[] nm={"اختيار","إلى المحفوظ","نسخ","حذف"};
		plst.removeAllViews();
		TextView[] tg=new TextView[nm.length];
		for(int i=0; i<tg.length; i++){
			tg[i]=new TextView(this);
			tg[i].setText(nm[i]);
			tg[i].setGravity(Gravity.CENTER);
			tg[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_highlight_square));
			tg[i].setTextSize(28);
			tg[i].setTextColor(Color.BLACK);
			plst.addView(tg[i]);
		}
		tg[0].setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					rac=rsv.getfm(id);
					toAll();
					wndup.dismiss();
				}
			});
		tg[1].setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					rsv.fmTosv(id);
					actvsv();
					wndup.dismiss();
				}
			});
		tg[2].setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					int ix=rsv.indexOf(rsv.lfm,id);
					clp.setText(rsv.lfm.get(ix).toName());
					wndup.dismiss();
				}
			});
		tg[3].setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					rsv.delfm(id);
					actvfm();
					wndup.dismiss();
				}
			});
	}
	void pop_ser(final String sx){
		String[] nm={"بحث","نسخ"};
		plst.removeAllViews();
		TextView[] tg=new TextView[nm.length];
		for(int i=0; i<tg.length; i++){
			tg[i]=new TextView(this);
			tg[i].setText(nm[i]);
			tg[i].setGravity(Gravity.CENTER);
			tg[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_highlight_square));
			tg[i].setTextSize(28);
			tg[i].setTextColor(Color.BLACK);
			plst.addView(tg[i]);
		}
		tg[0].setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					String s0,s3,s6,s7,s8,ss="";
					int u=sx.indexOf(" ("),d=sx.indexOf(")¥"),w=sx.indexOf(" / ولد في :"),t=sx.indexOf(" / توفي في :"),r=w;
					s0="-"+sx.substring(0,u);// اسم 
					if(w<0) { r=t; if(t<0) r=d; }
					s3="-"+sx.substring(u+2,r);// شهرة 
					if(w<0) s6=""; 
					else {
						r=t; if(t<0) r=d;
						u=sx.indexOf(":",w); 
						s6=sx.substring(u+1,r);// ولد في 
					}
					if(t<0) s7="";
						else {
							u=sx.indexOf(":",t); 
							s7=sx.substring(u+1,d);// توفي في 
						}
					s8=sx.substring(d+4);// تعديل 
					ss=s0+"\n"+s3+"¥¥¥¥¥¥"+s6+"¥"+s7+"¥e";
					tabhost.setCurrentTab(0);
					new async().execute(ss);
					wndup.dismiss();
				}
			});
		tg[1].setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					clp.setText(sx.replace("¥","\n"));
					wndup.dismiss();
				}
			});
	}
	// =================
	private void toAll()
	{
		kart.setText(rac.toName());
		toCmn(); toTch(); toStd();
	}
	void toCmn(){
		List<String> vs=rac.toCmn(sr_cmn.getText().toString());
		comn.removeAllViews();
		if(vs==null) return;
		cmn=new TextView[vs.size()];
		for(int i=0; i<vs.size(); i++){
			cmn[i]=new TextView(this);
			cmn[i].setText(getHtm(vs.get(i),i));
			cmn[i].setBackgroundDrawable(getResources().getDrawable(android.R.drawable.editbox_dropdown_light_frame));
			cmn[i].setTextSize(22);
			cmn[i].setTextIsSelectable(true);
			cmn[i].setTextColor(Color.BLACK);
			/*final String sx=vs.get(i);
			cmn[i].setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View vw)
					{
						pop_ser(sx);
						wndup.showAsDropDown(vw,0,-vw.getHeight(),Gravity.LEFT);
					}
				});*/
			comn.addView(cmn[i]);
		}
		nb_cmn.setText(""+vs.size());
	}
	void toTch(){
		List<String> vs=rac.toTch(sr_tch.getText().toString());
		tech.removeAllViews();
		if(vs==null) return;
		tec=new TextView[vs.size()];
		for(int i=0; i<vs.size(); i++){
			tec[i]=new TextView(this);
			tec[i].setText(getHtm(vs.get(i),i));
			tec[i].setBackgroundDrawable(getResources().getDrawable(android.R.drawable.editbox_dropdown_light_frame));
			tec[i].setTextSize(22);
			tec[i].setTextIsSelectable(true);
			tec[i].setTextColor(Color.BLACK);
			final String sx=vs.get(i);
			tec[i].setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View vw)
					{
						pop_ser(sx);
						wndup.showAsDropDown(vw,0,-vw.getHeight(),Gravity.LEFT);
					}
				});
			tech.addView(tec[i]);
		}
		nb_tch.setText(""+vs.size());
	}
	void toStd(){
		List<String> vs=rac.toStd(sr_std.getText().toString());
		stdy.removeAllViews();
		if(vs==null) return;
		std=new TextView[vs.size()];
		for(int i=0; i<vs.size(); i++){
			std[i]=new TextView(this);
			std[i].setText(getHtm(vs.get(i),i));
			std[i].setTextSize(22);
			std[i].setTextIsSelectable(true);
			std[i].setTextColor(Color.BLACK);
			std[i].setBackgroundDrawable(getResources().getDrawable(android.R.drawable.editbox_dropdown_light_frame));
			final String sx=vs.get(i);
			std[i].setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View vw)
					{
						pop_ser(sx);
						wndup.showAsDropDown(vw,0,-vw.getHeight(),Gravity.LEFT);
					}
				});
			stdy.addView(std[i]);
		}
		nb_std.setText(""+vs.size());
	}
	static Spanned getHtm(String sx,int n){
		String[] f=sx.split("¥"); // name¥say
		return Html.fromHtml(""+(n+1)+"-<font color=red>"+f[0]+" : </font>"+f[1]);
	}
	//***
	void initTab(){
		tabhost = (TabHost)findViewById(R.id.tabhost);
        tabhost.setup();
		String[] nm={"بحث","نتائج","بطاقة","تعليقات","شيوخ","تلاميذ","المحفوظ","المشاهير"};
		int[] tb={R.id.tab1,R.id.tab2,R.id.tab3,R.id.tab4,R.id.tab5,R.id.tab6,R.id.tab7,R.id.tab8};
		spec=new TabHost.TabSpec[nm.length];
		for(int i=0;i<nm.length; i++){
			spec[i] = tabhost.newTabSpec(""+i);
			spec[i] = spec[i].setContent(tb[i]);
        	spec[i] = spec[i].setIndicator(nm[i]);
        	tabhost.addTab(spec[i]);
			final int it=i;
			tabhost.getTabWidget().getChildAt(i).setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View p1)
					{
						tabhost.setCurrentTab(it);
						setTabColor();
					}
				});
		}
		addserch();
		TextView tv;
		for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {
			tv = (TextView) tabhost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
			tv.setTextSize(20);
			tv.setPadding(2,2,2,2);
		}
	}
	void addserch(){
		grp=(LinearLayout)findViewById(R.id.grp); // حاوية النتائج
		comn=(LinearLayout)findViewById(R.id.comn); // التعليقات
		tech=(LinearLayout)findViewById(R.id.tech); // الشيوخ
		stdy=(LinearLayout)findViewById(R.id.stdy); // التلاميذ
		sve=(LinearLayout)findViewById(R.id.sve);// المحفوظ 
		fms=(LinearLayout)findViewById(R.id.fms);// المشاهير 
		run=(TextView)findViewById(R.id.run); // btn بحث
		edn=(EditText)findViewById(R.id.names); // من أسمائه
		edc=(EditText)findViewById(R.id.nick); // كناه
		edf=(EditText)findViewById(R.id.fmly); // نسبته
		edg=(EditText)findViewById(R.id.grdn);// مواليه
		edt=(EditText)findViewById(R.id.techrs); //شيوخه
		eds=(EditText)findViewById(R.id.stdys); // تلاميذه
		brth=(EditText)findViewById(R.id.brth); //ولد في عام 
		deth=(EditText)findViewById(R.id.deth);// مات في عام 
		pwait=(ProgressBar)findViewById(R.id.pwait); // انتظار
		sr_cmn=(EditText)findViewById(R.id.sr_cmn); // مستطيل البحث في التعليقات
		sr_tch=(EditText)findViewById(R.id.sr_tch); // في المشايخ
		sr_std=(EditText)findViewById(R.id.sr_std); // في التلاميذ
		sr_ntg=(EditText)findViewById(R.id.sr_ntg);// في النتائج 
		sr_sve=(EditText)findViewById(R.id.sr_sve);// في المحفوظ 
		sr_fms=(EditText)findViewById(R.id.sr_fms);// في المشاهير 
		nb_cmn=(TextView)findViewById(R.id.nb_cmn); // عدد التعليقات
		nb_tch=(TextView)findViewById(R.id.nb_tch); // عدد الشيوخ
		nb_std=(TextView)findViewById(R.id.nb_std);// عدد التلاميذ 
		nb_ntg=(TextView)findViewById(R.id.nb_ntg); // عدد النتائج
		nb_sve=(TextView)findViewById(R.id.nb_sve);// عدد الرواة المحفوظين 
		nb_fms=(TextView)findViewById(R.id.nb_fms); // عدد المشاهير
		kart=(TextView)findViewById(R.id.kart);
		run.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1)
				{// name tch std nick fmly grdn
					String s0,s1,s2,s3,s4,s5,s6,s7,ss="";
					s0=edn.getText().toString();
					s1=edt.getText().toString();
					s2=eds.getText().toString();
					s3=edc.getText().toString();
					s4=edf.getText().toString();
					s5=edg.getText().toString();
					s6=brth.getText().toString();
					s7=deth.getText().toString();
					if(s0.isEmpty() && s1.isEmpty() && s2.isEmpty() && s3.isEmpty() && s4.isEmpty() && s5.isEmpty()) return;
					ss=s0+"¥"+s1+"¥"+s2+"¥"+s3+"¥"+s4+"¥"+s5+"¥"+s6+"¥"+s7+"¥e";
					new async().execute(ss);
				}
			});
		nb_ntg.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v)
				{
					
				}
			});
		sr_cmn.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) { }
				@Override
				public void onTextChanged(final CharSequence s, final int start, final int before, final int count) { }
				@Override
				public void afterTextChanged(final Editable s) { toCmn(); }
			});
		sr_tch.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) { }
				@Override
				public void onTextChanged(final CharSequence s, final int start, final int before, final int count) { }
				@Override
				public void afterTextChanged(final Editable s) { toTch(); }
			});
		sr_std.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) { }
				@Override
				public void onTextChanged(final CharSequence s, final int start, final int before, final int count) { }
				@Override
				public void afterTextChanged(final Editable s) { toStd(); }
			});
		sr_ntg.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) { }
				@Override
				public void onTextChanged(final CharSequence s, final int start, final int before, final int count) { }
				@Override
				public void afterTextChanged(final Editable s) { actvnt(); }
			});
		sr_sve.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) { }
				@Override
				public void onTextChanged(final CharSequence s, final int start, final int before, final int count) { }
				@Override
				public void afterTextChanged(final Editable s) { actvsv(); }
			});
		sr_fms.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) { }
				@Override
				public void onTextChanged(final CharSequence s, final int start, final int before, final int count) { }
				@Override
				public void afterTextChanged(final Editable s) { actvfm(); }
			});
	}
	
	public void setTabColor() {
		TextView tv;
		for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {
			tabhost.getTabWidget().getChildAt(i).setBackgroundDrawable(getResources().getDrawable(android.R.drawable.editbox_dropdown_light_frame));
			tv = (TextView) tabhost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
			tv.setTextColor(Color.BLACK);
			// unselected
		}
		int i=tabhost.getCurrentTab();
		tabhost.getTabWidget().getChildAt(i).setBackgroundDrawable(getResources().getDrawable(android.R.drawable.editbox_dropdown_dark_frame));
		tv = (TextView) tabhost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
		tv.setTextColor(Color.CYAN);
		// selected
	}
	void actvnt(){
		rsv.getsnt();
		grp.removeAllViews();
		if(rsv.lnt==null) { show("لا توجد نتائج"); return;} // hide
		if(rsv.lnt.size()>99) show("النتائج كثيرة");
		tabhost.setCurrentTab(1);
		int nbg=0;
		String sr=sr_ntg.getText().toString();
		List<Integer> m=new ArrayList<>();
		
		for(int i=0;i<rsv.lnt.size();i++){
			if(rsv.lnt.get(i).isName(sr)) { nbg++; m.add(i);}
		}
		
		gr=new TextView[nbg];
		for(int i=0;i<gr.length;i++){
			gr[i]=new TextView(this);
			gr[i].setTextSize(22);
			gr[i].setText(rsv.lnt.get(m.get(i)).toName(i));
			gr[i].setTextColor(Color.BLACK);
			gr[i].setTextIsSelectable(true);
			gr[i].setBackgroundDrawable(getResources().getDrawable(android.R.drawable.editbox_dropdown_light_frame));
			final int it=rsv.lnt.get(m.get(i)).id;
			gr[i].setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View vw)
					{
						pop_nt(it);
						wndup.showAsDropDown(vw,0,-vw.getHeight(),Gravity.LEFT);
					}
			});
			grp.addView(gr[i]);
		}
	  
		setTabColor();
		nb_ntg.setText(""+nbg);
	}
	void actvsv(){
		rsv.getssv();
		sve.removeAllViews();
		int nbg=0;
		String sr=sr_sve.getText().toString();
		List<Integer> m=new ArrayList<>();
		
		for(int i=0;i<rsv.lsv.size();i++){
			if(rsv.lsv.get(i).isName(sr)) { nbg++; m.add(i);}
		}
		
		sav=new TextView[nbg];
		for(int i=0;i<sav.length;i++){
			sav[i]=new TextView(this);
			sav[i].setTextSize(22);
			sav[i].setText(rsv.lsv.get(m.get(i)).toName(i));
			sav[i].setTextColor(Color.BLACK);
			sav[i].setTextIsSelectable(true);
			sav[i].setBackgroundDrawable(getResources().getDrawable(android.R.drawable.editbox_dropdown_light_frame));
			final int it=rsv.lsv.get(m.get(i)).id;
			sav[i].setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View vw)
					{
						pop_sv(it);
						wndup.showAsDropDown(vw,0,-vw.getHeight(),Gravity.LEFT);
					}
				});
			sve.addView(sav[i]);
		}
	  
		setTabColor();
		nb_sve.setText(""+nbg);
	}
	void actvfm(){
		rsv.getsfm();
		fms.removeAllViews();
		int nbg=0;
		String sr=sr_fms.getText().toString();
		List<Integer> m=new ArrayList<>();
		
		for(int i=0;i<rsv.lfm.size();i++){
			if(rsv.lfm.get(i).isName(sr)) { nbg++; m.add(i);}
		}
		
		fam=new TextView[nbg];
		for(int i=0;i<fam.length;i++){
			fam[i]=new TextView(this);
			fam[i].setTextSize(22);
			fam[i].setText(rsv.lfm.get(m.get(i)).toName(i));
			fam[i].setTextColor(Color.BLACK);
			fam[i].setTextIsSelectable(true);
			fam[i].setBackgroundDrawable(getResources().getDrawable(android.R.drawable.editbox_dropdown_light_frame));
			final int it=rsv.lfm.get(m.get(i)).id;
			fam[i].setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View vw)
					{
						pop_fm(it);
						wndup.showAsDropDown(vw,0,-vw.getHeight(),Gravity.LEFT);
					}
				});
			fms.addView(fam[i]);
		}
	  
		setTabColor();
		nb_fms.setText(""+nbg);
	}
	public static void hideKeyboard( Activity activity ) {
		InputMethodManager imm = (InputMethodManager)activity.getSystemService( Context.INPUT_METHOD_SERVICE );
		View f = activity.getCurrentFocus();
		if( null != f && null != f.getWindowToken() && EditText.class.isAssignableFrom( f.getClass() ) )
			imm.hideSoftInputFromWindow( f.getWindowToken(), 0 );
		else 
			activity.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );
	}
	void show(String msg){
		Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
	}
	// ==== AsyncTask ====== new async().execute("");
	public class async extends AsyncTask<String, String, Integer> 
	{
		@Override
		protected void onPreExecute() {
			run.setVisibility(View.INVISIBLE);
			pwait.setVisibility(View.VISIBLE);
			hideKeyboard(MainActivity.this);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected Integer doInBackground(String... params) {
			rsv.getGrps(params[0]);
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			actvnt();
			pwait.setVisibility(View.GONE);
			run.setVisibility(View.VISIBLE);
			super.onPostExecute(result);
		}

	}
}
