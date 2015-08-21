package ru.ainc.textedit;

import android.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v7.app.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import ru.ainc.textedit.File.*;


public class MainActivity extends AppCompatActivity
{
	
	//Объявления классов
	private EditText et; //Объявление поля ввода
	private FileIO io; //Объявление моего класса для обработки файлов
	private FloatingActionButton fab;
	private FrameLayout fLayout;
	private SharedPreferences prefs;
	private View coordinator;
	
	private int position;
	private String dir = "";
	private String curFileName = ""; //Текущее имя файла
	private static final String MY_DIRECTORY = "/.ainc"; //Директория файлов
	
	//Переопределение метода OnCreate()
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		et = (EditText) findViewById(R.id.et); //Нахождение по id главное поле ввода
		fab = (FloatingActionButton) findViewById(R.id.fab);
		fLayout = (FrameLayout) findViewById(R.id.fLayout);
		
		dir = Environment.getExternalStorageDirectory().toString() + MY_DIRECTORY; //Путь к моей директории
		//Иницилизация моего класса с обработкой файлов, 
		//принимающий в конструкторе путь к моей директории
		io = new FileIO(dir); 
		
		prefs = getSharedPreferences("ru.ainc.textedit_preferences", 0);
		
		fab.setBackgroundColor(Color.argb(255, 255, 64, 129));
		fab.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				newFileDialog();
			}
		});
		
		coordinator = findViewById(R.id.coordinator);
    }
	
	
	//Создает меню
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	//Срабатывает при нажатии пункта меню
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			case R.id.item1: 
				saveDialog(); //переходит в метод
				break;
			case R.id.item2:
				openDialog();
				break;
			case R.id.item3:
				startActivity(new Intent(this, Preferences.class));
				break;
			case R.id.item4:
				dialogInfo();
				break;
			case R.id.item5:
				//Закрывает главное активити
				MainActivity.this.finish();
				break;
				
		}
		
		return super.onOptionsItemSelected(item);
	}

	private void dialogInfo(){
		AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this); //Строитель диалогов, принимающий контекст
		adb.setTitle(R.string.app_name) //Заголовок диалога
		   .setIcon(R.drawable.ic_launcher) //Иконка диалога
		   .setMessage("Copyright © 2015 Ainc" + "\n\n" + "STE версия 0.9.5 \n" +
														  "Список изменений: \n" +
														  "-Теперь можно изменить цвет фона \n" +
														  "-Создание новых файлов \n" +
														  "-Автозапоминание последнего открытого файла \n" +
														  "-Изменен цвет плавающей кнопки \n" +
													      "-Toast -> Snackbar")
			//Добавляет нейтральную кнопку
		   .setPositiveButton(R.string.btn_good, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface p1, int p2){}
		}).show(); //возвращает созданный диалог
	}
	
	//Метод, вызывающий диалог
	private void saveDialog(){
		LayoutInflater inflater = this.getLayoutInflater(); //Класс, который может парсить layout
		View root = inflater.inflate(R.layout.savedialog, null); //создаем view и сразу парсим
		
		//объявляем поле ввода
		final EditText edt = (EditText) root.findViewById(R.id.saveDialogET);
		edt.setText(curFileName);
		
		AlertDialog.Builder adb2 = new AlertDialog.Builder(MainActivity.this); //создание нового диалога, примающий контекст
		adb2.setView(root) //задаем view диалогу, который будет отображать его в себе
	    .setTitle(R.string.save_) //Заголовок диалого
		.setCancelable(false) //Не закрывает диалог при нажатии на кнопку BACK
		//Создании кнопки, которая "позитивная"
			.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){ 
			@Override
			public void onClick(DialogInterface p1, int p2){
				io.saveFile(et.getText().toString(), edt.getText().toString()); //ссылается на метод в классе FileIO, чтобы созранить файл с уникальным именем и текстом внутри него
				Snackbar.make(coordinator, R.string.saved, Snackbar.LENGTH_SHORT).show();
				setTitle(edt.getText().toString());
			}
		})
			.setNegativeButton(R.string.nope, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface p1, int p2)	{
				
			}
		}).show(); //показывает диалог
	}
	
	private void openDialog(){
		try{
			final String[] files = FileTree.files(dir);
			if(files.length > 0){
				position = 0;
				AlertDialog.Builder adb3 = new AlertDialog.Builder(MainActivity.this);
				adb3.setTitle(R.string.open)
					.setSingleChoiceItems(files, 0, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int item){
						position = item;
					}
				})
					.setPositiveButton(R.string.choose, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface p1, int p2){
						curFileName = files[position];
						try{
							et.setText(io.openFile(curFileName));
						}catch(Exception e){}
						setTitle(curFileName);
						Snackbar.make(coordinator, R.string.opened, Snackbar.LENGTH_SHORT).show();
					}
				})
					.setNegativeButton(R.string.close, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int item){
						dialog.cancel();
					}
				}).setCancelable(false).show();
			}else{
				Snackbar.make(coordinator, R.string.notfiles, Snackbar.LENGTH_SHORT).show();
			}
		}catch(Exception e){}
	}
	
	private void newFileDialog(){
		LayoutInflater inflater = this.getLayoutInflater(); 
		View root = inflater.inflate(R.layout.newdialog, null); 

		//объявляем поле ввода
		final EditText edt = (EditText) root.findViewById(R.id.newDialogET);
		
		AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this); //Строитель диалогов, принимающий контекст
		adb.setView(root)
		.setTitle(R.string.createnew) 
		.setCancelable(false)
		.setPositiveButton(R.string.create, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface p1, int p2){
				curFileName = edt.getText().toString();
				io.newFile(curFileName);
				setTitle(curFileName);
				et.setText("");
			}
		}).setNegativeButton(R.string.close,  new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface p1, int p2){	}
		}).show();
	}

	@Override
	protected void onResume(){
		super.onResume();
		settings();
		String filename = prefs.getString("filename", "");
		if(!filename.equals("")){
			setTitle(filename);
			curFileName = filename;
			try{
				et.setText(io.openFile(filename));
			}catch(Exception e){}
		}
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("filename", curFileName);
		editor.commit();
	}
	
	
	private void settings(){
		switch(prefs.getString("pcolors", "")){
			case "1":
				fLayout.setBackgroundColor(Color.argb(255, 245, 245, 245));
				break;
			case "2":
				fLayout.setBackgroundColor(Color.argb(255, 196, 20, 17));
				break;
			case "3":
				fLayout.setBackgroundColor(Color.argb(255, 173, 20, 87));
				break;
			case "4":
				fLayout.setBackgroundColor(Color.argb(255, 106, 27, 154));
				break;
			case "5":
				fLayout.setBackgroundColor(Color.argb(255, 69, 39, 160));
				break;
			case "6":
				fLayout.setBackgroundColor(Color.argb(255, 40, 53, 147));
				break;
			case "7":
				fLayout.setBackgroundColor(Color.argb(255, 59, 80, 206));
				break;
			case "8":
				fLayout.setBackgroundColor(Color.argb(255, 2, 119, 189));
				break;
			case "9":
				fLayout.setBackgroundColor(Color.argb(255, 0, 131, 143));
				break;
			case "10":
				fLayout.setBackgroundColor(Color.argb(255, 0, 105, 92));
				break;
			case "11":
				fLayout.setBackgroundColor(Color.argb(255, 5, 111, 0));
				break;
			case "12":
				fLayout.setBackgroundColor(Color.argb(255, 85, 139, 47));
				break;
			case "13":
				fLayout.setBackgroundColor(Color.argb(255, 158, 157, 36));
				break;
			case "14":
				fLayout.setBackgroundColor(Color.argb(255, 249, 168, 37));
				break;
			case "15":
				fLayout.setBackgroundColor(Color.argb(255, 255, 143, 0));
				break;
			case "16":
				fLayout.setBackgroundColor(Color.argb(255, 239, 108, 0));
				break;
			case "17":
				fLayout.setBackgroundColor(Color.argb(255, 216, 67, 21));
				break;
			case "18":
				fLayout.setBackgroundColor(Color.argb(255, 78, 52, 46));
				break;
			case "19":
				fLayout.setBackgroundColor(Color.argb(255, 66, 66, 66));
				break;
			case "20":
				fLayout.setBackgroundColor(Color.argb(255, 55, 71, 79));
				break;
		}
		
	}

	
}
