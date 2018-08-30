package util;

import java.io.File;
import java.util.ArrayList;

public class MusicList {
	
	public static ArrayList<String> getMusicList(String dir){
		ArrayList<String> musicList=new ArrayList<String>();
		MusicFilter musicFilter=new MusicFilter();
		File homeFile=new File(dir);
		if(homeFile.listFiles(musicFilter).length>0){
			for(File file:homeFile.listFiles(musicFilter)){
				musicList.add(file.getName());
			}
			return musicList;
		}else {
			return null;
		}
	}

}
