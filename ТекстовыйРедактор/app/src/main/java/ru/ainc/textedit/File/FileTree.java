package ru.ainc.textedit.File;

import java.io.*;
import java.util.*;

public class FileTree{
	
	public static String[] files(String dir){
		ArrayList<String> items = new ArrayList<String>();
		try{
			File f = new File(dir);
			File[] files = f.listFiles();
			
			for(int i = 0; i < files.length; i++){
				File file = files[i];
				if(!file.isDirectory()){
					items.add(file.getName());
				}
			}
		}catch(Exception e){}
		return items.toArray(new String[items.size()]);
	}
	
}
