package framework.infra.data;

import FilesManager.FileManager;

import java.io.File;
import java.nio.file.Paths;

public class ResourceGenerator {
	
	private static ResourceGenerator _instance;	
	private final String _resourcesDir = Paths.get("").toAbsolutePath().toString() + "\\resources\\";
	
	private ResourceGenerator() {}
	
	public File generateResource(FileManager.FileType fileType) {
		
		switch(fileType)		
		{
			case IMAGE:
				//TODO add image resource
			break;
			
			case AUDIO:
				//TODO add audio resource
			break;
			
			case VIDEO:
				return new File("C:\\sample_video.avi");				
		}
		return null;
	}
	
	public static ResourceGenerator getInstance() {
		
		if(_instance==null)
			_instance = new ResourceGenerator();
		return _instance;
	}
}
