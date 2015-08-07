package ru.ainc.textedit.File;

import android.app.*;
import java.io.*;
import android.os.*;

public class FileIO{
	
	private static final String FILE_EXT = ".txt"; //разрешение у всех моих файлов
	private String dir; //путь к моей папке
	private File file; //файл
	
	//конструктор, который делает папку, если ее нет
	public FileIO(String dir){
		this.dir = dir; //передача текста из конструктора в класс
		File folder = new File(dir); //Создание класса File, который будет папкой
		if(!folder.exists()){ //если нет такой папки
			folder.mkdir(); //то создать
		}
	}
		
	//сохраняет файл, принимает текст файла и имя файла
	public void saveFile(String FileText, String FileName){
		//попытаться
		try{ 
			File file = new File(dir, FileName); //сделать файл
			FileOutputStream fos = new FileOutputStream(file); //с помощью потока
			fos.write(FileText.getBytes()); //записывает байты в поток
			fos.close(); //закрывает поток
		}catch(IOException e){} //если произошла ошибка, то здесь можно ее обработать
	}
	
	//открывает файл
	public String openFile(String FileName) throws IOException{
		StringBuffer buffer = new StringBuffer(); //создание буфера
		file = new File(dir, FileName);
		FileInputStream inStream = new FileInputStream(file); //читающий поток, который принимает наш файл
		//если поток иницилизирован, то запуститься этот блок
		if(inStream != null){
			InputStreamReader tmp = new InputStreamReader(inStream); //Читающий поток принимает читающий поток
			BufferedReader reader = new BufferedReader(tmp); //читающий буфер принимает читающий поток, который принимает читающий поток
			String str; //Переменная текста
			//пока и буфера все линии текста не перейдут в переменную выше это не прекратится
			while((str = reader.readLine()) != null){
				buffer.append(str + "\n"); //записывает в буфер стринглеров и делает интер
			}
		}
		inStream.close(); //закрывает поток
		return buffer.toString(); //возвращает буфер в стрингах
	}
	
	//проверяет есть ли такой файл
	public boolean isFile(){
		return file.exists(); 
	}
}
