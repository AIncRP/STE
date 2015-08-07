package ru.ainc.textedit;

import android.os.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.EditText;
import android.widget.Toast;
import java.io.*;
import android.app.Dialog;
import android.view.View.*;
import android.view.inputmethod.*;
import ru.ainc.textedit.File.*;
import android.support.v7.widget.*;
import android.content.*;
import android.support.design.widget.*;

public class MainActivity extends AppCompatActivity{
	
	//Объявления классов
	private EditText et; //Объявление поля ввода
	private FileIO io; //Объявление моего класса для обработки файлов
	private FloatingActionButton fab;
	
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
				
		dir = Environment.getExternalStorageDirectory().toString() + MY_DIRECTORY; //Путь к моей директории
		//Иницилизация моего класса с обработкой файлов, 
		//принимающий в конструкторе путь к моей директории
		io = new FileIO(dir); 
    }
	
	
	//Создает меню
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		
		menu.add(0, 0, 0, R.string.safe).setIcon(android.support.v7.appcompat.R.drawable.abc_ic_menu_copy_mtrl_am_alpha).setAlphabeticShortcut('s').setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		menu.add(1, 1, 0, R.string.open).setIcon(android.support.v7.appcompat.R.drawable.abc_ic_menu_paste_mtrl_am_alpha).setAlphabeticShortcut('o').setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
// 		menu.add(0, 2, 0, R.string.options);
		menu.add(0, 3, 0, R.string.info).setIcon(android.R.drawable.ic_menu_help).setAlphabeticShortcut('i');
		menu.add(0, 4, 0, R.string.exit).setIcon(android.R.drawable.ic_menu_close_clear_cancel).setAlphabeticShortcut('e');
		
		return super.onCreateOptionsMenu(menu);
	}

	//Срабатывает при нажатии пункта меню
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			case 0: 
				saveDialog(); //переходит в метод
				break;
			case 1:
				openDialog();
				break;
			case 2:
				//Фигня
				Intent intent = new Intent(MainActivity.this, Preferences.class);
				startActivity(intent);
				break;
			case 3:
				//Показывает диалог с id = 1
				showDialog(1);
				break;
			case 4:
				//Закрывает главное активити
				MainActivity.this.finish();
				break;
				
		}
		
		return super.onOptionsItemSelected(item);
	}

	//Срабатывает при нажатии на "три точки"
	@Override
	public boolean onPrepareOptionsMenu(Menu menu){		
		return super.onPrepareOptionsMenu(menu);
	}

	//Создает диалоги с id
	@Override
	protected Dialog onCreateDialog(int id){
		switch(id){
			case 1:
				AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this); //Строитель диалогов, принимающий контекст
				adb.setTitle(R.string.app_name); //Заголовок диалога
				adb.setIcon(R.drawable.ic_launcher); //Иконка диалога
				adb.setMessage("Copyright © 2015 Ainc" + "\n\n" + "STE версия 0.8.1 \n" +
														   "Список изменений: \n" +
														   "-Добавлена плавающая кнопка[Квадратная] \n" +
														   "-Локализиция на английский язык \n" +
														   "-Маленькие изменение в коде программы \n" +
														   "Ожидания в следующих версиях: \n" +
														   "-Добавление Snackbar вместо Toast. \n" +
														   "-Добавление действия к плавающей кнопке. \n" +
														   "-Изменение значков, на более подходящие. \n" +
														   "-Реализовать новый View-компонент."); //Сообщение диалога
				//Добавляет нейтральную кнопку
				adb.setNeutralButton(R.string.btn_good, new DialogInterface.OnClickListener(){ 
					@Override
					public void onClick(DialogInterface p1, int p2){	}
					});
				return adb.create(); //возвращает созданный диалог
				
		}
		return super.onCreateDialog(id);
	}
	
	//Метод, вызывающий диалог
	private void saveDialog(){
		LayoutInflater inflater = this.getLayoutInflater(); //Класс, который может парсить layout
		View root = inflater.inflate(R.layout.savedialog, null); //создаем view и сразу парсим
		
		//объявляем поле ввода
		final EditText edt = (EditText) root.findViewById(R.id.saveDialogET);
		edt.setText(curFileName);
		
		AlertDialog.Builder adb2 = new AlertDialog.Builder(MainActivity.this); //создание нового диалога, примающий контекст
		adb2.setView(root); //задаем view диалогу, который будет отображать его в себе
		adb2.setTitle(R.string.save_); //Заголовок диалого
		adb2.setCancelable(false); //Не закрывает диалог при нажатии на кнопку BACK
		//Создании кнопки, которая "позитивная"
		adb2.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){ 
			@Override
			public void onClick(DialogInterface p1, int p2){
				io.saveFile(et.getText().toString(), edt.getText().toString()); //ссылается на метод в классе FileIO, чтобы созранить файл с уникальным именем и текстом внутри него
				Toast.makeText(MainActivity.this, R.string.saved, Toast.LENGTH_SHORT).show(); //Вызывает тост
				setTitle(edt.getText().toString());
			}
		});
		adb2.setNegativeButton(R.string.nope, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface p1, int p2)	{
				
			}
		});
		adb2.show(); //показывает диалог
	}
	
	private void openDialog(){
		try{
			final String[] files = FileTree.files(dir);
			if(files.length > 0){
				position = 0;
				AlertDialog.Builder adb3 = new AlertDialog.Builder(MainActivity.this);
				adb3.setTitle(R.string.open);
				adb3.setSingleChoiceItems(files, 0, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int item){
						position = item;
					}
				});
				adb3.setPositiveButton(R.string.choose, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface p1, int p2){
						curFileName = files[position];
						try{
							et.setText(io.openFile(curFileName));
						}catch(Exception e){}
						setTitle(curFileName);
						Toast.makeText(MainActivity.this, R.string.opened, Toast.LENGTH_SHORT).show();
					}
				});
				adb3.setNegativeButton(R.string.close, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int item){
						dialog.cancel();
					}
				});
				adb3.setCancelable(false);
				adb3.show();
			}else{
				Toast.makeText(MainActivity.this, R.string.notfiles, Toast.LENGTH_SHORT).show();
			}
		}catch(Exception e){}
	}
	
}
